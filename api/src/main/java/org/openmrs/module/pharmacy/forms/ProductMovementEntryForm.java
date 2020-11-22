package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductExchangeEntity;
import org.openmrs.module.pharmacy.ProductMovementEntry;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.ReceptionQuantityMode;
import org.openmrs.module.pharmacy.enumerations.StockEntryType;

public class ProductMovementEntryForm extends ProductOperationForm {
    private ProductExchangeEntity sender;
    private StockEntryType stockEntryType;

    public ProductMovementEntryForm() {
        setIncidence(Incidence.POSITIVE);
    }

    public ProductExchangeEntity getSender() {
        return sender;
    }

    public void setSender(ProductExchangeEntity sender) {
        this.sender = sender;
    }

    public StockEntryType getStockEntryType() {
        return stockEntryType;
    }

    public void setStockEntryType(StockEntryType stockEntryType) {
        this.stockEntryType = stockEntryType;
    }

    public void setProductMovementEntry(ProductMovementEntry productMovementEntry) {
        setProductOperation(productMovementEntry);
        this.setSender(productMovementEntry.getSender());
        this.setStockEntryType(productMovementEntry.getStockEntryType());
    }

    public ProductMovementEntry getProductMovementEntry() {
        ProductMovementEntry productMovementEntry = (ProductMovementEntry) getProductOperation(new ProductMovementEntry());
        productMovementEntry.setSender(getSender());
        productMovementEntry.setStockEntryType(getStockEntryType());
        return productMovementEntry;
    }
}
