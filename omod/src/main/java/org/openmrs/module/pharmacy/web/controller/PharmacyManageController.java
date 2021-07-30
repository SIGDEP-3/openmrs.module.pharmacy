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
package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.ProductAttributeStock;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductAttributeStockService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.dto.ConsumptionReportDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * The main controller.
 */
@Controller
public class PharmacyManageController {
	
	protected final Log log = LogFactory.getLog(getClass());

	ProductAttributeStockService stockService() {
		return Context.getService(ProductAttributeStockService.class);
	}
	ProductProgramService programService() {
		return Context.getService(ProductProgramService.class);
	}
	PharmacyService service() {
		return Context.getService(PharmacyService.class);
	}

	@ModelAttribute("isDirectClient")
	public Boolean isDirectClient() {
		return OperationUtils.isDirectClient(OperationUtils.getUserLocation());
	}

	@ModelAttribute("canDistribute")
	public Boolean canDistribute() {
		return OperationUtils.canDistribute(OperationUtils.getUserLocation());
	}

	@RequestMapping(value = "/module/pharmacy/manage", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
	}

	@RequestMapping(value = "/module/pharmacy/operations/stock/list.form", method = RequestMethod.GET)
	public void stock(ModelMap modelMap,
					  @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId) {
		if (Context.isAuthenticated()) {
			modelMap.addAttribute("title", "Stock des produits par lots");
			modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
			List<ProductAttributeStock> stocks = new ArrayList<ProductAttributeStock>();
			if (programId != 0) {
				ProductProgram productProgram = programService().getOneProductProgramById(programId);
				if (productProgram != null) {
					List<ProductAttributeStock> tmpStocks = stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation(), false);
					for (ProductAttributeStock stock : tmpStocks) {
						if (stock.getProductAttribute().getProduct().getProductPrograms().contains(productProgram)){
							stocks.add(stock);
						}
					}
				}
			}
			modelMap.addAttribute("stocks", stocks);
		}
	}

	@RequestMapping(value = "/module/pharmacy/operations/stock/operationStatus.form", method = RequestMethod.GET)
	public void operations(ModelMap modelMap,
					  @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
					  @RequestParam(value = "startDade", defaultValue = "", required = false) Date startDate,
					  @RequestParam(value = "endDade", defaultValue = "", required = false) Date endDade) {
		if (Context.isAuthenticated()) {
			modelMap.addAttribute("title", "Etats des mouvenents de stock");
			modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
			modelMap.addAttribute("stocks", stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation(), false));
		}
	}

	@RequestMapping(value = "/module/pharmacy/operations/stock/transferStatus.form", method = RequestMethod.GET)
	public void transfer(ModelMap modelMap,
					  @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
					  @RequestParam(value = "startDade", defaultValue = "", required = false) Date startDate,
					  @RequestParam(value = "endDade", defaultValue = "", required = false) Date endDade) {
		if (Context.isAuthenticated()) {
			modelMap.addAttribute("title", "Etat de transfert");
			modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
			modelMap.addAttribute("transfers", null);
		}
	}

	@RequestMapping(value = "/module/pharmacy/operations/stock/stockStatus.form", method = RequestMethod.GET)
	public void stocks(ModelMap modelMap,
					  @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
					  @RequestParam(value = "startDade", defaultValue = "", required = false) Date startDate,
					  @RequestParam(value = "endDade", defaultValue = "", required = false) Date endDade) {
		if (Context.isAuthenticated()) {
			modelMap.addAttribute("title", "Etat de Stock");
			modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
			modelMap.addAttribute("stocks", stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation(), false));
		}
	}

	@RequestMapping(value = "/module/pharmacy/operations/stock/productConsumption.form", method = RequestMethod.GET)
	public void productConsumption(ModelMap modelMap,
					  @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
					  @RequestParam(value = "startDate", defaultValue = "", required = false) Date startDate,
					  @RequestParam(value = "endDate", defaultValue = "", required = false) Date endDate,
					  @RequestParam(value = "byWholesaleUnit", defaultValue = "false", required = false) Boolean byWholesaleUnit,
					  @RequestParam(value = "bySite", defaultValue = "false", required = false) Boolean bySite) {
		if (Context.isAuthenticated()) {
			modelMap.addAttribute("title", "Consommation de produit par période");
			modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
			if (programId != null && startDate != null && endDate != null) {
				ConsumptionReportDTO dto = service().getConsumptionReport(
						programService().getOneProductProgramById(programId),
						startDate,
						endDate,
						OperationUtils.getUserLocation(),
						bySite
				);
//				System.out.println("Obtained values from query ");
				dto.setByWholesaleUnit(byWholesaleUnit);
				modelMap.addAttribute("dto", dto);
			}
		}
	}

	@RequestMapping(value = "/module/pharmacy/center/manage.form", method = RequestMethod.GET)
	public void center(ModelMap modelMap) {
		if (Context.isAuthenticated()) {
			List<Location> locations = OperationUtils.getUserLocations();
			List<Location> locationAsClientList = new ArrayList<Location>();
			Map<Integer, List<Integer>> locationPrograms = new HashMap<Integer, List<Integer>>();

			for (Location location : locations) {
				if (OperationUtils.isDirectClient(location) && !location.equals(OperationUtils.getUserLocation())) {
					locationAsClientList.add(location);
				}
				List<Integer> programIds = new ArrayList<Integer>();
				for (ProductProgram program : OperationUtils.getLocationPrograms(location)) {
					programIds.add(program.getProductProgramId());
				}
				locationPrograms.put(location.getLocationId(), programIds);
			}

			if (locationAsClientList.size() != 0) {
				locations.removeAll(locationAsClientList);
			}

			List<Location> allLocations = Context.getLocationService().getAllLocations();
			for (Location location : Context.getLocationService().getAllLocations()) {
				if (location.getName().contains("DISTRICT SANITAIRE") ||
						location.getName().contains("REGION SANITAIRE") ||
						location.getName().startsWith("CHU") ||
						location.getName().startsWith("CHR") ||
						location.getName().startsWith("HG") ||
						location.getParentLocation() != null ||
						OperationUtils.isDirectClient(location) || location.getLocationId().equals(1)) {
					allLocations.remove(location);
				}
			}
			allLocations.removeAll(OperationUtils.getUserLocations());

			modelMap.addAttribute("locations", locations);
			modelMap.addAttribute("code", OperationUtils.getUserLocationCode());
			modelMap.addAttribute("userLocation", OperationUtils.getUserLocation());
			modelMap.addAttribute("title", "Gestion du centre / District");
			modelMap.addAttribute("locationPrograms", locationPrograms);
			modelMap.addAttribute("programs", programService().getAllProductProgram());
			modelMap.addAttribute("allLocations", allLocations);
			modelMap.addAttribute("services", OperationUtils.getServices());
		}
	}
}
