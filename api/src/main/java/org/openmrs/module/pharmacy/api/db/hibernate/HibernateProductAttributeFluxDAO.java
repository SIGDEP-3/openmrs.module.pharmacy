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
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Location;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductAttributeFluxDAO;
import org.openmrs.module.pharmacy.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of  {@link ProductAttributeFluxDAO}.
 */
@Repository
public class HibernateProductAttributeFluxDAO implements ProductAttributeFluxDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DbSessionFactory sessionFactory;
	
	private DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location, Boolean includeVoided) {
		Criteria criteria = getSession().createCriteria(ProductAttributeFlux.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location) {
		Criteria criteria = getSession().createCriteria(ProductAttributeFlux.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxes(Boolean includeVoided) {
		Criteria criteria = getSession().createCriteria(ProductAttributeFlux.class);
		return criteria.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location, Date startDate, Date endDate, Boolean includeVoided) {
		Criteria criteria = getSession().createCriteria(ProductAttributeFlux.class);
		return criteria.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.eq("location", location))
				.add(Restrictions.between("operationDate", startDate, endDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxByAttribute(ProductAttribute productAttribute, Boolean includeVoided) {
		Criteria criteria = getSession().createCriteria(ProductAttributeFlux.class);
		return criteria
				.add(Restrictions.eq("productAttribute", productAttribute))
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxByOperation(ProductOperation productOperation, Boolean includeVoided) {
		Criteria criteria = getSession().createCriteria(ProductAttributeFlux.class);
		return criteria
				.add(Restrictions.eq("productOperation", productOperation))
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@Override
	public ProductAttributeFlux getOneProductAttributeFluxById(Integer id) {
		return (ProductAttributeFlux) getSession().get(ProductAttributeFlux.class, id);
	}

	@Override
	public ProductAttributeFlux getOneProductAttributeFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation) {
		Criteria criteria = getSession().createCriteria(ProductAttributeFlux.class);
		return (ProductAttributeFlux) criteria
				.add(Restrictions.eq("productAttribute", productAttribute))
				.add(Restrictions.eq("productOperation", productOperation)).uniqueResult();
	}

	@Override
	public ProductAttributeFlux saveProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
		System.out.println("-----------------------" + productAttributeFlux.toString());
		getSession().saveOrUpdate(productAttributeFlux);
		System.out.println("----------------------- Fin = " + productAttributeFlux.getProductAttributeFluxId());
		return productAttributeFlux;
	}

	@Override
	public ProductAttributeFlux editProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
		getSession().saveOrUpdate(productAttributeFlux);
		return productAttributeFlux;
	}

	@Override
	public void removeProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
		getSession().delete(productAttributeFlux);
	}

	@Override
	public ProductAttributeFlux getOneProductAttributeFluxByUuid(String uuid) {
		Criteria criteria = getSession().createCriteria(ProductAttributeFlux.class);
		return (ProductAttributeFlux) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxes(Location location) {
		Criteria criteria = getSession().createCriteria(ProductAttributeOtherFlux.class);
		return criteria.add(Restrictions.eq("productAttribute", location)).list();
	}

	@Override
	public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation, Location location) {
		Criteria criteria = getSession().createCriteria(ProductAttributeOtherFlux.class);
		return (ProductAttributeOtherFlux) criteria
				.add(Restrictions.eq("productAttribute", productAttribute))
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("productOperation", productOperation)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByOperation(ProductOperation productOperation, Boolean b) {
		Criteria criteria = getSession().createCriteria(ProductAttributeOtherFlux.class);
		return criteria
				.add(Restrictions.eq("productOperation", productOperation)).list();
	}

	@Override
	public ProductAttributeOtherFlux getOneProductAttributeOtherFluxById(Integer id) {
		return (ProductAttributeOtherFlux) getSession().get(ProductAttributeOtherFlux.class, id);
	}

	@Override
	public ProductAttributeOtherFlux saveProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
		getSession().saveOrUpdate(productAttributeOtherFlux);
		return productAttributeOtherFlux;
	}

	@Override
	public ProductAttributeOtherFlux editProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
		getSession().saveOrUpdate(productAttributeOtherFlux);
		return productAttributeOtherFlux;
	}

	@Override
	public void removeProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
		getSession().delete(productAttributeOtherFlux);

	}

	@Override
	public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByUuid(String uuid) {
		Criteria criteria = getSession().createCriteria(ProductAttributeOtherFlux.class);
		return (ProductAttributeOtherFlux) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxByOperationAndProduct(ProductOperation operation, Product product) {
		Query query = getSession().createQuery("FROM ProductAttributeFlux s WHERE s.productAttribute.product = :product AND s.productOperation = :operation");
		query.setParameter("product", product)
				.setParameter("operation", operation);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getAllProductAttributeFluxByOperationAndProductCount(ProductOperation operation, Product product) {
		Query query = getSession().createQuery("FROM ProductAttributeFlux s WHERE s.productAttribute.product = :product AND s.productOperation = :operation");
		query.setParameter("product", product)
				.setParameter("operation", operation);
		List<ProductAttributeFlux> fluxes = query.list();
		Integer quantity = 0;
		for (ProductAttributeFlux flux : fluxes) {
			quantity += flux.getQuantity();
		}
		return quantity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByOperationAndProduct(ProductOperation operation, Product product) {
		Query query = getSession().createQuery("FROM ProductAttributeFlux s WHERE s.productAttribute.product = :product AND s.productOperation = :operation");
		query.setParameter("product", product)
				.setParameter("operation", operation);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getAllProductAttributeOtherFluxByOperationAndProductCount(ProductOperation operation, Product product) {
		Query query = getSession().createQuery("FROM ProductAttributeOtherFlux s WHERE s.productAttribute.product = :product AND s.productOperation = :operation");
		query.setParameter("product", product)
				.setParameter("operation", operation);
		List<ProductAttributeOtherFlux> fluxes = query.list();
		Integer quantity = 0;
		for (ProductAttributeOtherFlux flux : fluxes) {
			quantity += flux.getQuantity().intValue();
		}
		return quantity;
	}

	@Override
	public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByProductAndOperation(Product product, ProductOperation productOperation) {
		Query query = getSession().createQuery("FROM ProductAttributeOtherFlux s WHERE s.product = :product AND s.productOperation = :operation");
		query.setParameter("product", product)
				.setParameter("operation", productOperation);
		return (ProductAttributeOtherFlux) query.setMaxResults(1).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByProductAndOperation(Product product, ProductOperation productOperation, Location location) {
		Query query = getSession().createQuery("FROM ProductAttributeOtherFlux s WHERE s.product = :product AND s.productOperation = :operation AND s.location = :location");
		query.setParameter("product", product)
				.setParameter("operation", productOperation)
				.setParameter("location", productOperation.getLocation());
		return query.list();
	}

	@Override
	public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByProductAndOperationAndLabel(Product product, ProductOperation productOperation, String label, Location location) {
		Query query = getSession().createQuery("FROM ProductAttributeOtherFlux s WHERE s.product = :product AND s.productOperation = :operation AND s.label = :label AND s.location = :location");
		query.setParameter("product", product)
				.setParameter("operation", productOperation)
				.setParameter("location", location)
				.setParameter("label", label);
		return (ProductAttributeOtherFlux) query.setMaxResults(1).uniqueResult();
	}

}
