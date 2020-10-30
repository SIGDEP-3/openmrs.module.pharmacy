package org.openmrs.module.pharmacy;

import org.openmrs.Patient;
import org.openmrs.module.pharmacy.enumerations.Incidence;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Dispensing")
@Table(name = "pharmacy_dispensing")
public class Dispensing extends Operation {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "regimen_id")
    private ProductRegimen productRegimen;

    @Column(name = "prescription_date", nullable = false)
    private Date prescriptionDate;

    @Column(name = "treatment_days", nullable = false)
    private Integer treatmentDays;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Dispensing() {
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
