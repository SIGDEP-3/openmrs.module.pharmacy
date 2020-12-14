package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductDispensation;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.forms.ProductDispensationForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {ProductDispensationForm.class}, order = 50)
public class ProductDispensationFormValidation extends ProductOperationFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductDispensationForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductDispensationForm form = (ProductDispensationForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
//            ValidationUtils.rejectIfEmpty(errors, "productSupplierId", null, "Ce champ est requis");
//            ValidationUtils.rejectIfEmpty(errors, "receptionQuantityMode", null, "Ce champ est requis");
//            ValidationUtils.rejectIfEmpty(errors, "operationNumber", null, "Ce champ est requis");

//            if (form.getOperationNumber() != null && !form.getOperationNumber().isEmpty()) {
//                ProductDispensation reception = (ProductDispensation) service().getOneProductOperationByOperationNumber(form.getOperationNumber());
//                if (reception != null) {
//                    if (form.getProductSupplierId() != null) {
//                        if (!reception.getProductOperationId().equals(form.getProductOperationId())) {
//                            errors.rejectValue("operationNumber", null, "Une réception avec ce BL existe déjà ");
//                        }
//                    }
//                }
//            }



//            ProductDispensation dispensation = service().getOneProductOperationByOperationDateAndProductProgram(
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

        }
    }

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }
}
