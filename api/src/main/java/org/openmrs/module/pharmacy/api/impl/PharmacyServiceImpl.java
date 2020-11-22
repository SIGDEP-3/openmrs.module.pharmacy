/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.pharmacy.api.impl;

import org.openmrs.Location;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;

import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of {@link PharmacyService}.
 */
public class PharmacyServiceImpl extends BaseOpenmrsService implements PharmacyService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private PharmacyDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(PharmacyDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public PharmacyDAO getDao() {
	    return dao;
    }

    /******* PRODUCTS UNITS ******/

    @Override
    public ProductUnit saveProductUnit(ProductUnit productUnit) {
        return dao.saveProductUnit(productUnit);
    }

    @Override
    public ProductUnit editProductUnit(ProductUnit productUnit) {
        return dao.editProductUnit(productUnit);
    }

    @Override
    public void removeProductUnit(ProductUnit productUnit) {
        dao.removeProductUnit(productUnit);
    }

    @Override
    public ProductUnit getOneProductUnitById(Integer productUnitId) {
        return dao.getOneProductUnitById(productUnitId);
    }

    @Override
    public ProductUnit getOneProductUnitByUuid(String uuid) {
        return dao.getOneProductUnitByUuid(uuid);
    }

    @Override
    public ProductUnit getOneProductUnitByName(String name) {
        return dao.getOneProductUnitByName(name);
    }

    @Override
    public List<ProductUnit> getAllProductUnit() {
        return dao.getAllProductUnit();
    }

    /******* PRODUCTS ******/

    @Override
    public Product saveProduct(Product product) {
        return dao.saveProduct(product);
    }

    @Override
    public Product editProduct(Product product) {
        return dao.editProduct(product);
    }

    @Override
    public Product getOneProductById(Integer productId) {
        return dao.getOneProductById(productId);
    }

    @Override
    public Product getOneProductByCode(String code) {
        return dao.getOneProductByCode(code);
    }

    @Override
    public Product getOneProductByUuid(String uuid) {
        return dao.getOneProductByUuid(uuid);
    }

    @Override
    public Product getOneProductByRetailName(String retailName) {
        return dao.getOneProductByRetailName(retailName);
    }

    @Override
    public Product getOneProductByWholesaleName(String wholesaleName) {
        return dao.getOneProductByWholesaleName(wholesaleName);
    }

    @Override
    public Product getOneProductByName(String name) {
        return dao.getOneProductByName(name);
    }

    @Override
    public List<Product> getAllProduct() {
        return dao.getAllProduct();
    }

    @Override
    public List<Product> getAllProductByRetailUnit(ProductUnit retailUnit) {
        return dao.getAllProductByRetailUnit(retailUnit);
    }

    @Override
    public List<Product> getAllProductByWholesaleUnit(ProductUnit wholesaleUnit) {
        return dao.getAllProductByWholesaleUnit(wholesaleUnit);
    }

    @Override
    public List<Product> searchProductByNameLike(String nameSearch) {
        return dao.searchProductByNameLike(nameSearch);
    }

    @Override
    public List<ProductProgram> getAllProductProgram() {
        return dao.getAllProductProgram();
    }

    @Override
    public ProductProgram getOneProductProgramById(Integer programId) {
        return dao.getOneProductProgramById(programId);
    }

    @Override
    public void removeProductProgram(ProductProgram productProgram) {
        dao.removeProductProgram(productProgram);
    }

    @Override
    public ProductProgram saveProductProgram(ProductProgram programForm) {
        return dao.saveProductProgram(programForm);
    }

    @Override
    public ProductProgram getOneProductProgramByUuid(String uuid) {
        return dao.getOneProductProgramByUuid(uuid);
    }

    @Override
    public ProductProgram getOneProductProgramByName(String name) {
        return dao.getOneProductProgramByName(name);
    }

    /**** PRODUCTS REGIMENS ****/

    @Override
    public ProductRegimen saveProductRegimen(ProductRegimen productRegimen) {
        return dao.saveProductRegimen(productRegimen);
    }

    @Override
    public void removeProductRegimen(ProductRegimen productRegimen) {
        dao.removeProductRegimen(productRegimen);
    }

    @Override
    public ProductRegimen getOneProductRegimenById(Integer regimenId) {
        return dao.getOneProductRegimenById(regimenId);
    }

    @Override
    public ProductRegimen getOneProductRegimenByUuid(String uuid) {
        return dao.getOneProductRegimenByUuid(uuid);
    }

    @Override
    public ProductRegimen getOneProductRegimenByConceptName(String name) {
        return dao.getOneProductRegimenByConceptName(name);
    }

    @Override
    public ProductRegimen getOneProductRegimenByConceptId(Integer conceptId) {
        return dao.getOneProductRegimenByConceptId(conceptId);
    }

    @Override
    public List<ProductRegimen> getAllProductRegimen() {
        return dao.getAllProductRegimen();
    }

    /**** PRODUCTS PRICE ****/
    @Override
    public ProductPrice saveProductPrice(ProductPrice productPrice) {
        return dao.saveProductPrice(productPrice);
    }

    @Override
    public void removeProductPrice(ProductPrice productPrice) {
        dao.removeProductPrice(productPrice);
    }

    @Override
    public ProductPrice getOneProductPriceByProductId(Integer productId) {
        return dao.getOneProductPriceByProductId(productId);
    }

    @Override
    public ProductPrice getOneProductPriceById(Integer productPriceId) {
        return dao.getOneProductPriceById(productPriceId);
    }

    @Override
    public ProductPrice getOneProductPriceByProductProgramId(Integer productProgramId) {
        return dao.getOneProductPriceByProductProgramId(productProgramId);
    }

    @Override
    public ProductPrice getOneProductPriceByUuid(String uuid) {
        return dao.getOneProductPriceByUuid(uuid);
    }

    @Override
    public ProductPrice getOneActiveProductPriceByProductAndProductProgram() {
        return dao.getOneActiveProductPriceByProductAndProductProgram();
    }

    @Override
    public List<ProductPrice> getAllProductPriceByStatus(Boolean status) {
        return dao.getAllProductPriceByStatus(status);
    }

    @Override
    public List<ProductPrice> getAllProductPrices() {
        return dao.getAllProductPrices();
    }



    @Override
    public List<ProductSupplier> getAllProductSuppliers() {
        return dao.getAllProductSuppliers();
    }

    @Override
    public ProductSupplier saveProductSupplier(ProductSupplier productSupplier) {
        return dao.saveProductSupplier(productSupplier);
    }

    @Override
    public ProductSupplier editProductSupplier(ProductSupplier productSupplier) {
        return dao.editProductSupplier(productSupplier);
    }

    @Override
    public void removeProductSupplier(ProductSupplier productSupplier) {
        dao.removeProductSupplier(productSupplier);
    }

    @Override
    public ProductSupplier getOneProductSupplierById(Integer productSupplierId) {
        return dao.getOneProductSupplierById(productSupplierId);
    }

    @Override
    public ProductSupplier getOneProductSupplierByUuid(String uuid) {
        return dao.getOneProductSupplierByUuid(uuid);
    }

    @Override
    public ProductSupplier getOneProductSupplierByName(String name) {
        return dao.getOneProductSupplierByName(name);
    }

    @Override
    public List<ProductReception> getAllProductReceptions(Location location, Boolean includeVoided) {
        return dao.getAllProductReceptions(location, includeVoided);
    }

    @Override
    public List<ProductReception> getAllProductReceptions(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductReceptions(location, includeVoided, operationStartDate, operationEndDate);
    }

    @Override
    public List<ProductReception> getAllProductReceptions(Location location) {
        return dao.getAllProductReceptions(location);
    }

    @Override
    public List<ProductReception> getAllProductReceptions(Boolean includeVoided) {
        return dao.getAllProductReceptions(includeVoided);
    }

    @Override
    public ProductReception getOneProductReceptionById(Integer id) {
        return dao.getOneProductReceptionById(id);
    }

    @Override
    public ProductReception saveProductReception(ProductReception productReception) {
        return dao.saveProductReception(productReception);
    }

    @Override
    public ProductReception editProductReception(ProductReception productReception) {
        return dao.editProductReception(productReception);
    }

    @Override
    public void removeProductReception(ProductReception productReception) {
        dao.removeProductReception(productReception);
    }

    @Override
    public ProductReception getOneProductReceptionByUuid(String uuid) {
        return dao.getOneProductReceptionByUuid(uuid);
    }

    @Override
    public List<ProductReceptionFluxDTO> getProductReceptionFluxDTOs(ProductReception productReception) {
        return dao.getProductReceptionFluxDTOs(productReception);
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes(Location location, Boolean includeVoided) {
        return dao.getAllProductAttributes(location, includeVoided);
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes(Location location) {
        return dao.getAllProductAttributes(location);
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes(Boolean includeVoided) {
        return dao.getAllProductAttributes(includeVoided);
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes(Product product) {
        return dao.getAllProductAttributes(product);
    }

    @Override
    public ProductAttribute getOneProductAttributeById(Integer id) {
        return dao.getOneProductAttributeById(id);
    }

    @Override
    public ProductAttribute saveProductAttribute(ProductAttribute productAttribute) {
        return dao.saveProductAttribute(productAttribute);
    }

    @Override
    public ProductAttribute editProductAttribute(ProductAttribute productAttribute) {
        return dao.saveProductAttribute(productAttribute);
    }

    @Override
    public void removeProductAttribute(ProductAttribute productAttribute) {
        dao.removeProductAttribute(productAttribute);
    }

    @Override
    public ProductAttribute getOneProductAttributeByUuid(String uuid) {
        return dao.getOneProductAttributeByUuid(uuid);
    }

    @Override
    public ProductAttribute getOneProductAttributeByBatchNumber(String batchNumber) {
        return dao.getOneProductAttributeByBatchNumber(batchNumber);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location, Boolean includeVoided) {
        return dao.getAllProductAttributeFluxes(location, includeVoided);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location) {
        return dao.getAllProductAttributeFluxes(location);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxes(Boolean includeVoided) {
        return dao.getAllProductAttributeFluxes(includeVoided);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxByAttribute(ProductAttribute productAttribute, Boolean includeVoided) {
        return dao.getAllProductAttributeFluxByAttribute(productAttribute, includeVoided);
    }

    @Override
    public List<ProductAttributeFlux> getAllProductAttributeFluxByOperation(ProductOperation productOperation, Boolean includeVoided) {
        return dao.getAllProductAttributeFluxByOperation(productOperation, includeVoided);
    }

    @Override
    public ProductAttributeFlux getOneProductAttributeFluxById(Integer id) {
        return dao.getOneProductAttributeFluxById(id);
    }

    @Override
    public ProductAttributeFlux getOneProductAttributeFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation) {
        return dao.getOneProductAttributeFluxByAttributeAndOperation(productAttribute, productOperation);
    }

    @Override
    public ProductAttributeFlux saveProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
        return dao.saveProductAttributeFlux(productAttributeFlux);
    }

    @Override
    public ProductAttributeFlux editProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
        return dao.editProductAttributeFlux(productAttributeFlux);
    }

    @Override
    public void removeProductAttributeFlux(ProductAttributeFlux productAttributeFlux) {
        dao.removeProductAttributeFlux(productAttributeFlux);
    }

    @Override
    public ProductAttributeFlux getOneProductAttributeFluxByUuid(String uuid) {
        return dao.getOneProductAttributeFluxByUuid(uuid);
    }

    @Override
    public List<ProductAttributeStock> getAllProductAttributeStocks(Location location, Boolean includeVoided) {
        return dao.getAllProductAttributeStocks(location, includeVoided);
    }

    @Override
    public List<ProductAttributeStock> getAllProductAttributeStocks(Location location) {
        return dao.getAllProductAttributeStocks(location);
    }

    @Override
    public List<ProductAttributeStock> getAllProductAttributeStocks(Boolean includeVoided) {
        return dao.getAllProductAttributeStocks(includeVoided);
    }

    @Override
    public List<ProductAttributeStock> getAllProductAttributeStockByAttribute(ProductAttribute productAttribute, Boolean includeVoided) {
        return dao.getAllProductAttributeStockByAttribute(productAttribute, includeVoided);
    }

    @Override
    public ProductAttributeStock getOneProductAttributeStockByAttribute(ProductAttribute productAttribute, Location location, Boolean includeVoided) {
        return dao.getOneProductAttributeStockByAttribute(productAttribute, location, includeVoided);
    }

    @Override
    public ProductAttributeStock getOneProductAttributeStockById(Integer id) {
        return dao.getOneProductAttributeStockById(id);
    }

    @Override
    public ProductAttributeStock saveProductAttributeStock(ProductAttributeStock productAttributeStock) {
        return dao.saveProductAttributeStock(productAttributeStock);
    }

    @Override
    public ProductAttributeStock editProductAttributeStock(ProductAttributeStock productAttributeStock) {
        return dao.editProductAttributeStock(productAttributeStock);
    }

    @Override
    public void removeProductAttributeStock(ProductAttributeStock productAttributeStock) {
        dao.removeProductAttributeStock(productAttributeStock);
    }

    @Override
    public ProductAttributeStock getOneProductAttributeStockByUuid(String uuid) {
        return dao.getOneProductAttributeStockByUuid(uuid);
    }

    @Override
    public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxes(Location location) {
        return dao.getAllProductAttributeOtherFluxes(location);
    }

    @Override
    public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation) {
        return dao.getOneProductAttributeOtherFluxByAttributeAndOperation(productAttribute, productOperation);
    }

    @Override
    public List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByOperation(ProductOperation productOperation, Boolean b) {
        return dao.getAllProductAttributeOtherFluxByOperation(productOperation, b);
    }

    @Override
    public ProductAttributeOtherFlux getOneProductAttributeOtherFluxById(Integer id) {
        return dao.getOneProductAttributeOtherFluxById(id);
    }

    @Override
    public ProductAttributeOtherFlux saveProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
        return dao.saveProductAttributeOtherFlux(productAttributeOtherFlux);
    }

    @Override
    public ProductAttributeOtherFlux editProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
        return dao.editProductAttributeOtherFlux(productAttributeOtherFlux);
    }

    @Override
    public void removeProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux) {
        dao.removeProductAttributeOtherFlux(productAttributeOtherFlux);
    }

    @Override
    public ProductAttributeOtherFlux getOneProductAttributeOtherFluxByUuid(String uuid) {
        return dao.getOneProductAttributeOtherFluxByUuid(uuid);
    }

    @Override
    public Boolean validateOperation(ProductOperation operation) {
        return null;
    }

    /**** PRODUCT Exchange *****/

    @Override
    public List<ProductExchangeEntity> getAllProductExchange() {
        return dao.getAllProductExchange();
    }

    @Override
    public ProductExchangeEntity saveProductExchange(ProductExchangeEntity productExchange) {
        return dao.saveProductExchange(productExchange);
    }

    @Override
    public ProductExchangeEntity editProductExchange(ProductExchangeEntity productExchange) {
        return dao.editProductExchange(productExchange);
    }

    @Override
    public void removeProductExchange(ProductExchangeEntity productExchange) {
        dao.removeProductExchange(productExchange);
    }

    @Override
    public ProductExchangeEntity getOneProductExchangeById(Integer productExchangeId) {
        return dao.getOneProductExchangeById(productExchangeId);
    }

    @Override
    public ProductExchangeEntity getOneProductExchangeByUuid(String uuid) {
        return dao.getOneProductExchangeByUuid(uuid);
    }

    @Override
    public ProductExchangeEntity getOneProductExchangeByName(String name) {
        return dao.getOneProductExchangeByName(name);
    }

    /******** PRODUCT MOVEMENT ENTRY *********/

    @Override
    public List<ProductMovementEntry> getAllProductMovementEntry(Location location, Boolean includeVoided) {
        return dao.getAllProductMovementEntry(location, includeVoided);
    }

    @Override
    public List<ProductMovementEntry> getAllProductMovementEntry(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductMovementEntry(location, includeVoided, operationStartDate, operationEndDate);
    }

    @Override
    public List<ProductMovementEntry> getAllProductMovementEntry(Location location) {
        return dao.getAllProductMovementEntry(location);
    }

    @Override
    public List<ProductMovementEntry> getAllProductMovementEntry(Boolean includeVoided) {
        return dao.getAllProductMovementEntry(includeVoided);
    }

    @Override
    public ProductMovementEntry getOneProductMovementEntryById(Integer id) {
        return dao.getOneProductMovementEntryById(id);
    }

    @Override
    public ProductMovementEntry saveProductMovementEntry(ProductMovementEntry productMovementEntry) {
        return dao.saveProductMovementEntry(productMovementEntry);
    }

    @Override
    public ProductMovementEntry editProductMovementEntry(ProductMovementEntry productMovementEntry) {
        return dao.editProductMovementEntry(productMovementEntry);
    }

    @Override
    public void removeProductMovementEntry(ProductMovementEntry productMovementEntry) {
        dao.removeProductMovementEntry(productMovementEntry);
    }

    @Override
    public ProductMovementEntry getOneProductMovementEntryByUuid(String uuid) {
        return dao.getOneProductMovementEntryByUuid(uuid);
    }

//    @Override
//    public List<ProductReceptionFluxDTO> getProductReceptionFluxDTOs(ProductReception productReception) {
//        return dao.getProductReceptionFluxDTOs(productReception);
//    }

    /******** PRODUCT MOVEMENT OUT *********/

    @Override
    public List<ProductMovementOut> getAllProductMovementOut(Location location, Boolean includeVoided) {
        return dao.getAllProductMovementOut(location, includeVoided);
    }

    @Override
    public List<ProductMovementOut> getAllProductMovementOut(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate) {
        return dao.getAllProductMovementOut(location, includeVoided, operationStartDate, operationEndDate);
    }

    @Override
    public List<ProductMovementOut> getAllProductMovementOut(Location location) {
        return dao.getAllProductMovementOut(location);
    }

    @Override
    public List<ProductMovementOut> getAllProductMovementOut(Boolean includeVoided) {
        return dao.getAllProductMovementOut(includeVoided);
    }

    @Override
    public ProductMovementOut getOneProductMovementOutById(Integer id) {
        return dao.getOneProductMovementOutById(id);
    }

    @Override
    public ProductMovementOut saveProductMovementOut(ProductMovementOut productMovementOut) {
        return dao.saveProductMovementOut(productMovementOut);
    }

    @Override
    public ProductMovementOut editProductMovementOut(ProductMovementOut productMovementOut) {
        return dao.editProductMovementOut(productMovementOut);
    }

    @Override
    public void removeProductMovementOut(ProductMovementOut productMovementOut) {
        dao.removeProductMovementOut(productMovementOut);
    }

    @Override
    public ProductMovementOut getOneProductMovementOutByUuid(String uuid) {
        return dao.getOneProductMovementOutByUuid(uuid);
    }

//    @Override
//    public List<ProductReceptionFluxDTO> getProductReceptionFluxDTOs(ProductReception productReception) {
//        return dao.getProductReceptionFluxDTOs(productReception);
//    }
}
