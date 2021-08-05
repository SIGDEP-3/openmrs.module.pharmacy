package org.openmrs.module.pharmacy.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.ProductAttributeService;
import org.openmrs.module.pharmacy.api.ProductUnitService;
import org.openmrs.module.pharmacy.entities.ProductAttribute;
import org.openmrs.module.pharmacy.entities.ProductUnit;
import org.openmrs.module.pharmacy.utils.OperationUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Resource(name = RestConstants.VERSION_1 + PharmacyResourceController.PHARMACY_REST_NAMESPACE + "/product-unit",
        supportedClass = ProductUnit.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*", "1.11.*", "1.12.*", "2.*"})
public class ProductAttributeResource extends DelegatingCrudResource<ProductAttribute> {

    ProductAttributeService getService() {
        return Context.getService(ProductAttributeService.class);
    }

    @Override
    public ProductAttribute getByUniqueId(String s) {
        return getService().getOneProductAttributeByUuid(s);
    }

    @Override
    protected void delete(ProductAttribute productUnit, String s, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public ProductAttribute newDelegate() {
        return new ProductAttribute();
    }

    @Override
    public ProductAttribute save(ProductAttribute productUnit) {
        return getService().saveProductAttribute(productUnit);
    }

    @Override
    public void purge(ProductAttribute productAttribute, RequestContext requestContext) throws ResponseException {
        getService().removeProductAttribute(productAttribute);
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = null;
        if (representation instanceof FullRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("product", Representation.FULL);
            description.addProperty("batchNumber");
            description.addProperty("expiryDate");
            description.addProperty("location");
            description.addProperty("uuid");
        } else if (representation instanceof DefaultRepresentation || representation instanceof RefRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("product", Representation.DEFAULT);
            description.addProperty("batchNumber");
            description.addProperty("expiryDate");
            description.addProperty("location");
            description.addProperty("uuid");
        }
        return description;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("product");
        description.addRequiredProperty("batchNumber");
        description.addRequiredProperty("expiryDate");
        description.addRequiredProperty("location");
        description.addProperty("uuid");
        return description;
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("product");
        description.addProperty("batchNumber");
        description.addProperty("expiryDate");
        description.addProperty("location");
        description.addProperty("uuid");
        return description;
    }

    @Override
    protected PageableResult doGetAll(RequestContext context) throws ResponseException {
        return new NeedsPaging<ProductAttribute>(getService().getAllProductAttributes(false), context);
    }

    @Override
    protected PageableResult doSearch(RequestContext context) {
        String batchNumber = context.getParameter("batchNumber");
        String expiryDateString = context.getParameter("expiryDate");
        List<ProductAttribute> attributes = new ArrayList<ProductAttribute>();
        if (batchNumber != null) {
            if (expiryDateString == null) {
                attributes.add(getService().getOneProductAttributeByBatchNumber(batchNumber, OperationUtils.getUserLocation()));
            } else {
                try {
                    Date expiryDate = new SimpleDateFormat("dd/MM/yyyy").parse(expiryDateString);
                    attributes.add(getService().getOneProductAttributeByBatchNumberAndExpiryDate(batchNumber, expiryDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return new NeedsPaging<ProductAttribute>(attributes, context);
    }
}
