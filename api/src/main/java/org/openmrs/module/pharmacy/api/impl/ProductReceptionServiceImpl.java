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
import org.openmrs.module.pharmacy.api.ProductReceptionService;
import org.openmrs.module.pharmacy.api.db.ProductReceptionDAO;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;
import org.openmrs.module.pharmacy.models.ProductReceptionListDTO;
import org.openmrs.module.pharmacy.models.ProductReceptionReturnDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link ProductReceptionService}.
 */
public class ProductReceptionServiceImpl extends BaseOpenmrsService implements ProductReceptionService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductReceptionDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductReceptionDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductReceptionDAO getDao() {
	    return dao;
    }

    @Override
    public List<ProductReception> getAllProductReceptions(Location location, Boolean includeVoided) {
        return dao.getAllProductReceptions(location, includeVoided);
    }

    @Override
    public List<ProductReception> getAllProductReceptions(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductReceptions(location, includeVoided, operationStartDate, operationEndDate);
    }

    @Override
    public List<ProductReception> getAllProductReceptions(Location location) {
        return dao.getAllProductReceptions(location);
    }

    @Override
    public List<ProductReception> getAllProductReceptions(Boolean includeVoided) {
        return dao.getAllProductReceptions(includeVoided);
    }

    @Override
    public ProductReception getOneProductReceptionById(Integer id) {
        return dao.getOneProductReceptionById(id);
    }

    @Override
    public ProductReception saveProductReception(ProductReception productReception) {
        return dao.saveProductReception(productReception);
    }

    @Override
    public ProductReception editProductReception(ProductReception productReception) {
        return dao.editProductReception(productReception);
    }

    @Override
    public void removeProductReception(ProductReception productReception) {
        dao.removeProductReception(productReception);
    }

    @Override
    public ProductReception getOneProductReceptionByUuid(String uuid) {
        return dao.getOneProductReceptionByUuid(uuid);
    }

    @Override
    public List<ProductReceptionFluxDTO> getProductReceptionFluxDTOs(ProductReception productReception) {
        return dao.getProductReceptionFluxDTOs(productReception);
    }

    @Override
    public List<ProductReceptionReturnDTO> getProductReceptionReturnDTOs(ProductReception productReception) {
        return dao.getProductReceptionReturnDTOs(productReception);
    }

    @Override
    public ProductReceptionReturnDTO getOneProductReceptionReturnDTO(ProductReception reception, ProductAttribute productAttribute) {
        return dao.getOneProductReceptionReturnDTO(reception, productAttribute);
    }

    @Override
    public List<ProductReceptionListDTO> getProductReceptionListDTOs() {
        return dao.getProductReceptionListDTOs();
    }

}
