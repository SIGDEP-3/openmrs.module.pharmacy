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
import org.openmrs.module.pharmacy.ProductTransfer;
import org.openmrs.module.pharmacy.models.ProductOutFluxDTO;
//import org.openmrs.module.pharmacy.models.ProductTransferFluxDTO;

import java.util.Date;
import java.util.List;

/**
 *  Database methods for {@link org.openmrs.module.pharmacy.api.ProductTransferService}.
 */
public interface ProductTransferDAO {

    /*
	 * Add DAO methods here
	 */
	List<ProductTransfer> getAllProductTransfers(Location location, Boolean includeVoided);
	List<ProductTransfer> getAllProductTransfers(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	List<ProductTransfer> getAllProductTransfers(Location location);
	List<ProductTransfer> getAllProductTransfers(Boolean includeVoided);
	ProductTransfer getOneProductTransferById(Integer id);
	ProductTransfer saveProductTransfer(ProductTransfer productTransfer);
	ProductTransfer editProductTransfer(ProductTransfer productTransfer);
	void removeProductTransfer(ProductTransfer productTransfer);
	ProductTransfer getOneProductTransferByUuid(String uuid);
    List<Location> getAllClientLocation(Boolean includeVoided);
//	List<ProductOutFluxDTO> getProductTransferFluxDTOs(ProductTransfer productTransfer);
//	List<ProductTransferFluxDTO> getProductTransferFluxDTOs(ProductTransfer productTransfer);
//	ProductTransfer getLastProductTransfer(Location location, ProductProgram productProgram);
//    ProductTransfer getLastProductTransferByDate(Location location, ProductProgram productProgram, Date inventoryDate);
}
