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
import org.openmrs.module.pharmacy.ProductMovementEntry;
import org.openmrs.module.pharmacy.ProductMovementOut;
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
public interface ProductMovementService extends OpenmrsService {
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	List<ProductMovementEntry> getAllProductMovementEntry(Location location, Boolean includeVoided) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	List<ProductMovementEntry> getAllProductMovementEntry(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	List<ProductMovementEntry> getAllProductMovementEntry(Location location) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	List<ProductMovementEntry> getAllProductMovementEntry(Boolean includeVoided) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	ProductMovementEntry getOneProductMovementEntryById(Integer id) throws APIException;
	@Authorized(value = {PrivilegeConstants.SAVE_MOVEMENT})
	ProductMovementEntry saveProductMovementEntry(ProductMovementEntry productMovementEntry) throws APIException;
	@Authorized(value = {PrivilegeConstants.SAVE_MOVEMENT})
	ProductMovementEntry editProductMovementEntry(ProductMovementEntry productMovementEntry) throws APIException;
	@Authorized(value = {PrivilegeConstants.DELETE_MOVEMENT})
	void removeProductMovementEntry(ProductMovementEntry productMovementEntry) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	ProductMovementEntry getOneProductMovementEntryByUuid(String uuid) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	List<ProductMovementOut> getAllProductMovementOut(Location location, Boolean includeVoided) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	List<ProductMovementOut> getAllProductMovementOut(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	List<ProductMovementOut> getAllProductMovementOut(Location location) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	List<ProductMovementOut> getAllProductMovementOut(Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	ProductMovementOut getOneProductMovementOutById(Integer id);
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	ProductMovementOut saveProductMovementOut(ProductMovementOut productMovementOut) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	ProductMovementOut editProductMovementOut(ProductMovementOut productMovementOut) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	void removeProductMovementOut(ProductMovementOut productMovementOut) throws APIException;
	@Authorized(value = {PrivilegeConstants.VIEW_MOVEMENT})
	ProductMovementOut getOneProductMovementOutByUuid(String uuid) throws APIException;

}
