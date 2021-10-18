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
package org.openmrs.module.pharmacy.api.db;

import org.openmrs.Location;
import org.openmrs.module.pharmacy.entities.*;
import org.openmrs.module.pharmacy.dto.ProductReportLineDTO;

import java.util.Date;
import java.util.List;

/**
 *  Database methods for {@link org.openmrs.module.pharmacy.api.ProductReportService}.
 */
public interface ProductReportDAO {

	List<ProductReport> getAllProductReports(Location location, Boolean includeVoided);
	List<ProductReport> getAllProductReports(Location location, ProductProgram productProgram, Boolean includeVoided);
	List<ProductReport> getAllProductReports(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	List<ProductReport> getAllProductReports(Location location);
	List<ProductReport> getAllProductReports(Boolean includeVoided);
	List<ProductReport> getAllProductDistributionReports(Location location, Boolean includeVoided);
	List<ProductReport> getAllSubmittedChildProductReports(Location location, Boolean includeVoided);
	List<ProductReport> getAllTreatedChildProductReports(Location location, Boolean includeVoided);
	ProductReport getLastTreatedChildProductReports(Location location, Boolean includeVoided, ProductProgram productProgram, Date operationDate);
	ProductReport getLastTreatedChildProductReportsByProduct(Product product, Location location, Boolean includeVoided, ProductProgram productProgram, Date operationDate);
	List<ProductReport> getPeriodTreatedChildProductReports(Location location, ProductInventory inventory, Boolean includeVoided, Date operationDate);
	Integer getCountProductQuantityInLastTreatment(Location location, Boolean includeVoided, ProductProgram productProgram, Date operationDate, Product product);
	Integer getCountProductQuantityInPeriodTreatment(Location location, ProductInventory productInventory, Boolean includeVoided, Date operationDate, Product product);
	ProductReport getOneProductReportById(Integer id);
	ProductReport getOneProductReportByReportPeriodAndProgram(String reportPeriod, ProductProgram productProgram, Location location, Boolean includeVoided);
	ProductReport saveProductReport(ProductReport productReport);
	ProductReport editProductReport(ProductReport productReport);
	void removeProductReport(ProductReport productReport);
	ProductReport getOneProductReportByUuid(String uuid);
	List<ProductReportLineDTO> getProductReportFluxDTOs(ProductReport productReport);
	Integer getProductQuantityInStockOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent);
	Integer getProductReceivedQuantityInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent);
	Integer getProductInitialQuantityByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent);
	Integer getProductQuantityLostInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent);
	Integer getProductQuantityAdjustmentInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent);
	Integer getProductQuantityDistributedInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent);
	Integer getChildLocationsThatKnownRupture(Product product, ProductInventory inventory, Location location);
	Integer getProductQuantityDistributedInAgo1MonthOperationByProduct(Product product, ProductInventory inventory, Location location);
	Integer getProductQuantityDistributedInAgo2MonthOperationByProduct(Product product, ProductInventory inventory, Location location);
	Double getProductAverageMonthlyConsumption(Product product, ProductProgram productProgram, Location location, Boolean includeVoided);
	List<Product> getAllActivityProducts(ProductInventory inventory);
	ProductReport getLastProductReport(Location location, ProductProgram productProgram, Boolean urgent);
	ProductReport getLastProductReportByDate(Location location, ProductProgram productProgram, Date reportDate, Boolean urgent);
	List<ProductReportLineDTO> getReportDistributionLines(ProductReport report);
	ProductAttributeOtherFlux getPreviousReportProductAttributeOtherFluxByLabel(Product product, String label, ProductReport report, Location location);
	ProductReport getLastProductReportByProductAndByDate(Location location, ProductProgram productProgram, Product product, Date reportDate, Boolean urgent);
    ProductReport getPeriodTreatedProductReportsByReportPeriodAndLocation(String reportPeriod, ProductProgram program, Location childLocation, boolean isUrgent);

    ProductReport getLatestReportByLocationAndInventory(Location location, ProductInventory inventory);
	ProductReport getLatestDistributionByLocationAndInventory(Location location, Location reportLocation, ProductInventory inventory);
//	List<ProductReportReturnDTO> getProductReportReturnDTOs(ProductReport productReport);
//	ProductReportReturnDTO getOneProductReportReturnDTO(ProductReport reception, ProductAttribute productAttribute);
//	List<ProductReportListDTO> getProductReportListDTOs();
}
