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
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.ProductInventoryService;
import org.openmrs.module.pharmacy.api.db.ProductInventoryDAO;
import org.openmrs.module.pharmacy.api.db.ProductInventoryDAO;
import org.openmrs.module.pharmacy.models.ProductInventoryFluxDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link ProductInventoryService}.
 */
public class ProductInventoryServiceImpl extends BaseOpenmrsService implements ProductInventoryService {
	
	protected final Log log = LogFactory.getLog(this.getClass());

    private ProductInventoryDAO dao;

    /**
     * @param dao the dao to set
     */
    public void setDao(ProductInventoryDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public ProductInventoryDAO getDao() {
        return dao;
    }

    @Override
    public List<ProductInventory> getAllProductInventories(Location location, Boolean includeVoided) {
        return dao.getAllProductInventories(location, includeVoided);
    }

    @Override
    public List<ProductInventory> getAllProductInventories(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductInventories(location, includeVoided, operationStartDate, operationEndDate);
    }

    @Override
    public List<ProductInventory> getAllProductInventories(Location location) {
        return dao.getAllProductInventories(location);
    }

    @Override
    public List<ProductInventory> getAllProductInventories(Boolean includeVoided) {
        return dao.getAllProductInventories(includeVoided);
    }

    @Override
    public ProductInventory getOneProductInventoryById(Integer id) {
        return dao.getOneProductInventoryById(id);
    }

    @Override
    public ProductInventory saveProductInventory(ProductInventory productInventory) {
        return dao.saveProductInventory(productInventory);
    }

    @Override
    public ProductInventory editProductInventory(ProductInventory productInventory) {
        return dao.editProductInventory(productInventory);
    }

    @Override
    public void removeProductInventory(ProductInventory productInventory) {
        dao.removeProductInventory(productInventory);
    }

    @Override
    public ProductInventory getOneProductInventoryByUuid(String uuid) {
        return dao.getOneProductInventoryByUuid(uuid);
    }

    @Override
    public ProductInventory getLastProductInventory(Location location, ProductProgram productProgram) {
        return dao.getLastProductInventory(location, productProgram);
    }

    @Override
    public List<ProductInventoryFluxDTO> getProductInventoryFluxDTOs(ProductInventory productInventory) {
        return dao.getProductInventoryFluxDTOs(productInventory);
    }
}
