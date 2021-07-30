package org.openmrs.module.pharmacy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;
import org.openmrs.module.pharmacy.AbstractPharmacyObject;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductAttributeOtherFlux")
@Table(name = "pharmacy_product_attribute_other_flux")
public class ProductAttributeOtherFlux extends AbstractPharmacyObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_attribute_other_flux_id")
    private Integer productAttributeOtherFluxId;

    @ManyToOne
    @JoinColumn(name = "product_attribute_id")
    private ProductAttribute productAttribute;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @ManyToOne
    @JoinColumn(name = "operation_id", nullable = false)
    private ProductOperation productOperation;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public ProductAttributeOtherFlux() {
    }

    public Integer getProductAttributeOtherFluxId() {
        return productAttributeOtherFluxId;
    }

    public void setProductAttributeOtherFluxId(Integer productAttributeOtherFluxId) {
        this.productAttributeOtherFluxId = productAttributeOtherFluxId;
    }

    public ProductAttribute getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(ProductAttribute productAttribute) {
        this.productAttribute = productAttribute;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public ProductOperation getProductOperation() {
        return productOperation;
    }

    public void setProductOperation(ProductOperation productOperation) {
        this.productOperation = productOperation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
