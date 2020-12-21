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
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductAttributeStock;
import org.openmrs.module.pharmacy.api.ProductAttributeStockService;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * The main controller.
 */
@Controller
public class PharmacyManageController {
	
	protected final Log log = LogFactory.getLog(getClass());

	ProductAttributeStockService stockService() {
		return Context.getService(ProductAttributeStockService.class);
	}

	@RequestMapping(value = "/module/pharmacy/manage", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
	}

	@RequestMapping(value = "/module/pharmacy/operations/stock/list.form", method = RequestMethod.GET)
	public void stock(ModelMap modelMap) {
//		List<ProductAttributeStock> stocks = stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation());
		modelMap.addAttribute("user", Context.getAuthenticatedUser());
		modelMap.addAttribute("stocks", stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation(), false));
	}
}
