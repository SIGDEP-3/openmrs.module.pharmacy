package org.openmrs.module.pharmacy.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.ProductAttributeFluxService;
import org.openmrs.module.pharmacy.entities.ProductAttributeFlux;
import org.openmrs.module.pharmacy.entities.ProductOperation;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.webservices.docs.swagger.core.property.EnumProperty;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
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
import java.util.Arrays;
import java.util.List;

@SubResource(parent = ProductOperationResource.class, path = "flux",
        supportedClass = ProductAttributeFlux.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*", "1.11.*", "1.12.*", "2.*"})
public class ProductAttributeFluxResource extends DelegatingSubResource<ProductAttributeFlux, ProductOperation, ProductOperationResource> {

    ProductAttributeFluxService getService() {
        return Context.getService(ProductAttributeFluxService.class);
    }

    @Override
    public ProductAttributeFlux getByUniqueId(String s) {
        return getService().getOneProductAttributeFluxUuid(s);
    }

    @Override
    protected void delete(ProductAttributeFlux productAttributeFlux, String s, RequestContext requestContext) throws ResponseException {
    }

    @Override
    public void purge(ProductAttributeFlux productAttributeFlux, RequestContext requestContext) throws ResponseException {
    }

    @Override
    public ProductAttributeFlux newDelegate() {
        return new ProductAttributeFlux();
    }

    @Override
    public ProductAttributeFlux save(ProductAttributeFlux productAttributeFlux) {
        return getService().saveProductAttributeFlux(productAttributeFlux);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = null;
        if (representation instanceof FullRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("productAttribute", Representation.REF);
            description.addProperty("operationDate");
            description.addProperty("quantity");
            description.addProperty("status");
            description.addProperty("observation");
            description.addProperty("location", Representation.REF);
            description.addProperty("auditInfo");
            description.addProperty("uuid");
            description.addProperty("voided");
        } else if (representation instanceof DefaultRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("productAttribute", Representation.REF);
            description.addProperty("operationDate");
            description.addProperty("quantity");
            description.addProperty("display");
            description.addProperty("status");
            description.addProperty("observation");
            description.addProperty("voided");
            description.addProperty("uuid");
        } else if (representation instanceof RefRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("display");
            description.addProperty("uuid");
        }
        return description;
    }



    @Override
    public Model getGETModel(Representation rep) {
        ModelImpl model = (ModelImpl) super.getGETModel(rep);
        model.property("productAttribute", new RefProperty("#/definitions/ProductAttributeGet"))
                .property("operationDate", new DateProperty())
                .property("quantity", new IntegerProperty())
                .property("display", new StringProperty())
                .property("status", new EnumProperty(OperationStatus.class)
                        ._enum(Arrays.asList("AWAITING_VALIDATION", "DISABLED", "VALIDATED", "NOT_COMPLETED", "SUBMITTED", "AWAITING_TREATMENT", "TREATED")))
                .property("observation", new StringProperty())
                .property("location", new RefProperty("#/definitions/LocationGet"))
                .property("uuid", new StringProperty());
        return model;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("productAttribute");
        description.addRequiredProperty("operationDate");
        description.addRequiredProperty("quantity");
        description.addRequiredProperty("status");
        description.addRequiredProperty("status");
        description.addRequiredProperty("location");
        description.addProperty("observation");
        description.addProperty("uuid");
        return description;
    }

    @Override
    public Model getCREATEModel(Representation rep) {
        ModelImpl model = (ModelImpl) super.getGETModel(rep);
        model.property("productAttribute", new StringProperty().example("uuid"))
                .property("location", new StringProperty().example("uuid"))
                .property("operationDate", new DateProperty())
                .property("quantity", new IntegerProperty())
                .property("status", new IntegerProperty())
                .property("observation", new StringProperty())
                .property("uuid", new StringProperty())
                .required("productAttribute")
                .required("operationDate")
                .required("quantity")
                .required("status")
                .required("location");

        if (rep instanceof FullRepresentation) {
            model.property("location", new RefProperty("#/definitions/LocationCreate"))
                    .property("productAttribute", new RefProperty("#/definitions/ProductAttributeCreate"));
        }
        return model;
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("productAttribute");
        description.addProperty("operationDate");
        description.addProperty("quantity");
        description.addProperty("status");
        description.addProperty("status");
        description.addProperty("location");
        description.addProperty("observation");
        return description;
    }

    @Override
    public Model getUPDATEModel(Representation rep) {
        ModelImpl model = (ModelImpl) super.getGETModel(rep);
        model.property("productAttribute", new RefProperty("#/definitions/ProductAttributeUpdate"))
                .property("operationDate", new DateProperty())
                .property("quantity", new IntegerProperty())
                .property("status", new IntegerProperty())
                .property("observation", new StringProperty())
                .property("location", new RefProperty("#/definitions/LocationGet"))
                .property("uuid", new StringProperty());
        return model;
    }

    @Override
    public ProductOperation getParent(ProductAttributeFlux productAttributeFlux) {
        return productAttributeFlux.getProductOperation();
    }

    @Override
    public void setParent(ProductAttributeFlux productAttributeFlux, ProductOperation productOperation) {
        productAttributeFlux.setProductOperation(productOperation);
    }

    @Override
    public PageableResult doGetAll(ProductOperation productOperation, RequestContext requestContext) throws ResponseException {
        List<ProductAttributeFlux> fluxes = new ArrayList<ProductAttributeFlux>();
        for (ProductAttributeFlux flux : productOperation.getProductAttributeFluxes()) {
            if (!flux.isVoided()) {
                fluxes.add(flux);
            }
        }
        return new NeedsPaging<ProductAttributeFlux>(fluxes, requestContext);
    }

    @PropertyGetter("display")
    public String getDisplayString(ProductAttributeFlux flux) {
        if (flux.getProductAttribute() == null)
            return "";

        return flux.getProductWithAttribute() + " : " + flux.getQuantity();
    }
}
