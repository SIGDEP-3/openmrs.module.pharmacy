package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.entities.*;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.forms.movements.ProductBackSupplierForm;
import org.openmrs.module.pharmacy.forms.movements.BackSupplierAttributeFluxForm;
import org.openmrs.module.pharmacy.dto.ProductOutFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.forms.movements.validators.ProductBackSupplierAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.forms.movements.validators.ProductBackSupplierFormValidation;
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
import java.util.Set;

@Controller
public class PharmacyProductBackSupplierManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }

    private ProductBackSupplierService backSupplierService() {
        return Context.getService(ProductBackSupplierService.class);
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
        return "Retour de produits";
    }

    @ModelAttribute("isDirectClient")
    public Boolean isDirectClient() {
        return OperationUtils.isDirectClient(OperationUtils.getUserLocation());
    }

    @ModelAttribute("canDistribute")
    public Boolean canDistribute() {
        return OperationUtils.canDistribute(OperationUtils.getUserLocation());
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/site-back/list.form", method = RequestMethod.GET)
    public void listSite(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("siteBacks", backSupplierService().getAllProductBackSuppliers(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
            modelMap.addAttribute("subTitle", "Liste des retours au fournisseur");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/site-back/edit.form", method = RequestMethod.GET)
    public String editSite(ModelMap modelMap,
                               HttpServletRequest request,
                               @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                               @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                               ProductBackSupplierForm productBackSupplierForm) {
        if (Context.isAuthenticated()) {
            if (id != 0) {
                ProductBackSupplier productBackSupplier = backSupplierService().getOneProductBackSupplierById(id);
                if (productBackSupplier != null) {
                    if (!productBackSupplier.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        return "redirect:/module/pharmacy/operations/movement/site-back/editFlux.form?backSupplierId=" +
                                productBackSupplier.getProductOperationId();
                    }
                    productBackSupplierForm.setProductBackSupplier(productBackSupplier);
                    modelMap.addAttribute("program", productBackSupplier.getProductProgram());
                }
            } else {
                ProductProgram program = programService().getOneProductProgramById(programId);
                if (program == null) {
                    HttpSession session = request.getSession();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez sélectionner un programme !");
                    return "redirect:/module/pharmacy/operations/movement/site-back/list.form";
                }
                productBackSupplierForm = new ProductBackSupplierForm();
                productBackSupplierForm.setIncidence(Incidence.NEGATIVE);
                productBackSupplierForm.setOperationNumber(OperationUtils.generateNumber());
                productBackSupplierForm.setProductProgramId(program.getProductProgramId());
                productBackSupplierForm.setLocationId(OperationUtils.getUserLocation().getLocationId());
                productBackSupplierForm.setExchangeLocationId(OperationUtils.getUserLocation().getParentLocation().getLocationId());
                modelMap.addAttribute("program", program);
            }

            modelMap.addAttribute("productBackSupplierForm", productBackSupplierForm);
            modelMap.addAttribute("productBackSupplier", backSupplierService().getOneProductBackSupplierById(id));
            modelMap.addAttribute("supplier", OperationUtils.getUserLocation().getParentLocation());
            modelMap.addAttribute("subTitle", "Retour de produit au fournisseur <i class=\"fa fa-play\"></i> Entête");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/site-back/edit.form", method = RequestMethod.POST)
    public String saveSite(ModelMap modelMap,
                               HttpServletRequest request,
                               @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                               ProductBackSupplierForm productBackSupplierForm,
                               BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductBackSupplierFormValidation().validate(productBackSupplierForm, result);

            if (!result.hasErrors()) {
                ProductBackSupplier backSupplier = backSupplierService().saveProductBackSupplier(productBackSupplierForm.getProductBackSupplier());
                if (action.equals("addLine")) {
                    if (backSupplier.getProductAttributeFluxes().size() == 0) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                    }
                    return "redirect:/module/pharmacy/operations/movement/site-back/editFlux.form?backSupplierId=" +
                            backSupplier.getProductOperationId();
                } else {
                    return "redirect:/module/pharmacy/operations/movement/site-back/list.form";
                }
            }
            modelMap.addAttribute("backSupplierHeaderForm", productBackSupplierForm);
//            modelMap.addAttribute("latestBackSupplier", latestBackSupplier(productBackSupplierForm.getProductBackSupplier()));
            modelMap.addAttribute("program", programService().getOneProductProgramById(productBackSupplierForm.getProductProgramId()));
            modelMap.addAttribute("supplier", OperationUtils.getUserLocation().getParentLocation());
            modelMap.addAttribute("subTitle", "Retour de produit au fournisseur <i class=\"fa fa-play\"></i> Entête");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/site-back/editFlux.form", method = RequestMethod.GET)
    public String editSiteFlux(ModelMap modelMap,
                               @RequestParam(value = "backSupplierId") Integer backSupplierId,
                               @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                               @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                               BackSupplierAttributeFluxForm backSupplierAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductBackSupplier productBackSupplier = backSupplierService().getOneProductBackSupplierById(backSupplierId);
            if (fluxId != 0) {
                ProductAttributeFlux productAttributeFlux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
                if (productAttributeFlux != null) {
                    backSupplierAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productBackSupplier);
                } else {
                    backSupplierAttributeFluxForm = new BackSupplierAttributeFluxForm();
                    backSupplierAttributeFluxForm.setProductOperationId(backSupplierId);
                }
            } else {
                backSupplierAttributeFluxForm = new BackSupplierAttributeFluxForm();
                backSupplierAttributeFluxForm.setProductOperationId(productBackSupplier.getProductOperationId());
            }
            selectProduct(modelMap, selectedProductId, backSupplierAttributeFluxForm, productBackSupplier);
            //modelMappingForView(modelMap, backSupplierAttributeFluxForm, productBackSupplier);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/backSupplier/editFlux.form", method = RequestMethod.POST)
    public String saveSiteFlux(ModelMap modelMap,
                               HttpServletRequest request,
                               @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                               BackSupplierAttributeFluxForm backSupplierAttributeFluxForm,
                               BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductBackSupplierAttributeFluxFormValidation().validate(backSupplierAttributeFluxForm, result);
            ProductBackSupplier productBackSupplier = backSupplierService().getOneProductBackSupplierById(backSupplierAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = attributeService().saveProductAttribute(backSupplierAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux productAttributeFlux = backSupplierAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    productAttributeFlux.setStatus(productBackSupplier.getOperationStatus());
                    attributeFluxService().saveProductAttributeFlux(productAttributeFlux);

                    if (backSupplierAttributeFluxForm.getProductAttributeFluxId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit inséré avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/movement/site-back/editFlux.form?backSupplierId="
                            + backSupplierAttributeFluxForm.getProductOperationId();
                }
            }

            selectProduct(modelMap, selectedProductId, backSupplierAttributeFluxForm, productBackSupplier);
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/supplier-back/list.form", method = RequestMethod.GET)
    public void listBackToSupplier(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("supplierBacks", backSupplierService().getAllProductBackSuppliers(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("subTitle", "Liste des retours des sites");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/supplier-back/edit.form", method = RequestMethod.GET)
    public String editSupplier(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       ProductBackSupplierForm productBackSupplierForm) {
        if (Context.isAuthenticated()) {
            if (id != 0) {
                ProductBackSupplier productBackSupplier = backSupplierService().getOneProductBackSupplierById(id);
                if (productBackSupplier != null) {
                    if (!productBackSupplier.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        return "redirect:/module/pharmacy/operations/movement/supplier-back/editFlux.form?backSupplierId=" +
                                productBackSupplier.getProductOperationId();
                    }
                    productBackSupplierForm.setProductBackSupplier(productBackSupplier);
                    modelMap.addAttribute("program", productBackSupplier.getProductProgram());
                }
            } else {
                ProductProgram program = programService().getOneProductProgramById(programId);
                if (program == null) {
                    HttpSession session = request.getSession();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez sélectionner un programme !");
                    return "redirect:/module/pharmacy/operations/movement/backSupplier/list.form";
                }
                productBackSupplierForm = new ProductBackSupplierForm();
                productBackSupplierForm.setIncidence(Incidence.POSITIVE);
                productBackSupplierForm.setProductProgramId(program.getProductProgramId());
                productBackSupplierForm.setLocationId(OperationUtils.getUserLocation().getLocationId());
                modelMap.addAttribute("program", program);
            }

            modelMap.addAttribute("productBackSupplierForm", productBackSupplierForm);
            modelMap.addAttribute("productBackSupplier", backSupplierService().getOneProductBackSupplierById(id));
            modelMap.addAttribute("subLocations", OperationUtils.getUserLocation().getChildLocations());
            modelMap.addAttribute("subTitle", "Retour des produits des sites <i class=\"fa fa-play\"></i> Entête");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/supplier-back/edit.form", method = RequestMethod.POST)
    public String saveSupplier(ModelMap modelMap,
                                   HttpServletRequest request,
                                   @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                                   ProductBackSupplierForm productBackSupplierForm,
                                   BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductBackSupplierFormValidation().validate(productBackSupplierForm, result);

            if (!result.hasErrors()) {
                ProductBackSupplier backSupplier = backSupplierService().saveProductBackSupplier(productBackSupplierForm.getProductBackSupplier());

                if (action.equals("addLine")) {
                    if (backSupplier.getProductAttributeFluxes().size() == 0) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                    }
                    return "redirect:/module/pharmacy/operations/movement/supplier-back/editFlux.form?backSupplierId=" +
                            backSupplier.getProductOperationId();
                } else {
                    return "redirect:/module/pharmacy/operations/movement/supplier-back/list.form";
                }
            }
            modelMap.addAttribute("backSupplierHeaderForm", productBackSupplierForm);
//            modelMap.addAttribute("latestBackSupplier", latestBackSupplier(productBackSupplierForm.getProductBackSupplier()));
            modelMap.addAttribute("program", programService().getOneProductProgramById(productBackSupplierForm.getProductProgramId()));
            modelMap.addAttribute("subLocations", OperationUtils.getUserLocation().getChildLocations());
            modelMap.addAttribute("subTitle", "Retour des produits des sites <i class=\"fa fa-play\"></i> Entête");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/supplier-back/editFlux.form", method = RequestMethod.GET)
    public String editSupplierFlux(ModelMap modelMap,
                           @RequestParam(value = "backSupplierId") Integer backSupplierId,
                           @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                           BackSupplierAttributeFluxForm backSupplierAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductBackSupplier productBackSupplier = backSupplierService().getOneProductBackSupplierById(backSupplierId);
            if (fluxId != 0) {
                ProductAttributeFlux productAttributeFlux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
                if (productAttributeFlux != null) {
                    backSupplierAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productBackSupplier);
                } else {
                    backSupplierAttributeFluxForm = new BackSupplierAttributeFluxForm();
                    backSupplierAttributeFluxForm.setProductOperationId(backSupplierId);
                }
            } else {
                backSupplierAttributeFluxForm = new BackSupplierAttributeFluxForm();
                backSupplierAttributeFluxForm.setProductOperationId(productBackSupplier.getProductOperationId());
            }
            modelMappingForView(modelMap, backSupplierAttributeFluxForm, productBackSupplier);
        }
        return null;
    }

    private void selectProduct(ModelMap modelMap, @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId, BackSupplierAttributeFluxForm backSupplierAttributeFluxForm, ProductBackSupplier productBackSupplier) {
        if (selectedProductId != 0) {
            ProductAttributeStock stock =  stockService().getOneProductAttributeStockById(selectedProductId);
            modelMap.addAttribute("stock", stock);
            backSupplierAttributeFluxForm.setSelectedProductStockId(selectedProductId);
            backSupplierAttributeFluxForm.setProductId(stock.getProductAttribute().getProduct().getProductId());
            backSupplierAttributeFluxForm.setBatchNumber(stock.getProductAttribute().getBatchNumber());
            backSupplierAttributeFluxForm.setExpiryDate(stock.getProductAttribute().getExpiryDate());
            if (stock.getQuantityInStock() == 0) {
                modelMap.addAttribute("productMessage", "Ce produit est en rupture de stock");
            }
        }

        modelMappingForView(modelMap, backSupplierAttributeFluxForm, productBackSupplier);
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/supplier-back/editFlux.form", method = RequestMethod.POST)
    public String saveSupplierFlux(ModelMap modelMap,
                           HttpServletRequest request,
                           @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                           BackSupplierAttributeFluxForm backSupplierAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductBackSupplierAttributeFluxFormValidation().validate(backSupplierAttributeFluxForm, result);
            ProductBackSupplier productBackSupplier = backSupplierService().getOneProductBackSupplierById(backSupplierAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = attributeService().saveProductAttribute(backSupplierAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux productAttributeFlux = backSupplierAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    productAttributeFlux.setStatus(productBackSupplier.getOperationStatus());
                    attributeFluxService().saveProductAttributeFlux(productAttributeFlux);

                    if (backSupplierAttributeFluxForm.getProductOperationId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit inséré avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/movement/supplier-back/editFlux.form?backSupplierId="
                            + backSupplierAttributeFluxForm.getProductOperationId();
                }
            }

            selectProduct(modelMap, selectedProductId, backSupplierAttributeFluxForm, productBackSupplier);
        }

        return null;
    }

    private void modelMappingForView(ModelMap modelMap, BackSupplierAttributeFluxForm backSupplierAttributeFluxForm, ProductBackSupplier productBackSupplier) {
        modelMap.addAttribute("backSupplierAttributeFluxForm", backSupplierAttributeFluxForm);
        modelMap.addAttribute("productBackSupplier", productBackSupplier);
        if (productBackSupplier.getIncidence().equals(Incidence.POSITIVE)) {
            modelMap.addAttribute("products", programService().getOneProductProgramById(productBackSupplier.getProductProgram().getProductProgramId()).getProducts());
        } else {
            modelMap.addAttribute("stocks", stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation(), false));
        }
        String backSupplierTypeStr = productBackSupplier.getIncidence().equals(Incidence.POSITIVE) ? "Retour de produits du site" : "Retour de produits au fournisseur";
        if (!productBackSupplier.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
            if (productBackSupplier.getOperationStatus().equals(OperationStatus.VALIDATED))
                modelMap.addAttribute("subTitle", backSupplierTypeStr.toUpperCase() + " <i class=\"fa fa-play\"></i> APPROUVEE");
            else if (productBackSupplier.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
                modelMap.addAttribute("subTitle", backSupplierTypeStr.toUpperCase() +
                        "  <i class=\"fa fa-play\"></i> EN ATTENTE DE VALIDATION");
            }
            List<ProductAttributeFlux> productAttributeFluxes = attributeFluxService().getAllProductAttributeFluxByOperation(productBackSupplier, false);
            if (productAttributeFluxes.size() != 0) {
                Collections.sort(productAttributeFluxes, Collections.<ProductAttributeFlux>reverseOrder());
            }
            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
        } else {
            if (productBackSupplier.getIncidence().equals(Incidence.POSITIVE)) {
                List<ProductAttributeFlux> productAttributeFluxes = attributeFluxService().getAllProductAttributeFluxByOperation(productBackSupplier, false);
                if (productAttributeFluxes.size() != 0) {
                    Collections.sort(productAttributeFluxes, Collections.<ProductAttributeFlux>reverseOrder());
                }
                modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
            } else {
                List<ProductOutFluxDTO> productAttributeFluxes = service().getProductOutFluxDTOs(productBackSupplier);
                modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
            }
            modelMap.addAttribute("subTitle", backSupplierTypeStr + " <i class=\"fa fa-play\"></i> ajout de produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/back/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "backSupplierId") Integer backSupplierId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductBackSupplier backSupplier = backSupplierService().getOneProductBackSupplierById(backSupplierId);
        backSupplier.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
        backSupplierService().saveProductBackSupplier(backSupplier);
        attributeService().purgeUnusedAttributes();
        String message;
        String link;
        if (backSupplier.getIncidence().equals(Incidence.POSITIVE)) {
            message = "Le retour de produits du site ";
            link = "supplier-back";
        } else {
            message = "Le retour de produits au fournisseur ";
            link = "site-back";
        }
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message +
                " a été enregistré avec succès et est en attente de validation !");
        return "redirect:/module/pharmacy/operations/movement/"+ link + "/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/back/incomplete.form", method = RequestMethod.GET)
    public String incomplete(HttpServletRequest request,
                             @RequestParam(value = "backSupplierId") Integer backSupplierId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductBackSupplier backSupplier = backSupplierService().getOneProductBackSupplierById(backSupplierId);
        backSupplier.setOperationStatus(OperationStatus.NOT_COMPLETED);
        backSupplierService().saveProductBackSupplier(backSupplier);
        String message;
        String link;
        if (backSupplier.getIncidence().equals(Incidence.POSITIVE)) {
            message = "Le retour de produits du site ";
            link = "supplier-back";
        } else {
            message = "Le retour de produits au fournisseur ";
            link = "site-back";
        }
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à modifier " + message + " !");
        return "redirect:/module/pharmacy/operations/movement/"+ link + "/editFlux.form?backSupplierId=" + backSupplierId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/back/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "backSupplierId") Integer backSupplierId,
                             @RequestParam(value = "fluxId") Integer fluxId){
        if (!Context.isAuthenticated())
            return null;
        ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
        String link = "";
        if (flux != null) {
            HttpSession session = request.getSession();
            attributeFluxService().removeProductAttributeFlux(flux);

            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");
            if (flux.getProductOperation().getIncidence().equals(Incidence.POSITIVE)) {
                link = "supplier-back";
            } else {
                link = "site-back";
            }
        }
        return "redirect:/module/pharmacy/operations/movement/" + link + "/editFlux.form?backSupplierId=" + backSupplierId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/back/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "backSupplierId") Integer backSupplierId){
        if (!Context.isAuthenticated())
            return null;
        ProductOperation operation = service().getOneProductOperationById(backSupplierId);

        if (operation != null) {
            if (OperationUtils.validateOperation(operation)) {
                HttpSession session = request.getSession();
                String message;
                String link;
                if (operation.getIncidence().equals(Incidence.POSITIVE)) {
                    message = "Le retour de produits du site ";
                    link = "supplier-back";
                } else {
                    message = "Le retour de produits au fournisseur ";
                    link = "site-back";
                }
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message + "a été validé avec succès");
                return "redirect:/module/pharmacy/operations/movement/"+ link + "/list.form";
            }
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/back/delete.form", method = RequestMethod.GET)
    public String delete(HttpServletRequest request,
                         @RequestParam(value = "id") Integer id) {
        if (id != null) {
            ProductBackSupplier backSupplier = backSupplierService().getOneProductBackSupplierById(id);
            if (backSupplier != null) {
                Set<ProductAttributeFlux> fluxes = backSupplier.getProductAttributeFluxes();
                for (ProductAttributeFlux flux: fluxes) {
                    attributeFluxService().removeProductAttributeFlux(flux);
                }
                backSupplierService().removeProductBackSupplier(backSupplier);

                attributeService().purgeUnusedAttributes();
                HttpSession session = request.getSession();
                String message;
                String link;
                if (backSupplier.getIncidence().equals(Incidence.POSITIVE)) {
                    message = "Le retour de produits du site ";
                    link = "supplier-back";
                } else {
                    message = "Le retour de produits au fournisseur ";
                    link = "site-back";
                }
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message + "a été supprimé avec succès");
                return "redirect:/module/pharmacy/operations/movement/"+ link + "/list.form";
            }
        }
        return null;
    }

}
