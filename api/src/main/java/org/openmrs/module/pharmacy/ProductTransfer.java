package org.openmrs.module.pharmacy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;
import org.openmrs.module.pharmacy.enumerations.StockEntryType;
import org.openmrs.module.pharmacy.enumerations.TransferType;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductTransfer")
@Table(name = "pharmacy_product_transfer")
public class ProductTransfer extends ProductOperation {

    @ManyToOne
    @JoinColumn(name = "exchange_location", nullable = false)
    private Location exchangeLocation;

    @Column(name = "transfer_type", nullable = false)
    private TransferType transferType;

    public ProductTransfer() {}

    public Location getExchangeLocation() {
        return exchangeLocation;
    }

    public void setExchangeLocation(Location exchangeLocation) {
        this.exchangeLocation = exchangeLocation;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }
}
