package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.ProductReport;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.api.ProductReportService;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportAttributeFluxForm extends ProductAttributeFluxForm {
    private Integer initialQuantity;
    private Integer receivedQuantity;
    private Integer distributedQuantity;
    private Integer lostQuantity;
    private Integer quantityAdjustment;
    private Integer quantityInStock;
    private Integer numDaysOfRupture;
    private Integer numSitesInRupture;
    private Integer quantityDistributed2monthAgo;
    private Integer quantityDistributed1monthAgo;

    public ReportAttributeFluxForm() {
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public Integer getDistributedQuantity() {
        return distributedQuantity;
    }

    public void setDistributedQuantity(Integer distributedQuantity) {
        this.distributedQuantity = distributedQuantity;
    }

    public Integer getLostQuantity() {
        return lostQuantity;
    }

    public void setLostQuantity(Integer lostQuantity) {
        this.lostQuantity = lostQuantity;
    }

    public Integer getQuantityAdjustment() {
        return quantityAdjustment;
    }

    public void setQuantityAdjustment(Integer quantityAdjustment) {
        this.quantityAdjustment = quantityAdjustment;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getNumDaysOfRupture() {
        return numDaysOfRupture;
    }

    public void setNumDaysOfRupture(Integer numDaysOfRupture) {
        this.numDaysOfRupture = numDaysOfRupture;
    }

    public Integer getNumSitesInRupture() {
        return numSitesInRupture;
    }

    public void setNumSitesInRupture(Integer numSitesInRupture) {
        this.numSitesInRupture = numSitesInRupture;
    }

    public Integer getQuantityDistributed2monthAgo() {
        return quantityDistributed2monthAgo;
    }

    public void setQuantityDistributed2monthAgo(Integer quantityDistributed2monthAgo) {
        this.quantityDistributed2monthAgo = quantityDistributed2monthAgo;
    }

    public Integer getQuantityDistributed1monthAgo() {
        return quantityDistributed1monthAgo;
    }

    public void setQuantityDistributed1monthAgo(Integer quantityDistributed1monthAgo) {
        this.quantityDistributed1monthAgo = quantityDistributed1monthAgo;
    }

    public void setProductAttributeFlux(ProductAttributeFlux flux, ProductReport productReport) {
//        if (productReport.getReportQuantityMode().equals(ReportQuantityMode.WHOLESALE)) {
//            Product product = flux.getProductAttribute().getProduct();
//            double fluxQuantity = flux.getQuantity() / product.getUnitConversion();
//            flux.setQuantity((int) fluxQuantity);
//        }
//        super.setProductAttributeFlux(flux, productReport);
//        if (productReport.getIncidence().equals(Incidence.POSITIVE)) {
//            ProductAttributeOtherFlux otherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(
//                    flux.getProductAttribute(),
//                    productReport,
//                    OperationUtils.getUserLocation());
//            if (productReport.getReportQuantityMode().equals(ReportQuantityMode.WHOLESALE)) {
//                Product product = flux.getProductAttribute().getProduct();
//                double fluxQuantity = otherFlux.getQuantity() / product.getUnitConversion();
//                setQuantityToDeliver((int) fluxQuantity);
//            } else {
//                setQuantityToDeliver(otherFlux.getQuantity());
//            }
//        }
    }

    public ProductAttributeOtherFlux getProductAttributeOtherFlux() {
        ProductAttributeOtherFlux productAttributeOtherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(
                attributeService().getOneProductAttributeByBatchNumber(getBatchNumber(), OperationUtils.getUserLocation()),
                reportService().getOneProductReportById(getProductOperationId()),
                OperationUtils.getUserLocation());
//        if (productAttributeOtherFlux != null){
//            productAttributeOtherFlux.setQuantity(getQuantityToDeliver());
//        } else {
//            productAttributeOtherFlux = new ProductAttributeOtherFlux();
//            productAttributeOtherFlux.setQuantity(getQuantityToDeliver());
//            productAttributeOtherFlux.setLabel("Quantitié livrée");
//            productAttributeOtherFlux.setLocation(Context.getLocationService().getDefaultLocation());
//            productAttributeOtherFlux.setProductAttribute(getProductAttribute());
//            productAttributeOtherFlux.setProductOperation(reportService().getOneProductReportById(getProductOperationId()));
//        }

        return null;
    }

    public List<ProductAttributeOtherFlux> createProductReportOtherFluxes(Product product) {
        List<ProductAttributeOtherFlux> otherFluxes = new ArrayList<>();

        otherFluxes.add(createProductAttributeOtherFlux(product, getInitialQuantity(), "SI"));
        otherFluxes.add(createProductAttributeOtherFlux(product, getReceivedQuantity(), "QR"));
        otherFluxes.add(createProductAttributeOtherFlux(product, getDistributedQuantity(), "QD"));
        otherFluxes.add(createProductAttributeOtherFlux(product, getLostQuantity(), "QP"));
        if (getQuantityAdjustment() != null)
            otherFluxes.add(createProductAttributeOtherFlux(product, getQuantityAdjustment(), "QA"));
        otherFluxes.add(createProductAttributeOtherFlux(product, getQuantityInStock(), "SDU"));
        otherFluxes.add(createProductAttributeOtherFlux(product, getNumDaysOfRupture(), "NDR"));
        otherFluxes.add(createProductAttributeOtherFlux(product, getNumSitesInRupture(), "NSR"));
        otherFluxes.add(createProductAttributeOtherFlux(product, getQuantityDistributed1monthAgo(), "DM1"));
        otherFluxes.add(createProductAttributeOtherFlux(product, getQuantityDistributed2monthAgo(), "DM2"));

        return otherFluxes;
    }

    public Map<Product, List<ProductAttributeOtherFlux>> createProductReportOtherFluxMap() {


        return null;
    }

    private ProductAttributeOtherFlux createProductAttributeOtherFlux(Product product, Integer quantity, String label) {
        ProductAttributeOtherFlux productAttributeOtherFlux = fluxService().getOneProductAttributeOtherFluxByProductAndOperation(
                product,
                reportService().getOneProductReportById(getProductOperationId())
        );
        if (productAttributeOtherFlux != null){
            productAttributeOtherFlux.setQuantity(quantity);
        } else {
            productAttributeOtherFlux = new ProductAttributeOtherFlux();
            productAttributeOtherFlux.setQuantity(quantity);
            productAttributeOtherFlux.setLabel(label);
            productAttributeOtherFlux.setLocation(Context.getLocationService().getDefaultLocation());
            productAttributeOtherFlux.setProductAttribute(getProductAttribute());
            productAttributeOtherFlux.setProductOperation(reportService().getOneProductReportById(getProductOperationId()));
        }
        return  productAttributeOtherFlux;
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
}
