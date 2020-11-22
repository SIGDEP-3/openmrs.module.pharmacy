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
import org.openmrs.module.pharmacy.ProductAttribute;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.ProductOperation;

import java.util.List;

/**
 *  Database methods for {@link org.openmrs.module.pharmacy.api.ProductAttributeFluxService}.
 */
public interface ProductAttributeFluxDAO {

    /*
	 * Add DAO methods here
	 */

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

	List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxes(Location location);
	ProductAttributeOtherFlux getOneProductAttributeOtherFluxByAttributeAndOperation(ProductAttribute productAttribute, ProductOperation productOperation);
	List<ProductAttributeOtherFlux> getAllProductAttributeOtherFluxByOperation(ProductOperation reception, Boolean b);
	ProductAttributeOtherFlux getOneProductAttributeOtherFluxById(Integer id);
	ProductAttributeOtherFlux saveProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux);
	ProductAttributeOtherFlux editProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux);
	void removeProductAttributeOtherFlux(ProductAttributeOtherFlux productAttributeOtherFlux);
	ProductAttributeOtherFlux getOneProductAttributeOtherFluxByUuid(String uuid);
}
