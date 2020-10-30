package org.openmrs.module.pharmacy;

import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.StockEntryType;

import javax.persistence.*;

@Entity(name = "ProductMovementOut")
@Table(name = "pharmacy_product_movement_out")
public class ProductMovementOut extends Operation {

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private ProductExchangeEntity recipient;

    @Column(name = "stock_entry_type", nullable = false)
    private StockEntryType stockEntryType;

    public ProductMovementOut() {
        setIncidence(Incidence.POSITIVE);
    }

    public ProductExchangeEntity getRecipient() {
        return recipient;
    }

    public void setRecipient(ProductExchangeEntity recipient) {
        this.recipient = recipient;
    }

    public StockEntryType getStockEntryType() {
        return stockEntryType;
    }

    public void setStockEntryType(StockEntryType stockEntryType) {
        this.stockEntryType = stockEntryType;
    }
}
