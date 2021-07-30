package org.openmrs.module.pharmacy.forms.reception.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.entities.ProductReception;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.forms.reception.ReceptionAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ProductAttributeFluxFormValidation;
import org.springframework.validation.Errors;

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
            // ValidationUtils.rejectIfEmpty(errors, "quantityToDeliver", null, "La quantié à livrer est requise");

            if (form.getProductOperationId() != null) {
                ProductReception reception = (ProductReception) service().getOneProductOperationById(form.getProductOperationId());
                if (reception.getIncidence().equals(Incidence.POSITIVE)) {
                    if (form.getQuantityToDeliver() == null) {
                        errors.rejectValue("quantityToDeliver", null, "La quantité livrée est requise");
                    }
                }
            }
            if (form.getQuantityToDeliver() != null && form.getQuantityToDeliver() == 0) {
                errors.rejectValue("quantityToDeliver", null, "La quantité livrée ne peut pas être égale à 0");
            }

        }

    }
}
