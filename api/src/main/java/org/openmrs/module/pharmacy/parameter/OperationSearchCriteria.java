package org.openmrs.module.pharmacy.parameter;

import org.openmrs.Location;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.enumerations.Incidence;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class OperationSearchCriteria {
    protected Location location;
    protected Date fromDate;
    protected Date toDate;
    protected Date dateChanged;
    protected String operationNumber;
    protected Collection<ProductProgram> productPrograms;
    protected Collection<Incidence> incidences;
    protected Boolean includeVoided;

    /**
     *
     * @param location
     * @param fromDate
     * @param toDate
     * @param dateChanged
     * @param operationNumber
     * @param productPrograms
     * @param incidences
     * @param includeVoided
     */
    public OperationSearchCriteria(Location location, Date fromDate, Date toDate,
                                   Date dateChanged, String operationNumber,
                                   Collection<ProductProgram> productPrograms, Collection<Incidence> incidences,
                                   Boolean includeVoided) {
        this.location = location;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.dateChanged = dateChanged;
        this.operationNumber = operationNumber;
        this.productPrograms = productPrograms;
        this.incidences = incidences;
        this.includeVoided = includeVoided;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public String getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    public Collection<ProductProgram> getProductPrograms() {
        return productPrograms;
    }

    public void setProductPrograms(List<ProductProgram> productPrograms) {
        this.productPrograms = productPrograms;
    }

    public Collection<Incidence> getIncidences() {
        return incidences;
    }

    public void setIncidences(List<Incidence> incidences) {
        this.incidences = incidences;
    }

    public Boolean getIncludeVoided() {
        return includeVoided;
    }

    public void setIncludeVoided(Boolean includeVoided) {
        this.includeVoided = includeVoided;
    }
}
