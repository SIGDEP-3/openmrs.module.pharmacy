package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.entities.*;
import org.openmrs.module.pharmacy.enumerations.*;
import org.openmrs.module.pharmacy.forms.inventory.ProductInventoryForm;
import org.openmrs.module.pharmacy.forms.inventory.InventoryAttributeFluxForm;
import org.openmrs.module.pharmacy.dto.ProductInventoryFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.forms.inventory.validators.ProductInventoryAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.forms.inventory.validators.ProductInventoryFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class PharmacyProductInventoryManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
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

    @ModelAttribute("isDirectClient")
    public Boolean isDirectClient() {
        return OperationUtils.isDirectClient(OperationUtils.getUserLocation());
    }

    @ModelAttribute("canDistribute")
    public Boolean canDistribute() {
        return OperationUtils.canDistribute(OperationUtils.getUserLocation());
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("inventories", inventoryService().getAllProductInventories(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
            modelMap.addAttribute("subTitle", "Liste des Inventaires");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
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
                    modelMap.addAttribute("program", productInventory.getProductProgram());
                }
            } else {
                ProductProgram program = programService().getOneProductProgramById(programId);
                if (program == null) {
                    HttpSession session = request.getSession();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez sélectionner un programme !");
                    return "redirect:/module/pharmacy/operations/inventory/list.form";
                }
                productInventoryForm = new ProductInventoryForm();
                productInventoryForm.setProductProgramId(program.getProductProgramId());
                productInventoryForm.setLocationId(OperationUtils.getUserLocation().getLocationId());

                modelMap.addAttribute("program", program);
            }

            modelMap.addAttribute("latestInventory", latestInventory(productInventoryForm.getProductInventory()));
            modelMap.addAttribute("productInventoryForm", productInventoryForm);
            modelMap.addAttribute("productInventory", inventoryService().getOneProductInventoryById(id));
            modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
            modelMap.addAttribute("subTitle", "Inventaire - Entête");
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

            new ProductInventoryFormValidation().validate(productInventoryForm, result);

            if (!result.hasErrors()) {
//                boolean idExist = (inventoryHeaderForm.getProductOperationId() != null);
                ProductInventory inventory = inventoryService().saveProductInventory(productInventoryForm.getProductInventory());
                inventory.setIncidence(Incidence.EQUAL);
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
            modelMap.addAttribute("latestInventory", latestInventory(productInventoryForm.getProductInventory()));
            modelMap.addAttribute("program", programService().getOneProductProgramById(productInventoryForm.getProductProgramId()));
            modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
            modelMap.addAttribute("subTitle", "Inventaire - entête");
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

                    if (inventoryAttributeFluxForm.getProductAttributeFluxId() == null) {
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
        modelMap.addAttribute("latestInventory", getLatestInventory(productInventory));
        modelMap.addAttribute("inventoryAttributeFluxForm", inventoryAttributeFluxForm);
        modelMap.addAttribute("productInventory", productInventory);
        modelMap.addAttribute("products", programService().getOneProductProgramById(productInventory.getProductProgram().getProductProgramId()).getProducts());
        Comparator<ProductInventoryFluxDTO> compareByProductFluxName = new Comparator<ProductInventoryFluxDTO>() {
            @Override
            public int compare(ProductInventoryFluxDTO o1, ProductInventoryFluxDTO o2) {
                return o1.getRetailName().compareTo(o2.getRetailName());
            }
        };
        if (productInventory.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
            List<ProductInventoryFluxDTO> productAttributeFluxes = inventoryService().getProductInventoryFluxDTOs(productInventory);
            if (productAttributeFluxes.size() != 0) {
                Collections.sort(productAttributeFluxes, compareByProductFluxName);
            }
            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
            modelMap.addAttribute("subTitle", "Inventaire <i class=\"fa fa-play\"></i> EN ATTENTE DE VALIDATION");
        } else if (productInventory.getOperationStatus().equals(OperationStatus.VALIDATED)) {
            List<ProductInventoryFluxDTO> productAttributeFluxes = inventoryService().getProductInventoryFluxValidatedDTO(productInventory);
            if (productAttributeFluxes.size() != 0) {
                Collections.sort(productAttributeFluxes, compareByProductFluxName);
            }
            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
            modelMap.addAttribute("subTitle", "Inventaire <i class=\"fa fa-play\"></i> APPROUVEE");
        } else if (productInventory.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)){
            if (getLatestInventory(productInventory) != null) {
                List<ProductAttributeStock> stocks = stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation(), false);
                for (ProductAttributeStock stock : stocks) {
                    if (productInventory.getProductProgram().getProducts().contains(stock.getProductAttribute().getProduct())) {
                        if (!stock.getQuantityInStock().equals(0)) {
                            ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxByAttributeAndOperation(
                                    stock.getProductAttribute(), productInventory
                            );

                            if (flux == null) {
                                flux = new ProductAttributeFlux();
                                flux.setQuantity(0);
                                flux.setOperationDate(productInventory.getOperationDate());
                                flux.setLocation(OperationUtils.getUserLocation());
                                flux.setProductAttribute(stock.getProductAttribute());
                                flux.setProductOperation(productInventory);
                                flux.setStatus(productInventory.getOperationStatus());
                                attributeFluxService().saveProductAttributeFlux(flux);
                            }
                        }
                    }
                }
            }
            List<ProductAttributeFlux> productAttributeFluxes = attributeFluxService().getAllProductAttributeFluxByOperation(productInventory, false);
            if (productAttributeFluxes.size() != 0) {
                Comparator<ProductAttributeFlux> compareByProductName = new Comparator<ProductAttributeFlux>() {
                @Override
                public int compare(ProductAttributeFlux o1, ProductAttributeFlux o2) {
                    return o1.getProductAttribute().getProduct().getRetailName().compareTo(o2.getProductAttribute().getProduct().getRetailName());
                }
            };
                Collections.sort(productAttributeFluxes, compareByProductName);
            }
            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);

            modelMap.addAttribute("subTitle", "Inventaire <i class=\"fa fa-play\"></i> ajout de produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "inventoryId") Integer inventoryId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductInventory inventory = inventoryService().getOneProductInventoryById(inventoryId);
        if (inventory != null) {
            if (inventory.getInventoryType().equals(InventoryType.PARTIAL)) {
                for (ProductAttributeFlux flux : attributeFluxService().getAllProductAttributeFluxByOperation(inventory, false)) {
                    if (flux.getQuantity().equals(0)) {
                        attributeFluxService().removeProductAttributeFlux(flux);
//                    inventory.removeProductAttributeFlux(flux);
                    }
                }
            }
            inventory.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
            inventoryService().saveProductInventory(inventory);
            attributeService().purgeUnusedAttributes();
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "L'inventaire a été enregistré avec " +
                    "succès et est en attente de validation !");
            return "redirect:/module/pharmacy/operations/inventory/list.form";
        }
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "L'inventaire n'a pas pu être enregistré !");
        return "redirect:/module/pharmacy/operations/inventory/editFlux.form";
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
                                  @RequestParam(value = "id") Integer id){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductInventory inventory = inventoryService().getOneProductInventoryById(id);
        for (ProductAttributeOtherFlux otherFlux : attributeFluxService().getAllProductAttributeOtherFluxByOperation(inventory, false)) {
            attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
        }
        for (ProductAttributeFlux flux : attributeFluxService().getAllProductAttributeFluxByOperation(inventory, false)){
            attributeFluxService().removeProductAttributeFlux(flux);
        }
        attributeService().purgeUnusedAttributes();
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
        ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
        if (flux != null) {
            HttpSession session = request.getSession();
            attributeFluxService().removeProductAttributeFlux(flux);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");
        }
        return "redirect:/module/pharmacy/operations/inventory/editFlux.form?inventoryId=" + inventoryId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/inventory/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "inventoryId") Integer inventoryId){
        if (!Context.isAuthenticated())
            return null;
        ProductInventory operation = inventoryService().getOneProductInventoryById(inventoryId);

        if (operation != null) {

            ProductMovementOut movementOut = new ProductMovementOut();
            ProductMovementEntry movementEntry = new ProductMovementEntry();

            for (ProductAttributeFlux flux : operation.getProductAttributeFluxes()) {
                ProductAttributeOtherFlux otherFlux = attributeFluxService().getOneProductAttributeOtherFluxByAttributeAndOperation(flux.getProductAttribute(), operation, operation.getLocation());
                if (otherFlux == null) {
                    otherFlux = new ProductAttributeOtherFlux();
                    otherFlux.setProductOperation(operation);
                    otherFlux.setProductAttribute(flux.getProductAttribute());
                    otherFlux.setLabel("Gap");
                    otherFlux.setLocation(OperationUtils.getUserLocation());
                }

                ProductAttributeStock stock = stockService().getOneProductAttributeStockByAttribute(flux.getProductAttribute(), OperationUtils.getUserLocation(), false);
                if (stock != null) {
                    otherFlux.setQuantity(flux.getQuantity().doubleValue() - stock.getQuantityInStock());
                    if (flux.getQuantity() > stock.getQuantityInStock()){
                        ProductAttributeFlux movementEntryFlux = new ProductAttributeFlux();
                        movementEntryFlux.setProductAttribute(flux.getProductAttribute());
                        movementEntryFlux.setStatus(OperationStatus.VALIDATED);
                        movementEntryFlux.setQuantity((flux.getQuantity() - stock.getQuantityInStock()));
                        movementEntryFlux.setLocation(flux.getLocation());
                        movementEntryFlux.setOperationDate(flux.getOperationDate());
                        movementEntry.addProductAttributeFlux(movementEntryFlux);
                    } else if (flux.getQuantity() < stock.getQuantityInStock()){
                        ProductAttributeFlux movementOutFlux = new ProductAttributeFlux();
                        movementOutFlux.setProductAttribute(flux.getProductAttribute());
                        movementOutFlux.setStatus(OperationStatus.VALIDATED);
                        movementOutFlux.setQuantity((stock.getQuantityInStock() - flux.getQuantity()));
                        movementOutFlux.setLocation(flux.getLocation());
                        movementOutFlux.setOperationDate(flux.getOperationDate());
                        movementOut.addProductAttributeFlux(movementOutFlux);
                    }
                } else {
                    otherFlux.setQuantity(flux.getQuantity().doubleValue());
                }
                attributeFluxService().saveProductAttributeOtherFlux(otherFlux);

                if (movementEntry.getProductAttributeFluxes().size() != 0) {
                    movementEntry.setOperationDate(operation.getOperationDate());
                    movementEntry.setStockEntryType(StockEntryType.POSITIVE_INVENTORY_ADJUSTMENT);
                    movementEntry.setIncidence(Incidence.POSITIVE);
                    movementEntry.setProductProgram(operation.getProductProgram());
                    movementEntry.setOperationStatus(OperationStatus.VALIDATED);
                    movementEntry.setLocation(operation.getLocation());
                    Context.getService(ProductMovementService.class).saveProductMovementEntry(movementEntry);
                }
                if (movementOut.getProductAttributeFluxes().size() != 0) {
                    movementOut.setOperationDate(operation.getOperationDate());
                    movementOut.setStockOutType(StockOutType.NEGATIVE_INVENTORY_ADJUSTMENT);
                    movementOut.setIncidence(Incidence.NEGATIVE);
                    movementOut.setProductProgram(operation.getProductProgram());
                    movementOut.setOperationStatus(OperationStatus.VALIDATED);
                    movementOut.setLocation(operation.getLocation());
                    Context.getService(ProductMovementService.class).saveProductMovementOut(movementOut);
                }
            }

            if (operation.getInventoryType().equals(InventoryType.TOTAL)) {
                OperationUtils.emptyStock(operation.getLocation(), operation.getProductProgram());
            }

            if (OperationUtils.validateOperation(operation)) {
                HttpSession session = request.getSession();
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Votre inventaire a été validé avec succès !");
                return "redirect:/module/pharmacy/operations/inventory/list.form";
            }
        }
        return null;
    }

    ProductInventory getLatestInventory(ProductInventory productInventory) {
        ProductInventory lastProductInventory;
        if (productInventory.getProductOperationId() == null) {
            lastProductInventory = inventoryService().getLastProductInventory(
                    Context.getLocationService().getDefaultLocation(),
                    productInventory.getProductProgram(), InventoryType.TOTAL);
        } else {
            lastProductInventory = inventoryService().getLastProductInventoryByDate(
                    OperationUtils.getUserLocation(),
                    productInventory.getProductProgram(),
                    productInventory.getOperationDate(), InventoryType.TOTAL);
        }
        if (lastProductInventory != null) {
            if (lastProductInventory.getProductOperationId().equals(productInventory.getProductOperationId())) {
                return null;
            } else {
                return lastProductInventory;
            }
        }
        return null;
    }

    ProductInventory latestInventory(ProductInventory productInventory) {
        ProductInventory inventory = inventoryService().getLastProductInventory(
                OperationUtils.getUserLocation(),
                productInventory.getProductProgram(),InventoryType.TOTAL);
        if (inventory != null) {
            if (inventory.getProductOperationId().equals(productInventory.getProductOperationId())) {
                return null;
            }  else {
                return inventory;
            }
        } else {
            inventory = inventoryService().getLastProductInventoryByDate(OperationUtils.getUserLocation(), productInventory.getProductProgram(), productInventory.getOperationDate(), InventoryType.TOTAL);
            return inventory;
        }
    }

}
