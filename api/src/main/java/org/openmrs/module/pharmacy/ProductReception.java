package org.openmrs.module.pharmacy;

import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.ReceptionQuantityMode;

import javax.persistence.*;

@Entity
@Table(name = "pharmacy_product_reception")
public class ProductReception extends Operation {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "product_supplier")
    private ProductSupplier productSupplier;

    @Column(name = "observation")
    private String observation;

    @Column(name = "reception_mode")
    private ReceptionQuantityMode receptionQuantityMode;

    @Column(name = "status", nullable = false)
    private OperationStatus operationStatus;

    public ProductReception() {
        this.setIncidence(Incidence.POSITIVE);
    }

    public ProductSupplier getProductSupplier() {
        return productSupplier;
    }

    public void setProductSupplier(ProductSupplier productSupplier) {
        this.productSupplier = productSupplier;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public ReceptionQuantityMode getReceptionQuantityMode() {
        return receptionQuantityMode;
    }

    public void setReceptionQuantityMode(ReceptionQuantityMode receptionQuantityMode) {
        this.receptionQuantityMode = receptionQuantityMode;
    }

    public OperationStatus getActionStatus() {
        return operationStatus;
    }

    public void setActionStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }

}
