package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.Date;

public abstract class ProductAttributeFluxForm {
    private Integer productAttributeFluxId;
    private Integer productId;
    private String batchNumber;
    private Integer productOperationId;
    private Integer quantity;
    private Date expiryDate;
    private String observation;
    private Integer locationId;
    private String uuid;

    public ProductAttributeFluxForm() {
        setLocationId(OperationUtils.getUserLocation().getLocationId());
    }

    public Integer getProductAttributeFluxId() {
        return productAttributeFluxId;
    }

    public void setProductAttributeFluxId(Integer productAttributeFluxId) {
        this.productAttributeFluxId = productAttributeFluxId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getProductOperationId() {
        return productOperationId;
    }

    public void setProductOperationId(Integer productOperationId) {
        this.productOperationId = productOperationId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ProductAttribute getProductAttribute() {
        ProductAttribute productAttribute = attributeService().getOneProductAttributeByBatchNumber(getBatchNumber());
        if (productAttribute == null) {
            productAttribute = new ProductAttribute();
        }
        productAttribute.setExpiryDate(getExpiryDate());
        productAttribute.setBatchNumber(getBatchNumber());
        productAttribute.setExpiryDate(getExpiryDate());
        productAttribute.setProduct(productService().getOneProductById(getProductId()));
        productAttribute.setLocation(OperationUtils.getUserLocation());
        return productAttribute;
    }

    public ProductAttributeFlux getProductAttributeFlux(ProductAttribute productAttribute) {
        ProductAttributeFlux productAttributeFlux = new ProductAttributeFlux();
        if (getProductAttributeFluxId() != null) {
            productAttributeFlux = fluxService().getOneProductAttributeFluxById(getProductAttributeFluxId());
        }
        productAttributeFlux.setProductAttribute(productAttribute);
        productAttributeFlux.setQuantity(getQuantity());
        productAttributeFlux.setStatus(OperationStatus.NOT_COMPLETED);
        productAttributeFlux.setObservation(getObservation());
        productAttributeFlux.setProductOperation(service().getOneProductOperationById(getProductOperationId()));
        productAttributeFlux.setLocation(OperationUtils.getUserLocation());
        productAttributeFlux.setOperationDate(service()
                .getOneProductOperationById(getProductOperationId()).getOperationDate());
        return productAttributeFlux;
    }

    public void setProductAttributeFlux(ProductAttributeFlux productAttributeFlux, ProductOperation productOperation) {
        setProductAttributeFluxId(productAttributeFlux.getProductAttributeFluxId());
        setBatchNumber(productAttributeFlux.getProductAttribute().getBatchNumber());
        setQuantity(productAttributeFlux.getQuantity());
        setExpiryDate(productAttributeFlux.getProductAttribute().getExpiryDate());
        setProductOperationId(productAttributeFlux.getProductAttribute().getProductAttributeId());
        setProductId(productAttributeFlux.getProductAttribute().getProduct().getProductId());
        setObservation(productAttributeFlux.getObservation());
        setProductOperationId(productOperation.getProductOperationId());
        setLocationId(productAttributeFlux.getLocation().getLocationId());
    }

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private ProductAttributeService attributeService() {
        return Context.getService(ProductAttributeService.class);
    }

    private ProductService productService() {
        return Context.getService(ProductService.class);
    }

}
