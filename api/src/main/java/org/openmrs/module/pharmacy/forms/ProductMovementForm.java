package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductMovementEntry;
import org.openmrs.module.pharmacy.ProductMovementOut;
import org.openmrs.module.pharmacy.api.ProductExchangeEntityService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.StockEntryType;
import org.openmrs.module.pharmacy.enumerations.StockOutType;

public class ProductMovementForm extends ProductOperationForm {
    private Integer entityId;
    private StockEntryType stockEntryType;
    private StockOutType stockOutType;

    public ProductMovementForm() {
        setIncidence(Incidence.POSITIVE);
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public StockEntryType getStockEntryType() {
        return stockEntryType;
    }

    public void setStockEntryType(StockEntryType stockEntryType) {
        this.stockEntryType = stockEntryType;
    }

    public StockOutType getStockOutType() {
        return stockOutType;
    }

    public void setStockOutType(StockOutType stockOutType) {
        this.stockOutType = stockOutType;
    }


    public ProductMovementEntry getProductMovementEntry() {
        ProductMovementEntry productMovementEntry = (ProductMovementEntry) getProductOperation(new ProductMovementEntry());
        if (productMovementEntry.getStockEntryType().equals(StockEntryType.TRANSFER_IN)){

            productMovementEntry.setSender(Context.getService(ProductExchangeEntityService.class).getOneProductExchangeById(getEntityId()));
        }
        productMovementEntry.setStockEntryType(getStockEntryType());
        return productMovementEntry;
    }
    public void setProductMovementOut(ProductMovementOut productMovementOut) {
        setProductOperation(productMovementOut);
        this.setStockOutType(productMovementOut.getStockOutType());
        this.setEntityId(productMovementOut.getRecipient().getProductExchangeEntityId());
    }
    public void setProductMovementEntry(ProductMovementEntry productMovementEntry) {
        setProductOperation(productMovementEntry);
        this.setEntityId(productMovementEntry.getSender().getProductExchangeEntityId());
        this.setStockEntryType(productMovementEntry.getStockEntryType());
    }
    public ProductMovementOut getProductMovementOut() {
        ProductMovementOut productMovementOut = (ProductMovementOut) getProductOperation(new ProductMovementOut());
//        if (productMovementOut.getStockOutType().equals(StockOutType.TRANSFER_OUT)){
//            productMovementOut.setRecipient(Context.getService(ProductExchangeEntityService.class).getOneProductExchangeById(getEntityId()));
//        }

        productMovementOut.setStockOutType(getStockOutType());
        return productMovementOut;
    }
}
