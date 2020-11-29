package org.openmrs.module.pharmacy.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductUnit;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductUnitService;
import org.openmrs.module.pharmacy.web.controller.PharmacyResourceController;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;


@Resource(name = RestConstants.VERSION_1 + PharmacyResourceController.PHARMACY_REST_NAMESPACE + "/product-unit",
        supportedClass = ProductUnit.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*", "1.11.*", "1.12.*", "2.*"})
public class ProductUnitResource extends DelegatingCrudResource<ProductUnit> {

    ProductUnitService getService() {
        return Context.getService(ProductUnitService.class);
    }

    @Override
    public ProductUnit getByUniqueId(String s) {
        return getService().getOneProductUnitByUuid(s);
    }

    @Override
    protected void delete(ProductUnit productUnit, String s, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public ProductUnit newDelegate() {
        return new ProductUnit();
    }

    @Override
    public ProductUnit save(ProductUnit productUnit) {
        return getService().saveProductUnit(productUnit);
    }

    @Override
    public void purge(ProductUnit productUnit, RequestContext requestContext) throws ResponseException {
        getService().removeProductUnit(productUnit);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = null;
        if (representation instanceof FullRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("name");
            description.addProperty("description");
            description.addProperty("uuid");
        } else if (representation instanceof DefaultRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("name");
            description.addProperty("uuid");
        }
        return description;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("name");
        description.addProperty("description");
        description.addProperty("uuid");
        return description;
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("name");
        description.addProperty("description");
        description.addProperty("uuid");
        return description;
    }
}
