package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.ProductDispensation;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.api.ProductDispensationService;

public class DispensationAttributeFluxForm extends ProductAttributeFluxForm {
    private Integer quantityToDeliver;

    public DispensationAttributeFluxForm() {
    }


    public Integer getQuantityToDeliver() {
        return quantityToDeliver;
    }

    public void setQuantityToDeliver(Integer quantityToDeliver) {
        this.quantityToDeliver = quantityToDeliver;
    }

    public void setProductAttributeFlux(ProductAttributeFlux productAttributeFlux, ProductDispensation productDispensation) {
        super.setProductAttributeFlux(productAttributeFlux, productDispensation);
        ProductAttributeOtherFlux otherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(productAttributeFlux.getProductAttribute(), productDispensation);
        setQuantityToDeliver(otherFlux.getQuantity());
    }

    public ProductAttributeOtherFlux getProductAttributeOtherFlux() {
        ProductAttributeOtherFlux productAttributeOtherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(
                attributeService().getOneProductAttributeByBatchNumber(getBatchNumber()),
                receptionService().getOneProductDispensationById(getProductOperationId())
        );
        if (productAttributeOtherFlux != null){
            productAttributeOtherFlux.setQuantity(getQuantityToDeliver());
        } else {
            productAttributeOtherFlux = new ProductAttributeOtherFlux();
            productAttributeOtherFlux.setQuantity(getQuantityToDeliver());
            productAttributeOtherFlux.setLabel("Quantitié livrée");
            productAttributeOtherFlux.setLocation(Context.getLocationService().getDefaultLocation());
            productAttributeOtherFlux.setProductAttribute(getProductAttribute());
            productAttributeOtherFlux.setProductOperation(receptionService().getOneProductDispensationById(getProductOperationId()));
        }

        return productAttributeOtherFlux;
    }

    private ProductDispensationService receptionService() {
        return Context.getService(ProductDispensationService.class);
    }

    private ProductAttributeFluxService fluxService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    private ProductAttributeService attributeService() {
        return Context.getService(ProductAttributeService.class);
    }
}
