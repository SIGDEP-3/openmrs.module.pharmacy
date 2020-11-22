package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductPrice;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductRegimen;
import org.openmrs.module.pharmacy.api.PharmacyService;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProductPriceForm {
    private Integer productPriceId;
    private Double salePrice;
    private Double purchasePrice;
    private Integer productProgramId;
    private String uuid = UUID.randomUUID().toString();
    private Integer productId;
    private Date dateCreated = new Date();
    private Boolean isActive = true;

    public ProductPriceForm() {
    }

    public Integer getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(Integer productPriceId) {
        this.productPriceId = productPriceId;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getUuid() {
        return uuid;
    }
    public Integer getProductId() {
        return productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ProductPrice getProductPrice() {
        ProductPrice price = new ProductPrice();
        price.setProductPriceId(getProductPriceId());
        price.setProduct(Context.getService(PharmacyService.class).getOneProductById(getProductId()));
        price.setProductProgram(Context.getService(PharmacyService.class).getOneProductProgramById(getProductProgramId()));
        price.setPurchasePrice(getPurchasePrice());
        price.setSalePrice(getSalePrice());
        price.setDateCreated(getDateCreated());
        price.setActive(getActive());
        price.setUuid(getUuid());

        return price;
    }
    public void setProductPrice(ProductPrice price) {
        this.setProductPriceId(price.getProductPriceId());
        this.setProductProgramId(price.getProductProgram().getProductProgramId());
        this.setProductId(price.getProduct().getProductId());
        this.setSalePrice(price.getSalePrice());
        this.setPurchasePrice(price.getPurchasePrice());
        this.setDateCreated(price.getDateCreated());
//        this.setActive(price.getActive());
        this.setUuid(price.getUuid());
    }

    public Integer getProductProgramId() {
        return productProgramId;
    }

    public void setProductProgramId(Integer productProgramId) {
        this.productProgramId = productProgramId;
    }

    @Override
    public String toString() {
        return "ProductPriceForm{" +
                "productPriceId=" + productPriceId +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
