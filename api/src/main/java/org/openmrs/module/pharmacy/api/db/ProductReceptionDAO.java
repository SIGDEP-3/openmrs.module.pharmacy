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
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;

import java.util.Date;
import java.util.List;

/**
 *  Database methods for {@link org.openmrs.module.pharmacy.api.ProductReceptionService}.
 */
public interface ProductReceptionDAO {

    /*
	 * Add DAO methods here
	 */

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

}
