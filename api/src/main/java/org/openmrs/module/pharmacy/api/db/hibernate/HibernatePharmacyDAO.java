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
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;

import java.util.Arrays;
import java.util.Date;
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

	/******* PRODUCTS PRICES *******/

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
		return (ProductPrice) sessionFactory.getCurrentSession().get(ProductUnit.class, productPriceId);
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
	public List<ProductSupplier> getAllProductSuppliers() {
		return sessionFactory.getCurrentSession().createCriteria(ProductSupplier.class).list();
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReception> getAllProductReceptions(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReception.class);
		return criteria.add(Restrictions.eq("location", location)).
				add(Restrictions.eq("voided", includeVoided)).list();
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
		return (ProductPrice) sessionFactory.getCurrentSession();
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

	public List<ProductReception> getAllProductReceptions(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReception.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.between("operationDate", operationStartDate, operationEndDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReception> getAllProductReceptions(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReception.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReception> getAllProductReceptions(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReception.class);
		return criteria.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@Override
	public ProductReception getOneProductReceptionById(Integer id) {
		return (ProductReception) sessionFactory.getCurrentSession().get(ProductReception.class, id);
	}

	@Override
	public ProductReception saveProductReception(ProductReception productReception) {
		sessionFactory.getCurrentSession().saveOrUpdate(productReception);
		return productReception;
	}

	@Override
	public ProductReception editProductReception(ProductReception productReception) {
		sessionFactory.getCurrentSession().saveOrUpdate(productReception);
		return productReception;
	}

	@Override
	public void removeProductReception(ProductReception productReception) {
		sessionFactory.getCurrentSession().delete(productReception);
	}

	@Override
	public ProductReception getOneProductReceptionByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReception.class);
		return (ProductReception) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReceptionFluxDTO> getProductReceptionFluxDTOs(ProductReception productReception) {
		String sqlQuery =
				"SELECT " +
						"ppaf.product_attribute_flux_id as productAttributeFluxId, " +
						"ppr.product_operation_id as productOperationId, " +
						"pp.product_id as productId, " +
						"pp.code as code, " +
						"pp.retail_name as retailName, " +
						"pp.wholesale_name as wholesaleName, " +
						"ppu.name as retailUnit, " +
						"ppu2.name as wholesaleUnit, " +
						"ppa.batch_number as batchNumber, " +
						"ppa.expiry_date as expiryDate, " +
						"ppaf.quantity as receivedQuantity, " +
						"ppaof.quantity as deliveredQuantity, " +
						"ppaf.observation as observation, " +
						"ppaf.date_created as dateCreated " +
						"FROM pharmacy_product_reception ppr " +
						"LEFT JOIN pharmacy_product_operation ppo on ppr.product_operation_id = ppo.product_operation_id " +
						"LEFT JOIN pharmacy_product_attribute_flux ppaf on ppo.product_operation_id = ppaf.operation_id " +
						"LEFT JOIN pharmacy_product_attribute ppa on ppaf.product_attribute_id = ppa.product_attribute_id " +
						"LEFT JOIN pharmacy_product_attribute_other_flux ppaof on ppo.product_operation_id = ppaof.operation_id AND ppaof.product_attribute_id = ppa.product_attribute_id " +
						"LEFT JOIN pharmacy_product pp ON ppa.product_id = pp.product_id " +
						"LEFT JOIN pharmacy_product_unit ppu on pp.product_retail_unit = ppu.product_unit_id " +
						"LEFT JOIN pharmacy_product_unit ppu2 on pp.product_wholesale_unit = ppu2.product_unit_id " +
						"WHERE ppr.product_operation_id = :productOperationId " +
						"ORDER BY ppaf.date_created DESC ";

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("productAttributeFluxId", StandardBasicTypes.INTEGER)
				.addScalar("productOperationId", StandardBasicTypes.INTEGER)
				.addScalar("productId", StandardBasicTypes.INTEGER)
				.addScalar("code", StandardBasicTypes.STRING)
				.addScalar("retailName", StandardBasicTypes.STRING)
				.addScalar("wholesaleName", StandardBasicTypes.STRING)
				.addScalar("retailUnit", StandardBasicTypes.STRING)
				.addScalar("wholesaleUnit", StandardBasicTypes.STRING)
				.addScalar("batchNumber", StandardBasicTypes.STRING)
				.addScalar("expiryDate", StandardBasicTypes.DATE)
				.addScalar("deliveredQuantity", StandardBasicTypes.INTEGER)
				.addScalar("receivedQuantity", StandardBasicTypes.INTEGER)
				.addScalar("observation", StandardBasicTypes.STRING)
				.addScalar("dateCreated", StandardBasicTypes.DATE)
				.setParameter("productOperationId", productReception.getProductOperationId())
				.setResultTransformer(new AliasToBeanResultTransformer(ProductReceptionFluxDTO.class));
		try {
			return (List<ProductReceptionFluxDTO>) query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
//		System.out.println("query.list()--------------------------------");
//		System.out.println(Arrays.toString(query.getReturnAliases()));
//		System.out.println("query.list()--------------------------------");
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttribute> getAllProductAttributes(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttribute> getAllProductAttributes(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return criteria
				.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttribute> getAllProductAttributes(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return criteria
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttribute> getAllProductAttributes(Product product) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return criteria.add(Restrictions.eq("product", product)).list();
	}

	@Override
	public ProductAttribute getOneProductAttributeById(Integer id) {
		return (ProductAttribute) sessionFactory.getCurrentSession().get(ProductAttribute.class, id);
	}

	@Override
	public ProductAttribute saveProductAttribute(ProductAttribute productAttribute) {
		sessionFactory.getCurrentSession().saveOrUpdate(productAttribute);
		return productAttribute;
	}

	@Override
	public ProductAttribute editProductAttribute(ProductAttribute productAttribute) {
		sessionFactory.getCurrentSession().saveOrUpdate(productAttribute);
		return productAttribute;
	}

	@Override
	public void removeProductAttribute(ProductAttribute productAttribute) {
		sessionFactory.getCurrentSession().delete(productAttribute);
	}

	@Override
	public ProductAttribute getOneProductAttributeByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return (ProductAttribute) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@Override
	public ProductAttribute getOneProductAttributeByBatchNumber(String batchNumber) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttribute.class);
		return (ProductAttribute) criteria.add(Restrictions.eq("batchNumber", batchNumber)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeFlux.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeFlux.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxes(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeFlux.class);
		return criteria.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxByAttribute(ProductAttribute productAttribute, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeFlux.class);
		return criteria
				.add(Restrictions.eq("productAttribute", productAttribute))
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductAttributeFlux> getAllProductAttributeFluxByOperation(ProductOperation productOperation, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeFlux.class);
		return criteria
				.add(Restrictions.eq("productOperation", productOperation))
				.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@Override
	public ProductAttributeFlux getOneProductAttributeFluxById(Integer id) {
		return (ProductAttributeFlux) sessionFactory.getCurrentSession().get(ProductAttributeFlux.class, id);
	}

	@Override
	public ProductAttributeFlux getOneProductAttributeFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeFlux.class);
		return (ProductAttributeFlux) criteria
				.add(Restrictions.eq("productAttribute", productAttribute))
				.add(Restrictions.eq("productOperation", productOperation)).uniqueResult();
	}

	@Override
	public ProductAttributeFlux saveProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
		sessionFactory.getCurrentSession().saveOrUpdate(productAttributeFlux);
		return productAttributeFlux;
	}

	@Override
	public ProductAttributeFlux editProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
		sessionFactory.getCurrentSession().saveOrUpdate(productAttributeFlux);
		return productAttributeFlux;
	}

	@Override
	public void removeProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
		sessionFactory.getCurrentSession().delete(productAttributeFlux);
	}

	@Override
	public ProductAttributeFlux getOneProductAttributeFluxByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeFlux.class);
		return (ProductAttributeFlux) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
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
	public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxes(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeOtherFlux.class);
		return criteria.add(Restrictions.eq("productAttribute", location)).list();
	}

	@Override
	public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeOtherFlux.class);
		return (ProductAttributeOtherFlux) criteria
				.add(Restrictions.eq("productAttribute", productAttribute))
				.add(Restrictions.eq("productOperation", productOperation)).uniqueResult();
	}

	@Override
	public ProductAttributeOtherFlux getOneProductAttributeOtherFluxById(Integer id) {
		return (ProductAttributeOtherFlux) sessionFactory.getCurrentSession().get(ProductAttributeOtherFlux.class, id);
	}

	@Override
	public ProductAttributeOtherFlux saveProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
		sessionFactory.getCurrentSession().saveOrUpdate(productAttributeOtherFlux);
		return productAttributeOtherFlux;
	}

	@Override
	public ProductAttributeOtherFlux editProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
		sessionFactory.getCurrentSession().saveOrUpdate(productAttributeOtherFlux);
		return productAttributeOtherFlux;
	}

	@Override
	public void removeProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
		sessionFactory.getCurrentSession().delete(productAttributeOtherFlux);

	}

	@Override
	public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeOtherFlux.class);
		return (ProductAttributeOtherFlux) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
}
