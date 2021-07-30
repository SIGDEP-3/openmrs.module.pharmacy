package org.openmrs.module.pharmacy.forms.movements;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.api.ProductMovementService;
import org.openmrs.module.pharmacy.forms.ProductAttributeFluxForm;

public class MovementAttributeFluxForm extends ProductAttributeFluxForm {
    private Integer selectedProductStockId;

    public Integer getSelectedProductStockId() {
        return selectedProductStockId;
    }

    public void setSelectedProductStockId(Integer selectedProductStockId) {
        this.selectedProductStockId = selectedProductStockId;
    }

    private ProductMovementService movementService() { return Context.getService(ProductMovementService.class); }

    private ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private ProductAttributeService attributeService() {
        return Context.getService(ProductAttributeService.class);
    }
}
