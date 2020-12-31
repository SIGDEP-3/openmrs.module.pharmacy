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
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.models.ProductOutFluxDTO;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;
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
public interface PharmacyService extends OpenmrsService {
	@Authorized(value = {PrivilegeConstants.VALIDATE_OPERATION,
			PrivilegeConstants.VALIDATE_DISPENSATION,
			PrivilegeConstants.VALIDATE_INVENTORY,
			PrivilegeConstants.VALIDATE_RECEPTION,
			PrivilegeConstants.VALIDATE_MOVEMENT,
			PrivilegeConstants.VALIDATE_TRANSFER,
			PrivilegeConstants.VALIDATE_PRODUCT_BACK_SUPPLIER,
			PrivilegeConstants.VALIDATE_FLUX
	})
	Boolean validateOperation(ProductOperation operation);
	@Authorized(value = {PrivilegeConstants.CANCEL_DISPENSATION, PrivilegeConstants.CANCEL_OPERATION})
	Boolean cancelOperation(ProductOperation operation);
	@Authorized(value = {PrivilegeConstants.VIEW_OPERATION, PrivilegeConstants.VALIDATE_OPERATION})
	ProductOperation getOneProductOperationById(Integer productOperationId);
	@Authorized(value = {PrivilegeConstants.VIEW_OPERATION, PrivilegeConstants.VALIDATE_OPERATION})
	ProductOperation getOneProductOperationByOperationNumber(String operationNumber, Incidence incidence);
	@Authorized(value = {PrivilegeConstants.VIEW_OPERATION, PrivilegeConstants.VALIDATE_OPERATION})
	ProductOperation getOneProductOperationByOperationDateAndProductProgram(Date operationDate, ProductProgram productProgram, Location location, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.SAVE_OPERATION, PrivilegeConstants.VALIDATE_OPERATION})
	ProductOperation saveProductOperation(ProductOperation productOperation);
	@Authorized(value = {PrivilegeConstants.VIEW_OPERATION, PrivilegeConstants.VALIDATE_OPERATION})
    List<ProductOutFluxDTO> getProductOutFluxDTOs(ProductOperation productOperation);
}
