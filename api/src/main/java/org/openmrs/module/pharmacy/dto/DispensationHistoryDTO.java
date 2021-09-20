package org.openmrs.module.pharmacy.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DispensationHistoryDTO {
    Integer operationId;
    Date operationDate;
    String patientIdentifier;
    String patientGender;
    Integer patientAge;
    String category;
    String regimen;
    Integer treatmentDays;
    Date treatmentEndDate;
    String products;
    List<ProductHistoryDto> productHistoryDtos = new ArrayList<>();

    public DispensationHistoryDTO() {
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
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

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public List<ProductHistoryDto> getProductHistoryDtos() {
        return productHistoryDtos;
    }

    public void setProductHistoryDtos(List<ProductHistoryDto> productHistoryDtos) {
        String[] productLine = products.split("|");
        for (String line : productLine) {
            String[] productInfo = line.split(":");
            productHistoryDtos.add(new ProductHistoryDto(productInfo[0], productInfo[1], Integer.parseInt(productInfo[3])));
        }
        this.productHistoryDtos = productHistoryDtos;
    }
}
