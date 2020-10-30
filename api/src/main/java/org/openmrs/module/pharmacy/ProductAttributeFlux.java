package org.openmrs.module.pharmacy;

import org.openmrs.Location;

import javax.persistence.*;

@Entity(name = "ProductAttributeFlux")
@Table(name = "pharmacy_product_attribute_flux")
public class ProductAttributeFlux extends AbstractPharmacyData {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_attribute_flux_id")
    private Integer productAttributeFluxId;

    @ManyToOne
    @JoinColumn(name = "product_attribute_id")
    private ProductAttribute productAttribute;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
}
