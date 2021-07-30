package org.openmrs.module.pharmacy.forms.reports.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.reports.ReportEntryAttributeFluxForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Handler(supports = {ReportEntryAttributeFluxForm.class}, order = 50)
public class ProductReportEntryAttributeFluxFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ReportEntryAttributeFluxForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ReportEntryAttributeFluxForm form = (ReportEntryAttributeFluxForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            // ValidationUtils.rejectIfEmpty(errors, "quantityToDeliver", null, "La quantié à livrer est requise");
        }
    }
}
