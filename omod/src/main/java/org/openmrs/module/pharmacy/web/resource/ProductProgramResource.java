package org.openmrs.module.pharmacy.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.Product;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.web.controller.PharmacyResourceController;
import org.openmrs.module.webservices.rest.SimpleObject;
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

import java.util.ArrayList;
import java.util.List;


@Resource(name = RestConstants.VERSION_1 + PharmacyResourceController.PHARMACY_REST_NAMESPACE + "/program",
        supportedClass = ProductProgram.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*", "1.11.*", "1.12.*", "2.*"})
public class ProductProgramResource extends DelegatingCrudResource<ProductProgram> {

    public ProductProgramResource() {
    }

    ProductProgramService getService() {
        return Context.getService(ProductProgramService.class);
    }

    @Override
    public ProductProgram getByUniqueId(String s) {
        return getService().getOneProductProgramByUuid(s);
    }

    @Override
    protected void delete(ProductProgram productProgram, String s, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public ProductProgram newDelegate() {
        return new ProductProgram();
    }

    @Override
    public ProductProgram save(ProductProgram productProgram) {
        return getService().saveProductProgram(productProgram);
    }

    @Override
    public void purge(ProductProgram productProgram, RequestContext requestContext) throws ResponseException {
        getService().removeProductProgram(productProgram);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = null;
        if (representation instanceof FullRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("name");
            description.addProperty("description");
            description.addProperty("products", Representation.DEFAULT);
            description.addProperty("uuid");
        } else if (representation instanceof DefaultRepresentation || representation instanceof RefRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("name");
            description.addProperty("uuid");
        }
        return description;
    }

    @Override
    public Model getGETModel(Representation rep) {
        ModelImpl model = (ModelImpl) super.getGETModel(rep);
        model.property("name", new StringProperty())
                .property("description", new StringProperty())
                .property("products", new ArrayProperty(new RefProperty("#/definitions/ProductGet")))
                .property("uuid", new StringProperty());
        return model;
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
    public Model getCREATEModel(Representation rep) {
        ModelImpl model = (ModelImpl) super.getGETModel(rep);
        model.property("name", new StringProperty())
                .property("description", new StringProperty())
                .property("uuid", new StringProperty())
                .required("name");
        return model;
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("name");
        description.addProperty("description");
        description.addProperty("uuid");
        return description;
    }

    @Override
    public Model getUPDATEModel(Representation rep) {
        ModelImpl model = (ModelImpl) super.getGETModel(rep);
        model.property("name", new StringProperty())
                .property("description", new StringProperty());
        return model;
    }

    @Override
    protected PageableResult doGetAll(RequestContext context) throws ResponseException {
        List<ProductProgram> programs = getService().getAllProductProgram();
        return new NeedsPaging<ProductProgram>(programs, context);
    }

    @Override
    protected PageableResult doSearch(RequestContext context) {
        List<ProductProgram> programs = new ArrayList<ProductProgram>();
        String name = context.getParameter("name");
        if (name != null && !name.isEmpty()) {
            ProductProgram productProgram = getService().getOneProductProgramByName(name);
            if (productProgram != null) {
                programs.add(productProgram);
                return new NeedsPaging<ProductProgram>(programs, context);
            }
        }

        return super.doSearch(context);
    }

    @Override
    public SimpleObject search(RequestContext context) throws ResponseException {
        return super.search(context);
    }
}
