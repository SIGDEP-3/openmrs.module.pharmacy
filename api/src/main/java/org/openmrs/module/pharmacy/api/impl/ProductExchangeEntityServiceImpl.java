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
import org.openmrs.module.pharmacy.ProductExchangeEntity;
import org.openmrs.module.pharmacy.api.ProductExchangeEntityService;
import org.openmrs.module.pharmacy.api.db.ProductExchangeEntityDAO;

import java.util.List;

/**
 * It is a default implementation of {@link ProductExchangeEntityService}.
 */
public class ProductExchangeEntityServiceImpl extends BaseOpenmrsService implements ProductExchangeEntityService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductExchangeEntityDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductExchangeEntityDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductExchangeEntityDAO getDao() {
	    return dao;
    }


    @Override
    public List<ProductExchangeEntity> getAllProductExchange() {
        return dao.getAllProductExchange();
    }

    @Override
    public List<ProductExchangeEntity> getAllProductExchange(Location location) {
        return dao.getAllProductExchange(location);
    }

    @Override
    public ProductExchangeEntity saveProductExchange(ProductExchangeEntity productExchange) {
        return dao.saveProductExchange(productExchange);
    }

    @Override
    public ProductExchangeEntity editProductExchange(ProductExchangeEntity productExchange) {
        return dao.editProductExchange(productExchange);
    }

    @Override
    public void removeProductExchange(ProductExchangeEntity productExchange) {
        dao.removeProductExchange(productExchange);
    }

    @Override
    public ProductExchangeEntity getOneProductExchangeById(Integer productExchangeId) {
        return dao.getOneProductExchangeById(productExchangeId);
    }

    @Override
    public ProductExchangeEntity getOneProductExchangeByUuid(String uuid) {
        return dao.getOneProductExchangeByUuid(uuid);
    }

    @Override
    public ProductExchangeEntity getOneProductExchangeByName(String name) {
        return dao.getOneProductExchangeByName(name);
    }


}
