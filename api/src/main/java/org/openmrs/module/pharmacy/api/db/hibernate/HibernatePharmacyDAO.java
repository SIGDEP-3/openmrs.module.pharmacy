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
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeStockService;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.models.ConsumptionReportDTO;
import org.openmrs.module.pharmacy.models.LocationProductQuantity;
import org.openmrs.module.pharmacy.models.ProductOutFluxDTO;
import org.openmrs.module.pharmacy.models.ProductQuantity;

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
	public Boolean validateOperation(ProductOperation operation) {
		if (!operation.getIncidence().equals(Incidence.NONE)) {
			if (!operation.getOperationStatus().equals(OperationStatus.VALIDATED)) {
				Set<ProductAttributeFlux> fluxes = operation.getProductAttributeFluxes();
				if (fluxes != null && fluxes.size() != 0) {
					for (ProductAttributeFlux flux : fluxes) {
						ProductAttributeStock attributeStock = Context.getService(ProductAttributeStockService.class).getOneProductAttributeStockByAttribute(flux.getProductAttribute(), operation.getLocation(), false);
						if (attributeStock != null) {
							Integer quantity = operation.getIncidence().equals(Incidence.POSITIVE) ?
									attributeStock.getQuantityInStock() + flux.getQuantity() :
									(operation.getIncidence().equals(Incidence.NEGATIVE) ? attributeStock.getQuantityInStock() - flux.getQuantity() : flux.getQuantity());
							attributeStock.setQuantityInStock(quantity);
						} else {
							attributeStock = new ProductAttributeStock();
							attributeStock.setQuantityInStock(flux.getQuantity());
							attributeStock.setLocation(operation.getLocation());
							attributeStock.setProductAttribute(flux.getProductAttribute());
						}
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
							ProductAttributeStock attributeStock = Context.getService(ProductAttributeStockService.class).getOneProductAttributeStockByAttribute(flux.getProductAttribute(), operation.getLocation(), false);
							if (attributeStock != null) {
								Integer quantity = operation.getIncidence().equals(Incidence.POSITIVE) ?
										attributeStock.getQuantityInStock() - flux.getQuantity() :
										(operation.getIncidence().equals(Incidence.NEGATIVE) ? attributeStock.getQuantityInStock() + flux.getQuantity() : flux.getQuantity());
								attributeStock.setQuantityInStock(quantity);
							}
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
						"ppaf.product_attribute_flux_id productAttributeFluxId, " +
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
		StringBuilder locationIds = new StringBuilder("(" + location.getLocationId());
		// locationIds.add(location.getLocationId());
		if (byLocation) {
			for (Location childLocation : location.getChildLocations()) {
				locationIds.append(", ").append(childLocation.getLocationId());
			}
		}
		locationIds.append(")");

		String sqlQuery =
				"SELECT " +
						"    l.location_id locationId, " +
						"    pp.code, " +
						"    pp.retail_name retailName, " +
						"    pp.wholesale_name wholesaleName, " +
						"    SUM(ppaf.quantity) retailQuantity, " +
						"    FLOOR(SUM(ppaf.quantity) / pp.unit_conversion) wholesaleQuantity " +
						"FROM " +
						"pharmacy_product_operation ppo " +
						"INNER JOIN pharmacy_product_dispensation ppd on ppo.product_operation_id = ppd.product_operation_id " +
						"LEFT JOIN pharmacy_product_attribute_flux ppaf on ppo.product_operation_id = ppaf.operation_id " +
						"LEFT JOIN pharmacy_product_attribute ppa ON ppaf.product_attribute_id = ppa.product_attribute_id " +
						"LEFT JOIN pharmacy_product pp ON ppa.product_id = pp.product_id " +
						"LEFT JOIN (SELECT * FROM location WHERE location_id IN " + locationIds + ") l on ppaf.location_id = l.location_id " +
						"WHERE " +
						"      ppo.operation_status = 2 AND " +
						"      ppo.program_id = :programId AND " +
						"      ppo.operation_date BETWEEN :startDate AND :endDate " +
						"GROUP BY l.location_id, pp.product_id ";

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
//				.setParameterList("location", locationIds.toString())
				.setResultTransformer(new AliasToBeanResultTransformer(ProductQuantity.class));
		try {
			List<ProductQuantity> productQuantities = query.list();
//			System.out.println("-------------------------------> productQuantities "+ productQuantities.size());


			if (byLocation) {
				for (Location l : location.getChildLocations()) {
					LocationProductQuantity locationProductQuantity = new LocationProductQuantity(l.getName());

					for (ProductQuantity productQuantity : productQuantities) {
						if (productQuantity.getLocationId().equals(l.getLocationId())) {
							locationProductQuantity.getProductQuantities().add(productQuantity);
						}
					}
					consumptionReportDTO.getLocationProductQuantities().add(locationProductQuantity);
				}
			} else {
				LocationProductQuantity locationProductQuantity = new LocationProductQuantity(location.getName());

				for (ProductQuantity productQuantity : productQuantities) {
					if (productQuantity.getLocationId().equals(location.getLocationId())) {
						locationProductQuantity.getProductQuantities().add(productQuantity);
					}
				}
				consumptionReportDTO.getLocationProductQuantities().add(locationProductQuantity);
			}


			System.out.println("-------------------------------> consumptionReportDTO getLocationProductQuantities "
					+ consumptionReportDTO.getLocationProductQuantities().size());
			return consumptionReportDTO;
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
