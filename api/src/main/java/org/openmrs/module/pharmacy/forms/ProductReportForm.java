package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.ProductReport;
import org.openmrs.module.pharmacy.api.ProductReportService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.ReportType;

import java.util.Date;

public class ProductReportForm extends ProductOperationForm {
    private ReportType reportType;
    private String reportPeriod;
    private String reportInfo;
    private Date treatmentDate;
    private Boolean isUrgent;

    public ProductReportForm() {
        super();
        setIncidence(Incidence.NEGATIVE);
    }

    public void setProductReport(ProductReport productReport) {
        super.setProductOperation(productReport);
        setReportType(productReport.getReportType());
        setReportPeriod(productReport.getReportPeriod());
        setReportInfo(productReport.getReportInfo());
        if (productReport.getTreatmentDate() != null) {
            setTreatmentDate(productReport.getTreatmentDate());
        }
        if (productReport.getReportInfo() != null) {
            setReportInfo(productReport.getReportInfo());
        }
    }

    public ProductReport getProductReport() {
        ProductReport productReport = (ProductReport) super.getProductOperation(new ProductReport());
        productReport.setReportType(getReportType());
        productReport.setReportPeriod(getReportPeriod());
        productReport.setReportInfo(getReportInfo());
        if (getTreatmentDate() != null) {
            productReport.setTreatmentDate(getTreatmentDate());
        }
        if (getReportInfo() != null) {
            productReport.setReportInfo(getReportInfo());
        }
        return productReport;
    }

    private ProductReportService reportService() {
        return Context.getService(ProductReportService.class);
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(String reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public String getReportInfo() {
        return reportInfo;
    }

    public void setReportInfo(String reportInfo) {
        this.reportInfo = reportInfo;
    }

    public void setTreatmentDate(Date treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public Boolean getUrgent() {
        return isUrgent;
    }

    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }

    public Date getTreatmentDate() {
        return treatmentDate;
    }
}
