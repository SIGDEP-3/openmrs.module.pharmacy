package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductOperation;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.api.ProductReceptionService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.ReceptionQuantityMode;

import java.util.Date;
import java.util.UUID;

public abstract class ProductOperationForm {
    private Integer productOperationId;
    private String operationNumber;
    private Integer productProgramId;
    private Date operationDate;
    private Integer locationId;
    private OperationStatus operationStatus;
    private Incidence incidence;
    private String observation;
    private String uuid = UUID.randomUUID().toString();

    public ProductOperationForm() {
        operationStatus = OperationStatus.AWAITING_VALIDATION;
    }

    public Integer getProductOperationId() {
        return productOperationId;
    }

    public void setProductOperationId(Integer productOperationId) {
        this.productOperationId = productOperationId;
    }

    public String getOperationNumber() {
        return operationNumber;
    }

    public void setOperationNumber(String operationNumber) {
        this.operationNumber = operationNumber;
    }

    public Integer getProductProgramId() {
        return productProgramId;
    }

    public void setProductProgramId(Integer productProgramId) {
        this.productProgramId = productProgramId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public OperationStatus getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Incidence getIncidence() {
        return incidence;
    }

    public void setIncidence(Incidence incidence) {
        this.incidence = incidence;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setProductOperation(ProductOperation productOperation) {
        setProductOperationId(productOperation.getProductOperationId());
        setOperationDate(productOperation.getOperationDate());
        setProductProgramId(productOperation.getProductProgram().getProductProgramId());
        setLocationId(productOperation.getLocation().getLocationId());
        setOperationStatus(productOperation.getOperationStatus());
        if (productOperation.getOperationNumber() != null) {
            setOperationNumber(productOperation.getOperationNumber());
        }
        setUuid(productOperation.getUuid());
    }

    public ProductOperation getProductOperation(ProductOperation operation) {
        ProductOperation productOperation = operation;
        if (getProductOperationId() != null) {
            productOperation.setProductOperationId(getProductOperationId());
            productOperation = Context.getService(ProductReceptionService.class).getOneProductReceptionById(getProductOperationId());
        }
        productOperation.setOperationDate(getOperationDate());
        productOperation.setProductProgram(Context.getService(ProductProgramService.class).getOneProductProgramById(getProductProgramId()));
        productOperation.setLocation(Context.getLocationService().getLocation(getLocationId()));
        productOperation.setOperationNumber(getOperationNumber());
        productOperation.setOperationStatus(getOperationStatus());
        productOperation.setUuid(getUuid());
        return productOperation;
    }
}
