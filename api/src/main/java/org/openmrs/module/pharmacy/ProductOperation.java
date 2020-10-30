package org.openmrs.module.pharmacy;

import org.openmrs.Location;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Operation")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "pharmacy_product_operation")
public abstract class ProductOperation extends AbstractPharmacyData {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_operation_id")
    private Integer productOperationId;

    @Column(name = "operation_number")
    private String operationNumber;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private ProductProgram productProgram;

    @Temporal(TemporalType.DATE)
    @Column(name = "operation_date", nullable = false)
    private Date operationDate;

    @ManyToOne
    @JoinColumn(nullable = false, name = "location_id")
    private Location location;

    @Column(name = "status")
    private OperationStatus operationStatus;

    @Column(name = "incidence", nullable = false)
    private Incidence incidence;

    @Column(name = "observation")
    private String observation;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL)
    private Set<ProductAttributeFlux> productAttributeFluxes;

    public ProductOperation() {
    }

    public Integer getProductOperationId() {
        return productOperationId;
    }

    public void setProductOperationId(Integer productOperationId) {
        this.productOperationId = productOperationId;
    }

    public String getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public OperationStatus getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Incidence getIncidence() {
        return incidence;
    }

    public void setIncidence(Incidence incidence) {
        this.incidence = incidence;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Set<ProductAttributeFlux> getProductAttributeFluxes() {
        return productAttributeFluxes;
    }

    public void setProductAttributeFluxes(Set<ProductAttributeFlux> productAttributeFluxes) {
        this.productAttributeFluxes = productAttributeFluxes;
    }

    void addProduct(ProductAttributeFlux productAttributeFlux) {
        if (productAttributeFluxes == null) {
            productAttributeFluxes = new HashSet<ProductAttributeFlux>();
        }
        productAttributeFluxes.add(productAttributeFlux);
    }

    void removeProduct(ProductAttributeFlux productAttributeFlux) {
        if (productAttributeFluxes != null) {
            productAttributeFluxes.remove(productAttributeFlux);
        }
    }
}
