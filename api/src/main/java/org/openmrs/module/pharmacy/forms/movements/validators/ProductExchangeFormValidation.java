package org.openmrs.module.pharmacy.forms.movements.validators;
import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.movements.ExchangeEntityForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {ExchangeEntityForm.class}, order = 50)
public class ProductExchangeFormValidation implements Validator {
    @Override
    public boolean supports(Class aClass) {
        return aClass.equals(ExchangeEntityForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ExchangeEntityForm exchange = (ExchangeEntityForm) o;

        if (exchange == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "name", null, "Ce champ est requis");
//            ValidationUtils.rejectIfEmpty(errors, "locationId", null, "Ce champ est requis");

        }
    }
}
