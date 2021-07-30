package org.openmrs.module.pharmacy.forms.dispensation;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.entities.*;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.ArrayList;
import java.util.List;

public class DispensationAttributeFluxForm {
    private Integer productId;
    private Integer selectedProductId;
    private Integer productOperationId;
    private Integer dispensingQuantity;
    private Integer requestedQuantity;

    public DispensationAttributeFluxForm() {
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSelectedProductId() {
        return selectedProductId;
    }

    public void setSelectedProductId(Integer selectedProductId) {
        this.selectedProductId = selectedProductId;
    }

    public Integer getProductOperationId() {
        return productOperationId;
    }

    public void setProductOperationId(Integer productOperationId) {
        this.productOperationId = productOperationId;
    }


    public Integer getDispensingQuantity() {
        return dispensingQuantity;
    }

    public void setDispensingQuantity(Integer dispensingQuantity) {
        this.dispensingQuantity = dispensingQuantity;
    }

    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(Integer requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public void setProductAttributeFlux(ProductAttributeFlux productAttributeFlux, ProductDispensation productDispensation) {
//        super.setProductAttributeFlux(productAttributeFlux, productDispensation);
//        ProductAttributeOtherFlux otherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(productAttributeFlux.getProductAttribute(), productDispensation, OperationUtils.getUserLocation());
//        setDispensingQuantity(otherFlux.getQuantity());
    }

    public List<ProductAttributeFlux> getProductAttributeFluxes() {
        List<ProductAttributeFlux> fluxes = new ArrayList<ProductAttributeFlux>();
        if (getDispensingQuantity()!= null) {
            Integer productId = getProductId() == null ? getSelectedProductId() : getProductId();
            fluxes = OperationUtils.createProductAttributeFluxes(
                    productService().getOneProductById(productId),
                    dispensationService().getOneProductDispensationById(getProductOperationId()),
                    getDispensingQuantity()
            );
        }

        return fluxes;
    }

    public List<ProductAttributeOtherFlux> getProductAttributeOtherFluxes() {
        List<ProductAttributeOtherFlux> otherFluxes = new ArrayList<ProductAttributeOtherFlux>();
        if (getRequestedQuantity()!= null) {
            Integer productId = getProductId() == null ? getSelectedProductId() : getProductId();
            List<ProductAttributeStock> productAttributeStocks = stockService().getAllProductAttributeStockByProduct(productService().getOneProductById(productId), OperationUtils.getUserLocation());
            Double quantity = getRequestedQuantity().doubleValue();
            for (ProductAttributeStock stock : productAttributeStocks) {
                ProductAttributeOtherFlux productAttributeOtherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(
                        stock.getProductAttribute(),
                        dispensationService().getOneProductDispensationById(getProductOperationId()),
                        OperationUtils.getUserLocation()
                );
                if (productAttributeOtherFlux != null){
                    productAttributeOtherFlux.setQuantity(getRequestedQuantity().doubleValue());
                } else {
                    productAttributeOtherFlux = new ProductAttributeOtherFlux();
                    productAttributeOtherFlux.setQuantity(getDispensingQuantity().doubleValue());
                    productAttributeOtherFlux.setLabel("Quantitié demandée");
                    productAttributeOtherFlux.setLocation(OperationUtils.getUserLocation());
                    productAttributeOtherFlux.setProductAttribute(stock.getProductAttribute());
                    productAttributeOtherFlux.setProductOperation(dispensationService().getOneProductDispensationById(getProductOperationId()));
                }

                if (getRequestedQuantity() <= stock.getQuantityInStock()){
                    productAttributeOtherFlux.setQuantity(quantity);
                    otherFluxes.add(productAttributeOtherFlux);
                    break;
                } else {
                    productAttributeOtherFlux.setQuantity(stock.getQuantityInStock().doubleValue());
                    quantity -= stock.getQuantityInStock();
                    otherFluxes.add(productAttributeOtherFlux);
                    if (quantity.equals(0)) {
                        break;
                    }
                }
            }
        }

        return otherFluxes;
    }

    public ProductAttributeOtherFlux getProductAttributeOtherFlux() {
        Integer productId = getProductId() == null ? getSelectedProductId() : getProductId();
        ProductAttributeOtherFlux otherFlux = fluxService().getOneProductAttributeOtherFluxByProductAndOperation(
                productService().getOneProductById(productId),
                dispensationService().getOneProductDispensationById(getProductOperationId())
        );
        if (otherFlux != null){
            otherFlux.setQuantity(getRequestedQuantity().doubleValue());
        } else {
            otherFlux = new ProductAttributeOtherFlux();
            otherFlux.setQuantity(getDispensingQuantity().doubleValue());
            otherFlux.setLabel("Quantitié demandée");
            otherFlux.setLocation(OperationUtils.getUserLocation());
            otherFlux.setProduct(productService().getOneProductById(productId));
            otherFlux.setProductOperation(dispensationService().getOneProductDispensationById(getProductOperationId()));
        }
        return otherFlux;
    }

    public void setProductAttributeFlux(ProductDispensation productDispensation, Product product) {
        setProductId(product.getProductId());
        setProductOperationId(productDispensation.getProductOperationId());
        setDispensingQuantity(fluxService().getAllProductAttributeFluxByOperationAndProductCount(productDispensation, product));
        setRequestedQuantity(fluxService().getOneProductAttributeOtherFluxByProductAndOperation(product, productDispensation).getQuantity().intValue());
    }

    private ProductDispensationService dispensationService() {
        return Context.getService(ProductDispensationService.class);
    }

    private ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }

    private ProductService productService() {
        return Context.getService(ProductService.class);
    }
}
