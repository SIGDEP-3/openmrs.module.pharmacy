package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.forms.ProductReceptionForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {ProductReceptionForm.class}, order = 50)
public class ProductReceptionFormValidation extends ProductOperationFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductReceptionForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductReceptionForm form = (ProductReceptionForm) o;


        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {

            super.validate(form, errors);

            ValidationUtils.rejectIfEmpty(errors, "productSupplierId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "receptionQuantityMode", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "operationNumber", null, "Ce champ est requis");

            if (form.getOperationNumber() != null && !form.getOperationNumber().isEmpty()) {
                ProductReception reception = (ProductReception) service().getOneProductOperationByOperationNumber(form.getOperationNumber(), form.getIncidence());
                if (reception != null) {
                    if (form.getProductSupplierId() != null) {
                        if (!reception.getProductOperationId().equals(form.getProductOperationId())) {
                            errors.rejectValue("operationNumber", null, "Une réception avec ce BL existe déjà ");
                        }
                    }
                }
            }

        }
    }
}
