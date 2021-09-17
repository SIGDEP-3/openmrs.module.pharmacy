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
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductProgramDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
@Repository
public class HibernateProductProgramDAO implements ProductProgramDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DbSessionFactory sessionFactory;

	@Override
	public ProductProgram saveProductProgram(ProductProgram productProgram) {
		sessionFactory.getCurrentSession().saveOrUpdate(productProgram);
		return productProgram;
	}

	@Override
	public void removeProductProgram(ProductProgram productProgram) {
		sessionFactory.getCurrentSession().delete(productProgram);
	}

	@Override
	public ProductProgram getOneProductProgramById(Integer productProgramId) {
		return (ProductProgram) sessionFactory.getCurrentSession().get(ProductProgram.class, productProgramId);
	}

	@Override
	public ProductProgram getOneProductProgramByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductProgram.class);
		return (ProductProgram) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@Override
	public ProductProgram getOneProductProgramByName(String name) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductProgram.class);
		return (ProductProgram) criteria.add(Restrictions.eq("name", name)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductProgram> getAllProductProgram() {
//		if (OperationUtils.getUserLocationPrograms().size() != 0) {
//			return OperationUtils.getUserLocationPrograms();
//		}
		return sessionFactory.getCurrentSession().createCriteria(ProductProgram.class).list();
	}

}
