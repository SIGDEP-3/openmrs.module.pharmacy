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
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductRegimen;
import org.openmrs.module.pharmacy.ProductUnit;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;

import java.util.List;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
public class HibernatePharmacyDAO implements PharmacyDAO {
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

	/******* PRODUCTS *******/

	@Override
	public Product saveProduct(Product product) {
		sessionFactory.getCurrentSession().saveOrUpdate(product);
		return product;
	}

	@Override
	public Product editProduct(Product product) {
		sessionFactory.getCurrentSession().saveOrUpdate(product);
		return product;
	}

	@Override
	public Product getOneProductById(Integer productId) {
		return (Product) sessionFactory.getCurrentSession().get(Product.class, productId);
	}

	@Override
	public Product getOneProductByCode(String code) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		return (Product) criteria.add(Restrictions.eq("code", code)).uniqueResult();
	}

	@Override
	public Product getOneProductByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		return (Product) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@Override
	public Product getOneProductByRetailName(String retailName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		return (Product) criteria.add(Restrictions.eq("retailName", retailName)).uniqueResult();
	}

	@Override
	public Product getOneProductByWholesaleName(String wholesaleName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		return (Product) criteria.add(Restrictions.eq("wholesaleName", wholesaleName)).uniqueResult();
	}

	@Override
	public Product getOneProductByName(String name) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		return (Product) criteria.add(Restrictions.or(Restrictions.eq("wholesaleName", name),
				Restrictions.eq("retailName", name))).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAllProduct() {
		return sessionFactory.getCurrentSession().createCriteria(Product.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAllProductByRetailUnit(ProductUnit retailUnit) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		return (List<Product>) criteria.add(Restrictions.eq("productRetailUnit", retailUnit)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAllProductByWholesaleUnit(ProductUnit wholesaleUnit) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		return (List<Product>) criteria.add(Restrictions.eq("productWholesaleUnit", wholesaleUnit)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> searchProductByNameLike(String nameSearch) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		return (List<Product>) criteria.add(Restrictions.or(Restrictions.like("wholesaleName", nameSearch),
				Restrictions.like("retailName", nameSearch))).list();
	}

	/******* PRODUCTS PROGRAMS *******/

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
		return (ProductProgram) sessionFactory.getCurrentSession().get(ProductUnit.class, productProgramId);
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
		return sessionFactory.getCurrentSession().createCriteria(ProductProgram.class).list();
	}

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
