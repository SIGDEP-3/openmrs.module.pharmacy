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
import org.openmrs.Location;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.api.db.ProductReportDAO;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.StockOutType;
import org.openmrs.module.pharmacy.models.ProductReportLineDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of  {@link ProductReportDAO}.
 */
public class HibernateProductReportDAO implements ProductReportDAO {
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
	public List<ProductReport> getAllProductReports(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return criteria.add(Restrictions.eq("location", location)).
				add(Restrictions.eq("voided", includeVoided)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllProductDistributionReports(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.isNotNull("reportLocation")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllSubmittedChildProductReports(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		List<String> locations = new ArrayList<>();
		for (Location userLocation : location.getChildLocations()) {
			locations.add(userLocation.getName());
		}
		if (locations.size() != 0) {
			return criteria
					.add(
							Restrictions.and(
									Restrictions.and(
											Restrictions.eq("voided", includeVoided),
											Restrictions.eq("operationStatus", OperationStatus.SUBMITTED)
									),
									Restrictions.in("location.name", locations)
							)
					).list();
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllTreatedChildProductReports(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return criteria
				.add(Restrictions.in("location", location.getChildLocations(includeVoided)))
				.add(Restrictions.eq("operationStatus", OperationStatus.TREATED))
				.add(Restrictions.eq("voided", includeVoided))
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllProductReports(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.between("operationDate", operationStartDate, operationEndDate)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllProductReports(Location location) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return criteria.add(Restrictions.eq("location", location)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllProductReports(Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return criteria.add(Restrictions.eq("voided", includeVoided)).list();
	}

	@Override
	public ProductReport getOneProductReportById(Integer id) {
		return (ProductReport) sessionFactory.getCurrentSession().get(ProductReport.class, id);
	}

	@Override
	public ProductReport saveProductReport(ProductReport productReport) {
		sessionFactory.getCurrentSession().saveOrUpdate(productReport);
		return productReport;
	}

	@Override
	public ProductReport editProductReport(ProductReport productReport) {
		sessionFactory.getCurrentSession().saveOrUpdate(productReport);
		return productReport;
	}

	@Override
	public void removeProductReport(ProductReport productReport) {
		sessionFactory.getCurrentSession().delete(productReport);
	}

	@Override
	public ProductReport getOneProductReportByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return (ProductReport) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReportLineDTO> getProductReportFluxDTOs(ProductReport productReport) {
		String sqlQuery =
				"SELECT " +
						"ppaf.product_attribute_flux_id as productAttributeFluxId, " +
						"ppr.product_operation_id as productOperationId, " +
						"pp.product_id as productId, " +
						"pp.code as code, " +
						"pp.retail_name as retailName, " +
						"pp.wholesale_name as wholesaleName, " +
						"pp.unit_conversion as unitConversion, " +
						"ppu.name as retailUnit, " +
						"ppu2.name as wholesaleUnit, " +
						"ppa.batch_number as batchNumber, " +
						"ppa.expiry_date as expiryDate, " +
						"ppaf.quantity as quantity, " +
						"ppaof.quantity as quantityToDeliver, " +
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
						"WHERE ppr.product_operation_id = :productOperationId AND product_attribute_flux_id IS NOT NULL " +
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
				.addScalar("quantityToDeliver", StandardBasicTypes.INTEGER)
				.addScalar("unitConversion", StandardBasicTypes.DOUBLE)
				.addScalar("quantity", StandardBasicTypes.INTEGER)
				.addScalar("observation", StandardBasicTypes.STRING)
				.addScalar("dateCreated", StandardBasicTypes.DATE)
				.setParameter("productOperationId", productReport.getProductOperationId())
				.setResultTransformer(new AliasToBeanResultTransformer(ProductReportLineDTO.class));
		try {
			return query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public Integer getProductReceivedQuantityInLastOperationByProduct(Product product, ProductInventory inventory, Location location) {
		List<ProductReception> productReceptions = Context.getService(ProductReceptionService.class).getAllProductReceptions(location, false, inventory.getInventoryStartDate(), inventory.getOperationDate());
		Integer quantity = 0;
		for (ProductReception reception :
				productReceptions) {
			for (ProductAttributeFlux flux : reception.getProductAttributeFluxes()) {
				if (flux.getProductAttribute().getProduct().equals(product) && flux.getStatus().equals(OperationStatus.VALIDATED)) {
					if (reception.getIncidence().equals(Incidence.POSITIVE)) {
						quantity += flux.getQuantity();
					} else if (reception.getIncidence().equals(Incidence.NEGATIVE)) {
						quantity -= flux.getQuantity();
					}
				}
			}
		}
		return quantity;
	}

	@Override
	public Integer getProductQuantityInStockInLastOperationByProduct(Product product, ProductInventory inventory, Location location) {
		Integer quantity = 0;
		for (ProductAttributeFlux flux : inventory.getProductAttributeFluxes()) {
			if (flux.getProductAttribute().getProduct().equals(product) && flux.getStatus().equals(OperationStatus.VALIDATED)) {
				quantity += flux.getQuantity();
			}
		}
		return quantity;
	}

	@Override
	public Integer getProductQuantityLostInLastOperationByProduct(Product product, ProductInventory inventory, Location location) {
		List<ProductMovementOut> productMovementOuts = Context.getService(ProductMovementService.class).getAllProductMovementOut(location, false, inventory.getInventoryStartDate(), inventory.getOperationDate());
		Integer quantity = 0;
		for (ProductMovementOut movementOut :
				productMovementOuts) {
			if (!movementOut.getStockOutType().equals(StockOutType.BACK_TO_SUPPLIER)) {
				for (ProductAttributeFlux flux : movementOut.getProductAttributeFluxes()) {
					if (flux.getProductAttribute().getProduct().equals(product) && flux.getStatus().equals(OperationStatus.VALIDATED)) {
						quantity += flux.getQuantity();
					}
				}
			}
		}
		return quantity;
	}

	@Override
	public Integer getProductQuantityAdjustmentInLastOperationByProduct(Product product, ProductInventory inventory, Location location) {
		List<ProductTransfer> productTransfers = Context.getService(ProductTransferService.class).getAllProductTransfers(location, false, inventory.getInventoryStartDate(), inventory.getOperationDate());
		Integer quantity = 0;
		for (ProductTransfer productTransfer :
				productTransfers) {
			for (ProductAttributeFlux flux : productTransfer.getProductAttributeFluxes()) {
				if (flux.getProductAttribute().getProduct().equals(product) && flux.getStatus().equals(OperationStatus.VALIDATED)) {
					if (productTransfer.getIncidence().equals(Incidence.POSITIVE)) {
						quantity += flux.getQuantity();
					} else if (productTransfer.getIncidence().equals(Incidence.NEGATIVE)) {
						quantity -= flux.getQuantity();
					}
				}
			}
		}
		return quantity;
	}

	@Override
	public List<Product> getAllActivityProducts(ProductInventory inventory) {
		List<ProductAttributeFlux> fluxes = Context.getService(ProductAttributeFluxService.class).getAllProductAttributeFluxes(inventory.getLocation(), inventory.getInventoryStartDate(), inventory.getOperationDate(), false);
		List<Product> products = new ArrayList<>();
		for (ProductAttributeFlux flux :
				fluxes) {
			products.add(flux.getProductAttribute().getProduct());
		}
		return products;
	}


//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ProductReportReturnDTO> getProductReportReturnDTOs(ProductReport productReport) {
//		String sqlQuery =
//				"SELECT " +
//						"    ppaf.product_attribute_flux_id as productAttributeFluxId, " +
//						"    ppr.product_operation_id as productOperationId, " +
//						"    pp.product_id as productId, " +
//						"    pp.code as code, " +
//						"    pp.retail_name as retailName, " +
//						"    pp.wholesale_name as wholesaleName, " +
//						"    pp.unit_conversion as unitConversion, " +
//						"    ppu.name as retailUnit, " +
//						"    ppu2.name as wholesaleUnit, " +
//						"    ppa.batch_number as batchNumber, " +
//						"    ppa.expiry_date as expiryDate, " +
//						"    ppaf.quantity as quantityToReturn, " +
//						"    ppo.quantityReceived, " +
//						"    ppas.quantity_in_stock as quantityInStock, " +
//						"    ppaf.observation as observation, " +
//						"    ppaf.date_created as dateCreated " +
//						"FROM (SELECT o.product_operation_id, operation_number FROM pharmacy_product_reception r, pharmacy_product_operation o WHERE r.product_operation_id = o.product_operation_id AND r.product_operation_id = :productOperationId) ppr " +
//						"    LEFT JOIN pharmacy_product_attribute_flux ppaf on ppr.product_operation_id = ppaf.operation_id " +
//						"    LEFT JOIN ( " +
//						"        SELECT operation_number, quantity quantityReceived, product_attribute_id " +
//						"        FROM pharmacy_product_operation o " +
//						"            LEFT JOIN pharmacy_product_attribute_flux f ON o.product_operation_id = f.operation_id " +
//						"        WHERE o.incidence = 1 AND operation_number = :operationNumber " +
//						"    ) ppo ON ppo.operation_number = ppr.operation_number AND ppo.product_attribute_id = ppaf.product_attribute_id " +
//						"         LEFT JOIN pharmacy_product_attribute ppa on ppa.product_attribute_id = ppo.product_attribute_id " +
//						"         LEFT JOIN pharmacy_product_attribute_stock ppas on ppo.product_attribute_id = ppas.product_attribute_id " +
//						"         LEFT JOIN pharmacy_product pp ON ppa.product_id = pp.product_id " +
//						"         LEFT JOIN pharmacy_product_unit ppu on pp.product_retail_unit = ppu.product_unit_id " +
//						"         LEFT JOIN pharmacy_product_unit ppu2 on pp.product_wholesale_unit = ppu2.product_unit_id " +
//						"WHERE ppaf.product_attribute_flux_id IS NOT NULL ORDER BY ppaf.date_created DESC";
//
//		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
//				.addScalar("productAttributeFluxId", StandardBasicTypes.INTEGER)
//				.addScalar("productOperationId", StandardBasicTypes.INTEGER)
//				.addScalar("productId", StandardBasicTypes.INTEGER)
//				.addScalar("code", StandardBasicTypes.STRING)
//				.addScalar("retailName", StandardBasicTypes.STRING)
//				.addScalar("wholesaleName", StandardBasicTypes.STRING)
//				.addScalar("retailUnit", StandardBasicTypes.STRING)
//				.addScalar("wholesaleUnit", StandardBasicTypes.STRING)
//				.addScalar("batchNumber", StandardBasicTypes.STRING)
//				.addScalar("expiryDate", StandardBasicTypes.DATE)
//				.addScalar("unitConversion", StandardBasicTypes.DOUBLE)
//				.addScalar("quantityReceived", StandardBasicTypes.INTEGER)
//				.addScalar("quantityInStock", StandardBasicTypes.INTEGER)
//				.addScalar("quantityToReturn", StandardBasicTypes.INTEGER)
//				.addScalar("observation", StandardBasicTypes.STRING)
//				.addScalar("dateCreated", StandardBasicTypes.DATE)
//				.setParameter("productOperationId", productReport.getProductOperationId())
//				.setParameter("operationNumber", productReport.getOperationNumber())
//				.setResultTransformer(new AliasToBeanResultTransformer(ProductReportReturnDTO.class));
//		try {
//			return query.list();
//		} catch (HibernateException e) {
//			System.out.println(e.getMessage());
//		}
//		return null;
//	}
//
//	@Override
//	public ProductReportReturnDTO getOneProductReportReturnDTO(ProductReport reception, ProductAttribute productAttribute) {
//		String sqlQuery =
//				"SELECT " +
//						"    ppaf.product_attribute_flux_id as productAttributeFluxId, " +
//						"    ppr.product_operation_id as productOperationId, " +
//						"    pp.product_id as productId, " +
//						"    pp.code as code, " +
//						"    pp.retail_name as retailName, " +
//						"    pp.wholesale_name as wholesaleName, " +
//						"    pp.unit_conversion as unitConversion, " +
//						"    ppu.name as retailUnit, " +
//						"    ppu2.name as wholesaleUnit, " +
//						"    ppa.batch_number as batchNumber, " +
//						"    ppa.expiry_date as expiryDate, " +
//						"    ppaf.quantity as quantityToReturn, " +
//						"    ppo.quantityReceived, " +
//						"    ppas.quantity_in_stock as quantityInStock, " +
//						"    ppaf.observation as observation, " +
//						"    ppaf.date_created as dateCreated " +
//						"FROM (SELECT o.product_operation_id, operation_number FROM pharmacy_product_reception r, pharmacy_product_operation o WHERE r.product_operation_id = o.product_operation_id AND r.product_operation_id = :productOperationId) ppr " +
//						"    LEFT JOIN pharmacy_product_attribute_flux ppaf on ppr.product_operation_id = ppaf.operation_id " +
//						"    LEFT JOIN ( " +
//						"        SELECT operation_number, quantity quantityReceived, product_attribute_id " +
//						"        FROM pharmacy_product_operation o " +
//						"            LEFT JOIN pharmacy_product_attribute_flux f ON o.product_operation_id = f.operation_id " +
//						"        WHERE o.incidence = 1 AND operation_number = :operationNumber " +
//						"    ) ppo ON ppo.operation_number = ppr.operation_number " +
//						"         LEFT JOIN pharmacy_product_attribute ppa on ppa.product_attribute_id = ppo.product_attribute_id " +
//						"         LEFT JOIN pharmacy_product_attribute_stock ppas on ppo.product_attribute_id = ppas.product_attribute_id " +
//						"         LEFT JOIN pharmacy_product pp ON ppa.product_id = pp.product_id " +
//						"         LEFT JOIN pharmacy_product_unit ppu on pp.product_retail_unit = ppu.product_unit_id " +
//						"         LEFT JOIN pharmacy_product_unit ppu2 on pp.product_wholesale_unit = ppu2.product_unit_id " +
//						" WHERE ppo.product_attribute_id = :productAttributeId";
//
//		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
//				.addScalar("productAttributeFluxId", StandardBasicTypes.INTEGER)
//				.addScalar("productOperationId", StandardBasicTypes.INTEGER)
//				.addScalar("productId", StandardBasicTypes.INTEGER)
//				.addScalar("code", StandardBasicTypes.STRING)
//				.addScalar("retailName", StandardBasicTypes.STRING)
//				.addScalar("wholesaleName", StandardBasicTypes.STRING)
//				.addScalar("retailUnit", StandardBasicTypes.STRING)
//				.addScalar("wholesaleUnit", StandardBasicTypes.STRING)
//				.addScalar("batchNumber", StandardBasicTypes.STRING)
//				.addScalar("expiryDate", StandardBasicTypes.DATE)
//				.addScalar("unitConversion", StandardBasicTypes.DOUBLE)
//				.addScalar("quantityReceived", StandardBasicTypes.INTEGER)
//				.addScalar("quantityInStock", StandardBasicTypes.INTEGER)
//				.addScalar("quantityToReturn", StandardBasicTypes.INTEGER)
//				.addScalar("observation", StandardBasicTypes.STRING)
//				.addScalar("dateCreated", StandardBasicTypes.DATE)
//				.setParameter("productOperationId", reception.getProductOperationId())
//				.setParameter("productAttributeId", productAttribute.getProductAttributeId())
//				.setParameter("operationNumber", reception.getOperationNumber())
//				.setResultTransformer(new AliasToBeanResultTransformer(ProductReportReturnDTO.class));
//		try {
//			return (ProductReportReturnDTO) query.uniqueResult();
//		} catch (HibernateException e) {
//			System.out.println(e.getMessage());
//		}
//		return null;
//	}
//
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ProductReportListDTO> getProductReportListDTOs() {
//		String sqlQuery =
//				"SELECT " +
//						"    ppop.product_operation_id productOperationId, " +
//						"    ppop.operation_date operationDate, " +
//						"    pps.name productSupplier, " +
//						"    ppp.name programName, " +
//						"    operation_number operationNumber, " +
//						"    ppon.product_operation_id productReturnedOperationId, " +
//						"    IF((ppon.product_operation_id IS NULL OR (ppon.product_operation_id IS NOT NULL AND ppon.operation_status <> 2)) AND i.operation_date <= ppop.operation_date, 1,  0) canReturn, " +
//						"    IF(ppop.operation_status = 0, 'AWAITING_VALIDATION', " +
//						"        IF(ppop.operation_status = 1, 'DISABLED', " +
//						"            IF(ppop.operation_status = 2, 'VALIDATED', " +
//						"                IF(ppop.operation_status = 3, 'NOT_COMPLETED', NULL)) )) operationStatus, " +
//						"    IF(ppon.operation_status = 0, 'AWAITING_VALIDATION', " +
//						"        IF(ppon.operation_status = 1, 'DISABLED', " +
//						"            IF(ppon.operation_status = 2, 'VALIDATED', " +
//						"                IF(ppon.operation_status = 3, 'NOT_COMPLETED', NULL)) )) productReturnedOperationStatus, " +
//						"    IF(reception_mode = 0, 'RETAIL', " +
//						"        IF(reception_mode = 1, 'WHOLESALE', NULL)) receptionMode, " +
//						"    numberOfLine " +
//						"FROM " +
//						"    ( " +
//						"        SELECT ppr.product_operation_id, operation_date, product_supplier_id, operation_number, program_id, operation_status, incidence, location_id, reception_mode " +
//						"        FROM pharmacy_product_reception ppr, pharmacy_product_operation ppo " +
//						"        WHERE ppr.product_operation_id = ppo.product_operation_id AND incidence = 1 AND location_id = :locationId " +
//						"    ) ppop " +
//						"    LEFT JOIN pharmacy_product_supplier pps ON pps.product_supplier_id = ppop.product_supplier_id " +
//						"    LEFT JOIN pharmacy_product_program ppp ON ppp.product_program_id = ppop.program_id " +
//						"LEFT JOIN " +
//						"    ( " +
//						"        SELECT ppr.product_operation_id, operation_number returnedOperationNumber, program_id, location_id, operation_status " +
//						"        FROM pharmacy_product_reception ppr, pharmacy_product_operation ppo " +
//						"        WHERE ppr.product_operation_id = ppo.product_operation_id AND incidence = 0 " +
//						"    ) ppon " +
//						"ON ppon.returnedOperationNumber = ppop.operation_number AND ppop.program_id = ppon.program_id AND ppon.location_id = ppop.location_id " +
//						"LEFT JOIN ( " +
//						"    SELECT " +
//						"           ppo.operation_date, program_id " +
//						"    FROM pharmacy_product_inventory ppi, pharmacy_product_operation ppo " +
//						"    WHERE " +
//						"          ppi.product_operation_id = ppo.product_operation_id AND " +
//						"          ppo.operation_date = (SELECT MAX(operation_date) FROM pharmacy_product_operation po, pharmacy_product_inventory pi WHERE pi.product_operation_id = po.product_operation_id AND ppo.program_id = po.program_id AND po.location_id = :locationId " +
//						"        )) i ON i.program_id = ppop.program_id " +
//						"LEFT JOIN (SELECT COUNT(*) numberOfLine, operation_id FROM pharmacy_product_attribute_flux GROUP BY operation_id) ppaf ON operation_id = ppop.product_operation_id";
//
//		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
//				.addScalar("productOperationId", StandardBasicTypes.INTEGER)
//				.addScalar("productReturnedOperationId", StandardBasicTypes.INTEGER)
//				.addScalar("operationDate", StandardBasicTypes.DATE)
//				.addScalar("productSupplier", StandardBasicTypes.STRING)
//				.addScalar("programName", StandardBasicTypes.STRING)
//				.addScalar("operationNumber", StandardBasicTypes.STRING)
//				.addScalar("productReturnedOperationId", StandardBasicTypes.INTEGER)
//				.addScalar("canReturn", StandardBasicTypes.BOOLEAN)
//				.addScalar("operationStatus", StandardBasicTypes.STRING)
//				.addScalar("productReturnedOperationStatus", StandardBasicTypes.STRING)
//				.addScalar("receptionMode", StandardBasicTypes.STRING)
//				.addScalar("numberOfLine", StandardBasicTypes.INTEGER)
//				.setParameter("locationId", OperationUtils.getUserLocation())
//				.setResultTransformer(new AliasToBeanResultTransformer(ProductReportListDTO.class));
//		try {
//			return query.list();
//		} catch (HibernateException e) {
//			System.out.println(e.getMessage());
//		}
//		return null;
//	}

}
