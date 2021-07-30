package org.openmrs.module.pharmacy.dto;

import java.io.Serializable;
import java.util.Date;

public class ProductReceptionListDTO implements Serializable {
    private Integer productOperationId;
    private Integer productReturnedOperationId;
    private String productSupplier;
    private String programName;
    private String operationNumber;
    private Date operationDate;
    private String operationStatus;
    private String productReturnedOperationStatus;
    private String receptionMode;
    private Boolean canReturn;
    private Integer numberOfLine;

    public ProductReceptionListDTO() {
    }

    public Integer getProductOperationId() {
        return productOperationId;
    }

    public void setProductOperationId(Integer productOperationId) {
        this.productOperationId = productOperationId;
    }

    public Integer getProductReturnedOperationId() {
        return productReturnedOperationId;
    }

    public void setProductReturnedOperationId(Integer productReturnedOperationId) {
        this.productReturnedOperationId = productReturnedOperationId;
    }

    public String getProductSupplier() {
        return productSupplier;
    }

    public void setProductSupplier(String productSupplier) {
        this.productSupplier = productSupplier;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getReceptionMode() {
        return receptionMode;
    }

    public void setReceptionMode(String receptionMode) {
        this.receptionMode = receptionMode;
    }

    public Boolean getCanReturn() {
        return canReturn;
    }

    public void setCanReturn(Boolean canReturn) {
        this.canReturn = canReturn;
    }

    public Integer getNumberOfLine() {
        return numberOfLine;
    }

    public void setNumberOfLine(Integer numberOfLine) {
        this.numberOfLine = numberOfLine;
    }

    public String getProductReturnedOperationStatus() {
        return productReturnedOperationStatus;
    }

    public void setProductReturnedOperationStatus(String productReturnedOperationStatus) {
        this.productReturnedOperationStatus = productReturnedOperationStatus;
    }
}
