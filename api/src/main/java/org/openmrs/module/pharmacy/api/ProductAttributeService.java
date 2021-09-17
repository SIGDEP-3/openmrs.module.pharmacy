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
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.entities.Product;
import org.openmrs.module.pharmacy.entities.ProductAttribute;
import org.openmrs.module.pharmacy.utils.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
public interface ProductAttributeService extends OpenmrsService {

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT_ATTRIBUTE})
	List<ProductAttribute> getAllProductAttributes(Location location, Boolean includeVoided);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT_ATTRIBUTE})
	List<ProductAttribute> getAllProductAttributes(Location location);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT_ATTRIBUTE})
	List<ProductAttribute> getAllProductAttributes(Boolean includeVoided);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT_ATTRIBUTE})
	List<ProductAttribute> getAllProductAttributes(Product product);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT_ATTRIBUTE})
	ProductAttribute getOneProductAttributeById(Integer id);

	@Transactional
	@Authorized(value = {PrivilegeConstants.SAVE_PRODUCT_ATTRIBUTE})
	ProductAttribute saveProductAttribute(ProductAttribute productAttribute);

	@Transactional
	@Authorized(value = {PrivilegeConstants.SAVE_PRODUCT_ATTRIBUTE})
	ProductAttribute editProductAttribute(ProductAttribute productAttribute);

	@Transactional
	@Authorized(value = {PrivilegeConstants.DELETE_PRODUCT_ATTRIBUTE})
	void removeProductAttribute(ProductAttribute productAttribute);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT_ATTRIBUTE})
	ProductAttribute getOneProductAttributeByUuid(String uuid);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT_ATTRIBUTE})
	ProductAttribute getOneProductAttributeByBatchNumber(String batchNumber, Location location);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_PRODUCT_ATTRIBUTE})
    ProductAttribute getOneProductAttributeByBatchNumberAndExpiryDate(String batchNumber, Date expiryDate);

	@Transactional
	@Authorized(value = {PrivilegeConstants.SAVE_PRODUCT_ATTRIBUTE})
    Integer purgeUnusedAttributes();
}
