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
import org.openmrs.module.pharmacy.entities.ProductPrice;
import org.openmrs.module.pharmacy.api.ProductPriceService;
import org.openmrs.module.pharmacy.api.db.ProductPriceDAO;

import java.util.List;

/**
 * It is a default implementation of {@link ProductPriceService}.
 */
public class ProductPriceServiceImpl extends BaseOpenmrsService implements ProductPriceService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductPriceDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductPriceDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductPriceDAO getDao() {
	    return dao;
    }

    @Override
    public ProductPrice saveProductPrice(ProductPrice productPrice) {
        return dao.saveProductPrice(productPrice);
    }

    @Override
    public void removeProductPrice(ProductPrice productPrice) {
        dao.removeProductPrice(productPrice);
    }

    @Override
    public ProductPrice getOneProductPriceByProductId(Integer productId) {
        return dao.getOneProductPriceByProductId(productId);
    }

    @Override
    public ProductPrice getOneProductPriceById(Integer productPriceId) {
        return dao.getOneProductPriceById(productPriceId);
    }

    @Override
    public ProductPrice getOneProductPriceByProductProgramId(Integer productProgramId) {
        return dao.getOneProductPriceByProductProgramId(productProgramId);
    }

    @Override
    public ProductPrice getOneProductPriceByUuid(String uuid) {
        return dao.getOneProductPriceByUuid(uuid);
    }

    @Override
    public ProductPrice getOneActiveProductPriceByProductAndProductProgram() {
        return dao.getOneActiveProductPriceByProductAndProductProgram();
    }

    @Override
    public List<ProductPrice> getAllProductPriceByStatus(Boolean status) {
        return dao.getAllProductPriceByStatus(status);
    }

    @Override
    public List<ProductPrice> getAllProductPrices() {
        return dao.getAllProductPrices();
    }


}
