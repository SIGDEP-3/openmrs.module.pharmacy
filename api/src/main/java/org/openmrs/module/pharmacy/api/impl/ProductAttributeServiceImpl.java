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
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.api.db.ProductAttributeDAO;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link ProductAttributeService}.
 */
public class ProductAttributeServiceImpl extends BaseOpenmrsService implements ProductAttributeService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductAttributeDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductAttributeDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductAttributeDAO getDao() {
	    return dao;
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes(Location location, Boolean includeVoided) {
        return dao.getAllProductAttributes(location, includeVoided);
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes(Location location) {
        return dao.getAllProductAttributes(location);
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes(Boolean includeVoided) {
        return dao.getAllProductAttributes(includeVoided);
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes(Product product) {
        return dao.getAllProductAttributes(product);
    }

    @Override
    public ProductAttribute getOneProductAttributeById(Integer id) {
        return dao.getOneProductAttributeById(id);
    }

    @Override
    public ProductAttribute saveProductAttribute(ProductAttribute productAttribute) {
        return dao.saveProductAttribute(productAttribute);
    }

    @Override
    public ProductAttribute editProductAttribute(ProductAttribute productAttribute) {
        return dao.saveProductAttribute(productAttribute);
    }

    @Override
    public void removeProductAttribute(ProductAttribute productAttribute) {
        dao.removeProductAttribute(productAttribute);
    }

    @Override
    public ProductAttribute getOneProductAttributeByUuid(String uuid) {
        return dao.getOneProductAttributeByUuid(uuid);
    }

    @Override
    public ProductAttribute getOneProductAttributeByBatchNumber(String batchNumber) {
        return dao.getOneProductAttributeByBatchNumber(batchNumber);
    }

}
