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

import org.openmrs.module.pharmacy.ProductSupplier;

import java.util.List;

/**
 *  Database methods for {@link org.openmrs.module.pharmacy.api.ProductSupplierService}.
 */
public interface ProductSupplierDAO {

    /*
	 * Add DAO methods here
	 */

	List<ProductSupplier> getAllProductSuppliers();
	ProductSupplier saveProductSupplier(ProductSupplier productSupplier);
	ProductSupplier editProductSupplier(ProductSupplier productSupplier);
	void removeProductSupplier(ProductSupplier productSupplier);
	ProductSupplier getOneProductSupplierById(Integer productSupplierId);
	ProductSupplier getOneProductSupplierByUuid(String uuid);
	ProductSupplier getOneProductSupplierByName(String name);

}
