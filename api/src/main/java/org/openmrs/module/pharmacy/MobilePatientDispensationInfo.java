package org.openmrs.module.pharmacy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;
import org.openmrs.Provider;
import org.openmrs.module.pharmacy.enumerations.Goal;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "MobilePatientDispensationInfo")
@Table(name = "pharmacy_mobile_patient_dispensation_info")
public class MobilePatientDispensationInfo extends AbstractPharmacyData {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mobile_dispensation_info_id")
    private Integer mobileDispensationInfoId;

    @ManyToOne
    @JoinColumn(name = "regimen_id")
    private ProductRegimen productRegimen;

    @Column(name = "treatment_days")
    private Integer treatmentDays;

    @ManyToOne
    @JoinColumn(name = "mobile_patient_id", nullable = false)
    private MobilePatient mobilePatient;

    @ManyToOne
    @JoinColumn(name = "product_dispensation_id", nullable = false)
    private ProductDispensation dispensation;

    @Column(name = "goal")
    private Goal goal;

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
}
