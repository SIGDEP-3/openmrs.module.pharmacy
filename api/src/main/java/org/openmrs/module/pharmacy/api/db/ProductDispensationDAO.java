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
import org.openmrs.Patient;
import org.openmrs.module.pharmacy.MobilePatient;
import org.openmrs.module.pharmacy.MobilePatientDispensationInfo;
import org.openmrs.module.pharmacy.ProductDispensation;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.ProductDispensationService;
import org.openmrs.module.pharmacy.models.DispensationListDTO;
import org.openmrs.module.pharmacy.models.DispensationResultDTO;
import org.openmrs.module.pharmacy.models.DispensationTransformationResultDTO;
import org.openmrs.module.pharmacy.models.ProductDispensationFluxDTO;

import java.util.Date;
import java.util.List;

/**
 *  Database methods for {@link ProductDispensationService}.
 */
public interface ProductDispensationDAO {

    /*
	 * Add DAO methods here
	 */
	List<ProductDispensation> getAllProductDispensations(Location location, Boolean includeVoided);
	List<ProductDispensation> getAllProductDispensations(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	List<ProductDispensation> getAllProductDispensations(Location location);
	List<ProductDispensation> getAllProductDispensations(Boolean includeVoided);
	ProductDispensation getOneProductDispensationById(Integer id);
	ProductDispensation saveProductDispensation(ProductDispensation productDispensation);
	ProductDispensation editProductDispensation(ProductDispensation productDispensation);
	void removeProductDispensation(ProductDispensation productDispensation);
	void removeMobilePatientInfo(MobilePatientDispensationInfo info);
	void removeMobilePatient(MobilePatient patient);
	ProductDispensation getOneProductDispensationByUuid(String uuid);
	ProductDispensation getLastProductDispensation(Location location, ProductProgram productProgram);
	ProductDispensation getLastProductDispensationByDate(Location location, ProductProgram productProgram, Date dispensationDate);
	List<ProductDispensationFluxDTO> getProductDispensationFluxDTOs(ProductDispensation productDispensation);
	List<DispensationListDTO> getDispensationListDTOs(Location location);

    MobilePatientDispensationInfo getOneMobilePatientDispensationInfoId(Integer mobileDispensationInfoId);

	MobilePatient getOneMobilePatientByIdentifier(String patientIdentifier);

	MobilePatient saveMobilePatient(MobilePatient patient);
	List<MobilePatient> getAllMobilePatients(Location location);

	Patient getPatientByIdentifier(String identifier);

	MobilePatientDispensationInfo getOneMobilePatientDispensationInfoByDispensation(ProductDispensation productDispensation);
	MobilePatient getOneMobilePatientById(Integer mobilePatientId);

    MobilePatientDispensationInfo saveMobilePatientDispensationInfo(MobilePatientDispensationInfo mobilePatientDispensationInfo);

	ProductDispensation getLastProductDispensationByPatient(String identifier, ProductProgram productProgram, Location location);
	ProductDispensation getLastProductDispensationByPatient(String identifier, ProductProgram productProgram, Location location, Date dispensationDate);

    List<DispensationListDTO> getDispensationListDTOsByDate(Date startDate, Date endDate, Location location);
	DispensationResultDTO getDispensationResult(Date startDate, Date endDate, Location location);

    DispensationTransformationResultDTO transformDispensation(Location location);
	DispensationTransformationResultDTO transformPatientDispensation(MobilePatient mobilePatient);

    Integer countPatientToTransform(Location location);
}
