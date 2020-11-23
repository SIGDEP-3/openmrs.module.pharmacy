package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductAttribute;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.forms.ProductInventoryForm;
import org.openmrs.module.pharmacy.forms.InventoryAttributeFluxForm;
import org.openmrs.module.pharmacy.models.ProductInventoryFluxDTO;
import org.openmrs.module.pharmacy.validators.ProductInventoryAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.validators.ProductInventoryHeaderFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
public class PharmacyProductInventoryManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private ProductService productService() {
        return Context.getService(ProductService.class);
    }

    private ProductInventoryService inventoryService() {
        return Context.getService(ProductInventoryService.class);
    }

    private ProductProgramService programService(){
        return Context.getService(ProductProgramService.class);
    }

    private ProductSupplierService supplierService(){
        return Context.getService(ProductSupplierService.class);
    }

    private ProductAttributeFluxService attributeFluxService(){
        return Context.getService(ProductAttributeFluxService.class);
    }

    private ProductAttributeService attributeService(){
        return Context.getService(ProductAttributeService.class);
    }

    @ModelAttribute("title")
    public String getTile() {
        return "Inventaire de produits";
    }

    public Location getUserLocation() {
        return Context.getLocationService().getDefaultLocation();
//        return Context.getUserContext().getLocation();
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("inventories", inventoryService().getAllProductInventories(getUserLocation(), false));
            modelMap.addAttribute("subTitle", "Liste des Inventaires");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                     @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                     ProductInventoryForm productInventoryForm) {
        if (Context.isAuthenticated()) {
            if (id != 0) {
                ProductInventory productInventory = inventoryService().getOneProductInventoryById(id);
                if (productInventory != null) {
                    if (!productInventory.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        return "redirect:/module/pharmacy/operations/inventory/editFlux.form?inventoryId=" +
                            productInventory.getProductOperationId();
                    }
                    productInventoryForm.setProductInventory(productInventory);
                }
            } else {
                productInventoryForm = new ProductInventoryForm();
                productInventoryForm.setLocationId(getUserLocation().getLocationId());
            }

            modelMap.addAttribute("inventoryHeaderForm", productInventoryForm);
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("productInventory", inventoryService().getOneProductInventoryById(id));
            modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
            modelMap.addAttribute("subTitle", "Saisie d'inventaire - Entête");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                       ProductInventoryForm productInventoryForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductInventoryHeaderFormValidation().validate(productInventoryForm, result);

            if (!result.hasErrors()) {
//                boolean idExist = (inventoryHeaderForm.getProductOperationId() != null);
                ProductInventory inventory = inventoryService().saveProductInventory(productInventoryForm.getProductInventory());

                if (action.equals("addLine")) {
                    if (inventory.getProductAttributeFluxes().size() == 0) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                    }
                    return "redirect:/module/pharmacy/operations/inventory/editFlux.form?inventoryId=" +
                            inventory.getProductOperationId();
                } else {
                    return "redirect:/module/pharmacy/operations/inventory/list.form";
                }
            }
            modelMap.addAttribute("inventoryHeaderForm", productInventoryForm);
//            modelMap.addAttribute("product", inventoryHeaderForm.getProduct());
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
            modelMap.addAttribute("subTitle", "Saisie  de l'inventaire - entête");
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                         @RequestParam(value = "inventoryId") Integer inventoryId,
                         @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                         InventoryAttributeFluxForm inventoryAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductInventory productInventory = inventoryService().getOneProductInventoryById(inventoryId);
            if (fluxId != 0) {
                ProductAttributeFlux productAttributeFlux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
                if (productAttributeFlux != null) {
                    inventoryAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productInventory);
                } else {
                    inventoryAttributeFluxForm = new InventoryAttributeFluxForm();
                    inventoryAttributeFluxForm.setProductOperationId(inventoryId);
                }
            } else {
                inventoryAttributeFluxForm = new InventoryAttributeFluxForm();
                inventoryAttributeFluxForm.setProductOperationId(productInventory.getProductOperationId());
            }
            modelMappingForView(modelMap, inventoryAttributeFluxForm, productInventory);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/editFlux.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           HttpServletRequest request,
                           InventoryAttributeFluxForm inventoryAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductInventoryAttributeFluxFormValidation().validate(inventoryAttributeFluxForm, result);
            ProductInventory productInventory = inventoryService().getOneProductInventoryById(inventoryAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = attributeService().saveProductAttribute(inventoryAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux productAttributeFlux = inventoryAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    productAttributeFlux.setStatus(productInventory.getOperationStatus());
                    attributeFluxService().saveProductAttributeFlux(productAttributeFlux);
//                    if (attributeFluxService().saveProductAttributeFlux(productAttributeFlux) != null) {
//                        //attributeFluxService().saveProductAttributeOtherFlux(inventoryAttributeFluxForm.getProductAttributeOtherFlux());
//                    }

                    if (inventoryAttributeFluxForm.getProductOperationId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/inventory/editFlux.form?inventoryId="
                            + inventoryAttributeFluxForm.getProductOperationId();
                }
            }

            modelMappingForView(modelMap, inventoryAttributeFluxForm, productInventory);
        }

        return null;
    }

    private void modelMappingForView(ModelMap modelMap, InventoryAttributeFluxForm inventoryAttributeFluxForm, ProductInventory productInventory) {
        List<ProductAttributeFlux> productAttributeFluxes = attributeFluxService().getAllProductAttributeFluxByOperation(productInventory, false);
        if (productAttributeFluxes.size() != 0) {
            Collections.sort(productAttributeFluxes, Collections.<ProductAttributeFlux>reverseOrder());
        }

//        List<ProductAttributeFlux> productAttributeFluxList = attributeFluxService().getAllProductAttributeFluxByOperation(productInventory, false);
        modelMap.addAttribute("inventoryAttributeFluxForm", inventoryAttributeFluxForm);
        modelMap.addAttribute("productInventory", productInventory);
        modelMap.addAttribute("products", programService().getOneProductProgramById(productInventory.getProductProgram().getProductProgramId()).getProducts());
        modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
        if (!productInventory.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
            if (productInventory.getOperationStatus().equals(OperationStatus.VALIDATED))
            modelMap.addAttribute("subTitle", "Inventaire - APPROUVEE");
            else if (productInventory.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
                modelMap.addAttribute("subTitle", "Inventaire - EN ATTENTE DE VALIDATION");
            }
        } else {
            modelMap.addAttribute("subTitle", "Saisie de l'Inventaire - ajout de produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "inventoryId") Integer inventoryId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductInventory inventory = inventoryService().getOneProductInventoryById(inventoryId);
        inventory.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
        inventoryService().saveProductInventory(inventory);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "L'inventaire a été enregistré avec " +
                "succès et est en attente de validation !");
        return "redirect:/module/pharmacy/operations/inventory/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/incomplete.form", method = RequestMethod.GET)
    public String incomplete(HttpServletRequest request,
                             @RequestParam(value = "inventoryId") Integer inventoryId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductInventory inventory = inventoryService().getOneProductInventoryById(inventoryId);
        inventory.setOperationStatus(OperationStatus.NOT_COMPLETED);
        inventoryService().saveProductInventory(inventory);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                "continuer à modifier l'inventaire !");
        return "redirect:/module/pharmacy/operations/inventory/editFlux.form?inventoryId=" + inventoryId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/delete.form", method = RequestMethod.GET)
    public String deleteOperation(HttpServletRequest request,
                                  @RequestParam(value = "inventoryId") Integer inventoryId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductInventory inventory = inventoryService().getOneProductInventoryById(inventoryId);
        for (ProductAttributeOtherFlux otherFlux : attributeFluxService().getAllProductAttributeOtherFluxByOperation(inventory, false)) {
            attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
        }
        for (ProductAttributeFlux flux : attributeFluxService().getAllProductAttributeFluxByOperation(inventory, false)){
            attributeFluxService().removeProductAttributeFlux(flux);
        }
        inventoryService().removeProductInventory(inventory);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "L'inventaire a été supprimé avec succès !");
        return "redirect:/module/pharmacy/operations/inventory/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "inventoryId") Integer inventoryId,
                             @RequestParam(value = "fluxId") Integer fluxId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
        if (flux != null) {
            attributeFluxService().removeProductAttributeFlux(flux);
            ProductAttributeOtherFlux otherFlux = attributeFluxService()
                    .getOneProductAttributeOtherFluxByAttributeAndOperation(flux.getProductAttribute(), flux.getProductOperation());
            if (otherFlux != null) {
                attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
            }
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");
        }
        return "redirect:/module/pharmacy/operations/inventory/editFlux.form?inventoryId=" + inventoryId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "inventoryId") Integer inventoryId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductInventory inventory = inventoryService().getOneProductInventoryById(inventoryId);
        if (service().validateOperation(inventory)) {
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                    "continuer à modifier l'inventaire !");
            return "redirect:/module/pharmacy/operations/inventory/list.form";
        }
        return null;
    }

}
