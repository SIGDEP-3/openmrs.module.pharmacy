package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.ProductInventoryService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.enumerations.Incidence;

import java.util.Date;

public class ProductInventoryForm extends ProductOperationForm {
//    private Date inventoryDate;
    private Date inventoryStartDate;
//    private Date inventoryEndDate;

    public ProductInventoryForm() {
        setIncidence(Incidence.EQUAL);
        setLocationId(Context.getLocationService().getDefaultLocation().getLocationId());
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

//    public Date getInventoryEndDate() {
//        return inventoryEndDate;
//    }
//
//    public void setInventoryEndDate(Date inventoryEndDate) {
//        this.inventoryEndDate = inventoryEndDate;
//    }

    public void setProductInventory(ProductInventory productInventory) {
        super.setProductOperation(productInventory);
        if (productInventory.getInventoryStartDate() != null) {
            setInventoryStartDate(productInventory.getInventoryStartDate());
        }
        setInventoryStartDate(productInventory.getInventoryStartDate());
//        setInventoryDate(productInventory.getInventoryDate());
//        setInventoryEndDate(productInventory.getInventoryEndDate());
    }

    public ProductInventory getProductInventory() {
        ProductInventory productInventory = (ProductInventory) getProductOperation(new ProductInventory());
        if (getInventoryStartDate() == null) {
            ProductInventory previousInventory = service().getLastProductInventory(Context.getLocationService().getDefaultLocation(),
                    Context.getService(ProductProgramService.class).getOneProductProgramById(getProductProgramId()));
            if (previousInventory != null) {
                setInventoryStartDate(previousInventory.getOperationDate());
            } else {
                setInventoryStartDate(getOperationDate());
            }
        }
        productInventory.setInventoryStartDate(getInventoryStartDate());
        return productInventory;
    }

    public ProductInventoryService service() {
        return Context.getService(ProductInventoryService.class);
    }
}
