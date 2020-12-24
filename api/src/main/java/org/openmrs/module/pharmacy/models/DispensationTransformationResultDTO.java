package org.openmrs.module.pharmacy.models;

public class DispensationTransformationResultDTO {
    private Integer total;
    private Integer transformed;
    private Integer notTransformed;

    public DispensationTransformationResultDTO() {
        total= 0;
        transformed = 0;
        notTransformed = 0;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTransformed() {
        return transformed;
    }

    public void setTransformed(Integer transformed) {
        this.transformed = transformed;
    }

    public Integer getNotTransformed() {
        return notTransformed;
    }

    public void setNotTransformed(Integer notTransformed) {
        this.notTransformed = notTransformed;
    }
}
