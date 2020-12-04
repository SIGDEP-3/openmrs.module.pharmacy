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
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.ProductAttributeStockService;
import org.openmrs.module.pharmacy.api.db.ProductAttributeStockDAO;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link ProductAttributeStockService}.
 */
public class ProductAttributeStockServiceImpl extends BaseOpenmrsService implements ProductAttributeStockService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductAttributeStockDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductAttributeStockDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductAttributeStockDAO getDao() {
	    return dao;
    }

    @Override
    public List<ProductAttributeStock> getAllProductAttributeStocks(Location location, Boolean includeVoided) {
        return dao.getAllProductAttributeStocks(location, includeVoided);
    }

    @Override
    public List<ProductAttributeStock> getAllProductAttributeStocks(Location location) {
        return dao.getAllProductAttributeStocks(location);
    }

    @Override
    public List<ProductAttributeStock> getAllProductAttributeStocks(Boolean includeVoided) {
        return dao.getAllProductAttributeStocks(includeVoided);
    }

    @Override
    public List<ProductAttributeStock> getAllProductAttributeStockByAttribute(ProductAttribute productAttribute, Boolean includeVoided) {
        return dao.getAllProductAttributeStockByAttribute(productAttribute, includeVoided);
    }

    @Override
    public ProductAttributeStock getOneProductAttributeStockByAttribute(ProductAttribute productAttribute, Location location, Boolean includeVoided) throws APIException {
        return dao.getOneProductAttributeStockByAttribute(productAttribute, location, includeVoided);
    }

    @Override
    public ProductAttributeStock getOneProductAttributeStockById(Integer id) {
        return dao.getOneProductAttributeStockById(id);
    }

    @Override
    public ProductAttributeStock saveProductAttributeStock(ProductAttributeStock productAttributeStock) {
        return dao.saveProductAttributeStock(productAttributeStock);
    }

    @Override
    public ProductAttributeStock editProductAttributeStock(ProductAttributeStock productAttributeStock) {
        return dao.editProductAttributeStock(productAttributeStock);
    }

    @Override
    public void removeProductAttributeStock(ProductAttributeStock productAttributeStock) {
        dao.removeProductAttributeStock(productAttributeStock);
    }

    @Override
    public ProductAttributeStock getOneProductAttributeStockByUuid(String uuid) {
        return dao.getOneProductAttributeStockByUuid(uuid);
    }

    @Override
    public List<ProductAttributeStock> getProductAttributeStocksByProduct(Product product) {
        return dao.getProductAttributeStocksByProduct(product);
    }

    @Override
    public Integer getProductAttributeStocksByProductCount(Product product) {
        return dao.getProductAttributeStocksByProductCount(product);
    }

    @Override
    public List<ProductAttributeStock> getAllProductAttributeStockByProduct(Product product, Location location) {
        return dao.getAllProductAttributeStockByProduct(product, location);
    }

    @Override
    public Integer getAllProductAttributeStockByProductCount(Product product, Location location) {
        return dao.getAllProductAttributeStockByProductCount(product, location);
    }

}
