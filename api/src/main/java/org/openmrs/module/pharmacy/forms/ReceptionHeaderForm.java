package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.ReceptionQuantityMode;

import java.util.Date;
import java.util.UUID;

public class ReceptionHeaderForm {
    private Integer productOperationId;
    private String operationNumber;
    private Integer productProgramId;
    private Date operationDate;
    private Integer locationId;
    private OperationStatus operationStatus;
    private Incidence incidence;
    private String observation;
    private Integer productSupplierId;
    private ReceptionQuantityMode receptionQuantityMode;
    private String uuid = UUID.randomUUID().toString();

    public ReceptionHeaderForm() {
        incidence = Incidence.POSITIVE;
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

    public Integer getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(Integer productSupplierId) {
        this.productSupplierId = productSupplierId;
    }

    public ReceptionQuantityMode getReceptionQuantityMode() {
        return receptionQuantityMode;
    }

    public void setReceptionQuantityMode(ReceptionQuantityMode receptionQuantityMode) {
        this.receptionQuantityMode = receptionQuantityMode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setProductReception(ProductReception productReception) {
        setProductOperationId(productReception.getProductOperationId());
        setOperationDate(productReception.getOperationDate());
        setProductProgramId(productReception.getProductProgram().getProductProgramId());
        setProductSupplierId(productReception.getProductSupplier().getProductSupplierId());
        setObservation(productReception.getObservation());
        setReceptionQuantityMode(productReception.getReceptionQuantityMode());
        setLocationId(productReception.getLocation().getLocationId());
        setOperationStatus(productReception.getOperationStatus());
        if (productReception.getOperationNumber() != null) {
            setOperationNumber(productReception.getOperationNumber());
        }
        setUuid(productReception.getUuid());
    }

    public ProductReception getProductReception() {
        ProductReception productReception = new ProductReception();
        if (getProductOperationId() != null) {
            productReception.setProductOperationId(getProductOperationId());
            productReception = Context.getService(PharmacyService.class).getOneProductReceptionById(getProductOperationId());
        }
        productReception.setOperationDate(getOperationDate());
        productReception.setProductProgram(Context.getService(PharmacyService.class).getOneProductProgramById(getProductProgramId()));
        productReception.setProductSupplier(Context.getService(PharmacyService.class).getOneProductSupplierById(getProductSupplierId()));
        productReception.setReceptionQuantityMode(getReceptionQuantityMode());
        productReception.setLocation(Context.getLocationService().getLocation(getLocationId()));
        productReception.setOperationNumber(getOperationNumber());
        productReception.setOperationStatus(getOperationStatus());
        productReception.setUuid(getUuid());
        return productReception;
    }
}
