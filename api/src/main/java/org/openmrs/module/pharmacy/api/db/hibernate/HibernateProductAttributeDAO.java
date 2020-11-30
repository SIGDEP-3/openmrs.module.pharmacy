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
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Location;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductAttribute;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductAttributeDAO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
public class HibernateProductAttributeDAO implements ProductAttributeDAO {
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
	public List<ProductAttribute> getAllProductAttributes(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttribute> getAllProductAttributes(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return criteria
				.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttribute> getAllProductAttributes(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return criteria
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttribute> getAllProductAttributes(Product product) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return criteria.add(Restrictions.eq("product", product)).list();
	}

	@Override
	public ProductAttribute getOneProductAttributeById(Integer id) {
		return (ProductAttribute) sessionFactory.getCurrentSession().get(ProductAttribute.class, id);
	}

	@Override
	public ProductAttribute saveProductAttribute(ProductAttribute productAttribute) {
		sessionFactory.getCurrentSession().saveOrUpdate(productAttribute);
		return productAttribute;
	}

	@Override
	public ProductAttribute editProductAttribute(ProductAttribute productAttribute) {
		sessionFactory.getCurrentSession().saveOrUpdate(productAttribute);
		return productAttribute;
	}

	@Override
	public void removeProductAttribute(ProductAttribute productAttribute) {
		sessionFactory.getCurrentSession().delete(productAttribute);
	}

	@Override
	public ProductAttribute getOneProductAttributeByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return (ProductAttribute) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@Override
	public ProductAttribute getOneProductAttributeByBatchNumber(String batchNumber, Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return (ProductAttribute) criteria.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("location", location)).uniqueResult();
	}

	@Override
	public ProductAttribute getOneProductAttributeByBatchNumberAndExpiryDate(String batchNumber, Date expiryDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return (ProductAttribute) criteria.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("expiryDate", expiryDate)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer purgeUnusedAttributes() {
		List<ProductAttribute> productAttributes = sessionFactory.getCurrentSession().createQuery("" +
				"FROM ProductAttribute p WHERE p.productAttributeId NOT IN (SELECT pf.productAttribute.productAttributeId FROM ProductAttributeFlux pf)").list();
		for (ProductAttribute attribute : productAttributes) {
			removeProductAttribute(attribute);
		}
		return productAttributes.size();
	}
}
