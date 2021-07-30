package org.openmrs.module.pharmacy.forms.transfer;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.api.ProductReceptionService;
import org.openmrs.module.pharmacy.forms.ProductAttributeFluxForm;

public class TransferAttributeFluxForm extends ProductAttributeFluxForm {
    private Integer selectedProductStockId;

    public Integer getSelectedProductStockId() {
        return selectedProductStockId;
    }

    public void setSelectedProductStockId(Integer selectedProductStockId) {
        this.selectedProductStockId = selectedProductStockId;
    }

    private ProductReceptionService receptionService() {
        return Context.getService(ProductReceptionService.class);
    }

    private ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private ProductAttributeService attributeService() {
        return Context.getService(ProductAttributeService.class);
    }
}
