package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductAttribute;
import org.openmrs.module.pharmacy.ProductAttributeFlux;
import org.openmrs.module.pharmacy.ProductReception;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.forms.ReceptionAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ReceptionHeaderForm;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;
import org.openmrs.module.pharmacy.validators.ProductAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.validators.ProductReceptionHeaderFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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

    public Location getUserLocation() {
        return Context.getLocationService().getDefaultLocation();
//        return Context.getUserContext().getLocation();
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("receptions", service().getAllProductReceptions(getUserLocation(), false));
            modelMap.addAttribute("title", "Liste des Réceptions de produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                     @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                     ReceptionHeaderForm receptionHeaderForm) {
        if (Context.isAuthenticated()) {
            if (id != 0) {
                ProductReception productReception = service().getOneProductReceptionById(id);
                if (productReception != null) {
                    receptionHeaderForm.setProductReception(productReception);
                }
            } else {
                receptionHeaderForm = new ReceptionHeaderForm();
                receptionHeaderForm.setLocationId(getUserLocation().getLocationId());
            }

            modelMap.addAttribute("receptionHeaderForm", receptionHeaderForm);
            modelMap.addAttribute("programs", service().getAllProductProgram());
            modelMap.addAttribute("suppliers", service().getAllProductSuppliers());
            modelMap.addAttribute("title", "Saisie de réception de Produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       ReceptionHeaderForm receptionHeaderForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductReceptionHeaderFormValidation().validate(receptionHeaderForm, result);

            if (!result.hasErrors()) {
//                boolean idExist = (receptionHeaderForm.getProductOperationId() != null);
                ProductReception reception = service().saveProductReception(receptionHeaderForm.getProductReception());

                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                return "redirect:/module/pharmacy/operations/reception/editFlux.form?receptionId=" + reception.getProductOperationId();
            }
            modelMap.addAttribute("receptionHeaderForm", receptionHeaderForm);
//            modelMap.addAttribute("product", receptionHeaderForm.getProduct());
            modelMap.addAttribute("programs", service().getAllProductProgram());
            modelMap.addAttribute("suppliers", service().getAllProductSuppliers());
            modelMap.addAttribute("title", "Saisie  de réception des Produits");
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                           HttpServletRequest request,
                         @RequestParam(value = "receptionId") Integer receptionId,
                         @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                         ReceptionAttributeFluxForm receptionAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();
            ProductReception productReception = service().getOneProductReceptionById(receptionId);
            if (productReception != null) {
                if (fluxId != 0) {
                    ProductAttributeFlux productAttributeFlux = service().getOneProductAttributeFluxById(receptionId);
                    if (productAttributeFlux != null) {
                        receptionAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productReception);
                    } else {
                        receptionAttributeFluxForm = new ReceptionAttributeFluxForm();
                        receptionAttributeFluxForm.setProductOperationId(productReception.getProductOperationId());
                    }
                } else {
                    receptionAttributeFluxForm = new ReceptionAttributeFluxForm();
                    receptionAttributeFluxForm.setProductOperationId(productReception.getProductOperationId());
                }
                modelMappingForView(modelMap, receptionAttributeFluxForm, productReception);
            } else {
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous n'avez pas sélectioné une réception existante");
                return "redirect:/module/pharmacy/operations/reception/list.form";
            }
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

            new ProductAttributeFluxFormValidation().validate(receptionAttributeFluxForm, result);
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
//                new ArrayList<ProductReceptionItemModel>();
//        if (productAttributeFluxes.size() != 0) {
//            Collections.sort(productAttributeFluxes, Collections.<ProductReceptionItemModel>reverseOrder());
//        }
        //System.out.println(productAttributeFluxes);
        modelMap.addAttribute("receptionAttributeFluxForm", receptionAttributeFluxForm);
        modelMap.addAttribute("productReception", productReception);
        modelMap.addAttribute("products", service().getOneProductProgramById(productReception.getProductProgram().getProductProgramId()).getProducts());
        modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
        modelMap.addAttribute("title", "Saisie de réception de Produits (Ajout des produits)");
    }
}
