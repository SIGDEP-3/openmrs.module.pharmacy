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
import org.openmrs.module.pharmacy.entities.ProductUnit;
import org.openmrs.module.pharmacy.api.db.ProductUnitDAO;

import java.util.List;

/**
 * It is a default implementation of  {@link ProductUnitDAO}.
 */
public class HibernateProductUnitDAO implements ProductUnitDAO {
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

	/******* PRODUCTS UNITS *******/

	@Override
	public ProductUnit saveProductUnit(ProductUnit productUnit) {
		sessionFactory.getCurrentSession().saveOrUpdate(productUnit);
		return productUnit;
	}

	@Override
	public ProductUnit editProductUnit(ProductUnit productUnit) {
		sessionFactory.getCurrentSession().saveOrUpdate(productUnit);
		return productUnit;
	}

	@Override
	public void removeProductUnit(ProductUnit productUnit) {
		sessionFactory.getCurrentSession().delete(productUnit);
	}

	@Override
	public ProductUnit getOneProductUnitById(Integer productUnitId) {
		return (ProductUnit) sessionFactory.getCurrentSession().get(ProductUnit.class, productUnitId);
	}

	@Override
	public ProductUnit getOneProductUnitByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductUnit.class);
		return (ProductUnit) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@Override
	public ProductUnit getOneProductUnitByName(String name) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductUnit.class);
		return (ProductUnit) criteria.add(Restrictions.eq("name", name)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductUnit> getAllProductUnit() {
		return sessionFactory.getCurrentSession().createCriteria(ProductUnit.class).list();
	}

}
