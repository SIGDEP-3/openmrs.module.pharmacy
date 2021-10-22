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
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pharmacy.entities.ProductPrice;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductPriceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
@Repository
public class HibernateProductPriceDAO implements ProductPriceDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DbSessionFactory sessionFactory;

//	public void setSessionFactory(DbSessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}

	@Override
	public ProductPrice saveProductPrice(ProductPrice productPrice) {
		sessionFactory.getCurrentSession().saveOrUpdate(productPrice);
		return productPrice;
	}

	@Override
	public void removeProductPrice(ProductPrice productPrice) {
		sessionFactory.getCurrentSession().delete(productPrice);
	}

	@Override
	public ProductPrice getOneProductPriceById(Integer productPriceId) {
		return (ProductPrice) sessionFactory.getCurrentSession().get(ProductPrice.class, productPriceId);
	}

	@Override
	public ProductPrice getOneProductPriceByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductPrice.class);
		return (ProductPrice) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@Override
	public ProductPrice getOneProductPriceByProductProgramId(Integer productProgramId) {
		return (ProductPrice) sessionFactory.getCurrentSession().get(ProductPrice.class, productProgramId);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductPrice> getAllProductPriceByStatus(Boolean status) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductPrice.class);
		return (List<ProductPrice>) criteria.add(Restrictions.eq("true", status));
	}

	@Override
	public ProductPrice getOneActiveProductPriceByProductAndProductProgram(Integer productProgramId) {
		return (ProductPrice) sessionFactory.getCurrentSession().get(ProductPrice.class, productProgramId);
	}

	@Override
	public ProductPrice getOneActiveProductPriceByProductAndProductProgram() {
		return (ProductPrice) sessionFactory.getCurrentSession().createCriteria(ProductPrice.class)
				.add(Restrictions.eq("product", null)).setMaxResults(1).uniqueResult();
	}

	@Override
	public ProductPrice getOneProductPriceByProductId(Integer productId) {
		return (ProductPrice) sessionFactory.getCurrentSession().get(ProductPrice.class, productId);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ProductPrice> getAllProductPrices() {
		return sessionFactory.getCurrentSession().createCriteria(ProductPrice.class).list();
	}
}
