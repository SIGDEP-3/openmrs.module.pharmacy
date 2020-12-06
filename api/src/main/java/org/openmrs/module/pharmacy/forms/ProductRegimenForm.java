package org.openmrs.module.pharmacy.forms;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductRegimen;
import org.openmrs.module.pharmacy.api.ProductRegimenService;
import org.openmrs.module.pharmacy.api.ProductService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProductRegimenForm {
    private Integer productRegimenId;
    private Integer conceptId;
    private String uuid = UUID.randomUUID().toString();
    private Set<Integer> productIds = new HashSet<Integer>();

    public ProductRegimenForm() {
    }

    public Integer getProductRegimenId() {
        return productRegimenId;
    }

    public void setProductRegimenId(Integer productRegimenId) {
        this.productRegimenId = productRegimenId;
    }

    public Integer getConceptId() {
        return conceptId;
    }

    public void setConceptId(Integer conceptId) {
        this.conceptId = conceptId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Set<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(Set<Integer> productIds) {
        this.productIds = productIds;
    }

    public ProductRegimen getProductRegimen() {

        ProductRegimen regimen = Context.getService(ProductRegimenService.class).getOneProductRegimenById(getProductRegimenId());
        if (regimen == null) {
            regimen = new ProductRegimen();
//            regimen.setProductRegimenId(getProductRegimenId());
            Concept concept = Context.getConceptService().getConcept(getConceptId());
            regimen.setConcept(concept);
            regimen.setUuid(concept.getUuid());
        }
        regimen.setProducts(getProductByIds(getProductIds()));
        return regimen;
    }

    public void setProductRegimen(ProductRegimen regimen) {
        this.setProductRegimenId(regimen.getProductRegimenId());
        this.setConceptId(regimen.getConcept().getConceptId());
        this.addProgramIds(regimen);
        this.setUuid(regimen.getUuid());
    }

    private void addProgramIds(ProductRegimen regimen) {
        for (Product product : regimen.getProducts()) {
            getProductIds().add(product.getProductId());
        }
    }

    private Set<Product> getProductByIds(Set<Integer> productIds) {
        Set<Product> products = new HashSet<Product>();
        for (Integer id : productIds) {
            products.add(Context.getService(ProductService.class).getOneProductById(id));
        }
        return products;
    }
    @Override
    public String toString() {
        return "ProductRegimenForm{" +
                "productRegimenId=" + productRegimenId +
                ", conceptId=" + conceptId +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
