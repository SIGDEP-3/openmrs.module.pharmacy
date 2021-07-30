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
import org.openmrs.module.pharmacy.entities.ProductBackSupplier;
import org.openmrs.module.pharmacy.api.ProductBackSupplierService;
import org.openmrs.module.pharmacy.api.db.ProductBackSupplierDAO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link ProductBackSupplierService}.
 */
public class ProductBackSupplierServiceImpl extends BaseOpenmrsService implements ProductBackSupplierService {
	
	protected final Log log = LogFactory.getLog(this.getClass());

    private ProductBackSupplierDAO dao;

    /**
     * @param dao the dao to set
     */
    public void setDao(ProductBackSupplierDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public ProductBackSupplierDAO getDao() {
        return dao;
    }

    @Override
    public List<ProductBackSupplier> getAllProductBackSuppliers(Location location, Boolean includeVoided) {
        return dao.getAllProductBackSuppliers(location, includeVoided);
    }

    @Override
    public List<ProductBackSupplier> getAllProductBackSuppliers(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductBackSuppliers(location, includeVoided, operationStartDate, operationEndDate);
    }

    @Override
    public List<ProductBackSupplier> getAllProductBackSuppliers(Location location) {
        return dao.getAllProductBackSuppliers(location);
    }

    @Override
    public List<ProductBackSupplier> getAllProductBackSuppliers(Boolean includeVoided) {
        return dao.getAllProductBackSuppliers(includeVoided);
    }

    @Override
    public ProductBackSupplier getOneProductBackSupplierById(Integer id) {
        return dao.getOneProductBackSupplierById(id);
    }

    @Override
    public ProductBackSupplier saveProductBackSupplier(ProductBackSupplier productBackSupplier) {
        return dao.saveProductBackSupplier(productBackSupplier);
    }

    @Override
    public ProductBackSupplier editProductBackSupplier(ProductBackSupplier productBackSupplier) {
        return dao.editProductBackSupplier(productBackSupplier);
    }

    @Override
    public void removeProductBackSupplier(ProductBackSupplier productBackSupplier) {
        dao.removeProductBackSupplier(productBackSupplier);
    }

    @Override
    public ProductBackSupplier getOneProductBackSupplierByUuid(String uuid) {
        return dao.getOneProductBackSupplierByUuid(uuid);
    }

    @Override
    public List<Location> getAllClientLocation(Boolean includeVoided) {
        return dao.getAllClientLocation(includeVoided);
    }

}
