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
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pharmacy.entities.ProductTransfer;
import org.openmrs.module.pharmacy.api.db.ProductTransferDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of  {@link ProductTransferDAO}.
 */
@Repository
public class HibernateProductTransferDAO implements ProductTransferDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DbSessionFactory sessionFactory;

//	public void setSessionFactory(DbSessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductTransfer> getAllProductTransfers(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductTransfer.class);
		return criteria.add(Restrictions.eq("location", location)).
				add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductTransfer> getAllProductTransfers(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductTransfer.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.between("operationDate", operationStartDate, operationEndDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductTransfer> getAllProductTransfers(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductTransfer.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductTransfer> getAllProductTransfers(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductTransfer.class);
		return criteria.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@Override
	public ProductTransfer getOneProductTransferById(Integer id) {
		return (ProductTransfer) sessionFactory.getCurrentSession().get(ProductTransfer.class, id);
	}

	@Override
	public ProductTransfer saveProductTransfer(ProductTransfer productTransfer) {
		sessionFactory.getCurrentSession().saveOrUpdate(productTransfer);
		return productTransfer;
	}

	@Override
	public ProductTransfer editProductTransfer(ProductTransfer productTransfer) {
		sessionFactory.getCurrentSession().saveOrUpdate(productTransfer);
		return productTransfer;
	}

	@Override
	public void removeProductTransfer(ProductTransfer productTransfer) {
		sessionFactory.getCurrentSession().delete(productTransfer);
	}

	@Override
	public ProductTransfer getOneProductTransferByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductTransfer.class);
		return (ProductTransfer) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Location> getAllClientLocation(Boolean includeVoided) throws HibernateException {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT l.location FROM LocationAttribute l WHERE l.attributeType.uuid = 'NPSPCLIENTCCCCCCCCCCCCCCCCCCCCCCCCCC' AND l.valueReference = 'true' AND l.voided = :includeVoided ");
			query.setParameter("includeVoided", includeVoided);
			return query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ProductOutFluxDTO> getProductTransferFluxDTOs(ProductTransfer productTransfer) {
//		String sqlQuery =
//				"SELECT " +
//						"ppaf.product_attribute_flux_id productAttributeFluxId, " +
//						"pp.code, " +
//						"pp.retail_name retailName, " +
//						"ppu.name retailUnit, " +
//						"ppa.batch_number batchNumber, " +
//						"ppa.expiry_date expiryDate, " +
//						"ppaf.quantity, " +
//						"ppas.quantity_in_stock quantityInStock, " +
//						"ppaf.observation, " +
//						"ppaf.date_created dateCreated " +
//						"FROM pharmacy_product_attribute_flux ppaf " +
//						"LEFT JOIN pharmacy_product_operation ppo ON ppo.product_operation_id = ppaf.operation_id " +
//						"LEFT JOIN pharmacy_product_attribute ppa ON ppa.product_attribute_id = ppaf.product_attribute_id " +
//						"LEFT JOIN pharmacy_product pp on pp.product_id = ppa.product_id " +
//						"LEFT JOIN pharmacy_product_unit ppu on ppu.product_unit_id = pp.product_retail_unit " +
//						"LEFT JOIN pharmacy_product_attribute_stock ppas on ppas.product_attribute_id = ppa.product_attribute_id " +
//						"WHERE ppaf.operation_id = :productOperationId ORDER BY ppaf.date_created DESC ";
//
//		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
//				.addScalar("productAttributeFluxId", StandardBasicTypes.INTEGER)
//				.addScalar("code", StandardBasicTypes.STRING)
//				.addScalar("retailName", StandardBasicTypes.STRING)
//				.addScalar("retailUnit", StandardBasicTypes.STRING)
//				.addScalar("batchNumber", StandardBasicTypes.STRING)
//				.addScalar("expiryDate", StandardBasicTypes.DATE)
//				.addScalar("quantity", StandardBasicTypes.INTEGER)
//				.addScalar("quantityInStock", StandardBasicTypes.INTEGER)
//				.addScalar("observation", StandardBasicTypes.INTEGER)
//				.addScalar("dateCreated", StandardBasicTypes.DATE)
//				.setParameter("productOperationId", productTransfer.getProductOperationId())
//				.setResultTransformer(new AliasToBeanResultTransformer(ProductOutFluxDTO.class));
//		try {
//			return query.list();
//		} catch (HibernateException e) {
//			System.out.println(e.getMessage());
//		}
//		return null;
//	}
//
//	@Override
//	public ProductTransfer getLastProductTransfer(Location location, ProductProgram productProgram) {
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductTransfer.class);
//		return (ProductTransfer) criteria
//				.add(Restrictions.eq("location", location))
//				.add(Restrictions.eq("productProgram", productProgram))
//				.add(Restrictions.eq("inventoryType", TransferType.FULL))
//				.addOrder(Order.desc("operationDate")).setMaxResults(1).uniqueResult();
//	}
//
//	@Override
//	public ProductTransfer getLastProductTransferByDate(Location location, ProductProgram productProgram, Date inventoryDate) {
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductTransfer.class);
//		return (ProductTransfer) criteria
//				.add(Restrictions.eq("location", location))
//				.add(Restrictions.eq("productProgram", productProgram))
//				.add(Restrictions.eq("inventoryType", TransferType.FULL))
//				.add(Restrictions.lt("operationDate", inventoryDate))
//				.addOrder(Order.desc("operationDate")).setMaxResults(1).uniqueResult();
//	}

}
