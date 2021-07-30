package org.openmrs.module.pharmacy.dto;

import java.io.Serializable;
import java.util.Date;

public class ProductMovementHistoryDTO implements Serializable {
    private Date movementDate;
    private String movementType;
    private Integer quantity;
    private Integer incidence;
    private Integer quantityAtTime;

    public ProductMovementHistoryDTO() {
    }

    public Date getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(Date movementDate) {
        this.movementDate = movementDate;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getIncidence() {
        return incidence;
    }

    public void setIncidence(Integer incidence) {
        this.incidence = incidence;
    }

    public Integer getQuantityAtTime() {
        return quantityAtTime;
    }

    public void setQuantityAtTime(Integer quantityAtTime) {
        this.quantityAtTime = quantityAtTime;
    }
}
