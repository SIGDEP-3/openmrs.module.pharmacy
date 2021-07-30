package org.openmrs.module.pharmacy.forms.transfer.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.transfer.TransferAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ProductAttributeFluxFormValidation;
import org.springframework.validation.Errors;

@Handler(supports = {TransferAttributeFluxForm.class}, order = 50)
public class ProductTransferAttributeFluxFormValidation extends ProductAttributeFluxFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(TransferAttributeFluxForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TransferAttributeFluxForm form = (TransferAttributeFluxForm) o;

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
