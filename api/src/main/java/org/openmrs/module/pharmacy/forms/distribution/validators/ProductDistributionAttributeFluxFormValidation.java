package org.openmrs.module.pharmacy.forms.distribution.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.distribution.DistributionAttributeFluxForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Handler(supports = {DistributionAttributeFluxForm.class}, order = 50)
public class ProductDistributionAttributeFluxFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(DistributionAttributeFluxForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DistributionAttributeFluxForm form = (DistributionAttributeFluxForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            // ValidationUtils.rejectIfEmpty(errors, "quantityToDeliver", null, "La quantié à livrer est requise");

//            if (form.getProductOperationId() != null) {
//                ProductReport reception = (ProductReport) service().getOneProductOperationById(form.getProductOperationId());
//                if (reception.getIncidence().equals(Incidence.POSITIVE)) {
//                    if (form.getQuantityToDeliver() == null) {
//                        errors.rejectValue("quantityToDeliver", null, "La quantité livrée est requise");
//                    }
//                }
//            }
//            if (form.getQuantityToDeliver() != null && form.getQuantityToDeliver() == 0) {
//                errors.rejectValue("quantityToDeliver", null, "La quantité livrée ne peut pas être égale à 0");
//            }

        }

    }
}
