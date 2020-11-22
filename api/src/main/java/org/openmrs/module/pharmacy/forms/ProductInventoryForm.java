package org.openmrs.module.pharmacy.forms;

import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.enumerations.Incidence;

import java.util.Date;

public class ProductInventoryForm extends ProductOperationForm {
    private Date inventoryDate;
    private Date inventoryStartDate;
    private Date inventoryEndDate;

    public ProductInventoryForm() {
        setIncidence(Incidence.EQUAL);
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

    public void setProductInventory(ProductInventory productInventory) {
        super.setProductOperation(productInventory);
        setInventoryStartDate(productInventory.getInventoryStartDate());
        setInventoryDate(productInventory.getInventoryDate());
        setInventoryEndDate(productInventory.getInventoryEndDate());
    }

    public ProductInventory getProductInventory() {
        ProductInventory productInventory = (ProductInventory) getProductOperation(new ProductInventory());
        productInventory.setInventoryDate(getInventoryDate());
        productInventory.setInventoryStartDate(getInventoryStartDate());
        productInventory.setInventoryEndDate(getInventoryEndDate());
        return productInventory;
    }
}
