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
package org.openmrs.module.pharmacy.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacy.dto.*;
import org.openmrs.module.pharmacy.entities.ProductOperation;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link PharmacyService}.
 */
public class PharmacyServiceImpl extends BaseOpenmrsService implements PharmacyService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private PharmacyDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(PharmacyDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public PharmacyDAO getDao() {
	    return dao;
    }

    @Override
    public Boolean validateOperation(ProductOperation operation) {
        return dao.validateOperation(operation);
    }

    @Override
    public Boolean cancelOperation(ProductOperation operation) {
        return dao.cancelOperation(operation);
    }

    @Override
    public ProductOperation getOneProductOperationById(Integer productOperationId) {
        return dao.getOneProductOperationById(productOperationId);
    }

    @Override
    public ProductOperation getOneProductOperationByOperationNumber(String operationNumber, Incidence incidence) {
        return dao.getOneProductOperationByOperationNumber(operationNumber, incidence);
    }

    @Override
    public ProductOperation getOneProductOperationByOperationDateAndProductProgram(Date operationDate, ProductProgram productProgram, Location location, Boolean includeVoided) {
        return dao.getOneProductOperationByOperationDateAndProductProgram(operationDate, productProgram, location, includeVoided);
    }

    @Override
    public ProductOperation saveProductOperation(ProductOperation productOperation) {
        return dao.saveProductOperation(productOperation);
    }

    @Override
    public List<ProductOutFluxDTO> getProductOutFluxDTOs(ProductOperation productOperation) {
        return dao.getProductOutFluxDTOs(productOperation);
    }

    @Override
    public ConsumptionReportDTO getConsumptionReport(ProductProgram productProgram, Date startDate, Date endDate, Location location, boolean byLocation) {
        return dao.getConsumptionReport(productProgram, startDate, endDate, location, byLocation);
    }

    @Override
    public List<ProductMovementHistoryDTO> getProductMovementHistory(Date startDate, Date endDate, Location location, ProductProgram productProgram) {
        return dao.getProductMovementHistory(startDate, endDate, location, productProgram);
    }

    @Override
    public List<RegimenReportIndicatorDTO> getRegimenAndIndicatorReport(Date startDate, Date endDate, Location location, ProductProgram productProgram) {
        return dao.getRegimenAndIndicatorReport(startDate, endDate, location, productProgram);
    }

    @Override
    public List<DispensationHistoryDTO> getProductDispensationHistory(Date startDate, Date endDate, Location location, ProductProgram productProgram) {
        return dao.getProductDispensationHistory(startDate, endDate, location, productProgram);
    }

    @Override
    public ProductOperation getOneProductOperationByUuid(String uuid) {
        return dao.getOneProductOperationByUuid(uuid);
    }

    @Override
    public void removeProductOperation(ProductOperation productOperation) {
        dao.removeProductOperation(productOperation);
    }

    @Override
    public List<ProductOperation> getAll(boolean includeVoided) {
        return dao.getAll(includeVoided, OperationUtils.getUserLocation());
    }

}
