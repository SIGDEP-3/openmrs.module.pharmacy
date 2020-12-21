package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.ProductTransfer;
import org.openmrs.module.pharmacy.forms.ProductTransferForm;
import org.openmrs.module.pharmacy.forms.ProductOperationForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {ProductTransferForm.class}, order = 50)
public class ProductTransferFormValidation extends ProductOperationFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductTransferForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductTransferForm form = (ProductTransferForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "exchangeLocationId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "transferType", null, "Ce champ est requis");
        }
    }
}
