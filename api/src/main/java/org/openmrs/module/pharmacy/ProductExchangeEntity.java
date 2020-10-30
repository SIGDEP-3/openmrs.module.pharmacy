package org.openmrs.module.pharmacy;

import javax.persistence.*;

@Entity(name = "ProductSender")
@Table(name = "pharmacy_product_sender")
public class ProductExchangeEntity extends AbstractPharmacyObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_exchange_entity_id")
    private Integer productExchangeEntityId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    public ProductExchangeEntity() {
    }

    public Integer getProductExchangeEntityId() {
        return productExchangeEntityId;
    }

    public void setProductExchangeEntityId(Integer productSenderId) {
        this.productExchangeEntityId = productSenderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
