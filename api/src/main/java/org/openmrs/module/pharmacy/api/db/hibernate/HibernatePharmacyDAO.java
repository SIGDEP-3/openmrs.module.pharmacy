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
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeStockService;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.dto.*;
import org.openmrs.module.pharmacy.entities.ProductAttributeFlux;
import org.openmrs.module.pharmacy.entities.ProductAttributeStock;
import org.openmrs.module.pharmacy.entities.ProductOperation;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
public class HibernatePharmacyDAO implements PharmacyDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	private SessionFactory sessionFactory;

	private final HibernateProductAttributeFluxDAO productAttributeFluxDAO;
	private final HibernateProductAttributeStockDAO productAttributeStockDAO;

	public HibernatePharmacyDAO(HibernateProductAttributeFluxDAO productAttributeFluxDAO,
								HibernateProductAttributeStockDAO productAttributeStockDAO) {
		this.productAttributeFluxDAO = productAttributeFluxDAO;
		this.productAttributeStockDAO = productAttributeStockDAO;
	}

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

	public HibernateProductAttributeFluxDAO getProductAttributeFluxDAO() {
		return productAttributeFluxDAO;
	}

	public HibernateProductAttributeStockDAO getProductAttributeStockDAO() {
		return productAttributeStockDAO;
	}

	@Override
	@Transactional
	public Boolean validateOperation(ProductOperation operation) {
		if (!operation.getIncidence().equals(Incidence.NONE)) {
			if (!operation.getOperationStatus().equals(OperationStatus.VALIDATED)) {
				Set<ProductAttributeFlux> fluxes = operation.getProductAttributeFluxes();
				if (fluxes != null && fluxes.size() != 0) {
					for (ProductAttributeFlux flux : fluxes) {
						Integer quantityInStock = 0;

						ProductAttributeStock attributeStock = Context.getService(ProductAttributeStockService.class)
								.getOneProductAttributeStockByAttribute(flux.getProductAttribute(), operation.getLocation(), false);
						if (attributeStock != null) {
//							System.out.println("--------------------------------------> attribute stock found");
							quantityInStock = attributeStock.getQuantityInStock();
//							System.out.println("--------------------------------------> got quantity");
							attributeStock.setVoided(true);
							attributeStock.setDateVoided(new Date());
							attributeStock.setVoidedBy(Context.getAuthenticatedUser());
							attributeStock.setVoidReason("Voided by user because not to be used");
							Context.getService(ProductAttributeStockService.class).saveProductAttributeStock(attributeStock);
//							System.out.println("--------------------------------------> attribute stock voided");
						}

						Integer quantity = operation.getIncidence().equals(Incidence.POSITIVE) ?
								quantityInStock + flux.getQuantity() :
								(operation.getIncidence().equals(Incidence.NEGATIVE) ? quantityInStock - flux.getQuantity() : flux.getQuantity());

						attributeStock = new ProductAttributeStock();
						attributeStock.setQuantityInStock(quantity);
						attributeStock.setLocation(operation.getLocation());
						attributeStock.setProductAttribute(flux.getProductAttribute());
						attributeStock.setOperation(operation);
						Context.getService(ProductAttributeStockService.class).saveProductAttributeStock(attributeStock);

						flux.setStatus(OperationStatus.VALIDATED);
						Context.getService(ProductAttributeFluxService.class).saveProductAttributeFlux(flux);
					}
				}

			}
		}
		operation.setOperationStatus(OperationStatus.VALIDATED);
		return saveProductOperation(operation) != null;
	}

	@Override
	public Boolean cancelOperation(ProductOperation operation) {
		if (!operation.getIncidence().equals(Incidence.NONE)) {
			if (operation.getOperationStatus().equals(OperationStatus.VALIDATED)) {
				Set<ProductAttributeFlux> fluxes = operation.getProductAttributeFluxes();
				if (fluxes != null && fluxes.size() != 0) {
					for (ProductAttributeFlux flux : fluxes) {
						if (flux.getStatus().equals(OperationStatus.VALIDATED)) {
							Integer quantityInStock = 0;
							ProductAttributeStock attributeStock = Context.getService(ProductAttributeStockService.class)
									.getOneProductAttributeStockByAttribute(
											flux.getProductAttribute(),
											operation.getLocation(), false);
							if (attributeStock != null) {
								quantityInStock = attributeStock.getQuantityInStock();
								attributeStock.setVoided(true);
								attributeStock.setDateVoided(new Date());
								attributeStock.setVoidedBy(Context.getAuthenticatedUser());
								attributeStock.setVoidReason("Voided by user because not to be used");
								Context.getService(ProductAttributeStockService.class).saveProductAttributeStock(attributeStock);
							}
							Integer quantity = operation.getIncidence().equals(Incidence.POSITIVE) ?
									quantityInStock - flux.getQuantity() :
									(operation.getIncidence().equals(Incidence.NEGATIVE) ? quantityInStock + flux.getQuantity() : flux.getQuantity());

							attributeStock = new ProductAttributeStock();
							attributeStock.setQuantityInStock(quantity);
							attributeStock.setLocation(operation.getLocation());
							attributeStock.setProductAttribute(flux.getProductAttribute());
							attributeStock.setOperation(operation);

							Context.getService(ProductAttributeStockService.class).saveProductAttributeStock(attributeStock);

							flux.setStatus(OperationStatus.DISABLED);
							Context.getService(ProductAttributeFluxService.class).saveProductAttributeFlux(flux);
						}
					}
				}
			}
		}
		operation.setOperationStatus(OperationStatus.DISABLED);
		return saveProductOperation(operation) != null;
	}

	@Override
	public ProductOperation getOneProductOperationById(Integer productOperationId) {
		return (ProductOperation) sessionFactory.getCurrentSession().get(ProductOperation.class, productOperationId);
	}

	@Override
	public ProductOperation getOneProductOperationByOperationNumber(String operationNumber, Incidence incidence) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductOperation.class);
		return (ProductOperation) criteria.add(Restrictions.eq("operationNumber", operationNumber))
				.add(Restrictions.eq("incidence", incidence)).uniqueResult();
	}

	@Override
	public ProductOperation getOneProductOperationByOperationDateAndProductProgram(Date operationDate, ProductProgram productProgram, Location location, Boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductOperation.class);
		return (ProductOperation) criteria
				.add(Restrictions.eq("operationDate", operationDate))
				.add(Restrictions.eq("productProgram", productProgram))
				.add(Restrictions.eq("location", location))
				.add(Restrictions.eq("voided", includeVoided))
				.uniqueResult();
	}

	@Override
	public ProductOperation saveProductOperation(ProductOperation productOperation) {
		sessionFactory.getCurrentSession().saveOrUpdate(productOperation);
		return productOperation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductOutFluxDTO> getProductOutFluxDTOs(ProductOperation productOperation) {
		String sqlQuery =
				"SELECT " +
						"DISTINCT ppaf.product_attribute_flux_id productAttributeFluxId, " +
						"pp.code, " +
						"pp.retail_name retailName, " +
						"ppu.name retailUnit, " +
						"ppa.batch_number batchNumber, " +
						"ppa.expiry_date expiryDate, " +
						"ppaf.quantity, " +
						"ppas.quantity_in_stock quantityInStock, " +
						"ppaf.observation, " +
						"ppaf.date_created dateCreated " +
						"FROM pharmacy_product_attribute_flux ppaf " +
						"LEFT JOIN pharmacy_product_operation ppo ON ppo.product_operation_id = ppaf.operation_id " +
						"LEFT JOIN pharmacy_product_attribute ppa ON ppa.product_attribute_id = ppaf.product_attribute_id " +
						"LEFT JOIN pharmacy_product pp on pp.product_id = ppa.product_id " +
						"LEFT JOIN pharmacy_product_unit ppu on ppu.product_unit_id = pp.product_retail_unit " +
						"LEFT JOIN pharmacy_product_attribute_stock ppas on ppas.product_attribute_id = ppa.product_attribute_id " +
						"WHERE ppaf.operation_id = :productOperationId ORDER BY ppaf.date_created DESC ";

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("productAttributeFluxId", StandardBasicTypes.INTEGER)
				.addScalar("code", StandardBasicTypes.STRING)
				.addScalar("retailName", StandardBasicTypes.STRING)
				.addScalar("retailUnit", StandardBasicTypes.STRING)
				.addScalar("batchNumber", StandardBasicTypes.STRING)
				.addScalar("expiryDate", StandardBasicTypes.DATE)
				.addScalar("quantity", StandardBasicTypes.INTEGER)
				.addScalar("quantityInStock", StandardBasicTypes.INTEGER)
				.addScalar("observation", StandardBasicTypes.INTEGER)
				.addScalar("dateCreated", StandardBasicTypes.DATE)
				.setParameter("productOperationId", productOperation.getProductOperationId())
				.setResultTransformer(new AliasToBeanResultTransformer(ProductOutFluxDTO.class));
		try {
			return query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ConsumptionReportDTO getConsumptionReport(ProductProgram productProgram, Date startDate, Date endDate, Location location, boolean byLocation) {
		ConsumptionReportDTO consumptionReportDTO = new ConsumptionReportDTO();
		consumptionReportDTO.setByLocation(byLocation);
		consumptionReportDTO.setStartDate(startDate);
		consumptionReportDTO.setEndDate(endDate);
		consumptionReportDTO.setProductProgramName(productProgram.getName());

		String whereLocation = byLocation ? " WHERE l2.parent_location = :locationId" : " WHERE l2.location_id = :locationId ";
		String sqlQueryDispensation =
				"SELECT" +
						"    location_id locationId," +
						"    name locationName," +
						"    code," +
						"    retail_name retailName," +
						"    wholesale_name wholesaleName," +
						"    SUM(quantityDistributed) retailQuantity," +
						"    FLOOR(SUM(quantityDistributed) / unit_conversion) wholesaleQuantity " +
						"FROM " +
						"    (" +
						"        SELECT" +
						"            ppaf.quantity quantityDistributed, po.location_id, l2.name, retail_name, code, wholesale_name, unit_conversion " +
						"        FROM ( " +
						"                 SELECT * FROM pharmacy_product_operation" +
						"                 WHERE operation_date BETWEEN :startDate AND :endDate AND operation_status = 2 AND voided = 0 AND program_id = :programId) po" +
						"                 INNER JOIN pharmacy_product_dispensation ppd on po.product_operation_id = ppd.product_operation_id" +
						"                 LEFT JOIN pharmacy_product_attribute_flux ppaf on po.product_operation_id = ppaf.operation_id" +
						"                 LEFT JOIN pharmacy_product_attribute ppa ON ppaf.product_attribute_id = ppa.product_attribute_id" +
						"                 LEFT JOIN (SELECT product_id, retail_name, code, wholesale_name, unit_conversion FROM pharmacy_product) pp ON ppa.product_id = pp.product_id" +
						"                 LEFT JOIN location l2 on po.location_id = l2.location_id " +
						whereLocation +
						"    )  po " +
						"GROUP BY location_id, retail_name " +
						"ORDER BY retail_name";

		List<ProductQuantityDTO> productQuantities = getConsumptionDto(sqlQueryDispensation, productProgram, startDate, endDate, location, false);
		if (productQuantities == null || productQuantities.size() == 0) {
			String queryReport = "SELECT " +
					"    l.location_id locationId, " +
					"    l.name locationName, " +
					"    pps.code, " +
					"    pps.retail_name retailName, " +
					"    pps.wholesale_name wholesaleName, " +
					"    SUM(quantityReported) retailQuantity, " +
					"    FLOOR(SUM(quantityReported) / pps.unit_conversion) wholesaleQuantity " +
					" " +
					"FROM " +
					"( " +
					"    SELECT " +
					"        po.operation_date, po.operation_status, ppaof.quantity quantityReported, po.location_id, pp.product_id, program_id " +
					"    FROM pharmacy_product_operation po, " +
					"         pharmacy_product_report ppr, " +
					"         pharmacy_product_attribute_other_flux ppaof, " +
					"         (SELECT product_id FROM pharmacy_product) pp " +
					"    WHERE " +
					"        po.product_operation_id = ppr.product_operation_id AND " +
					"        ppaof.operation_id = po.product_operation_id AND " +
					"        pp.product_id = ppaof.product_id AND " +
					"        ppr.report_period IN (:reportPeriod) AND " +
					"        po.operation_status IN (2, 4, 5, 6) AND " +
					"        po.program_id = :programId AND " +
					"        label = 'QD' AND po.voided = 0 " +
					") ppo " +
					"LEFT JOIN pharmacy_product pps ON pps.product_id = ppo.product_id " +
					"LEFT JOIN location l ON l.location_id = ppo.location_id " +
					"WHERE " +
					"    (l.location_id = :locationId OR l.parent_location = :locationId) " +
					"GROUP BY l.location_id, pps.product_id";
			productQuantities = getConsumptionDto(queryReport, productProgram, startDate, endDate, location, true);
			if (productQuantities == null || productQuantities.size() == 0) {
				return consumptionReportDTO;
			}
		}

		if (byLocation) {
			for (Location l : location.getChildLocations()) {
				LocationProductQuantityDTO locationProductQuantityDTO = new LocationProductQuantityDTO(l.getName());

				for (ProductQuantityDTO productQuantityDTO : productQuantities) {
					if (productQuantityDTO.getLocationId().equals(l.getLocationId())) {
						locationProductQuantityDTO.getProductQuantities().add(productQuantityDTO);
					}
				}
				consumptionReportDTO.getLocationProductQuantities().add(locationProductQuantityDTO);
			}
		} else {
			LocationProductQuantityDTO locationProductQuantityDTO = new LocationProductQuantityDTO(location.getName());

			for (ProductQuantityDTO productQuantityDTO : productQuantities) {
				if (productQuantityDTO.getLocationId().equals(location.getLocationId())) {
					locationProductQuantityDTO.getProductQuantities().add(productQuantityDTO);
				}
			}
			consumptionReportDTO.getLocationProductQuantities().add(locationProductQuantityDTO);
		}

		return consumptionReportDTO;
//
//		try {
//			Query query = getQuery(sqlQueryDispensation, productProgram, startDate, endDate, location);
//			List<ProductQuantityDTO> productQuantities = query.list();
//			if (productQuantities.size() == 0) {
//				if (byLocation) {
//					query = getQuery(queryReport, productProgram, startDate, endDate, location);
//					productQuantities = query.list();
//
//					if (productQuantities.size() == 0) {
//						return consumptionReportDTO;
//					}
//				}
//			}
//
//			if (byLocation) {
//				for (Location l : location.getChildLocations()) {
//					LocationProductQuantityDTO locationProductQuantityDTO = new LocationProductQuantityDTO(l.getName());
//
//					for (ProductQuantityDTO productQuantityDTO : productQuantities) {
//						if (productQuantityDTO.getLocationId().equals(l.getLocationId())) {
//							locationProductQuantityDTO.getProductQuantities().add(productQuantityDTO);
//						}
//					}
//					consumptionReportDTO.getLocationProductQuantities().add(locationProductQuantityDTO);
//				}
//			} else {
//				LocationProductQuantityDTO locationProductQuantityDTO = new LocationProductQuantityDTO(location.getName());
//
//				for (ProductQuantityDTO productQuantityDTO : productQuantities) {
//					if (productQuantityDTO.getLocationId().equals(location.getLocationId())) {
//						locationProductQuantityDTO.getProductQuantities().add(productQuantityDTO);
//					}
//				}
//				consumptionReportDTO.getLocationProductQuantities().add(locationProductQuantityDTO);
//			}
//
//
//		} catch (HibernateException e) {
//			System.out.println(e.getMessage());
//		}
//		return null;
	}

	@SuppressWarnings("unchecked")
	private List<ProductQuantityDTO> getConsumptionDto(String sqlQuery, ProductProgram productProgram, Date startDate, Date endDate, Location location, boolean isReport) {
		try {
			Query query = getQuery(sqlQuery, productProgram, startDate, endDate, location, isReport);
			return (List<ProductQuantityDTO>) query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	private Query getQuery(String sqlQuery, ProductProgram productProgram, Date startDate, Date endDate, Location location, boolean isReport) {
		Query query =  sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("locationId", StandardBasicTypes.INTEGER)
				.addScalar("code", StandardBasicTypes.STRING)
				.addScalar("retailName", StandardBasicTypes.STRING)
				.addScalar("wholesaleName", StandardBasicTypes.STRING)
				.addScalar("retailQuantity", StandardBasicTypes.INTEGER)
				.addScalar("wholesaleQuantity", StandardBasicTypes.INTEGER)
				.setParameter("programId", productProgram.getProductProgramId())
				.setParameter("locationId", location.getLocationId());
		if (isReport) {
			query.setParameterList("reportPeriod", OperationUtils.getReportPeriodOfPeriod(startDate, endDate).toArray());
		} else {
			query.setParameter("startDate", startDate)
					.setParameter("endDate", endDate);
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(ProductQuantityDTO.class));

		return query;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProductMovementHistoryDTO> getProductMovementHistory(Date startDate, Date endDate, Location location, ProductProgram productProgram) {
		String sqlQuery =
				"SELECT " +
						"    ppo.operation_date operationDate, " +
						"    pp.code, pp.retail_name productName, " +
						"    ppa.batch_number batchNumber, " +
						"    IF(ppd.product_operation_id IS NOT NULL, IF(ppo.incidence = 0, 'Dispensation', 'Annulation de Dispensation'), " +
						"        IF(ppi.product_operation_id IS NOT NULL, IF(ppi.inventory_type = 1, 'Inventaire total', 'Inventaire partiel'), " +
						"            IF(ppmo.product_operation_id IS NOT NULL, " +
						"                IF(ppmo.stock_out_type = 1, 'Autre perte de produits', " +
						"                    IF(ppmo.stock_out_type = 2, 'Vol', " +
						"                        IF(ppmo.stock_out_type = 4, 'Produits périmés', " +
						"                            IF(ppmo.stock_out_type = 5, 'Produits avariés', " +
						"                                IF(ppmo.stock_out_type = 6, 'Produits endommagés', NULL))))), " +
						"                IF(ppme.product_operation_id IS NOT NULL, 'Don de produits', " +
						"                    IF(ppr.product_operation_id IS NOT NULL, IF(ppo.incidence = 1, 'Réception', 'Retour de réception'), " +
						"                        IF(ppt.product_operation_id IS NOT NULL, IF(ppt.transfer_type = 0, 'Transfert entrant', 'Transfert sortant'), " +
						"                            IF(pprp.product_operation_id IS NOT NULL, 'Distribution', NULL))))))) operationType, " +
						"    quantity, " +
						"    quantity_in_stock quantityInStock, " +
						"    CONCAT(family_name, ' ', given_name) createdBy, " +
						"    ppo.date_created dateCreated " +
						" " +
						"FROM " +
						"    pharmacy_product_attribute_stock ppas " +
						"    INNER JOIN users u on ppas.creator = u.user_id " +
						"    INNER JOIN person p on u.person_id = p.person_id " +
						"    INNER JOIN person_name pn on p.person_id = pn.person_id " +
						"    INNER JOIN pharmacy_product_attribute ppa ON ppas.product_attribute_id = ppa.product_attribute_id " +
						"    INNER JOIN pharmacy_product pp ON ppa.product_id = pp.product_id " +
						"    INNER JOIN pharmacy_product_operation ppo ON ppas.operation_id = ppo.product_operation_id " +
						"    INNER JOIN pharmacy_product_attribute_flux ppaf on ppa.product_attribute_id = ppaf.product_attribute_id AND ppo.product_operation_id = ppaf.operation_id " +
						"    LEFT JOIN pharmacy_product_dispensation ppd ON ppo.product_operation_id = ppd.product_operation_id " +
						"    LEFT JOIN pharmacy_product_inventory ppi ON ppo.product_operation_id = ppi.product_operation_id " +
						"    LEFT JOIN pharmacy_product_reception ppr on ppo.product_operation_id = ppr.product_operation_id " +
						"    LEFT JOIN pharmacy_product_report pprp on ppo.product_operation_id = pprp.product_operation_id " +
						"    LEFT JOIN pharmacy_product_transfer ppt on ppo.product_operation_id = ppt.product_operation_id " +
						"    LEFT JOIN pharmacy_product_movement_entry ppme ON ppo.product_operation_id = ppme.product_operation_id " +
						"    LEFT JOIN pharmacy_product_movement_out ppmo ON ppo.product_operation_id = ppmo.product_operation_id " +
						" WHERE ppo.operation_date BETWEEN :startDate AND :endDate AND ppo.location_id = :locationId AND ppo.program_id = :programId " +
						"ORDER BY pp.retail_name, ppo.operation_date";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("operationDate", StandardBasicTypes.DATE)
				.addScalar("code", StandardBasicTypes.STRING)
				.addScalar("productName", StandardBasicTypes.STRING)
				.addScalar("batchNumber", StandardBasicTypes.STRING)
				.addScalar("operationType", StandardBasicTypes.STRING)
				.addScalar("quantity", StandardBasicTypes.INTEGER)
				.addScalar("quantityInStock", StandardBasicTypes.INTEGER)
				.addScalar("createdBy", StandardBasicTypes.STRING)
				.addScalar("dateCreated", StandardBasicTypes.TIMESTAMP)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("locationId", location.getLocationId())
				.setParameter("programId", productProgram.getProductProgramId())
				.setResultTransformer(new AliasToBeanResultTransformer(ProductMovementHistoryDTO.class));
		try {
			return query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegimenReportIndicatorDTO> getRegimenAndIndicatorReport(Date startDate, Date endDate, Location location, ProductProgram productProgram) {
		String sqlQuery =
				"SELECT " +
						"       regimenLine, " +
						"       regimen, " +
						"       SUM(IF(age >= 15 AND sousCotrim IS NOT NULL, val, 0)) adultCtx, " +
						"       SUM(IF(age >= 15 AND TBDignostique IS NOT NULL, val, 0)) adultTB, " +
						"       SUM(IF(age >= 15 AND hepatiteB = 'oui', val, 0)) adultHepB, " +
						"       SUM(IF(age >= 15 AND anemie = 'oui', val, 0)) adultAnemia, " +
						"       SUM(IF(age >= 15 AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065 ), val, 0)) adultMPTC, " +
						"       SUM(IF(age >= 15 AND goal = 'AES', val, 0)) adultAES, " +
						"       SUM(IF(age >= 15, val, 0)) adult, " +
						" " +
						"       SUM(IF(age < 15 AND sousCotrim IS NOT NULL, val, 0)) childCtx, " +
						"       SUM(IF(age < 15 AND TBDignostique IS NOT NULL, val, 0)) childTB, " +
						"       SUM(IF(age < 15 AND hepatiteB = 'oui', val, 0)) childHepB, " +
						"       SUM(IF(age < 15 AND anemie = 'oui', val, 0)) childAnemia, " +
						"       SUM(IF(age < 15 AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065 ), val, 0)) childMPTC, " +
						"       SUM(IF(age < 15 AND goal = 'AES', val, 0)) childAES, " +
						"       SUM(IF(age < 15, val, 0)) child, " +
						" " +
						"       SUM(IF(age >= 15 AND sousCotrim IS NOT NULL AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) adultInclusionCtx, " +
						"       SUM(IF(age >= 15 AND TBDignostique IS NOT NULL AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) adultInclusionTB, " +
						"       SUM(IF(age >= 15 AND hepatiteB = 'oui' AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) adultInclusionHepB, " +
						"       SUM(IF(age >= 15 AND anemie = 'oui' AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) adultInclusionAnemia, " +
						"       SUM(IF(age >= 15 AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065 ) AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) adultInclusionMPTC, " +
						"       SUM(IF(age >= 15 AND goal = 'AES' AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) adultInclusionAES, " +
						"       SUM(IF(age >= 15 AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) adultInclusion, " +
						" " +
						"       SUM(IF(age < 15 AND sousCotrim IS NOT NULL AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) childInclusionCtx, " +
						"       SUM(IF(age < 15 AND TBDignostique IS NOT NULL AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) childInclusionTB, " +
						"       SUM(IF(age < 15 AND hepatiteB = 'oui' AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) childInclusionHepB, " +
						"       SUM(IF(age < 15 AND anemie = 'oui' AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) childInclusionAnemia, " +
						"       SUM(IF(age < 15 AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065 ) AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) childInclusionMPTC, " +
						"       SUM(IF(age < 15 AND goal = 'AES' AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) childInclusionAES, " +
						"       SUM(IF(age < 15 AND dateInitARV BETWEEN :startDate AND :endDate, val, 0)) childInclusion, " +
						" " +
						"       SUM(IF(age >= 15 AND sousCotrim IS NOT NULL  AND stabilite = 'non stable', val, 0)) adultNotStableCtx, " +
						"       SUM(IF(age >= 15 AND TBDignostique IS NOT NULL  AND stabilite = 'non stable', val, 0)) adultNotStableTB, " +
						"       SUM(IF(age >= 15 AND hepatiteB = 'oui' AND stabilite = 'non stable', val, 0)) adultNotStableHepB, " +
						"       SUM(IF(age >= 15 AND anemie = 'oui' AND stabilite = 'non stable', val, 0)) adultNotStableAnemia, " +
						"       SUM(IF(age >= 15 AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065 ) AND stabilite = 'non stable', val, 0)) adultNotStableMPTC, " +
						"       SUM(IF(age >= 15 AND goal = 'AES' AND stabilite = 'non stable', val, 0)) adultNotStableAES, " +
						"       SUM(IF(age >= 15 AND stabilite = 'non stable', val, 0)) adultNotStable, " +
						" " +
						"       SUM(IF(age < 15 AND sousCotrim IS NOT NULL  AND stabilite = 'non stable', val, 0)) childNotStableCtx, " +
						"       SUM(IF(age < 15 AND TBDignostique IS NOT NULL  AND stabilite = 'non stable', val, 0)) childNotStableTB, " +
						"       SUM(IF(age < 15 AND hepatiteB = 'oui' AND stabilite = 'non stable', val, 0)) childNotStableHepB, " +
						"       SUM(IF(age < 15 AND anemie = 'oui' AND stabilite = 'non stable', val, 0)) childNotStableAnemia, " +
						"       SUM(IF(age < 15 AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065 ) AND stabilite = 'non stable', val, 0)) childNotStableMPTC, " +
						"       SUM(IF(age < 15 AND goal = 'AES' AND stabilite = 'non stable', val, 0)) childNotStableAES, " +
						"       SUM(IF(age < 15 AND stabilite = 'non stable', val, 0)) childNotStable, " +
						" " +
						"       SUM(IF(age >= 15 AND sousCotrim IS NOT NULL  AND stabilite = 'stable', val, 0)) adultStableCtx, " +
						"       SUM(IF(age >= 15 AND TBDignostique IS NOT NULL  AND stabilite = 'stable', val, 0)) adultStableTB, " +
						"       SUM(IF(age >= 15 AND hepatiteB = 'oui' AND stabilite = 'stable', val, 0)) adultStableHepB, " +
						"       SUM(IF(age >= 15 AND anemie = 'oui' AND stabilite = 'stable', val, 0)) adultStableAnemia, " +
						"       SUM(IF(age >= 15 AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065 ) AND stabilite = 'stable', val, 0)) adultStableMPTC, " +
						"       SUM(IF(age >= 15 AND goal = 'AES' AND stabilite = 'stable', val, 0)) adultStableAES, " +
						"       SUM(IF(age >= 15 AND stabilite = 'stable', val, 0)) adultStable, " +
						" " +
						"       SUM(IF(age < 15 AND sousCotrim IS NOT NULL  AND stabilite = 'stable', val, 0)) childStableCtx, " +
						"       SUM(IF(age < 15 AND TBDignostique IS NOT NULL  AND stabilite = 'stable', val, 0)) childStableTB, " +
						"       SUM(IF(age < 15 AND hepatiteB = 'oui' AND stabilite = 'stable', val, 0)) childStableHepB, " +
						"       SUM(IF(age < 15 AND anemie = 'oui' AND stabilite = 'stable', val, 0)) childStableAnemia, " +
						"       SUM(IF(age < 15 AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065 ) AND stabilite = 'stable', val, 0)) childStableMPTC, " +
						"       SUM(IF(age < 15 AND goal = 'AES' AND stabilite = 'stable', val, 0)) childStableAES, " +
						"       SUM(IF(age < 15 AND stabilite = 'stable', val, 0)) childStable, " +
						" " +
						"       SUM(IF(dateTransfert IS NOT NULL AND sousCotrim IS NOT NULL , val, 0)) transferredCtx, " +
						"       SUM(IF(dateTransfert IS NOT NULL AND TBDignostique IS NOT NULL , val, 0)) transferredTB, " +
						"       SUM(IF(dateTransfert IS NOT NULL AND hepatiteB = 'oui', val, 0)) transferredHepB, " +
						"       SUM(IF(dateTransfert IS NOT NULL AND anemie = 'oui', val, 0)) transferredAnemia, " +
						"       SUM(IF(dateTransfert IS NOT NULL AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065 ), val, 0)) transferredMPTC, " +
						"       SUM(IF(dateTransfert IS NOT NULL AND goal = 'AES', val, 0)) transferredAES, " +
						"       SUM(IF(dateTransfert IS NOT NULL, val, 0)) transferred, " +
						" " +
						"       SUM(IF(dateAdmission IS NOT NULL AND dateInitARV IS NULL  AND sousCotrim IS NOT NULL , val, 0)) referredCtx, " +
						"       SUM(IF(dateAdmission IS NOT NULL AND dateInitARV IS NULL  AND TBDignostique IS NOT NULL , val, 0)) referredTB, " +
						"       SUM(IF(dateAdmission IS NOT NULL AND dateInitARV IS NULL  AND hepatiteB = 'oui', val, 0)) referredHepB, " +
						"       SUM(IF(dateAdmission IS NOT NULL AND dateInitARV IS NULL  AND anemie = 'oui', val, 0)) referredAnemia, " +
						"       SUM(IF(dateAdmission IS NOT NULL AND dateInitARV IS NULL  AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065), val, 0)) referredMPTC, " +
						"       SUM(IF(dateAdmission IS NOT NULL AND dateInitARV IS NULL  AND goal = 'AES', val, 0)) referredAES, " +
						"       SUM(IF(dateAdmission IS NOT NULL AND dateInitARV IS NULL , val, 0)) referred, " +
						" " +
						"       SUM(IF(dateDeces IS NOT NULL AND sousCotrim IS NOT NULL , val, 0)) deadCtx, " +
						"       SUM(IF(dateDeces IS NOT NULL AND TBDignostique IS NOT NULL , val, 0)) deadTB, " +
						"       SUM(IF(dateDeces IS NOT NULL AND hepatiteB = 'oui', val, 0)) deadHepB, " +
						"       SUM(IF(dateDeces IS NOT NULL AND anemie = 'oui', val, 0)) deadAnemia, " +
						"       SUM(IF(dateDeces IS NOT NULL AND (goal = 'PTME' OR Grossesse = 1065 OR Allaitement = 1065 ), val, 0)) deadMPTC, " +
						"       SUM(IF(dateDeces IS NOT NULL AND goal = 'AES', val, 0)) deadAES, " +
						"       SUM(IF(dateDeces IS NOT NULL, val, 0)) dead " +
						" " +
						"FROM " +
						"     ( " +
						"SELECT " +
						"    ppo.operation_date, IF(regimen IS NULL OR regimen = 'COTRIMOXAZOLE', 'Aucun régime', regimen) regimen, regimenLine, goal, age, NULL as dateAdmission, NULL AS dateInitARV, NULL AS dateDeces, NULL AS dateTransfert, NULL AS stabilite, NULL AS TBDignostique, NULL AS hepatiteB, NULL AS anemie, CTX.operation_id sousCotrim, NULL AS Allaitement, NULL AS Grossesse, 1 AS val " +
						"FROM pharmacy_product_dispensation ppd " +
						"    INNER JOIN (SELECT * FROM pharmacy_product_operation WHERE operation_date BETWEEN :startDate AND :endDate AND location_id = :location) ppo on ppo.product_operation_id = ppd.product_operation_id " +
						"         INNER JOIN ( " +
						"    SELECT " +
						"           mobile_dispensation_info_id, " +
						"           regimen_id, " +
						"           mobile_patient_id, " +
						"           IF(goal = 0, 'non applicable', " +
						"              IF(goal = 1, 'PEC', " +
						"                 IF(goal = 2, 'PTME', " +
						"                    IF(goal = 3, 'AES', " +
						"                       IF(goal = 4, 'PREP', NULL))))) goal, location_id, patient_id, IF(regimen_line IS NULL , 1, regimen_line) regimenLine " +
						"    FROM pharmacy_mobile_patient_dispensation_info " +
						") pmpdi on ppd.product_operation_id = pmpdi.mobile_dispensation_info_id " +
						"    LEFT JOIN ( " +
						"    SELECT " +
						"        pf.operation_id " +
						"    FROM " +
						"        pharmacy_product_attribute_flux pf, " +
						"        pharmacy_product_attribute ppa, " +
						"        (SELECT * FROM pharmacy_product WHERE code IN ('AR01321','AR01340','AR01350')) pp " +
						"    WHERE pf.product_attribute_id = ppa.product_attribute_id AND pp.product_id = ppa.product_id GROUP BY operation_id " +
						") CTX ON CTX.operation_id = ppd.product_operation_id " +
						"         LEFT JOIN pharmacy_product_regimen ppr on pmpdi.regimen_id = ppr.product_regimen_id " +
						"         LEFT JOIN (SELECT name regimen, concept_id FROM concept_name WHERE concept_name_type = 'FULLY_SPECIFIED' AND LENGTH(name) <= 16 GROUP BY name) cn ON ppr.concept_id = cn.concept_id " +
						"         LEFT JOIN pharmacy_mobile_patient pmp ON pmpdi.mobile_patient_id = pmp.mobile_patient_id " +
						"GROUP BY pmp.mobile_patient_id " +
						"UNION " +
						"SELECT " +
						"    ppo.operation_date, IF(regimen IS NULL OR regimen = 'COTRIMOXAZOLE', 'Aucun régime', regimen) regimen, IF(Ln.regimenLine IS NULL, 1, regimenLine) regimenLine, goal, age, NULL as dateAdmission, dateInitARV, dateDeces, dateTransfert, " +
						"    IF(DATEDIFF(:endDate, dateInitARV)/30 < 12, NULL, " +
						"       IF( " +
						"                       date28JoursApresRupture >= :endDate " +
						"                   AND (dateAvantDerniereCV IS NOT NULL AND dateDerniereCV IS NOT NULL AND dateAvantDerniereCV<>DateDerniereCV) " +
						"                   AND avantDerniereCV < 1000 " +
						"                   AND derniereCV < 1000 " +
						"                   AND TBDignostique IS NULL " +
						"                   AND PathologieAsso IS NULL " +
						"                   AND MotifInterrup IS NULL " +
						"                   AND (Allaitement = 1066 OR Allaitement IS NULL) " +
						"                   AND (Grossesse = 1066 OR Grossesse IS NULL) " +
						"                   AND ((DATEDIFF(:endDate, dateDerniereCV)/30) <= 15), 'stable','non stable' " +
						"           )) " +
						"        stabilite, " +
						"    TBDignostique, " +
						"       IF(PathologieAsso LIKE '%Hép%' OR PathologieAsso LIKE '%hep%', 'oui', 'non') hepatiteB, " +
						"       IF(PathologieAsso LIKE '%anémie%' OR PathologieAsso LIKE '%anemie%', 'oui', 'non') anemie, CTX.operation_id sousCotrim, Allaitement, Grossesse, " +
						"    1 AS val " +
						"FROM pharmacy_product_dispensation ppd " +
						"   LEFT JOIN ( " +
						"    SELECT " +
						"        pf.operation_id " +
						"    FROM " +
						"        pharmacy_product_attribute_flux pf, " +
						"        pharmacy_product_attribute ppa, " +
						"        (SELECT * FROM pharmacy_product WHERE code IN ('AR01321','AR01340','AR01350')) pp " +
						"    WHERE pf.product_attribute_id = ppa.product_attribute_id AND pp.product_id = ppa.product_id GROUP BY operation_id " +
						") CTX ON CTX.operation_id = ppd.product_operation_id " +
						"LEFT JOIN (SELECT * FROM pharmacy_product_operation WHERE operation_date BETWEEN :startDate AND :endDate AND location_id = :location) ppo on ppo.product_operation_id = ppd.product_operation_id " +
						"LEFT JOIN (SELECT value_text goal, encounter_id, person_id FROM obs WHERE concept_id = 163000 AND voided = 0) G ON ppd.encounter_id = G.encounter_id " +
						"LEFT JOIN (SELECT value_coded, encounter_id FROM obs o WHERE o.concept_id = 165033 AND o.voided = 0) R ON ppd.encounter_id = R.encounter_id " +
						"LEFT JOIN (SELECT name regimen, concept_id FROM concept_name WHERE concept_name_type = 'FULLY_SPECIFIED' GROUP BY name) RP ON R.value_coded = RP.concept_id " +
						"LEFT JOIN (SELECT value_datetime DateRupture, ADDDATE(value_datetime, INTERVAL 28 DAY) date28JoursApresRupture, encounter_id FROM obs o WHERE o.concept_id = 165040 AND o.voided = 0) DR ON ppd.encounter_id = DR.encounter_id " +
						"LEFT JOIN (SELECT IF(value_coded = 164734, 3, " +
						"                     IF(value_coded = 164732, 2, 1)) regimenLine, encounter_id FROM obs WHERE concept_id = 164767 AND voided = 0) Ln ON ppd.encounter_id = Ln.encounter_id " +
						"LEFT JOIN (SELECT person_id, FLOOR(DATEDIFF(:endDate, birthdate)/365.25) age, gender FROM person) pe ON G.person_id = pe.person_id " +
						"LEFT JOIN (SELECT encounter_datetime DateAdmission, patient_id FROM encounter " +
						"           WHERE encounter_type = 1 AND voided = 0 AND encounter_datetime BETWEEN :startDate AND :endDate AND location_id = :location GROUP BY patient_id) I ON I.patient_id = pe.person_id " +
						"LEFT JOIN (SELECT person_id, value_datetime dateDeces FROM obs WHERE concept_id = 1543 AND value_datetime BETWEEN :startDate AND :endDate AND voided = 0) Deces " +
						"          ON Deces.person_id = pe.person_id " +
						"LEFT JOIN (SELECT person_id, value_datetime dateTransfert FROM obs WHERE concept_id = 164595 AND voided = 0 AND location_id = :location AND value_datetime BETWEEN :startDate AND :endDate) Transf " +
						"          ON Transf.person_id = pe.person_id " +
						"LEFT JOIN " +
						"     ( " +
						"         SELECT " +
						"             L.person_id, " +
						"             dateAvantDerniereCV, " +
						"             avantDerniereCV, " +
						"             dateDerniereCV, " +
						"             derniereCV " +
						"         FROM " +
						"             ( " +
						"                 SELECT " +
						"                     person_id, " +
						"                     encounter_datetime dateDerniereCV, " +
						"                     value_numeric derniereCV " +
						"                 FROM obs o, encounter e " +
						"                 WHERE " +
						"                         e.encounter_id = o.encounter_id AND " +
						"                         encounter_type = 8 AND " +
						"                         concept_id = 856 AND e.voided = 0  AND e.location_id = :location " +
						"                   AND e.encounter_id = " +
						"                       ( " +
						"                           SELECT e2.encounter_id " +
						"                           FROM encounter e2, obs o2 " +
						"                           WHERE " +
						"                                   e2.encounter_id = o2.encounter_id AND " +
						"                                   encounter_type = 8 AND e2.patient_id = o.person_id AND " +
						"                                   o2.concept_id = 856 AND o2.voided = 0 AND e2.location_id = o.location_id AND e2.encounter_datetime <= :endDate ORDER BY e2.encounter_datetime DESC LIMIT 1 " +
						"                       ) " +
						"             ) L " +
						" " +
						"                 LEFT JOIN ( " +
						"                 SELECT " +
						"                     person_id, " +
						"                     encounter_datetime dateAvantDerniereCV, " +
						"                     value_numeric avantDerniereCV " +
						"                 FROM obs o, encounter e " +
						"                 WHERE " +
						"                         e.encounter_id = o.encounter_id AND " +
						"                         encounter_type = 8 AND " +
						"                         concept_id = 856 AND e.voided = 0  AND e.location_id = :location " +
						"                   AND e.encounter_id = " +
						"                       ( " +
						"                           SELECT e2.encounter_id " +
						"                           FROM encounter e2, " +
						"                                obs o2 " +
						"                           WHERE e2.encounter_id = o2.encounter_id " +
						"                             AND encounter_type = 8 " +
						"                             AND e2.patient_id = o.person_id " +
						"                             AND o2.concept_id = 856 " +
						"                             AND o2.voided = 0 " +
						"                             AND e2.location_id = o.location_id " +
						"                             AND e2.encounter_datetime <= :endDate " +
						"                           ORDER BY e2.encounter_datetime DESC " +
						"                           LIMIT 1, 1 " +
						"                       ) " +
						"             ) LB ON LB.person_id = L.person_id " +
						"     ) ADCV ON ADCV.person_id = pe.person_id " +
						" " +
						"LEFT JOIN " +
						"     ( " +
						"         SELECT person_id, value_datetime dateInitARV " +
						"         FROM obs o, encounter e " +
						"         WHERE o.encounter_id = e.encounter_id " +
						"           AND concept_id IN (164588,159599,165032) " +
						"           AND encounter_type = 1 AND e.voided = 0 AND value_datetime <= :endDate " +
						"           AND o.voided = 0  AND o.location_id = :location " +
						"         GROUP BY person_id " +
						"     ) IARV ON IARV.person_id = pe.person_id " +
						" " +
						"LEFT JOIN " +
						"     ( " +
						"         SELECT " +
						"             encounter_id, E.patient_id, DateDerniereVisite " +
						"         FROM " +
						"             encounter E, " +
						"             ( " +
						"                 SELECT " +
						"                     patient_id, MAX(encounter_datetime) DateDerniereVisite " +
						"                 FROM encounter " +
						"                 WHERE " +
						"                         encounter_datetime <= :endDate AND encounter_type = 2 AND location_id = :location AND voided = 0 GROUP BY patient_id " +
						"             ) LE " +
						"         WHERE E.patient_id = LE.patient_id AND E.encounter_datetime = LE.DateDerniereVisite AND E.encounter_type = 2 AND E.voided = 0 " +
						"     ) V ON pe.person_id = V.patient_id " +
						"LEFT JOIN " +
						"     ( " +
						"         SELECT DISTINCT value_coded TBDignostique, encounter_id FROM obs o " +
						"         WHERE " +
						"                 concept_id = 164772 AND value_coded = 164762 AND voided = 0 AND location_id = :location " +
						"     ) TB ON TB.encounter_id = V.encounter_id " +
						" " +
						"LEFT JOIN " +
						"     ( " +
						"         SELECT DISTINCT value_text PathologieAsso, encounter_id FROM obs " +
						"         WHERE concept_id = 164733 AND voided = 0 AND location_id = :location " +
						"     ) PA ON PA.encounter_id = V.encounter_id " +
						" " +
						"LEFT JOIN " +
						"     ( " +
						"         SELECT DISTINCT value_coded MotifInterrup, encounter_id FROM obs o " +
						"         WHERE " +
						"                 concept_id = 1252 AND value_coded IN (102) AND voided = 0 AND location_id = :location " +
						"     ) MI ON MI.encounter_id = V.encounter_id " +
						" " +
						"LEFT JOIN " +
						"     ( " +
						"         SELECT DISTINCT value_coded Grossesse, encounter_id FROM obs o " +
						"         WHERE " +
						"                 concept_id = 5272 AND location_id = :location AND voided = 0 " +
						"     ) GR ON GR.encounter_id = V.encounter_id " +
						" " +
						"LEFT JOIN " +
						"     ( " +
						"         SELECT DISTINCT value_coded Allaitement, encounter_id FROM obs o " +
						"         WHERE " +
						"                 concept_id = 164764 AND voided = 0 AND location_id = :location " +
						"     ) A ON A.encounter_id = V.encounter_id " +
						" " +
						"    GROUP BY pe.person_id) _ " +
						" " +
						"GROUP BY regimenLine, regimen";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("regimenLine", StandardBasicTypes.INTEGER)
				.addScalar("regimen", StandardBasicTypes.STRING)

				.addScalar("adultCtx", StandardBasicTypes.INTEGER)
				.addScalar("adultTB", StandardBasicTypes.INTEGER)
				.addScalar("adultHepB", StandardBasicTypes.INTEGER)
				.addScalar("adultAnemia", StandardBasicTypes.INTEGER)
				.addScalar("adultMPTC", StandardBasicTypes.INTEGER)
				.addScalar("adultAES", StandardBasicTypes.INTEGER)
				.addScalar("adult", StandardBasicTypes.INTEGER)
				.addScalar("childCtx", StandardBasicTypes.INTEGER)
				.addScalar("childTB", StandardBasicTypes.INTEGER)
				.addScalar("childHepB", StandardBasicTypes.INTEGER)
				.addScalar("childAnemia", StandardBasicTypes.INTEGER)
				.addScalar("childMPTC", StandardBasicTypes.INTEGER)
				.addScalar("childAES", StandardBasicTypes.INTEGER)
				.addScalar("child", StandardBasicTypes.INTEGER)

				.addScalar("adultInclusionCtx", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionTB", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionHepB", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionAnemia", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionMPTC", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionAES", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusion", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionCtx", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionTB", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionHepB", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionAnemia", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionMPTC", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionAES", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusion", StandardBasicTypes.INTEGER)

				.addScalar("adultInclusionCtx", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionTB", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionHepB", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionAnemia", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionMPTC", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusionAES", StandardBasicTypes.INTEGER)
				.addScalar("adultInclusion", StandardBasicTypes.INTEGER)
				.addScalar("childInclusionCtx", StandardBasicTypes.INTEGER)
				.addScalar("childInclusionTB", StandardBasicTypes.INTEGER)
				.addScalar("childInclusionHepB", StandardBasicTypes.INTEGER)
				.addScalar("childInclusionAnemia", StandardBasicTypes.INTEGER)
				.addScalar("childInclusionMPTC", StandardBasicTypes.INTEGER)
				.addScalar("childInclusionAES", StandardBasicTypes.INTEGER)
				.addScalar("childInclusion", StandardBasicTypes.INTEGER)

				.addScalar("adultNotStableCtx", StandardBasicTypes.INTEGER)
				.addScalar("adultNotStableTB", StandardBasicTypes.INTEGER)
				.addScalar("adultNotStableHepB", StandardBasicTypes.INTEGER)
				.addScalar("adultNotStableAnemia", StandardBasicTypes.INTEGER)
				.addScalar("adultNotStableMPTC", StandardBasicTypes.INTEGER)
				.addScalar("adultNotStableAES", StandardBasicTypes.INTEGER)
				.addScalar("adultNotStable", StandardBasicTypes.INTEGER)
				.addScalar("childNotStableCtx", StandardBasicTypes.INTEGER)
				.addScalar("childNotStableTB", StandardBasicTypes.INTEGER)
				.addScalar("childNotStableHepB", StandardBasicTypes.INTEGER)
				.addScalar("childNotStableAnemia", StandardBasicTypes.INTEGER)
				.addScalar("childNotStableMPTC", StandardBasicTypes.INTEGER)
				.addScalar("childNotStableAES", StandardBasicTypes.INTEGER)
				.addScalar("childNotStable", StandardBasicTypes.INTEGER)

				.addScalar("adultStableCtx", StandardBasicTypes.INTEGER)
				.addScalar("adultStableTB", StandardBasicTypes.INTEGER)
				.addScalar("adultStableHepB", StandardBasicTypes.INTEGER)
				.addScalar("adultStableAnemia", StandardBasicTypes.INTEGER)
				.addScalar("adultStableMPTC", StandardBasicTypes.INTEGER)
				.addScalar("adultStableAES", StandardBasicTypes.INTEGER)
				.addScalar("adultStable", StandardBasicTypes.INTEGER)
				.addScalar("childStableCtx", StandardBasicTypes.INTEGER)
				.addScalar("childStableTB", StandardBasicTypes.INTEGER)
				.addScalar("childStableHepB", StandardBasicTypes.INTEGER)
				.addScalar("childStableAnemia", StandardBasicTypes.INTEGER)
				.addScalar("childStableMPTC", StandardBasicTypes.INTEGER)
				.addScalar("childStableAES", StandardBasicTypes.INTEGER)
				.addScalar("childStable", StandardBasicTypes.INTEGER)

				.addScalar("transferredCtx", StandardBasicTypes.INTEGER)
				.addScalar("transferredTB", StandardBasicTypes.INTEGER)
				.addScalar("transferredHepB", StandardBasicTypes.INTEGER)
				.addScalar("transferredAnemia", StandardBasicTypes.INTEGER)
				.addScalar("transferredMPTC", StandardBasicTypes.INTEGER)
				.addScalar("transferredAES", StandardBasicTypes.INTEGER)
				.addScalar("transferred", StandardBasicTypes.INTEGER)

				.addScalar("referredCtx", StandardBasicTypes.INTEGER)
				.addScalar("referredTB", StandardBasicTypes.INTEGER)
				.addScalar("referredHepB", StandardBasicTypes.INTEGER)
				.addScalar("referredAnemia", StandardBasicTypes.INTEGER)
				.addScalar("referredMPTC", StandardBasicTypes.INTEGER)
				.addScalar("referredAES", StandardBasicTypes.INTEGER)
				.addScalar("referred", StandardBasicTypes.INTEGER)

				.addScalar("deadCtx", StandardBasicTypes.INTEGER)
				.addScalar("deadTB", StandardBasicTypes.INTEGER)
				.addScalar("deadHepB", StandardBasicTypes.INTEGER)
				.addScalar("deadAnemia", StandardBasicTypes.INTEGER)
				.addScalar("deadMPTC", StandardBasicTypes.INTEGER)
				.addScalar("deadAES", StandardBasicTypes.INTEGER)
				.addScalar("dead", StandardBasicTypes.INTEGER)

				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("location", location.getLocationId())
//				.setParameter("programId", productProgram.getProductProgramId())
				.setResultTransformer(new AliasToBeanResultTransformer(RegimenReportIndicatorDTO.class));
		try {
			return query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<DispensationHistoryDTO> getProductDispensationHistory(Date startDate, Date endDate, Location location, ProductProgram productProgram) {
		String sqlQuery =
				"SELECT " +
						"    operation_id operationId, " +
						"    operation_date operationDate, " +
						"    IF(pmp.identifier IS NULL, pi.identifier, pmp.identifier) patientIdentifier, " +
						"    IF(pmp.gender IS NULL, p3.gender, pmp.gender) patientGender, " +
						"    IF(pmp.age IS NULL, FLOOR(DATEDIFF(:endDate, birthdate)/365.25), pmp.age) patientAge, " +
						"    IF(age < 15, 'enfant', 'adulte') category, " +
						"    name regimen, " +
						"    treatment_days treatmentDays, " +
						"    treatment_end_date treatmentEndDate, " +
						"    P.products " +
						"FROM ( " +
						"    SELECT " +
						"        operation_id, GROUP_CONCAT(CONCAT_WS(':', code, retail_name, quantity) SEPARATOR '|') products, operation_date, program_id, location_id " +
						"    FROM " +
						"        ( " +
						"             SELECT " +
						"                ppaf.quantity, " +
						"                    operation_id, " +
						"                    code, " +
						"                    retail_name, " +
						"                    ppo.operation_date, " +
						"                    ppo.program_id, " +
						"                    ppo.location_id " +
						" " +
						"          FROM " +
						"              pharmacy_product_operation ppo, " +
						"              pharmacy_product_attribute_flux ppaf, " +
						"              pharmacy_product_attribute ppa, " +
						"               pharmacy_product pp " +
						"          WHERE " +
						"            ppaf.operation_id = ppo.product_operation_id AND " +
						"            ppaf.product_attribute_id = ppa.product_attribute_id AND " +
						"            ppa.product_id = pp.product_id  AND " +
						"            ppo.operation_status = 2 " +
						"        ) _ " +
						"    GROUP BY operation_id " +
						"    ) P " +
						"LEFT JOIN pharmacy_product_dispensation ppd ON ppd.product_operation_id = P.operation_id " +
						"INNER JOIN pharmacy_mobile_patient_dispensation_info pmpdi on ppd.product_operation_id = pmpdi.mobile_dispensation_info_id " +
						"LEFT JOIN pharmacy_mobile_patient pmp on pmpdi.mobile_patient_id = pmp.mobile_patient_id " +
						"LEFT JOIN patient p2 on pmpdi.patient_id = p2.patient_id " +
						"LEFT JOIN patient_identifier pi on p2.patient_id = pi.patient_id " +
						"LEFT JOIN person p3 on p2.patient_id = p3.person_id " +
						"LEFT JOIN pharmacy_product_regimen ppr on pmpdi.regimen_id = ppr.product_regimen_id " +
						"LEFT JOIN (SELECT * FROM concept_name WHERE (name LIKE '% 3TC %' OR name LIKE '% FTC %' OR name LIKE '% ABC %' OR name LIKE '% DDI %' OR name = 'COTRIMOXAZOLE') GROUP BY concept_id) cn on ppr.concept_id = cn.concept_id " +
						"WHERE operation_date BETWEEN :startDate AND :endDate AND P.program_id = :programId AND P.location_id = :locationId " +
						"UNION " +
						"SELECT " +
						"    operation_id operationId, " +
						"    operation_date operationDate, " +
						"    pi.identifier patientIdentifier, " +
						"    p3.gender patientGender, " +
						"    FLOOR(DATEDIFF(:endDate, birthdate)/365.25) patientAge, " +
						"    IF(FLOOR(DATEDIFF(:endDate, birthdate)/365.25) < 15, 'enfant', 'adulte') category, " +
						"    name regimen, " +
						"    N.value_numeric treatmentDays, " +
						"    V.value_datetime treatmentEndDate, " +
						"    P.products " +
						"FROM ( " +
						"         SELECT " +
						"             operation_id, GROUP_CONCAT(CONCAT_WS(':', code, retail_name, quantity) SEPARATOR '|') products, operation_date, program_id, location_id " +
						"         FROM " +
						"             ( " +
						"                 SELECT " +
						"                     ppaf.quantity, " +
						"                     operation_id, " +
						"                     code, " +
						"                     retail_name, " +
						"                     ppo.operation_date, " +
						"                    ppo.program_id, " +
						"                    ppo.location_id " +
						" " +
						"                 FROM " +
						"                     pharmacy_product_operation ppo, " +
						"                     pharmacy_product_attribute_flux ppaf, " +
						"                     pharmacy_product_attribute ppa, " +
						"                     pharmacy_product pp " +
						"                 WHERE " +
						"                         ppaf.operation_id = ppo.product_operation_id AND " +
						"                         ppaf.product_attribute_id = ppa.product_attribute_id AND " +
						"                         ppa.product_id = pp.product_id AND" +
						"                         ppo.operation_status = 2" +
						"             ) _ " +
						"         GROUP BY operation_id " +
						"     ) P " +
						"    LEFT JOIN pharmacy_product_dispensation ppd ON ppd.product_operation_id = P.operation_id " +
						"    INNER JOIN encounter e on ppd.encounter_id = e.encounter_id " +
						"    LEFT JOIN patient p2 on e.patient_id = p2.patient_id " +
						"    LEFT JOIN patient_identifier pi on p2.patient_id = pi.patient_id " +
						"    LEFT JOIN person p3 on p2.patient_id = p3.person_id " +
						"    LEFT JOIN (SELECT encounter_id, value_coded FROM obs WHERE concept_id =  165033 AND voided = 0) R ON R.encounter_id = e.encounter_id " +
						"    LEFT JOIN (SELECT * FROM concept_name WHERE (name LIKE '% 3TC %' OR name LIKE '% FTC %' OR name LIKE '% ABC %' OR name LIKE '% DDI %' OR name = 'COTRIMOXAZOLE') GROUP BY concept_id) cn on R.value_coded = cn.concept_id " +
						"    LEFT JOIN (SELECT encounter_id, value_datetime FROM obs WHERE concept_id =  165040 AND voided = 0) V ON V.encounter_id = e.encounter_id " +
						"    LEFT JOIN (SELECT encounter_id, value_numeric FROM obs WHERE concept_id =  165011 AND voided = 0) N ON N.encounter_id = e.encounter_id " +
						"WHERE operation_date BETWEEN :startDate AND :endDate AND P.program_id = :programId AND P.location_id = :locationId";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("operationId", StandardBasicTypes.INTEGER)
				.addScalar("operationDate", StandardBasicTypes.DATE)
				.addScalar("patientIdentifier", StandardBasicTypes.STRING)
				.addScalar("patientGender", StandardBasicTypes.STRING)
				.addScalar("patientAge", StandardBasicTypes.INTEGER)
				.addScalar("category", StandardBasicTypes.STRING)
				.addScalar("regimen", StandardBasicTypes.STRING)
				.addScalar("treatmentDays", StandardBasicTypes.INTEGER)
				.addScalar("treatmentEndDate", StandardBasicTypes.DATE)
				.addScalar("products", StandardBasicTypes.STRING)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("locationId", location.getLocationId())
				.setParameter("programId", productProgram.getProductProgramId())
				.setResultTransformer(new AliasToBeanResultTransformer(DispensationHistoryDTO.class));
		try {
			return query.list();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}


}
