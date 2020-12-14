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
package org.openmrs.module.pharmacy.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacy.MobilePatient;
import org.openmrs.module.pharmacy.MobilePatientDispensationInfo;
import org.openmrs.module.pharmacy.ProductDispensation;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.ProductDispensationService;
import org.openmrs.module.pharmacy.api.db.ProductDispensationDAO;
import org.openmrs.module.pharmacy.models.DispensationListDTO;
import org.openmrs.module.pharmacy.models.ProductDispensationFluxDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link ProductDispensationService}.
 */
public class ProductDispensationServiceImpl extends BaseOpenmrsService implements ProductDispensationService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductDispensationDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductDispensationDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductDispensationDAO getDao() {
	    return dao;
    }

    @Override
    public List<ProductDispensation> getAllProductDispensations(Location location, Boolean includeVoided) {
        return dao.getAllProductDispensations(location, includeVoided);
    }

    @Override
    public List<ProductDispensation> getAllProductDispensations(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductDispensations(location, includeVoided, operationEndDate, operationEndDate);
    }

    @Override
    public List<ProductDispensation> getAllProductDispensations(Location location) {
        return dao.getAllProductDispensations(location);
    }

    @Override
    public List<ProductDispensation> getAllProductDispensations(Boolean includeVoided) {
        return dao.getAllProductDispensations(includeVoided);
    }

    @Override
    public ProductDispensation getOneProductDispensationById(Integer id) {
        return dao.getOneProductDispensationById(id);
    }

    @Override
    public ProductDispensation saveProductDispensation(ProductDispensation productDispensation) {
        return dao.saveProductDispensation(productDispensation);
    }

    @Override
    public ProductDispensation editProductDispensation(ProductDispensation productDispensation) {
        return dao.editProductDispensation(productDispensation);
    }

    @Override
    public void removeProductDispensation(ProductDispensation productDispensation) {
        dao.removeProductDispensation(productDispensation);
    }

    @Override
    public void removeMobilePatientInfo(MobilePatientDispensationInfo info) throws APIException {
        dao.removeMobilePatientInfo(info);
    }

    @Override
    public void removeMobilePatient(MobilePatient patient) throws APIException {
        dao.removeMobilePatient(patient);
    }

    @Override
    public ProductDispensation getOneProductDispensationByUuid(String uuid) {
        return dao.getOneProductDispensationByUuid(uuid);
    }

    @Override
    public ProductDispensation getLastProductDispensation(Location location, ProductProgram productProgram) {
        return dao.getLastProductDispensation(location, productProgram);
    }

    @Override
    public ProductDispensation getLastProductDispensationByDate(Location location, ProductProgram productProgram, Date dispensationDate) {
        return dao. getLastProductDispensationByDate(location, productProgram, dispensationDate);
    }

    @Override
    public List<ProductDispensationFluxDTO> getProductDispensationFluxDTOs(ProductDispensation productDispensation) {
        return dao.getProductDispensationFluxDTOs(productDispensation);
    }

    @Override
    public List<DispensationListDTO> getDispensationListDTOs(Location location) throws APIException {
        return dao.getDispensationListDTOs(location);
    }

    @Override
    public MobilePatientDispensationInfo getOneMobilePatientDispensationInfoId(Integer mobileDispensationInfoId) {
        return dao. getOneMobilePatientDispensationInfoId(mobileDispensationInfoId);
    }

    @Override
    public MobilePatient getOneMobilePatientByIdentifier(String patientIdentifier) {
        return dao.getOneMobilePatientByIdentifier(patientIdentifier);
    }

    @Override
    public Patient getPatientByIdentifier(String identifier) throws APIException {
        return dao.getPatientByIdentifier(identifier);
    }

    @Override
    public MobilePatient saveMobilePatient(MobilePatient patient) {
        return dao.saveMobilePatient(patient);
    }

    @Override
    public MobilePatientDispensationInfo getOneMobilePatientDispensationInfoByDispensation(ProductDispensation productDispensation) {
        return dao.getOneMobilePatientDispensationInfoByDispensation(productDispensation);
    }

    @Override
    public MobilePatient getOneMobilePatientById(Integer mobilePatientId) {
        return dao.getOneMobilePatientById(mobilePatientId);
    }

    @Override
    public MobilePatientDispensationInfo saveMobilePatientDispensationInfo(MobilePatientDispensationInfo mobilePatientDispensationInfo) {
        return dao.saveMobilePatientDispensationInfo(mobilePatientDispensationInfo);
    }

    @Override
    public ProductDispensation getLastProductDispensationByPatient(String identifier, ProductProgram productProgram, Location location) {
        return dao.getLastProductDispensationByPatient(identifier, productProgram, location);
    }

    @Override
    public ProductDispensation getLastProductDispensationByPatient(String identifier, ProductProgram productProgram, Location location, Date dispensationDate) {
        return dao.getLastProductDispensationByPatient(identifier, productProgram, location, dispensationDate);
    }

    @Override
    public List<DispensationListDTO> getDispensationListDTOsByDate(Date startDate, Date endDate, Location location) {
        return dao.getDispensationListDTOsByDate(startDate, endDate, location);
    }
}
