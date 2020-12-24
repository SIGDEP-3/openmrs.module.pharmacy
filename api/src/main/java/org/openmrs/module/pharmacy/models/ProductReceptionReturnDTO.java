package org.openmrs.module.pharmacy.models;

import java.io.Serializable;
import java.util.Date;

public class ProductReceptionReturnDTO implements Serializable {
    private Integer productAttributeFluxId;
    private Integer productOperationId;
    private Integer productId;
    private String code;
    private String retailName;
    private String wholesaleName;
    private String retailUnit;
    private String wholesaleUnit;
    private Double unitConversion;
    private String batchNumber;
    private Date expiryDate;
    private Integer quantityReceived;
    private Integer quantityInStock;
    private Integer quantityToReturn;
    private String observation;
    private Date dateCreated;

    public ProductReceptionReturnDTO() {
    }

    public Integer getProductAttributeFluxId() {
        return productAttributeFluxId;
    }

    public void setProductAttributeFluxId(Integer productAttributeFluxId) {
        this.productAttributeFluxId = productAttributeFluxId;
    }

    public Integer getProductOperationId() {
        return productOperationId;
    }

    public void setProductOperationId(Integer productOperationId) {
        this.productOperationId = productOperationId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRetailName() {
        return retailName;
    }

    public void setRetailName(String retailName) {
        this.retailName = retailName;
    }

    public String getWholesaleName() {
        return wholesaleName;
    }

    public void setWholesaleName(String wholesaleName) {
        this.wholesaleName = wholesaleName;
    }

    public String getRetailUnit() {
        return retailUnit;
    }

    public void setRetailUnit(String retailUnit) {
        this.retailUnit = retailUnit;
    }

    public String getWholesaleUnit() {
        return wholesaleUnit;
    }

    public void setWholesaleUnit(String wholesaleUnit) {
        this.wholesaleUnit = wholesaleUnit;
    }

    public Double getUnitConversion() {
        return unitConversion;
    }

    public void setUnitConversion(Double unitConversion) {
        this.unitConversion = unitConversion;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public Integer getQuantityToReturn() {
        return quantityToReturn;
    }

    public void setQuantityToReturn(Integer quantityToReturn) {
        this.quantityToReturn = quantityToReturn;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
