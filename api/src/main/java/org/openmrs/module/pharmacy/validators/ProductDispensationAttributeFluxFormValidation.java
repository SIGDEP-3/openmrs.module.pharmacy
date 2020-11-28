package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.DispensationAttributeFluxForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {DispensationAttributeFluxForm.class}, order = 50)
public class ProductDispensationAttributeFluxFormValidation extends ProductAttributeFluxFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(DispensationAttributeFluxForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DispensationAttributeFluxForm form = (DispensationAttributeFluxForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
//            ValidationUtils.rejectIfEmpty(errors, "quantityToDeliver", null, "La quantié à livrer est requise");

//            if (form.getQuantityToDeliver() != null && form.getQuantityToDeliver() == 0) {
//                errors.rejectValue("quantityToDeliver", null, "La quantité livrée ne peut pas être égale à 0");
//            }
        }

    }
}
