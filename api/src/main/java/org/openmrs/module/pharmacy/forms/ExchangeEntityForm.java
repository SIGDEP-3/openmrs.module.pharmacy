package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductExchangeEntity;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.UUID;

public class ExchangeEntityForm {
    private Integer productExchangeEntityId;
    private String name;
    private String description;
    private Integer locationInList;
    private Integer locationId;
    private String uuid = UUID.randomUUID().toString();

    public ExchangeEntityForm() {

    }

    public Integer getProductExchangeEntityId() {
        return productExchangeEntityId;
    }

    public void setProductExchangeEntityId(Integer productExchangeEntityId) {
        this.productExchangeEntityId = productExchangeEntityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLocationInList() {
        return locationInList;
    }

    public void setLocationInList(Integer locationInList) {
        this.locationInList = locationInList;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setProductExchangeEntity(ProductExchangeEntity productExchangeEntity) {
        this.setProductExchangeEntityId(productExchangeEntity.getProductExchangeEntityId());
        this.setName(productExchangeEntity.getName());
        this.setDescription(productExchangeEntity.getDescription());
        this.setLocationInList(productExchangeEntity.getLocation().getLocationId());
        this.setUuid(productExchangeEntity.getUuid());
    }

    public ProductExchangeEntity getProductExchangeEntity() {
        ProductExchangeEntity exchangeEntity = new ProductExchangeEntity();
        exchangeEntity.setProductExchangeEntityId(getProductExchangeEntityId());
        exchangeEntity.setDescription(getDescription());
        if (getLocationInList() != null) {
            exchangeEntity.setName(Context.getLocationService().getLocation(getLocationInList()).getName());
        } else {
            exchangeEntity.setName(getName());
        }
        exchangeEntity.setUuid(getUuid());
        exchangeEntity.setLocation(OperationUtils.getUserLocation());
        return exchangeEntity;
    }

}
