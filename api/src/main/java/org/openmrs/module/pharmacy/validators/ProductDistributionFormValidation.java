package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductReport;
import org.openmrs.module.pharmacy.api.ProductReportService;
import org.openmrs.module.pharmacy.forms.ProductDistributionForm;
import org.openmrs.module.pharmacy.forms.ProductOperationForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {ProductDistributionForm.class}, order = 50)
public class ProductDistributionFormValidation extends ProductOperationFormValidation  {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductDistributionForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductDistributionForm form = (ProductDistributionForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "reportPeriod", null, "Ce champ est requis");
//            ValidationUtils.rejectIfEmpty(errors, "receptionQuantityMode", null, "Ce champ est requis");
//            ValidationUtils.rejectIfEmpty(errors, "operationNumber", null, "Ce champ est requis");
            if (!form.getUrgent()) {
                ProductReport otherReport = reportService().getOneProductReportByReportPeriodAndProgram(
                        form.getReportPeriod(),
                        programService().getOneProductProgramById(form.getProductProgramId()),
                        OperationUtils.getUserLocation(),
                        false
                );
                if (form.getReportPeriod() != null) {
                    if (otherReport != null &&
                            (form.getProductOperationId() == null || (!otherReport.getProductOperationId().equals(form.getProductOperationId())))) {
                        if (!otherReport.getUrgent()) {
                            errors.rejectValue("reportPeriod", null, "Un rapport de cette période existe déjà !");
                        }
                    }
                }
            }
            if (form.getReportPeriod() != null && !form.getReportPeriod().isEmpty()) {
                ProductReport existingDistribution = reportService().getPeriodTreatedProductReportsByReportPeriodAndLocation(
                        form.getReportPeriod(),
                        programService().getOneProductProgramById(form.getProductProgramId()),
                        Context.getLocationService().getLocation(form.getReportLocationId()),
                        false
                );

                if (existingDistribution != null) {
                    if (form.getProductOperationId() == null || !form.getProductOperationId().equals(existingDistribution.getProductOperationId())) {
                        errors.rejectValue("reportPeriod", null, "Une distribution pour ce Site ou PPS à cette période existe déjà !");
                    }
                }
            }
        }
    }

    @Override
    protected void lastInventoryCheck(ProductOperationForm form, Errors errors) {

    }
    private ProductReportService reportService() {
        return Context.getService(ProductReportService.class);
    }

}
