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
import org.openmrs.module.pharmacy.ProductMovementEntry;
import org.openmrs.module.pharmacy.ProductMovementOut;
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
public interface ProductMovementService extends OpenmrsService {

	List<ProductMovementEntry> getAllProductMovementEntry(Location location, Boolean includeVoided);
	List<ProductMovementEntry> getAllProductMovementEntry(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	List<ProductMovementEntry> getAllProductMovementEntry(Location location);
	List<ProductMovementEntry> getAllProductMovementEntry(Boolean includeVoided);
	ProductMovementEntry getOneProductMovementEntryById(Integer id);
	ProductMovementEntry saveProductMovementEntry(ProductMovementEntry productMovementEntry);
	ProductMovementEntry editProductMovementEntry(ProductMovementEntry productMovementEntry);
	void removeProductMovementEntry(ProductMovementEntry productMovementEntry);
	ProductMovementEntry getOneProductMovementEntryByUuid(String uuid);
//	List<ProductMovementEntryFluxDTO> getProductMovementEntryFluxDTOs(ProductReception productReception);

	List<ProductMovementOut> getAllProductMovementOut(Location location, Boolean includeVoided);
	List<ProductMovementOut> getAllProductMovementOut(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	List<ProductMovementOut> getAllProductMovementOut(Location location);
	List<ProductMovementOut> getAllProductMovementOut(Boolean includeVoided);
	ProductMovementOut getOneProductMovementOutById(Integer id);
	ProductMovementOut saveProductMovementOut(ProductMovementOut productMovementOut);
	ProductMovementOut editProductMovementOut(ProductMovementOut productMovementOut);
	void removeProductMovementOut(ProductMovementOut productMovementOut);
	ProductMovementOut getOneProductMovementOutByUuid(String uuid);
//	List<ProductReceptionFluxDTO> getProductMovementOutFluxDTOs(ProductReception productReception);

}
