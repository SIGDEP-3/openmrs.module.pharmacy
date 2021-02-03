package org.openmrs.module.pharmacy.forms;

import org.apache.commons.lang.time.DateUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductReport;
import org.openmrs.module.pharmacy.api.ProductReportService;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.enumerations.ReportType;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.*;

public class ProductReportForm extends ProductOperationForm {
    private ReportType reportType;
    private String reportPeriod;
    private String reportInfo;
    private Date treatmentDate;
    private Boolean isUrgent;
    private Set<Integer> productIds = new HashSet<Integer>();

    public ProductReportForm() {
        super();
        setOperationDate(DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH));
//        setIncidence(Incidence.NEGATIVE);
    }

    public void setProductReport(ProductReport productReport) {
        super.setProductOperation(productReport);
        setReportType(productReport.getReportType());
        setReportPeriod(productReport.getReportPeriod());
        setReportInfo(productReport.getReportInfo());
        setUrgent(productReport.getUrgent());
        setIncidence(productReport.getIncidence());
        if (productReport.getTreatmentDate() != null) {
            setTreatmentDate(productReport.getTreatmentDate());
        }
        if (productReport.getReportInfo() != null) {
            setReportInfo(productReport.getReportInfo());
        }
        if (productReport.getUrgent()) {
            if (productReport.getReportInfo() != null) {
                for (String code : productReport.getReportInfo().split(",")) {
                    getProductIds().add(productService().getOneProductByCode(code).getProductId());
                }
            }
        }
    }

    public ProductReport getProductReport() {
        ProductReport productReport = (ProductReport) super.getProductOperation(new ProductReport());
        productReport.setReportType(getReportType());
        productReport.setReportPeriod(getReportPeriod());
        productReport.setReportInfo(getReportInfo());
        productReport.setUrgent(getUrgent());
        if (getTreatmentDate() != null) {
            productReport.setTreatmentDate(getTreatmentDate());
        }
        if (getReportInfo() != null) {
            productReport.setReportInfo(getReportInfo());
        }
        if (getUrgent()) {
            List<String> codes = new ArrayList<>();
            for (Integer id : getProductIds()) {
                codes.add(productService().getOneProductById(id).getCode());
            }
            productReport.setReportInfo(OperationUtils.join(",", codes));
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

    public Set<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(Set<Integer> productIds) {
        this.productIds = productIds;
    }

    private ProductService productService() {
        return Context.getService(ProductService.class);
    }
}
