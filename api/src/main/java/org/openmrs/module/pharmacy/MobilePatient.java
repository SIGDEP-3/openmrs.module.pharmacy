package org.openmrs.module.pharmacy;

import org.openmrs.Location;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "pharmacy_mobile_patient")
public class MobilePatient extends AbstractPharmacyObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mobile_patient_id")
    private Integer mobilePatientId;

    @Column(name = "age")
    private Integer age;

    @Column(name = "identifier", unique = true)
    private String identifier;

    @Column(name = "gender")
    private String gender;

    @ManyToMany
    @JoinTable(name = "pharmacy_mobile_patient_delivery_members",
            joinColumns = @JoinColumn(name = "mobile_patient_id"),
            inverseJoinColumns = @JoinColumn(name = "dispensing_id"))
    private Set<Dispensing> dispensing;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

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

    public Set<Dispensing> getDispensing() {
        return dispensing;
    }

    public void setDispensing(Set<Dispensing> orderDeliveries) {
        this.dispensing = orderDeliveries;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
