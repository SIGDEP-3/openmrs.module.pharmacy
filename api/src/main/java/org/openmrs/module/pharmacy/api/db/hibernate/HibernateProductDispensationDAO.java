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
package org.openmrs.module.pharmacy.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.MobilePatient;
import org.openmrs.module.pharmacy.entities.MobilePatientDispensationInfo;
import org.openmrs.module.pharmacy.entities.ProductDispensation;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.api.db.ProductDispensationDAO;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.PatientType;
import org.openmrs.module.pharmacy.dto.DispensationListDTO;
import org.openmrs.module.pharmacy.dto.DispensationResultDTO;
import org.openmrs.module.pharmacy.dto.DispensationTransformationResultDTO;
import org.openmrs.module.pharmacy.dto.ProductDispensationFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * It is a default implementation of  {@link ProductDispensationDAO}.
 */
public class HibernateProductDispensationDAO implements ProductDispensationDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	private SessionFactory sessionFactory;

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDispensation> getAllProductDispensations(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return criteria.add(Restrictions.eq("location", location)).
				add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDispensation> getAllProductDispensations(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.between("operationDate", operationStartDate, operationEndDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDispensation> getAllProductDispensations(ProductProgram program, Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.eq("productProgram", program))
				.add(Restrictions.between("operationDate", operationStartDate, operationEndDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDispensation> getAllProductDispensations(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDispensation> getAllProductDispensations(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return criteria.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@Override
	public ProductDispensation getOneProductDispensationById(Integer id) {
		return (ProductDispensation) sessionFactory.getCurrentSession().get(ProductDispensation.class, id);
	}

	@Override
	public ProductDispensation saveProductDispensation(ProductDispensation productDispensation) {
		sessionFactory.getCurrentSession().saveOrUpdate(productDispensation);
		return productDispensation;
	}

	@Override
	public ProductDispensation editProductDispensation(ProductDispensation productDispensation) {
		sessionFactory.getCurrentSession().saveOrUpdate(productDispensation);
		return productDispensation;
	}

	@Override
	public void removeProductDispensation(ProductDispensation productDispensation) {
		sessionFactory.getCurrentSession().delete(productDispensation);
	}

	@Override
	public void removeMobilePatientInfo(MobilePatientDispensationInfo info) {
		sessionFactory.getCurrentSession().delete(info);
	}

	@Override
	public void removeMobilePatient(MobilePatient patient) {
		sessionFactory.getCurrentSession().delete(patient);
	}

	@Override
	public ProductDispensation getOneProductDispensationByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return (ProductDispensation) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDispensationFluxDTO> getProductDispensationFluxDTOs(ProductDispensation productDispensation) {
		String sqlQuery =
				"SELECT " +
						"pp.product_id productId, " +
						"pp.code, " +
						"pp.retail_name retailName, " +
						"ppu.name retailUnit, " +
						"pf.dispensingQuantity, " +
						"ppaof.quantity requestedQuantity, " +
						"ps.quantityInStock, " +
						"pf.date_created dateCreated " +
						"FROM pharmacy_product_dispensation ppd " +
						"LEFT JOIN pharmacy_product_operation ppo ON ppo.product_operation_id = ppd.product_operation_id " +
						"LEFT JOIN pharmacy_product_attribute_other_flux ppaof ON ppo.product_operation_id = ppaof.operation_id " +
						"LEFT JOIN ( " +
						"    SELECT pa.product_attribute_id, operation_id, pa.product_id, SUM(ppaf.quantity) dispensingQuantity, date_created FROM pharmacy_product_attribute_flux ppaf LEFT JOIN pharmacy_product_attribute pa ON ppaf.product_attribute_id = pa.product_attribute_id WHERE operation_id = :productOperationId GROUP BY pa.product_id " +
						") pf on ppo.product_operation_id = pf.operation_id AND pf.product_id = ppaof.product_id " +
						"LEFT JOIN pharmacy_product pp on pp.product_id = ppaof.product_id " +
						"LEFT JOIN pharmacy_product_unit ppu on ppu.product_unit_id = pp.product_retail_unit and ppu.product_unit_id = pp.product_wholesale_unit " +
						"LEFT JOIN ( " +
						"    SELECT pa.product_attribute_id, pa.product_id, SUM(ppas.quantity_in_stock) quantityInStock FROM pharmacy_product_attribute_stock ppas LEFT JOIN pharmacy_product_attribute pa ON ppas.product_attribute_id = pa.product_attribute_id  GROUP BY pa.product_id " +
						"    ) ps ON ps.product_id = ppaof.product_id " +
						"WHERE ppd.product_operation_id = :productOperationId HAVING quantityInStock IS NOT NULL ORDER BY pf.date_created DESC ";

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("productId", StandardBasicTypes.INTEGER)
				.addScalar("code", StandardBasicTypes.STRING)
				.addScalar("retailName", StandardBasicTypes.STRING)
				.addScalar("retailUnit", StandardBasicTypes.STRING)
				.addScalar("requestedQuantity", StandardBasicTypes.INTEGER)
				.addScalar("dispensingQuantity", StandardBasicTypes.INTEGER)
				.addScalar("quantityInStock", StandardBasicTypes.INTEGER)
				.addScalar("dateCreated", StandardBasicTypes.DATE)
				.setParameter("productOperationId", productDispensation.getProductOperationId())
				.setResultTransformer(new AliasToBeanResultTransformer(ProductDispensationFluxDTO.class));
		try {
			return query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}

	@Override
	public MobilePatientDispensationInfo getOneMobilePatientDispensationInfoId(Integer mobileDispensationInfoId) {
		return (MobilePatientDispensationInfo) sessionFactory.getCurrentSession().get(MobilePatientDispensationInfo.class, mobileDispensationInfoId);
	}

	@Override
	public MobilePatient getOneMobilePatientByIdentifier(String patientIdentifier) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MobilePatient.class);
		return (MobilePatient) criteria
				.add(Restrictions.eq("identifier", patientIdentifier)).uniqueResult();
	}

	@Override
	public MobilePatient saveMobilePatient(MobilePatient patient) {
		sessionFactory.getCurrentSession().saveOrUpdate(patient);
		return patient;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MobilePatient> getAllMobilePatients(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MobilePatient.class);
		return criteria
				.add(Restrictions.eq("location", location)).list();
	}

	@Override
	public Patient getPatientByIdentifier(String identifier) {
		return (Patient) sessionFactory.getCurrentSession().createQuery("" +
				"SELECT i.patient FROM PatientIdentifier i WHERE i.identifier = :identifier " +
				"AND i.preferred = true AND i.voided = false AND i.location = :location")
				.setParameter("identifier", identifier)
				.setParameter("location", OperationUtils.getUserLocation())
				.uniqueResult();
	}

	@Override
	public MobilePatientDispensationInfo getOneMobilePatientDispensationInfoByDispensation(ProductDispensation productDispensation) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MobilePatientDispensationInfo.class);
		return (MobilePatientDispensationInfo) criteria
				.add(Restrictions.eq("productDispensation", productDispensation)).uniqueResult();
	}

	@Override
	public MobilePatient getOneMobilePatientById(Integer mobilePatientId) {
		return (MobilePatient) sessionFactory.getCurrentSession().get(MobilePatient.class, mobilePatientId);
	}

	@Override
	public MobilePatientDispensationInfo saveMobilePatientDispensationInfo(MobilePatientDispensationInfo mobilePatientDispensationInfo) {
		sessionFactory.getCurrentSession().saveOrUpdate(mobilePatientDispensationInfo);
		return mobilePatientDispensationInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DispensationListDTO> getDispensationListDTOs(Location location) {
		String sqlQuery = "SELECT  " +
				"       ppd.product_operation_id productOperationId,  " +
				"       ppd.prescription_date prescriptionDate,  " +
				"       ppo.operation_date operationDate,  " +
				"       ppp.name programName,  " +
				"       e.encounter_id encounterId,  " +
				"       IF(conceptRegimen IS NOT NULL, conceptRegimen,  " +
				"           IF(mobileRegimen IS NOT NULL, mobileRegimen, 'N/A')) regimen,  " +
				"       IF(treatmentDaysConcept IS NOT NULL, treatmentDaysConcept,  " +
				"           IF(treatment_days IS NOT NULL, treatment_days, NULL)) treatmentDays,  " +
				"       IF(identifierMobilePatient IS NOT NULL, identifierMobilePatient,  " +
				"           IF(identifierPatientRegistered IS NOT NULL, identifierPatientRegistered, 'PAS DE PATIENT')) patientIdentifier,  " +
				"       IF(ppo.operation_status = 1, 'DISABLED', IF(ppo.operation_status = 2, 'VALIDATED', 'NOT_COMPLETED')) operationStatus,  " +
				"       IF(e.encounter_id IS NOT NULL OR pmp.patient_type = 0, 'PATIENT PEC',  " +
				"           IF (pmp.patient_type = 1, 'PATIENT MOBILE',  " +
				"               IF(pmp.patient_type = 2, 'PREVENTION', 'PAS DE PATIENT')) ) patientType,  " +
				"       ppo.date_created dateCreated  " +
				"FROM pharmacy_product_dispensation ppd  " +
				"LEFT JOIN pharmacy_product_operation ppo ON ppd.product_operation_id = ppo.product_operation_id  " +
				"LEFT JOIN pharmacy_product_program ppp ON ppo.program_id = ppp.product_program_id  " +
				"LEFT JOIN pharmacy_mobile_patient_dispensation_info pmpdi on ppd.product_operation_id = pmpdi.mobile_dispensation_info_id  " +
				"LEFT JOIN encounter e on e.encounter_id = ppd.encounter_id  " +
				"LEFT JOIN (SELECT name conceptRegimen, encounter_id FROM obs o LEFT JOIN concept_name cn ON o.concept_id = cn.concept_id WHERE o.concept_id = 165033 AND o.voided = 0) R on e.encounter_id = R.encounter_id  " +
				"LEFT JOIN (SELECT DISTINCT product_regimen_id, name mobileRegimen FROM pharmacy_product_regimen p, concept_name n WHERE p.concept_id = n.concept_id) ppr on ppr.product_regimen_id = pmpdi.regimen_id  " +
				"LEFT JOIN (SELECT encounter_id, value_numeric treatmentDaysConcept FROM obs WHERE concept_id = 165011) D ON D.encounter_id = e.encounter_id  " +
				"LEFT JOIN (SELECT mobile_patient_id, identifier identifierMobilePatient, patient_type FROM pharmacy_mobile_patient) pmp ON pmpdi.mobile_patient_id = pmp.mobile_patient_id  " +
				"LEFT JOIN (SELECT patient_id, identifier identifierPatientRegistered FROM patient_identifier) pi on e.patient_id = pi.patient_id  " +
				"WHERE ppo.location_id = :locationId";
//		Type StatusEnum = sessionFactory.getTypeHelper().custom(OperationStatus.class);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("productOperationId", StandardBasicTypes.INTEGER)
				.addScalar("prescriptionDate", StandardBasicTypes.DATE)
				.addScalar("operationDate", StandardBasicTypes.DATE)
				.addScalar("programName", StandardBasicTypes.STRING)
				.addScalar("encounterId", StandardBasicTypes.INTEGER)
				.addScalar("regimen", StandardBasicTypes.STRING)
				.addScalar("treatmentDays", StandardBasicTypes.INTEGER)
				.addScalar("patientIdentifier", StandardBasicTypes.STRING)
				.addScalar("operationStatus", StandardBasicTypes.STRING)
				.addScalar("patientType", StandardBasicTypes.STRING)
				.addScalar("dateCreated", StandardBasicTypes.DATE)
				.setParameter("locationId", location.getLocationId())
				.setResultTransformer(new AliasToBeanResultTransformer(DispensationListDTO.class));
		try {
			return query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DispensationListDTO> getDispensationListDTOsByDate(Date startDate, Date endDate, Location location) {
		String sqlQuery = "SELECT  DISTINCT " +
				"       ppd.product_operation_id productOperationId,  " +
				"       ppd.prescription_date prescriptionDate,  " +
				"       ppo.operation_date operationDate,  " +
				"       ppp.name programName,  " +
				"       e.encounter_id encounterId,  " +
				"       IF(conceptRegimen IS NOT NULL, conceptRegimen,  " +
				"           IF(mobileRegimen IS NOT NULL, mobileRegimen, 'N/A')) regimen,  " +
				"       IF(treatmentDaysConcept IS NOT NULL, treatmentDaysConcept,  " +
				"           IF(treatment_days IS NOT NULL, treatment_days, NULL)) treatmentDays,  " +
				"       IF(identifierMobilePatient IS NOT NULL, identifierMobilePatient,  " +
				"           IF(identifierPatientRegistered IS NOT NULL, identifierPatientRegistered, 'PAS DE PATIENT')) patientIdentifier,  " +
				"       IF(ppo.operation_status = 1, 'DISABLED', IF(ppo.operation_status = 2, 'VALIDATED', 'NOT_COMPLETED')) operationStatus,  " +
				"       IF(e.encounter_id IS NOT NULL OR pmp.patient_type = 0, 'PATIENT PEC',  " +
				"           IF (pmp.patient_type = 1, 'PATIENT MOBILE',  " +
				"               IF(pmp.patient_type = 2, 'PREVENTION', 'PAS DE PATIENT')) ) patientType, " +
				"       ppo.date_created dateCreated " +
				"FROM pharmacy_product_dispensation ppd " +
				"LEFT JOIN (SELECT * FROM pharmacy_product_operation WHERE date_created BETWEEN :startDate AND :endDate AND voided = 0) ppo ON ppd.product_operation_id = ppo.product_operation_id  " +
				"LEFT JOIN pharmacy_product_program ppp ON ppo.program_id = ppp.product_program_id  " +
				"LEFT JOIN pharmacy_mobile_patient_dispensation_info pmpdi on ppd.product_operation_id = pmpdi.mobile_dispensation_info_id  " +
				"LEFT JOIN encounter e on e.encounter_id = ppd.encounter_id  " +
				"LEFT JOIN (SELECT name conceptRegimen, encounter_id FROM obs o LEFT JOIN concept_name cn ON o.value_coded = cn.concept_id WHERE o.concept_id = 165033 AND o.voided = 0) R on e.encounter_id = R.encounter_id  " +
				"LEFT JOIN (SELECT DISTINCT product_regimen_id, name mobileRegimen FROM pharmacy_product_regimen p, concept_name n WHERE p.concept_id = n.concept_id) ppr on ppr.product_regimen_id = pmpdi.regimen_id  " +
				"LEFT JOIN (SELECT encounter_id, value_numeric treatmentDaysConcept FROM obs WHERE concept_id = 165011) D ON D.encounter_id = e.encounter_id  " +
				"LEFT JOIN (SELECT mobile_patient_id, identifier identifierMobilePatient, patient_type FROM pharmacy_mobile_patient) pmp ON pmpdi.mobile_patient_id = pmp.mobile_patient_id  " +
				"LEFT JOIN (SELECT patient_id, identifier identifierPatientRegistered FROM patient_identifier) pi on e.patient_id = pi.patient_id  " +
				"WHERE ppo.location_id = :locationId";
//		Type StatusEnum = sessionFactory.getTypeHelper().custom(OperationStatus.class);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("productOperationId", StandardBasicTypes.INTEGER)
				.addScalar("prescriptionDate", StandardBasicTypes.DATE)
				.addScalar("operationDate", StandardBasicTypes.DATE)
				.addScalar("programName", StandardBasicTypes.STRING)
				.addScalar("encounterId", StandardBasicTypes.INTEGER)
				.addScalar("regimen", StandardBasicTypes.STRING)
				.addScalar("treatmentDays", StandardBasicTypes.INTEGER)
				.addScalar("patientIdentifier", StandardBasicTypes.STRING)
				.addScalar("operationStatus", StandardBasicTypes.STRING)
				.addScalar("patientType", StandardBasicTypes.STRING)
				.addScalar("dateCreated", StandardBasicTypes.DATE)
				.setParameter("locationId", location.getLocationId())
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setResultTransformer(new AliasToBeanResultTransformer(DispensationListDTO.class));
		try {
			return query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}

	@Override
	public ProductDispensation getLastProductDispensation(Location location, ProductProgram productProgram) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return (ProductDispensation) criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("productProgram", productProgram))
				.addOrder(Order.desc("operationDate")).setMaxResults(1).uniqueResult();
	}

	@Override
	public ProductDispensation getLastProductDispensationByDate(Location location, ProductProgram productProgram, Date dispensationDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return (ProductDispensation) criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("productProgram", productProgram))
				.add(Restrictions.lt("operationDate", dispensationDate))
				.addOrder(Order.desc("operationDate")).setMaxResults(1).uniqueResult();
	}

	@Override
	public ProductDispensation getLastProductDispensationByPatient(String identifier, ProductProgram productProgram, Location location) throws HibernateException {
		Patient patient = getPatientByIdentifier(identifier);
		MobilePatient mobilePatient = getOneMobilePatientByIdentifier(identifier);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class, "d");
		if (patient != null) {
			Concept concept = Context.getConceptService().getConcept(165033);
			return (ProductDispensation) criteria.createAlias("d.encounter", "e")
					.createAlias("e.obs", "o")
					.add(Restrictions.eq("d.productProgram", productProgram))
					.add(Restrictions.eq("d.operationStatus", OperationStatus.VALIDATED))
					.add(Restrictions.eq("d.voided", false))
					.add(Restrictions.eq("d.location", location))
					.add(Restrictions.eq("o.concept", concept))
					.add(Restrictions.eq("e.patient", patient))
					.addOrder(Order.desc("d.operationDate")).setMaxResults(1).uniqueResult();
		} else if (mobilePatient != null) {
			return (ProductDispensation) criteria.createAlias("d.mobilePatientDispensationInfo", "i")
					.add(Restrictions.eq("d.productProgram", productProgram))
					.add(Restrictions.eq("d.operationStatus", OperationStatus.VALIDATED))
					.add(Restrictions.eq("d.voided", false))
					.add(Restrictions.eq("d.location", location))
					.add(Restrictions.eq("i.mobilePatient", mobilePatient))
					.add(Restrictions.isNotNull("i.productRegimen"))
					.addOrder(Order.desc("d.operationDate")).setMaxResults(1).uniqueResult();
		}
		return null;

//		String queryStr = "";
//		if (patient != null) {
//			queryStr = "SELECT p FROM ProductDispensation p " +
//					"WHERE " +
//					"p.encounter.patient = :patient AND " +
//					"p.productProgram = :program AND p.voided = false AND p.location = :location AND " +
//					"p.operationStatus = :status ORDER BY p.operationDate DESC ";
//		} else if (mobilePatient != null) {
//			queryStr = "SELECT p FROM ProductDispensation p " +
//					"WHERE " +
//					"p.mobilePatientDispensationInfo.mobilePatient = :mobilePatient AND " +
//					"p.mobilePatientDispensationInfo.productRegimen IS NOT NULL AND " +
//					"p.productProgram = :program AND p.voided = false AND p.location = :location AND " +
//					"p.operationStatus = :status ORDER BY p.operationDate DESC ";
//		}
//		Query query = sessionFactory.getCurrentSession().createQuery(queryStr)
//				.setParameter("program", productProgram)
//				.setParameter("status", OperationStatus.VALIDATED)
//				.setParameter("location", location);
//		if (patient != null) {
//			query.setParameter("patient", patient);
//		} else {
//			query.setParameter("mobilePatient", mobilePatient);
//		}
//
//		return (ProductDispensation) query.setMaxResults(1).uniqueResult();
	}

	@Override
	public ProductDispensation getLastProductDispensationByPatient(String identifier, ProductProgram productProgram, Location location, Date dispensationDate) throws HibernateException {
		Patient patient = getPatientByIdentifier(identifier);
		MobilePatient mobilePatient = getOneMobilePatientByIdentifier(identifier);
		String queryStr = "";
		if (patient != null) {
			queryStr = "SELECT p FROM ProductDispensation p " +
					"WHERE " +
					"p.encounter.patient = :patient AND " +
					"p.productProgram = :program AND p.voided = false AND p.location = :location AND " +
					"p.operationStatus = :status AND p.operationDate < :operationDate ORDER BY p.operationDate DESC ";
		} else if (mobilePatient != null) {
			queryStr = "SELECT p FROM ProductDispensation p " +
					"WHERE " +
					"p.mobilePatientDispensationInfo.mobilePatient = :mobilePatient AND " +
					"p.productProgram = :program AND p.voided = false AND p.location = :location AND " +
					"p.operationStatus = :status AND p.operationDate < :operationDate ORDER BY p.operationDate DESC ";
		}
		Query query = sessionFactory.getCurrentSession().createQuery(queryStr)
				.setParameter("program", productProgram)
				.setParameter("status", OperationStatus.VALIDATED)
				.setParameter("location", location)
				.setParameter("operationDate", dispensationDate);

		if (patient != null) {
			query.setParameter("patient", patient);
		} else {
			query.setParameter("mobilePatient", mobilePatient);
		}
		System.out.println("-------------------------- Before return in query getLastProductDispensationByPatient ");
		return (ProductDispensation) query.setMaxResults(1).uniqueResult();
	}

	@Override
	public DispensationResultDTO getDispensationResult(Date startDate, Date endDate, Location location) {
		String sqlQuery = "SELECT " +
				"       COUNT(IF(age >= 15, age, NULL)) adult, " +
				"       COUNT(IF(age < 15, age, NULL)) child, " +
				"       COUNT(IF(gender = 'M', gender, NULL)) male, " +
				"       COUNT(IF(gender = 'F', gender, NULL)) female, " +
				"       COUNT(IF(patientType = 'ON_SITE', patientType, NULL)) onSite, " +
				"       COUNT(IF(patientType = 'MOBILE', patientType, NULL)) mobile, " +
				"       COUNT(IF(goal = 'PEC', patientType, NULL)) pec, " +
				"       COUNT(IF(goal = 'NOT_APPLICABLE', patientType, NULL)) notApplicable, " +
				"       COUNT(IF(goal = 'PTME', patientType, NULL)) ptme, " +
				"       COUNT(IF(goal = 'AES', patientType, NULL)) aes, " +
				"       COUNT(IF(goal = 'PREP', patientType, NULL)) prep, " +
				"       COUNT(*) total " +
				"FROM ( " +
				"  SELECT " +
				"      IF(patientAge IS NOT NULL, patientAge, mobileAge) age, " +
				"      IF(patientGender IS NOT NULL, patientGender, mobileGender) gender, " +
				"      IF(patientGoal IS NOT NULL, patientGoal, mobileGoal) goal, " +
				"      IF(patientAge IS NOT NULL OR patient_type = 0, 'ON_SITE', " +
				"         IF(patient_type = 1, 'MOBILE', " +
				"            IF(patient_type = 2, 'OTHER_HIV', NULL))) patientType " +
				"  FROM ( " +
				"           SELECT " +
				"               FLOOR(DATEDIFF(NOW(), p.birthdate) / 365.25) patientAge, " +
				"               pmp.age mobileAge, " +
				"               p.gender patientGender, " +
				"               pmp.gender mobileGender, " +
				"               IF(pmpdi.goal = 0, 'NOT_APPLICABLE', " +
				"                  IF(pmpdi.goal = 1, 'PEC', " +
				"                     IF(pmpdi.goal = 2, 'PTME', " +
				"                        IF(pmpdi.goal IN (3,5,6,7), 'AES', " +
				"                           IF(pmpdi.goal = 4, 'PREP', NULL))))) mobileGoal, " +
				"               o.value_text patientGoal, " +
				"               patient_type " +
				"           FROM pharmacy_product_operation ppo " +
				"                    INNER JOIN pharmacy_product_dispensation ppd ON ppo.product_operation_id = ppd.product_operation_id " +
				"                    LEFT JOIN pharmacy_mobile_patient_dispensation_info pmpdi ON ppd.product_operation_id = pmpdi.mobile_dispensation_info_id " +
				"                    LEFT JOIN pharmacy_mobile_patient pmp ON pmpdi.mobile_patient_id = pmp.mobile_patient_id " +
				"                    LEFT JOIN encounter e ON ppd.encounter_id = e.encounter_id " +
				"                    LEFT JOIN person p ON p.person_id = e.patient_id " +
				"                    LEFT JOIN (SELECT * FROM obs WHERE concept_id = 165009 AND voided = 0) o ON e.encounter_id = o.encounter_id " +
				"           WHERE ppo.operation_date BETWEEN :startDate AND :endDate AND ppo.location_id = :locationId AND ppo.operation_status = 2 " +
				"       ) _ " +
				"    ) _1";
//		Type StatusEnum = sessionFactory.getTypeHelper().custom(OperationStatus.class);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("onSite", StandardBasicTypes.INTEGER)
				.addScalar("mobile", StandardBasicTypes.INTEGER)
				.addScalar("adult", StandardBasicTypes.INTEGER)
				.addScalar("child", StandardBasicTypes.INTEGER)
				.addScalar("male", StandardBasicTypes.INTEGER)
				.addScalar("female", StandardBasicTypes.INTEGER)
				.addScalar("pec", StandardBasicTypes.INTEGER)
				.addScalar("ptme", StandardBasicTypes.INTEGER)
				.addScalar("aes", StandardBasicTypes.INTEGER)
				.addScalar("prep", StandardBasicTypes.INTEGER)
				.addScalar("notApplicable", StandardBasicTypes.INTEGER)
				.addScalar("total", StandardBasicTypes.INTEGER)
				.setParameter("locationId", location.getLocationId())
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setResultTransformer(new AliasToBeanResultTransformer(DispensationResultDTO.class));
		try {
			return (DispensationResultDTO) query.uniqueResult();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public DispensationTransformationResultDTO transformDispensation(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MobilePatient.class);
		List<MobilePatient> mobilePatients = criteria.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("patientType", PatientType.MOBILE)).list();

		DispensationTransformationResultDTO transformationResultDTO = new DispensationTransformationResultDTO();
		transformationResultDTO.setTotalPatient(mobilePatients.size());
		for (MobilePatient mobilePatient : mobilePatients) {
			Integer patientTotalDispensation = 0;
			Integer patientTotalDispensationTransformed = 0;
			Patient patient = getPatientByIdentifier(mobilePatient.getIdentifier());
			if (patient != null) {
				Set<MobilePatientDispensationInfo> dispensationInfos = mobilePatient.getMobilePatientDispensationInfos();
				patientTotalDispensation = dispensationInfos.size();
				transformationResultDTO.setTotalDispensation(transformationResultDTO.getTotalDispensation() + dispensationInfos.size());
				transform(transformationResultDTO, patient, dispensationInfos, patientTotalDispensationTransformed);
				if (patientTotalDispensation.equals(patientTotalDispensationTransformed)){
					removeMobilePatient(mobilePatient);
				}
			}
		}

		return transformationResultDTO;
	}

	private void transform(DispensationTransformationResultDTO transformationResultDTO, Patient patient, Set<MobilePatientDispensationInfo> dispensationInfos, Integer patientTotalDispensationTransformed) {
		for (MobilePatientDispensationInfo dispensationInfo : dispensationInfos) {
			if (dispensationInfo.getDispensation().getOperationStatus().equals(OperationStatus.VALIDATED)) {
				Encounter encounter = createEncounter(patient, dispensationInfo);
				if (Context.getEncounterService().saveEncounter(encounter) != null) {
					transformationResultDTO.setTransformed(transformationResultDTO.getTransformed() + 1);
					patientTotalDispensationTransformed ++;
					removeMobilePatientInfo(dispensationInfo);
				} else {
					transformationResultDTO.setNotTransformed(transformationResultDTO.getNotTransformed() + 1);
				}
			} else {
				transformationResultDTO.setNotTransformed(transformationResultDTO.getNotTransformed() + 1);
			}
		}
	}

	@Override
	public DispensationTransformationResultDTO transformPatientDispensation(MobilePatient mobilePatient) {
		DispensationTransformationResultDTO transformationResultDTO = new DispensationTransformationResultDTO();
		transformationResultDTO.setTotalPatient(1);
		Patient patient = getPatientByIdentifier(mobilePatient.getIdentifier());

		if (patient != null) {

			Date transferDate = transferDate(patient, mobilePatient.getLocation());
			Date admissionDate = admissionDate(patient, mobilePatient.getLocation());

			if (admissionDate != null) {
				Integer patientTotalDispensationTransformed = 0;
				Set<MobilePatientDispensationInfo> dispensationInfos = mobilePatient.getMobilePatientDispensationInfos();
				Set<MobilePatientDispensationInfo> tempInfos = mobilePatient.getMobilePatientDispensationInfos();
				for (MobilePatientDispensationInfo info : tempInfos) {
					if (info.getDispensation().getOperationDate().before(admissionDate)) {
						dispensationInfos.remove(info);
					} else if (transferDate != null &&
							(info.getDispensation().getOperationDate().after(transferDate))) {
						dispensationInfos.remove(info);
					}
				}
				int patientTotalDispensation = tempInfos.size();

				transformationResultDTO.setTotalDispensation(patientTotalDispensation);
				transform(transformationResultDTO, patient, dispensationInfos, patientTotalDispensationTransformed);

				removeMobilePatient(mobilePatient);
			}
		}

		return transformationResultDTO;
	}

	@Override
	public Integer countPatientToTransform(Location location) {
		int quantity = 0;
		List<MobilePatient> mobilePatients = getAllMobilePatients(location);
		for (MobilePatient mobilePatient : mobilePatients) {
			Patient patient = getPatientByIdentifier(mobilePatient.getIdentifier());
			if (patient != null) {
				quantity = quantity + 1;
			}
		}
		return quantity;
	}

	private Encounter createEncounter(Patient patient, MobilePatientDispensationInfo dispensationInfo) {
		Encounter encounter = new Encounter();
		encounter.setEncounterDatetime(dispensationInfo.getDispensation().getOperationDate());
		encounter.setPatient(patient);
		encounter.setLocation(OperationUtils.getUserLocation());
		encounter.setEncounterType(Context.getEncounterService().getEncounterType(17));
		encounter.addProvider(Context.getEncounterService().getEncounterRole(1), dispensationInfo.getProvider());

		encounter.setObs(OperationUtils.getDispensationObsList(dispensationInfo, patient));
		return encounter;
	}

	@Override
	public Boolean isTransferred(Patient patient, Location location) {
//		Patient transferredPatient = (Patient) sessionFactory.getCurrentSession().createQuery(
//				"SELECT p FROM Patient p, Obs o " +
//						"WHERE p.patient = :patient AND o.person.personId = p.patient.patientId AND o.concept.conceptId = 164595 AND " +
//						" o.valueDatetime >= (SELECT MAX(e.encounterDatetime) FROM Encounter e WHERE e.patient = :patient AND e.encounterType.name = 'PEC - Ouverture de dossier' AND e.voided = false AND e.location = :location GROUP BY e.patient) AND " +
//						" o.voided = false AND o.location = :location"
//		)
//				.setParameter("patient", patient)
//				.setParameter("location", location).uniqueResult();
		return transferDate(patient, location) != null;
	}

	@Override
	public Date transferDate(Patient patient, Location location) {
		Obs obs = (Obs) sessionFactory.getCurrentSession().createQuery(
				"SELECT o FROM Obs o " +
						"WHERE o.person = :patient AND o.concept.conceptId = 164595 AND " +
						" o.valueDatetime >= (SELECT MAX(e.encounterDatetime) FROM Encounter e WHERE e.patient = :patient AND e.encounterType.uuid = '8d5b27bc-c2cc-11de-8d13-0010c6dffd0f' AND e.voided = false AND e.location = :location GROUP BY e.patient) AND " +
						" o.voided = false AND o.location = :location"
		)
				.setParameter("patient", patient)
				.setParameter("location", location).uniqueResult();
		if (obs != null) {
			return obs.getValueDate() != null ? obs.getValueDate() : obs.getValueDatetime();
		}
		return null;
	}

	@Override
	public Boolean isDead(Patient patient, Location location) {
//		Patient deadPatient = (Patient) sessionFactory.getCurrentSession().createQuery("SELECT p FROM Patient p, Obs o " +
//				"WHERE o.person.personId = p.patient.patientId AND o.concept.conceptId = 1543 AND  p.patient = :patient AND " +
//				"o.voided = false AND o.location = :location")
//				.setParameter("patient", patient)
//				.setParameter("location", location)
//				.uniqueResult();
		return deathDate(patient, location) != null;
	}

	@Override
	public Date deathDate(Patient patient, Location location) {
		Obs obs = (Obs) sessionFactory.getCurrentSession().createQuery("SELECT o FROM Obs o " +
				"WHERE o.person = :patient AND o.concept.conceptId = 1543 AND " +
				"o.voided = false AND o.location = :location")
				.setParameter("patient", patient)
				.setParameter("location", location)
				.uniqueResult();
		if (obs != null) {
			return obs.getValueDate() != null ? obs.getValueDate() : obs.getValueDatetime();
		}
		return null;
	}

	@Override
	public Date admissionDate(Patient patient, Location location) {
		Encounter encounter = (Encounter) sessionFactory.getCurrentSession().createQuery(
				"SELECT e FROM Encounter e " +
						"WHERE e.patient = :patient AND " +
						" e.encounterDatetime >= (SELECT MAX(em.encounterDatetime) FROM Encounter em WHERE em.patient = :patient AND em.encounterType.uuid = '8d5b27bc-c2cc-11de-8d13-0010c6dffd0f' AND em.voided = false AND em.location = :location GROUP BY em.patient) AND " +
						" e.voided = false AND e.location = :location AND e.encounterType.uuid = '8d5b27bc-c2cc-11de-8d13-0010c6dffd0f'"
		)
				.setParameter("patient", patient)
				.setParameter("location", location).uniqueResult();
		return encounter != null ? encounter.getEncounterDatetime() : null;
	}
}
