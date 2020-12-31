package org.openmrs.module.pharmacy.forms;

public class BackSupplierAttributeFluxForm extends ProductAttributeFluxForm {
    private Integer selectedProductStockId;

    public Integer getSelectedProductStockId() {
        return selectedProductStockId;
    }

    public void setSelectedProductStockId(Integer selectedProductStockId) {
        this.selectedProductStockId = selectedProductStockId;
    }
}
