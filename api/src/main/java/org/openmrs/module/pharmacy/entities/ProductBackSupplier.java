package org.openmrs.module.pharmacy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductBackSupplier")
@Table(name = "pharmacy_product_back_supplier")
//@DiscriminatorValue("ProductBackSupplier")
public class ProductBackSupplier extends ProductOperation {

    @ManyToOne
    @JoinColumn(name = "exchange_location", nullable = false)
    private Location exchangeLocation;

    public ProductBackSupplier() {}

    public Location getExchangeLocation() {
        return exchangeLocation;
    }

    public void setExchangeLocation(Location exchangeLocation) {
        this.exchangeLocation = exchangeLocation;
    }

}
