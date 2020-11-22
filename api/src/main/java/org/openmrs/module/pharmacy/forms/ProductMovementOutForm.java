package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductExchangeEntity;
import org.openmrs.module.pharmacy.ProductMovementOut;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.ReceptionQuantityMode;
import org.openmrs.module.pharmacy.enumerations.StockOutType;

public class ProductMovementOutForm extends ProductOperationForm {
    private ProductExchangeEntity recipient;
    private StockOutType stockOutType;

    public ProductMovementOutForm() {
        setIncidence(Incidence.NEGATIVE);
    }

    public ProductExchangeEntity getRecipient() {
        return recipient;
    }

    public void setRecipient(ProductExchangeEntity recipient) {
        this.recipient = recipient;
    }

    public StockOutType getStockOutType() {
        return stockOutType;
    }

    public void setStockOutType(StockOutType stockOutType) {
        this.stockOutType = stockOutType;
    }

    public void setProductMovementOut(ProductMovementOut productMovementOut) {
        setProductOperation(productMovementOut);
        this.setStockOutType(productMovementOut.getStockOutType());
        this.setRecipient(productMovementOut.getRecipient());
    }

    public ProductMovementOut getProductMovementOut() {
        ProductMovementOut productMovementOut = (ProductMovementOut) getProductOperation(new ProductMovementOut());
        productMovementOut.setRecipient(getRecipient());
        productMovementOut.setStockOutType(getStockOutType());
        productMovementOut.setRecipient(getRecipient());
        return productMovementOut;
    }
}
