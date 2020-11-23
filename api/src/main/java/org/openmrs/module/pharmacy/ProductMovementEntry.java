package org.openmrs.module.pharmacy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.StockEntryType;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductMovementEntry")
@Table(name = "pharmacy_product_movement_entry")
public class ProductMovementEntry extends ProductOperation {

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private ProductExchangeEntity sender;

    @Column(name = "stock_entry_type", nullable = false)
    private StockEntryType stockEntryType;

    public ProductMovementEntry() {
        setIncidence(Incidence.NEGATIVE);
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

}
