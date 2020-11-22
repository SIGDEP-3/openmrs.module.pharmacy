package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductRegimen;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.api.ProductRegimenService;
import org.openmrs.module.pharmacy.api.ProductUnitService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProductForm {
    private Integer productId;
    private String code;
    private String retailName;
    private String wholesaleName;
    private Integer productRetailUnitId;
    private Integer productWholesaleUnitId;
    private Double unitConversion;
    private Set<Integer> productRegimenIds = new HashSet<Integer>();
    private Set<Integer> productProgramIds = new HashSet<Integer>();
    private String uuid = UUID.randomUUID().toString();

    public ProductForm() {
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

    public void setRetailName(String retailName) {
        this.retailName = retailName;
    }

    public String getWholesaleName() {
        return wholesaleName;
    }

    public void setWholesaleName(String wholesaleName) {
        this.wholesaleName = wholesaleName;
    }

    public Integer getProductRetailUnitId() {
        return productRetailUnitId;
    }

    public void setProductRetailUnitId(Integer productRetailUnitId) {
        this.productRetailUnitId = productRetailUnitId;
    }

    public Integer getProductWholesaleUnitId() {
        return productWholesaleUnitId;
    }

    public void setProductWholesaleUnitId(Integer productWholesaleUnitId) {
        this.productWholesaleUnitId = productWholesaleUnitId;
    }

    public Double getUnitConversion() {
        return unitConversion;
    }

    public void setUnitConversion(Double unitConversion) {
        this.unitConversion = unitConversion;
    }

    public Set<Integer> getProductRegimenIds() {
        return productRegimenIds;
    }

    public void setProductRegimenIds(Set<Integer> productRegimenIds) {
        this.productRegimenIds = productRegimenIds;
    }

    public Set<Integer> getProductProgramIds() {
        return productProgramIds;
    }

    public void setProductProgramIds(Set<Integer> productProgramIds) {
        this.productProgramIds = productProgramIds;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setProduct(Product product) {
        setProductId(product.getProductId());
        setCode(product.getCode());
        setUnitConversion(product.getUnitConversion());
        setRetailName(product.getRetailName());
        setWholesaleName(product.getWholesaleName());
        setProductWholesaleUnitId(product.getProductWholesaleUnit().getProductUnitId());
        setProductRetailUnitId(product.getProductRetailUnit().getProductUnitId());
        if (!product.getProductPrograms().isEmpty()) {
            addProgramIds(product);
        }
        if (!product.getProductRegimens().isEmpty()) {
            addRegimenIds(product);
        }
        setUuid(product.getUuid());
    }

    public Product getProduct() {
        Product product = new Product();
        product.setProductId(getProductId());
        product.setCode(getCode());
        product.setUnitConversion(getUnitConversion());
        product.setRetailName(getRetailName());
        product.setWholesaleName(getWholesaleName());
        product.setProductRetailUnit(Context.getService(ProductUnitService.class).getOneProductUnitById(getProductRetailUnitId()));
        product.setProductWholesaleUnit(Context.getService(ProductUnitService.class).getOneProductUnitById(getProductWholesaleUnitId()));
        if (!productProgramIds.isEmpty()) {
            product.getProductPrograms().addAll(getProgramsByIds(getProductProgramIds()));
        }
        if ((!productRegimenIds.isEmpty())) {
            product.getProductRegimens().addAll(getRegimensByIds(getProductRegimenIds()));
        }

        product.setUuid(getUuid());

        return product;
    }

    private void addProgramIds(Product product) {
        for (ProductProgram program : product.getProductPrograms()) {
            getProductProgramIds().add(program.getProductProgramId());
        }
    }

    private void addRegimenIds(Product product) {
        for (ProductRegimen regimen : product.getProductRegimens()) {
            getProductRegimenIds().add(regimen.getProductRegimenId());
        }
    }

    private Set<ProductProgram> getProgramsByIds(Set<Integer> programIds) {
        Set<ProductProgram> programs = new HashSet<ProductProgram>();
        for (Integer id : programIds) {
            programs.add(Context.getService(ProductProgramService.class).getOneProductProgramById(id));
        }
        return programs;
    }

    private Set<ProductRegimen> getRegimensByIds(Set<Integer> programIds) {
        Set<ProductRegimen> regimens = new HashSet<ProductRegimen>();
        for (Integer id : programIds) {
            regimens.add(Context.getService(ProductRegimenService.class).getOneProductRegimenById(id));
        }
        return regimens;
    }
}
