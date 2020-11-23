package org.openmrs.module.pharmacy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;

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

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

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

    public void setProductAttributeOtherFluxId(Integer productAttributeFluxId) {
        this.productAttributeOtherFluxId = productAttributeFluxId;
    }

    public ProductAttribute getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(ProductAttribute productAttribute) {
        this.productAttribute = productAttribute;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
