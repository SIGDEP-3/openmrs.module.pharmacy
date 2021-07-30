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

import org.openmrs.module.pharmacy.entities.Product;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.entities.ProductUnit;
import org.openmrs.module.pharmacy.dto.ProductUploadResumeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *  Database methods for {@link org.openmrs.module.pharmacy.api.ProductService}.
 */
public interface ProductDAO {

    /*
	 * Add DAO methods here
	 */

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

	ProductUploadResumeDTO uploadProducts(MultipartFile file);
	ProductUploadResumeDTO uploadProductRegimens(MultipartFile file);

    List<Product> getProductWithoutRegimenByProgram(ProductProgram productProgram);
}
