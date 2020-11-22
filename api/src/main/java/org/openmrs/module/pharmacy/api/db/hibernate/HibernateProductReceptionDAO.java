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
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.openmrs.Location;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductReceptionDAO;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
public class HibernateProductReceptionDAO implements ProductReceptionDAO {
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
	public List<ProductReception> getAllProductReceptions(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReception.class);
		return criteria.add(Restrictions.eq("location", location)).
				add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReception> getAllProductReceptions(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReception.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.between("operationDate", operationStartDate, operationEndDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReception> getAllProductReceptions(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReception.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReception> getAllProductReceptions(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReception.class);
		return criteria.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@Override
	public ProductReception getOneProductReceptionById(Integer id) {
		return (ProductReception) sessionFactory.getCurrentSession().get(ProductReception.class, id);
	}

	@Override
	public ProductReception saveProductReception(ProductReception productReception) {
		sessionFactory.getCurrentSession().saveOrUpdate(productReception);
		return productReception;
	}

	@Override
	public ProductReception editProductReception(ProductReception productReception) {
		sessionFactory.getCurrentSession().saveOrUpdate(productReception);
		return productReception;
	}

	@Override
	public void removeProductReception(ProductReception productReception) {
		sessionFactory.getCurrentSession().delete(productReception);
	}

	@Override
	public ProductReception getOneProductReceptionByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReception.class);
		return (ProductReception) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReceptionFluxDTO> getProductReceptionFluxDTOs(ProductReception productReception) {
		String sqlQuery =
				"SELECT " +
						"ppaf.product_attribute_flux_id as productAttributeFluxId, " +
						"ppr.product_operation_id as productOperationId, " +
						"pp.product_id as productId, " +
						"pp.code as code, " +
						"pp.retail_name as retailName, " +
						"pp.wholesale_name as wholesaleName, " +
						"ppu.name as retailUnit, " +
						"ppu2.name as wholesaleUnit, " +
						"ppa.batch_number as batchNumber, " +
						"ppa.expiry_date as expiryDate, " +
						"ppaf.quantity as quantity, " +
						"ppaof.quantity as quantityToDeliver, " +
						"ppaf.observation as observation, " +
						"ppaf.date_created as dateCreated " +
						"FROM pharmacy_product_reception ppr " +
						"LEFT JOIN pharmacy_product_operation ppo on ppr.product_operation_id = ppo.product_operation_id " +
						"LEFT JOIN pharmacy_product_attribute_flux ppaf on ppo.product_operation_id = ppaf.operation_id " +
						"LEFT JOIN pharmacy_product_attribute ppa on ppaf.product_attribute_id = ppa.product_attribute_id " +
						"LEFT JOIN pharmacy_product_attribute_other_flux ppaof on ppo.product_operation_id = ppaof.operation_id AND ppaof.product_attribute_id = ppa.product_attribute_id " +
						"LEFT JOIN pharmacy_product pp ON ppa.product_id = pp.product_id " +
						"LEFT JOIN pharmacy_product_unit ppu on pp.product_retail_unit = ppu.product_unit_id " +
						"LEFT JOIN pharmacy_product_unit ppu2 on pp.product_wholesale_unit = ppu2.product_unit_id " +
						"WHERE ppr.product_operation_id = :productOperationId AND product_attribute_flux_id IS NOT NULL " +
						"ORDER BY ppaf.date_created DESC ";

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("productAttributeFluxId", StandardBasicTypes.INTEGER)
				.addScalar("productOperationId", StandardBasicTypes.INTEGER)
				.addScalar("productId", StandardBasicTypes.INTEGER)
				.addScalar("code", StandardBasicTypes.STRING)
				.addScalar("retailName", StandardBasicTypes.STRING)
				.addScalar("wholesaleName", StandardBasicTypes.STRING)
				.addScalar("retailUnit", StandardBasicTypes.STRING)
				.addScalar("wholesaleUnit", StandardBasicTypes.STRING)
				.addScalar("batchNumber", StandardBasicTypes.STRING)
				.addScalar("expiryDate", StandardBasicTypes.DATE)
				.addScalar("quantityToDeliver", StandardBasicTypes.INTEGER)
				.addScalar("quantity", StandardBasicTypes.INTEGER)
				.addScalar("observation", StandardBasicTypes.STRING)
				.addScalar("dateCreated", StandardBasicTypes.DATE)
				.setParameter("productOperationId", productReception.getProductOperationId())
				.setResultTransformer(new AliasToBeanResultTransformer(ProductReceptionFluxDTO.class));
		try {
			return query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
