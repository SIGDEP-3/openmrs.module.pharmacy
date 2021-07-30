package org.openmrs.module.pharmacy.forms.reports.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.reports.ReportAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ProductAttributeFluxFormValidation;
import org.springframework.validation.Errors;

@Handler(supports = {ReportAttributeFluxForm.class}, order = 50)
public class ProductReportAttributeFluxFormValidation extends ProductAttributeFluxFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ReportAttributeFluxForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ReportAttributeFluxForm form = (ReportAttributeFluxForm) o;

        super.validate(form, errors);

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
