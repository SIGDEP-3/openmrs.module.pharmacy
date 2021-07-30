package org.openmrs.module.pharmacy.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsumptionReportDTO {
    private String productProgramName;
    private Date startDate;
    private Date endDate;
    private boolean byWholesaleUnit;
    private boolean byLocation;
    private List<LocationProductQuantityDTO> locationProductQuantities = new ArrayList<>();

    public ConsumptionReportDTO() {
    }

    public ConsumptionReportDTO(String productProgramName, Date startDate, Date endDate, boolean byWholesaleUnit, boolean byLocation) {
        this.productProgramName = productProgramName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.byWholesaleUnit = byWholesaleUnit;
        this.byLocation = byLocation;
    }

    public String getProductProgramName() {
        return productProgramName;
    }

    public void setProductProgramName(String productProgramName) {
        this.productProgramName = productProgramName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isByWholesaleUnit() {
        return byWholesaleUnit;
    }

    public void setByWholesaleUnit(boolean byWholesaleUnit) {
        this.byWholesaleUnit = byWholesaleUnit;
    }

    public boolean isByLocation() {
        return byLocation;
    }

    public void setByLocation(boolean byLocation) {
        this.byLocation = byLocation;
    }

    public List<LocationProductQuantityDTO> getLocationProductQuantities() {
        return locationProductQuantities;
    }

    public void setLocationProductQuantities(List<LocationProductQuantityDTO> locationProductQuantities) {
        this.locationProductQuantities = locationProductQuantities;
    }
}
