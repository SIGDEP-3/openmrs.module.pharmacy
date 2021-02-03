package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductMovementEntry;
import org.openmrs.module.pharmacy.api.ProductMovementService;
import org.openmrs.module.pharmacy.forms.ProductMovementForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Handler(supports = {ProductMovementForm.class}, order = 50)
public class ProductMovementFormValidation extends ProductOperationFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductMovementForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductMovementForm form = (ProductMovementForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            super.validate(form, errors);

            ValidationUtils.rejectIfEmpty(errors, "productProgramId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "operationDate", null, "Ce champ est requis");

//            if (form.getOperationDate() != null) {
//                List<ProductMovementEntry> productMovement =  Context.getService(ProductMovementService.class).getAllProductMovementEntry(
//                        Context.getLocationService().getDefaultLocation(), false, form.getOperationDate(), form.getOperationDate()
//                );
//                for (ProductMovementEntry movement : productMovement) {
//                    if (!movement.getProductOperationId().equals(form.getProductOperationId())) {
//                        if (movement.getOperationDate().equals(form.getOperationDate()) &&
//                                movement.getProductProgram().getProductProgramId().equals(form.getProductProgramId())) {
//                            errors.rejectValue("operationDate", "Un produit possédant ce code existe déjà !");
//                            break;
//                        }
//                    }
//                }
//            }
        }


    }
}
