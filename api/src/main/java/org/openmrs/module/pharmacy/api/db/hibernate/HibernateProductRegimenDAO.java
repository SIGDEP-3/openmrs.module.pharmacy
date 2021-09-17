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
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pharmacy.entities.ProductRegimen;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductRegimenDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
@Repository
public class HibernateProductRegimenDAO implements ProductRegimenDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DbSessionFactory sessionFactory;

	@Override
	public ProductRegimen saveProductRegimen(ProductRegimen productRegimen) {
		sessionFactory.getCurrentSession().saveOrUpdate(productRegimen);
		return productRegimen;
	}

	@Override
	public void removeProductRegimen(ProductRegimen productRegimen) {
		sessionFactory.getCurrentSession().delete(productRegimen);
	}

	@Override
	public ProductRegimen getOneProductRegimenById(Integer regimenId) {
		return (ProductRegimen) sessionFactory.getCurrentSession().get(ProductRegimen.class, regimenId);
	}

	@Override
	public ProductRegimen getOneProductRegimenByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductRegimen.class);
		return (ProductRegimen) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@Override
	public ProductRegimen getOneProductRegimenByConceptName(String name) {
		Concept concept = Context.getConceptService().getConceptByName(name);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductRegimen.class);
		return (ProductRegimen) criteria.add(Restrictions.eq("concept", concept)).uniqueResult();
	}

	@Override
	public ProductRegimen getOneProductRegimenByConceptId(Integer conceptId) {
		Concept concept = Context.getConceptService().getConcept(conceptId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductRegimen.class);
		return (ProductRegimen) criteria.add(Restrictions.eq("concept", concept)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductRegimen> getAllProductRegimen() {
		return sessionFactory.getCurrentSession().createCriteria(ProductRegimen.class).list();
	}

}
