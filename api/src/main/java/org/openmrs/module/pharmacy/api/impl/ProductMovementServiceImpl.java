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
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacy.ProductMovementEntry;
import org.openmrs.module.pharmacy.ProductMovementOut;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.ProductMovementService;
import org.openmrs.module.pharmacy.api.db.ProductMovementDAO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link ProductMovementService}.
 */
public class ProductMovementServiceImpl extends BaseOpenmrsService implements ProductMovementService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductMovementDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductMovementDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductMovementDAO getDao() {
	    return dao;
    }


    /******** PRODUCT MOVEMENT ENTRY *********/

    @Override
    public List<ProductMovementEntry> getAllProductMovementEntry(Location location, Boolean includeVoided) {
        return dao.getAllProductMovementEntry(location, includeVoided);
    }

    @Override
    public List<ProductMovementEntry> getAllProductMovementEntry(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductMovementEntry(location, includeVoided, operationStartDate, operationEndDate);
    }

    @Override
    public List<ProductMovementEntry> getAllProductMovementEntry(ProductProgram productProgram, Location location, Boolean includeVoided, Date startDate, Date endDate) {
        return dao.getAllProductMovementEntry(productProgram, location, includeVoided, startDate, endDate);
    }

    @Override
    public List<ProductMovementEntry> getAllProductMovementEntry(Location location) {
        return dao.getAllProductMovementEntry(location);
    }

    @Override
    public List<ProductMovementEntry> getAllProductMovementEntry(Boolean includeVoided) {
        return dao.getAllProductMovementEntry(includeVoided);
    }

    @Override
    public ProductMovementEntry getOneProductMovementEntryById(Integer id) {
        return dao.getOneProductMovementEntryById(id);
    }

    @Override
    public ProductMovementEntry saveProductMovementEntry(ProductMovementEntry productMovementEntry) throws APIException {
        return dao.saveProductMovementEntry(productMovementEntry);
    }

    @Override
    public ProductMovementEntry editProductMovementEntry(ProductMovementEntry productMovementEntry) {
        return dao.editProductMovementEntry(productMovementEntry);
    }

    @Override
    public void removeProductMovementEntry(ProductMovementEntry productMovementEntry) {
        dao.removeProductMovementEntry(productMovementEntry);
    }

    @Override
    public ProductMovementEntry getOneProductMovementEntryByUuid(String uuid) {
        return dao.getOneProductMovementEntryByUuid(uuid);
    }

//    @Override
//    public List<ProductReceptionFluxDTO> getProductReceptionFluxDTOs(ProductReception productReception) {
//        return dao.getProductReceptionFluxDTOs(productReception);
//    }

    /******** PRODUCT MOVEMENT OUT *********/

    @Override
    public List<ProductMovementOut> getAllProductMovementOut(Location location, Boolean includeVoided) {
        return dao.getAllProductMovementOut(location, includeVoided);
    }

    @Override
    public List<ProductMovementOut> getAllProductMovementOut(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductMovementOut(location, includeVoided, operationStartDate, operationEndDate);
    }

    @Override
    public List<ProductMovementOut> getAllProductMovementOut(ProductProgram productProgram, Location location, Boolean includeVoided, Date startDate, Date endDate) {
        return dao.getAllProductMovementOut(productProgram, location, includeVoided, startDate, endDate);
    }

    @Override
    public List<ProductMovementOut> getAllProductMovementOut(Location location) {
        return dao.getAllProductMovementOut(location);
    }

    @Override
    public List<ProductMovementOut> getAllProductMovementOut(Boolean includeVoided) {
        return dao.getAllProductMovementOut(includeVoided);
    }

    @Override
    public ProductMovementOut getOneProductMovementOutById(Integer id) {
        return dao.getOneProductMovementOutById(id);
    }

    @Override
    public ProductMovementOut saveProductMovementOut(ProductMovementOut productMovementOut) throws APIException {
        return dao.saveProductMovementOut(productMovementOut);
    }

    @Override
    public ProductMovementOut editProductMovementOut(ProductMovementOut productMovementOut) {
        return dao.editProductMovementOut(productMovementOut);
    }

    @Override
    public void removeProductMovementOut(ProductMovementOut productMovementOut) {
        dao.removeProductMovementOut(productMovementOut);
    }

    @Override
    public ProductMovementOut getOneProductMovementOutByUuid(String uuid) {
        return dao.getOneProductMovementOutByUuid(uuid);
    }

}
