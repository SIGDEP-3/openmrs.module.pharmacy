package org.openmrs.module.pharmacy.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.ProductRegimen;
import org.openmrs.module.pharmacy.api.ProductRegimenService;
import org.openmrs.module.pharmacy.web.controller.PharmacyResourceController;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.List;


@Resource(name = RestConstants.VERSION_1 + PharmacyResourceController.PHARMACY_REST_NAMESPACE + "/regimen",
        supportedClass = ProductRegimen.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*", "1.11.*", "1.12.*", "2.*"})
public class ProductRegimenResource extends DelegatingCrudResource<ProductRegimen> {

    ProductRegimenService getService() {
        return Context.getService(ProductRegimenService.class);
    }

    @Override
    public ProductRegimen getByUniqueId(String s) {
        return getService().getOneProductRegimenByUuid(s);
    }

    @Override
    protected void delete(ProductRegimen productRegimen, String s, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public ProductRegimen newDelegate() {
        return new ProductRegimen();
    }

    @Override
    public ProductRegimen save(ProductRegimen productRegimen) {
        return getService().saveProductRegimen(productRegimen);
    }

    @Override
    public void purge(ProductRegimen productRegimen, RequestContext requestContext) throws ResponseException {
        getService().removeProductRegimen(productRegimen);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = null;
        if (representation instanceof FullRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("concept", Representation.DEFAULT);
            description.addProperty("products", Representation.REF);
            description.addProperty("uuid");
        } else if (representation instanceof DefaultRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("concept", Representation.DEFAULT);
            description.addProperty("uuid");
        } else if (representation instanceof RefRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("concept", Representation.REF);
            description.addProperty("uuid");
        }
        return description;
    }

    @Override
    public Model getGETModel(Representation rep) {
        ModelImpl model = (ModelImpl) super.getGETModel(rep);
        model.property("concept", new RefProperty("#/definitions/ConceptGet"))
                .property("products", new ArrayProperty(new RefProperty("#/definitions/ProductGet")))
                .property("uuid", new StringProperty());
        return model;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("concept");
        description.addProperty("uuid");
        return description;
    }

    @Override
    public Model getCREATEModel(Representation rep) {
        ModelImpl model = (ModelImpl) super.getGETModel(rep);
        model.property("concept", new RefProperty("#/definitions/ConceptGet"))
                .property("uuid", new StringProperty()).required("concept");
        return model;
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("concept");
        description.addProperty("uuid");
        return description;
    }

    @Override
    public Model getUPDATEModel(Representation rep) {
        ModelImpl model = (ModelImpl) super.getGETModel(rep);
        model.property("concept", new RefProperty("#/definitions/ConceptGet"));
        return model;
    }

    @Override
    protected PageableResult doGetAll(RequestContext context) throws ResponseException {
        List<ProductRegimen> regimen = getService().getAllProductRegimen();
        return new NeedsPaging<ProductRegimen>(regimen, context);
    }
}
