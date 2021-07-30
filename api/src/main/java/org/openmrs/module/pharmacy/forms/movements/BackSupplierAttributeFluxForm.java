package org.openmrs.module.pharmacy.forms.movements;

import org.openmrs.module.pharmacy.forms.ProductAttributeFluxForm;

public class BackSupplierAttributeFluxForm extends ProductAttributeFluxForm {
    private Integer selectedProductStockId;

    public Integer getSelectedProductStockId() {
        return selectedProductStockId;
    }

    public void setSelectedProductStockId(Integer selectedProductStockId) {
        this.selectedProductStockId = selectedProductStockId;
    }
}
