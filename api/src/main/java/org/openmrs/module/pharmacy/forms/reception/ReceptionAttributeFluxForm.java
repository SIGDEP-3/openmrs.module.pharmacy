package org.openmrs.module.pharmacy.forms.reception;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.Product;
import org.openmrs.module.pharmacy.entities.ProductAttributeFlux;
import org.openmrs.module.pharmacy.entities.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.entities.ProductReception;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.api.ProductReceptionService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.ReceptionQuantityMode;
import org.openmrs.module.pharmacy.forms.ProductAttributeFluxForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;

public class ReceptionAttributeFluxForm extends ProductAttributeFluxForm {
    private Integer quantityToDeliver;
    private Integer selectedProductFluxId;

    public ReceptionAttributeFluxForm() {
    }

    public Integer getQuantityToDeliver() {
        return quantityToDeliver;
    }

    public void setQuantityToDeliver(Integer quantityToDeliver) {
        this.quantityToDeliver = quantityToDeliver;
    }

    public Integer getSelectedProductFluxId() {
        return selectedProductFluxId;
    }

    public void setSelectedProductFluxId(Integer selectedProductFluxId) {
        this.selectedProductFluxId = selectedProductFluxId;
    }

    public void setProductAttributeFlux(ProductAttributeFlux flux, ProductReception productReception) {
        if (productReception.getReceptionQuantityMode().equals(ReceptionQuantityMode.WHOLESALE)) {
            Product product = flux.getProductAttribute().getProduct();
            double fluxQuantity = flux.getQuantity() / product.getUnitConversion();
            flux.setQuantity((int) fluxQuantity);
        }
        super.setProductAttributeFlux(flux, productReception);
        if (productReception.getIncidence().equals(Incidence.POSITIVE)) {
            ProductAttributeOtherFlux otherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(
                    flux.getProductAttribute(),
                    productReception,
                    OperationUtils.getUserLocation());
            if (productReception.getReceptionQuantityMode().equals(ReceptionQuantityMode.WHOLESALE)) {
                Product product = flux.getProductAttribute().getProduct();
                double fluxQuantity = otherFlux.getQuantity() / product.getUnitConversion();
                setQuantityToDeliver((int) fluxQuantity);
            } else {
                setQuantityToDeliver(otherFlux.getQuantity().intValue());
            }
        }
    }

    public ProductAttributeOtherFlux getProductAttributeOtherFlux() {
        ProductAttributeOtherFlux productAttributeOtherFlux = fluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(
                attributeService().getOneProductAttributeByBatchNumber(getBatchNumber(), OperationUtils.getUserLocation()),
                receptionService().getOneProductReceptionById(getProductOperationId()),
                OperationUtils.getUserLocation());
        if (productAttributeOtherFlux != null){
            productAttributeOtherFlux.setQuantity(getQuantityToDeliver().doubleValue());
        } else {
            productAttributeOtherFlux = new ProductAttributeOtherFlux();
            productAttributeOtherFlux.setQuantity(getQuantityToDeliver().doubleValue());
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
