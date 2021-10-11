/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.pharmacy.api.db;

import org.openmrs.Location;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.dto.*;
import org.openmrs.module.pharmacy.entities.ProductOperation;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.enumerations.Incidence;

import java.util.Date;
import java.util.List;

/**
 *  Database methods for {@link PharmacyService}.
 */
public interface PharmacyDAO {

    /*
	 * Add DAO methods here
	 */
	Boolean validateOperation(ProductOperation operation);
	Boolean cancelOperation(ProductOperation operation);
    ProductOperation getOneProductOperationById(Integer productOperationId);
	ProductOperation getOneProductOperationByOperationNumber(String operationNumber, Incidence incidence);

	ProductOperation getOneProductOperationByOperationDateAndProductProgram(Date operationDate, ProductProgram productProgram, Location location, Boolean includeVoided);

    ProductOperation saveProductOperation(ProductOperation productOperation);

	List<ProductOutFluxDTO> getProductOutFluxDTOs(ProductOperation productOperation);

	ConsumptionReportDTO getConsumptionReport(ProductProgram productProgram, Date startDate, Date endDate, Location location, boolean byLocation);

	List<ProductMovementHistoryDTO> getProductMovementHistory(Date startDate, Date endDate, Location location, ProductProgram productProgram);

	List<RegimenReportIndicatorDTO> getRegimenAndIndicatorReport(Date startDate, Date endDate, Location location, ProductProgram productProgram);

	List<DispensationHistoryDTO> getProductDispensationHistory(Date startDate, Date endDate, Location location, ProductProgram productProgram);

	ProductOperation getOneProductOperationByUuid(String uuid);

	void removeProductOperation(ProductOperation productOperation);

	List<ProductOperation> getAll(boolean includeVoided, Location userLocation);
}
