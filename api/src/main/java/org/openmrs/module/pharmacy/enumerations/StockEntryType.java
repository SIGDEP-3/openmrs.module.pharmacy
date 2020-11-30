package org.openmrs.module.pharmacy.enumerations;

public enum  StockEntryType {
    SITE_PRODUCT_BACK(0),
    TRANSFER_IN(1),
    DONATION(2),
    POSITIVE_INVENTORY_ADJUSTMENT(3);

    int type;
    StockEntryType(int i) {
        type = i;
    }
}
