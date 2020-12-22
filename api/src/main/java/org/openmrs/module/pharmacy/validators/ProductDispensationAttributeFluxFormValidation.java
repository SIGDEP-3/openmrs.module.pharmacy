package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.DispensationAttributeFluxForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {DispensationAttributeFluxForm.class}, order = 50)
public class ProductDispensationAttributeFluxFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(DispensationAttributeFluxForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DispensationAttributeFluxForm form = (DispensationAttributeFluxForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
//            ValidationUtils.rejectIfEmpty(errors, "selectedProductId", null, "Le produit doit être sélectionné SVP");
            ValidationUtils.rejectIfEmpty(errors, "dispensingQuantity", null, "La quantité dispensée est requise");
            ValidationUtils.rejectIfEmpty(errors, "requestedQuantity", null, "La quantité demandée est requise");

            if (form.getProductId() == null && form.getSelectedProductId() == null) {
                errors.rejectValue("selectedProductId", null, "Le produit doit être sélectionné SVP");
            }
//            if (form.getQuantityToDeliver() != null && form.getQuantityToDeliver() == 0) {
//                errors.rejectValue("quantityToDeliver", null, "La quantité livrée ne peut pas être égale à 0");
//            }
        }

    }
}
