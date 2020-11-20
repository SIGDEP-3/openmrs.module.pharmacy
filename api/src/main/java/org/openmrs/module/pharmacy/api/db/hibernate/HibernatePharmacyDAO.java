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
package org.openmrs.module.pharmacy.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.db.PharmacyDAO;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * It is a default implementation of  {@link PharmacyDAO}.
 */
public class HibernatePharmacyDAO implements PharmacyDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	private SessionFactory sessionFactory;
	private HibernateProductAttributeFluxDAO productAttributeFluxDAO;
	private HibernateProductAttributeStockDAO productAttributeStockDAO;

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public HibernateProductAttributeFluxDAO getProductAttributeFluxDAO() {
		return productAttributeFluxDAO;
	}

	public void setProductAttributeFluxDAO(HibernateProductAttributeFluxDAO productAttributeFluxDAO) {
		this.productAttributeFluxDAO = productAttributeFluxDAO;
	}

	public HibernateProductAttributeStockDAO getProductAttributeStockDAO() {
		return productAttributeStockDAO;
	}

	public void setProductAttributeStockDAO(HibernateProductAttributeStockDAO productAttributeStockDAO) {
		this.productAttributeStockDAO = productAttributeStockDAO;
	}

	@Override
	public Boolean validateOperation(ProductOperation operation) {
		operation.setOperationStatus(OperationStatus.VALIDATED);
		sessionFactory.getCurrentSession().saveOrUpdate(operation);
		for (ProductAttributeFlux flux : operation.getProductAttributeFluxes()) {
			flux.setStatus(OperationStatus.VALIDATED);
			getProductAttributeFluxDAO().saveProductAttributeFlux(flux);

			if (!operation.getIncidence().equals(Incidence.NONE)) {
				List<ProductAttributeStock> productAttributeStocks = getProductAttributeStockDAO().getAllProductAttributeStockByAttribute(flux.getProductAttribute(), false);
				//ProductAttributeStock attributeStock = getOneProductAttributeStockByAttribute(flux.getProductAttribute(), Context.getUserContext().getLocation(), false);
				ProductAttributeStock attributeStock = getProductAttributeStockDAO().getOneProductAttributeStockByAttribute(flux.getProductAttribute(), Context.getLocationService().getDefaultLocation(), false);
				if (productAttributeStocks != null) {
					Integer quantity = operation.getIncidence().equals(Incidence.POSITIVE) ?
							attributeStock.getQuantityInStock() + flux.getQuantity() :
							(operation.getIncidence().equals(Incidence.NEGATIVE) ? attributeStock.getQuantityInStock() - flux.getQuantity() : flux.getQuantity());
					attributeStock.setQuantityInStock(quantity);
				} else {
					attributeStock = new ProductAttributeStock();
					attributeStock.setQuantityInStock(flux.getQuantity());
					attributeStock.setLocation(Context.getLocationService().getDefaultLocation());
					//attributeStock.setLocation(Context.getUserContext().getLocation());
					attributeStock.setProductAttribute(flux.getProductAttribute());
				}
				attributeStock.setDateCreated(new Date());
				getProductAttributeStockDAO().saveProductAttributeStock(attributeStock);
			}
		}

		return true;
	}
}
