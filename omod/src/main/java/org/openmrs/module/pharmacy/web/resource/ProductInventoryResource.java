package org.openmrs.module.pharmacy.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductInventoryService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.entities.ProductInventory;
import org.openmrs.module.pharmacy.entities.ProductOperation;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.entities.ProductUnit;
import org.openmrs.module.pharmacy.enumerations.InventoryType;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.web.controller.PharmacyResourceController;
import org.openmrs.module.webservices.docs.swagger.core.property.EnumProperty;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
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
import org.openmrs.module.webservices.rest.web.response.ConversionException;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.validation.ValidateUtil;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Resource(name = RestConstants.VERSION_1 + PharmacyResourceController.PHARMACY_REST_NAMESPACE + "/inventory",
        supportedClass = ProductUnit.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*", "1.11.*", "1.12.*", "2.*"})
public class ProductInventoryResource extends DataDelegatingCrudResource<ProductInventory> {

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
            description.addProperty("productOperation", Representation.DEFAULT);
            description.addProperty("uuid");

        } else if (representation instanceof DefaultRepresentation || representation instanceof RefRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("inventoryStartDate");
            description.addProperty("inventoryType");
            description.addProperty("productOperation", Representation.DEFAULT);
            description.addProperty("uuid");
        }
        return description;
    }

    @PropertyGetter("uuid")
    public static String getUuid(ProductInventory inventory) {
        return inventory.getUuid();
    }

    @PropertyGetter("productOperation")
    public static ProductOperation getOperation(ProductInventory inventory) {
        return new ProductOperation(inventory);
    }

    @PropertySetter("productOperation")
    public static void setOperation(ProductInventory instance, String operationUuid) {
    }

    @Override
    public Model getGETModel(Representation rep) {
        ModelImpl model = (ModelImpl) super.getGETModel(rep);
        model.property("inventoryStartDate", new DateProperty())
                .property("inventoryType", new EnumProperty(InventoryType.class))
                ._enum(Arrays.asList(InventoryType.TOTAL.name(), InventoryType.PARTIAL.name()))
                .property("productOperation", new RefProperty("#/definitions/ProductOperationGet"))
                .property("uuid", new StringProperty());
        return model;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("inventoryStartDate");
        description.addRequiredProperty("inventoryType");
        description.addRequiredProperty("productOperation");
        return description;
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("inventoryStartDate");
        description.addProperty("inventoryType");
        description.addProperty("productOperation");
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

    @Override
    public Object create(SimpleObject propertiesToCreate, RequestContext context) throws ResponseException {
        ProductInventory delegate = getInventory(propertiesToCreate);
        ValidateUtil.validate(delegate);
        delegate = save(delegate);
        return ConversionUtil.convertToRepresentation(delegate, Representation.DEFAULT);
    }

    public ProductInventory getInventory(SimpleObject propertiesToCreate) {
        Object operationProperty = propertiesToCreate.get("productOperation");
        ProductOperation operation = null;
        if (operationProperty == null) {
            throw new ConversionException("The person property is missing");
        } else if (operationProperty instanceof String){
            operation = Context.getService(PharmacyService.class).getOneProductOperationByUuid((String) operationProperty);
            Context.evictFromSession(operation);
        } else if (operationProperty instanceof Map) {
            operation = (ProductOperation) ConversionUtil.convert(operationProperty, ProductOperation.class);
            propertiesToCreate.put("productOperation", "");
        }

        ProductInventory delegate = new ProductInventory(operation);
        setConvertedProperties(delegate, propertiesToCreate, getCreatableProperties(), true);
        return delegate;
    }
}
