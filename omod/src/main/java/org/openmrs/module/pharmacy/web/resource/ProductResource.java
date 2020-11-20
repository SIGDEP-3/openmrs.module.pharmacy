package org.openmrs.module.pharmacy.web.resource;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductUnit;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.api.ProductUnitService;
import org.openmrs.module.pharmacy.web.controller.PharmacyResourceController;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.List;


@Resource(name = RestConstants.VERSION_1 + PharmacyResourceController.PHARMACY_REST_NAMESPACE + "/product",
        supportedClass = Product.class, supportedOpenmrsVersions = {"1.8.*", "1.9.*", "1.11.*", "1.12.*", "2.*"})
public class ProductResource extends DelegatingCrudResource<Product> {

    ProductService getService() {
        return Context.getService(ProductService.class);
    }
    ProductUnitService unitService() {
        return Context.getService(ProductUnitService.class);
    }

    @Override
    public Product getByUniqueId(String s) {
        return getService().getOneProductByUuid(s);
    }

    @Override
    protected void delete(Product product, String s, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public Product newDelegate() {
        return new Product();
    }

    @Override
    public Product save(Product product) {
        return getService().saveProduct(product);
    }

    @Override
    public void purge(Product product, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
        DelegatingResourceDescription description = null;
        if (representation instanceof FullRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("code");
            description.addProperty("retailName");
            description.addProperty("wholesaleName");
            description.addProperty("productRetailUnit", Representation.FULL);
            description.addProperty("productWholesaleUnit", Representation.FULL);
            description.addProperty("unitConversion");
            description.addProperty("productPrograms", Representation.DEFAULT);
            description.addProperty("productRegimens", Representation.DEFAULT);
            description.addProperty("uuid");
        } else if (representation instanceof DefaultRepresentation) {
            description = new DelegatingResourceDescription();
            description.addProperty("code");
            description.addProperty("retailName");
            description.addProperty("wholesaleName");
            description.addProperty("productRetailUnit", Representation.DEFAULT);
            description.addProperty("productWholesaleUnit", Representation.DEFAULT);
            description.addProperty("unitConversion");
            description.addProperty("uuid");
        }
        return description;
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        return getDelegatingResourceDescription();
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
        return getDelegatingResourceDescription();
    }

    private DelegatingResourceDescription getDelegatingResourceDescription() {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addRequiredProperty("code");
        description.addRequiredProperty("retailName");
        description.addRequiredProperty("wholesaleName");
        description.addRequiredProperty("productRetailUnit");
        description.addRequiredProperty("productWholesaleUnit");
        description.addRequiredProperty("unitConversion");
        description.addProperty("uuid");
        return description;
    }

    @Override
    protected NeedsPaging<Product> doGetAll(RequestContext context) throws ResponseException {
        List<Product> products = getService().getAllProduct();
        return new NeedsPaging<Product>(products, context);
    }

    @Override
    protected PageableResult doSearch(RequestContext context) {

        String name = context.getRequest().getParameter("name");
        String retailUnitName = context.getRequest().getParameter("retailUnit");
        String wholesaleUnitName = context.getRequest().getParameter("wholesaleUnit");

        List<Product> products = null;

        if (StringUtils.isNotBlank(name)) {
            products = getService().searchProductByNameLike(name);
        } else if (StringUtils.isNotBlank(retailUnitName)) {
            ProductUnit productUnit = unitService().getOneProductUnitByName(retailUnitName);
            if (productUnit != null) {
                products = getService().getAllProductByRetailUnit(productUnit);
            }
        } else if (StringUtils.isNotBlank(wholesaleUnitName)) {
            ProductUnit productUnit = unitService().getOneProductUnitByName(wholesaleUnitName);
            if (productUnit != null) {
                products = getService().getAllProductByWholesaleUnit(productUnit);
            }
        }
        return new NeedsPaging<Product>(products, context);
    }
}
