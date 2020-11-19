package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductAttribute;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductAttributeOtherFlux;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.forms.ProductReceptionForm;
import org.openmrs.module.pharmacy.forms.ReceptionAttributeFluxForm;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;
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
import java.util.*;

@Controller
public class PharmacyProductReceptionManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    @ModelAttribute("title")
    public String getTile() {
        return "Réception de produits";
    }

    public Location getUserLocation() {
        return Context.getLocationService().getDefaultLocation();
//        return Context.getUserContext().getLocation();
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("receptions", service().getAllProductReceptions(getUserLocation(), false));
            modelMap.addAttribute("subTitle", "Liste des Réceptions");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                     @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                     ProductReceptionForm productReceptionForm) {
        if (Context.isAuthenticated()) {
            if (id != 0) {
                ProductReception productReception = service().getOneProductReceptionById(id);
                if (productReception != null) {
                    if (!productReception.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        return "redirect:/module/pharmacy/operations/reception/editFlux.form?receptionId=" +
                            productReception.getProductOperationId();
                    }
                    productReceptionForm.setProductReception(productReception);
                }
            } else {
                productReceptionForm = new ProductReceptionForm();
                productReceptionForm.setLocationId(getUserLocation().getLocationId());
            }

            modelMap.addAttribute("receptionHeaderForm", productReceptionForm);
            modelMap.addAttribute("programs", service().getAllProductProgram());
            modelMap.addAttribute("suppliers", service().getAllProductSuppliers());
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

            new ProductReceptionHeaderFormValidation().validate(productReceptionForm, result);

            if (!result.hasErrors()) {
//                boolean idExist = (receptionHeaderForm.getProductOperationId() != null);
                ProductReception reception = service().saveProductReception(productReceptionForm.getProductReception());

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
//            modelMap.addAttribute("product", receptionHeaderForm.getProduct());
            modelMap.addAttribute("programs", service().getAllProductProgram());
            modelMap.addAttribute("suppliers", service().getAllProductSuppliers());
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
            ProductReception productReception = service().getOneProductReceptionById(receptionId);
            if (fluxId != 0) {
                ProductAttributeFlux productAttributeFlux = service().getOneProductAttributeFluxById(fluxId);
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
            ProductReception productReception = service().getOneProductReceptionById(receptionAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = service().saveProductAttribute(receptionAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux productAttributeFlux = receptionAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    productAttributeFlux.setStatus(productReception.getOperationStatus());

                    if (service().saveProductAttributeFlux(productAttributeFlux) != null) {
                        service().saveProductAttributeOtherFlux(receptionAttributeFluxForm.getProductAttributeOtherFlux());
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
        List<ProductReceptionFluxDTO> productAttributeFluxes = service().getProductReceptionFluxDTOs(productReception);
//        if (productAttributeFluxes.size() != 0) {
//            Collections.sort(productAttributeFluxes, Collections.<ProductReceptionFluxDTO>reverseOrder());
//        }
        modelMap.addAttribute("receptionAttributeFluxForm", receptionAttributeFluxForm);
        modelMap.addAttribute("productReception", productReception);
        modelMap.addAttribute("products", service().getOneProductProgramById(productReception.getProductProgram().getProductProgramId()).getProducts());
        modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
        modelMap.addAttribute("subTitle", "Saisie de réception - ajout de produits");
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "receptionId") Integer receptionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReception reception = service().getOneProductReceptionById(receptionId);
        reception.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
        service().saveProductReception(reception);
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
        ProductReception reception = service().getOneProductReceptionById(receptionId);
        reception.setOperationStatus(OperationStatus.NOT_COMPLETED);
        service().saveProductReception(reception);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                "continuer à modifier la réception !");
        return "redirect:/module/pharmacy/operations/reception/editFlux.form?receptionId=" + receptionId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/delete.form", method = RequestMethod.GET)
    public String deleteOperation(HttpServletRequest request,
                                  @RequestParam(value = "receptionId") Integer receptionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReception reception = service().getOneProductReceptionById(receptionId);
        for (ProductAttributeOtherFlux otherFlux : service().getAllProductAttributeOtherFluxByOperation(reception, false)) {
            service().removeProductAttributeOtherFlux(otherFlux);
        }
        for (ProductAttributeFlux flux : service().getAllProductAttributeFluxByOperation(reception, false)){
            service().removeProductAttributeFlux(flux);
        }
        service().removeProductReception(reception);
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
        ProductAttributeFlux flux = service().getOneProductAttributeFluxById(fluxId);
        if (flux != null) {
            service().removeProductAttributeFlux(flux);
            ProductAttributeOtherFlux otherFlux = service()
                    .getOneProductAttributeOtherFluxByAttributeAndOperation(flux.getProductAttribute(), flux.getProductOperation());
            if (otherFlux != null) {
                service().removeProductAttributeOtherFlux(otherFlux);
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
        ProductReception reception = service().getOneProductReceptionById(receptionId);
        if (service().validateOperation(reception)) {
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                    "continuer à modifier la réception !");
            return "redirect:/module/pharmacy/operations/reception/list.form";
        }
        return null;
    }

}
