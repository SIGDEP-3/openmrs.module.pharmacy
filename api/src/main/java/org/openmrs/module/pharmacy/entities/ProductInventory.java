package org.openmrs.module.pharmacy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.InventoryType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductInventory")
//@DiscriminatorValue("ProductInventory")
@Table(name = "pharmacy_product_inventory")
public class ProductInventory extends ProductOperation {

    private static final long serialVersionUID = 1L;

//    @Column(name = "inventory_date", nullable = false)
//    private Date inventoryDate;

    @Column(name = "inventory_start_date", nullable = false)
    private Date inventoryStartDate;

    @Column(name = "inventory_type", nullable = false)
    private InventoryType inventoryType;

//    @Column(name = "inventory_end_date", nullable = false)
//    private Date inventoryEndDate;

    public ProductInventory() {
        this.setIncidence(Incidence.EQUAL);
    }

//    public Date getInventoryDate() {
//        return inventoryDate;
//    }
//
//    public void setInventoryDate(Date inventoryDate) {
//        this.inventoryDate = inventoryDate;
//    }

    public Date getInventoryStartDate() {
        return inventoryStartDate;
    }

    public void setInventoryStartDate(Date inventoryStartDate) {
        this.inventoryStartDate = inventoryStartDate;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }
//    public Date getInventoryEndDate() {
//        return inventoryEndDate;
//    }
//
//    public void setInventoryEndDate(Date inventoryEndDate) {
//        this.inventoryEndDate = inventoryEndDate;
//    }

}
