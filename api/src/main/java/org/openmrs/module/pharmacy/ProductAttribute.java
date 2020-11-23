package org.openmrs.module.pharmacy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.Location;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "ProductAttribute")
@Table(name = "pharmacy_product_attribute")
public class ProductAttribute extends AbstractPharmacyObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_attribute_id")
    private Integer productAttributeId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "batch_number", nullable = false, unique = true)
    private String batchNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Override
    public Integer getId() {
        return getProductAttributeId();
    }

    @Override
    public void setId(Integer integer) {
        setProductAttributeId(integer);
    }

    public Integer getProductAttributeId() {
        return productAttributeId;
    }

    public void setProductAttributeId(Integer productAttributeId) {
        this.productAttributeId = productAttributeId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String lotNumber) {
        this.batchNumber = lotNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expireDate) {
        this.expiryDate = expireDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
