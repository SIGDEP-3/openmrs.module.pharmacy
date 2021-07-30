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
package org.openmrs.module.pharmacy.api.db;

import org.openmrs.Location;
import org.openmrs.module.pharmacy.entities.ProductInventory;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.enumerations.InventoryType;
import org.openmrs.module.pharmacy.dto.ProductInventoryFluxDTO;

import java.util.Date;
import java.util.List;

/**
 *  Database methods for {@link org.openmrs.module.pharmacy.api.ProductInventoryService}.
 */
public interface ProductInventoryDAO {

    /*
	 * Add DAO methods here
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
	List<ProductInventoryFluxDTO> getProductInventoryFluxDTOs(ProductInventory productInventory);
	ProductInventory getLastProductInventory(Location location, ProductProgram productProgram, InventoryType inventoryType);
    ProductInventory getLastProductInventoryByDate(Location location, ProductProgram productProgram, Date inventoryDate, InventoryType inventoryType);
	List<ProductInventoryFluxDTO> getProductInventoryFluxValidatedDTO(ProductInventory productInventory);
    ProductInventory getProductInventoryByDate(Location location, ProductProgram program, Date operationDate);
	ProductInventory getOneProductInventoryByOperationNumber(Location location, ProductProgram program, String operationNumber, InventoryType inventoryType);
}
