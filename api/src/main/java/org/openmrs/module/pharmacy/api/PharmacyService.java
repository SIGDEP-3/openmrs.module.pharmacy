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
package org.openmrs.module.pharmacy.api;

import org.openmrs.Location;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(PharmacyService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface PharmacyService extends OpenmrsService {
     
	/*
	 * Add service methods here
	 * 
	 */

	ProductUnit saveProductUnit(ProductUnit productUnit);
	ProductUnit editProductUnit(ProductUnit productUnit);
	void removeProductUnit(ProductUnit productUnit);
	ProductUnit getOneProductUnitById(Integer productUnitId);
	ProductUnit getOneProductUnitByUuid(String uuid);
	ProductUnit getOneProductUnitByName(String name);
	List<ProductUnit> getAllProductUnit();

	Product saveProduct(Product product);
	Product editProduct(Product product);
	Product getOneProductById(Integer productId);
	Product getOneProductByCode(String code);
	Product getOneProductByUuid(String uuid);
	Product getOneProductByRetailName(String retailName);
	Product getOneProductByWholesaleName(String wholesaleName);
	Product getOneProductByName(String name);
	List<Product> getAllProduct();
	List<Product> getAllProductByRetailUnit(ProductUnit retailUnit);
	List<Product> getAllProductByWholesaleUnit(ProductUnit wholesaleUnit);
	List<Product> searchProductByNameLike(String nameSearch);

	ProductProgram saveProductProgram(ProductProgram programForm);
	void removeProductProgram(ProductProgram productProgram);
	ProductProgram getOneProductProgramById(Integer programId);
	ProductProgram getOneProductProgramByUuid(String uuid);
	ProductProgram getOneProductProgramByName(String name);
	List<ProductProgram> getAllProductProgram();

	ProductRegimen saveProductRegimen(ProductRegimen productRegimen);
	void removeProductRegimen(ProductRegimen productRegimen);
	ProductRegimen getOneProductRegimenById(Integer regimenId);
	ProductRegimen getOneProductRegimenByUuid(String uuid);
	ProductRegimen getOneProductRegimenByConceptName(String name);
	ProductRegimen getOneProductRegimenByConceptId(Integer conceptId);
	List<ProductRegimen> getAllProductRegimen();

	List<ProductSupplier> getAllProductSuppliers();
	ProductSupplier saveProductSupplier(ProductSupplier productSupplier);
	ProductSupplier editProductSupplier(ProductSupplier productSupplier);
	void removeProductSupplier(ProductSupplier productSupplier);
	ProductSupplier getOneProductSupplierById(Integer productSupplierId);
	ProductSupplier getOneProductSupplierByUuid(String uuid);
	ProductSupplier getOneProductSupplierByName(String name);

	List<ProductReception> getAllProductReceptions(Location location, Boolean includeVoided);
	List<ProductReception> getAllProductReceptions(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	List<ProductReception> getAllProductReceptions(Location location);
	List<ProductReception> getAllProductReceptions(Boolean includeVoided);
	ProductReception getOneProductReceptionById(Integer id);
	ProductReception saveProductReception(ProductReception productReception);
	ProductReception editProductReception(ProductReception productReception);
	void removeProductReception(ProductReception productReception);
	ProductReception getOneProductReceptionByUuid(String uuid);
	List<ProductReceptionFluxDTO> getProductReceptionFluxDTOs(ProductReception productReception);

	List<ProductAttribute> getAllProductAttributes(Location location, Boolean includeVoided);
	List<ProductAttribute> getAllProductAttributes(Location location);
	List<ProductAttribute> getAllProductAttributes(Boolean includeVoided);
	List<ProductAttribute> getAllProductAttributes(Product product);
	ProductAttribute getOneProductAttributeById(Integer id);
	ProductAttribute saveProductAttribute(ProductAttribute productAttribute);
	ProductAttribute editProductAttribute(ProductAttribute productAttribute);
	void removeProductAttribute(ProductAttribute productAttribute);
	ProductAttribute getOneProductAttributeByUuid(String uuid);
	ProductAttribute getOneProductAttributeByBatchNumber(String batchNumber);

	List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location, Boolean includeVoided);
	List<ProductAttributeFlux> getAllProductAttributeFluxes(Location location);
	List<ProductAttributeFlux> getAllProductAttributeFluxes(Boolean includeVoided);
	List<ProductAttributeFlux> getAllProductAttributeFluxByAttribute(ProductAttribute productAttribute, Boolean includeVoided);
	List<ProductAttributeFlux> getAllProductAttributeFluxByOperation(ProductOperation productOperation, Boolean includeVoided);
	ProductAttributeFlux getOneProductAttributeFluxById(Integer id);
	ProductAttributeFlux getOneProductAttributeFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation);
	ProductAttributeFlux saveProductAttributeFlux(ProductAttributeFlux productAttributeFlux);
	ProductAttributeFlux editProductAttributeFlux(ProductAttributeFlux productAttributeFlux);
	void removeProductAttributeFlux(ProductAttributeFlux productAttributeFlux);
	ProductAttributeFlux getOneProductAttributeFluxByUuid(String uuid);

	List<ProductAttributeStock> getAllProductAttributeStocks(Location location, Boolean includeVoided);
	List<ProductAttributeStock> getAllProductAttributeStocks(Location location);
	List<ProductAttributeStock> getAllProductAttributeStocks(Boolean includeVoided);
	List<ProductAttributeStock> getAllProductAttributeStockByAttribute(ProductAttribute productAttribute, Boolean includeVoided);
	ProductAttributeStock getOneProductAttributeStockByAttribute(ProductAttribute productAttribute, Location location, Boolean includeVoided);
	ProductAttributeStock getOneProductAttributeStockById(Integer id);
	ProductAttributeStock saveProductAttributeStock(ProductAttributeStock productAttributeStock);
	ProductAttributeStock editProductAttributeStock(ProductAttributeStock productAttributeStock);
	void removeProductAttributeStock(ProductAttributeStock productAttributeStock);
	ProductAttributeStock getOneProductAttributeStockByUuid(String uuid);

	List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxes(Location location);
	ProductAttributeOtherFlux getOneProductAttributeOtherFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation);
	List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByOperation(ProductOperation reception, Boolean b);
	ProductAttributeOtherFlux getOneProductAttributeOtherFluxById(Integer id);
	ProductAttributeOtherFlux saveProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux);
	ProductAttributeOtherFlux editProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux);
	void removeProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux);
	ProductAttributeOtherFlux getOneProductAttributeOtherFluxByUuid(String uuid);

	Boolean validateOperation(ProductOperation operation);
}
