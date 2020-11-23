package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.forms.ProductMovementEntryForm;
import org.openmrs.module.pharmacy.forms.ReceptionAttributeFluxForm;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;
import org.openmrs.module.pharmacy.validators.ProductMovementEntryHeaderFormValidation;
import org.openmrs.module.pharmacy.validators.ProductMovementOutHeaderFormValidation;
import org.openmrs.module.pharmacy.validators.ProductReceptionAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.validators.ProductReceptionHeaderFormValidation;
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
import java.util.List;

@Controller
public class PharmacyProductMovementManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private ProductMovementService service() {
        return Context.getService(ProductMovementService.class);
    }
    private ProductProgramService programService(){
        return Context.getService(ProductProgramService.class);
    }
    private ProductExchangeEntityService ExchangeService() {
        return Context.getService(ProductExchangeEntityService.class);
    }
    private ProductService ProductService() {
        return Context.getService(ProductService.class);
    }
    private ProductReceptionService receptionService() {
        return Context.getService(ProductReceptionService.class);
    }
    private PharmacyService pharmacyService() {
        return Context.getService(PharmacyService.class);
    }
    private ProductAttributeFluxService productAttributeFluxService(){
        return Context.getService(ProductAttributeFluxService.class);
    }
    private ProductAttributeService productAttributeService(){
        return Context.getService(ProductAttributeService.class);
    }

    @ModelAttribute("title")
    public String getTile() {
        return "Pertes et Ajustements";
    }

    public Location getUserLocation() {
        return Context.getLocationService().getDefaultLocation();
//        return Context.getUserContext().getLocation();
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("entries", service().getAllProductMovementEntry(getUserLocation(), false));
            modelMap.addAttribute("outs", service().getAllProductMovementOut(getUserLocation(), false));
            modelMap.addAttribute("subTitle", "Liste des Mouvements");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                     @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       ProductMovementEntryForm productMovementEntryForm) {
        if (Context.isAuthenticated()) {
            if (id != 0) {
                ProductMovementEntry productMovementEntry = service().getOneProductMovementEntryById(id);
                if (productMovementEntry != null) {
                    if (!productMovementEntry.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        return "redirect:/module/pharmacy/operations/movement/editFlux.form?receptionId=" +
                            productMovementEntry.getProductOperationId();
                    }
                    productMovementEntryForm.setProductMovementEntry(productMovementEntry);
                }
            } else {
                productMovementEntryForm = new ProductMovementEntryForm();
                productMovementEntryForm.setLocationId(getUserLocation().getLocationId());
            }

            modelMap.addAttribute("productMovementEntryForm", productMovementEntryForm);
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("exchanges", ExchangeService().getAllProductExchange());
            modelMap.addAttribute("subTitle", "Saisie de movement");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                       ProductMovementEntryForm productMovementEntryForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductMovementEntryHeaderFormValidation().validate(productMovementEntryForm, result);

            if (!result.hasErrors()) {
//                boolean idExist = (receptionHeaderForm.getProductOperationId() != null);
                ProductMovementEntry entry = service().saveProductMovementEntry(productMovementEntryForm.getProductMovementEntry());

                if (action.equals("addLine")) {
                    if (entry.getProductAttributeFluxes().size() == 0) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                    }
                    return "redirect:/module/pharmacy/operations/movement/editFlux.form?receptionId=" +
                            entry.getProductOperationId();
                } else {
                    return "redirect:/module/pharmacy/operations/movement/list.form";
                }
            }
            modelMap.addAttribute("productMovementEntryForm", productMovementEntryForm);
//            modelMap.addAttribute("product", receptionHeaderForm.getProduct());
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("exchanges", ExchangeService().getAllProductExchange());
            modelMap.addAttribute("subTitle", "Saisie  de Mouvements");
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                         @RequestParam(value = "receptionId") Integer receptionId,
                         @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                         ReceptionAttributeFluxForm receptionAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductReception productReception = receptionService().getOneProductReceptionById(receptionId);
            if (fluxId != 0) {
                ProductAttributeFlux productAttributeFlux = productAttributeFluxService().getOneProductAttributeFluxById(fluxId);
                if (productAttributeFlux != null) {
                    receptionAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productReception);
                } else {
                    receptionAttributeFluxForm = new ReceptionAttributeFluxForm();
                    receptionAttributeFluxForm.setProductOperationId(receptionId);
                }
            } else {
                receptionAttributeFluxForm = new ReceptionAttributeFluxForm();
                receptionAttributeFluxForm.setProductOperationId(productReception.getProductOperationId());
            }
            modelMappingForView(modelMap, receptionAttributeFluxForm, productReception);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/editFlux.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           HttpServletRequest request,
                           ReceptionAttributeFluxForm receptionAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductReceptionAttributeFluxFormValidation().validate(receptionAttributeFluxForm, result);
            ProductReception productReception = receptionService().getOneProductReceptionById(receptionAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = productAttributeService().saveProductAttribute(receptionAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux productAttributeFlux = receptionAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    productAttributeFlux.setStatus(productReception.getOperationStatus());

                    if (productAttributeFluxService().saveProductAttributeFlux(productAttributeFlux) != null) {
                        productAttributeFluxService().saveProductAttributeOtherFlux(receptionAttributeFluxForm.getProductAttributeOtherFlux());
                    }

                    if (receptionAttributeFluxForm.getProductOperationId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/movement/editFlux.form?receptionId="
                            + receptionAttributeFluxForm.getProductOperationId();
                }
            }

            modelMappingForView(modelMap, receptionAttributeFluxForm, productReception);
        }

        return null;
    }

    private void modelMappingForView(ModelMap modelMap, ReceptionAttributeFluxForm receptionAttributeFluxForm, ProductReception productReception) {
        List<ProductReceptionFluxDTO> productAttributeFluxes = receptionService().getProductReceptionFluxDTOs(productReception);
//        if (productAttributeFluxes.size() != 0) {
//            Collections.sort(productAttributeFluxes, Collections.<ProductReceptionFluxDTO>reverseOrder());
//        }
        modelMap.addAttribute("receptionAttributeFluxForm", receptionAttributeFluxForm);
        modelMap.addAttribute("productReception", productReception);
        modelMap.addAttribute("products", programService().getOneProductProgramById(productReception.getProductProgram().getProductProgramId()).getProducts());
        modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
        modelMap.addAttribute("subTitle", "Saisie de réception - ajout de produits");
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "receptionId") Integer receptionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReception reception = receptionService().getOneProductReceptionById(receptionId);
        reception.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
        receptionService().saveProductReception(reception);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La réception a été enregistré avec " +
                "succès et est en attente de validation !");
        return "redirect:/module/pharmacy/operations/reception/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/incomplete.form", method = RequestMethod.GET)
    public String incomplete(HttpServletRequest request,
                             @RequestParam(value = "receptionId") Integer receptionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReception reception = receptionService().getOneProductReceptionById(receptionId);
        reception.setOperationStatus(OperationStatus.NOT_COMPLETED);
        receptionService().saveProductReception(reception);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                "continuer à modifier la réception !");
        return "redirect:/module/pharmacy/operations/movement/editFlux.form?receptionId=" + receptionId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/delete.form", method = RequestMethod.GET)
    public String deleteOperation(HttpServletRequest request,
                                  @RequestParam(value = "receptionId") Integer receptionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReception reception = receptionService().getOneProductReceptionById(receptionId);
        for (ProductAttributeOtherFlux otherFlux : productAttributeFluxService().getAllProductAttributeOtherFluxByOperation(reception, false)) {
            productAttributeFluxService().removeProductAttributeOtherFlux(otherFlux);
        }
        for (ProductAttributeFlux flux : productAttributeFluxService().getAllProductAttributeFluxByOperation(reception, false)){
            productAttributeFluxService().removeProductAttributeFlux(flux);
        }
        receptionService().removeProductReception(reception);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La réception a été supprimée avec succès !");
        return "redirect:/module/pharmacy/operations/reception/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "receptionId") Integer receptionId,
                             @RequestParam(value = "fluxId") Integer fluxId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductAttributeFlux flux = productAttributeFluxService().getOneProductAttributeFluxById(fluxId);
        if (flux != null) {
            productAttributeFluxService().removeProductAttributeFlux(flux);
            ProductAttributeOtherFlux otherFlux = productAttributeFluxService()
                    .getOneProductAttributeOtherFluxByAttributeAndOperation(flux.getProductAttribute(), flux.getProductOperation());
            if (otherFlux != null) {
                productAttributeFluxService().removeProductAttributeOtherFlux(otherFlux);
            }
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");
        }
        return "redirect:/module/pharmacy/operations/movement/editFlux.form?receptionId=" + receptionId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "receptionId") Integer receptionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReception reception = receptionService().getOneProductReceptionById(receptionId);
        if (pharmacyService().validateOperation(reception)) {
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                    "continuer à modifier la réception !");
            return "redirect:/module/pharmacy/operations/movement/list.form";
        }
        return null;
    }

}
