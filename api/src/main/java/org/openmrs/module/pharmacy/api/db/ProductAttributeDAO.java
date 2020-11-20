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

import org.openmrs.Location;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductAttribute;

import java.util.List;

/**
 *  Database methods for {@link org.openmrs.module.pharmacy.api.ProductAttributeService}.
 */
public interface ProductAttributeDAO {

    /*
	 * Add DAO methods here
	 */

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
}
