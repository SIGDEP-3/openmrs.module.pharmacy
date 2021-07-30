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
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacy.api.ProductSupplierService;
import org.openmrs.module.pharmacy.api.db.ProductSupplierDAO;
import org.openmrs.module.pharmacy.entities.ProductSupplier;

import java.util.List;

/**
 * It is a default implementation of {@link ProductSupplierService}.
 */
public class ProductSupplierServiceImpl extends BaseOpenmrsService implements ProductSupplierService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductSupplierDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductSupplierDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductSupplierDAO getDao() {
	    return dao;
    }

    @Override
    public List<ProductSupplier> getAllProductSuppliers() {
        return dao.getAllProductSuppliers();
    }

    @Override
    public ProductSupplier saveProductSupplier(ProductSupplier productSupplier) {
        return dao.saveProductSupplier(productSupplier);
    }

    @Override
    public ProductSupplier editProductSupplier(ProductSupplier productSupplier) {
        return dao.editProductSupplier(productSupplier);
    }

    @Override
    public void removeProductSupplier(ProductSupplier productSupplier) {
        dao.removeProductSupplier(productSupplier);
    }

    @Override
    public ProductSupplier getOneProductSupplierById(Integer productSupplierId) {
        return dao.getOneProductSupplierById(productSupplierId);
    }

    @Override
    public ProductSupplier getOneProductSupplierByUuid(String uuid) {
        return dao.getOneProductSupplierByUuid(uuid);
    }

    @Override
    public ProductSupplier getOneProductSupplierByName(String name) {
        return dao.getOneProductSupplierByName(name);
    }

}
