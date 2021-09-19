package org.openmrs.module.pharmacy.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.ProductInventoryService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.entities.ProductInventory;
import org.openmrs.module.pharmacy.entities.ProductOperation;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.entities.ProductUnit;
import org.openmrs.module.pharmacy.enumerations.InventoryType;
import org.openmrs.module.pharmacy.utils.OperationUtils;
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
import org.openmrs.util.OpenmrsUtil;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Resource(name = RestConstants.VERSION_1 + PharmacyResourceController.PHARMACY_REST_NAMESPACE + "/inventory",
        supportedClass = ProductUnit.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*", "1.11.*", "1.12.*", "2.*"})
public class ProductInventoryResource extends DelegatingCrudResource<ProductInventory> {

    ProductInventoryService getService() {
        return Context.getService(ProductInventoryService.class);
    }

    @Override
    public ProductInventory getByUniqueId(String s) {
        return getService().getOneProductInventoryByUuid(s);
    }

    @Override
    protected void delete(ProductInventory inventory, String s, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public ProductInventory newDelegate() {
        return new ProductInventory();
    }

    @Override
    public ProductInventory save(ProductInventory productUnit) {
        return getService().saveProductInventory(productUnit);
    }

    @Override
    public void purge(ProductInventory productInventory, RequestContext requestContext) throws ResponseException {
        getService().removeProductInventory(productInventory);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = null;
        if (representation instanceof FullRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("inventoryStartDate");
            description.addProperty("inventoryType");
            description.addProperty("operationNumber");
            description.addProperty("productProgram", Representation.FULL);
            description.addProperty("operationDate");
            description.addProperty("location", Representation.FULL);
            description.addProperty("operationStatus");
            description.addProperty("incidence");
            description.addProperty("observation");
            description.addProperty("productAttributeFluxes", Representation.FULL);
            description.addProperty("productAttributeOtherFluxes", Representation.FULL);
            description.addProperty("uuid");
        } else if (representation instanceof DefaultRepresentation || representation instanceof RefRepresentation) {
            description = new DelegatingResourceDescription();

            description.addProperty("inventoryStartDate");
            description.addProperty("inventoryType");
            description.addProperty("operationNumber");
            description.addProperty("productProgram", Representation.DEFAULT);
            description.addProperty("operationDate");
            description.addProperty("location", Representation.DEFAULT);
            description.addProperty("operationStatus");
            description.addProperty("incidence");
            description.addProperty("observation");
            description.addProperty("productAttributeFluxes", Representation.DEFAULT);
            description.addProperty("productAttributeOtherFluxes", Representation.DEFAULT);
            description.addProperty("uuid");
        }
        return description;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("inventoryStartDate");
        description.addRequiredProperty("inventoryType");
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
        description.addProperty("inventoryStartDate");
        description.addProperty("inventoryType");
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
        return new NeedsPaging<ProductInventory>(getService().getAllProductInventories(false), context);
    }

    @Override
    protected PageableResult doSearch(RequestContext context) {
        String period = context.getParameter("period");
        String inventoryType = context.getParameter("type");
        String program = context.getParameter("productProgram");

        List<ProductInventory> productInventories = new ArrayList<ProductInventory>();

        if (!StringUtils.isEmpty(program) && !StringUtils.isEmpty(period) && !StringUtils.isEmpty(inventoryType)) {
            ProductProgram productProgram = Context.getService(ProductProgramService.class).getOneProductProgramByUuid(program);
            if (productProgram != null) {
                InventoryType type = inventoryType.equals("TOTAL") ? InventoryType.TOTAL : InventoryType.PARTIAL;
                productInventories.add(getService().getOneProductInventoryByOperationNumber(
                        OperationUtils.getUserLocation(),
                        productProgram,
                        period,
                        type)
                );

            }
        }

        return new NeedsPaging<ProductInventory>(productInventories, context);
    }
}
