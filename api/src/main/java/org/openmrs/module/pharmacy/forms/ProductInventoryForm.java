package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.api.ProductInventoryService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.InventoryType;

import java.util.Date;

public class ProductInventoryForm extends ProductOperationForm {
    private Date inventoryStartDate;
    private InventoryType inventoryType;

    public ProductInventoryForm() {
        super();
        setIncidence(Incidence.EQUAL);
    }

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

    public void setProductInventory(ProductInventory productInventory) {
        super.setProductOperation(productInventory);
        if (productInventory.getInventoryStartDate() != null) {
            setInventoryStartDate(productInventory.getInventoryStartDate());
        }
        setInventoryType(productInventory.getInventoryType());
    }

    public ProductInventory getProductInventory() {
        ProductInventory productInventory = (ProductInventory) super.getProductOperation(new ProductInventory());
        if (getInventoryStartDate() == null) {
            ProductInventory previousInventory = service().getLastProductInventory(
                    Context.getLocationService().getLocation(getLocationId()),
                    programService().getOneProductProgramById(getProductProgramId())
            );
            if (previousInventory != null) {
                setInventoryStartDate(previousInventory.getOperationDate());
            } else {
                setInventoryStartDate(getOperationDate());
            }
        }
        productInventory.setInventoryStartDate(getInventoryStartDate());
        productInventory.setInventoryType(getInventoryType());
        return productInventory;
    }

    public ProductInventoryService service() {
        return Context.getService(ProductInventoryService.class);
    }

    public ProductProgramService programService() {
        return Context.getService(ProductProgramService.class);
    }
}
