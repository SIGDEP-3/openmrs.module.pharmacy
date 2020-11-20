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
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductProgramDAO;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link PharmacyService}.
 */
public class ProductProgramServiceImpl extends BaseOpenmrsService implements ProductProgramService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductProgramDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductProgramDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductProgramDAO getDao() {
	    return dao;
    }

    @Override
    public List<ProductProgram> getAllProductProgram() {
        return dao.getAllProductProgram();
    }

    @Override
    public ProductProgram getOneProductProgramById(Integer programId) {
        return dao.getOneProductProgramById(programId);
    }

    @Override
    public void removeProductProgram(ProductProgram productProgram) {
        dao.removeProductProgram(productProgram);
    }

    @Override
    public ProductProgram saveProductProgram(ProductProgram programForm) {
        return dao.saveProductProgram(programForm);
    }

    @Override
    public ProductProgram getOneProductProgramByUuid(String uuid) {
        return dao.getOneProductProgramByUuid(uuid);
    }

    @Override
    public ProductProgram getOneProductProgramByName(String name) {
        return dao.getOneProductProgramByName(name);
    }

}
