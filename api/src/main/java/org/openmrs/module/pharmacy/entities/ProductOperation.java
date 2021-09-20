package org.openmrs.module.pharmacy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;
import org.openmrs.module.pharmacy.AbstractPharmacyData;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;

import javax.persistence.*;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductOperation")
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name="operation_type")
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

    @Column(name = "operation_status")
    private OperationStatus operationStatus;

    @Column(name = "incidence", nullable = false)
    private Incidence incidence;

    @Column(name = "observation")
    private String observation;

    @OneToMany(mappedBy = "productOperation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductAttributeFlux> productAttributeFluxes = new HashSet<>();

    @OneToMany(mappedBy = "productOperation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductAttributeOtherFlux> productAttributeOtherFluxes = new HashSet<>();

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

    public Set<ProductAttributeOtherFlux> getProductAttributeOtherFluxes() {
        return productAttributeOtherFluxes;
    }

    public void setProductAttributeOtherFluxes(Set<ProductAttributeOtherFlux> productAttributeOtherFluxes) {
        this.productAttributeOtherFluxes = productAttributeOtherFluxes;
    }

    public void addProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
        if (productAttributeFluxes == null) {
            productAttributeFluxes = new HashSet<ProductAttributeFlux>();
        }
        productAttributeFlux.setProductOperation(this);
        productAttributeFluxes.add(productAttributeFlux);
    }

    public void removeProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
        if (productAttributeFluxes != null) {
            productAttributeFluxes.remove(productAttributeFlux);
        }
    }

    public void addProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
        if (productAttributeOtherFluxes == null) {
            productAttributeOtherFluxes = new HashSet<ProductAttributeOtherFlux>();
        }
        productAttributeOtherFlux.setProductOperation(this);
        productAttributeOtherFluxes.add(productAttributeOtherFlux);
    }

    public void removeProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
        if (productAttributeOtherFlux != null) {
            productAttributeOtherFluxes.remove(productAttributeOtherFlux);
        }
    }

    public List<Product> getOtherFluxesProductList() {
        List<Product> products = new ArrayList<>();
        for (ProductAttributeOtherFlux otherFlux : productAttributeOtherFluxes) {
            if (!products.contains(otherFlux.getProduct())) {
                products.add(otherFlux.getProduct());
            }
        }
        return products;
    }

    public List<Product> getFluxesProductList() {
        List<Product> products = new ArrayList<>();
        for (ProductAttributeFlux flux : productAttributeFluxes) {
            if (!products.contains(flux.getProductAttribute().getProduct())) {
                products.add(flux.getProductAttribute().getProduct());
            }
        }
        return products;
    }
}
