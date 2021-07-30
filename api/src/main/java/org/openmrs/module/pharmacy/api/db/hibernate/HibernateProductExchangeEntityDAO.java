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
import org.openmrs.module.pharmacy.entities.ProductExchangeEntity;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductExchangeEntityDAO;

import java.util.List;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
public class HibernateProductExchangeEntityDAO implements ProductExchangeEntityDAO {
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
	public List<ProductExchangeEntity> getAllProductExchange() {
		return sessionFactory.getCurrentSession().createCriteria(ProductExchangeEntity.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductExchangeEntity> getAllProductExchange(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductExchangeEntity.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@Override
	public ProductExchangeEntity saveProductExchange(ProductExchangeEntity productExchange) {
		sessionFactory.getCurrentSession().saveOrUpdate(productExchange);
		return productExchange;
	}

	@Override
	public ProductExchangeEntity editProductExchange(ProductExchangeEntity productExchange) {
		sessionFactory.getCurrentSession().saveOrUpdate(productExchange);
		return productExchange;
	}

	@Override
	public void removeProductExchange(ProductExchangeEntity productExchangeEntity) {
		sessionFactory.getCurrentSession().delete(productExchangeEntity);
	}

	@Override
	public ProductExchangeEntity getOneProductExchangeById(Integer productExchangeId) {
		return (ProductExchangeEntity) sessionFactory.getCurrentSession().get(ProductExchangeEntity.class, productExchangeId);
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductExchangeEntity.class);
//		return (ProductExchangeEntity) criteria.add(Restrictions.eq("productExchangeId", productExchangeId)).uniqueResult();
	}

	@Override
	public ProductExchangeEntity getOneProductExchangeByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductExchangeEntity.class);
		return (ProductExchangeEntity) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@Override
	public ProductExchangeEntity getOneProductExchangeByName(String name) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductExchangeEntity.class);
		return (ProductExchangeEntity) criteria.add(Restrictions.eq("name", name)).uniqueResult();
	}

}
