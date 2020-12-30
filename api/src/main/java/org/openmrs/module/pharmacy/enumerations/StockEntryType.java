package org.openmrs.module.pharmacy.enumerations;

public enum  StockEntryType {
    TRANSFER_IN(0),
    DONATION(1),
    POSITIVE_INVENTORY_ADJUSTMENT(2),
    BACK_FROM_SITE(3);

    int type;

    StockEntryType(int i) {
        type = i;
    }
}
