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
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.module.pharmacy.MobilePatient;
import org.openmrs.module.pharmacy.MobilePatientDispensationInfo;
import org.openmrs.module.pharmacy.ProductDispensation;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.db.ProductDispensationDAO;
import org.openmrs.module.pharmacy.models.ProductDispensationFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public ProductDispensation getOneProductDispensationByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return (ProductDispensation) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDispensationFluxDTO> getProductDispensationFluxDTOs(ProductDispensation productDispensation) {
//		String sqlQuery =
//				"SELECT " +
//						"    ppaf.product_attribute_flux_id as productAttributeFluxId, " +
//						"    pp.code as code, " +
//						"    pp.retail_name as retailName, " +
//						"    pp.wholesale_name as wholesaleName, " +
//						"    ppu.name as retailUnit, " +
//						"    ppu2.name as wholesaleUnit, " +
//						"    ppa.batch_number as batchNumber, " +
//						"    ppa.expiry_date as expiryDate, " +
//						"    ppaf.quantity as physicalQuantity, " +
//						"    ppo.product_operation_id as operationId, " +
//						"    ppas.quantity_in_stock as theoreticalQuantity, " +
//						"    ppaf.observation as observation, " +
//						"    IF(ppaf.date_created IS NOT NULL, ppaf.date_created, ppas.date_created) as dateCreated, " +
//						"    ppaf.status " +
//						" " +
//						"FROM (SELECT product_attribute_id, product_id, batch_number, expiry_date FROM pharmacy_product_attribute) ppa " +
//						"LEFT JOIN (SELECT * FROM pharmacy_product_attribute_flux WHERE operation_id = :productOperationId) ppaf ON ppaf.product_attribute_id = ppa.product_attribute_id " +
//						"LEFT JOIN (SELECT * FROM pharmacy_product_operation WHERE operation_status = 2 AND product_operation_id = :productOperationId) ppo ON ppaf.operation_id = ppo.product_operation_id " +
//						"LEFT JOIN pharmacy_product_attribute_stock ppas ON ppas.product_attribute_id = ppa.product_attribute_id " +
//						"LEFT JOIN pharmacy_product pp ON ppa.product_id = pp.product_id " +
//						"LEFT JOIN pharmacy_product_unit ppu ON pp.product_retail_unit = ppu.product_unit_id " +
//						"LEFT JOIN pharmacy_product_unit ppu2 ON pp.product_wholesale_unit = ppu2.product_unit_id " +
//						"WHERE NOT (ppaf.uuid IS NULL AND ppas.uuid IS NULL) " +
//						"ORDER BY ppaf.date_created, ppas.date_created DESC";
//
//		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
//				.addScalar("productAttributeFluxId", StandardBasicTypes.INTEGER)
////				.addScalar("operationId", StandardBasicTypes.INTEGER)
////				.addScalar("inventoryId", StandardBasicTypes.INTEGER)
////				.addScalar("productId", StandardBasicTypes.INTEGER)
//				.addScalar("code", StandardBasicTypes.STRING)
//				.addScalar("retailName", StandardBasicTypes.STRING)
//				.addScalar("wholesaleName", StandardBasicTypes.STRING)
//				.addScalar("retailUnit", StandardBasicTypes.STRING)
//				.addScalar("wholesaleUnit", StandardBasicTypes.STRING)
//				.addScalar("batchNumber", StandardBasicTypes.STRING)
//				.addScalar("expiryDate", StandardBasicTypes.DATE)
//				.addScalar("physicalQuantity", StandardBasicTypes.INTEGER)
//				.addScalar("theoreticalQuantity", StandardBasicTypes.INTEGER)
//				.addScalar("observation", StandardBasicTypes.STRING)
//				.addScalar("dateCreated", StandardBasicTypes.DATE)
//				.setParameter("productOperationId", productDispensation.getProductOperationId())
//				.setResultTransformer(new AliasToBeanResultTransformer(ProductDispensationFluxDTO.class));
//		try {
//			return query.list();
//		} catch (HibernateException e) {
//			System.out.println(e.getMessage());
//		}
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

	@Override
	public ProductDispensation getLastProductDispensation(Location location, ProductProgram productProgram) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return (ProductDispensation) criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("productProgram", productProgram))
				.addOrder(Order.desc("operationDate")).setMaxResults(1).uniqueResult();
	}

	@Override
	public ProductDispensation getLastProductDispensationByDate(Location location, ProductProgram productProgram, Date inventoryDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductDispensation.class);
		return (ProductDispensation) criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("productProgram", productProgram))
				.add(Restrictions.lt("operationDate", inventoryDate))
				.addOrder(Order.desc("operationDate")).setMaxResults(1).uniqueResult();
	}

}
