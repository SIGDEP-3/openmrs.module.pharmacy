package org.openmrs.module.pharmacy.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductDispensation")
//@DiscriminatorValue("ProductDispensation")
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

    @Transient
    private String identifier;

    @Transient
    private Integer age;

    @Transient
    private String gender;

    @Transient
    private Integer treatmentDays;

    @Transient
    private Date treatmentEndDate;

    @Transient
    private String regimen;

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
//        setAge(encounter.getPatient().getAge());
//        setGender(encounter.getPatient().getGender());
//        setIdentifier(encounter.getPatient().getPatientIdentifier().getIdentifier());
//        setDispensationDate(encounter.getEncounterDatetime());
//        for (Obs obs : encounter.getObs()) {
//            if (obs.getConcept().equals(OperationUtils.getConceptInGlobalProperties("Regimen"))) {
//                setRegimen(obs.getValueCodedName().getName());
//            } else if(obs.getConcept().equals(OperationUtils.getConceptInGlobalProperties("TreatmentDays"))) {
//                setTreatmentDays(obs.getValueNumeric().intValue());
//            } else if(obs.getConcept().equals(OperationUtils.getConceptInGlobalProperties("TreatmentEndDate"))) {
//                setTreatmentEndDate(obs.getValueDate());
//            }
//        }
    }

    public MobilePatientDispensationInfo getMobilePatientDispensationInfo() {
        return mobilePatientDispensationInfo;
    }

    public void setMobilePatientDispensationInfo(MobilePatientDispensationInfo mobilePatientDispensationInfo) {
        this.mobilePatientDispensationInfo = mobilePatientDispensationInfo;
//        setIdentifier(mobilePatientDispensationInfo.getMobilePatient().getIdentifier());
//        setAge(mobilePatientDispensationInfo.getMobilePatient().getAge());
//        setGender(mobilePatientDispensationInfo.getMobilePatient().getGender());
//        setDispensationDate(getOperationDate());
//        setRegimen(mobilePatientDispensationInfo.getProductRegimen().getConcept().getName().getName());
//        setTreatmentDays(mobilePatientDispensationInfo.getTreatmentDays());
//        setTreatmentEndDate(mobilePatientDispensationInfo.getTreatmentEndDate());
    }

    public String getIdentifier() {
        if (encounter != null) {
            String identifier = encounter.getPatient().getPatientIdentifier().getIdentifier();
            if (identifier != null) {
                setIdentifier(identifier);
            }
        } else {
            MobilePatient mobilePatient = mobilePatientDispensationInfo.getMobilePatient();
            if (mobilePatient != null) {
                setIdentifier(mobilePatient.getIdentifier());
            } else {
                setIdentifier(mobilePatientDispensationInfo.getPatient().getPatientIdentifier().getIdentifier());
            }
        }
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getAge() {
        if (encounter != null) {
            setAge(encounter.getPatient().getAge());
        } else {
            MobilePatient mobilePatient = mobilePatientDispensationInfo.getMobilePatient();
            if (mobilePatient != null) {
                setAge(mobilePatient.getAge());
            } else {
                setAge(mobilePatientDispensationInfo.getPatient().getAge());
            }
        }
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        if (encounter != null) {
            setGender(encounter.getPatient().getGender());
        } else {
            MobilePatient mobilePatient = mobilePatientDispensationInfo.getMobilePatient();
            if (mobilePatient != null) {
                setGender(mobilePatient.getGender());
            } else {
                setGender(mobilePatientDispensationInfo.getPatient().getGender());
            }
        }
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getTreatmentDays() {
        if (encounter != null) {
            for (Obs obs : encounter.getObs()) {
                if(obs.getConcept().equals(OperationUtils.getConceptInGlobalProperties("TreatmentDays"))) {
                    setTreatmentDays(obs.getValueNumeric().intValue());
                    break;
                }
            }
        } else {
            setTreatmentDays(mobilePatientDispensationInfo.getTreatmentDays());
        }
        return treatmentDays;
    }

    public void setTreatmentDays(Integer treatmentDays) {
        this.treatmentDays = treatmentDays;
    }

    public Date getTreatmentEndDate() {
        if (encounter != null) {
            for (Obs obs : encounter.getObs()) {
                if(obs.getConcept().equals(OperationUtils.getConceptInGlobalProperties("TreatmentEndDate"))) {
                    setTreatmentEndDate(obs.getValueDate());
                    break;
                }
            }
        } else {
            setTreatmentEndDate(mobilePatientDispensationInfo.getTreatmentEndDate());
        }
        return treatmentEndDate;
    }

    public void setTreatmentEndDate(Date treatmentEndDate) {
        this.treatmentEndDate = treatmentEndDate;
    }

    public String getRegimen() {
        if (encounter != null) {
            for (Obs obs : encounter.getObs()) {
                if(obs.getConcept().equals(OperationUtils.getConceptInGlobalProperties("Regimen"))) {
                    setRegimen(obs.getValueCoded().getName().getName());
                    break;
                }
            }
        } else {
            if (mobilePatientDispensationInfo.getProductRegimen() != null) {
                setRegimen(mobilePatientDispensationInfo.getProductRegimen().getConcept().getName().getName());
            }
        }
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }
}
