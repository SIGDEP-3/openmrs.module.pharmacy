package org.openmrs.module.pharmacy;

import org.openmrs.module.pharmacy.enumerations.Incidence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "ProductInventory")
@Table(name = "pharmacy_product_inventory")
public class ProductInventory extends ProductOperation {

    private static final long serialVersionUID = 1L;

    @Column(name = "inventory_date", nullable = false)
    private Date inventoryDate;

    @Column(name = "inventory_start_date", nullable = false)
    private Date inventoryStartDate;

    @Column(name = "inventory_end_date", nullable = false)
    private Date inventoryEndDate;

    public ProductInventory() {
        this.setIncidence(Incidence.NONE);
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public Date getInventoryStartDate() {
        return inventoryStartDate;
    }

    public void setInventoryStartDate(Date inventoryStartDate) {
        this.inventoryStartDate = inventoryStartDate;
    }

    public Date getInventoryEndDate() {
        return inventoryEndDate;
    }

    public void setInventoryEndDate(Date inventoryEndDate) {
        this.inventoryEndDate = inventoryEndDate;
    }

}
