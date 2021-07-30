package org.openmrs.module.pharmacy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Encounter;
import org.openmrs.module.pharmacy.enumerations.Incidence;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductDispensation")
@Table(name = "pharmacy_product_dispensation")
public class ProductDispensation extends ProductOperation {

    private static final long serialVersionUID = 1L;

    @Column(name = "prescription_date")
    private Date prescriptionDate;

    @ManyToOne
    @JoinColumn(name = "encounter_id")
    private Encounter encounter;

    @OneToOne(mappedBy = "dispensation")
    private MobilePatientDispensationInfo mobilePatientDispensationInfo;

    public ProductDispensation() {
        this.setIncidence(Incidence.NEGATIVE);
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date orderDate) {
        this.prescriptionDate = orderDate;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public MobilePatientDispensationInfo getMobilePatientDispensationInfo() {
        return mobilePatientDispensationInfo;
    }

    public void setMobilePatientDispensationInfo(MobilePatientDispensationInfo mobilePatientDispensationInfo) {
        this.mobilePatientDispensationInfo = mobilePatientDispensationInfo;
    }
}
