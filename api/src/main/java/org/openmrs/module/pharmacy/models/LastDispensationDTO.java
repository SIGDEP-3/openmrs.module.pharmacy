package org.openmrs.module.pharmacy.models;

import org.openmrs.Obs;
import org.openmrs.module.pharmacy.ProductDispensation;
import org.openmrs.module.pharmacy.enumerations.Goal;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.io.Serializable;
import java.util.Date;

public class LastDispensationDTO implements Serializable {
    private Date dispensationDate;
    private String regimen;
    private Integer treatmentDays;
    private Date treatmentEndDate;
    private String regimenLine;

    public LastDispensationDTO() {
    }

    public Date getDispensationDate() {
        return dispensationDate;
    }

    public void setDispensationDate(Date dispensationDate) {
        this.dispensationDate = dispensationDate;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
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

    public LastDispensationDTO setDispensation(ProductDispensation dispensation) {
        if (dispensation.getEncounter() != null) {
            for (Obs obs : dispensation.getEncounter().getAllObs()) {
                if (obs.getConcept().getConceptId().equals(OperationUtils.getConceptIdInGlobalProperties("Regimen"))) {
                    setRegimen(obs.getValueCoded().getName().getName());
                } else if (obs.getConcept().getConceptId().equals(OperationUtils.getConceptIdInGlobalProperties("RegimenLine"))) {
                    if (obs.getValueCoded().getConceptId().equals(164730)) {
                        setRegimenLine("Ligne 1");
                    } else if (obs.getValueCoded().getConceptId().equals(164732)) {
                        setRegimenLine("Ligne 2");
                    } else if (obs.getValueCoded().getConceptId().equals(164734)) {
                        setRegimenLine("Ligne 3");
                    }
                } else if (obs.getConcept().getConceptId().equals(OperationUtils.getConceptIdInGlobalProperties("TreatmentDays"))) {
                    setTreatmentDays(obs.getValueNumeric().intValue());
                } else if (obs.getConcept().getConceptId().equals(OperationUtils.getConceptIdInGlobalProperties("TreatmentEndDate"))) {
                    setTreatmentEndDate(obs.getValueDate());
                }
            }
        } else {
            setRegimen(dispensation.getMobilePatientDispensationInfo().getProductRegimen().getConcept().getName().getName());
            setTreatmentDays(dispensation.getMobilePatientDispensationInfo().getTreatmentDays());
            setTreatmentEndDate(dispensation.getMobilePatientDispensationInfo().getTreatmentEndDate());
            setRegimenLine("Ligne " + dispensation.getMobilePatientDispensationInfo().getRegimenLine());
        }

        setDispensationDate(dispensation.getOperationDate());
        return this;
    }

    public String getRegimenLine() {
        return regimenLine;
    }

    public void setRegimenLine(String regimenLine) {
        this.regimenLine = regimenLine;
    }
}
