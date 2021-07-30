package org.openmrs.module.pharmacy.forms.dispensation;

import org.openmrs.module.pharmacy.enumerations.DispensationType;
import org.openmrs.module.pharmacy.enumerations.PatientType;

public class FindPatientForm {
    private Integer productProgramId;
    private String patientIdentifier;
    private DispensationType dispensationType;
    private PatientType patientType;

    public FindPatientForm() {
    }

    public Integer getProductProgramId() {
        return productProgramId;
    }

    public void setProductProgramId(Integer productProgramId) {
        this.productProgramId = productProgramId;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    public DispensationType getDispensationType() {
        return dispensationType;
    }

    public void setDispensationType(DispensationType dispensationType) {
        this.dispensationType = dispensationType;
    }

    public PatientType getPatientType() {
        return patientType;
    }

    public void setPatientType(PatientType patientType) {
        this.patientType = patientType;
    }
}
