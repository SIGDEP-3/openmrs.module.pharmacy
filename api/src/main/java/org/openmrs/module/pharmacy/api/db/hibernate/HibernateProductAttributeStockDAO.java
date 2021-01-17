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
import org.openmrs.Location;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductAttribute;
import org.openmrs.module.pharmacy.ProductAttributeStock;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.api.db.ProductAttributeStockDAO;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.List;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
public class HibernateProductAttributeStockDAO implements ProductAttributeStockDAO {
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
	public List<ProductAttributeStock> getAllProductAttributeStocks(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeStock.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeStock> getAllProductAttributeStocks(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeStock.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeStock> getAllProductAttributeStocks(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeStock.class);
		return criteria.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeStock> getAllProductAttributeStockByAttribute(ProductAttribute productAttribute, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeStock.class);
		return criteria
				.add(Restrictions.eq("productAttribute", productAttribute))
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@Override
	public ProductAttributeStock getOneProductAttributeStockByAttribute(ProductAttribute productAttribute, Location location, Boolean includeVoided) throws HibernateException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeStock.class);

		return (ProductAttributeStock) criteria
				.add(Restrictions.eq("productAttribute", productAttribute))
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided)).uniqueResult();
	}

	@Override
	public ProductAttributeStock getOneProductAttributeStockById(Integer id) {
		return (ProductAttributeStock) sessionFactory.getCurrentSession().get(ProductAttributeStock.class, id);
	}

	@Override
	public ProductAttributeStock saveProductAttributeStock(ProductAttributeStock productAttributeStock) {
		sessionFactory.getCurrentSession().saveOrUpdate(productAttributeStock);
		return productAttributeStock;
	}

	@Override
	public ProductAttributeStock editProductAttributeStock(ProductAttributeStock productAttributeStock) {
		sessionFactory.getCurrentSession().saveOrUpdate(productAttributeStock);
		return productAttributeStock;
	}

	@Override
	public void removeProductAttributeStock(ProductAttributeStock productAttributeStock) {
		sessionFactory.getCurrentSession().delete(productAttributeStock);
	}

	@Override
	public ProductAttributeStock getOneProductAttributeStockByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeStock.class);
		return (ProductAttributeStock) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeStock> getProductAttributeStocksByProduct(Product product, Location userLocation) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM ProductAttributeStock s WHERE s.productAttribute.product = :product AND s.location = :location ORDER BY s.productAttribute.expiryDate ASC ");
		query.setParameter("product", product);
		query.setParameter("location", userLocation);

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeStock> getAllProductAttributeStockByProduct(Product product, Location location) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM ProductAttributeStock s WHERE s.productAttribute.product = :product AND s.location = :location ORDER BY s.productAttribute.expiryDate ASC ");
		query.setParameter("product", product)
				.setParameter("location", location);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getAllProductAttributeStockByProductCount(Product product, Location location, Boolean includeChildren) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM ProductAttributeStock s WHERE s.productAttribute.product = :product AND s.location IN :location ORDER BY s.productAttribute.expiryDate ASC ");
		query.setParameter("product", product)
				.setParameter("location", !includeChildren ? location : OperationUtils.getUserLocations());
		List<ProductAttributeStock> stocks = query.list();
		Integer quantity = 0;
		for (ProductAttributeStock stock : stocks) {
			quantity += stock.getQuantityInStock();
		}
		return quantity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getProductAttributeStocksByProductCount(Product product) {
		Query query = sessionFactory.getCurrentSession().createQuery("FROM ProductAttributeStock s WHERE s.productAttribute.product = :product ORDER BY s.productAttribute.expiryDate DESC ");
		query.setParameter("product", product);

		List<ProductAttributeStock> stocks = query.list();
		Integer quantity = 0;
		for (ProductAttributeStock stock : stocks) {
			quantity += stock.getQuantityInStock();
		}
		return quantity;
	}

}
