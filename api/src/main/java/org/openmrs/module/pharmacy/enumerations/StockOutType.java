package org.openmrs.module.pharmacy.enumerations;

public enum StockOutType {
    TRANSFER_OUT(4),
    OTHER_LOST(5),
    THIEF(6),
    NEGATIVE_INVENTORY_ADJUSTMENT(7),
    EXPIRED_PRODUCT(8),
    SPOILED_PRODUCT(9),
    DESTROYED(10);

    int type;
    StockOutType(int i) {
        type = i;
    }
}
