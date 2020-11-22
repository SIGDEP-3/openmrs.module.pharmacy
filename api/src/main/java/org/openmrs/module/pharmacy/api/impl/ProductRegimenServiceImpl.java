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
import org.openmrs.module.pharmacy.api.ProductReceptionService;
import org.openmrs.module.pharmacy.api.ProductRegimenService;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductRegimenDAO;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link PharmacyService}.
 */
public class ProductRegimenServiceImpl extends BaseOpenmrsService implements ProductRegimenService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductRegimenDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductRegimenDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductRegimenDAO getDao() {
	    return dao;
    }


    @Override
    public ProductRegimen saveProductRegimen(ProductRegimen productRegimen) {
        return dao.saveProductRegimen(productRegimen);
    }

    @Override
    public void removeProductRegimen(ProductRegimen productRegimen) {
        dao.removeProductRegimen(productRegimen);
    }

    @Override
    public ProductRegimen getOneProductRegimenById(Integer regimenId) {
        return dao.getOneProductRegimenById(regimenId);
    }

    @Override
    public ProductRegimen getOneProductRegimenByUuid(String uuid) {
        return dao.getOneProductRegimenByUuid(uuid);
    }

    @Override
    public ProductRegimen getOneProductRegimenByConceptName(String name) {
        return dao.getOneProductRegimenByConceptName(name);
    }

    @Override
    public ProductRegimen getOneProductRegimenByConceptId(Integer conceptId) {
        return dao.getOneProductRegimenByConceptId(conceptId);
    }

    @Override
    public List<ProductRegimen> getAllProductRegimen() {
        return dao.getAllProductRegimen();
    }

}
