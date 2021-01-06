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
import org.openmrs.module.pharmacy.ProductAttributeStock;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.ProductAttributeStockService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@ModelAttribute("isDirectClient")
	public Boolean isDirectClient() {
		return OperationUtils.isDirectClient(OperationUtils.getUserLocation());
	}

	@RequestMapping(value = "/module/pharmacy/manage", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
	}

	@RequestMapping(value = "/module/pharmacy/operations/stock/list.form", method = RequestMethod.GET)
	public void stock(ModelMap modelMap) {
		if (Context.isAuthenticated()) {
			modelMap.addAttribute("stocks", stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation(), false));
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
						location.getName().contains("CHU") ||
						location.getName().contains("CHR") ||
						location.getName().contains("HG") ||
						location.getParentLocation() != null ||
						OperationUtils.isDirectClient(location) || location.getLocationId().equals(1)) {
					allLocations.remove(location);
				}
			}
			allLocations.removeAll(OperationUtils.getUserLocations());

			modelMap.addAttribute("locations", locations);
			modelMap.addAttribute("userLocation", OperationUtils.getUserLocation());
			modelMap.addAttribute("title", "Gestion du centre / District");
			modelMap.addAttribute("locationPrograms", locationPrograms);
			modelMap.addAttribute("programs", programService().getAllProductProgram());
			modelMap.addAttribute("allLocations", allLocations);
		}
	}

}
