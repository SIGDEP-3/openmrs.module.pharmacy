package org.openmrs.module.pharmacy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;
import org.openmrs.module.pharmacy.enumerations.PatientType;

import javax.persistence.*;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "MobilePatient")
@Table(name = "pharmacy_mobile_patient")
public class MobilePatient extends AbstractPharmacyObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mobile_patient_id")
    private Integer mobilePatientId;

    @Column(name = "age")
    private Integer age;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "gender")
    private String gender;

    @Column(name = "patient_type", nullable = false)
    private PatientType patientType;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

//    @ManyToMany
//    @JoinTable(name = "pharmacy_mobile_patient_product_dispensing_members",
//            joinColumns = @JoinColumn(name = "mobile_patient_id"),
//            inverseJoinColumns = @JoinColumn(name = "product_dispensing_id"))
//    private Set<ProductDispensation> productDispensation;

    @OneToMany(mappedBy = "mobilePatient")
    private Set<MobilePatientDispensationInfo> mobilePatientDispensationInfos;

    public MobilePatient() {
    }

    @Override
    public Integer getId() {
        return getMobilePatientId();
    }

    @Override
    public void setId(Integer integer) {
        setMobilePatientId(integer);
    }

    public Integer getMobilePatientId() {
        return mobilePatientId;
    }

    public void setMobilePatientId(Integer mobilePatientId) {
        this.mobilePatientId = mobilePatientId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<MobilePatientDispensationInfo> getMobilePatientDispensationInfos() {
        return mobilePatientDispensationInfos;
    }

    public void setMobilePatientDispensationInfos(Set<MobilePatientDispensationInfo> mobilePatientDispensationInfos) {
        this.mobilePatientDispensationInfos = mobilePatientDispensationInfos;
    }

    public PatientType getPatientType() {
        return patientType;
    }

    public void setPatientType(PatientType patientType) {
        this.patientType = patientType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
