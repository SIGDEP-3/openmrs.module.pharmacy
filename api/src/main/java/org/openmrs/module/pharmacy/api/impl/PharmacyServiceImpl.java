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

import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductUnit;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;

import java.util.List;

/**
 * It is a default implementation of {@link PharmacyService}.
 */
public class PharmacyServiceImpl extends BaseOpenmrsService implements PharmacyService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private PharmacyDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(PharmacyDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public PharmacyDAO getDao() {
	    return dao;
    }

    /******* PRODUCTS UNITS ******/

    @Override
    public ProductUnit saveProductUnit(ProductUnit productUnit) {
        return dao.saveProductUnit(productUnit);
    }

    @Override
    public ProductUnit editProductUnit(ProductUnit productUnit) {
        return dao.editProductUnit(productUnit);
    }

    @Override
    public void removeProductUnit(ProductUnit productUnit) {
        dao.removeProductUnit(productUnit);
    }

    @Override
    public ProductUnit getOneProductUnitById(Integer productUnitId) {
        return dao.getOneProductUnitById(productUnitId);
    }

    @Override
    public ProductUnit getOneProductUnitByUuid(String uuid) {
        return dao.getOneProductUnitByUuid(uuid);
    }

    @Override
    public ProductUnit getOneProductUnitByName(String name) {
        return dao.getOneProductUnitByName(name);
    }

    @Override
    public List<ProductUnit> getAllProductUnit() {
        return dao.getAllProductUnit();
    }

    /******* PRODUCTS ******/

    @Override
    public Product saveProduct(Product product) {
        return dao.saveProduct(product);
    }

    @Override
    public Product editProduct(Product product) {
        return dao.editProduct(product);
    }

    @Override
    public Product getOneProductById(Integer productId) {
        return dao.getOneProductById(productId);
    }

    @Override
    public Product getOneProductByCode(String code) {
        return dao.getOneProductByCode(code);
    }

    @Override
    public Product getOneProductByUuid(String uuid) {
        return dao.getOneProductByUuid(uuid);
    }

    @Override
    public Product getOneProductByRetailName(String retailName) {
        return dao.getOneProductByRetailName(retailName);
    }

    @Override
    public Product getOneProductByWholesaleName(String wholesaleName) {
        return dao.getOneProductByWholesaleName(wholesaleName);
    }

    @Override
    public Product getOneProductByName(String name) {
        return dao.getOneProductByName(name);
    }

    @Override
    public List<Product> getAllProduct() {
        return dao.getAllProduct();
    }

    @Override
    public List<Product> getAllProductByRetailUnit(ProductUnit retailUnit) {
        return dao.getAllProductByRetailUnit(retailUnit);
    }

    @Override
    public List<Product> getAllProductByWholesaleUnit(ProductUnit wholesaleUnit) {
        return dao.getAllProductByWholesaleUnit(wholesaleUnit);
    }

    @Override
    public List<Product> searchProductByNameLike(String nameSearch) {
        return dao.searchProductByNameLike(nameSearch);
    }
}
