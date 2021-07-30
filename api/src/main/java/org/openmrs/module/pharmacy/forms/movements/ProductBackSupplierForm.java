package org.openmrs.module.pharmacy.forms.movements;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.ProductBackSupplier;
import org.openmrs.module.pharmacy.api.ProductBackSupplierService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.forms.ProductOperationForm;

public class ProductBackSupplierForm extends ProductOperationForm {
    private Integer exchangeLocationId;

    public ProductBackSupplierForm() {
        super();
    }

    public Integer getExchangeLocationId() {
        return exchangeLocationId;
    }

    public void setExchangeLocationId(Integer exchangeLocationId) {
        this.exchangeLocationId = exchangeLocationId;
    }

    public void setProductBackSupplier(ProductBackSupplier productBackSupplier) {
        super.setProductOperation(productBackSupplier);
        setExchangeLocationId(productBackSupplier.getExchangeLocation().getLocationId());
    }

    public ProductBackSupplier getProductBackSupplier() {
        ProductBackSupplier productBackSupplier = (ProductBackSupplier) super.getProductOperation(new ProductBackSupplier());
        productBackSupplier.setExchangeLocation(Context.getLocationService().getLocation(getExchangeLocationId()));

        return productBackSupplier;
    }

    public ProductBackSupplierService service() {
        return Context.getService(ProductBackSupplierService.class);
    }

    public ProductProgramService programService() {
        return Context.getService(ProductProgramService.class);
    }
}
