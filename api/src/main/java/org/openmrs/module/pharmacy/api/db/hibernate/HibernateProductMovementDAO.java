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
import org.openmrs.api.APIException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pharmacy.entities.ProductMovementEntry;
import org.openmrs.module.pharmacy.entities.ProductMovementOut;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.api.db.ProductMovementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of  {@link ProductMovementDAO}.
 */
@Repository
public class HibernateProductMovementDAO implements ProductMovementDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DbSessionFactory sessionFactory;

	/********* PRODUCT MOVEMENT ENTRY ***********/

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductMovementEntry> getAllProductMovementEntry(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementEntry.class);
		return criteria.add(Restrictions.eq("location", location)).
				add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	public List<ProductMovementEntry> getAllProductMovementEntry(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementEntry.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.between("operationDate", operationStartDate, operationEndDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductMovementEntry> getAllProductMovementEntry(ProductProgram productProgram, Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementEntry.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("productProgram", productProgram))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.between("operationDate", operationStartDate, operationEndDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductMovementEntry> getAllProductMovementEntry(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementEntry.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductMovementEntry> getAllProductMovementEntry(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementEntry.class);
		return criteria.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@Override
	public ProductMovementEntry getOneProductMovementEntryById(Integer id) {
		return (ProductMovementEntry) sessionFactory.getCurrentSession().get(ProductMovementEntry.class, id);
	}

	@Override
	public ProductMovementEntry saveProductMovementEntry(ProductMovementEntry productMovementEntry) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(productMovementEntry);
		return productMovementEntry;
	}

	@Override
	public ProductMovementEntry editProductMovementEntry(ProductMovementEntry productMovementEntry) {
		sessionFactory.getCurrentSession().saveOrUpdate(productMovementEntry);
		return productMovementEntry;
	}

	@Override
	public void removeProductMovementEntry(ProductMovementEntry productMovementEntry) {
		sessionFactory.getCurrentSession().delete(productMovementEntry);
	}

	@Override
	public ProductMovementEntry getOneProductMovementEntryByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementEntry.class);
		return (ProductMovementEntry) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}


	/********* PRODUCT MOVEMENT OUT ***********/

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductMovementOut> getAllProductMovementOut(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementOut.class);
		return criteria.add(Restrictions.eq("location", location)).
				add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	public List<ProductMovementOut> getAllProductMovementOut(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementOut.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.between("operationDate", operationStartDate, operationEndDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductMovementOut> getAllProductMovementOut(ProductProgram productProgram, Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementOut.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("productProgram", productProgram))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.between("operationDate", operationStartDate, operationEndDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductMovementOut> getAllProductMovementOut(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementOut.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductMovementOut> getAllProductMovementOut(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementOut.class);
		return criteria.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@Override
	public ProductMovementOut getOneProductMovementOutById(Integer id) {
		return (ProductMovementOut) sessionFactory.getCurrentSession().get(ProductMovementOut.class, id);
	}

	@Override
	public ProductMovementOut saveProductMovementOut(ProductMovementOut productMovementOut) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(productMovementOut);
		return productMovementOut;
	}

	@Override
	public ProductMovementOut editProductMovementOut(ProductMovementOut productMovementOut) {
		sessionFactory.getCurrentSession().saveOrUpdate(productMovementOut);
		return productMovementOut;
	}

	@Override
	public void removeProductMovementOut(ProductMovementOut productMovementOut) {
		sessionFactory.getCurrentSession().delete(productMovementOut);
	}

	@Override
	public ProductMovementOut getOneProductMovementOutByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductMovementOut.class);
		return (ProductMovementOut) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

}
