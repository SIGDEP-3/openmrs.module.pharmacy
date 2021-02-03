package org.openmrs.module.pharmacy.models;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductReport;
import org.openmrs.module.pharmacy.api.ProductReportService;
import org.openmrs.module.pharmacy.utils.OperationUtils;

public class ProductReportLineExtendedDTO extends ProductReportLineDTO {
    private Integer parentQuantityInStock;
    private Integer accordedQuantity;

    public ProductReportLineExtendedDTO() {}

    public Integer getParentQuantityInStock() {
        return parentQuantityInStock;
    }

    public void setParentQuantityInStock(Integer parentQuantityInStock) {
        this.parentQuantityInStock = parentQuantityInStock;
    }

    public Integer getAccordedQuantity() {
        return accordedQuantity;
    }

    public void setAccordedQuantity(Integer accordedQuantity) {
        this.accordedQuantity = accordedQuantity;
    }

    public void getProductLineDto(ProductReportLineDTO lineDTO) {
        this.setCode(lineDTO.getCode());
        this.setRetailName(lineDTO.getRetailName());
        this.setRetailUnit(lineDTO.getRetailUnit());
        this.setInitialQuantity(lineDTO.getInitialQuantity());

        this.setReceivedQuantity(lineDTO.getReceivedQuantity());
        this.setDistributedQuantity(lineDTO.getDistributedQuantity());
        this.setLostQuantity(lineDTO.getLostQuantity());
        this.setAdjustmentQuantity(lineDTO.getAdjustmentQuantity());
        this.setQuantityInStock(lineDTO.getQuantityInStock());
        this.setNumDaysOfRupture(lineDTO.getNumDaysOfRupture());
        this.setQuantityDistributed1monthAgo(lineDTO.getQuantityDistributed1monthAgo());
        this.setQuantityDistributed2monthAgo(lineDTO.getQuantityDistributed2monthAgo());
    }

    public Double getMonthOfStockAvailable() {
        return this.quantityInStock / this.calculatedAverageMonthlyConsumption;
    }
}
