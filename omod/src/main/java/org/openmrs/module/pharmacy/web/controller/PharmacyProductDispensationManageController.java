package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.forms.FindPatientForm;
import org.openmrs.module.pharmacy.forms.ProductDispensationForm;
import org.openmrs.module.pharmacy.forms.DispensationAttributeFluxForm;
import org.openmrs.module.pharmacy.models.ProductDispensationFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.validators.ProductDispensationAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.validators.ProductDispensationFormValidation;
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
import java.util.Date;
import java.util.List;

@Controller
public class PharmacyProductDispensationManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private ProductDispensationService dispensationService() {
        return Context.getService(ProductDispensationService.class);
    }

    private ProductRegimenService regimenService() {
        return Context.getService(ProductRegimenService.class);
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
        return "Dispensation / Vente";
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap,
                     @RequestParam(value = "startDate", defaultValue = "", required = false) Date startDate,
                     @RequestParam(value = "endDate", defaultValue = "", required = false) Date endDate,
                     FindPatientForm findPatientForm) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("dispensations", dispensationService().getAllProductDispensations(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("findPatientForm", findPatientForm);
            modelMap.addAttribute("subTitle", "Liste des Dispensations du jour");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/list.form", method = RequestMethod.POST)
    public String findPatient(HttpServletRequest request,
                              FindPatientForm findPatientForm) {
        if (Context.isAuthenticated()) {
            String mobile = "0";
            String regimen = "0";
            String patientId = "0";
            if (findPatientForm.getPatientIdentifier() != null) {
                Patient patient = dispensationService().getPatientByIdentifier(findPatientForm.getPatientIdentifier());
                if (patient != null) {
                    regimen = "1";
                    patientId = patient.getPatientId().toString();
                } else {
                    MobilePatient mobilePatient = dispensationService().getOneMobilePatientByIdentifier(findPatientForm.getPatientIdentifier());
                    if (mobilePatient != null) {
                        patientId = mobilePatient.getMobilePatientId().toString();
                    } else {
                        mobilePatient = new MobilePatient();
                        mobilePatient.setLocation(OperationUtils.getUserLocation());
                        mobilePatient.setIdentifier(findPatientForm.getPatientIdentifier());
                        patientId = dispensationService().saveMobilePatient(mobilePatient).getMobilePatientId().toString();
                    }
                }
            }

            return "redirect:/module/pharmacy/operations/dispensation/edit.form?M=" + mobile + "&R=" + regimen +
                    "&patientId=" + patientId + "&programId=" + findPatientForm.getProductProgramId();

        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       @RequestParam(value = "R", defaultValue = "0", required = false) Integer regimen,
                       @RequestParam(value = "M", defaultValue = "0", required = false) Integer mobile,
                       ProductDispensationForm productDispensationForm) {
        if (Context.isAuthenticated()) {
            if (id != 0) {
                ProductDispensation productDispensation = dispensationService().getOneProductDispensationById(id);

                if (productDispensation != null) {
                    if (!productDispensation.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        return "redirect:/module/pharmacy/operations/dispensation/editFlux.form?dispensationId=" +
                            productDispensation.getProductOperationId();
                    }
                    productDispensationForm.setProductDispensation(productDispensation);
                    if (productDispensation.getEncounter() == null) {
                        productDispensationForm.setMobileDispensationInfo(dispensationService().getOneMobilePatientDispensationInfoByDispensation(productDispensation));
                    }
                    modelMap.addAttribute("program", productDispensation.getProductProgram());
                }
            } else {
                ProductProgram program = programService().getOneProductProgramById(programId);
                if (program == null) {
                    HttpSession session = request.getSession();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez sélectionner un programme !");
                    return "redirect:/module/pharmacy/operations/dispensation/list.form";
                }
                productDispensationForm = new ProductDispensationForm();
                productDispensationForm.setLocationId(OperationUtils.getUserLocation().getLocationId());

                modelMap.addAttribute("program", program);
            }

            modelMap.addAttribute("dispensationHeaderForm", productDispensationForm);
//            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("productDispensation", dispensationService().getOneProductDispensationById(id));
            modelMap.addAttribute("regimen", supplierService().getAllProductSuppliers());
            modelMap.addAttribute("subTitle", "Saisie de réception - entête");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                       ProductDispensationForm productDispensationForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductDispensationFormValidation().validate(productDispensationForm, result);

            if (!result.hasErrors()) {
//                boolean idExist = (dispensationHeaderForm.getProductOperationId() != null);
                ProductDispensation dispensation = dispensationService().saveProductDispensation(productDispensationForm.getProductDispensation());

                if (action.equals("addLine")) {
                    if (dispensation.getProductAttributeFluxes().size() == 0) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                    }
                    return "redirect:/module/pharmacy/operations/dispensation/editFlux.form?dispensationId=" +
                            dispensation.getProductOperationId();
                } else {
                    return "redirect:/module/pharmacy/operations/dispensation/list.form";
                }
            }
            modelMap.addAttribute("dispensationHeaderForm", productDispensationForm);
//            modelMap.addAttribute("product", dispensationHeaderForm.getProduct());
            modelMap.addAttribute("program", programService().getOneProductProgramById(productDispensationForm.getProductProgramId()));
            modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
            modelMap.addAttribute("subTitle", "Saisie  de réception - entête");
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                         @RequestParam(value = "dispensationId") Integer dispensationId,
                         @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                         DispensationAttributeFluxForm dispensationAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductDispensation productDispensation = dispensationService().getOneProductDispensationById(dispensationId);
            if (fluxId != 0) {
                ProductAttributeFlux productAttributeFlux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
                if (productAttributeFlux != null) {
                    dispensationAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productDispensation);
                } else {
                    dispensationAttributeFluxForm = new DispensationAttributeFluxForm();
                    dispensationAttributeFluxForm.setProductOperationId(dispensationId);
                }
            } else {
                dispensationAttributeFluxForm = new DispensationAttributeFluxForm();
                dispensationAttributeFluxForm.setProductOperationId(productDispensation.getProductOperationId());
            }
            modelMappingForView(modelMap, dispensationAttributeFluxForm, productDispensation);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/editFlux.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           HttpServletRequest request,
                           DispensationAttributeFluxForm dispensationAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductDispensationAttributeFluxFormValidation().validate(dispensationAttributeFluxForm, result);
            ProductDispensation productDispensation = dispensationService().getOneProductDispensationById(dispensationAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = attributeService().saveProductAttribute(dispensationAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux productAttributeFlux = dispensationAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    productAttributeFlux.setStatus(productDispensation.getOperationStatus());

                    if (attributeFluxService().saveProductAttributeFlux(productAttributeFlux) != null) {
                        attributeFluxService().saveProductAttributeOtherFlux(dispensationAttributeFluxForm.getProductAttributeOtherFlux());
                    }

                    if (dispensationAttributeFluxForm.getProductOperationId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/dispensation/editFlux.form?dispensationId="
                            + dispensationAttributeFluxForm.getProductOperationId();
                }
            }

            modelMappingForView(modelMap, dispensationAttributeFluxForm, productDispensation);
        }

        return null;
    }

    private void modelMappingForView(ModelMap modelMap, DispensationAttributeFluxForm dispensationAttributeFluxForm, ProductDispensation productDispensation) {
        List<ProductDispensationFluxDTO> productAttributeFluxes = dispensationService().getProductDispensationFluxDTOs(productDispensation);
//        if (productAttributeFluxes.size() != 0) {
//            Collections.sort(productAttributeFluxes, Collections.<ProductDispensationFluxDTO>reverseOrder());
//        }
        modelMap.addAttribute("dispensationAttributeFluxForm", dispensationAttributeFluxForm);
        modelMap.addAttribute("productDispensation", productDispensation);
        modelMap.addAttribute("products", programService().getOneProductProgramById(productDispensation.getProductProgram().getProductProgramId()).getProducts());
        modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
        if (!productDispensation.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
            if (productDispensation.getOperationStatus().equals(OperationStatus.VALIDATED))
            modelMap.addAttribute("subTitle", "Réception - APPROUVEE");
            else if (productDispensation.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
                modelMap.addAttribute("subTitle", "Réception - EN ATTENTE DE VALIDATION");
            }
        } else {
            modelMap.addAttribute("subTitle", "Saisie de réception - ajout de produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "dispensationId") Integer dispensationId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductDispensation dispensation = dispensationService().getOneProductDispensationById(dispensationId);
        dispensation.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
        dispensationService().saveProductDispensation(dispensation);
        attributeService().purgeUnusedAttributes();
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La réception a été enregistré avec " +
                "succès et est en attente de validation !");
        return "redirect:/module/pharmacy/operations/dispensation/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/incomplete.form", method = RequestMethod.GET)
    public String incomplete(HttpServletRequest request,
                             @RequestParam(value = "dispensationId") Integer dispensationId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductDispensation dispensation = dispensationService().getOneProductDispensationById(dispensationId);
        dispensation.setOperationStatus(OperationStatus.NOT_COMPLETED);
        dispensationService().saveProductDispensation(dispensation);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                "continuer à modifier la réception !");
        return "redirect:/module/pharmacy/operations/dispensation/editFlux.form?dispensationId=" + dispensationId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/delete.form", method = RequestMethod.GET)
    public String deleteOperation(HttpServletRequest request,
                                  @RequestParam(value = "dispensationId") Integer dispensationId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductDispensation dispensation = dispensationService().getOneProductDispensationById(dispensationId);
        for (ProductAttributeOtherFlux otherFlux : attributeFluxService().getAllProductAttributeOtherFluxByOperation(dispensation, false)) {
            attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
        }
        for (ProductAttributeFlux flux : attributeFluxService().getAllProductAttributeFluxByOperation(dispensation, false)){
            attributeFluxService().removeProductAttributeFlux(flux);
        }
        dispensationService().removeProductDispensation(dispensation);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La réception a été supprimée avec succès !");
        return "redirect:/module/pharmacy/operations/dispensation/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "dispensationId") Integer dispensationId,
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
        return "redirect:/module/pharmacy/operations/dispensation/editFlux.form?dispensationId=" + dispensationId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "dispensationId") Integer dispensationId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductDispensation dispensation = dispensationService().getOneProductDispensationById(dispensationId);
        if (OperationUtils.validateOperation(dispensation)) {
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                    "continuer à modifier la réception !");
            return "redirect:/module/pharmacy/operations/dispensation/list.form";
        }
        return null;
    }

}
