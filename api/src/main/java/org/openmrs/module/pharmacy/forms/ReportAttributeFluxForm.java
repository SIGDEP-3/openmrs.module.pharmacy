package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.models.ProductReportLineDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportAttributeFluxForm extends ProductAttributeFluxForm {
    private ProductInventory inventory;

    public ReportAttributeFluxForm() {
    }

    public ProductInventory getInventory() {
        return inventory;
    }

    public void setInventory(ProductInventory inventory) {
        this.inventory = inventory;
    }

    public ProductReportLineDTO createProductReportOtherFluxes(Product product) {
        ProductReportLineDTO reportLineDTO = new ProductReportLineDTO();
        reportLineDTO.setCode(product.getCode());
        reportLineDTO.setRetailName(product.getRetailName());
        reportLineDTO.setRetailUnit(product.getProductRetailUnit().getName());
        reportLineDTO.setInitialQuantity(createProductAttributeOtherFlux(product, reportService().getProductQuantityInStockInLastOperationByProduct(product, getInventory(),OperationUtils.getUserLocation()).doubleValue(), "SI").getQuantity().intValue());
        reportLineDTO.setReceivedQuantity(createProductAttributeOtherFlux(product, reportService().getProductReceivedQuantityInLastOperationByProduct(product, getInventory(), OperationUtils.getUserLocation()).doubleValue(), "QR").getQuantity().intValue());
        reportLineDTO.setDistributedQuantity(
                createProductAttributeOtherFlux(
                        product,
                        reportService().getProductQuantityDistributedInLastOperationByProduct(product, getInventory(), OperationUtils.getUserLocation()).doubleValue(),
                        "QD").getQuantity().intValue());
        reportLineDTO.setLostQuantity(createProductAttributeOtherFlux(product, reportService().getProductQuantityLostInLastOperationByProduct(product, getInventory(), OperationUtils.getUserLocation()).doubleValue(), "QL").getQuantity().intValue());
        reportLineDTO.setAdjustmentQuantity(createProductAttributeOtherFlux(product, reportService().getProductQuantityAdjustmentInLastOperationByProduct(product, getInventory(), OperationUtils.getUserLocation()).doubleValue(), "QA").getQuantity().intValue());
        reportLineDTO.setQuantityInStock(createProductAttributeOtherFlux(product, reportService().getProductQuantityInStockOperationByProduct(product, getInventory(), OperationUtils.getUserLocation()).doubleValue(), "SDU").getQuantity().intValue());
        reportLineDTO.setNumDaysOfRupture(createProductAttributeOtherFlux(product, 0.0, "NDR").getQuantity().intValue());
        if (OperationUtils.isDirectClient(OperationUtils.getUserLocation())) {
            reportLineDTO.setNumSitesInRupture(createProductAttributeOtherFlux(product, reportService().getChildLocationsThatKnownRupture(product, getInventory(), OperationUtils.getUserLocation()).doubleValue(), "NSR").getQuantity().intValue());
            reportLineDTO.setAverageMonthlyConsumption(
                    createProductAttributeOtherFlux(product,
                            reportService().getProductAverageMonthlyConsumption(product, getInventory().getProductProgram(), OperationUtils.getUserLocation(), false), "CMM").getQuantity());
            if (reportLineDTO.getDistributedQuantity() > 0 || reportLineDTO.getAverageMonthlyConsumption() > 0) {
                reportLineDTO.setQuantityToOrder(createProductAttributeOtherFlux(product, (OperationUtils.getUserLocationStockMax() * reportLineDTO.getAverageMonthlyConsumption()) - reportLineDTO.getQuantityInStock(), "QTO").getQuantity());
            }
            reportLineDTO.setCalculatedAverageMonthlyConsumption(reportService().getProductAverageMonthlyConsumption(product, getInventory().getProductProgram(), OperationUtils.getUserLocation(), false));
        }
        if (!OperationUtils.isDirectClient(OperationUtils.getUserLocation())) {
            reportLineDTO.setQuantityDistributed1monthAgo(createProductAttributeOtherFlux(product, reportService().getProductQuantityDistributedInAgo1MonthOperationByProduct(product, getInventory(), OperationUtils.getUserLocation()).doubleValue(), "DM1").getQuantity().intValue());
            reportLineDTO.setQuantityDistributed2monthAgo(createProductAttributeOtherFlux(product, reportService().getProductQuantityDistributedInAgo2MonthOperationByProduct(product, getInventory(), OperationUtils.getUserLocation()).doubleValue(), "DM2").getQuantity().intValue());
        }

        return reportLineDTO;
    }

    public List<ProductReportLineDTO> createProductReportOtherFluxMap() {
        List<ProductReportLineDTO> productListMap = new ArrayList<>();
        List<Product> products = reportService().getAllActivityProducts(getInventory());
        for (Product product : products) {
            productListMap.add(createProductReportOtherFluxes(product));
        }
        return productListMap;
    }

    private ProductAttributeOtherFlux createProductAttributeOtherFlux(Product product, Double quantity, String label) {
        ProductAttributeOtherFlux productAttributeOtherFlux = fluxService().getOneProductAttributeOtherFluxByProductAndOperationAndLabel(
                product,
                reportService().getOneProductReportById(getProductOperationId()),
                label,
                OperationUtils.getUserLocation()
        );
        if (productAttributeOtherFlux != null){
            return productAttributeOtherFlux;
//            if (!productAttributeOtherFlux.getQuantity().equals(quantity) && quantity > 0) {
//                productAttributeOtherFlux.setQuantity(quantity);
//                return fluxService().saveProductAttributeOtherFlux(productAttributeOtherFlux);
//            } else {
//                return productAttributeOtherFlux;
//            }
        } else {
            productAttributeOtherFlux = new ProductAttributeOtherFlux();
            productAttributeOtherFlux.setQuantity(quantity);
            productAttributeOtherFlux.setLabel(label);
            productAttributeOtherFlux.setLocation(OperationUtils.getUserLocation());
            productAttributeOtherFlux.setProduct(product);
            productAttributeOtherFlux.setProductOperation(reportService().getOneProductReportById(getProductOperationId()));
            return  fluxService().saveProductAttributeOtherFlux(productAttributeOtherFlux);
        }
    }

    private ProductReportService reportService() {
        return Context.getService(ProductReportService.class);
    }

    private ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private ProductAttributeService attributeService() {
        return Context.getService(ProductAttributeService.class);
    }

    private ProductInventoryService inventoryService() {
        return Context.getService(ProductInventoryService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }
}
