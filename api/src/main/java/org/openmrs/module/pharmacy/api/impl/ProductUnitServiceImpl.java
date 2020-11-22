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
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductUnitService;
import org.openmrs.module.pharmacy.api.ProductUnitService;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductUnitDAO;
import org.openmrs.module.pharmacy.api.db.ProductUnitDAO;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link PharmacyService}.
 */
public class ProductUnitServiceImpl extends BaseOpenmrsService implements ProductUnitService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductUnitDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductUnitDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductUnitDAO getDao() {
	    return dao;
    }


    @Override
    public List<ProductUnit> getAllProductUnit() {
        return dao.getAllProductUnit();
    }

    @Override
    public ProductUnit getOneProductUnitById(Integer programId) {
        return dao.getOneProductUnitById(programId);
    }

    @Override
    public void removeProductUnit(ProductUnit productUnit) {
        dao.removeProductUnit(productUnit);
    }

    @Override
    public ProductUnit saveProductUnit(ProductUnit programForm) {
        return dao.saveProductUnit(programForm);
    }

    @Override
    public ProductUnit editProductUnit(ProductUnit productUnit) {
        return dao.editProductUnit(productUnit);
    }

    @Override
    public ProductUnit getOneProductUnitByUuid(String uuid) {
        return dao.getOneProductUnitByUuid(uuid);
    }

    @Override
    public ProductUnit getOneProductUnitByName(String name) {
        return dao.getOneProductUnitByName(name);
    }


}
