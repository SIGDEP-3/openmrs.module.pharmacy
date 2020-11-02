package org.openmrs.module.pharmacy;

import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.StockOutType;

import javax.persistence.*;

@Entity(name = "ProductMovementOut")
@Table(name = "pharmacy_product_movement_out")
public class ProductMovementOut extends ProductOperation {

    @ManyToOne
    @JoinColumn(name = "recipient", nullable = false)
    private ProductExchangeEntity recipient;

    @Column(name = "stock_out_type", nullable = false)
    private StockOutType stockOutType;

    public ProductMovementOut() {
        setIncidence(Incidence.POSITIVE);
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
}
