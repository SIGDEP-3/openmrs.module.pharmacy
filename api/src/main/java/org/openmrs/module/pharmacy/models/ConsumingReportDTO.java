package org.openmrs.module.pharmacy.models;

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
}
