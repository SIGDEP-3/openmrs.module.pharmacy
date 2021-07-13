package org.openmrs.module.pharmacy.models;

import org.openmrs.Provider;
import org.openmrs.module.pharmacy.MobilePatient;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductRegimen;
import org.openmrs.module.pharmacy.enumerations.Goal;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.PatientType;

import java.util.Date;

public class DispensationHeaderDTO {
    private Integer productOperationId;
    private ProductProgram productProgram;
    private Date operationDate;
    private ProductRegimen productRegimen;
    private Goal goal;
    private Date prescriptionDate;
    private Provider provider;
    private String patientIdentifier;
    private Integer treatmentDays;
    private Integer productRegimenLine;
    private Date treatmentEndDate;
    private Integer age;
    private String gender;
    private PatientType patientType;
    private Date lastDispensationDate;
    private ProductRegimen lastDispensationProductRegimen;
    private Integer lastDispensationTreatmentDays;
    private Integer lastProductRegimenLine;
    private Date getLastDispensationTreatmentEndDate;
    private OperationStatus operationStatus;
    private MobilePatient mobilePatient;

    public DispensationHeaderDTO() {
    }

    public Integer getProductOperationId() {
        return productOperationId;
    }

    public void setProductOperationId(Integer productOperationId) {
        this.productOperationId = productOperationId;
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

    public ProductRegimen getProductRegimen() {
        return productRegimen;
    }

    public void setProductRegimen(ProductRegimen productRegimen) {
        this.productRegimen = productRegimen;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public PatientType getPatientType() {
        return patientType;
    }

    public void setPatientType(PatientType patientType) {
        this.patientType = patientType;
    }

    public Date getLastDispensationDate() {
        return lastDispensationDate;
    }

    public void setLastDispensationDate(Date lastDispensationDate) {
        this.lastDispensationDate = lastDispensationDate;
    }

    public ProductRegimen getLastDispensationProductRegimen() {
        return lastDispensationProductRegimen;
    }

    public void setLastDispensationProductRegimen(ProductRegimen lastDispensationProductRegimen) {
        this.lastDispensationProductRegimen = lastDispensationProductRegimen;
    }

    public Integer getLastDispensationTreatmentDays() {
        return lastDispensationTreatmentDays;
    }

    public void setLastDispensationTreatmentDays(Integer lastDispensationTreatmentDays) {
        this.lastDispensationTreatmentDays = lastDispensationTreatmentDays;
    }

    public Date getGetLastDispensationTreatmentEndDate() {
        return getLastDispensationTreatmentEndDate;
    }

    public void setGetLastDispensationTreatmentEndDate(Date getLastDispensationTreatmentEndDate) {
        this.getLastDispensationTreatmentEndDate = getLastDispensationTreatmentEndDate;
    }

    public void setOperationStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }

    public OperationStatus getOperationStatus() {
        return operationStatus;
    }

    public MobilePatient getMobilePatient() {
        return mobilePatient;
    }

    public void setMobilePatient(MobilePatient mobilePatient) {
        this.mobilePatient = mobilePatient;
    }

    public Integer getProductRegimenLine() {
        return productRegimenLine;
    }

    public void setProductRegimenLine(Integer productRegimenLine) {
        this.productRegimenLine = productRegimenLine;
    }

    public Integer getLastProductRegimenLine() {
        return lastProductRegimenLine;
    }

    public void setLastProductRegimenLine(Integer lastProductRegimenLine) {
        this.lastProductRegimenLine = lastProductRegimenLine;
    }
}
