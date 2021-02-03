package org.openmrs.module.pharmacy.models;

public class DispensationTransformationResultDTO {
    private Integer totalPatient;
    private Integer totalDispensation;
    private Integer transformed;
    private Integer notTransformed;

    public DispensationTransformationResultDTO() {
        totalPatient = 0;
        transformed = 0;
        notTransformed = 0;
        totalDispensation = 0;
    }

    public Integer getTotalPatient() {
        return totalPatient;
    }

    public void setTotalPatient(Integer totalPatient) {
        this.totalPatient += totalPatient;
    }

    public Integer getTransformed() {
        return transformed;
    }

    public void setTransformed(Integer transformed) {
        this.transformed += transformed;
    }

    public Integer getNotTransformed() {
        return notTransformed;
    }

    public void setNotTransformed(Integer notTransformed) {
        this.notTransformed += notTransformed;
    }

    public Integer getTotalDispensation() {
        return totalDispensation;
    }

    public void setTotalDispensation(Integer totalDispensation) {
        this.totalDispensation += totalDispensation;
    }

    @Override
    public String toString() {
        return "DispensationTransformationResultDTO{" +
                "totalPatient=" + totalPatient +
                ", totalDispensation=" + totalDispensation +
                ", transformed=" + transformed +
                ", notTransformed=" + notTransformed +
                '}';
    }
}
