package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.entities.*;
import org.openmrs.module.pharmacy.enumerations.*;
import org.openmrs.module.pharmacy.forms.dispensation.FindPatientForm;
import org.openmrs.module.pharmacy.forms.dispensation.ProductDispensationForm;
import org.openmrs.module.pharmacy.forms.dispensation.DispensationAttributeFluxForm;
import org.openmrs.module.pharmacy.dto.DispensationHeaderDTO;
import org.openmrs.module.pharmacy.dto.DispensationTransformationResultDTO;
import org.openmrs.module.pharmacy.dto.LastDispensationDTO;
import org.openmrs.module.pharmacy.dto.ProductDispensationFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.forms.dispensation.validators.FindPatientFormValidation;
import org.openmrs.module.pharmacy.forms.dispensation.validators.ProductDispensationAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.forms.dispensation.validators.ProductDispensationFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class PharmacyProductDispensationManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private ProductDispensationService dispensationService() {
        return Context.getService(ProductDispensationService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }

    private ProductService productService() {
        return Context.getService(ProductService.class);
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

    private ProductInventoryService getInventoryService() {
        return Context.getService(ProductInventoryService.class);
    }

    private ProductAttributeService attributeService(){
        return Context.getService(ProductAttributeService.class);
    }

    @ModelAttribute("title")
    public String getTile() {
        return "Dispensation / Vente";
    }

    @ModelAttribute("isDirectClient")
    public Boolean isDirectClient() {
        return OperationUtils.isDirectClient(OperationUtils.getUserLocation());
    }

    @ModelAttribute("canDistribute")
    public Boolean canDistribute() {
        return OperationUtils.canDistribute(OperationUtils.getUserLocation());
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap,
                     @RequestParam(value = "startDate", defaultValue = "", required = false) Date startDate,
                     @RequestParam(value = "endDate", defaultValue = "", required = false) Date endDate,
                     FindPatientForm findPatientForm) {
        if (Context.isAuthenticated()) {
            boolean canShowList = false;
            if (startDate == null) {
                ProductInventory latestInventory = getInventoryService().getLastProductInventory(
                        OperationUtils.getUserLocation(),
                        programService().getOneProductProgramById(1),
                        InventoryType.TOTAL);

                if (latestInventory != null) {
                    startDate = latestInventory.getOperationDate();
                    canShowList = true;
                }
            } else {
                canShowList = true;
            }
            DispensationTransformationResultDTO resultDTO = dispensationService().transformDispensation(OperationUtils.getUserLocation());
            if (resultDTO.getTotalPatient() != 0) {
                modelMap.addAttribute("transformationMessage", resultDTO);
            }

            if (canShowList) {
                modelMap.addAttribute("dispensationResult", dispensationService().getDispensationResult(
                        startDate,
                        new Date(),
                        OperationUtils.getUserLocation()));

                if (endDate == null) {
                    endDate = new Date();
                }

//            System.out.println("-------------------------------------------> Start date = " + OperationUtils.dateToDdMmYyyy(startDate));
//            System.out.println("-------------------------------------------> End date = " + OperationUtils.dateToDdMmYyyy(endDate));

                getDispensationByPeriodIndicatedByUser(modelMap, startDate, endDate);
            }

            modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
            modelMap.addAttribute("findPatientForm", findPatientForm);
            modelMap.addAttribute("numberPatientToTransform",
                    dispensationService().countPatientToTransform(OperationUtils.getUserLocation()).size());
            modelMap.addAttribute("subTitle", "Liste des Dispensations");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/list.form", method = RequestMethod.POST)
    public String findPatient(ModelMap modelMap,
                              @RequestParam(value = "startDate", defaultValue = "", required = false) Date startDate,
                              @RequestParam(value = "endDate", defaultValue = "", required = false) Date endDate,
                              FindPatientForm findPatientForm,
                              BindingResult result) {
        if (Context.isAuthenticated()) {
            new FindPatientFormValidation().validate(findPatientForm, result);

            if (!result.hasErrors()) {
                String mobile = "0";
                String regimen = "0";
                String patientId = "0";
                if (findPatientForm.getDispensationType().equals(DispensationType.HIV_PATIENT)) {
                    regimen = "1";

                    if (findPatientForm.getPatientIdentifier() != null) {
                        if (findPatientForm.getPatientType().equals(PatientType.OTHER_HIV)) {
                            mobile = "2";
                            patientId = getPatientInfo(findPatientForm);
                        } else
                        /*if (findPatientForm.getPatientType().equals(PatientType.ON_SITE))*/ {
                            Patient patient = dispensationService().getPatientByIdentifier(findPatientForm.getPatientIdentifier());
                            if (patient != null) {
                                MobilePatient mobilePatient = dispensationService().getOneMobilePatientByIdentifier(findPatientForm.getPatientIdentifier());
                                if (mobilePatient != null) {
                                    dispensationService().transformPatientDispensation(mobilePatient);
                                }
                                patientId = patient.getPatientId().toString();
                            } else {
                                findPatientForm.setPatientType(PatientType.MOBILE);
                                patientId = getPatientInfo(findPatientForm);
                                mobile = "1";
                            }
                        }/* else {
//                            if (findPatientForm.getPatientType().equals(PatientType.MOBILE)) {
//                                mobile = "1";
//                            } else {
//                            }
                            mobile = "2";
                            patientId = getPatientInfo(findPatientForm);
                        }*/
                    }
                } else if (findPatientForm.getDispensationType().equals(DispensationType.OTHER_PATIENT)) {
                    patientId = getPatientInfo(findPatientForm);
                }

                return "redirect:/module/pharmacy/operations/dispensation/edit.form?mob=" + mobile + "&reg=" + regimen +
                        "&patientId=" + patientId + "&programId=" + findPatientForm.getProductProgramId();
            }
            getDispensationByPeriodIndicatedByUser(modelMap, startDate, endDate);
            modelMap.addAttribute("subTitle", "Liste des Dispensations du jour");
            modelMap.addAttribute("dispensationResult", dispensationService().getDispensationResult(
                    OperationUtils.getCurrentMonthRange().getStartDate(),
                    OperationUtils.getCurrentMonthRange().getEndDate(),
                    OperationUtils.getUserLocation()));
//            modelMap.addAttribute("dispensations", dispensationService().getAllProductDispensations(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("findPatientForm", findPatientForm);
        }

        return null;
    }

    private void getDispensationByPeriodIndicatedByUser(ModelMap modelMap,
                                                        Date startDate,
                                                        Date endDate) {
        modelMap.addAttribute("dispensations", dispensationService().getDispensationListDTOsByDate(
                startDate,
                endDate,
                OperationUtils.getUserLocation()));
        // modelMap.addAttribute("subTitle", "Liste des Dispensations");
//        if (startDate == null && endDate == null) {
//            ProductInventory latestInventory = getInventoryService().getLastProductInventory(
//                    OperationUtils.getUserLocation(),
//                    programService().getOneProductProgramById(1),
//                    InventoryType.TOTAL);
//            if (latestInventory != null) {
//                modelMap.addAttribute("dispensations", dispensationService().getDispensationListDTOsByDate(
//                        latestInventory.getOperationDate(),
//                        new Date(),
//                        OperationUtils.getUserLocation()));
//            }
//            modelMap.addAttribute("subTitle", "Liste des Dispensations");
//        } else {
//
//        }
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
                }
            } else {
                productDispensationForm = new ProductDispensationForm();
                productDispensationForm.setIncidence(Incidence.NEGATIVE);
                productDispensationForm.setProductProgramId(programId);
            }

            setPatient(modelMap, mob, patientId, productDispensationForm);

            if (reg != 0) {
                modelMap.addAttribute("regimens", regimenService().getAllProductRegimen());
            }

            modelMap.addAttribute("program", programService().getOneProductProgramById(programId));
            modelMap.addAttribute("productDispensationForm", productDispensationForm);
            modelMap.addAttribute("providers", OperationUtils.getLocationPrescribers(OperationUtils.getUserLocation()));
            modelMap.addAttribute("subTitle", "Dispensation <i class=\"fa fa-play\"></i> Saisie entête");
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
                    //dispensation.setIncidence(Incidence.NEGATIVE);
                    if (productDispensationForm.getPatientType().equals(PatientType.ON_SITE)/* && programId == 1*/) {
                        Encounter encounter = productDispensationForm.getEncounter();
                        dispensation.setEncounter(Context.getEncounterService().saveEncounter(encounter));
                    } else {
                        MobilePatientDispensationInfo info = productDispensationForm.getMobileDispensationInfo();
                        info.setDispensation(dispensation);
                        Patient patient = productDispensationForm.getPatient();
                        if (patient != null &&
                                dispensationService().isTransferred(patient, OperationUtils.getUserLocation())
                                && productDispensationForm.getPatientIdentifier().equals(patient.getPatientIdentifier().getIdentifier())) {
                            info.setPatient(patient);
                            info.setMobilePatient(null);
                        } else {
                            dispensationService().saveMobilePatient(productDispensationForm.getMobilePatient());
                        }
                        dispensationService().saveMobilePatientDispensationInfo(info);
                    }
//                    dispensationService().saveProductDispensation(dispensation);

//                    if (dispensation.getProductAttributeFluxes().size() == 0) {
//                    } else {
//                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
//                    }
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    return "redirect:/module/pharmacy/operations/dispensation/editFlux.form?dispensationId=" +
                            dispensationService().saveProductDispensation(dispensation).getProductOperationId();
                }
            }

            if (reg != 0) {
                modelMap.addAttribute("regimens", regimenService().getAllProductRegimen());
            }

            setPatient(modelMap, mob, patientId, productDispensationForm);

            modelMap.addAttribute("program", programService().getOneProductProgramById(programId));
            modelMap.addAttribute("providers", OperationUtils.getLocationPrescribers(OperationUtils.getUserLocation()));
            modelMap.addAttribute("dispensationHeaderForm", productDispensationForm);
            modelMap.addAttribute("subTitle", "Dispensation <i class=\"fa fa-play\"></i> Saisie entête");
        }

        return null;
    }

    private void setPatient(ModelMap modelMap,
                            @RequestParam(value = "mob", defaultValue = "0", required = false) Integer mob,
                            @RequestParam(value = "patientId", defaultValue = "0", required = false) Integer patientId,
                            ProductDispensationForm productDispensationForm) {

        String patientIdentifier = "";

        if (mob != 0) {
            MobilePatient mobilePatient = dispensationService().getOneMobilePatientById(patientId);
            if (mobilePatient != null) {
                productDispensationForm.setMobilePatient(mobilePatient);
                modelMap.addAttribute("mobilePatient", mobilePatient);
                patientIdentifier = mobilePatient.getIdentifier();
            }
        } else {
            Patient patient = Context.getPatientService().getPatient(patientId);
            if (patient != null) {
                if (dispensationService().isDead(patient, OperationUtils.getUserLocation())) {
                    String deathDate = OperationUtils.formatDate(dispensationService().deathDate(patient, OperationUtils.getUserLocation()));

                    modelMap.addAttribute("patientAlert", "<span class=\"text-danger\"><i class=\"fa fa-exclamation-triangle\"></i> Décédé le : <span class=\"font-weight-bold\">" +
                            deathDate + "</span></span>");
                } else if (dispensationService().isTransferred(patient, OperationUtils.getUserLocation())) {
                    productDispensationForm.setPatientType(PatientType.MOBILE);
                    String transferDate = OperationUtils.formatDate(dispensationService().transferDate(patient, OperationUtils.getUserLocation()));
                    modelMap.addAttribute("patientAlert", "<span class=\"text-warning\"><i class=\"fa fa-exclamation-triangle\"></i> Transféré le : <span class=\"font-weight-bold\">" +
                            transferDate + "</span></span>");
                }
                productDispensationForm.setPatient(Context.getPatientService().getPatient(patientId));
                patientIdentifier = patient.getPatientIdentifier().getIdentifier();
            }
        }

        if (patientIdentifier != null) {
            ProductProgram program = programService().getOneProductProgramById(productDispensationForm.getProductProgramId());
            if (program != null) {
                ProductDispensation lastDispensation = dispensationService().getLastProductDispensationByPatient(
                        patientIdentifier,
                        program,
                        OperationUtils.getUserLocation()
                );

                if (lastDispensation != null) {
                    LastDispensationDTO lastDispensationDTO = new LastDispensationDTO();
                    modelMap.addAttribute("lastDispensation", lastDispensationDTO.setDispensation(lastDispensation));
                }
            }
        }

    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/deletePatient.form", method = RequestMethod.GET)
    public String deletePatient(HttpServletRequest request,
                                @RequestParam(value = "patientId", defaultValue = "0", required = false) Integer patientId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();

        if (patientId != 0) {
            MobilePatient mobilePatient = dispensationService().getOneMobilePatientById(patientId);
            if (mobilePatient != null) {
                if (mobilePatient.getMobilePatientDispensationInfos() != null && mobilePatient.getMobilePatientDispensationInfos().size() == 0)
                    dispensationService().removeMobilePatient(mobilePatient);
            }
        }
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La dispensation a été annulée !");
        return "redirect:/module/pharmacy/operations/dispensation/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                           @RequestParam(value = "dispensationId") Integer dispensationId,
                           @RequestParam(value = "productId", defaultValue = "0", required = false) Integer productId,
                           @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                           DispensationAttributeFluxForm dispensationAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductDispensation productDispensation = dispensationService().getOneProductDispensationById(dispensationId);
            if (productDispensation != null) {
                if (productId != 0) {
                    Product product = productService().getOneProductById(productId);
                    if (product != null) {
                        dispensationAttributeFluxForm.setProductAttributeFlux(productDispensation, product);
                    } else {
                        dispensationAttributeFluxForm = new DispensationAttributeFluxForm();
                        dispensationAttributeFluxForm.setProductOperationId(dispensationId);
                    }
                } else {
                    dispensationAttributeFluxForm = new DispensationAttributeFluxForm();
                    dispensationAttributeFluxForm.setProductOperationId(productDispensation.getProductOperationId());
                }

                selectProduct(modelMap, (productId == 0 ? selectedProductId : productId), dispensationAttributeFluxForm, productDispensation);

            }

        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/editFlux.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           HttpServletRequest request,
                           @RequestParam(value = "productId", defaultValue = "0", required = false) Integer productId,
                           @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                           DispensationAttributeFluxForm dispensationAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductDispensationAttributeFluxFormValidation().validate(dispensationAttributeFluxForm, result);
            ProductDispensation productDispensation = dispensationService().getOneProductDispensationById(dispensationAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                List<ProductAttributeFlux> fluxes = dispensationAttributeFluxForm.getProductAttributeFluxes();
                if (fluxes != null) {
                    for (ProductAttributeFlux flux : fluxes) {
                        if (flux.getQuantity() != 0) {
                            flux.setStatus(productDispensation.getOperationStatus());
                            attributeFluxService().saveProductAttributeFlux(flux);
                        } else {
                            attributeFluxService().removeProductAttributeFlux(flux);
                        }
                    }
                    attributeFluxService().saveProductAttributeOtherFlux(dispensationAttributeFluxForm.getProductAttributeOtherFlux());

                    if (dispensationAttributeFluxForm.getProductId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/dispensation/editFlux.form?dispensationId="
                            + dispensationAttributeFluxForm.getProductOperationId();
                }
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit indisponible en stock !");
            }

            selectProduct(modelMap, (productId == 0 ? selectedProductId : productId), dispensationAttributeFluxForm, productDispensation);
        }

        return null;
    }

    private void selectProduct(ModelMap modelMap, @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId, DispensationAttributeFluxForm dispensationAttributeFluxForm, ProductDispensation productDispensation) {
        if (selectedProductId != 0) {
            Product product =  productService().getOneProductById(selectedProductId);
            modelMap.addAttribute("selectedProduct", product);
            List<ProductAttributeStock> stocks = stockService().getProductAttributeStocksByProduct(product, OperationUtils.getUserLocation());
            Integer quantity = stockService().getAllProductAttributeStockByProductCount(
                    product, productDispensation.getProductProgram(),
                    OperationUtils.getUserLocation(), false);

            modelMap.addAttribute("selectedProductQuantityInStock", quantity);
            if (stocks.size() == 0) {
                modelMap.addAttribute("productMessage", "Ce produit n'existe pas en stock");
            } else if (quantity == 0) {
                modelMap.addAttribute("productMessage", "Ce produit est en rupture de stock");
            }
            dispensationAttributeFluxForm.setSelectedProductId(selectedProductId);
        }

        modelMappingForView(modelMap, dispensationAttributeFluxForm, productDispensation);
    }

    private void modelMappingForView(ModelMap modelMap,
                                     DispensationAttributeFluxForm dispensationAttributeFluxForm,
                                     ProductDispensation productDispensation) {

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
                if (info.getPatient() != null) {
                    headerDTO.setAge(info.getPatient().getAge());
                    headerDTO.setGender(info.getPatient().getGender());
                    headerDTO.setPatientIdentifier(info.getPatient().getPatientIdentifier().getIdentifier());
                    if (dispensationService().isTransferred(info.getPatient(), OperationUtils.getUserLocation())) {
                        if (dispensationService().transferDate(info.getPatient(), OperationUtils.getUserLocation()).after(productDispensation.getOperationDate())) {
                            modelMap.addAttribute("patientAlert", "<span class=\"text-warning\">Transféré le : <span class=\"font-weight-bold\">" +
                                    dispensationService().transferDate(info.getPatient(), OperationUtils.getUserLocation()) + "</span></span>");
                            headerDTO.setPatientType(PatientType.MOBILE);
                        } else {
                            headerDTO.setPatientType(PatientType.ON_SITE);
                        }
                    } else if (dispensationService().isDead(info.getPatient(), OperationUtils.getUserLocation())){
                        modelMap.addAttribute("patientAlert", "<span class=\"text-danger\">Décédé le : <span class=\"font-weight-bold\">" +
                                dispensationService().deathDate(info.getPatient(), OperationUtils.getUserLocation())+ "</span></span>");
                    }

                    modelMap.addAttribute("mobilePatient", info.getPatient());
                } else if (info.getMobilePatient() != null) {
                    headerDTO.setAge(info.getMobilePatient().getAge());
                    headerDTO.setGender(info.getMobilePatient().getGender());
                    headerDTO.setPatientIdentifier(info.getMobilePatient().getIdentifier());
                    headerDTO.setPatientType(info.getMobilePatient().getPatientType());
                    modelMap.addAttribute("mobilePatient", info.getMobilePatient());
                }
                if (info.getProvider() != null) {
                    headerDTO.setProvider(info.getProvider());
                }
                headerDTO.setTreatmentDays(info.getTreatmentDays());
                headerDTO.setGoal(info.getGoal());
                headerDTO.setTreatmentEndDate(info.getTreatmentEndDate());
                headerDTO.setProductRegimen(info.getProductRegimen());
                headerDTO.setProductRegimenLine(info.getRegimenLine());
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
                        .equals(OperationUtils.getConceptIdInGlobalProperties("TreatmentEndDate"))) {
                    headerDTO.setTreatmentEndDate(obs.getValueDatetime());
                } else if (obs.getConcept().getConceptId()
                        .equals(OperationUtils.getConceptIdInGlobalProperties("TreatmentDays"))) {
                    headerDTO.setTreatmentDays(obs.getValueNumeric().intValue());
                } else if (obs.getConcept().getConceptId()
                        .equals(OperationUtils.getConceptIdInGlobalProperties("Regimen"))) {
                    headerDTO.setProductRegimen(regimenService().getOneProductRegimenByConceptId(obs.getValueCoded().getConceptId()));
                    regimen = headerDTO.getProductRegimen();
                } else if (obs.getConcept().getConceptId()
                        .equals(OperationUtils.getConceptIdInGlobalProperties("RegimenLine"))) {
                    if (obs.getValueCoded().getConceptId().equals(164730)) {
                        headerDTO.setProductRegimenLine(1);
                    } else if (obs.getValueCoded().getConceptId().equals(164732)) {
                        headerDTO.setProductRegimenLine(2);
                    } else if (obs.getValueCoded().getConceptId().equals(164734)) {
                        headerDTO.setProductRegimenLine(3);
                    }
                } else if (obs.getConcept().getConceptId()
                        .equals(OperationUtils.getConceptIdInGlobalProperties("Goal"))) {
                    headerDTO.setGoal(Goal.valueOf(obs.getValueText()));
                }
            }
            headerDTO.setPatientType(PatientType.ON_SITE);
        }

        if (headerDTO.getPatientIdentifier() != null) {
            ProductDispensation lastDispensation;
            if (productDispensation.getOperationStatus().equals(OperationStatus.VALIDATED)) {
                lastDispensation = dispensationService().getLastProductDispensationByPatient(headerDTO.getPatientIdentifier(),
                        headerDTO.getProductProgram(),
                        OperationUtils.getUserLocation(), productDispensation.getOperationDate());
            } else {
                lastDispensation = dispensationService().getLastProductDispensationByPatient(headerDTO.getPatientIdentifier(),
                        headerDTO.getProductProgram(),
                        OperationUtils.getUserLocation());
            }

            if (lastDispensation != null) {
                LastDispensationDTO lastDispensationDTO = new LastDispensationDTO();
                modelMap.addAttribute("lastDispensation", lastDispensationDTO.setDispensation(lastDispensation));
            }
        }

        Set<Product> products;
        if (regimen != null && regimen.getProducts().size() != 0) {
            products = regimen.getProducts();
        } else {
            products = new HashSet<Product>(productService().getProductWithoutRegimenByProgram(headerDTO.getProductProgram()));
        }
        List<ProductDispensationFluxDTO> productAttributeFluxes = dispensationService().getProductDispensationFluxDTOs(productDispensation);
        if (productAttributeFluxes == null) {
            productAttributeFluxes = new ArrayList<ProductDispensationFluxDTO>();
        }
        if (productAttributeFluxes.size() != 0 && products.size() != 0) {
            for (ProductDispensationFluxDTO fluxDTO : productAttributeFluxes) {
                products.remove(productService().getOneProductById(fluxDTO.getProductId()));
            }
        }

        modelMap.addAttribute("dispensationAttributeFluxForm", dispensationAttributeFluxForm);
        modelMap.addAttribute("headerDTO", headerDTO);
        modelMap.addAttribute("products", products);
        modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
        modelMap.addAttribute("dispensationId", productDispensation.getProductOperationId());
        if (productDispensation.getOperationStatus().equals(OperationStatus.VALIDATED))
            modelMap.addAttribute("subTitle", "Dispensation <i class=\"fa fa-play\"></i> APPROUVEE");
        else {
            modelMap.addAttribute("subTitle", "Dispensation <i class=\"fa fa-play\"></i> Ajout de produits");
        }
    }

//    public DispensationHeaderDTO getHeaderDTO (ProductDispensation productDispensation) {
//
//        DispensationHeaderDTO headerDTO = new DispensationHeaderDTO();
//        headerDTO.setPrescriptionDate(productDispensation.getPrescriptionDate());
//        headerDTO.setOperationDate(productDispensation.getOperationDate());
//        headerDTO.setProductOperationId(productDispensation.getProductOperationId());
//        headerDTO.setProductProgram(productDispensation.getProductProgram());
//        headerDTO.setOperationStatus(productDispensation.getOperationStatus());
//
//        if (productDispensation.getEncounter() == null) {
//
//            MobilePatientDispensationInfo info = dispensationService().
//                    getOneMobilePatientDispensationInfoId(productDispensation.getProductOperationId());
//            if (info != null) {
//                if (info.getMobilePatient() != null) {
//                    headerDTO.setAge(info.getMobilePatient().getAge());
//                    headerDTO.setGender(info.getMobilePatient().getGender());
//                    headerDTO.setPatientIdentifier(info.getMobilePatient().getIdentifier());
//                    headerDTO.setPatientType(info.getMobilePatient().getPatientType());
//                    headerDTO.setMobilePatient(info.getMobilePatient());
//                }
//
//                headerDTO.setProvider(info.getProvider());
//                headerDTO.setTreatmentDays(info.getTreatmentDays());
//                headerDTO.setGoal(info.getGoal());
//                headerDTO.setTreatmentEndDate(info.getTreatmentEndDate());
//                headerDTO.setProductRegimen(info.getProductRegimen());
//            }
//        } else {
//
//            Encounter encounter = productDispensation.getEncounter();
//            headerDTO.setAge(encounter.getPatient().getAge());
//            headerDTO.setGender(encounter.getPatient().getGender());
//            headerDTO.setPatientIdentifier(encounter.getPatient().getPatientIdentifier().getIdentifier());
//            Set<EncounterProvider> encounterProviders = encounter.getEncounterProviders();
//            for (EncounterProvider encounterProvider : encounterProviders) {
//                headerDTO.setProvider(encounterProvider.getProvider());
//                break;
//            }
//
//            for (Obs obs : encounter.getAllObs(false)) {
//                if (obs.getConcept().getConceptId()
//                        .equals(Integer.getInteger(Context.getAdministrationService().getGlobalProperty("pharmacy.dispensationTreatmentEndDate")))) {
//                    headerDTO.setTreatmentEndDate(obs.getValueDate());
//                } else if (obs.getConcept().getConceptId()
//                        .equals(Integer.getInteger(Context.getAdministrationService().getGlobalProperty("pharmacy.dispensationTreatmentDays")))) {
//                    headerDTO.setTreatmentDays(obs.getValueNumeric().intValue());
//                } else if (obs.getConcept().getConceptId()
//                        .equals(Integer.getInteger(Context.getAdministrationService().getGlobalProperty("pharmacy.dispensationRegimen")))) {
//                    headerDTO.setProductRegimen(regimenService().getOneProductRegimenByConceptId(obs.getValueCoded().getConceptId()));
//                } else if (obs.getConcept().getConceptId()
//                        .equals(Integer.getInteger(Context.getAdministrationService().getGlobalProperty("pharmacy.dispensationGoal")))) {
//                    headerDTO.setGoal(Goal.valueOf(obs.getValueText()));
//                }
//            }
//            headerDTO.setPatientType(PatientType.ON_SITE);
//        }
//        return headerDTO;
//    }

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
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La dispensation a été enregistrée avec " +
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
        if (dispensation.getMobilePatientDispensationInfo() != null) {
            MobilePatientDispensationInfo info = dispensation.getMobilePatientDispensationInfo();
            MobilePatient mobilePatient = info.getMobilePatient();
            dispensationService().removeMobilePatientInfo(info);
            if (mobilePatient != null) {
                if (mobilePatient.getMobilePatientDispensationInfos().size() == 0) {
                    dispensationService().removeMobilePatient(mobilePatient);
                }
            }
        } else if (dispensation.getEncounter() != null) {
            Encounter encounter = dispensation.getEncounter();
            Context.getEncounterService().voidEncounter(encounter, "Cancelled information for dispensation");
        }

        dispensationService().removeProductDispensation(dispensation);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La dispensation a été supprimée avec succès !");
        return "redirect:/module/pharmacy/operations/dispensation/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "dispensationId") Integer dispensationId,
                             @RequestParam(value = "productId") Integer productId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductDispensation dispensation = dispensationService().getOneProductDispensationById(dispensationId);
        if (dispensation != null) {
            Product product = productService().getOneProductById(productId);
            if (product != null) {
                List<ProductAttributeFlux> fluxes = attributeFluxService().getAllProductAttributeFluxByOperationAndProduct(dispensation, product);

                for (ProductAttributeFlux flux : fluxes) {
                    attributeFluxService().removeProductAttributeFlux(flux);
                }
                ProductAttributeOtherFlux otherFlux = attributeFluxService().getOneProductAttributeOtherFluxByProductAndOperation(product, dispensation);
                if (otherFlux != null) {
                    attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
                }
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");

                return "redirect:/module/pharmacy/operations/dispensation/editFlux.form?dispensationId=" + dispensationId;
            }
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "dispensationId") Integer dispensationId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductDispensation dispensation = dispensationService().getOneProductDispensationById(dispensationId);
        if (OperationUtils.validateOperation(dispensation)) {
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La dispensation a été enregistrée avec succès !");
            return "redirect:/module/pharmacy/operations/dispensation/list.form";
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/dispensation/cancel.form", method = RequestMethod.GET)
    public String cancel(HttpServletRequest request,
                         @RequestParam(value = "dispensationId") Integer dispensationId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductDispensation dispensation = dispensationService().getOneProductDispensationById(dispensationId);
        if (OperationUtils.cancelDispensation(dispensation)) {
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La dispensation a été annulée avec succès !");
            return "redirect:/module/pharmacy/operations/dispensation/list.form";
        }
        return null;
    }


    @RequestMapping(value = "transform-dispensation.form", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> transformDispensationAjax() {

        DispensationTransformationResultDTO resultDTO = dispensationService().transformDispensation(OperationUtils.getUserLocation());
        return new ResponseEntity<String>(resultDTO.toString(), HttpStatus.OK);
    }

//
//    @RequestMapping(value = "patient-dead.form", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<String> patientDeadAjax() {
//        DispensationTransformationResultDTO resultDTO = dispensationService().transformDispensation(OperationUtils.getUserLocation());
//        return new ResponseEntity<String>(resultDTO.toString(), HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "patient-transferred.form", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<String> patientTransferredAjax() {
//
//        DispensationTransformationResultDTO resultDTO = dispensationService().transformDispensation(OperationUtils.getUserLocation());
//        return new ResponseEntity<String>(resultDTO.toString(), HttpStatus.OK);
//    }


}
