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
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.ProductReport;
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
    public Integer getProductReceivedQuantityInLastOperationByProduct(Product product, ProductInventory inventory, Location location) {
        return dao.getProductReceivedQuantityInLastOperationByProduct(product, inventory, location);
    }

    @Override
    public Integer getProductQuantityInStockInLastOperationByProduct(Product product, ProductInventory inventory, Location location) {
        return getDao().getProductQuantityInStockInLastOperationByProduct(product, inventory, location);
    }

    @Override
    public Integer getProductQuantityLostInLastOperationByProduct(Product product, ProductInventory inventory, Location location) {
        return dao.getProductQuantityLostInLastOperationByProduct(product, inventory, location);
    }

    @Override
    public Integer getProductQuantityAdjustmentInLastOperationByProduct(Product product, ProductInventory inventory, Location location) {
        return dao.getProductQuantityAdjustmentInLastOperationByProduct(product, inventory, location);
    }

    @Override
    public List<Product> getAllActivityProducts(ProductInventory inventory) {
        return dao.getAllActivityProducts(inventory);
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
