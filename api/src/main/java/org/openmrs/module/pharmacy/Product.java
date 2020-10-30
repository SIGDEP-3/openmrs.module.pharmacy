package org.openmrs.module.pharmacy;

import javax.persistence.*;

@Entity
@Table(name = "pharmacy_product")
public class Product extends AbstractPharmacyObject {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "retail_name", nullable = false, unique = true)
    private String retailName;

    @Column(name = "wholesale_name", nullable = false, unique = true)
    private String wholesaleName;

    @ManyToOne
    @JoinColumn(name = "product_retail_unit", nullable = false)
    private ProductUnit productRetailUnit;

    @ManyToOne
    @JoinColumn(name = "product_wholesale_unit", nullable = false)
    private ProductUnit productWholesaleUnit;

    @Column(name = "unit_conversion", nullable = false)
    private Double unitConversion;

    @Override
    public Integer getId() {
        return productId;
    }

    @Override
    public void setId(Integer integer) {
        productId = integer;
    }

    public Product() {
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRetailName() {
        return retailName;
    }

    public void setRetailName(String detailedName) {
        this.retailName = detailedName;
    }

    public String getWholesaleName() {
        return wholesaleName;
    }

    public void setWholesaleName(String packagedName) {
        this.wholesaleName = packagedName;
    }

    public ProductUnit getProductRetailUnit() {
        return productRetailUnit;
    }

    public void setProductRetailUnit(ProductUnit productDetailedUnit) {
        this.productRetailUnit = productDetailedUnit;
    }

    public ProductUnit getProductWholesaleUnit() {
        return productWholesaleUnit;
    }

    public void setProductWholesaleUnit(ProductUnit productPackagedUnit) {
        this.productWholesaleUnit = productPackagedUnit;
    }

    public Double getUnitConversion() {
        return unitConversion;
    }

    public void setUnitConversion(Double unitConversion) {
        this.unitConversion = unitConversion;
    }
}
