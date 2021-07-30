package org.openmrs.module.pharmacy.forms.dispensation;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.MobilePatient;
import org.openmrs.module.pharmacy.entities.MobilePatientDispensationInfo;
import org.openmrs.module.pharmacy.entities.ProductDispensation;
import org.openmrs.module.pharmacy.api.ProductDispensationService;
import org.openmrs.module.pharmacy.api.ProductRegimenService;
import org.openmrs.module.pharmacy.enumerations.Goal;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.PatientType;
import org.openmrs.module.pharmacy.forms.ProductOperationForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ProductDispensationForm extends ProductOperationForm {
    private Integer productRegimenId;
    private Integer productRegimenLine;
    private Goal goal;
    private Integer encounterId;
    private Date prescriptionDate;
    private Integer mobilePatientId;
    private Integer patientId;
    private Integer mobileDispensationInfoId;
    private Integer providerId;
    private String patientIdentifier;
    private Integer treatmentDays;
    private Date treatmentEndDate;
    private Integer age;
    private String gender;
    private PatientType patientType;
    private Integer treatmentDaysLost;

    public ProductDispensationForm() {
        super();
        setIncidence(Incidence.NEGATIVE);
    }

    public Integer getProductRegimenLine() {
        return productRegimenLine;
    }

    public void setProductRegimenLine(Integer productRegimenLine) {
        this.productRegimenLine = productRegimenLine;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public Integer getMobilePatientId() {
        return mobilePatientId;
    }

    public void setMobilePatientId(Integer mobilePatientId) {
        this.mobilePatientId = mobilePatientId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getMobileDispensationInfoId() {
        return mobileDispensationInfoId;
    }

    public void setMobileDispensationInfoId(Integer mobileDispensationInfoId) {
        this.mobileDispensationInfoId = mobileDispensationInfoId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
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

    public Integer getProductRegimenId() {
        return productRegimenId;
    }

    public void setProductRegimenId(Integer productRegimenId) {
        this.productRegimenId = productRegimenId;
    }

    public PatientType getPatientType() {
        return patientType;
    }

    public void setPatientType(PatientType patientType) {
        this.patientType = patientType;
    }

    public Integer getTreatmentDaysLost() {
        return treatmentDaysLost;
    }

    public void setTreatmentDaysLost(Integer treatmentDaysLost) {
        this.treatmentDaysLost = treatmentDaysLost;
    }

    public void setProductDispensation(ProductDispensation productDispensation) {
        super.setProductOperation(productDispensation);
        setPrescriptionDate(productDispensation.getPrescriptionDate());
    }

    public void setMobilePatient(MobilePatient patient) {
        if (patient.getIdentifier() != null) {
            setPatientIdentifier(patient.getIdentifier());
        }
        setPatientId(patient.getMobilePatientId());
        setAge(patient.getAge());
        setGender(patient.getGender());
        setMobilePatientId(patient.getMobilePatientId());
        setPatientType(patient.getPatientType());
    }

    public void setMobileDispensationInfo(MobilePatientDispensationInfo mobileDispensationInfo) {
        setMobileDispensationInfoId(mobileDispensationInfo.getMobileDispensationInfoId());
        setGoal(mobileDispensationInfo.getGoal());
        setProductRegimenId(mobileDispensationInfo.getProductRegimen().getProductRegimenId());
        if (mobileDispensationInfo.getProvider().getProviderId() != null) {
            setProviderId(mobileDispensationInfo.getProvider().getProviderId());
        }
        setTreatmentEndDate(mobileDispensationInfo.getTreatmentEndDate());

        setMobilePatientId(mobileDispensationInfo.getMobilePatient().getMobilePatientId());
        setPatientIdentifier(mobileDispensationInfo.getMobilePatient().getIdentifier());
        setAge(mobileDispensationInfo.getMobilePatient().getAge());
        setGender(mobileDispensationInfo.getMobilePatient().getGender());
        setPatientType(mobileDispensationInfo.getMobilePatient().getPatientType());

        setProductDispensation(mobileDispensationInfo.getDispensation());
        setProductRegimenLine(mobileDispensationInfo.getRegimenLine());
    }

    public ProductDispensation getProductDispensation() {
        ProductDispensation productDispensation = (ProductDispensation) super.getProductOperation(new ProductDispensation());
        productDispensation.setPrescriptionDate(getPrescriptionDate());
        return productDispensation;
    }

    public MobilePatientDispensationInfo getMobileDispensationInfo() {
        MobilePatientDispensationInfo info = new MobilePatientDispensationInfo();
        if (getMobileDispensationInfoId() != null) {
            info = dispensationService().getOneMobilePatientDispensationInfoId(getMobileDispensationInfoId());
        }
        if (getGoal() != null) {
            info.setGoal(getGoal());
        }
        info.setLocation(OperationUtils.getUserLocation());
//        if (getProductRegimenId() != null) {
//            info.setProductRegimen(regimenService().getOneProductRegimenByConceptId(getProductRegimenId()));
//        }
        if (getProviderId() != null) {
            info.setProvider(Context.getProviderService().getProvider(getProviderId()));
        }
        info.setTreatmentDays(getTreatmentDays());
        if (getProductRegimenId() != null) {
            info.setProductRegimen(regimenService().getOneProductRegimenById(getProductRegimenId()));
            info.setTreatmentEndDate(getTreatmentEndDate());
            info.setRegimenLine(getProductRegimenLine());
        }
        info.setMobilePatient(getMobilePatient());
        return info;
    }

    public Encounter getEncounter() {
        Encounter encounter = new Encounter();
        if (getEncounterId() == null) {
            encounter.setEncounterDatetime(getOperationDate());
            encounter.setPatient(Context.getPatientService().getPatient(getPatientId()));
            encounter.setLocation(OperationUtils.getUserLocation());
            encounter.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid("cfb217pd-4af2-40f4-9d6e-0d1b38d2af86"));
            if (getProviderId() != null) {
                encounter.addProvider(Context.getEncounterService().getEncounterRole(1), Context.getProviderService().getProvider(getProviderId()));
            }
            encounter.addObs(getDispensationDateObs());
            encounter.addObs(getDispensationGoalObs());
            if (getProductRegimenId() != null) {
                encounter.addObs(getDispensationRegimenObs());
                encounter.addObs(getDispensationTreatmentEndDateObs());
            }
            encounter.addObs(getDispensationRegimenLineObs());
            encounter.addObs(getDispensationTreatmentDaysObs());

        } else {
            encounter = Context.getEncounterService().getEncounter(getEncounterId());
            if (encounter != null) {
                if (getProviderId() != null) {
                    encounter.addProvider(null, Context.getProviderService().getProvider(getProviderId()));
                }
                Set<Obs> obsSet = new HashSet<Obs>();
                for (Obs obs : encounter.getAllObs()) {
                    if (obs.getConcept().getConceptId().equals(getConceptIdInGlobalProperties("Regimen"))) {
                        obs.setValueCoded(regimenService().getOneProductRegimenById(getProductRegimenId()).getConcept());
                        obsSet.add(obs);
                    } else if (obs.getConcept().getConceptId().equals(getConceptIdInGlobalProperties("RegimenLine"))) {
                        if (getProductRegimenLine() == 1) {
                            obs.setValueCoded(Context.getConceptService().getConcept(164730));
                        } else if (getProductRegimenLine() == 2) {
                            obs.setValueCoded(Context.getConceptService().getConcept(164732));
                        } else if (getProductRegimenLine() == 3) {
                            obs.setValueCoded(Context.getConceptService().getConcept(164734));
                        }
                        obsSet.add(obs);
                    } else if (obs.getConcept().getConceptId().equals(getConceptIdInGlobalProperties("Goal"))) {
                        obs.setValueText(getGoal().name());
                        obsSet.add(obs);
                    } else if (obs.getConcept().getConceptId().equals(getConceptIdInGlobalProperties("TreatmentDays"))) {
                        obs.setValueNumeric(getTreatmentDays().doubleValue());
                        obsSet.add(obs);
                    } else if (obs.getConcept().getConceptId().equals(getConceptIdInGlobalProperties("TreatmentEndDate"))) {
                        if (getProductRegimenId() != null /*&& getProductRegimenId() != 105281*/) {
                            obs.setValueDate(getTreatmentEndDate());
                            obsSet.add(obs);
                        }
                    }
                }
                encounter.setObs(obsSet);
            }
        }

        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        setEncounterId(encounter.getEncounterId());
        setAge(encounter.getPatient().getAge());
        setPatientIdentifier(encounter.getPatient().getPatientIdentifier().getIdentifier());
        setGender(encounter.getPatient().getGender());
        setPatientId(encounter.getPatient().getPatientId());

        for (Obs obs : encounter.getAllObs()) {
            if (obs.getConcept().getConceptId().equals(getConceptIdInGlobalProperties("Regimen"))) {
                setProductRegimenId(regimenService().getOneProductRegimenByConceptId(obs.getConcept().getConceptId()).getProductRegimenId());
            } else if (obs.getConcept().getConceptId().equals(getConceptIdInGlobalProperties("RegimenLine"))) {
                if (obs.getValueCoded().getConceptId().equals(164730)) {
                    setProductRegimenLine(1);
                } else if (obs.getValueCoded().getConceptId().equals(164732)) {
                    setProductRegimenLine(2);
                } else if (obs.getValueCoded().getConceptId().equals(164734)) {
                    setProductRegimenLine(3);
                }
            } else if (obs.getConcept().getConceptId().equals(getConceptIdInGlobalProperties("Goal"))) {
                setGoal(Goal.valueOf(obs.getValueText()));
            } else if (obs.getConcept().getConceptId().equals(getConceptIdInGlobalProperties("TreatmentDays"))) {
                setTreatmentDays(obs.getValueNumeric().intValue());
            } else if (obs.getConcept().getConceptId().equals(getConceptIdInGlobalProperties("TreatmentEndDate"))) {
                setTreatmentEndDate(obs.getValueDate());
            }
        }
    }

    public MobilePatient getMobilePatient() {
        MobilePatient patient = dispensationService().getOneMobilePatientById(getMobilePatientId());
        if (patient != null) {
            patient.setAge(getAge());
            patient.setGender(getGender());
            patient.setIdentifier(getPatientIdentifier());
            patient.setLocation(OperationUtils.getUserLocation());
            patient.setPatientType(getPatientType());
        }
        return patient;
    }

    private Obs getDispensationRegimenObs() {
        Obs obs = new Obs();
        obs.setConcept(Context.getConceptService().getConcept(getConceptIdInGlobalProperties("Regimen")));
        obs.setLocation(OperationUtils.getUserLocation());
        obs.setPerson(Context.getPersonService().getPerson(getPatientId()));
        obs.setValueCoded(Context.getService(ProductRegimenService.class).getOneProductRegimenById(getProductRegimenId()).getConcept());
        return obs;
    }

    private Obs getDispensationRegimenLineObs() {
        Obs obs = new Obs();
        obs.setConcept(Context.getConceptService().getConcept(getConceptIdInGlobalProperties("RegimenLine")));
        obs.setLocation(OperationUtils.getUserLocation());
        obs.setPerson(Context.getPersonService().getPerson(getPatientId()));
        if (getProductRegimenLine() == 1) {
            obs.setValueCoded(Context.getConceptService().getConcept(164730));
        } else if (getProductRegimenLine() == 2) {
            obs.setValueCoded(Context.getConceptService().getConcept(164732));
        } else if (getProductRegimenLine() == 3) {
            obs.setValueCoded(Context.getConceptService().getConcept(164734));
        }
//        obs.setValueCoded(Context.getService(ProductRegimenService.class).getOneProductRegimenById(getProductRegimenId()).getConcept());
        return obs;
    }

    // Préciser la ligne du régime ARV (164767)	Answers:	1 -> Ligne 1 du régime ARV (164730)
    //2 -> Ligne 2 du régime ARV (164732)
    //3 -> Ligne 3 du régime ARV (164734)

    private Obs getDispensationTreatmentDaysObs() {
        Obs obs = new Obs();
        obs.setConcept(Context.getConceptService().getConcept(getConceptIdInGlobalProperties("TreatmentDays")));
        obs.setLocation(OperationUtils.getUserLocation());
        obs.setPerson(Context.getPersonService().getPerson(getPatientId()));
        obs.setValueNumeric(getTreatmentDays().doubleValue());
        return obs;
    }

    private Obs getDispensationTreatmentEndDateObs() {
        Obs obs = new Obs();
        obs.setConcept(Context.getConceptService().getConcept(getConceptIdInGlobalProperties("TreatmentEndDate")));
        obs.setLocation(OperationUtils.getUserLocation());
        obs.setPerson(Context.getPersonService().getPerson(getPatientId()));
        obs.setValueDate(getTreatmentEndDate());
        return obs;
    }

    private Obs getDispensationGoalObs() {
        Obs obs = new Obs();
        obs.setConcept(Context.getConceptService().getConcept(getConceptIdInGlobalProperties("Goal")));
        obs.setLocation(OperationUtils.getUserLocation());
        obs.setPerson(Context.getPersonService().getPerson(getPatientId()));
        obs.setValueText(getGoal().name());
        return obs;
    }

    private Obs getDispensationDateObs() {
        Obs obs = new Obs();
        obs.setConcept(Context.getConceptService().getConcept(165010));
        obs.setLocation(OperationUtils.getUserLocation());
        obs.setPerson(Context.getPersonService().getPerson(getPatientId()));
        obs.setValueDate(getOperationDate());
        return obs;
    }

    private ProductRegimenService regimenService() {
        return Context.getService(ProductRegimenService.class);
    }

    private ProductDispensationService dispensationService() {
        return Context.getService(ProductDispensationService.class);
    }

    private Integer getConceptIdInGlobalProperties(String property) {
        String value = Context.getAdministrationService().getGlobalProperty("pharmacy.dispensation"+ property + "Concept");
        if (!value.isEmpty()) {
            return Integer.parseInt(value);
        }
        return null;
    }


    public void setPatient(Patient patient) {
        setGender(patient.getGender());
        setAge(patient.getAge());
        setPatientIdentifier(patient.getPatientIdentifier().getIdentifier());
        setPatientId(patient.getPatientId());
        setPatientType(PatientType.ON_SITE);
    }

    public Patient getPatient() {
        return Context.getPatientService().getPatient(getPatientId());
    }
}
