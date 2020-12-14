package org.openmrs.module.pharmacy.models;

import java.io.Serializable;
import java.util.Date;

public class ProductDispensationFluxDTO implements Serializable {
    private Integer productId;
    private String code;
    private String retailName;
    private String retailUnit;
    private Integer requestedQuantity;
    private Integer dispensingQuantity;
    private Integer quantityInStock;
    private Date dateCreated;

    public ProductDispensationFluxDTO() {
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

    public String getRetailUnit() {
        return retailUnit;
    }

    public void setRetailUnit(String retailUnit) {
        this.retailUnit = retailUnit;
    }

    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(Integer requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public Integer getDispensingQuantity() {
        return dispensingQuantity;
    }

    public void setDispensingQuantity(Integer dispensingQuantity) {
        this.dispensingQuantity = dispensingQuantity;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
