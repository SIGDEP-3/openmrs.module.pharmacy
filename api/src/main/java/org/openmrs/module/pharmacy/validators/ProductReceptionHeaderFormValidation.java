package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.forms.ProductForm;
import org.openmrs.module.pharmacy.forms.ReceptionHeaderForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Handler(supports = {ReceptionHeaderForm.class}, order = 50)
public class ProductReceptionHeaderFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ReceptionHeaderForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ReceptionHeaderForm form = (ReceptionHeaderForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "productProgramId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "productSupplierId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "receptionQuantityMode", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "operationDate", null, "Ce champ est requis");

            if (form.getOperationDate() != null) {
                List<ProductReception> productReceptions =  Context.getService(PharmacyService.class).getAllProductReceptions(
                        Context.getLocationService().getDefaultLocation(), false, form.getOperationDate(), form.getOperationDate()
                );
                for (ProductReception reception : productReceptions) {
                    if (!reception.getProductOperationId().equals(form.getProductOperationId())) {
                        if (reception.getOperationDate().equals(form.getOperationDate()) &&
                                reception.getProductProgram().getProductProgramId().equals(form.getProductProgramId())) {
                            errors.rejectValue("operationDate", "Un produit possédant ce code existe déjà !");
                            break;
                        }
                    }
                }
            }
        }


    }
}
