package org.openmrs.module.pharmacy.dto;

public class ConsumingReportDTO {
    String productCode;
    String productRetailName;
    Integer initialQuantity;
    Integer receivedQuantity;
    Integer consumedQuantity;
    Integer lostQuantity;
    Integer adjustedQuantity;
    Integer quantityInStock;
    Integer numberDaysOutOfStock;
    Integer numberSitesOutOfStock;
    Double CMM;
    Double MSD;
    Double quantityToOrder;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductRetailName() {
        return productRetailName;
    }

    public void setProductRetailName(String productRetailName) {
        this.productRetailName = productRetailName;
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

    public Integer getConsumedQuantity() {
        return consumedQuantity;
    }

    public void setConsumedQuantity(Integer consumedQuantity) {
        this.consumedQuantity = consumedQuantity;
    }

    public Integer getLostQuantity() {
        return lostQuantity;
    }

    public void setLostQuantity(Integer lostQuantity) {
        this.lostQuantity = lostQuantity;
    }

    public Integer getAdjustedQuantity() {
        return adjustedQuantity;
    }

    public void setAdjustedQuantity(Integer adjustedQuantity) {
        this.adjustedQuantity = adjustedQuantity;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getNumberDaysOutOfStock() {
        return numberDaysOutOfStock;
    }

    public void setNumberDaysOutOfStock(Integer numberDaysOutOfStock) {
        this.numberDaysOutOfStock = numberDaysOutOfStock;
    }

    public Integer getNumberSitesOutOfStock() {
        return numberSitesOutOfStock;
    }

    public void setNumberSitesOutOfStock(Integer numberSitesOutOfStock) {
        this.numberSitesOutOfStock = numberSitesOutOfStock;
    }

    public Double getCMM() {
        return CMM;
    }

    public void setCMM(Double CMM) {
        this.CMM = CMM;
    }

    public Double getMSD() {
        return MSD;
    }

    public void setMSD(Double MSD) {
        this.MSD = MSD;
    }

    public Double getQuantityToOrder() {
        return quantityToOrder;
    }

    public void setQuantityToOrder(Double quantityToOrder) {
        this.quantityToOrder = quantityToOrder;
    }
}
