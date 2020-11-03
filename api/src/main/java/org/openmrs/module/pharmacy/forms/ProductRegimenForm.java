package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductRegimen;

import java.util.UUID;

public class ProductRegimenForm {
    private Integer productRegimenId;
    private Integer conceptId;
    private String uuid = UUID.randomUUID().toString();

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

    public ProductRegimen getProductRegimen() {
        ProductRegimen regimen = new ProductRegimen();
        regimen.setProductRegimenId(getProductRegimenId());
        regimen.setConcept(Context.getConceptService().getConcept(getConceptId()));
        regimen.setUuid(getUuid());

        return regimen;
    }

    public void setProductRegimen(ProductRegimen regimen) {
        this.setProductRegimenId(regimen.getProductRegimenId());
        this.setConceptId(regimen.getConcept().getConceptId());
        this.setUuid(regimen.getUuid());
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
