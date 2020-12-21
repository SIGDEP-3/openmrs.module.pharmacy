package org.openmrs.module.pharmacy.models;

public class DispensationResultDTO {
    private Integer onSite;
    private Integer mobile;
    private Integer adult;
    private Integer child;
    private Integer male;
    private Integer female;
    private Integer pec;
    private Integer ptme;
    private Integer aes;
    private Integer prep;
    private Integer notApplicable;
    private Integer total;

    public DispensationResultDTO() {
    }

    public Integer getOnSite() {
        return onSite;
    }

    public void setOnSite(Integer onSite) {
        this.onSite = onSite;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public Integer getAdult() {
        return adult;
    }

    public void setAdult(Integer adult) {
        this.adult = adult;
    }

    public Integer getChild() {
        return child;
    }

    public void setChild(Integer child) {
        this.child = child;
    }

    public Integer getMale() {
        return male;
    }

    public void setMale(Integer male) {
        this.male = male;
    }

    public Integer getFemale() {
        return female;
    }

    public void setFemale(Integer female) {
        this.female = female;
    }

    public Integer getPec() {
        return pec;
    }

    public void setPec(Integer pec) {
        this.pec = pec;
    }

    public Integer getPtme() {
        return ptme;
    }

    public void setPtme(Integer ptme) {
        this.ptme = ptme;
    }

    public Integer getAes() {
        return aes;
    }

    public void setAes(Integer aes) {
        this.aes = aes;
    }

    public Integer getPrep() {
        return prep;
    }

    public void setPrep(Integer prep) {
        this.prep = prep;
    }

    public Integer getNotApplicable() {
        return notApplicable;
    }

    public void setNotApplicable(Integer notApplicable) {
        this.notApplicable = notApplicable;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
