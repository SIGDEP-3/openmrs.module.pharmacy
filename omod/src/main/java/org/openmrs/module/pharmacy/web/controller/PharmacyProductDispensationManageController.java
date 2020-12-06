package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterProvider;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.Goal;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.PatientType;
import org.openmrs.module.pharmacy.forms.FindPatientForm;
import org.openmrs.module.pharmacy.forms.ProductDispensationForm;
import org.openmrs.module.pharmacy.forms.DispensationAttributeFluxForm;
import org.openmrs.module.pharmacy.models.DispensationHeaderDTO;
import org.openmrs.module.pharmacy.models.ProductDispensationFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.validators.FindPatientFormValidation;
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
import java.util.Set;

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
    public String findPatient(ModelMap modelMap,
                              FindPatientForm findPatientForm,
                              BindingResult result) {
        if (Context.isAuthenticated()) {
            new FindPatientFormValidation().validate(findPatientForm, result);

            if (!result.hasErrors()) {
                String mobile = "0";
                String regimen = "0";
                String patientId = "0";
                if (findPatientForm.getPatientIdentifier() != null) {
                    if (!findPatientForm.getPatientType().equals(PatientType.MOBILE)) {
                        regimen = "1";
                        Patient patient = dispensationService().getPatientByIdentifier(findPatientForm.getPatientIdentifier());
                        if (patient != null) {
                            patientId = patient.getPatientId().toString();
                        } else {
                            patientId = getPatientInfo(findPatientForm);
                            mobile = "1";
                        }
                    } else {
                        patientId = getPatientInfo(findPatientForm);
                        mobile = "1";
                    }
                }

                return "redirect:/module/pharmacy/operations/dispensation/edit.form?mob=" + mobile + "&reg=" + regimen +
                        "&patientId=" + patientId + "&programId=" + findPatientForm.getProductProgramId();
            }

            modelMap.addAttribute("dispensations", dispensationService().getAllProductDispensations(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("findPatientForm", findPatientForm);
            modelMap.addAttribute("subTitle", "Liste des Dispensations du jour");
        }

        return null;
    }

    private String getPatientInfo(FindPatientForm findPatientForm) {
        String patientId;
        MobilePatient mobilePatient = dispensationService().getOneMobilePatientByIdentifier(findPatientForm.getPatientIdentifier());
        if (mobilePatient != null) {
            patientId = mobilePatient.getMobilePatientId().toString();
        } else {
            mobilePatient = new MobilePatient();
            mobilePatient.setLocation(OperationUtils.getUserLocation());
            mobilePatient.setIdentifier(findPatientForm.getPatientIdentifier());
            mobilePatient.setPatientType(findPatientForm.getPatientType());
            mobilePatient.setAge(0);
            patientId = dispensationService().saveMobilePatient(mobilePatient).getMobilePatientId().toString();
        }
        return patientId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       @RequestParam(value = "reg", defaultValue = "0", required = false) Integer reg,
                       @RequestParam(value = "mob", defaultValue = "0", required = false) Integer mob,
                       @RequestParam(value = "patientId", defaultValue = "0", required = false) Integer patientId,
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

                productDispensationForm = new ProductDispensationForm();

                setPatient(mob, patientId, productDispensationForm);
                productDispensationForm.setProductProgramId(programId);
            }

            if (reg != 0) {
                modelMap.addAttribute("regimens", regimenService().getAllProductRegimen());
            }
            modelMap.addAttribute("program", programService().getOneProductProgramById(programId));
            modelMap.addAttribute("productDispensationForm", productDispensationForm);
            modelMap.addAttribute("providers", Context.getProviderService().getAllProviders());
            modelMap.addAttribute("subTitle", "Saisie de dispensation");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       @RequestParam(value = "reg", defaultValue = "0", required = false) Integer reg,
                       @RequestParam(value = "mob", defaultValue = "0", required = false) Integer mob,
                       @RequestParam(value = "patientId", defaultValue = "0", required = false) Integer patientId,
                       ProductDispensationForm productDispensationForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductDispensationFormValidation().validate(productDispensationForm, result);

            if (!result.hasErrors()) {

                ProductDispensation dispensation = productDispensationForm.getProductDispensation();
                if (dispensation != null) {
                    if (mob == 0) {
                        Encounter encounter = productDispensationForm.getEncounter();
                        dispensation.setEncounter(Context.getEncounterService().saveEncounter(encounter));
                        dispensationService().saveProductDispensation(dispensation);

                    } else {
                        MobilePatient mobilePatient = dispensationService().saveMobilePatient(productDispensationForm.getMobilePatient());
                        dispensationService().saveProductDispensation(dispensation);
                        MobilePatientDispensationInfo info = productDispensationForm.getMobileDispensationInfo();
                        info.setMobilePatient(mobilePatient);
                        info.setDispensation(dispensation);
                        dispensationService().saveMobilePatientDispensationInfo(info);
                    }

//                    if (dispensation.getProductAttributeFluxes().size() == 0) {
//                    } else {
//                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
//                    }
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    return "redirect:/module/pharmacy/operations/dispensation/editFlux.form?dispensationId=" +
                            dispensation.getProductOperationId();
                }

            }

            if (reg != 0) {
                modelMap.addAttribute("regimens", regimenService().getAllProductRegimen());
            }

            setPatient(mob, patientId, productDispensationForm);

            modelMap.addAttribute("program", programService().getOneProductProgramById(programId));
            modelMap.addAttribute("providers", Context.getProviderService().getAllProviders());
            modelMap.addAttribute("dispensationHeaderForm", productDispensationForm);
            modelMap.addAttribute("subTitle", "Saisie de dispensation");
        }

        return null;
    }

    private void setPatient(@RequestParam(value = "mob", defaultValue = "0", required = false) Integer mob,
                            @RequestParam(value = "patientId", defaultValue = "0", required = false) Integer patientId,
                            ProductDispensationForm productDispensationForm) {
        if (mob != 0) {
            MobilePatient mobilePatient = dispensationService().getOneMobilePatientById(patientId);
            if (mobilePatient != null) {
                productDispensationForm.setMobilePatient(mobilePatient);
            }
        } else {
            Patient patient = Context.getPatientService().getPatient(patientId);
            if (patient != null) {
                productDispensationForm.setPatient(Context.getPatientService().getPatient(patientId));
            }
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                         @RequestParam(value = "dispensationId") Integer dispensationId,
                         @RequestParam(value = "productId", defaultValue = "0", required = false) Integer fluxId,
                         DispensationAttributeFluxForm dispensationAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductDispensation productDispensation = dispensationService().getOneProductDispensationById(dispensationId);
            if (productDispensation != null) {
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

                System.out.println(" ========================================> Here is the place where we will begin entering in maps ");

                modelMappingForView(modelMap, dispensationAttributeFluxForm, productDispensation);
            }

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

//            if (!result.hasErrors()) {
//                ProductAttribute productAttribute = attributeService().saveProductAttribute(dispensationAttributeFluxForm.getProductAttribute());
//                if (productAttribute != null) {
//                    ProductAttributeFlux productAttributeFlux = dispensationAttributeFluxForm.getProductAttributeFlux(productAttribute);
//                    productAttributeFlux.setStatus(productDispensation.getOperationStatus());
//
//                    if (attributeFluxService().saveProductAttributeFlux(productAttributeFlux) != null) {
//                        attributeFluxService().saveProductAttributeOtherFlux(dispensationAttributeFluxForm.getProductAttributeOtherFlux());
//                    }
//
//                    if (dispensationAttributeFluxForm.getProductOperationId() == null) {
//                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
//                    } else {
//                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
//                    }
//
//                    return "redirect:/module/pharmacy/operations/dispensation/editFlux.form?dispensationId="
//                            + dispensationAttributeFluxForm.getProductOperationId();
//                }
//            }

            modelMappingForView(modelMap, dispensationAttributeFluxForm, productDispensation);
        }

        return null;
    }

    private void modelMappingForView(ModelMap modelMap,
                                     DispensationAttributeFluxForm dispensationAttributeFluxForm,
                                     ProductDispensation productDispensation) {
        List<ProductDispensationFluxDTO> productAttributeFluxes = dispensationService().getProductDispensationFluxDTOs(productDispensation);
//        if (productAttributeFluxes.size() != 0) {
//            Collections.sort(productAttributeFluxes, Collections.<ProductDispensationFluxDTO>reverseOrder());
//        }

        ProductRegimen regimen = new ProductRegimen();

        DispensationHeaderDTO headerDTO = new DispensationHeaderDTO();
        headerDTO.setPrescriptionDate(productDispensation.getPrescriptionDate());
        headerDTO.setOperationDate(productDispensation.getOperationDate());
        headerDTO.setProductOperationId(productDispensation.getProductOperationId());
        headerDTO.setProductProgram(productDispensation.getProductProgram());
        headerDTO.setOperationStatus(productDispensation.getOperationStatus());

        if (productDispensation.getEncounter() == null) {

            MobilePatientDispensationInfo info = dispensationService().
                    getOneMobilePatientDispensationInfoId(productDispensation.getProductOperationId());
            if (info != null) {
                if (info.getMobilePatient() != null) {
                    headerDTO.setAge(info.getMobilePatient().getAge());
                    headerDTO.setGender(info.getMobilePatient().getGender());
                    headerDTO.setPatientIdentifier(info.getMobilePatient().getIdentifier());
                    headerDTO.setPatientType(info.getMobilePatient().getPatientType());
                }
                headerDTO.setProvider(info.getProvider());
                headerDTO.setTreatmentDays(info.getTreatmentDays());
                headerDTO.setGoal(info.getGoal());
                headerDTO.setTreatmentEndDate(info.getTreatmentEndDate());
                headerDTO.setProductRegimen(info.getProductRegimen());
                regimen = info.getProductRegimen();
            }
        } else {
            Encounter encounter = productDispensation.getEncounter();
            headerDTO.setAge(encounter.getPatient().getAge());
            headerDTO.setGender(encounter.getPatient().getGender());
            headerDTO.setPatientIdentifier(encounter.getPatient().getPatientIdentifier().getIdentifier());
            Set<EncounterProvider> encounterProviders = encounter.getEncounterProviders();
            for (EncounterProvider encounterProvider : encounterProviders) {
                headerDTO.setProvider(encounterProvider.getProvider());
                break;
            }

            for (Obs obs : encounter.getAllObs(false)) {
                if (obs.getConcept().getConceptId()
                        .equals(Integer.getInteger(Context.getAdministrationService().getGlobalProperty("pharmacy.dispensationTreatmentEndDate")))) {
                    headerDTO.setTreatmentEndDate(obs.getValueDate());
                } else if (obs.getConcept().getConceptId()
                        .equals(Integer.getInteger(Context.getAdministrationService().getGlobalProperty("pharmacy.dispensationTreatmentDays")))) {
                    headerDTO.setTreatmentDays(obs.getValueNumeric().intValue());
                } else if (obs.getConcept().getConceptId()
                        .equals(Integer.getInteger(Context.getAdministrationService().getGlobalProperty("pharmacy.dispensationRegimen")))) {
                    headerDTO.setProductRegimen(regimenService().getOneProductRegimenByConceptId(obs.getValueCoded().getConceptId()));
                    regimen = headerDTO.getProductRegimen();
                } else if (obs.getConcept().getConceptId()
                        .equals(Integer.getInteger(Context.getAdministrationService().getGlobalProperty("pharmacy.dispensationGoal")))) {
                    headerDTO.setGoal(Goal.valueOf(obs.getValueText()));
                }
            }
            headerDTO.setPatientType(PatientType.ON_SITE);
        }
        modelMap.addAttribute("dispensationAttributeFluxForm", dispensationAttributeFluxForm);
        modelMap.addAttribute("headerDTO", headerDTO);
        modelMap.addAttribute("products", regimen.getProducts());
        modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
        if (productDispensation.getOperationStatus().equals(OperationStatus.VALIDATED))
            modelMap.addAttribute("subTitle", "Dispensation - APPROUVEE");
        else {
            modelMap.addAttribute("subTitle", "Saisie de dispensation");
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
                    .getOneProductAttributeOtherFluxByAttributeAndOperation(flux.getProductAttribute(), flux.getProductOperation(), OperationUtils.getUserLocation());
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
