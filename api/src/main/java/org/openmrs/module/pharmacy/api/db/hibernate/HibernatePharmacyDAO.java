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
import org.openmrs.api.db.hibernate.DbSessionFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
@Repository
public class HibernatePharmacyDAO implements PharmacyDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private DbSessionFactory sessionFactory;

	public HibernatePharmacyDAO() {}

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
//		StringBuilder locationIds = new StringBuilder("(" + location.getLocationId());
//		// locationIds.add(location.getLocationId());
//		if (byLocation) {
//			for (Location childLocation : location.getChildLocations()) {
//				locationIds.append(", ").append(childLocation.getLocationId());
//			}
//		}
//		locationIds.append(")");
//		System.out.println("-------------------------------> creating sql");

		String sqlQuery =
				"SELECT " +
						"    l.location_id locationId, " +
						"    l.name locationName, " +
						"    pps.code, " +
						"    pps.retail_name retailName, " +
						"    pps.wholesale_name wholesaleName, " +
						"    SUM(IF(ppo.quantityDistributed, quantityDistributed, quantityReported)) retailQuantity, " +
						"    FLOOR(SUM(IF(ppo.quantityDistributed, quantityDistributed, quantityReported)) / pps.unit_conversion) wholesaleQuantity " +
						" " +
						"FROM " +
						"( " +
						"    SELECT " +
						"        po.operation_date, po.operation_status, ppaof.quantity quantityReported, NULL AS quantityDistributed, po.location_id, pp.product_id, program_id " +
						"    FROM pharmacy_product_operation po, " +
						"         pharmacy_product_report ppr, " +
						"         pharmacy_product_attribute_other_flux ppaof, " +
						"         (SELECT product_id FROM pharmacy_product) pp " +
						"    WHERE " +
						"        po.product_operation_id = ppr.product_operation_id AND " +
						"        ppaof.operation_id = po.product_operation_id AND " +
						"        pp.product_id = ppaof.product_id AND " +
						"        ppr.report_period IN (:reportPeriod) AND " +
						"        label = 'QD' AND po.voided = 0 " +
						" " +
						"    UNION " +
						"    SELECT " +
						"        po.operation_date, po.operation_status, NULL AS quantityReported, ppaf.quantity quantityDistributed, po.location_id, pp.product_id, program_id " +
						"    FROM (SELECT * FROM pharmacy_product_operation WHERE operation_date BETWEEN :startDate AND :endDate) po " +
						"             INNER JOIN pharmacy_product_dispensation ppd on po.product_operation_id = ppd.product_operation_id " +
						"             LEFT JOIN pharmacy_product_attribute_flux ppaf on po.product_operation_id = ppaf.operation_id " +
						"             LEFT JOIN pharmacy_product_attribute ppa ON ppaf.product_attribute_id = ppa.product_attribute_id " +
						"             LEFT JOIN (SELECT product_id FROM pharmacy_product) pp ON ppa.product_id = pp.product_id " +
						"    WHERE " +
						"        po.voided = 0 " +
						") ppo " +
						"LEFT JOIN pharmacy_product pps ON pps.product_id = ppo.product_id " +
						"LEFT JOIN location l ON l.location_id = ppo.location_id " +
						"WHERE " +
						"    program_id = :programId AND " +
						"    (l.location_id = :locationId OR l.parent_location = :locationId) AND " +
						"    operation_status IN (2, 4, 5, 6) " +
						"GROUP BY l.location_id, pps.product_id";

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
				.addScalar("locationId", StandardBasicTypes.INTEGER)
				.addScalar("code", StandardBasicTypes.STRING)
				.addScalar("retailName", StandardBasicTypes.STRING)
				.addScalar("wholesaleName", StandardBasicTypes.STRING)
				.addScalar("retailQuantity", StandardBasicTypes.INTEGER)
				.addScalar("wholesaleQuantity", StandardBasicTypes.INTEGER)
				.setParameter("programId", productProgram.getProductProgramId())
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("locationId", location.getLocationId())
				.setParameterList("reportPeriod", OperationUtils.getReportPeriodOfPeriod(startDate, endDate).toArray())
				.setResultTransformer(new AliasToBeanResultTransformer(ProductQuantityDTO.class));
		try {
			List<ProductQuantityDTO> productQuantities = query.list();

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
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return null;
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



}
