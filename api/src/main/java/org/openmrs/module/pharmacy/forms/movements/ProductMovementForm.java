package org.openmrs.module.pharmacy.forms.movements;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.ProductMovementEntry;
import org.openmrs.module.pharmacy.entities.ProductMovementOut;
import org.openmrs.module.pharmacy.api.ProductExchangeEntityService;
import org.openmrs.module.pharmacy.enumerations.StockEntryType;
import org.openmrs.module.pharmacy.enumerations.StockOutType;
import org.openmrs.module.pharmacy.forms.ProductOperationForm;

public class ProductMovementForm extends ProductOperationForm {
    private Integer entityId;
    private StockEntryType stockEntryType;
    private StockOutType stockOutType;

    public ProductMovementForm() {
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
//        if (productMovementEntry.getStockEntryType().equals(StockEntryType.TRANSFER_IN)){
//
//            productMovementEntry.setSender(Context.getService(ProductExchangeEntityService.class).getOneProductExchangeById(getEntityId()));
//        }
        productMovementEntry.setSender(Context.getService(ProductExchangeEntityService.class).getOneProductExchangeById(getEntityId()));
        productMovementEntry.setStockEntryType(getStockEntryType());
        return productMovementEntry;
    }
    public void setProductMovementOut(ProductMovementOut productMovementOut) {
        super.setProductOperation(productMovementOut);
        this.setStockOutType(productMovementOut.getStockOutType());
        this.setProductProgramId(productMovementOut.getProductProgram().getProductProgramId());
        this.setIncidence(productMovementOut.getIncidence());
        if (productMovementOut.getRecipient() != null){
            this.setEntityId(productMovementOut.getRecipient().getProductExchangeEntityId());
        }

    }
    public void setProductMovementEntry(ProductMovementEntry productMovementEntry) {
        super.setProductOperation(productMovementEntry);
        this.setEntityId(productMovementEntry.getSender().getProductExchangeEntityId());
        this.setProductProgramId(productMovementEntry.getProductProgram().getProductProgramId());
        this.setStockEntryType(productMovementEntry.getStockEntryType());
        this.setIncidence(productMovementEntry.getIncidence());
    }
    public ProductMovementOut getProductMovementOut() {
        ProductMovementOut productMovementOut = (ProductMovementOut) getProductOperation(new ProductMovementOut());
        productMovementOut.setStockOutType(getStockOutType());
        return productMovementOut;
    }
}
