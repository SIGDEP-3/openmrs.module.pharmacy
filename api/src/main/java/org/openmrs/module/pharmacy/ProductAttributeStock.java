package org.openmrs.module.pharmacy;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ProductAttributeStock")
@Table(name = "pharmacy_product_attribute_stock")
public class ProductAttributeStock extends AbstractPharmacyObject {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_attribute_stock_id")
    private Integer productAttributeStockId;

    @ManyToOne
    @JoinColumn(name = "product_attirbute_id", nullable = false)
    private ProductAttribute productAttribute;

    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;

    @Column(name = "dateCreated", nullable = false)
    private Date dateCreated;
}
