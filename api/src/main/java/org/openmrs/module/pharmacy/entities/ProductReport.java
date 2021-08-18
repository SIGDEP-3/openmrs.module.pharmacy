package org.openmrs.module.pharmacy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;
import org.openmrs.module.pharmacy.enumerations.ReportType;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductReport")
//@DiscriminatorValue("ProductReport")
@Table(name = "pharmacy_product_report")
public class ProductReport extends ProductOperation {
    private static final long serialVersionUID = 1L;

    @Column(name = "report_type", nullable = false)
    private ReportType reportType;
    @Column(name = "report_period", nullable = false)
    private String reportPeriod;
    @Column(name = "report_info", nullable = false)
    private String reportInfo;
    @ManyToOne
    @JoinColumn(name = "report_location_id")
    private Location reportLocation;
    @Temporal(TemporalType.DATE)
    @Column(name = "report_treatment_date")
    private Date treatmentDate;
    @Column(name = "is_urgent")
    private Boolean isUrgent = false;
    @ManyToOne
    @JoinColumn(name = "child_location_report_id")
    private ProductReport childLocationReport;

    public ProductReport() {
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

    public Location getReportLocation() {
        return reportLocation;
    }

    public void setReportLocation(Location reportLocation) {
        this.reportLocation = reportLocation;
    }

    public Date getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(Date treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public ProductReport getChildLocationReport() {
        return childLocationReport;
    }

    public void setChildLocationReport(ProductReport childLocationReport) {
        this.childLocationReport = childLocationReport;
    }

    public Boolean getUrgent() {
        return isUrgent;
    }

    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }
}
