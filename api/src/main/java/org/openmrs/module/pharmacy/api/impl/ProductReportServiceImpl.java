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
package org.openmrs.module.pharmacy.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.ProductReportService;
import org.openmrs.module.pharmacy.api.db.ProductReportDAO;
import org.openmrs.module.pharmacy.models.ProductReportLineDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link ProductReportService}.
 */
public class ProductReportServiceImpl extends BaseOpenmrsService implements ProductReportService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ProductReportDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(ProductReportDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public ProductReportDAO getDao() {
	    return dao;
    }

    @Override
    public List<ProductReport> getAllProductReports(Location location, Boolean includeVoided) {
        return dao.getAllProductReports(location, includeVoided);
    }

    @Override
    public List<ProductReport> getAllProductReports(Location location, ProductProgram productProgram, Boolean includeVoided) {
        return dao.getAllProductReports(location, productProgram, includeVoided);
    }

    @Override
    public List<ProductReport> getAllProductDistributionReports(Location location, Boolean includeVoided) {
        return dao.getAllProductDistributionReports(location, includeVoided);
    }

    @Override
    public List<ProductReport> getAllSubmittedChildProductReports(Location location, Boolean includeVoided) {
        return dao.getAllSubmittedChildProductReports(location, includeVoided);
    }

    @Override
    public List<ProductReport> getAllProductReports(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductReports(location, includeVoided, operationStartDate, operationEndDate);
    }

    @Override
    public List<ProductReport> getAllProductReports(Location location) {
        return dao.getAllProductReports(location);
    }

    @Override
    public List<ProductReport> getAllProductReports(Boolean includeVoided) {
        return dao.getAllProductReports(includeVoided);
    }

    @Override
    public ProductReport getOneProductReportById(Integer id) {
        return dao.getOneProductReportById(id);
    }

    @Override
    public ProductReport getOneProductReportByReportPeriodAndProgram(String reportPeriod, ProductProgram productProgram, Location location, Boolean includeVoided) {
        return dao.getOneProductReportByReportPeriodAndProgram(reportPeriod, productProgram, location, includeVoided);
    }

    @Override
    public ProductReport saveProductReport(ProductReport productReport) {
        return dao.saveProductReport(productReport);
    }

    @Override
    public ProductReport editProductReport(ProductReport productReport) {
        return dao.editProductReport(productReport);
    }

    @Override
    public void removeProductReport(ProductReport productReport) {
        dao.removeProductReport(productReport);
    }

    @Override
    public ProductReport getOneProductReportByUuid(String uuid) {
        return dao.getOneProductReportByUuid(uuid);
    }

    @Override
    public List<ProductReportLineDTO> getProductReportFluxDTOs(ProductReport productReport) {
        return dao.getProductReportFluxDTOs(productReport);
    }

    @Override
    public Integer getProductReceivedQuantityInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) {
        return dao.getProductReceivedQuantityInLastOperationByProduct(product, inventory, location, isUrgent);
    }

    @Override
    public Integer getProductQuantityInStockInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) {
        return getDao().getProductInitialQuantityByProduct(product, inventory, location, isUrgent);
    }

    @Override
    public Integer getProductQuantityInStockOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) {
        return dao.getProductQuantityInStockOperationByProduct(product, inventory, location, isUrgent);
    }

    @Override
    public Integer getProductQuantityLostInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) {
        return dao.getProductQuantityLostInLastOperationByProduct(product, inventory, location, isUrgent);
    }

    @Override
    public Integer getProductQuantityAdjustmentInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) {
        return dao.getProductQuantityAdjustmentInLastOperationByProduct(product, inventory, location, isUrgent);
    }

    @Override
    public Integer getProductQuantityDistributedInLastOperationByProduct(Product product, ProductInventory inventory, Location location, Boolean isUrgent) {
        return dao.getProductQuantityDistributedInLastOperationByProduct(product, inventory, location, isUrgent);
    }

    @Override
    public Integer getChildLocationsThatKnownRupture(Product product, ProductInventory inventory, Location location) {
        return dao.getChildLocationsThatKnownRupture(product, inventory, location);
    }

    @Override
    public Integer getProductQuantityDistributedInAgo1MonthOperationByProduct(Product product, ProductInventory inventory, Location location) {
        return dao.getProductQuantityDistributedInAgo1MonthOperationByProduct(product, inventory, location);
    }

    @Override
    public Integer getProductQuantityDistributedInAgo2MonthOperationByProduct(Product product, ProductInventory inventory, Location location) {
        return dao.getProductQuantityDistributedInAgo2MonthOperationByProduct(product, inventory, location);
    }

    @Override
    public Double getProductAverageMonthlyConsumption(Product product, ProductProgram productProgram, Location location, Boolean includeVoided) {
        return dao.getProductAverageMonthlyConsumption(product, productProgram, location, includeVoided);
    }

    @Override
    public List<Product> getAllActivityProducts(ProductInventory inventory) {
        return dao.getAllActivityProducts(inventory);
    }

    @Override
    public ProductReport getLastProductReport(Location location, ProductProgram productProgram) {
        return dao.getLastProductReport(location, productProgram);
    }

    @Override
    public ProductReport getLastProductReportByDate(Location location, ProductProgram productProgram, Date reportDate) {
        return dao.getLastProductReportByDate(location, productProgram, reportDate);
    }

    @Override
    public List<ProductReportLineDTO> getReportDistributionLines(ProductReport report) {
        return dao.getReportDistributionLines(report);
    }

    @Override
    public ProductAttributeOtherFlux getPreviousReportProductAttributeOtherFluxByLabel(Product product, String label, ProductReport report, Location location) {
        return dao.getPreviousReportProductAttributeOtherFluxByLabel(product, label, report, location);
    }

//
//    @Override
//    public List<ProductReportReturnDTO> getProductReportReturnDTOs(ProductReport productReport) {
//        return dao.getProductReportReturnDTOs(productReport);
//    }
//
//    @Override
//    public ProductReportReturnDTO getOneProductReportReturnDTO(ProductReport reception, ProductAttribute productAttribute) {
//        return dao.getOneProductReportReturnDTO(reception, productAttribute);
//    }
//
//    @Override
//    public List<ProductReportListDTO> getProductReportListDTOs() {
//        return dao.getProductReportListDTOs();
//    }

}
