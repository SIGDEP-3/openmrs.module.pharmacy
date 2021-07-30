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

	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	List<ProductAttributeStock> getAllProductAttributeStocks(Location location, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	List<ProductAttributeStock> getAllProductAttributeStocks(Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	List<ProductAttributeStock> getAllProductAttributeStocks(Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	List<ProductAttributeStock> getAllProductAttributeStockByAttribute(ProductAttribute productAttribute, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	ProductAttributeStock getOneProductAttributeStockByAttribute(ProductAttribute productAttribute, Location location, Boolean includeVoided) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	ProductAttributeStock getOneProductAttributeStockById(Integer id);
	@Authorized(value = {PrivilegeConstants.MANAGE_STOCK})
	ProductAttributeStock saveProductAttributeStock(ProductAttributeStock productAttributeStock);
	@Authorized(value = {PrivilegeConstants.MANAGE_STOCK})
	ProductAttributeStock editProductAttributeStock(ProductAttributeStock productAttributeStock);
	@Authorized(value = {PrivilegeConstants.MANAGE_STOCK})
	void removeProductAttributeStock(ProductAttributeStock productAttributeStock);
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	ProductAttributeStock getOneProductAttributeStockByUuid(String uuid);
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	List<ProductAttributeStock> getProductAttributeStocksByProduct(Product product, Location userLocation);
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
	Integer getProductAttributeStocksByProductCount(Product product);
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
    List<ProductAttributeStock> getAllProductAttributeStockByProduct(Product product, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
    Integer getAllProductAttributeStockByProductCount(Product product, Location location, Boolean includeChildren);
	@Authorized(value = {PrivilegeConstants.VIEW_STOCK})
    void voidProductAttributeStock(ProductAttributeStock attributeStock);
}
