package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductAttribute;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;

import java.util.Date;

public class ReceptionAttributeFluxForm {
    private Integer productAttributeFluxId;
    private Integer productId;
    private String batchNumber;
    private Integer productOperationId;
    private Integer receivedQuantity;
    private Integer receptionQuantity;
    private Date expiryDate;
    private String observation;
    private Integer locationId;
    private String uuid;

    public ReceptionAttributeFluxForm() {
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

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public Integer getReceptionQuantity() {
        return receptionQuantity;
    }

    public void setReceptionQuantity(Integer receptionQuantity) {
        this.receptionQuantity = receptionQuantity;
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
        ProductAttribute productAttribute = Context.getService(PharmacyService.class).getOneProductAttributeByBatchNumber(getBatchNumber());
        if (productAttribute == null) {
            productAttribute = new ProductAttribute();
        }
        productAttribute.setExpiryDate(getExpiryDate());
        productAttribute.setBatchNumber(getBatchNumber());
        productAttribute.setExpiryDate(getExpiryDate());
        productAttribute.setProduct(Context.getService(PharmacyService.class).getOneProductById(getProductId()));
        productAttribute.setLocation(Context.getLocationService().getDefaultLocation());
        productAttribute.setProduct(Context.getService(PharmacyService.class).getOneProductById(getProductId()));
        return productAttribute;
    }

    public ProductAttributeFlux getProductAttributeFlux(ProductAttribute productAttribute) {
        ProductAttributeFlux productAttributeFlux = new ProductAttributeFlux();
        if (getProductAttributeFluxId() != null) {
            productAttributeFlux = Context.getService(PharmacyService.class).getOneProductAttributeFluxById(getProductAttributeFluxId());
        }
        productAttributeFlux.setProductAttribute(productAttribute);
        productAttributeFlux.setQuantity(getReceivedQuantity());
        productAttributeFlux.setStatus(OperationStatus.NOT_COMPLETED);
        productAttributeFlux.setObservation(getObservation());
        productAttributeFlux.setProductOperation(Context.getService(PharmacyService.class).getOneProductReceptionById(getProductOperationId()));
        productAttributeFlux.setLocation(Context.getLocationService().getDefaultLocation());
        productAttributeFlux.setOperationDate(Context.getService(PharmacyService.class)
                .getOneProductReceptionById(getProductOperationId()).getOperationDate());
        return productAttributeFlux;
    }

    public void setProductAttributeFlux(ProductAttributeFlux productAttributeFlux, ProductReception productReception) {
        setProductAttributeFluxId(productAttributeFlux.getProductAttributeFluxId());
        setBatchNumber(productAttributeFlux.getProductAttribute().getBatchNumber());
        setReceivedQuantity(productAttributeFlux.getQuantity());
        setExpiryDate(productAttributeFlux.getProductAttribute().getExpiryDate());
        setProductOperationId(productAttributeFlux.getProductAttribute().getProductAttributeId());
        setProductId(productAttributeFlux.getProductAttribute().getProduct().getProductId());
        setObservation(productAttributeFlux.getObservation());
        setProductOperationId(productReception.getProductOperationId());
        setLocationId(productAttributeFlux.getLocation().getLocationId());
    }

    public ProductAttributeOtherFlux getProductAttributeOtherFlux() {
        ProductAttributeOtherFlux productAttributeOtherFlux = Context.getService(PharmacyService.class).getOneProductAttributeOtherFluxByAttributeAndOperation(
                Context.getService(PharmacyService.class).getOneProductAttributeByBatchNumber(getBatchNumber()),
                Context.getService(PharmacyService.class).getOneProductReceptionById(getProductOperationId())
        );
        if (productAttributeOtherFlux != null){
            productAttributeOtherFlux.setQuantity(getReceptionQuantity());
        } else {
            productAttributeOtherFlux = new ProductAttributeOtherFlux();
            productAttributeOtherFlux.setQuantity(getReceptionQuantity());
            productAttributeOtherFlux.setLabel("Quantitié livrée");
            productAttributeOtherFlux.setLocation(Context.getLocationService().getDefaultLocation());
            productAttributeOtherFlux.setProductAttribute(getProductAttribute());
            productAttributeOtherFlux.setProductOperation(Context.getService(PharmacyService.class).getOneProductReceptionById(getProductOperationId()));
        }

        return productAttributeOtherFlux;
    }
}
