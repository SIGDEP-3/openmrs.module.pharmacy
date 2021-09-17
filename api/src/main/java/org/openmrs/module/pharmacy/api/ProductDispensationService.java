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
import org.openmrs.Patient;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.entities.MobilePatient;
import org.openmrs.module.pharmacy.entities.MobilePatientDispensationInfo;
import org.openmrs.module.pharmacy.entities.ProductDispensation;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.dto.DispensationListDTO;
import org.openmrs.module.pharmacy.dto.DispensationResultDTO;
import org.openmrs.module.pharmacy.dto.DispensationTransformationResultDTO;
import org.openmrs.module.pharmacy.dto.ProductDispensationFluxDTO;
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
public interface ProductDispensationService extends OpenmrsService {
     
	/*
	 * Add service methods here
	 * 
	 */

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	List<ProductDispensation> getAllProductDispensations(Location location, Boolean includeVoided) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	List<ProductDispensation> getAllProductDispensations(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	List<ProductDispensation> getAllProductDispensations(ProductProgram program, Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	List<ProductDispensation> getAllProductDispensations(Location location) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	List<ProductDispensation> getAllProductDispensations(Boolean includeVoided) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	ProductDispensation getOneProductDispensationById(Integer id) throws APIException;

	@Transactional
	@Authorized(value = {PrivilegeConstants.SAVE_DISPENSATION})
	ProductDispensation saveProductDispensation(ProductDispensation productDispensation) throws APIException;

	@Transactional
	@Authorized(value = {PrivilegeConstants.SAVE_DISPENSATION})
	ProductDispensation editProductDispensation(ProductDispensation productDispensation) throws APIException;

	@Transactional
	@Authorized(value = {PrivilegeConstants.DELETE_DISPENSATION})
	void removeProductDispensation(ProductDispensation productDispensation) throws APIException;

	@Transactional
	@Authorized(value = {PrivilegeConstants.DELETE_DISPENSATION})
	void removeMobilePatientInfo(MobilePatientDispensationInfo info) throws APIException;

	@Transactional
	@Authorized(value = {PrivilegeConstants.DELETE_DISPENSATION})
	void removeMobilePatient(MobilePatient patient) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	ProductDispensation getOneProductDispensationByUuid(String uuid) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	ProductDispensation getLastProductDispensation(Location location, ProductProgram productProgram) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	ProductDispensation getLastProductDispensationByDate(Location location, ProductProgram productProgram, Date dispensationDate) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	List<ProductDispensationFluxDTO> getProductDispensationFluxDTOs(ProductDispensation productDispensation) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	List<DispensationListDTO> getDispensationListDTOs(Location location) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
    MobilePatientDispensationInfo getOneMobilePatientDispensationInfoId(Integer mobileDispensationInfoId) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	MobilePatient getOneMobilePatientByIdentifier(String patientIdentifier) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	Patient getPatientByIdentifier(String identifier) throws APIException;

	@Transactional
	@Authorized(value = {PrivilegeConstants.SAVE_DISPENSATION})
	MobilePatient saveMobilePatient(MobilePatient patient) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	List<MobilePatient> getAllMobilePatients(Location location);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	MobilePatientDispensationInfo getOneMobilePatientDispensationInfoByDispensation(ProductDispensation productDispensation) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	MobilePatient getOneMobilePatientById(Integer mobilePatientId);

	@Transactional
	@Authorized(value = {PrivilegeConstants.SAVE_DISPENSATION})
	MobilePatientDispensationInfo saveMobilePatientDispensationInfo(MobilePatientDispensationInfo mobilePatientDispensationInfo) throws APIException;

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	ProductDispensation getLastProductDispensationByPatient(String identifier, ProductProgram productProgram, Location location);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	ProductDispensation getLastProductDispensationByPatient(String identifier, ProductProgram productProgram, Location location, Date dispensationDate);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	List<DispensationListDTO> getDispensationListDTOsByDate(Date startDate, Date endDate, Location location);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.VIEW_DISPENSATION})
	DispensationResultDTO getDispensationResult(Date startDate, Date endDate, Location location);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.TRANSFORM_DISPENSATION})
	DispensationTransformationResultDTO transformDispensation(Location location);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.TRANSFORM_DISPENSATION})
	DispensationTransformationResultDTO transformPatientDispensation(MobilePatient mobilePatient);

	@Transactional(readOnly = true)
	@Authorized(value = {PrivilegeConstants.TRANSFORM_DISPENSATION})
	Integer countPatientToTransform(Location location);

	@Transactional(readOnly = true)
	Boolean isDead(Patient patient, Location location);

	@Transactional(readOnly = true)
	Boolean isTransferred(Patient patient, Location location);

	@Transactional(readOnly = true)
	Date admissionDate(Patient patient, Location location);

	@Transactional(readOnly = true)
	Date deathDate(Patient patient, Location location);

	@Transactional(readOnly = true)
	Date transferDate(Patient patient, Location location);
}
