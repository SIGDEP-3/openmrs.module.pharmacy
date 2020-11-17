package org.openmrs.module.pharmacy;

import org.openmrs.Location;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ProductAttributeFlux")
@Table(name = "pharmacy_product_attribute_flux")
public class ProductAttributeFlux extends AbstractPharmacyData implements Comparable<ProductAttributeFlux> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_attribute_flux_id")
    private Integer productAttributeFluxId;

    @ManyToOne
    @JoinColumn(name = "product_attribute_id")
    private ProductAttribute productAttribute;

    @Temporal(TemporalType.DATE)
    @Column(name = "operation_date")
    private Date operationDate;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "status")
    private OperationStatus status;

    @ManyToOne
    @JoinColumn(name = "operation_id", nullable = false)
    private ProductOperation productOperation;

    @Column(name = "observation")
    private String observation;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public ProductAttributeFlux() {
    }

    public Integer getProductAttributeFluxId() {
        return productAttributeFluxId;
    }

    public void setProductAttributeFluxId(Integer productAttributeFluxId) {
        this.productAttributeFluxId = productAttributeFluxId;
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

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Override
    public int compareTo(ProductAttributeFlux o) {
        return this.getDateCreated().compareTo(o.getDateCreated());
    }
}
