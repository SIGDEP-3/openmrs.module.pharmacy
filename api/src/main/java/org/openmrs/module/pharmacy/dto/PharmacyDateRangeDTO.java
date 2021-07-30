package org.openmrs.module.pharmacy.dto;

import java.util.Date;

public class PharmacyDateRangeDTO {
    private Date startDate;
    private Date endDate;

    public PharmacyDateRangeDTO() {
    }

    public PharmacyDateRangeDTO(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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
}
