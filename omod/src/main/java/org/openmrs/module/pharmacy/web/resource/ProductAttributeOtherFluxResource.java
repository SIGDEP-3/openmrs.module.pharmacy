package org.openmrs.module.pharmacy.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.entities.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.entities.ProductOperation;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.ArrayList;
import java.util.List;


@SubResource(parent = ProductOperationResource.class, path = "otherFlux",
        supportedClass = ProductAttributeOtherFlux.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*", "1.11.*", "1.12.*", "2.*"})
public class ProductAttributeOtherFluxResource extends DelegatingSubResource<ProductAttributeOtherFlux, ProductOperation, ProductOperationResource> {

    ProductAttributeFluxService getService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    @Override
    public ProductAttributeOtherFlux getByUniqueId(String s) {
        return getService().getOneProductAttributeOtherFluxUuid(s);
    }

    @Override
    protected void delete(ProductAttributeOtherFlux productAttributeOtherFlux, String s, RequestContext requestContext) throws ResponseException {
    }

    @Override
    public void purge(ProductAttributeOtherFlux productAttributeOtherFlux, RequestContext requestContext) throws ResponseException {
    }

    @Override
    public ProductAttributeOtherFlux newDelegate() {
        return new ProductAttributeOtherFlux();
    }

    @Override
    public ProductAttributeOtherFlux save(ProductAttributeOtherFlux productAttributeOtherFlux) {
        return getService().saveProductAttributeOtherFlux(productAttributeOtherFlux);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = null;
        if (representation instanceof FullRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("productAttribute", Representation.REF);
            description.addProperty("product", Representation.REF);
            description.addProperty("quantity");
            description.addProperty("label");
            description.addProperty("location", Representation.REF);
            description.addProperty("uuid");
        } else if (representation instanceof DefaultRepresentation || representation instanceof RefRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("productAttribute", Representation.REF);
            description.addProperty("product", Representation.REF);
            description.addProperty("quantity");
            description.addProperty("label");
            description.addProperty("uuid");
        }
        return description;
    }

    @Override
    public Model getGETModel(Representation rep) {
        ModelImpl model = new ModelImpl();
        model.property("productAttribute", new RefProperty("#/definitions/ProductAttributeGet"))
                .property("product", new RefProperty("#/definitions/ProductGet"))
                .property("quantity", new DoubleProperty())
                .property("label", new StringProperty())
                .property("location", new RefProperty("#/definitions/LocationGet"))
                .property("uuid", new StringProperty());
        return model;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("productAttribute");
        description.addRequiredProperty("product");
        description.addRequiredProperty("quantity");
        description.addRequiredProperty("label");
        description.addRequiredProperty("location");
        description.addProperty("uuid");
        return description;
    }

    @Override
    public Model getCREATEModel(Representation rep) {
        ModelImpl model = new ModelImpl();
        model.property("productAttribute", new StringProperty().example("uuid"))
                .property("product", new StringProperty().example("uuid"))
                .property("location", new StringProperty().example("uuid"))
                .property("quantity", new DoubleProperty())
                .property("label", new StringProperty())
                .property("uuid", new StringProperty())
                .required("productAttribute")
                .required("quantity")
                .required("location");
        if (rep instanceof FullRepresentation) {
            model.property("location", new RefProperty("#/definitions/LocationGet"))
                    .property("product", new RefProperty("#/definitions/ProductGet"))
                    .property("productAttribute", new RefProperty("#/definitions/ProductAttributeCreate"));
        }
        return model;
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("productAttribute");
        description.addProperty("product");
        description.addProperty("quantity");
        description.addProperty("label");
        description.addProperty("location");
        description.addProperty("uuid");
        return description;
    }

    @Override
    public Model getUPDATEModel(Representation rep) {
        ModelImpl model = new ModelImpl();
        model.property("productAttribute", new StringProperty().example("uuid"))
                .property("product", new StringProperty().example("uuid"))
                .property("location", new StringProperty().example("uuid"))
                .property("quantity", new DoubleProperty())
                .property("label", new StringProperty())
                .property("uuid", new StringProperty());
        if (rep instanceof FullRepresentation) {
            model.property("location", new RefProperty("#/definitions/LocationGet"))
                    .property("product", new RefProperty("#/definitions/ProductGet"))
                    .property("productAttribute", new RefProperty("#/definitions/ProductAttributeCreate"));
        }
        return model;
    }

    @Override
    public ProductOperation getParent(ProductAttributeOtherFlux productAttributeOtherFlux) {
        return productAttributeOtherFlux.getProductOperation();
    }

    @Override
    public void setParent(ProductAttributeOtherFlux productAttributeOtherFlux, ProductOperation productOperation) {
        productAttributeOtherFlux.setProductOperation(productOperation);
    }

    @Override
    public PageableResult doGetAll(ProductOperation productOperation, RequestContext requestContext) throws ResponseException {
        List<ProductAttributeOtherFlux> otherFluxes = new ArrayList<ProductAttributeOtherFlux>(productOperation.getProductAttributeOtherFluxes());
        return new NeedsPaging<ProductAttributeOtherFlux>(otherFluxes, requestContext);
    }
}
