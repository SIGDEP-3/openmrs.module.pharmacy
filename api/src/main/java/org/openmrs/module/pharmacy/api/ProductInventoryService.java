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
import org.openmrs.module.pharmacy.entities.ProductInventory;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.enumerations.InventoryType;
import org.openmrs.module.pharmacy.dto.ProductInventoryFluxDTO;
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
public interface ProductInventoryService extends OpenmrsService {
     
	/*
	 * Add service methods here
	 * 
	 */

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	List<ProductInventory> getAllProductInventories(Location location, Boolean includeVoided);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	List<ProductInventory> getAllProductInventories(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	List<ProductInventory> getAllProductInventories(Location location);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	List<ProductInventory> getAllProductInventories(Boolean includeVoided);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	ProductInventory getOneProductInventoryById(Integer id);

	@Transactional
	@Authorized(value = {PrivilegeConstants.SAVE_INVENTORY})
	ProductInventory saveProductInventory(ProductInventory productInventory);

	@Transactional
	@Authorized(value = {PrivilegeConstants.SAVE_INVENTORY})
	ProductInventory editProductInventory(ProductInventory productInventory);

	@Transactional
	@Authorized(value = {PrivilegeConstants.DELETE_INVENTORY})
	void removeProductInventory(ProductInventory productInventory);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	ProductInventory getOneProductInventoryByUuid(String uuid);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	ProductInventory getLastProductInventory(Location location, ProductProgram productProgram, InventoryType inventoryType);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	ProductInventory getLastProductInventoryByDate(Location location, ProductProgram productProgram, Date inventoryDate, InventoryType inventoryType);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	List<ProductInventoryFluxDTO> getProductInventoryFluxDTOs(ProductInventory productInventory);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	List<ProductInventoryFluxDTO> getProductInventoryFluxValidatedDTO(ProductInventory productInventory);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
    ProductInventory getProductInventoryByDate(Location userLocation, ProductProgram oneProductProgramById, Date operationDate);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_INVENTORY})
	ProductInventory getOneProductInventoryByOperationNumber(Location location, ProductProgram program, String operationNumber, InventoryType inventoryType);
}
