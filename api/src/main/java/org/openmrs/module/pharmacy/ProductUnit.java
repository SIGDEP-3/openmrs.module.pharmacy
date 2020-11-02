package org.openmrs.module.pharmacy;

import javax.persistence.*;

@Entity
@Table(name = "pharmacy_product_unit")
public class ProductUnit extends AbstractPharmacyObject {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_unit_id")
    private Integer productUnitId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Override
    public Integer getId() {
        return getProductUnitId();
    }

    @Override
    public void setId(Integer integer) {
        setProductUnitId(integer);
    }

    public Integer getProductUnitId() {
        return productUnitId;
    }

    public void setProductUnitId(Integer productUnitId) {
        this.productUnitId = productUnitId;
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
