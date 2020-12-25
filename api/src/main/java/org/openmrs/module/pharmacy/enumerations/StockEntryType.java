package org.openmrs.module.pharmacy.enumerations;

public enum  StockEntryType {
//    SITE_PRODUCT_BACK(0),
    TRANSFER_IN(0),
    DONATION(1),
    POSITIVE_INVENTORY_ADJUSTMENT(2);

    int type;

    StockEntryType(int i) {
        type = i;
    }
}
