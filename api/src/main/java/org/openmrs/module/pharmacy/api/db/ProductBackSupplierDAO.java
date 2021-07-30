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
import org.openmrs.module.pharmacy.entities.ProductBackSupplier;

import java.util.Date;
import java.util.List;

/**
 *  Database methods for {@link org.openmrs.module.pharmacy.api.ProductBackSupplierService}.
 */
public interface ProductBackSupplierDAO {

    /*
	 * Add DAO methods here
	 */
	List<ProductBackSupplier> getAllProductBackSuppliers(Location location, Boolean includeVoided);
	List<ProductBackSupplier> getAllProductBackSuppliers(Location location, Boolean includeVoided, Date operationStartDate, Date operationEndDate);
	List<ProductBackSupplier> getAllProductBackSuppliers(Location location);
	List<ProductBackSupplier> getAllProductBackSuppliers(Boolean includeVoided);
	ProductBackSupplier getOneProductBackSupplierById(Integer id);
	ProductBackSupplier saveProductBackSupplier(ProductBackSupplier productBackSupplier);
	ProductBackSupplier editProductBackSupplier(ProductBackSupplier productBackSupplier);
	void removeProductBackSupplier(ProductBackSupplier productBackSupplier);
	ProductBackSupplier getOneProductBackSupplierByUuid(String uuid);
    List<Location> getAllClientLocation(Boolean includeVoided);
}
