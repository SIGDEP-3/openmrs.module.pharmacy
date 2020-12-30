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
package org.openmrs.module.pharmacy.api;

import org.openmrs.Location;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.ProductReport;
import org.openmrs.module.pharmacy.models.ProductReportLineDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(PharmacyService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface ProductReportService extends OpenmrsService {
     
	/*
	 * Add service methods here
	 * 
	 */

	List<ProductReport> getAllProductReports(Location location, Boolean includeVoided);
	List<ProductReport> getAllProductReports(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	List<ProductReport> getAllProductReports(Location location);
	List<ProductReport> getAllProductReports(Boolean includeVoided);
	ProductReport getOneProductReportById(Integer id);
	ProductReport saveProductReport(ProductReport productReport);
	ProductReport editProductReport(ProductReport productReport);
	void removeProductReport(ProductReport productReport);
	ProductReport getOneProductReportByUuid(String uuid);
	List<ProductReportLineDTO> getProductReportFluxDTOs(ProductReport productReport);
	Integer getProductReceivedQuantityInLastOperationByProduct(Product product, ProductInventory inventory, Location location);
	Integer getProductQuantityInStockInLastOperationByProduct(Product product, ProductInventory inventory, Location location);
	Integer getProductQuantityLostInLastOperationByProduct(Product product, ProductInventory inventory, Location location);
	Integer getProductQuantityAdjustmentInLastOperationByProduct(Product product, ProductInventory inventory, Location location);
	List<Product> getAllActivityProducts(ProductInventory inventory);
//	List<ProductReportReturnDTO> getProductReportReturnDTOs(ProductReport productReport);
//	ProductReportReturnDTO getOneProductReportReturnDTO(ProductReport reception, ProductAttribute productAttribute);
//	List<ProductReportListDTO> getProductReportListDTOs();
}
