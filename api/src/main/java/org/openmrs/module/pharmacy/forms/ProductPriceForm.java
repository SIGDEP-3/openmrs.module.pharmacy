package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductPrice;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductRegimen;
import org.openmrs.module.pharmacy.api.PharmacyService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProductPriceForm {
    private Integer productPriceId;
    private Double salePrice;
    private Double purchasePrice;
    private Product product;
    private ProductProgram productProgram;
    private String uuid = UUID.randomUUID().toString();
    private Integer productId;
    private Set<Integer> productProgramIds = new HashSet<Integer>();

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

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public String getUuid() {
        return uuid;
    }
    public Integer getProductId() {
        return productId;
    }
    public Set<Integer> getProductProgramIds() {
        return productProgramIds;
    }

    public void setProductProgramIds(Set<Integer> productProgramIds) {
        this.productProgramIds = productProgramIds;
    }


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ProductPrice getProductPrice() {
        ProductPrice price = new ProductPrice();
        price.setProductPriceId(getProductPriceId());
        price.setUuid(getUuid());

        return price;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setProductPrice(ProductPrice price) {
        this.setProductPriceId(price.getProductPriceId());
        this.setUuid(price.getUuid());
    }
    private Set<ProductProgram> getProgramsByIds(Set<Integer> programIds) {
        Set<ProductProgram> programs = new HashSet<ProductProgram>();
        for (Integer id : programIds) {
            programs.add(Context.getService(PharmacyService.class).getOneProductProgramById(id));
        }
        return programs;
    }
    public Product getProduct() {
        Product product = new Product();
        product.setProductId(getProductId());
        if (!productProgramIds.isEmpty()) {
            product.getProductPrograms().addAll(getProgramsByIds(getProductProgramIds()));
        }
        product.setUuid(getUuid());

        return product;
    }

    @Override
    public String toString() {
        return "ProductPriceForm{" +
                "productPriceId=" + productPriceId +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
