package org.openmrs.module.pharmacy;

import org.openmrs.Concept;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "pharmacy_product_regimen")
public class ProductRegimen extends AbstractPharmacyObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_regimen_id")
    private Integer productRegimenId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "concept_id")
    private Concept concept;

    @ManyToMany
    @JoinTable(name = "pharmacy_product_regimen_members",
            joinColumns = @JoinColumn(name = "regimen_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products;

    @Override
    public Integer getId() {
        return getProductRegimenId();
    }

    @Override
    public void setId(Integer integer) {
        setProductRegimenId(integer);
    }

    public Integer getProductRegimenId() {
        return productRegimenId;
    }

    public void setProductRegimenId(Integer productRegimenId) {
        this.productRegimenId = productRegimenId;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
