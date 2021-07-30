package org.openmrs.module.pharmacy.forms.reports;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.entities.Product;
import org.openmrs.module.pharmacy.entities.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.entities.ProductInventory;
import org.openmrs.module.pharmacy.entities.ProductReport;
import org.openmrs.module.pharmacy.enumerations.ReportType;
import org.openmrs.module.pharmacy.forms.ProductAttributeFluxForm;
import org.openmrs.module.pharmacy.dto.ProductReportLineDTO;
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
        ProductReport report = reportService().getOneProductReportById(getProductOperationId());

        ProductReportLineDTO reportLineDTO = new ProductReportLineDTO();
        reportLineDTO.setCode(product.getCode());
        reportLineDTO.setRetailName(product.getRetailName());
        reportLineDTO.setRetailUnit(product.getProductRetailUnit().getName());

//        System.out.println("-----------------------------> Product information collected ");


        reportLineDTO.setInitialQuantity(
                createProductAttributeOtherFlux(
                        product,
                        reportService().getProductQuantityInStockInLastOperationByProduct(
                                product,
                                getInventory(),
                                OperationUtils.getUserLocation(),
                                report.getUrgent()
                        ).doubleValue(),
                        "SI"
                ).getQuantity().intValue());

        reportLineDTO.setReceivedQuantity(
                createProductAttributeOtherFlux(
                        product,
                        reportService().getProductReceivedQuantityInLastOperationByProduct(
                                product,
                                getInventory(),
                                OperationUtils.getUserLocation(),
                                report.getUrgent()
                        ).doubleValue(),
                        "QR"
                ).getQuantity().intValue());

        reportLineDTO.setDistributedQuantity(
                createProductAttributeOtherFlux(
                        product,
                        reportService().getProductQuantityDistributedInLastOperationByProduct(
                                product,
                                getInventory(),
                                OperationUtils.getUserLocation(),
                                report.getUrgent()
                        ).doubleValue(),
                        "QD"
                ).getQuantity().intValue());
        reportLineDTO.setLostQuantity(
                createProductAttributeOtherFlux(
                        product,
                        reportService().getProductQuantityLostInLastOperationByProduct(
                                product,
                                getInventory(),
                                OperationUtils.getUserLocation(),
                                report.getUrgent()
                        ).doubleValue(),
                        "QL"
                ).getQuantity().intValue());
        reportLineDTO.setAdjustmentQuantity(
                createProductAttributeOtherFlux(
                        product,
                        reportService().getProductQuantityAdjustmentInLastOperationByProduct(
                                product,
                                getInventory(),
                                OperationUtils.getUserLocation(),
                                report.getUrgent()
                        ).doubleValue(),
                        "QA"
                ).getQuantity().intValue());

        reportLineDTO.setQuantityInStock(
                createProductAttributeOtherFlux(
                        product,
                        reportService().getProductQuantityInStockOperationByProduct(
                                product,
                                getInventory(),
                                OperationUtils.getUserLocation(),
                                report.getUrgent()
                        ).doubleValue(),
                        "SDU"
                ).getQuantity().intValue());

        reportLineDTO.setNumDaysOfRupture(
                createProductAttributeOtherFlux(
                        product,
                        0.0,
                        "NDR"
                ).getQuantity().intValue());

//        System.out.println("-----------------------------> In old ProductAttributeOtherFlux : null ");

        if (report.getReportType().equals(ReportType.CLIENT_REPORT)) {
            reportLineDTO.setNumSitesInRupture(
                    createProductAttributeOtherFlux(
                            product,
                            reportService().getChildLocationsThatKnownRupture(
                                    product,
                                    getInventory(),
                                    OperationUtils.getUserLocation()
                            ).doubleValue(),
                            "NSR"
                    ).getQuantity().intValue());

            reportLineDTO.setAverageMonthlyConsumption(
                    createProductAttributeOtherFlux(
                            product,
                            reportService().getProductAverageMonthlyConsumption(
                                    product,
                                    getInventory().getProductProgram(),
                                    OperationUtils.getUserLocation(),
                                    false),
                            "CMM"
                    ).getQuantity());

            if (reportLineDTO.getDistributedQuantity() > 0 || reportLineDTO.getAverageMonthlyConsumption() > 0) {
                reportLineDTO.setProposedQuantity(
                        createProductAttributeOtherFlux(
                                product,
                                (OperationUtils.getUserLocationStockMax() * reportLineDTO.getAverageMonthlyConsumption()) - reportLineDTO.getQuantityInStock(),
                                "QTO"
                        ).getQuantity());
            }
            reportLineDTO.setCalculatedAverageMonthlyConsumption(
                    reportService().getProductAverageMonthlyConsumption(
                            product,
                            getInventory().getProductProgram(),
                            OperationUtils.getUserLocation(),
                            false));
        } else {
            reportLineDTO.setQuantityDistributed1monthAgo(
                    createProductAttributeOtherFlux(
                            product,
                            reportService().getProductQuantityDistributedInAgo1MonthOperationByProduct(
                                    product,
                                    getInventory(),
                                    OperationUtils.getUserLocation()).doubleValue(),
                            "DM1"
                    ).getQuantity().intValue());

            reportLineDTO.setQuantityDistributed2monthAgo(
                    createProductAttributeOtherFlux(
                            product,
                            reportService().getProductQuantityDistributedInAgo2MonthOperationByProduct(
                                    product,
                                    getInventory(),
                                    OperationUtils.getUserLocation()
                            ).doubleValue(),
                            "DM2"
                    ).getQuantity().intValue());
        }
        System.out.println("-----------------------------> All Product quantity information collected and created ");
        return reportLineDTO;
    }

    public List<ProductReportLineDTO> createProductReportOtherFluxMap() {
        List<ProductReportLineDTO> productListMap = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        ProductReport report = reportService().getOneProductReportById(getProductOperationId());
        if (report.getUrgent()) {
            for (String code : report.getReportInfo().split(",")) {
                products.add(productService().getOneProductByCode(code));
            }
        } else {
            System.out.println("-----------------------------> In Client product collection ");

            products = reportService().getAllActivityProducts(getInventory());
            for (Location location : OperationUtils.getUserLocation().getChildLocations()) {
                ProductReport childReport = reportService().getOneProductReportByReportPeriodAndProgram(
                        report.getReportPeriod(),
                        report.getProductProgram(),
                        location, false
                );
                if (childReport != null) {
                    List<Product> tmpProducts = childReport.getOtherFluxesProductList();
                    for (Product product : tmpProducts) {
                        if (!products.contains(product)) {
                            products.add(product);
                        }
                    }
                }
            }
        }
        for (Product product : products) {
            System.out.println("-----------------------------> In creating ProductAttributeOtherFluxes Function ");
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
//            System.out.println("-----------------------------> In old ProductAttributeOtherFlux : " + productAttributeOtherFlux.getProductAttributeOtherFluxId());
            return productAttributeOtherFlux;
        } else {
            productAttributeOtherFlux = new ProductAttributeOtherFlux();
            productAttributeOtherFlux.setQuantity(quantity);
            productAttributeOtherFlux.setLabel(label);
            productAttributeOtherFlux.setLocation(OperationUtils.getUserLocation());
            productAttributeOtherFlux.setProduct(product);
            productAttributeOtherFlux.setProductOperation(reportService().getOneProductReportById(getProductOperationId()));
//            System.out.println("-----------------------------> In old ProductAttributeOtherFlux : null ");

            return  fluxService().saveProductAttributeOtherFlux(productAttributeOtherFlux);
        }
    }

    private ProductReportService reportService() {
        return Context.getService(ProductReportService.class);
    }

    private ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private ProductService productService() {
        return Context.getService(ProductService.class);
    }

    private ProductInventoryService inventoryService() {
        return Context.getService(ProductInventoryService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }
}
