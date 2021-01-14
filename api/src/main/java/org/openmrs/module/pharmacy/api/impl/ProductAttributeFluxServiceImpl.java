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
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.db.ProductAttributeFluxDAO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link ProductAttributeFluxService}.
 */
public class ProductAttributeFluxServiceImpl extends BaseOpenmrsService implements ProductAttributeFluxService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductAttributeFluxDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductAttributeFluxDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductAttributeFluxDAO getDao() {
	    return dao;
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location, Boolean includeVoided) {
        return dao.getAllProductAttributeFluxes(location, includeVoided);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location) {
        return dao.getAllProductAttributeFluxes(location);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxes(Boolean includeVoided) {
        return dao.getAllProductAttributeFluxes(includeVoided);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location, Date startDate, Date endDate, Boolean includeVoided) {
        return dao.getAllProductAttributeFluxes(location, startDate, endDate, includeVoided);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxByAttribute(ProductAttribute productAttribute, Boolean includeVoided) {
        return dao.getAllProductAttributeFluxByAttribute(productAttribute, includeVoided);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxByOperation(ProductOperation productOperation, Boolean includeVoided) {
        return dao.getAllProductAttributeFluxByOperation(productOperation, includeVoided);
    }

    @Override
    public ProductAttributeFlux getOneProductAttributeFluxById(Integer id) {
        return dao.getOneProductAttributeFluxById(id);
    }

    @Override
    public ProductAttributeFlux getOneProductAttributeFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation) {
        return dao.getOneProductAttributeFluxByAttributeAndOperation(productAttribute, productOperation);
    }

    @Override
    public ProductAttributeFlux saveProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
        return dao.saveProductAttributeFlux(productAttributeFlux);
    }

    @Override
    public ProductAttributeFlux editProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
        return dao.editProductAttributeFlux(productAttributeFlux);
    }

    @Override
    public void removeProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
        dao.removeProductAttributeFlux(productAttributeFlux);
    }

    @Override
    public ProductAttributeFlux getOneProductAttributeFluxByUuid(String uuid) {
        return dao.getOneProductAttributeFluxByUuid(uuid);
    }

    @Override
    public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxes(Location location) {
        return dao.getAllProductAttributeOtherFluxes(location);
    }

    @Override
    public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation, Location location) {
        return dao.getOneProductAttributeOtherFluxByAttributeAndOperation(productAttribute, productOperation, location);
    }

    @Override
    public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByOperation(ProductOperation productOperation, Boolean b) {
        return dao.getAllProductAttributeOtherFluxByOperation(productOperation, b);
    }

    @Override
    public ProductAttributeOtherFlux getOneProductAttributeOtherFluxById(Integer id) {
        return dao.getOneProductAttributeOtherFluxById(id);
    }

    @Override
    public ProductAttributeOtherFlux saveProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
        return dao.saveProductAttributeOtherFlux(productAttributeOtherFlux);
    }

    @Override
    public ProductAttributeOtherFlux editProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
        return dao.editProductAttributeOtherFlux(productAttributeOtherFlux);
    }

    @Override
    public void removeProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
        dao.removeProductAttributeOtherFlux(productAttributeOtherFlux);
    }

    @Override
    public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByUuid(String uuid) {
        return dao.getOneProductAttributeOtherFluxByUuid(uuid);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxByOperationAndProduct(ProductOperation operation, Product product) {
        return dao.getAllProductAttributeFluxByOperationAndProduct(operation, product);
    }

    @Override
    public Integer getAllProductAttributeFluxByOperationAndProductCount(ProductOperation operation, Product product) {
        return dao.getAllProductAttributeFluxByOperationAndProductCount(operation, product);
    }

    @Override
    public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByOperationAndProduct(ProductOperation operation, Product product, Location location) {
        return dao.getAllProductAttributeOtherFluxByOperationAndProduct(operation, product);
    }

    @Override
    public Integer getAllProductAttributeOtherFluxByOperationAndProductCount(ProductOperation operation, Product product) {
        return dao.getAllProductAttributeOtherFluxByOperationAndProductCount(operation, product);
    }

    @Override
    public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByProductAndOperation(Product product, ProductOperation productOperation) {
        return dao.getOneProductAttributeOtherFluxByProductAndOperation(product, productOperation);
    }

    @Override
    public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByProductAndOperationAndLabel(Product product, ProductOperation productOperation, String label, Location location) {
        return dao.getOneProductAttributeOtherFluxByProductAndOperationAndLabel(product, productOperation, label, location);
    }

    @Override
    public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByProductAndOperation(Product product, ProductOperation productOperation, Location location) {
        return dao.getAllProductAttributeOtherFluxByProductAndOperation(product, productOperation, location);
    }

}
