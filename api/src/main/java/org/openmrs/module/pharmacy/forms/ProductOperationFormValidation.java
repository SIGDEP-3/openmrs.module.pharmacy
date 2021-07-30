package org.openmrs.module.pharmacy.forms;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.ProductInventory;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductInventoryService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.enumerations.InventoryType;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.forms.ProductOperationForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.notification.Alert;
import org.openmrs.notification.AlertRecipient;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Handler(supports = {ProductOperationForm.class}, order = 50)
public class ProductOperationFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductOperationForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductOperationForm form = (ProductOperationForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "productProgramId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "operationDate", null, "Ce champ est requis");

            if (form.getOperationDate() != null) {
                Date now = Calendar.getInstance().getTime();
                if (form.getOperationDate().after(now)) {
                    //System.out.println("-------------------------------------- : Now () = " + now);
                    errors.rejectValue("operationDate", null, "Impossible de créer une opération avec une date future !");
                } else {
//                    ProductOperation operation = service().getOneProductOperationByOperationDateAndProductProgram(
//                            form.getOperationDate(),
//                            programService().getOneProductProgramById(form.getProductProgramId()),
//                            OperationUtils.getUserLocation(), false
//                    );
//
//                    if (operation != null) {
//                        if (!operation.getProductOperationId().equals(form.getProductOperationId())) {
//                            if (operation.getOperationDate().equals(form.getOperationDate()) &&
//                                    operation.getProductProgram().getProductProgramId().equals(form.getProductProgramId())) {
//                                errors.rejectValue("operationDate", null, "Un operatiob à cette date existe déjà !");
//                            }
//                        }
//                    }

                    lastInventoryCheck(form, errors);
                }
            }
        }
    }

    protected PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    protected ProductProgramService programService() {
        return Context.getService(ProductProgramService.class);
    }

    protected ProductInventoryService inventoryService() {
        return Context.getService(ProductInventoryService.class);
    }

    protected void lastInventoryCheck(ProductOperationForm form, Errors errors) {
        if (form.getProductProgramId()!= null) {
            ProductProgram productProgram = programService().getOneProductProgramById(form.getProductProgramId());
            if (productProgram != null) {
                ProductInventory productInventory = inventoryService().getLastProductInventory(OperationUtils.getUserLocation(), productProgram, InventoryType.TOTAL);
                if (productInventory != null) {
                    if (productInventory.getOperationStatus().equals(OperationStatus.NOT_COMPLETED) ||
                            productInventory.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
                        if (form.getOperationDate().after(productInventory.getOperationDate()) &&
                                form.getOperationDate().before(productInventory.getInventoryStartDate())) {
                            errors.rejectValue("operationDate", null, "Vous avez renseigné une date invalide pour cette opération");
                            createAlert("Un inventaire est en cours, veuillez valider cet inventaire avant toute opération après ce dernier !");
                        }
                    } else if (productInventory.getOperationStatus().equals(OperationStatus.VALIDATED)) {
                        if (form.getOperationDate().before(productInventory.getOperationDate())) {
                            errors.rejectValue("operationDate", null, "Vous avez renseigné une date invalide pour cette opération");
                            createAlert("Une opération précédant la date du dernier inventaire ne peut être réalisé !");
                        }
                    }
                }
                else {
                    errors.rejectValue("productProgramId", null, "Premier Inventaire complet non réalisé pour ce programme");
                    createAlert("Vous devez avant toute opération réaliser le premier inventaire complet de votre centre !");
                }
            }
        }
    }

    protected void createAlert(String message) {
        Alert alert = new Alert();
        alert.setText(message);
        Set<AlertRecipient> recipients = new HashSet<AlertRecipient>();
        AlertRecipient recipient = new AlertRecipient();

        recipient.setRecipient(Context.getUserService().getUserByUsername("admin"));
        recipients.add(recipient);

        if (!Context.getAuthenticatedUser().getUsername().equals("admin")) {
            AlertRecipient recipientOther = new AlertRecipient();
            recipientOther.setRecipient(Context.getAuthenticatedUser());
            recipients.add(recipientOther);
        }

        alert.setRecipients(recipients);
        Context.getAlertService().saveAlert(alert);
    }
}
