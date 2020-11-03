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
package org.openmrs.module.pharmacy.api.db;

import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductRegimen;
import org.openmrs.module.pharmacy.ProductUnit;
import org.openmrs.module.pharmacy.api.PharmacyService;

import java.util.List;

/**
 *  Database methods for {@link PharmacyService}.
 */
public interface PharmacyDAO {

    /*
	 * Add DAO methods here
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
	public ProductRegimen getOneProductRegimenByConceptId(Integer conceptId);
	List<ProductRegimen> getAllProductRegimen();
}
