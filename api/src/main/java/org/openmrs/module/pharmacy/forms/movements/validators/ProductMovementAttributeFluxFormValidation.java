package org.openmrs.module.pharmacy.forms.movements.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.movements.MovementAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ProductAttributeFluxFormValidation;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {MovementAttributeFluxForm.class}, order = 50)
public class ProductMovementAttributeFluxFormValidation extends ProductAttributeFluxFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(MovementAttributeFluxForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MovementAttributeFluxForm form = (MovementAttributeFluxForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "quantity", null, "La quantié est requise");

            if (form.getQuantity() != null && form.getQuantity() == 0) {
                errors.rejectValue("quantityToDeliver", null, "La quantité livrée ne peut pas être égale à 0");
            }
        }

    }
}
