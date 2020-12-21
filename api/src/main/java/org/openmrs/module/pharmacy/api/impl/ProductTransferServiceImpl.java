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
import org.openmrs.module.pharmacy.ProductTransfer;
import org.openmrs.module.pharmacy.api.ProductTransferService;
import org.openmrs.module.pharmacy.api.db.ProductTransferDAO;
import org.openmrs.module.pharmacy.models.ProductOutFluxDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link ProductTransferService}.
 */
public class ProductTransferServiceImpl extends BaseOpenmrsService implements ProductTransferService {
	
	protected final Log log = LogFactory.getLog(this.getClass());

    private ProductTransferDAO dao;

    /**
     * @param dao the dao to set
     */
    public void setDao(ProductTransferDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public ProductTransferDAO getDao() {
        return dao;
    }

    @Override
    public List<ProductTransfer> getAllProductTransfers(Location location, Boolean includeVoided) {
        return dao.getAllProductTransfers(location, includeVoided);
    }

    @Override
    public List<ProductTransfer> getAllProductTransfers(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductTransfers(location, includeVoided, operationStartDate, operationEndDate);
    }

    @Override
    public List<ProductTransfer> getAllProductTransfers(Location location) {
        return dao.getAllProductTransfers(location);
    }

    @Override
    public List<ProductTransfer> getAllProductTransfers(Boolean includeVoided) {
        return dao.getAllProductTransfers(includeVoided);
    }

    @Override
    public ProductTransfer getOneProductTransferById(Integer id) {
        return dao.getOneProductTransferById(id);
    }

    @Override
    public ProductTransfer saveProductTransfer(ProductTransfer productTransfer) {
        return dao.saveProductTransfer(productTransfer);
    }

    @Override
    public ProductTransfer editProductTransfer(ProductTransfer productTransfer) {
        return dao.editProductTransfer(productTransfer);
    }

    @Override
    public void removeProductTransfer(ProductTransfer productTransfer) {
        dao.removeProductTransfer(productTransfer);
    }

    @Override
    public ProductTransfer getOneProductTransferByUuid(String uuid) {
        return dao.getOneProductTransferByUuid(uuid);
    }

    @Override
    public List<Location> getAllClientLocation(Boolean includeVoided) {
        return dao.getAllClientLocation(includeVoided);
    }

//    @Override
//    public List<ProductOutFluxDTO> getProductTransferFluxDTOs(ProductTransfer productTransfer) {
//        return dao.getProductTransferFluxDTOs(productTransfer);
//    }

//    @Override
//    public ProductTransfer getLastProductTransfer(Location location, ProductProgram productProgram) {
//        return dao.getLastProductTransfer(location, productProgram);
//    }
//
//    @Override
//    public ProductTransfer getLastProductTransferByDate(Location location, ProductProgram productProgram, Date inventoryDate) {
//        return dao.getLastProductTransferByDate(location, productProgram, inventoryDate);
//    }

//    @Override
//    public List<ProductTransferFluxDTO> getProductTransferFluxDTOs(ProductTransfer productTransfer) {
//        return dao.getProductTransferFluxDTOs(productTransfer);
//    }
}
