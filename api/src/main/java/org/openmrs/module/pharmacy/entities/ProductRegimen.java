package org.openmrs.module.pharmacy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Concept;
import org.openmrs.module.pharmacy.AbstractPharmacyObject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductRegimen")
@Table(name = "pharmacy_product_regimen")
public class ProductRegimen extends AbstractPharmacyObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_regimen_id")
    private Integer productRegimenId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "concept_id", unique = true)
    private Concept concept;

    @ManyToMany(mappedBy="productRegimens", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<Product>();

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
