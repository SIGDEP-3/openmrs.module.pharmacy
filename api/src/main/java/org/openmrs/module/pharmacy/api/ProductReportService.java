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
import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.models.ProductReportLineDTO;
import org.openmrs.module.pharmacy.utils.PrivilegeConstants;
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

	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	List<ProductReport> getAllProductReports(Location location, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	List<ProductReport> getAllProductReports(Location location, ProductProgram productProgram, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	List<ProductReport> getAllProductDistributionReports(Location location, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	List<ProductReport> getAllSubmittedChildProductReports(Location location, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	List<ProductReport> getAllProductReports(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	List<ProductReport> getAllProductReports(Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	List<ProductReport> getAllProductReports(Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	ProductReport getOneProductReportById(Integer id);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	ProductReport getOneProductReportByReportPeriodAndProgram(String reportPeriod, ProductProgram productProgram, Location location, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.SAVE_REPORT})
	ProductReport saveProductReport(ProductReport productReport);
	@Authorized(value = {PrivilegeConstants.SAVE_REPORT})
	ProductReport editProductReport(ProductReport productReport);
	@Authorized(value = {PrivilegeConstants.REMOVE_REPORT})
	void removeProductReport(ProductReport productReport);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	ProductReport getOneProductReportByUuid(String uuid);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	List<ProductReportLineDTO> getProductReportFluxDTOs(ProductReport productReport);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	Integer getProductReceivedQuantityInLastOperationByProduct(Product product, ProductInventory inventory, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	Integer getProductQuantityInStockInLastOperationByProduct(Product product, ProductInventory inventory, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	Integer getProductQuantityInStockOperationByProduct(Product product, ProductInventory inventory, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	Integer getProductQuantityLostInLastOperationByProduct(Product product, ProductInventory inventory, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	Integer getProductQuantityAdjustmentInLastOperationByProduct(Product product, ProductInventory inventory, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	Integer getProductQuantityDistributedInLastOperationByProduct(Product product, ProductInventory inventory, Location userLocation);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	Integer getChildLocationsThatKnownRupture(Product product, ProductInventory inventory, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	Integer getProductQuantityDistributedInAgo1MonthOperationByProduct(Product product, ProductInventory inventory, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	Integer getProductQuantityDistributedInAgo2MonthOperationByProduct(Product product, ProductInventory inventory, Location location);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	Double getProductAverageMonthlyConsumption(Product product, ProductProgram productProgram, Location location, Boolean includeVoided);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	List<Product> getAllActivityProducts(ProductInventory inventory);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	ProductReport getLastProductReport(Location location, ProductProgram productProgram);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	ProductReport getLastProductReportByDate(Location location, ProductProgram productProgram, Date reportDate);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	List<ProductReportLineDTO> getReportDistributionLines(ProductReport report);
	@Authorized(value = {PrivilegeConstants.VIEW_REPORT})
	ProductAttributeOtherFlux getPreviousReportProductAttributeOtherFluxByLabel(Product product, String label, ProductReport report, Location location);
//	List<ProductReportReturnDTO> getProductReportReturnDTOs(ProductReport productReport);
//	ProductReportReturnDTO getOneProductReportReturnDTO(ProductReport reception, ProductAttribute productAttribute);
//	List<ProductReportListDTO> getProductReportListDTOs();
}
