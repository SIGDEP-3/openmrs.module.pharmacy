package org.openmrs.module.pharmacy.web.controller;

import javafx.scene.effect.SepiaTone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.StockEntryType;
import org.openmrs.module.pharmacy.enumerations.StockOutType;
import org.openmrs.module.pharmacy.forms.InventoryAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.MovementAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ProductMovementForm;
import org.openmrs.module.pharmacy.forms.ReceptionAttributeFluxForm;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.validators.ProductMovementAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.validators.ProductMovementFormValidation;
import org.openmrs.module.pharmacy.validators.ProductReceptionAttributeFluxFormValidation;
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
    private ProductReceptionService receptionService() {
        return Context.getService(ProductReceptionService.class);
    }
    private PharmacyService pharmacyService() {
        return Context.getService(PharmacyService.class);
    }
    private ProductAttributeFluxService productAttributeFluxService(){
        return Context.getService(ProductAttributeFluxService.class);
    }
    private ProductAttributeFluxService attributeFluxService(){
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
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {

        if (Context.isAuthenticated()) {
            modelMap.addAttribute("entries", service().getAllProductMovementEntry(getUserLocation(), false));
            modelMap.addAttribute("outs", service().getAllProductMovementOut(getUserLocation(), false));
            modelMap.addAttribute("stockEntryTypes", getEntryTypeLabels());
            modelMap.addAttribute("stockOutTypes", getOutTypeLabels());
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("subTitle", "Liste des Mouvements");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       @RequestParam(value = "type") String type,
                       ProductMovementForm productMovementForm) {
        if (Context.isAuthenticated()) {
            String movementType = "";
            StockEntryType entryType;
            StockOutType outType;
            ProductProgram program;
            if (getOutTypeLabels().containsKey(type)){
                movementType = "out";
                outType = StockOutType.valueOf(type);
                if (id != 0) {
                    productMovementForm.setProductMovementOut(service().getOneProductMovementOutById(id));
                    program = programService().getOneProductProgramById(productMovementForm.getProductProgramId());
                }
                else {
                    productMovementForm = new ProductMovementForm();
                    productMovementForm.setIncidence(Incidence.NEGATIVE);
                    productMovementForm.setProductProgramId(programId);
                    productMovementForm.setLocationId(getUserLocation().getLocationId());
                    productMovementForm.setStockOutType(outType);
                    program = programService().getOneProductProgramById(programId);
                }
                modelMap.addAttribute("subTitle", "Mouvement de sortie ("+ getOutTypeLabels()
                        .get(outType.name())+")");
            }
            else {
                movementType = "entry";
                entryType = StockEntryType.valueOf(type);
                if (id != 0) {
                    productMovementForm.setProductMovementEntry(service().getOneProductMovementEntryById(id));
                    program = programService().getOneProductProgramById(productMovementForm.getProductProgramId());
                }
                else {
                    productMovementForm = new ProductMovementForm();
                    productMovementForm.setIncidence(Incidence.POSITIVE);
                    productMovementForm.setLocationId(getUserLocation().getLocationId());
                    productMovementForm.setStockEntryType(entryType);
                    program = programService().getOneProductProgramById(programId);
                }
                modelMap.addAttribute("subTitle", "Mouvement d'entrée ("+ getEntryTypeLabels()
                        .get(entryType.name())+")");
            }
            productMovementForm.setProductProgramId(programId);
            modelMap.addAttribute("productMovementForm", productMovementForm);
            modelMap.addAttribute("type", type);
            modelMap.addAttribute("program", program);
            modelMap.addAttribute("exchanges", ExchangeService().getAllProductExchange());
            modelMap.addAttribute("movementType", movementType);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       @RequestParam(value = "type") String type,
                       ProductMovementForm productMovementEntryForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();
            new ProductMovementFormValidation().validate(productMovementEntryForm, result);

            Integer movementId;
            String movementType;
            Set<ProductAttributeFlux> fluxes = new HashSet<ProductAttributeFlux>();
            System.out.println("_--------------- debut des enregistrements");

            if (!result.hasErrors()) {
                System.out.println("_--------------- si pas d'erreur");
                if (getOutTypeLabels().containsKey(type)){
                    ProductMovementOut out = productMovementEntryForm.getProductMovementOut();
                    // out = service().saveProductMovementOut(out);
                    movementId = service().saveProductMovementOut(out).getProductOperationId();
                    movementType = "out";
                    fluxes = out.getProductAttributeFluxes();
                    System.out.println("_--------------- enregistrement des sorties");
                }
                else {
                    System.out.println("_--------------- enregistrement des entrees");
                    ProductMovementEntry entry = service().saveProductMovementEntry(productMovementEntryForm.getProductMovementEntry());
                    movementId = entry.getProductOperationId();
                    movementType = "entry";
                    fluxes = entry.getProductAttributeFluxes();

                }
                if (action.equals("addLine")) {
                    if (fluxes.size() == 0) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                    }

                    return "redirect:/module/pharmacy/operations/movement/editFlux.form?" +
                            "type="+ movementType +"&movementId=" + movementId +"&programId=" + programId;
                }
                else {
                    if (productMovementEntryForm.getProductOperationId() != null){
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Votre Operation a été Modifiée avec succès !");
                    }else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Votre Operation a été enregistrée avec succès !");
                    }
                    return "redirect:/module/pharmacy/operations/movement/list.form";
                }
            }
            modelMap.addAttribute("productMovementEntryForm", productMovementEntryForm);
//            modelMap.addAttribute("product", receptionHeaderForm.getProduct());
            modelMap.addAttribute("programs", programService().getAllProductProgram());
//            modelMap.addAttribute("program", programService().getOneProductProgramById(programId));
            modelMap.addAttribute("exchanges", ExchangeService().getAllProductExchange());
            modelMap.addAttribute("subTitle", "Saisie  de Mouvements");
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                           @RequestParam(value = "movementId") Integer movementId,
                           @RequestParam(value = "type") String type ,
                           @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                           MovementAttributeFluxForm movementAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductOperation productMovement = pharmacyService().getOneProductOperationById(movementId);
            if (productMovement != null){
                if (fluxId != 0) {
                    ProductAttributeFlux productAttributeFlux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
                    if (productAttributeFlux != null) {
                        movementAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productMovement);
                    } else {
                        movementAttributeFluxForm = new MovementAttributeFluxForm();
                        movementAttributeFluxForm.setProductOperationId(movementId);
                    }
                }
                else {
                    movementAttributeFluxForm = new MovementAttributeFluxForm();
                    movementAttributeFluxForm.setProductOperationId(movementId);
                }

                modelMappingForView(modelMap, movementAttributeFluxForm, productMovement, type);
            }
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/editFlux.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           @RequestParam(value = "type") String type,
                           HttpServletRequest request,
                           MovementAttributeFluxForm movementAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();
            new ProductMovementAttributeFluxFormValidation().validate(movementAttributeFluxForm, result);
            ProductOperation productMovement = pharmacyService().getOneProductOperationById(movementAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = productAttributeService().saveProductAttribute(movementAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux productAttributeFlux = movementAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    productAttributeFlux.setStatus(productMovement.getOperationStatus());
                    productAttributeFluxService().saveProductAttributeFlux(productAttributeFlux);
                    if (movementAttributeFluxForm.getProductOperationId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/movement/editFlux.form?type=" + type +"&movementId="
                            + movementAttributeFluxForm.getProductOperationId();
                }
            }

            modelMappingForView(modelMap, movementAttributeFluxForm, productMovement, type);
        }

        return null;
    }

    private void modelMappingForView(ModelMap modelMap, MovementAttributeFluxForm movementAttributeFluxForm,
                                     ProductOperation productMovement, String type) {
        List<ProductAttributeFlux> productAttributeFluxes = attributeFluxService()
                .getAllProductAttributeFluxByOperation(productMovement, false);
        if (productAttributeFluxes.size() != 0) {
            Collections.sort(productAttributeFluxes, Collections.<ProductAttributeFlux>reverseOrder());
        }
        if (type.equals("out")){
            modelMap.addAttribute("productMovement", service().getOneProductMovementOutById(productMovement.getProductOperationId()));
        }
        else {
            modelMap.addAttribute("productMovement", service().getOneProductMovementEntryById(productMovement.getProductOperationId()));
        }
        modelMap.addAttribute("movementAttributeFluxForm", movementAttributeFluxForm);
        modelMap.addAttribute("type", type);
        modelMap.addAttribute("products", programService().getOneProductProgramById(productMovement.getProductProgram().getProductProgramId()).getProducts());
        modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);

        if (!productMovement.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
            if (productMovement.getOperationStatus().equals(OperationStatus.VALIDATED))
                modelMap.addAttribute("subTitle", "Inventaire - APPROUVEE");
            else if (productMovement.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
                modelMap.addAttribute("subTitle", "Mouvenemet - EN ATTENTE DE VALIDATION");
            }
        } else {
            modelMap.addAttribute("subTitle", "Saisie du mouvement - ajout de produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "movementId") Integer movementId,
                           @RequestParam(value = "type") String type){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        if (type.equals("out")){
            ProductMovementOut movementOut = service().getOneProductMovementOutById(movementId);
            movementOut.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
            service().saveProductMovementOut(movementOut);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le Mouvement de sortiea été enregistré avec " +
                    "succès et est en attente de validation !");
        }
        else{
            ProductMovementEntry movementEntry = service().getOneProductMovementEntryById(movementId);
            movementEntry.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
            service().saveProductMovementEntry(movementEntry);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le Mouvement d'entée a été enregistré avec " +
                    "succès et est en attente de validation !");
        }

        return "redirect:/module/pharmacy/operations/movement/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/incomplete.form", method = RequestMethod.GET)
    public String incomplete(HttpServletRequest request,
                             @RequestParam(value = "movementId") Integer movementId,
                             @RequestParam(value = "type") String type){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        if (type.equals("out")){
            ProductMovementOut movementOut = service().getOneProductMovementOutById(movementId);
            movementOut.setOperationStatus(OperationStatus.NOT_COMPLETED);
            service().saveProductMovementOut(movementOut);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à modifier le Mouvement de sortie");
        }
        else{
            ProductMovementEntry movementEntry = service().getOneProductMovementEntryById(movementId);
            movementEntry.setOperationStatus(OperationStatus.NOT_COMPLETED);
            service().saveProductMovementEntry(movementEntry);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à modifier le Mouvement d'entée");
        }
        return "redirect:/module/pharmacy/operations/movement/editFlux.form?type=" +type+ "&movementId=" + movementId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/delete.form", method = RequestMethod.GET)
    public String deleteOperation(HttpServletRequest request,
                                  @RequestParam(value = "movementId") Integer movementId,
                                  @RequestParam(value = "type") String type){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        if (type.equals("out")){
            ProductMovementOut movementOut = service().getOneProductMovementOutById(movementId);
            for (ProductAttributeFlux flux : productAttributeFluxService().getAllProductAttributeFluxByOperation(movementOut, false)){
                productAttributeFluxService().removeProductAttributeFlux(flux);
            }
            service().removeProductMovementOut(movementOut);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le movement de sortie a été supprimée avec succès !");
        }
        else {
            ProductMovementEntry movementEntry = service().getOneProductMovementEntryById(movementId);
            for (ProductAttributeFlux flux : productAttributeFluxService().getAllProductAttributeFluxByOperation(movementEntry, false)){
                productAttributeFluxService().removeProductAttributeFlux(flux);
            }
            service().removeProductMovementEntry(movementEntry);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le movement de sortie a été supprimée avec succès !");

        }
        return "redirect:/module/pharmacy/operations/reception/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "movementId") Integer movementId,
                             @RequestParam(value = "fluxId") Integer fluxId,
                             @RequestParam(value = "type") String type){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductAttributeFlux flux = productAttributeFluxService().getOneProductAttributeFluxById(fluxId);
        if (flux != null) {
            productAttributeFluxService().removeProductAttributeFlux(flux);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");
        }
        return "redirect:/module/pharmacy/operations/movement/editFlux.form?type="+ type +"&movementId=" + movementId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "movementId") Integer movementId,
                           @RequestParam(value = "type") String type){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        if (type.equals("out")){
            ProductMovementOut movementOut = service().getOneProductMovementOutById(movementId);
            if (OperationUtils.validateOperation(movementOut)) {
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                        "continuer à modifier le movement de sortie !");
            }
        }
        else {
            ProductMovementEntry movementEntry = service().getOneProductMovementEntryById(movementId);
            if (OperationUtils.validateOperation(movementEntry)) {
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                        "continuer à modifier le movement d'entrée !");
            }
        }
        return "redirect:/module/pharmacy/operations/movement/list.form";
    }

    public Map<String, String> getEntryTypeLabels(){
        Map<String, String> entryTypeMap = new HashMap<String, String>();
        for (StockEntryType value : StockEntryType.values()){
            String label = "";
            switch (value){
                case DONATION: label = "Don";
                    break;
                case TRANSFER_IN: label = "Transfert Entrant";
                    break;
//                case SITE_PRODUCT_BACK: label = "Retour de produit du site";
//                    break;
                case POSITIVE_INVENTORY_ADJUSTMENT: label = "Ajustement inventaire positif";
                    break;
            }
            entryTypeMap.put(value.name(), label);
        }
        return entryTypeMap;
    }
    public Map<String, String> getOutTypeLabels(){
        Map<String, String> outTypeMap = new HashMap<String, String>();
        for (StockOutType value : StockOutType.values()){
            String label = "";
            switch (value){
                case THIEF: label = "Vol(s)";
                    break;
                case DESTROYED: label = "Endommagés";
                    break;
                case EXPIRED_PRODUCT: label = "Produits Perimés";
                    break;
                case SPOILED_PRODUCT: label = "Produits Avariés";
                    break;
                case TRANSFER_OUT: label = "Transfert Sortant";
                    break;
                case NEGATIVE_INVENTORY_ADJUSTMENT: label = "Ajustement inventaire négatif";
                    break;
                case OTHER_LOST: label = "Autres pertes";
                    break;
            }
            outTypeMap.put(value.name(), label);
        }
        return outTypeMap;
    }
}
