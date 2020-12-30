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
    private Integer reportLocationId;
    private Date treatmentDate;
    private Boolean isUrgent;
    private Integer childLocationReportId;

    public ProductReportForm() {
        super();
        setIncidence(Incidence.NEGATIVE);
    }

    public void setProductReport(ProductReport productReport) {
        super.setProductOperation(productReport);
        setReportType(productReport.getReportType());
        setReportPeriod(productReport.getReportPeriod());
        setReportInfo(productReport.getReportInfo());
        if (productReport.getChildLocationReport() != null) {
            setChildLocationReportId(productReport.getChildLocationReport().getProductOperationId());
        }
        if (productReport.getReportInfo() != null) {
            setReportInfo(productReport.getReportInfo());
        }
        if (productReport.getReportLocation() != null) {
            setReportLocationId(productReport.getReportLocation().getLocationId());
        }
    }

    public ProductReport getProductReport() {
        ProductReport productReport = (ProductReport) super.getProductOperation(new ProductReception());
        productReport.setReportType(getReportType());
        productReport.setReportPeriod(getReportPeriod());
        productReport.setReportInfo(getReportInfo());
        if (getChildLocationReportId() != null) {
            productReport.setChildLocationReport(reportService().getOneProductReportById(getChildLocationReportId()));
        }
        if (getReportInfo() != null) {
            productReport.setReportInfo(getReportInfo());
        }
        if (getReportLocationId() != null) {
            productReport.setReportLocation(Context.getLocationService().getLocation(getReportLocationId()));
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

    public Integer getReportLocationId() {
        return reportLocationId;
    }

    public void setReportLocationId(Integer reportLocationId) {
        this.reportLocationId = reportLocationId;
    }

    public Date getTreatmentDate() {
        return treatmentDate;
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

    public Integer getChildLocationReportId() {
        return childLocationReportId;
    }

    public void setChildLocationReportId(Integer childLocationReportId) {
        this.childLocationReportId = childLocationReportId;
    }
}
