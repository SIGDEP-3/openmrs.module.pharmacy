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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.api.db.ProductReportDAO;
import org.openmrs.module.pharmacy.entities.*;
import org.openmrs.module.pharmacy.enumerations.*;
import org.openmrs.module.pharmacy.dto.ProductReportLineDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of  {@link ProductReportDAO}.
 */
@Repository
public class HibernateProductReportDAO implements ProductReportDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DbSessionFactory sessionFactory;

//	public void setSessionFactory(DbSessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllProductReports(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.isNull("reportLocation")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllProductReports(Location location, ProductProgram productProgram, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.eq("productProgram", productProgram))
				.add(Restrictions.ne("operationStatus", OperationStatus.NOT_COMPLETED))
				.add(Restrictions.isNull("reportLocation"))
				.add(Restrictions.isNull("childLocationReport"))
				.list();
	}

	private ProductInventory getFirstInventoryDateOfLastPeriod(Location location) {
		ProductInventory returnedInventory = null;
		for (ProductProgram program : OperationUtils.getUserLocationPrograms()) {
			ProductInventory inventory = Context.getService(ProductInventoryService.class).getLastProductInventory(location, program, InventoryType.TOTAL);
			if (inventory != null) {
				if (returnedInventory == null) {
					returnedInventory = inventory;
				} else {
					if (returnedInventory.getOperationDate().before(inventory.getOperationDate())) {
						returnedInventory = inventory;
					}
				}
			}
		}
		return returnedInventory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllProductDistributionReports(Location location, Boolean includeVoided) {
		ProductInventory inventory = getFirstInventoryDateOfLastPeriod(location);
		if (inventory == null) {
			return null;
		} else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
			return criteria
					.add(Restrictions.eq("location", location))
					.add(Restrictions.eq("voided", includeVoided))
					.add(Restrictions.ge("operationDate", inventory.getOperationDate()))
					.add(Restrictions.isNotNull("reportLocation")).list();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllSubmittedChildProductReports(Location location, Boolean includeVoided) {
		ProductInventory inventory = getFirstInventoryDateOfLastPeriod(location);
		if (inventory == null) {
			return null;
		} else {
//			System.out.println("------------------------------------> Inventory date : " + OperationUtils.formatDate(inventory.getOperationDate()));
			return (List<ProductReport>) sessionFactory.getCurrentSession().createQuery(
					"SELECT r FROM ProductReport r " +
							"WHERE r.operationDate >= :operationDate " +
							"AND ((r.location = :location AND r.reportLocation IS NOT NULL AND r.reportLocation IN (:locations)) OR (r.location IN (:locations))) " +
							"AND (r.operationStatus = :submitted OR r.operationStatus = :validated OR r.operationStatus = :treated) " +
							"AND r.voided = :voided")
					.setParameter("operationDate", inventory.getOperationDate())
					.setParameter("location", location)
					.setParameterList("locations", location.getChildLocations())
					.setParameter("submitted", OperationStatus.SUBMITTED)
					.setParameter("validated", OperationStatus.VALIDATED)
					.setParameter("treated", OperationStatus.TREATED)
					.setParameter("voided", includeVoided)
					.list();
		}
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
//		if (location.getChildLocations() != null) {
//			return criteria
//					.add(
//							Restrictions.and(
//									Restrictions.and(
//											Restrictions.eq("voided", includeVoided),
//											Restrictions.eq("operationStatus", OperationStatus.SUBMITTED)
//									),
//									Restrictions.in("location", location.getChildLocations())
//							)
//					).list();
//		}
//		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllTreatedChildProductReports(Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		if (location.getChildLocations() != null) {
			return criteria
					.add(Restrictions.in("location", location.getChildLocations(includeVoided)))
					.add(Restrictions.eq("operationStatus", OperationStatus.TREATED))
					.add(Restrictions.eq("voided", includeVoided))
					.list();
		}
		return new ArrayList<>();
	}

	@Override
	public ProductReport getLastTreatedChildProductReports(Location location, Boolean includeVoided, ProductProgram productProgram, Date operationDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return (ProductReport) criteria
				.add(Restrictions.eq("reportLocation", location))
				.add(Restrictions.eq("operationStatus", OperationStatus.VALIDATED))
				.add(Restrictions.eq("productProgram", productProgram))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.lt("treatmentDate", operationDate))
				.addOrder(Order.desc("operationDate")).setMaxResults(1)
				.uniqueResult();
	}

	@Override
	public ProductReport getLastTreatedChildProductReportsByProduct(Product product, Location location, Boolean includeVoided, ProductProgram productProgram, Date operationDate) {
		return (ProductReport) sessionFactory.getCurrentSession().createQuery(
				"SELECT r FROM ProductReport r JOIN r.productAttributeFluxes f " +
				"WHERE r.reportLocation = :location AND f.productAttribute.product = :product " +
				"AND r.operationStatus = :operationStatus AND r.voided = :voided AND r.productProgram = :productProgram " +
				"AND r.treatmentDate < :operationDate " +
				"ORDER BY r.operationDate DESC ")
				.setParameter("location", location)
				.setParameter("operationStatus", OperationStatus.VALIDATED)
				.setParameter("productProgram", productProgram)
				.setParameter("product", product)
				.setParameter("operationDate", operationDate)
				.setParameter("voided", includeVoided)
				.setMaxResults(1).uniqueResult();

//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class, "R");
//		criteria.createAlias("R.productAttributeOtherFluxes", "F");
////		criteria.createAlias("F.product", "P");
//
//		return (ProductReport) criteria
//				.add(Restrictions.eq("R.reportLocation", location))
//				.add(Restrictions.eq("R.operationStatus", OperationStatus.VALIDATED))
//				.add(Restrictions.eq("R.productProgram", productProgram))
//				.add(Restrictions.eq("R.voided", includeVoided))
//				.add(Restrictions.eq("F.product", product))
//				.add(Restrictions.lt("R.treatmentDate", operationDate))
//				.addOrder(Order.desc("R.operationDate")).setMaxResults(1)
//				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getPeriodTreatedChildProductReports(Location location, ProductInventory inventory, Boolean includeVoided, Date operationDate) throws HibernateException {
//		System.out.println("------------------------------> Inventor.getInventoryStartDate() : " + inventory.getInventoryStartDate());
//		System.out.println("------------------------------> Inventor.getOperationDate() : " + inventory.getOperationDate());
//		System.out.println("------------------------------> location : " + location.getName());
		Date startDate = inventory.getOperationDate().before(operationDate) ? inventory.getOperationDate() : operationDate;
		Date endDate = inventory.getOperationDate().before(operationDate) ? operationDate : inventory.getOperationDate();
//		System.out.println("------------------------------> Start date : " + startDate);
//		System.out.println("------------------------------> End date : " + endDate);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return criteria
				.add(Restrictions.eq("reportLocation", location))
				.add(Restrictions.eq("operationStatus", OperationStatus.VALIDATED))
				.add(Restrictions.eq("productProgram", inventory.getProductProgram()))
				 .add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.between("treatmentDate", startDate, endDate))
				// .addOrder(Order.desc("operationDate"))
				.list();
	}

	@Override
	public Integer getCountProductQuantityInLastTreatment(Location location, Boolean includeVoided, ProductProgram productProgram, Date operationDate, Product product) {
		ProductReport productReport = getLastTreatedChildProductReports(location, includeVoided, productProgram, operationDate);
		if (productReport != null) {
			for (ProductAttributeFlux flux : productReport.getProductAttributeFluxes()) {
				if (flux.getProductAttribute().getProduct().equals(product)) {
					return flux.getQuantity();
				}
			}
		}
		return 0;
	}

	@Override
	public Integer getCountProductQuantityInPeriodTreatment(Location location, ProductInventory inventory, Boolean includeVoided, Date operationDate, Product product) {
		List<ProductReport> productReports = getPeriodTreatedChildProductReports(location, inventory, includeVoided, operationDate);
		if (productReports != null) {
			Integer quantity = 0;
			for (ProductReport report : productReports) {
				for (ProductAttributeFlux flux : report.getProductAttributeFluxes()) {
					if (flux.getProductAttribute().getProduct().equals(product)) {
						quantity += flux.getQuantity();
					}
				}
			}
			return quantity;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductReport> getAllProductReports(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.add(Restrictions.isNull("reportLocation"))
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
	public ProductReport getOneProductReportByReportPeriodAndProgram(String reportPeriod, ProductProgram productProgram, Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return (ProductReport) criteria
				.add(Restrictions.eq("reportPeriod", reportPeriod))
				.add(Restrictions.eq("productProgram", productProgram))
				.add(Restrictions.eq("location", location))
				.add(Restrictions.ne("isUrgent", false))
				.add(Restrictions.isNull("reportLocation"))
				.add(Restrictions.eq("voided", includeVoided))
				.uniqueResult();
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
//		String sqlQuery =
//				"SELECT " +
//						"ppaf.product_attribute_flux_id as productAttributeFluxId, " +
//						"ppr.product_operation_id as productOperationId, " +
//						"pp.product_id as productId, " +
//						"pp.code as code, " +
//						"pp.retail_name as retailName, " +
//						"pp.wholesale_name as wholesaleName, " +
//						"pp.unit_conversion as unitConversion, " +
//						"ppu.name as retailUnit, " +
//						"ppu2.name as wholesaleUnit, " +
//						"ppa.batch_number as batchNumber, " +
//						"ppa.expiry_date as expiryDate, " +
//						"ppaf.quantity as quantity, " +
//						"ppaof.quantity as quantityToDeliver, " +
//						"ppaf.observation as observation, " +
//						"ppaf.date_created as dateCreated " +
//						"FROM pharmacy_product_reception ppr " +
//						"LEFT JOIN pharmacy_product_operation ppo on ppr.product_operation_id = ppo.product_operation_id " +
//						"LEFT JOIN pharmacy_product_attribute_flux ppaf on ppo.product_operation_id = ppaf.operation_id " +
//						"LEFT JOIN pharmacy_product_attribute ppa on ppaf.product_attribute_id = ppa.product_attribute_id " +
//						"LEFT JOIN pharmacy_product_attribute_other_flux ppaof on ppo.product_operation_id = ppaof.operation_id AND ppaof.product_attribute_id = ppa.product_attribute_id " +
//						"LEFT JOIN pharmacy_product pp ON ppa.product_id = pp.product_id " +
//						"LEFT JOIN pharmacy_product_unit ppu on pp.product_retail_unit = ppu.product_unit_id " +
//						"LEFT JOIN pharmacy_product_unit ppu2 on pp.product_wholesale_unit = ppu2.product_unit_id " +
//						"WHERE ppr.product_operation_id = :productOperationId AND product_attribute_flux_id IS NOT NULL " +
//						"ORDER BY ppaf.date_created DESC ";
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
//				.addScalar("quantityToDeliver", StandardBasicTypes.INTEGER)
//				.addScalar("unitConversion", StandardBasicTypes.DOUBLE)
//				.addScalar("quantity", StandardBasicTypes.INTEGER)
//				.addScalar("observation", StandardBasicTypes.STRING)
//				.addScalar("dateCreated", StandardBasicTypes.DATE)
//				.setParameter("productOperationId", productReport.getProductOperationId())
//				.setResultTransformer(new AliasToBeanResultTransformer(ProductReportLineDTO.class));
//		try {
//			return query.list();
//		} catch (HibernateException e) {
//			System.out.println(e.getMessage());
//		}
		return null;
	}

	@Override
	public Integer getProductReceivedQuantityInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) throws HibernateException {
		Integer quantity = 0;
		List<ProductReport> lastDistributions = getPeriodTreatedChildProductReports(
				location,
				inventory, false,
				inventory.getInventoryStartDate());

		if (lastDistributions != null &&lastDistributions.size() > 0) {
			for (ProductReport report : lastDistributions) {
				for (ProductAttributeFlux flux : report.getProductAttributeFluxes()) {
					if (flux.getProductAttribute().getProduct().equals(product)) {
						quantity += flux.getQuantity();
					}
				}
			}
//			System.out.println("-------------------------------> Returning the distribution quantity");
			return quantity;
		}

		if (isUrgent) {
			return getCurrentMonthReceivedQuantity(product, inventory);
		}

		List<ProductReception> productReceptions = Context.getService(ProductReceptionService.class).getAllProductReceptions(location, false, inventory.getInventoryStartDate(), inventory.getOperationDate());

		for (ProductReception reception :
				productReceptions) {
			quantity += getFluxQuantity(reception, product);
//			System.out.println("-----------------------------> Returning the all quantity received");
		}
		return quantity;
	}

	@Override
	public Integer getProductQuantityInStockOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) throws HibernateException {
		if (isUrgent) {
			return Context.getService(ProductAttributeStockService.class)
					.getAllProductAttributeStockByProductCount(product, inventory.getProductProgram(), location, false);
		}
		Double quantity = 0.0;
		for (ProductAttributeFlux flux : inventory.getProductAttributeFluxes()) {
			if (flux.getProductAttribute().getProduct().equals(product) && flux.getStatus().equals(OperationStatus.VALIDATED)) {
				quantity += flux.getQuantity();
			}
		}

		return getChildLocationReportProductQuantity(location, product, inventory, "SDU", quantity);
	}

	@Override
	public Integer getProductInitialQuantityByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) throws HibernateException {
		Double quantity = 0.0;
		if (isUrgent) {
			return getCurrentMonthInitialQuantity(product, inventory);
		}
		ProductInventory inventoryBeforeLatest = Context.getService(ProductInventoryService.class).getLastProductInventoryByDate(location, inventory.getProductProgram(), inventory.getOperationDate(), InventoryType.TOTAL);

		for (ProductAttributeFlux flux : inventoryBeforeLatest.getProductAttributeFluxes()) {
			if (flux.getProductAttribute().getProduct().equals(product) && flux.getStatus().equals(OperationStatus.VALIDATED)) {
				quantity += flux.getQuantity();
			}
		}
		return getChildLocationReportProductQuantity(location, product, inventory, "SI", quantity);
	}

	@Override
	public Integer getProductQuantityLostInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) throws HibernateException {
		Double quantity = 0.0;
		if (isUrgent) {
			return getCurrentMonthLostQuantity(product, inventory);
		}
		List<ProductMovementOut> productMovementOuts = Context.getService(ProductMovementService.class).getAllProductMovementOut(location, false, inventory.getInventoryStartDate(), inventory.getOperationDate());
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
		return getChildLocationReportProductQuantity(location, product, inventory, "QL", quantity);
	}

	@Override
	public Integer getProductQuantityAdjustmentInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) throws HibernateException {
		if (isUrgent) {
			return getCurrentMonthAdjustmentQuantity(product, inventory);
		}
		Double quantity = 0.0;
		List<ProductTransfer> productTransfers = Context.getService(ProductTransferService.class).getAllProductTransfers(location, false, inventory.getInventoryStartDate(), inventory.getOperationDate());

		for (ProductTransfer productTransfer : productTransfers) {
			quantity += getFluxQuantity(productTransfer, product);
		}

		List<ProductBackSupplier> productBackSuppliers = Context.getService(ProductBackSupplierService.class).getAllProductBackSuppliers(location, false);
		for (ProductBackSupplier backSupplier : productBackSuppliers) {
			quantity += getFluxQuantity(backSupplier, product);
		}

		List<ProductMovementEntry> productMovementEntries = Context.getService(ProductMovementService.class).getAllProductMovementEntry(location, false);
		for (ProductMovementEntry movementEntry : productMovementEntries) {
			quantity += getFluxQuantity(movementEntry, product);
		}

		return getChildLocationReportProductQuantity(location, product, inventory, "QA", quantity);
	}

	private Integer getFluxQuantity(ProductOperation operation, Product product) throws HibernateException {
		Integer quantity = 0;
		for (ProductAttributeFlux flux : operation.getProductAttributeFluxes()) {
			if (flux.getProductAttribute().getProduct().equals(product) && flux.getStatus().equals(OperationStatus.VALIDATED)) {
				if (operation.getIncidence().equals(Incidence.POSITIVE)) {
					quantity += flux.getQuantity();
				} else if (operation.getIncidence().equals(Incidence.NEGATIVE)) {
					quantity -= flux.getQuantity();
				}
			}
		}
		return quantity;
	}

	@Override
	public Integer getChildLocationsThatKnownRupture(Product product, ProductInventory inventory, Location location) throws HibernateException {
		if (location.getChildLocations().size() != 0) {
			Integer quantity = 0;
			String reportPeriod = inventory.getOperationNumber().split("-")[1];
			for (Location childLocation : location.getChildLocations()) {
				ProductReport report = getOneProductReportByReportPeriodAndProgram(reportPeriod, inventory.getProductProgram(), childLocation, false);
				if (report != null) {
					ProductAttributeOtherFlux otherFlux = Context.getService(ProductAttributeFluxService.class)
							.getOneProductAttributeOtherFluxByProductAndOperationAndLabel(
									product,
									report,
									"NDR",
									childLocation
							);
					if (otherFlux != null && otherFlux.getQuantity() > 0) {
						quantity += 1;
						break;
					}
				}
			}
			return quantity;
		}
		return 0;
	}

	@Override
	public Integer getProductQuantityDistributedInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) throws HibernateException {
//		System.out.println("--------------------------------------> getDistributionQuantity " + quantity);
		if (isUrgent) {
			return getCurrentMonthQuantityDistributed(product, inventory);
		}
		Double quantity = 0.;
		quantity = getDistributionQuantity(product, inventory, location);
		return getChildLocationReportProductQuantity(location, product, inventory, "QD", quantity);
	}

	private Integer getCurrentMonthQuantityDistributed(Product product, ProductInventory inventory) {
		Integer quantity = 0;
		ProductInventory lastPartialInventory = Context.getService(ProductInventoryService.class).getLastProductInventory(
				inventory.getLocation(),
				inventory.getProductProgram(),
				InventoryType.PARTIAL
		);
		List<ProductDispensation> dispensations = Context.getService(ProductDispensationService.class)
				.getAllProductDispensations(
						inventory.getProductProgram(),
						inventory.getLocation(),
						false,
						OperationUtils.addDayToDate(inventory.getOperationDate(), 1),
						lastPartialInventory.getOperationDate()
				);
		for (ProductDispensation dispensation : dispensations) {
			for (ProductAttributeFlux flux : dispensation.getProductAttributeFluxes()) {
				if (flux.getProductAttribute().getProduct().equals(product))
					quantity += flux.getQuantity();
			}
		}

		return quantity;
	}

	private Integer getCurrentMonthInitialQuantity(Product product, ProductInventory inventory) throws HibernateException {
		Integer quantity = 0;
		for (ProductAttributeFlux flux : inventory.getProductAttributeFluxes()) {
			if (flux.getProductAttribute().getProduct().equals(product))
				quantity += flux.getQuantity();
		}

		return quantity;
	}

	private Integer getCurrentMonthReceivedQuantity(Product product, ProductInventory inventory) throws HibernateException {
		Integer quantity = 0;
		ProductInventory lastPartialInventory = Context.getService(ProductInventoryService.class).getLastProductInventory(
				inventory.getLocation(),
				inventory.getProductProgram(),
				InventoryType.PARTIAL
		);
		List<ProductReception> receptions = Context.getService(ProductReceptionService.class)
				.getAllProductReceptions(
						inventory.getProductProgram(),
						inventory.getLocation(), false,
						OperationUtils.addDayToDate(inventory.getOperationDate(), 1),
						lastPartialInventory.getOperationDate()
				);
		for (ProductReception reception : receptions) {
			for (ProductAttributeFlux flux : reception.getProductAttributeFluxes()) {
				if (flux.getProductAttribute().getProduct().equals(product))
					quantity += flux.getQuantity();
			}
		}

		return quantity;
	}

	private Integer getCurrentMonthLostQuantity(Product product, ProductInventory inventory) throws HibernateException {
		Integer quantity = 0;
		ProductInventory lastPartialInventory = Context.getService(ProductInventoryService.class).getLastProductInventory(
				inventory.getLocation(),
				inventory.getProductProgram(),
				InventoryType.PARTIAL
		);
		List<ProductMovementOut> movementOuts = Context.getService(ProductMovementService.class)
				.getAllProductMovementOut(
						inventory.getProductProgram(),
						inventory.getLocation(),
						false,
						OperationUtils.addDayToDate(inventory.getOperationDate(), 1),
						lastPartialInventory.getOperationDate()
				);
		for (ProductMovementOut movementOut : movementOuts) {
			for (ProductAttributeFlux flux : movementOut.getProductAttributeFluxes()) {
				if (flux.getProductAttribute().getProduct().equals(product))
					quantity += flux.getQuantity();
			}
		}

		return quantity;
	}

	private Integer getCurrentMonthAdjustmentQuantity(Product product, ProductInventory inventory) throws HibernateException {
		Integer quantity = 0;
		ProductInventory lastPartialInventory = Context.getService(ProductInventoryService.class).getLastProductInventory(
				inventory.getLocation(),
				inventory.getProductProgram(),
				InventoryType.PARTIAL
		);
		List<ProductMovementEntry> movementEntries = Context.getService(ProductMovementService.class)
				.getAllProductMovementEntry(
						inventory.getProductProgram(),
						inventory.getLocation(), false,
						OperationUtils.addDayToDate(inventory.getOperationDate(), 1),
						lastPartialInventory.getOperationDate()
				);
		for (ProductMovementEntry movementEntry : movementEntries) {
			for (ProductAttributeFlux flux : movementEntry.getProductAttributeFluxes()) {
				if (flux.getProductAttribute().getProduct().equals(product))
					quantity += flux.getQuantity();
			}
		}

		return quantity;
	}

	@Override
	public Integer getProductQuantityDistributedInAgo1MonthOperationByProduct(Product product, ProductInventory inventory, Location location) throws HibernateException {
		ProductReport report = getLastProductReportByDate(location, inventory.getProductProgram(), inventory.getOperationDate(), null);
		if (report != null) {
			return getReportQuantityDistributed(report, product, "QD").intValue();
		}
		ProductInventory inventoryBefore = Context.getService(ProductInventoryService.class).getLastProductInventoryByDate(inventory.getLocation(), inventory.getProductProgram(),inventory.getOperationDate(), InventoryType.TOTAL);
		return getDistributionQuantity(product, inventoryBefore, location).intValue();
	}

	@Override
	public Integer getProductQuantityDistributedInAgo2MonthOperationByProduct(Product product, ProductInventory inventory, Location location) throws HibernateException {
		ProductReport report = getLastProductReportByDate(location,
				inventory.getProductProgram(),
				inventory.getInventoryStartDate(),
				null
		);
		if (report != null) {
			return getReportQuantityDistributed(report, product, "DM1").intValue();
		}
		ProductInventory inventoryBefore = Context.getService(ProductInventoryService.class).getLastProductInventoryByDate(inventory.getLocation(), inventory.getProductProgram(),inventory.getOperationDate(), InventoryType.TOTAL);
		ProductInventory inventoryBeforeAntLast = Context.getService(ProductInventoryService.class).getLastProductInventoryByDate(inventory.getLocation(), inventoryBefore.getProductProgram(),inventoryBefore.getOperationDate(), InventoryType.TOTAL);
		if (inventoryBeforeAntLast == null) {
			return 0;
		}
		return getDistributionQuantity(product, inventoryBeforeAntLast, location).intValue();
	}

	private Double getDistributionQuantity(Product product, ProductInventory inventory, Location location) throws HibernateException {
		List<ProductDispensation> dispensations = Context.getService(ProductDispensationService.class).getAllProductDispensations(location, false, inventory.getInventoryStartDate(), inventory.getOperationDate());
		Double quantity = 0.0;
		for (ProductDispensation dispensation : dispensations) {
			for (ProductAttributeFlux flux : dispensation.getProductAttributeFluxes()) {
				if (flux.getProductAttribute().getProduct().equals(product) && flux.getStatus().equals(OperationStatus.VALIDATED)) {
//					System.out.println("--------------------------------------> flux.getQuantity() " + flux.getQuantity());
					quantity += flux.getQuantity();
				}
			}
		}
		return quantity;
	}

	private Double getReportQuantityDistributed(ProductReport report, Product product, String label) throws HibernateException {
		ProductAttributeOtherFlux otherFlux = Context.getService(ProductAttributeFluxService.class)
				.getOneProductAttributeOtherFluxByProductAndOperationAndLabel(
						product,
						report,
						label,
						report.getLocation()
				);
		if (otherFlux != null) {
			return otherFlux.getQuantity();
		}

		return 0.0;
	}

	private Integer getChildLocationReportProductQuantity(Location location, Product product, ProductInventory inventory, String label, Double quantity) throws HibernateException {
		if (location.getChildLocations().size() != 0) {
			String reportPeriod = inventory.getOperationNumber().split("-")[1];
			for (Location childLocation : location.getChildLocations()) {
				ProductReport report = getOneProductReportByReportPeriodAndProgram(reportPeriod, inventory.getProductProgram(), childLocation, false);
				if (report != null) {
					ProductAttributeOtherFlux otherFlux = Context.getService(ProductAttributeFluxService.class)
							.getOneProductAttributeOtherFluxByProductAndOperationAndLabel(
									product,
									report,
									label,
									report.getLocation()
							);
					if (otherFlux != null) {
						quantity += otherFlux.getQuantity();
					}
				}
			}
		}
		return quantity.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double getProductAverageMonthlyConsumption(Product product, ProductProgram productProgram, Location location, Boolean includeVoided) throws HibernateException {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		int months = OperationUtils.getMonthsForCMM();
		List<ProductReport> reports = criteria
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("productProgram", productProgram))
				.add(Restrictions.eq("voided", includeVoided))
				.addOrder(Order.desc("operationDate")).setMaxResults(months)
				.list();

		if (reports != null && reports.size() != 0) {
			int countReport = 0;
			Double quantity = 0.0;
			for (ProductReport report : reports) {
				ProductAttributeOtherFlux otherFlux = Context.getService(ProductAttributeFluxService.class)
						.getOneProductAttributeOtherFluxByProductAndOperationAndLabel(
								product,
								report,
								"QD",
								report.getLocation()
						);
				if (otherFlux != null) {
					quantity += otherFlux.getQuantity();
				}
				countReport += 1;
				if (countReport == months) {
					return quantity / months;
				}
			}
			return quantity / countReport;
		}

		return 0.0;
	}

	@Override
	public List<Product> getAllActivityProducts(ProductInventory inventory) {
		List<ProductAttributeFlux> fluxes = Context.getService(ProductAttributeFluxService.class).getAllProductAttributeFluxes(inventory.getLocation(), inventory.getInventoryStartDate(), inventory.getOperationDate(), false);
		List<Product> products = new ArrayList<>();
		for (ProductAttributeFlux flux :
				fluxes) {
			if (!products.contains(flux.getProductAttribute().getProduct()))
				products.add(flux.getProductAttribute().getProduct());
		}
		return products;
	}

	@Override
	public ProductReport getLastProductReport(Location location, ProductProgram productProgram, Boolean urgent) {
		ProductInventory inventory = Context.getService(ProductInventoryService.class).getLastProductInventory(location, productProgram, InventoryType.TOTAL);
// Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		ProductReport report;
		if (urgent) {
			report = (ProductReport) sessionFactory.getCurrentSession().createQuery(
					"SELECT r FROM ProductReport r " +
							"WHERE r.location = :location " +
							"AND (r.operationStatus = :operationValidated OR r.operationStatus = :operationTreated) " +
							"AND r.operationDate >= :operationDate " +
							"AND r.voided = false AND r.productProgram = :productProgram AND r.isUrgent = true " +
							"ORDER BY r.operationDate DESC")
					.setParameter("location", location)
					.setParameter("operationDate", inventory.getOperationDate())
					.setParameter("operationValidated", OperationStatus.VALIDATED)
					.setParameter("operationTreated", OperationStatus.TREATED)
					.setParameter("productProgram", productProgram)
					.setMaxResults(1).uniqueResult();
			if (report != null) {
				return report;
			}
		}
		report = (ProductReport) sessionFactory.getCurrentSession().createQuery(
				"SELECT r FROM ProductReport r " +
						"WHERE r.location = :location " +
						"AND (r.operationStatus = :operationValidated OR r.operationStatus = :operationTreated) " +
						"AND r.voided = false AND r.productProgram = :productProgram AND r.isUrgent = false " +
						"ORDER BY r.operationDate DESC"
		).setParameter("location", location)
				.setParameter("operationValidated", OperationStatus.VALIDATED)
				.setParameter("operationTreated", OperationStatus.TREATED)
				.setParameter("productProgram", productProgram)
				.setMaxResults(1).uniqueResult();

		return report;
	}

	@Override
	public ProductReport getLastProductReportByDate(Location location, ProductProgram productProgram, Date reportDate, Boolean urgent) {
		ProductReport report;
		ProductInventory inventory = Context.getService(ProductInventoryService.class).getLastProductInventory(location, productProgram, InventoryType.TOTAL);

		// Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		if (urgent == null) {
//			System.out.println("------------------------------------> Urgent is null !");
			return (ProductReport) sessionFactory.getCurrentSession().createQuery(
					"SELECT r FROM ProductReport r " +
							"WHERE r.location = :location " +
							"AND (r.operationStatus = :operationValidated OR r.operationStatus = :operationTreated) " +
							"AND r.voided = false AND r.productProgram = :productProgram " +
							"AND r.operationDate < :operationDate " +
							"AND r.operationDate >= :inventoryDate " +
							"ORDER BY r.operationDate DESC "
			).setParameter("location", location)
					.setParameter("operationValidated", OperationStatus.VALIDATED)
					.setParameter("operationTreated", OperationStatus.TREATED)
					.setParameter("productProgram", productProgram)
					.setParameter("operationDate", reportDate)
					.setParameter("inventoryDate", inventory.getOperationDate())
					.setMaxResults(1).uniqueResult();
		}
		if (urgent) {
			report = (ProductReport) sessionFactory.getCurrentSession().createQuery(
					"SELECT r FROM ProductReport r " +
							"WHERE r.location = :location " +
							"AND (r.operationStatus = :operationValidated OR r.operationStatus = :operationTreated) " +
							"AND r.voided = false AND r.productProgram = :productProgram " +
							"AND r.operationDate < :operationDate " +
							"AND r.operationDate >= :inventoryDate " +
							"AND r.isUrgent = true " +
							"ORDER BY r.operationDate DESC "
			).setParameter("location", location)
					.setParameter("operationValidated", OperationStatus.VALIDATED)
					.setParameter("operationTreated", OperationStatus.TREATED)
					.setParameter("productProgram", productProgram)
					.setParameter("operationDate", reportDate)
					.setParameter("inventoryDate", inventory.getOperationDate())
					.setMaxResults(1).uniqueResult();
			if (report != null) {
				return report;
			}
		}

		return  (ProductReport) sessionFactory.getCurrentSession().createQuery(
				"SELECT r FROM ProductReport r " +
						"WHERE r.location = :location " +
						"AND (r.operationStatus = :operationValidated OR r.operationStatus = :operationTreated) " +
						"AND r.voided = false AND r.productProgram = :productProgram " +
						"AND r.operationDate < :operationDate " +
						"AND r.isUrgent = false " +
						"ORDER BY r.operationDate DESC ")
				.setParameter("location", location)
				.setParameter("operationValidated", OperationStatus.VALIDATED)
				.setParameter("operationTreated", OperationStatus.TREATED)
				.setParameter("productProgram", productProgram)
				.setParameter("operationDate", reportDate)
				.setMaxResults(1).uniqueResult();
	}

	@Override
	public List<ProductReportLineDTO> getReportDistributionLines(ProductReport report) {
		List<ProductReportLineDTO> reportLineDTOS = new ArrayList<>();

		List<Product> products = report.getOtherFluxesProductList();
		for (Product product : products) {
			ProductReportLineDTO productReportLineDTO = new ProductReportLineDTO();

			List<ProductAttributeOtherFlux> otherFluxes =
					Context.getService(ProductAttributeFluxService.class).getAllProductAttributeOtherFluxByProductAndOperation(
							product,
							report,
							report.getLocation());

			if (otherFluxes.size() != 0) {
				productReportLineDTO.setProductId(product.getProductId());
				productReportLineDTO.setCode(product.getCode());
				productReportLineDTO.setRetailName(product.getRetailName());
				productReportLineDTO.setRetailUnit(product.getProductRetailUnit().getName());

				for (ProductAttributeOtherFlux otherFlux : otherFluxes) {
					switch (otherFlux.getLabel()) {
						case "SI":
							productReportLineDTO.setInitialQuantity(otherFlux.getQuantity().intValue());
							break;
						case "QR":
							productReportLineDTO.setReceivedQuantity(otherFlux.getQuantity().intValue());
							break;
						case "QD":
							productReportLineDTO.setDistributedQuantity(otherFlux.getQuantity().intValue());
							break;
						case "QL":
							productReportLineDTO.setLostQuantity(otherFlux.getQuantity().intValue());
							break;
						case "QA":
							productReportLineDTO.setAdjustmentQuantity(otherFlux.getQuantity().intValue());
							break;
						case "SDU":
							productReportLineDTO.setQuantityInStock(otherFlux.getQuantity().intValue());
							break;
						case "NDR":
							productReportLineDTO.setNumDaysOfRupture(otherFlux.getQuantity().intValue());
							break;
						case "DM1":
							productReportLineDTO.setQuantityDistributed1monthAgo(otherFlux.getQuantity().intValue());
							break;
						case "DM2":
							productReportLineDTO.setQuantityDistributed2monthAgo(otherFlux.getQuantity().intValue());
							break;
					}
				}
				reportLineDTOS.add(productReportLineDTO);
			}
		}

		return reportLineDTOS;
	}

	@Override
	public ProductAttributeOtherFlux getPreviousReportProductAttributeOtherFluxByLabel(Product product, String label, ProductReport report, Location location) {
		ProductReport lastReport = getLastProductReportByProductAndByDate(location, report.getProductProgram(), product, report.getOperationDate(), report.getUrgent());
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductAttributeOtherFlux.class);
		return (ProductAttributeOtherFlux) criteria
//				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("productOperation", lastReport))
				.add(Restrictions.eq("product", product))
				.add(Restrictions.eq("label", label)).setMaxResults(1).uniqueResult();
	}


	@Override
	public ProductReport getLastProductReportByProductAndByDate(Location location, ProductProgram productProgram, Product product, Date reportDate, Boolean urgent) {
		ProductInventory inventory = Context.getService(ProductInventoryService.class).getLastProductInventory(location, productProgram, InventoryType.TOTAL);

		ProductReport report;

		// Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		if (urgent == null) {
//			System.out.println("------------------------------------> Urgent is null !");
			return (ProductReport) sessionFactory.getCurrentSession().createQuery(
					"SELECT r FROM ProductReport r JOIN r.productAttributeOtherFluxes f " +
							"WHERE r.location = :location " +
							"AND f.product = :product " +
							"AND (r.operationStatus = :operationValidated OR r.operationStatus = :operationTreated) " +
							"AND r.voided = false AND r.productProgram = :productProgram " +
							"AND r.operationDate < :operationDate " +
							"AND r.operationDate >= :inventoryDate " +
							"ORDER BY r.operationDate DESC "
			).setParameter("location", location)
					.setParameter("operationValidated", OperationStatus.VALIDATED)
					.setParameter("product", product)
					.setParameter("operationTreated", OperationStatus.TREATED)
					.setParameter("productProgram", productProgram)
					.setParameter("operationDate", reportDate)
					.setParameter("inventoryDate", inventory.getOperationDate())
					.setMaxResults(1).uniqueResult();
		}
		if (urgent) {
			report = (ProductReport) sessionFactory.getCurrentSession().createQuery(
					"SELECT r FROM ProductReport r JOIN r.productAttributeOtherFluxes f " +
							"WHERE r.location = :location " +
							"AND f.product = :product " +
							"AND (r.operationStatus = :operationValidated OR r.operationStatus = :operationTreated) " +
							"AND r.voided = false AND r.productProgram = :productProgram " +
							"AND r.operationDate < :operationDate " +
							"AND r.operationDate >= :inventoryDate " +
							"AND r.isUrgent = true " +
							"ORDER BY r.operationDate DESC "
			).setParameter("location", location)
					.setParameter("product", product)
					.setParameter("operationValidated", OperationStatus.VALIDATED)
					.setParameter("operationTreated", OperationStatus.TREATED)
					.setParameter("productProgram", productProgram)
					.setParameter("operationDate", reportDate)
					.setParameter("inventoryDate", inventory.getOperationDate())
					.setMaxResults(1).uniqueResult();
			if (report != null) {
				return report;
			}
		}

		return  (ProductReport) sessionFactory.getCurrentSession().createQuery(
				"SELECT r FROM ProductReport r JOIN r.productAttributeOtherFluxes f " +
						"WHERE r.location = :location " +
						"AND f.product = :product " +
						"AND (r.operationStatus = :operationValidated OR r.operationStatus = :operationTreated) " +
						"AND r.voided = false AND r.productProgram = :productProgram " +
						"AND r.operationDate < :operationDate " +
						"AND r.isUrgent = false " +
						"ORDER BY r.operationDate DESC ")
				.setParameter("location", location)
				.setParameter("product", product)
				.setParameter("operationValidated", OperationStatus.VALIDATED)
				.setParameter("operationTreated", OperationStatus.TREATED)
				.setParameter("productProgram", productProgram)
				.setParameter("operationDate", reportDate)
				.setMaxResults(1).uniqueResult();
	}

	@Override
	public ProductReport getPeriodTreatedProductReportsByReportPeriodAndLocation(String reportPeriod, ProductProgram program, Location childLocation, boolean isUrgent) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductReport.class);
		return (ProductReport) criteria
				.add(Restrictions.eq("reportLocation", childLocation))
				.add(Restrictions.eq("productProgram", program))
				.add(Restrictions.eq("reportPeriod", reportPeriod))
				.add(Restrictions.eq("operationStatus", OperationStatus.VALIDATED))
				.add(Restrictions.eq("isUrgent", isUrgent)).setMaxResults(1).uniqueResult();
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
