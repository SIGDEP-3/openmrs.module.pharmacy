package org.openmrs.module.pharmacy.parameter;

import org.openmrs.Location;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.enumerations.Incidence;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class OperationSearchCriteriaBuilder {
    private Location location;
    private Date fromDate;
    private Date toDate;
    private Date dateChanged;
    private String operationNumber;
    private Collection<ProductProgram> productPrograms;
    private Collection<Incidence> incidences;
    private Boolean includeVoided;

    public OperationSearchCriteriaBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    public OperationSearchCriteriaBuilder setFromDate(Date fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public OperationSearchCriteriaBuilder setToDate(Date toDate) {
        this.toDate = toDate;
        return this;
    }

    public OperationSearchCriteriaBuilder setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
        return this;
    }

    public OperationSearchCriteriaBuilder setOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
        return this;
    }

    public OperationSearchCriteriaBuilder setProductPrograms(Collection<ProductProgram> productPrograms) {
        this.productPrograms = productPrograms;
        return this;
    }

    public OperationSearchCriteriaBuilder setIncidences(Collection<Incidence> incidences) {
        this.incidences = incidences;
        return this;
    }

    public OperationSearchCriteriaBuilder setIncludeVoided(Boolean includeVoided) {
        this.includeVoided = includeVoided;
        return this;
    }

    public OperationSearchCriteria createOperationSearchCriteria() {
        return new OperationSearchCriteria(location, fromDate, toDate, dateChanged, operationNumber, productPrograms, incidences, includeVoided);
    }
}
