package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductTransfer;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.api.ProductTransferService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.TransferType;

public class ProductTransferForm extends ProductOperationForm {
    private Integer exchangeLocationId;
    private TransferType transferType;

    public ProductTransferForm() {
        super();
    }

    public Integer getExchangeLocationId() {
        return exchangeLocationId;
    }

    public void setExchangeLocationId(Integer exchangeLocationId) {
        this.exchangeLocationId = exchangeLocationId;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
        if (this.transferType.equals(TransferType.IN)) {
            this.incidence = Incidence.POSITIVE;
        } else {
            this.incidence = Incidence.NEGATIVE;
        }
    }

    public void setProductTransfer(ProductTransfer productTransfer) {
        super.setProductOperation(productTransfer);
        setExchangeLocationId(productTransfer.getExchangeLocation().getLocationId());
        setTransferType(productTransfer.getTransferType());
    }

    public ProductTransfer getProductTransfer() {
        ProductTransfer productTransfer = (ProductTransfer) super.getProductOperation(new ProductTransfer());
        productTransfer.setExchangeLocation(Context.getLocationService().getLocation(getExchangeLocationId()));
        productTransfer.setTransferType(getTransferType());

        return productTransfer;
    }

    public ProductTransferService service() {
        return Context.getService(ProductTransferService.class);
    }

    public ProductProgramService programService() {
        return Context.getService(ProductProgramService.class);
    }
}
