package org.openmrs.module.pharmacy;

import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.ReceptionQuantityMode;

import javax.persistence.*;

@Entity
@Table(name = "pharmacy_product_reception")
public class ProductReception extends ProductOperation {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "product_supplier_id")
    private ProductSupplier productSupplier;

    @Column(name = "reception_mode")
    private ReceptionQuantityMode receptionQuantityMode;

    public ProductReception() {
        this.setIncidence(Incidence.POSITIVE);
    }

    public ProductSupplier getProductSupplier() {
        return productSupplier;
    }

    public void setProductSupplier(ProductSupplier productSupplier) {
        this.productSupplier = productSupplier;
    }

    public ReceptionQuantityMode getReceptionQuantityMode() {
        return receptionQuantityMode;
    }

    public void setReceptionQuantityMode(ReceptionQuantityMode receptionQuantityMode) {
        this.receptionQuantityMode = receptionQuantityMode;
    }

}
