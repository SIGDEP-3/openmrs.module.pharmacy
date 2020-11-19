package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductAttribute;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;

import java.util.Date;

public class ReceptionAttributeFluxForm extends ProductAttributeFluxForm {
    private Integer quantityToDeliver;

    public ReceptionAttributeFluxForm() {
    }


    public Integer getQuantityToDeliver() {
        return quantityToDeliver;
    }

    public void setQuantityToDeliver(Integer quantityToDeliver) {
        this.quantityToDeliver = quantityToDeliver;
    }

    public void setProductAttributeFlux(ProductAttributeFlux productAttributeFlux, ProductReception productReception) {
        super.setProductAttributeFlux(productAttributeFlux, productReception);
        ProductAttributeOtherFlux otherFlux = Context.getService(PharmacyService.class).getOneProductAttributeOtherFluxByAttributeAndOperation(productAttributeFlux.getProductAttribute(), productReception);
        setQuantityToDeliver(otherFlux.getQuantity());
    }

    public ProductAttributeOtherFlux getProductAttributeOtherFlux() {
        ProductAttributeOtherFlux productAttributeOtherFlux = Context.getService(PharmacyService.class).getOneProductAttributeOtherFluxByAttributeAndOperation(
                Context.getService(PharmacyService.class).getOneProductAttributeByBatchNumber(getBatchNumber()),
                Context.getService(PharmacyService.class).getOneProductReceptionById(getProductOperationId())
        );
        if (productAttributeOtherFlux != null){
            productAttributeOtherFlux.setQuantity(getQuantityToDeliver());
        } else {
            productAttributeOtherFlux = new ProductAttributeOtherFlux();
            productAttributeOtherFlux.setQuantity(getQuantityToDeliver());
            productAttributeOtherFlux.setLabel("Quantitié livrée");
            productAttributeOtherFlux.setLocation(Context.getLocationService().getDefaultLocation());
            productAttributeOtherFlux.setProductAttribute(getProductAttribute());
            productAttributeOtherFlux.setProductOperation(Context.getService(PharmacyService.class).getOneProductReceptionById(getProductOperationId()));
        }

        return productAttributeOtherFlux;
    }
}
