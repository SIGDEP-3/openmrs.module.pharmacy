package org.openmrs.module.pharmacy;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "pharmacy_product_program")
public class ProductProgram extends AbstractPharmacyObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_program_id")
    private Integer productProgramId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(name = "pharmacy_product_program_members",
            joinColumns = @JoinColumn(name = "program_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products;

    @Override
    public Integer getId() {
        return getProductProgramId();
    }

    @Override
    public void setId(Integer integer) {
        setProductProgramId(integer);
    }

    public Integer getProductProgramId() {
        return productProgramId;
    }

    public void setProductProgramId(Integer productProgramId) {
        this.productProgramId = productProgramId;
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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
