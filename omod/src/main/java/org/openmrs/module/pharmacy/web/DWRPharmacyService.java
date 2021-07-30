package org.openmrs.module.pharmacy.web;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.Product;
import org.openmrs.module.pharmacy.api.ProductAttributeStockService;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.utils.OperationUtils;

public class DWRPharmacyService {
    public Integer getProductQuantityInStock(Integer productId) {
        Product product = productService().getOneProductById(productId);
        return stockService().getAllProductAttributeStockByProductCount(product, OperationUtils.getUserLocation(), false);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }

    private ProductService productService() {
        return Context.getService(ProductService.class);
    }


}
