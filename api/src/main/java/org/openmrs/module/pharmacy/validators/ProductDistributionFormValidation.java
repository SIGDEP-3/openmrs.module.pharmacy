package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.ProductDistributionForm;
import org.openmrs.module.pharmacy.forms.ProductReportForm;
import org.springframework.validation.Errors;

@Handler(supports = {ProductDistributionForm.class}, order = 50)
public class ProductDistributionFormValidation extends ProductReportFormValidation  {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductDistributionForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductDistributionForm form = (ProductDistributionForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
//            ValidationUtils.rejectIfEmpty(errors, "productSupplierId", null, "Ce champ est requis");
//            ValidationUtils.rejectIfEmpty(errors, "receptionQuantityMode", null, "Ce champ est requis");
//            ValidationUtils.rejectIfEmpty(errors, "operationNumber", null, "Ce champ est requis");

//            if (form.getOperationNumber() != null && !form.getOperationNumber().isEmpty()) {
//                ProductReport reception = (ProductReport) service().getOneProductOperationByOperationNumber(form.getOperationNumber(), form.getIncidence());
//                if (reception != null) {
//                    if (form.getProductSupplierId() != null) {
//                        if (!reception.getProductOperationId().equals(form.getProductOperationId())) {
//                            errors.rejectValue("operationNumber", null, "Une réception avec ce BL existe déjà ");
//                        }
//                    }
//                }
//            }

//            if (form.getOperationDate() != null) {
//                ProductProgram productProgram = programService().getOneProductProgramById(form.getProductProgramId());
//                if (productProgram != null) {
//                    ProductInventory productInventory = inventoryService().getLastProductInventory(
//                            OperationUtils.getUserLocation(), productProgram);
//                    if (productInventory == null) {
//                        createAlert("Vous devez avant toute opération réaliser le premier inventaire complet de votre centre !");
//                        errors.rejectValue("productProgramId", null, "Premier Inventaire complet non réalisé");
//                    }
//                }
//            }
        }
    }

}
