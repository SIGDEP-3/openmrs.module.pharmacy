package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.ArrayList;
import java.util.Collection;
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

//
//    public Integer getLocationId() {
//        return locationId;
//    }
//
//    public void setLocationId(Integer locationId) {
//        this.locationId = locationId;
//    }
//
//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }

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
            List<ProductAttributeStock> productAttributeStocks = stockService().getAllProductAttributeStockByProduct(productService().getOneProductById(productId), OperationUtils.getUserLocation());
            Integer quantity = getDispensingQuantity();
            int countFlux = 0;
            int countOldFlux = dispensationService().getOneProductDispensationById(getProductOperationId()).getProductAttributeFluxes().size();
            for (ProductAttributeStock stock : productAttributeStocks) {
                countFlux ++;
                ProductAttributeFlux productAttributeFlux = fluxService().getOneProductAttributeFluxByAttributeAndOperation(
                        stock.getProductAttribute(),
                        dispensationService().getOneProductDispensationById(getProductOperationId())
                );
                if (productAttributeFlux == null) {
                    productAttributeFlux = new ProductAttributeFlux();
//                    productAttributeFlux.setQuantity(getDispensingQuantity());
                    productAttributeFlux.setLocation(OperationUtils.getUserLocation());
                    productAttributeFlux.setProductAttribute(stock.getProductAttribute());
                    productAttributeFlux.setOperationDate(dispensationService().getOneProductDispensationById(getProductOperationId()).getOperationDate());
                    productAttributeFlux.setProductOperation(dispensationService().getOneProductDispensationById(getProductOperationId()));
                }

                if (quantity <= stock.getQuantityInStock()){
                    productAttributeFlux.setQuantity(quantity);
                    fluxes.add(productAttributeFlux);
                    break;
                } else {
                    productAttributeFlux.setQuantity(stock.getQuantityInStock());
                    quantity -= stock.getQuantityInStock();
                    fluxes.add(productAttributeFlux);
                    if (quantity.equals(0)) {
                        break;
                    }
                }
            }
            if (countOldFlux > countFlux) {
                int remainFluxesCount = countOldFlux - countFlux;
                List<ProductAttributeFlux> attributeFluxes =
                        OperationUtils.getLastElements(
                                dispensationService().getOneProductDispensationById(
                                        getProductOperationId()).getProductAttributeFluxes(),
                                remainFluxesCount
                        );
                for (ProductAttributeFlux flux : attributeFluxes) {
                    flux.setQuantity(0);
                    fluxes.add(flux);
                }
            }
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

    private ProductAttributeService attributeService() {
        return Context.getService(ProductAttributeService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }

    private ProductService productService() {
        return Context.getService(ProductService.class);
    }
}
