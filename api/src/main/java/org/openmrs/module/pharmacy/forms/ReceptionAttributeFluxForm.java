package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.api.ProductReceptionService;
import org.openmrs.module.pharmacy.enumerations.ReceptionQuantityMode;
import org.openmrs.module.pharmacy.utils.OperationUtils;

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

    public void setProductAttributeFlux(ProductAttributeFlux flux, ProductReception productReception) {
        if (productReception.getReceptionQuantityMode().equals(ReceptionQuantityMode.WHOLESALE)) {
            Product product = flux.getProductAttribute().getProduct();
            double fluxQuantity = flux.getQuantity() / product.getUnitConversion();
            flux.setQuantity((int) fluxQuantity);
        }
        super.setProductAttributeFlux(flux, productReception);
        ProductAttributeOtherFlux otherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(
                flux.getProductAttribute(),
                productReception,
                OperationUtils.getUserLocation());
        if (productReception.getReceptionQuantityMode().equals(ReceptionQuantityMode.WHOLESALE)) {
            Product product = flux.getProductAttribute().getProduct();
            double fluxQuantity = otherFlux.getQuantity() / product.getUnitConversion();
            setQuantityToDeliver((int) fluxQuantity);
        } else {
            setQuantityToDeliver(otherFlux.getQuantity());
        }
    }

    public ProductAttributeOtherFlux getProductAttributeOtherFlux() {
        ProductAttributeOtherFlux productAttributeOtherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(
                attributeService().getOneProductAttributeByBatchNumber(getBatchNumber(), OperationUtils.getUserLocation()),
                receptionService().getOneProductReceptionById(getProductOperationId()),
                OperationUtils.getUserLocation());
        if (productAttributeOtherFlux != null){
            productAttributeOtherFlux.setQuantity(getQuantityToDeliver());
        } else {
            productAttributeOtherFlux = new ProductAttributeOtherFlux();
            productAttributeOtherFlux.setQuantity(getQuantityToDeliver());
            productAttributeOtherFlux.setLabel("Quantitié livrée");
            productAttributeOtherFlux.setLocation(Context.getLocationService().getDefaultLocation());
            productAttributeOtherFlux.setProductAttribute(getProductAttribute());
            productAttributeOtherFlux.setProductOperation(receptionService().getOneProductReceptionById(getProductOperationId()));
        }

        return productAttributeOtherFlux;
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
