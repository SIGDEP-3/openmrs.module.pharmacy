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
package org.openmrs.module.pharmacy.api;

import org.openmrs.Location;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.entities.Product;
import org.openmrs.module.pharmacy.entities.ProductAttribute;
import org.openmrs.module.pharmacy.entities.ProductAttributeStock;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.utils.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(PharmacyService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface ProductAttributeStockService extends OpenmrsService {
     
	/*
	 * Add service methods here
	 * 
	 */

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	List<ProductAttributeStock> getAllProductAttributeStocks(Location location, Boolean includeVoided);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	List<ProductAttributeStock> getAllProductAttributeStocks(Location location);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	List<ProductAttributeStock> getAllProductAttributeStocks(Boolean includeVoided);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	List<ProductAttributeStock> getAllProductAttributeStockByAttribute(ProductAttribute productAttribute, Boolean includeVoided);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	ProductAttributeStock getOneProductAttributeStockByAttribute(ProductAttribute productAttribute, Location location, Boolean includeVoided) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	ProductAttributeStock getOneProductAttributeStockById(Integer id);

	@Transactional
	@Authorized(value = {PrivilegeConstants.MANAGE_STOCK})
	ProductAttributeStock saveProductAttributeStock(ProductAttributeStock productAttributeStock);

	@Transactional
	@Authorized(value = {PrivilegeConstants.MANAGE_STOCK})
	ProductAttributeStock editProductAttributeStock(ProductAttributeStock productAttributeStock);

	@Transactional
	@Authorized(value = {PrivilegeConstants.MANAGE_STOCK})
	void removeProductAttributeStock(ProductAttributeStock productAttributeStock);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	ProductAttributeStock getOneProductAttributeStockByUuid(String uuid);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	List<ProductAttributeStock> getProductAttributeStocksByProduct(Product product, Location userLocation);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	Integer getProductAttributeStocksByProductCount(Product product, ProductProgram productProgram);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
    List<ProductAttributeStock> getAllProductAttributeStockByProduct(Product product, Location location);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
    Integer getAllProductAttributeStockByProductCount(Product product, ProductProgram productProgram, Location location, Boolean includeChildren);

	@Transactional
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
    void voidProductAttributeStock(ProductAttributeStock attributeStock) throws APIException;
}
