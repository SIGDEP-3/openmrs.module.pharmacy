package org.openmrs.module.pharmacy.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.entities.*;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.web.controller.PharmacyResourceController;
import org.openmrs.module.webservices.docs.swagger.core.property.EnumProperty;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.util.StringUtils;

import java.util.*;


@Resource(name = RestConstants.VERSION_1 + PharmacyResourceController.PHARMACY_REST_NAMESPACE + "/operation",
        supportedClass = ProductOperation.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*", "1.11.*", "1.12.*", "2.*"})
public class ProductOperationResource extends DataDelegatingCrudResource<ProductOperation> {

    PharmacyService getService() {
        return Context.getService(PharmacyService.class);
    }

    @Override
    public ProductOperation getByUniqueId(String s) {
        return getService().getOneProductOperationByUuid(s);
    }

    @Override
    protected void delete(ProductOperation operation, String s, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public ProductOperation newDelegate() {
        return new ProductOperation();
    }

    @Override
    public ProductOperation save(ProductOperation productUnit) {
        return getService().saveProductOperation(productUnit);
    }

    @Override
    public void purge(ProductOperation productOperation, RequestContext requestContext) throws ResponseException {
        getService().removeProductOperation(productOperation);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = null;
        if (representation instanceof FullRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("operationNumber");
            description.addProperty("productProgram", Representation.DEFAULT);
            description.addProperty("operationDate");
            description.addProperty("location", Representation.REF);
            description.addProperty("operationStatus");
            description.addProperty("incidence");
            description.addProperty("observation");
            description.addProperty("attributeFluxes", Representation.DEFAULT);
            description.addProperty("attributeOtherFluxes", Representation.DEFAULT);
            description.addProperty("fluxProducts", Representation.DEFAULT);
            description.addProperty("otherFluxProducts", Representation.DEFAULT);
            description.addProperty("voided");
            description.addProperty("uuid");
        } else if (representation instanceof DefaultRepresentation || representation instanceof RefRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("operationNumber");
            description.addProperty("productProgram", Representation.DEFAULT);
            description.addProperty("operationDate");
            description.addProperty("location", Representation.REF);
            description.addProperty("operationStatus");
            description.addProperty("incidence");
            description.addProperty("observation");
            description.addProperty("attributeFluxes", Representation.DEFAULT);
            description.addProperty("attributeOtherFluxes", Representation.DEFAULT);
            description.addProperty("voided");
            description.addProperty("uuid");
        }
        return description;
    }

    @Override
    public List<String> getPropertiesToExposeAsSubResources() {
        return Arrays.asList("attributeFluxes", "attributeOtherFluxes");
    }

    @PropertyGetter("fluxProducts")
    public static List<Product> getFluxProducts(ProductOperation operation) {
        return operation.getFluxesProductList();
    }

    @PropertyGetter("otherFluxProducts")
    public static List<Product> getOtherFluxProducts(ProductOperation operation) {
        return operation.getOtherFluxesProductList();
    }

    @PropertyGetter("attributeFluxes")
    public static Set<ProductAttributeFlux> getFluxes(ProductOperation operation) {
        return new LinkedHashSet<ProductAttributeFlux>(operation.getProductAttributeFluxes());
    }

    @PropertySetter("attributeFluxes")
    public static void setFluxes(ProductOperation operation, List<ProductAttributeFlux> fluxes)
            throws ResourceDoesNotSupportOperationException {
        if (operation.getProductAttributeFluxes() != null && operation.getProductAttributeFluxes().containsAll(fluxes)) {
            return;
        }
        if (operation.getProductAttributeFluxes() != null && !operation.getProductAttributeFluxes().isEmpty()) {
            throw new ResourceDoesNotSupportOperationException("Fluxes can only be set for newly created objects !");
        }
        if (fluxes == null || fluxes.isEmpty()) {
            throw new ResourceDoesNotSupportOperationException("At least one flux required");
        }
        for (ProductAttributeFlux flux : fluxes) {
            ProductAttributeFlux existingFlux = operation.getProductAttributeFluxes() != null ?
                    getMatchingFlux(flux, operation.getProductAttributeFluxes()) : null;
            if (existingFlux != null) {
                copyFluxFields(existingFlux, flux);
            } else {
                operation.addProductAttributeFlux(flux);
            }
        }
    }

    private static void copyFluxFields(ProductAttributeFlux existingFlux, ProductAttributeFlux flux) {
        existingFlux.setStatus(flux.getStatus());
        existingFlux.setOperationDate(flux.getOperationDate());
        existingFlux.setProductAttribute(flux.getProductAttribute());
        existingFlux.setVoided(flux.getVoided());
        existingFlux.setQuantity(flux.getQuantity());
        existingFlux.setObservation(flux.getObservation());
        existingFlux.setLocation(flux.getLocation());
    }

    private static ProductAttributeFlux getMatchingFlux(ProductAttributeFlux flux, Set<ProductAttributeFlux> productAttributeFluxes) {
        for (ProductAttributeFlux existingFlux : productAttributeFluxes) {
            if (existingFlux.getUuid() != null && existingFlux.getUuid().equals(flux.getUuid())) {
                return existingFlux;
            }
        }
        return null;
    }

    @PropertyGetter("attributeOtherFluxes")
    public static Set<ProductAttributeOtherFlux> getOtherFluxes(ProductOperation operation) {
        return new LinkedHashSet<ProductAttributeOtherFlux>(operation.getProductAttributeOtherFluxes());
    }

    @PropertySetter("attributeOtherFluxes")
    public static void setOtherFluxes(ProductOperation operation, List<ProductAttributeOtherFlux> fluxes)
            throws ResourceDoesNotSupportOperationException {
        if (operation.getProductAttributeOtherFluxes() != null && operation.getProductAttributeOtherFluxes().containsAll(fluxes)) {
            return;
        }
        if (operation.getProductAttributeOtherFluxes() != null && !operation.getProductAttributeOtherFluxes().isEmpty()) {
            throw new ResourceDoesNotSupportOperationException("Other fluxes can only be set for newly created objects !");
        }
        if (fluxes == null || fluxes.isEmpty()) {
            throw new ResourceDoesNotSupportOperationException("At least one other flux required");
        }
        for (ProductAttributeOtherFlux flux : fluxes) {
            ProductAttributeOtherFlux existingFlux = operation.getProductAttributeOtherFluxes() != null ?
                    getMatchingOtherFlux(flux, operation.getProductAttributeOtherFluxes()) : null;
            if (existingFlux != null) {
                copyOtherFluxFields(existingFlux, flux);
            } else {
                operation.addProductAttributeOtherFlux(flux);
            }
        }
    }

    private static void copyOtherFluxFields(ProductAttributeOtherFlux existingFlux, ProductAttributeOtherFlux flux) {
        existingFlux.setProduct(flux.getProduct());
        existingFlux.setProductAttribute(flux.getProductAttribute());
        existingFlux.setLabel(flux.getLabel());
        existingFlux.setQuantity(flux.getQuantity());
        existingFlux.setLocation(flux.getLocation());
    }

    private static ProductAttributeOtherFlux getMatchingOtherFlux(ProductAttributeOtherFlux flux, Set<ProductAttributeOtherFlux> productAttributeFluxes) {
        for (ProductAttributeOtherFlux existingFlux : productAttributeFluxes) {
            if (existingFlux.getUuid() != null && existingFlux.getUuid().equals(flux.getUuid())) {
                return existingFlux;
            }
        }
        return null;
    }

    @Override
    public Model getGETModel(Representation rep) {
        ModelImpl model = new ModelImpl();
        model.property("operationNumber", new StringProperty())
                .property("productProgram", new StringProperty())
                .property("operationDate", new StringProperty())
                .property("location", new StringProperty())
                .property("operationStatus", new EnumProperty(OperationStatus.class)
                        ._enum(Arrays.asList("AWAITING_VALIDATION", "DISABLED", "VALIDATED", "NOT_COMPLETED", "SUBMITTED", "AWAITING_TREATMENT", "TREATED")))
                .property("incidence", new EnumProperty(Incidence.class)
                        ._enum(Arrays.asList(Incidence.EQUAL.name(), Incidence.NONE.name(), Incidence.POSITIVE.name(), Incidence.NEGATIVE.name())))
                .property("observation", new StringProperty())
                .property("attributeFluxes", new ArrayProperty(new RefProperty("#/definitions/ProductAttributeOtherFluxGet")))
                .property("attributeOtherFluxes", new ArrayProperty(new RefProperty("#/definitions/ProductAttributeOtherFluxGet")))
                .property("fluxProducts", new ArrayProperty(new RefProperty("#/definitions/ProductGet")))
                .property("otherFluxProducts", new ArrayProperty(new RefProperty("#/definitions/ProductGet")))
                .property("voided", new BooleanProperty())
                .property("uuid", new StringProperty());

        return model;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("operationNumber");
        description.addRequiredProperty("productProgram");
        description.addRequiredProperty("operationDate");
        description.addRequiredProperty("location");
        description.addRequiredProperty("operationStatus");
        description.addRequiredProperty("incidence");
        description.addRequiredProperty("productAttributeFluxes");
        description.addRequiredProperty("productAttributeOtherFluxes");
        description.addProperty("observation");
        description.addProperty("uuid");
        return description;
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("operationNumber");
        description.addProperty("productProgram");
        description.addProperty("operationDate");
        description.addProperty("location");
        description.addProperty("operationStatus");
        description.addProperty("incidence");
        description.addProperty("productAttributeFluxes");
        description.addProperty("productAttributeOtherFluxes");
        description.addProperty("observation");
        description.addProperty("uuid");
        return description;
    }

    @Override
    protected PageableResult doGetAll(RequestContext context) throws ResponseException {
        return new NeedsPaging<ProductOperation>(getService().getAll(false), context);
    }

    @Override
    protected PageableResult doSearch(RequestContext context) {
        String period = context.getParameter("period");
        String inventoryType = context.getParameter("type");
        String program = context.getParameter("productProgram");

        List<ProductOperation> productInventories = new ArrayList<ProductOperation>();

        if (!StringUtils.isEmpty(program) && !StringUtils.isEmpty(period) && !StringUtils.isEmpty(inventoryType)) {
            ProductProgram productProgram = Context.getService(ProductProgramService.class).getOneProductProgramByUuid(program);
//            if (productProgram != null) {
//                OperationType type = inventoryType.equals("TOTAL") ? OperationType.TOTAL : OperationType.PARTIAL;
//                productInventories.add(getService().getOneProductOperationByOperationNumber(
//                        OperationUtils.getUserLocation(),
//                        productProgram,
//                        period,
//                        type)
//                );
//
//            }
        }

        return new NeedsPaging<ProductOperation>(productInventories, context);
    }
}
