package org.openmrs.module.pharmacy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;
import org.openmrs.Provider;
import org.openmrs.module.pharmacy.enumerations.Goal;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "MobilePatientDispensationInfo")
@Table(name = "pharmacy_mobile_patient_dispensation_info")
public class MobilePatientDispensationInfo extends AbstractPharmacyData {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "mobile_dispensation_info_id")
    private Integer mobileDispensationInfoId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "mobile_dispensation_info_id", nullable = false)
    private ProductDispensation dispensation;

    @ManyToOne
    @JoinColumn(name = "regimen_id")
    private ProductRegimen productRegimen;

    @Column(name = "treatment_days")
    private Integer treatmentDays;

    @ManyToOne
    @JoinColumn(name = "mobile_patient_id", nullable = false)
    private MobilePatient mobilePatient;

    @Column(name = "goal")
    private Goal goal;

    @Column(name = "treatment_end_date", nullable = false)
    private Date treatmentEndDate;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public MobilePatientDispensationInfo() {
    }

    public Integer getMobileDispensationInfoId() {
        return mobileDispensationInfoId;
    }

    public void setMobileDispensationInfoId(Integer mobileDispensationInfo) {
        this.mobileDispensationInfoId = mobileDispensationInfo;
    }

    public ProductRegimen getProductRegimen() {
        return productRegimen;
    }

    public void setProductRegimen(ProductRegimen productRegimen) {
        this.productRegimen = productRegimen;
    }

    public MobilePatient getMobilePatient() {
        return mobilePatient;
    }

    public void setMobilePatient(MobilePatient mobilePatient) {
        this.mobilePatient = mobilePatient;
    }

    public ProductDispensation getDispensation() {
        return dispensation;
    }

    public void setDispensation(ProductDispensation dispensation) {
        this.dispensation = dispensation;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
}
