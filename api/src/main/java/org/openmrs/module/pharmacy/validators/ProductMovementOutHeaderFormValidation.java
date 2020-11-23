package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductMovementOut;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductMovementService;
import org.openmrs.module.pharmacy.forms.ProductMovementOutForm;
import org.openmrs.module.pharmacy.forms.ProductReceptionForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Handler(supports = {ProductMovementOutForm.class}, order = 50)
public class ProductMovementOutHeaderFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductMovementOutForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductMovementOutForm form = (ProductMovementOutForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "productProgramId", null, "Ce champ est requis");
//            ValidationUtils.rejectIfEmpty(errors, "productSupplierId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "receptionQuantityMode", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "operationDate", null, "Ce champ est requis");

            if (form.getOperationDate() != null) {
                List<ProductMovementOut> productMovementOuts =  Context.getService(ProductMovementService.class).getAllProductMovementOut(
                        Context.getLocationService().getDefaultLocation(), false, form.getOperationDate(), form.getOperationDate()
                );
                for (ProductMovementOut movementOut : productMovementOuts) {
                    if (!movementOut.getProductOperationId().equals(form.getProductOperationId())) {
                        if (movementOut.getOperationDate().equals(form.getOperationDate()) &&
                                movementOut.getProductProgram().getProductProgramId().equals(form.getProductProgramId())) {
                            errors.rejectValue("operationDate", "Un produit possédant ce code existe déjà !");
                            break;
                        }
                    }
                }
            }
        }


    }
}
