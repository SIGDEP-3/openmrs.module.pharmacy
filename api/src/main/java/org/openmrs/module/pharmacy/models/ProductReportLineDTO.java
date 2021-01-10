package org.openmrs.module.pharmacy.models;

import org.openmrs.module.pharmacy.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.utils.OperationUtils;

public class ProductReportLineDTO {
    private String code;
    private String retailName;
    private String retailUnit;
    private Integer initialQuantity;
    private Integer receivedQuantity;
    private Integer distributedQuantity;
    private Integer lostQuantity;
    private Integer adjustmentQuantity;
    private Integer quantityInStock;
    private Integer numDaysOfRupture;
    private Integer numSitesInRupture;
    private Integer quantityDistributed2monthAgo;
    private Integer quantityDistributed1monthAgo;
    private Double averageMonthlyConsumption;
    private Double calculatedAverageMonthlyConsumption;
    private Double quantityToOrder;

    public ProductReportLineDTO() {
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

    public Integer getAdjustmentQuantity() {
        return adjustmentQuantity;
    }

    public void setAdjustmentQuantity(Integer adjustmentQuantity) {
        this.adjustmentQuantity = adjustmentQuantity;
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

    public Double getAverageMonthlyConsumption() {
        return averageMonthlyConsumption;
    }

    public void setAverageMonthlyConsumption(Double averageMonthlyConsumption) {
        this.averageMonthlyConsumption = averageMonthlyConsumption;
    }

    public Double getCalculatedAverageMonthlyConsumption() {
        return calculatedAverageMonthlyConsumption;
    }

    public void setCalculatedAverageMonthlyConsumption(Double calculatedAverageMonthlyConsumption) {
        this.calculatedAverageMonthlyConsumption = calculatedAverageMonthlyConsumption;
    }

    public Double getMonthOfStockAvailable() {
        return getQuantityInStock() / getAverageMonthlyConsumption();
    }

    public Double getQuantityToOrder(){
        return this.quantityToOrder;
    }

    public Boolean getAsserted() {
        Integer sd = getInitialQuantity() + getReceivedQuantity() -
                (getLostQuantity() - getDistributedQuantity()) +
                (getAdjustmentQuantity());
        return getQuantityInStock().equals(sd);
    }

    public void setQuantityToOrder(Double quantity) {
        this.quantityToOrder = quantity;
    }
}
