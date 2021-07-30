package org.openmrs.module.pharmacy.forms.inventory.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.inventory.InventoryAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ProductAttributeFluxFormValidation;
import org.springframework.validation.Errors;

@Handler(supports = {InventoryAttributeFluxForm.class}, order = 50)
public class ProductInventoryAttributeFluxFormValidation extends ProductAttributeFluxFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(InventoryAttributeFluxForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        InventoryAttributeFluxForm form = (InventoryAttributeFluxForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        }
//        else {
////            ValidationUtils.rejectIfEmpty(errors, "quantityToDeliver", null, "La quantié à livrer est requise");
//
////            if (form.getQuantityToDeliver() != null && form.getQuantityToDeliver() == 0) {
////                errors.rejectValue("quantityToDeliver", null, "La quantité livrée ne peut pas être égale à 0");
////            }
//        }

    }
}
