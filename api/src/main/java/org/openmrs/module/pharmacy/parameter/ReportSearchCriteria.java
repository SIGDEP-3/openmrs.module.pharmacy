package org.openmrs.module.pharmacy.parameter;

import org.openmrs.Location;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.enumerations.Incidence;

import java.util.Collection;
import java.util.Date;

public class ReportSearchCriteria extends OperationSearchCriteria {
    public ReportSearchCriteria(Location location, Date fromDate, Date toDate,
                                Date dateChanged, String operationNumber,
                                Collection<ProductProgram> productPrograms, Collection<Incidence> incidences,
                                Boolean includeVoided) {
        super(location, fromDate, toDate, dateChanged, operationNumber, productPrograms, incidences, includeVoided);
    }
}
