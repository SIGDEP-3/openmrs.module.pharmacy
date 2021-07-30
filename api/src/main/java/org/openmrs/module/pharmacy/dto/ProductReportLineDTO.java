package org.openmrs.module.pharmacy.dto;

import java.io.Serializable;

public class ProductReportLineDTO implements Serializable, Comparable<ProductReportLineDTO> {
    protected Integer productId;
    protected String code;
    protected String retailName;
    protected String retailUnit;
    protected Integer initialQuantity;
    protected Integer receivedQuantity;
    protected Integer distributedQuantity;
    protected Integer lostQuantity;
    protected Integer adjustmentQuantity;
    protected Integer quantityInStock;
    protected Integer numDaysOfRupture;
    protected Integer numSitesInRupture;
    protected Integer quantityDistributed2monthAgo;
    protected Integer quantityDistributed1monthAgo;
    protected Double averageMonthlyConsumption;
    protected Double calculatedAverageMonthlyConsumption;
    protected Double proposedQuantity;
    protected Boolean asserted;

    public ProductReportLineDTO() {
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

    public Double getProposedQuantity(){
        return this.proposedQuantity;
    }

    public void setAsserted(Boolean asserted) {
        this.asserted = asserted;
    }

    public Boolean getAsserted() {
        Integer sd = getInitialQuantity() + getReceivedQuantity() -
                (getLostQuantity() + getDistributedQuantity()) +
                (getAdjustmentQuantity());
        this.asserted = getQuantityInStock().equals(sd);
        return this.asserted;
    }

    public void setProposedQuantity(Double quantity) {
        this.proposedQuantity = quantity;
    }

    @Override
    public int compareTo(ProductReportLineDTO o) {
        if (getRetailName() == null || o.getRetailName() == null) {
            return 0;
        } else {
            return getRetailName().compareTo(o.getRetailName());
        }
    }
}
