package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductReport;
import org.openmrs.module.pharmacy.api.ProductReportService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.ReportType;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.util.OpenmrsUtil;

import java.util.UUID;

public class ProductDistributionForm extends ProductReportForm {
    private Integer reportLocationId;
    private Integer childLocationReportId;

    public ProductDistributionForm() {
        super();
        setIncidence(Incidence.NEGATIVE);
        setReportType(ReportType.NOT_CLIENT_REPORT);
    }

    public Integer getReportLocationId() {
        return reportLocationId;
    }

    public void setReportLocationId(Integer reportLocationId) {
        this.reportLocationId = reportLocationId;
    }

    public Integer getChildLocationReportId() {
        return childLocationReportId;
    }

    public void setChildLocationReportId(Integer childLocationReportId) {
        this.childLocationReportId = childLocationReportId;
    }

    public void setProductReport(ProductReport productReport) {
        super.setProductReport(productReport);
        setReportLocationId(productReport.getReportLocation().getLocationId());
        if (productReport.getChildLocationReport() != null) {
            setChildLocationReportId(productReport.getChildLocationReport().getProductOperationId());
        }
    }

    public ProductReport getChildReport() {
        ProductReport childReport = new ProductReport();
        if (getChildLocationReportId() != null ) {
            childReport = service().getOneProductReportById(getChildLocationReportId());
        } else {
            childReport.setIncidence(Incidence.NONE);
            childReport.setOperationStatus(OperationStatus.NOT_COMPLETED);
            childReport.setLocation(Context.getLocationService().getLocation(getReportLocationId()));
            childReport.setReportPeriod(getReportPeriod());
            childReport.setOperationDate(getOperationDate());
            childReport.setReportType(getReportType());
            childReport.setProductProgram(programService().getOneProductProgramById(getProductProgramId()));
            childReport.setUrgent(getUrgent());
            childReport.setUuid(UUID.randomUUID().toString());
        }
        return childReport;
    }

    public ProductReport getProductReport() {
        ProductReport productReport = super.getProductReport();
        productReport.setReportType(getReportType());
        if (productReport.getChildLocationReport() != null) {
            productReport.setChildLocationReport(service().getOneProductReportById(getChildLocationReportId()));
        }
        productReport.setReportLocation(Context.getLocationService().getLocation(getReportLocationId()));
        return productReport;
    }

    public ProductReportService service() {
        return Context.getService(ProductReportService.class);
    }

    public ProductProgramService programService() {
        return Context.getService(ProductProgramService.class);
    }
}
