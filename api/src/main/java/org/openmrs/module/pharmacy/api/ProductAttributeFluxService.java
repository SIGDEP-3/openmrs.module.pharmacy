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
import org.openmrs.module.pharmacy.entities.*;
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
public interface ProductAttributeFluxService extends OpenmrsService {
     
	/*
	 * Add service methods here
	 * 
	 */
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	List<ProductAttributeFlux> getAllProductAttributeFluxes(Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location, Date startDate, Date endDate, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	List<ProductAttributeFlux> getAllProductAttributeFluxByAttribute(ProductAttribute productAttribute, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	List<ProductAttributeFlux> getAllProductAttributeFluxByOperation(ProductOperation productOperation, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	ProductAttributeFlux getOneProductAttributeFluxById(Integer id);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	ProductAttributeFlux getOneProductAttributeFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation);
	@Authorized(value = {PrivilegeConstants.SAVE_FLUX})
	ProductAttributeFlux saveProductAttributeFlux(ProductAttributeFlux productAttributeFlux);
	@Authorized(value = {PrivilegeConstants.SAVE_FLUX})
	ProductAttributeFlux editProductAttributeFlux(ProductAttributeFlux productAttributeFlux);
	@Authorized(value = {PrivilegeConstants.DELETE_FLUX})
	void removeProductAttributeFlux(ProductAttributeFlux productAttributeFlux);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	ProductAttributeFlux getOneProductAttributeFluxByUuid(String uuid);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxes(Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	ProductAttributeOtherFlux getOneProductAttributeOtherFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByOperation(ProductOperation reception, Boolean b);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	ProductAttributeOtherFlux getOneProductAttributeOtherFluxById(Integer id);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	ProductAttributeOtherFlux saveProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	ProductAttributeOtherFlux editProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	void removeProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	ProductAttributeOtherFlux getOneProductAttributeOtherFluxByUuid(String uuid);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
    List<ProductAttributeFlux> getAllProductAttributeFluxByOperationAndProduct(ProductOperation operation, Product product);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	Integer getAllProductAttributeFluxByOperationAndProductCount(ProductOperation operation, Product product);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
    List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByOperationAndProduct(ProductOperation operation, Product product, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	Integer getAllProductAttributeOtherFluxByOperationAndProductCount(ProductOperation operation, Product product);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
    ProductAttributeOtherFlux getOneProductAttributeOtherFluxByProductAndOperation(Product product, ProductOperation productOperation);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
    ProductAttributeOtherFlux getOneProductAttributeOtherFluxByProductAndOperationAndLabel(Product product, ProductOperation productOperation, String label, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_FLUX})
	List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByProductAndOperation(Product product, ProductOperation productOperation, Location location);
}
