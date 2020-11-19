package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.ReceptionAttributeFluxForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {ReceptionAttributeFluxForm.class}, order = 50)
public class ProductReceptionAttributeFluxFormValidation extends ProductAttributeFluxFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ReceptionAttributeFluxForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ReceptionAttributeFluxForm form = (ReceptionAttributeFluxForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "quantityToDeliver", null, "La quantié à livrer est requise");

            if (form.getQuantityToDeliver() != null && form.getQuantityToDeliver() == 0) {
                errors.rejectValue("quantityToDeliver", null, "La quantité livrée ne peut pas être égale à 0");
            }
        }

    }
}
