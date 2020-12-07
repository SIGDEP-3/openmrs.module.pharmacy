package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.forms.ProductReceptionForm;
import org.openmrs.module.pharmacy.forms.ReceptionAttributeFluxForm;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.validators.ProductReceptionAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.validators.ProductReceptionFormValidation;
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
import java.util.*;

@Controller
public class PharmacyProductReceptionManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private ProductReceptionService receptionService() {
        return Context.getService(ProductReceptionService.class);
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
        return "Réception de produits";
    }



    @RequestMapping(value = "/module/pharmacy/operations/reception/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("receptions", receptionService().getAllProductReceptions(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("subTitle", "Liste des Réceptions");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       ProductReceptionForm productReceptionForm) {
        if (Context.isAuthenticated()) {
            if (id != 0) {
                ProductReception productReception = receptionService().getOneProductReceptionById(id);
                if (productReception != null) {
                    if (!productReception.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        return "redirect:/module/pharmacy/operations/reception/editFlux.form?receptionId=" +
                            productReception.getProductOperationId();
                    }
                    productReceptionForm.setProductReception(productReception);
                    modelMap.addAttribute("program", productReception.getProductProgram());
                }
            } else {
                ProductProgram program = programService().getOneProductProgramById(programId);
                if (program == null) {
                    HttpSession session = request.getSession();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez sélectionner un programme !");
                    return "redirect:/module/pharmacy/operations/reception/list.form";
                }
                productReceptionForm = new ProductReceptionForm(programId);
                productReceptionForm.setProductProgramId(program.getProductProgramId());
                productReceptionForm.setLocationId(OperationUtils.getUserLocation().getLocationId());

                modelMap.addAttribute("program", program);
            }

            modelMap.addAttribute("productReceptionForm", productReceptionForm);
            modelMap.addAttribute("productReception", receptionService().getOneProductReceptionById(id));
            modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
            modelMap.addAttribute("subTitle", "Saisie de réception - entête");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                       ProductReceptionForm productReceptionForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductReceptionFormValidation().validate(productReceptionForm, result);

            if (!result.hasErrors()) {
//                boolean idExist = (receptionHeaderForm.getProductOperationId() != null);
                ProductReception reception = receptionService().saveProductReception(productReceptionForm.getProductReception());

                if (action.equals("addLine")) {
                    if (reception.getProductAttributeFluxes().size() == 0) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                    }
                    return "redirect:/module/pharmacy/operations/reception/editFlux.form?receptionId=" +
                            reception.getProductOperationId();
                } else {
                    return "redirect:/module/pharmacy/operations/reception/list.form";
                }
            }
            modelMap.addAttribute("receptionHeaderForm", productReceptionForm);
            modelMap.addAttribute("program", programService().getOneProductProgramById(productReceptionForm.getProductProgramId()));
            modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
            modelMap.addAttribute("subTitle", "Saisie  de réception - entête");
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                         @RequestParam(value = "receptionId") Integer receptionId,
                         @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                         ReceptionAttributeFluxForm receptionAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductReception productReception = receptionService().getOneProductReceptionById(receptionId);
            if (fluxId != 0) {
                ProductAttributeFlux productAttributeFlux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
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

    @RequestMapping(value = "/module/pharmacy/operations/reception/editFlux.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           HttpServletRequest request,
                           ReceptionAttributeFluxForm receptionAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductReceptionAttributeFluxFormValidation().validate(receptionAttributeFluxForm, result);
            ProductReception productReception = receptionService().getOneProductReceptionById(receptionAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = attributeService().saveProductAttribute(receptionAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux productAttributeFlux = receptionAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    productAttributeFlux.setStatus(productReception.getOperationStatus());

                    if (attributeFluxService().saveProductAttributeFlux(productAttributeFlux) != null) {
                        attributeFluxService().saveProductAttributeOtherFlux(receptionAttributeFluxForm.getProductAttributeOtherFlux());
                    }

                    if (receptionAttributeFluxForm.getProductOperationId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/reception/editFlux.form?receptionId="
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
        if (!productReception.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
            if (productReception.getOperationStatus().equals(OperationStatus.VALIDATED))
            modelMap.addAttribute("subTitle", "Réception - APPROUVEE");
            else if (productReception.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
                modelMap.addAttribute("subTitle", "Réception - EN ATTENTE DE VALIDATION");
            }
        } else {
            modelMap.addAttribute("subTitle", "Saisie de réception - ajout de produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "receptionId") Integer receptionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReception reception = receptionService().getOneProductReceptionById(receptionId);
        reception.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
        receptionService().saveProductReception(reception);
        attributeService().purgeUnusedAttributes();
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La réception a été enregistré avec " +
                "succès et est en attente de validation !");
        return "redirect:/module/pharmacy/operations/reception/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/incomplete.form", method = RequestMethod.GET)
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
        return "redirect:/module/pharmacy/operations/reception/editFlux.form?receptionId=" + receptionId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/delete.form", method = RequestMethod.GET)
    public String deleteOperation(HttpServletRequest request,
                                  @RequestParam(value = "id") Integer id){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReception reception = receptionService().getOneProductReceptionById(id);
        for (ProductAttributeOtherFlux otherFlux : attributeFluxService().getAllProductAttributeOtherFluxByOperation(reception, false)) {
            attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
        }
        for (ProductAttributeFlux flux : attributeFluxService().getAllProductAttributeFluxByOperation(reception, false)){
            attributeFluxService().removeProductAttributeFlux(flux);
        }
        receptionService().removeProductReception(reception);
        attributeService().purgeUnusedAttributes();
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La réception a été supprimée avec succès !");
        return "redirect:/module/pharmacy/operations/reception/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "receptionId") Integer receptionId,
                             @RequestParam(value = "fluxId") Integer fluxId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
        if (flux != null) {
            attributeFluxService().removeProductAttributeFlux(flux);
            ProductAttributeOtherFlux otherFlux = attributeFluxService()
                    .getOneProductAttributeOtherFluxByAttributeAndOperation(flux.getProductAttribute(), flux.getProductOperation(), OperationUtils.getUserLocation());
            if (otherFlux != null) {
                attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
            }
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");
        }
        return "redirect:/module/pharmacy/operations/reception/editFlux.form?receptionId=" + receptionId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "receptionId") Integer receptionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReception reception = receptionService().getOneProductReceptionById(receptionId);
        if (OperationUtils.validateOperation(reception)) {
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                    "continuer à modifier la réception !");
            return "redirect:/module/pharmacy/operations/reception/list.form";
        }
        return null;
    }
}
