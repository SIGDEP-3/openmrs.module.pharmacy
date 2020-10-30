package org.openmrs.module.pharmacy;

import org.openmrs.Patient;
import org.openmrs.module.pharmacy.enumerations.Incidence;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ProductDispensing")
@Table(name = "pharmacy_product_dispensing")
public class ProductDispensing extends ProductOperation {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "regimen_id")
    private ProductRegimen productRegimen;

    @Column(name = "prescription_date", nullable = false)
    private Date prescriptionDate;

    @Column(name = "treatment_days", nullable = false)
    private Integer treatmentDays;

    @Column(name = "treatment_end_date", nullable = false)
    private Date treatmentEndDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public ProductDispensing() {
        this.setIncidence(Incidence.NEGATIVE);
    }

    public ProductRegimen getProductRegimen() {
        return productRegimen;
    }

    public void setProductRegimen(ProductRegimen productRegimen) {
        this.productRegimen = productRegimen;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date orderDate) {
        this.prescriptionDate = orderDate;
    }

    public Integer getTreatmentDays() {
        return treatmentDays;
    }

    public void setTreatmentDays(Integer treatmentDays) {
        this.treatmentDays = treatmentDays;
    }

    public Date getTreatmentEndDate() {
        return treatmentEndDate;
    }

    public void setTreatmentEndDate(Date treatmentEndDate) {
        this.treatmentEndDate = treatmentEndDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
