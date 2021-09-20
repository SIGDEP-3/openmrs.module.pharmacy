package org.openmrs.module.pharmacy.dto;

public class ProductHistoryDto {
    private String code;
    private String name;
    private Integer quantity;

    public ProductHistoryDto(String code, String name, Integer quantity) {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
    }

    public ProductHistoryDto() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
