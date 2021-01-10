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
import org.openmrs.module.pharmacy.ProductSupplier;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductSupplierDAO;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.List;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
public class HibernateProductSupplierDAO implements ProductSupplierDAO {
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
	public List<ProductSupplier> getAllProductSuppliers() {
		List<ProductSupplier> suppliers = sessionFactory.getCurrentSession().createCriteria(ProductSupplier.class).list();
		if (!OperationUtils.isDirectClient(OperationUtils.getUserLocation())) {
			suppliers.remove(getOneProductSupplierById(1));
		}
		return suppliers;
	}

	@Override
	public ProductSupplier saveProductSupplier(ProductSupplier productSupplier) {
		sessionFactory.getCurrentSession().saveOrUpdate(productSupplier);
		return productSupplier;
	}

	@Override
	public ProductSupplier editProductSupplier(ProductSupplier productSupplier) {
		sessionFactory.getCurrentSession().saveOrUpdate(productSupplier);
		return productSupplier;
	}

	@Override
	public void removeProductSupplier(ProductSupplier productSupplier) {
		sessionFactory.getCurrentSession().delete(productSupplier);
	}

	@Override
	public ProductSupplier getOneProductSupplierById(Integer productSupplierId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductSupplier.class);
		return (ProductSupplier) criteria.add(Restrictions.eq("productSupplierId", productSupplierId)).uniqueResult();
	}

	@Override
	public ProductSupplier getOneProductSupplierByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductSupplier.class);
		return (ProductSupplier) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@Override
	public ProductSupplier getOneProductSupplierByName(String name) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductSupplier.class);
		return (ProductSupplier) criteria.add(Restrictions.eq("name", name)).uniqueResult();
	}

}
