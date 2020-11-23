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
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.models.ProductInventoryFluxDTO;
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
	List<ProductInventory> getAllProductInventories(Location location, Boolean includeVoided);
	List<ProductInventory> getAllProductInventories(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	List<ProductInventory> getAllProductInventories(Location location);
	List<ProductInventory> getAllProductInventories(Boolean includeVoided);
	ProductInventory getOneProductInventoryById(Integer id);
	ProductInventory saveProductInventory(ProductInventory productInventory);
	ProductInventory editProductInventory(ProductInventory productInventory);
	void removeProductInventory(ProductInventory productInventory);
	ProductInventory getOneProductInventoryByUuid(String uuid);
	ProductInventory getLastProductInventory(Location location, ProductProgram productProgram);
	List<ProductInventoryFluxDTO> getProductInventoryFluxDTOs(ProductInventory productInventory);
}
