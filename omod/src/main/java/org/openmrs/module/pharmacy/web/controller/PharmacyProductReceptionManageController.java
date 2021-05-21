package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.LocationAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.ReceptionQuantityMode;
import org.openmrs.module.pharmacy.forms.ProductReceptionForm;
import org.openmrs.module.pharmacy.forms.ReceptionAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.TransferAttributeFluxForm;
import org.openmrs.module.pharmacy.models.ProductReceptionFluxDTO;
import org.openmrs.module.pharmacy.models.ProductReceptionReturnDTO;
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

    @ModelAttribute("isDirectClient")
    public Boolean isDirectClient() {
        return OperationUtils.isDirectClient(OperationUtils.getUserLocation());
    }

    @ModelAttribute("canDistribute")
    public Boolean canDistribute() {
        return OperationUtils.canDistribute(OperationUtils.getUserLocation());
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("receptions", receptionService().getProductReceptionListDTOs());
            modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
            modelMap.addAttribute("subTitle", "Liste des Réceptions");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "receptionId", defaultValue = "0", required = false) Integer receptionId,
                       @RequestParam(value = "receptionBackId", defaultValue = "0", required = false) Integer receptionBackId,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       ProductReceptionForm productReceptionForm) {
        if (Context.isAuthenticated()) {
            if (receptionId != 0) {
                ProductReception reception = receptionService().getOneProductReceptionById(receptionId);
                if (reception != null) {
                    ProductReception returnBackProductExisting = (ProductReception) service().getOneProductOperationByOperationNumber(reception.getOperationNumber(), Incidence.NEGATIVE);
                    if (returnBackProductExisting == null) {
                        if (receptionBackId == 0) {
                            productReceptionForm.setIncidence(Incidence.NEGATIVE);
                            productReceptionForm.setProductProgramId(reception.getProductProgram().getProductProgramId());
                            productReceptionForm.setProductSupplierId(reception.getProductSupplier().getProductSupplierId());
                            productReceptionForm.setLocationId(OperationUtils.getUserLocation().getLocationId());
                            productReceptionForm.setReceptionQuantityMode(reception.getReceptionQuantityMode());
                            productReceptionForm.setOperationNumber(reception.getOperationNumber());
                            productReceptionForm.setOperationStatus(OperationStatus.NOT_COMPLETED);

                        } else {
                            ProductReception receptionBack = receptionService().getOneProductReceptionById(receptionBackId);
                            productReceptionForm.setProductReception(receptionBack);
                        }
                        modelMap.addAttribute("reception", reception);
                    } else {
                        return "redirect:/module/pharmacy/operations/reception/editFlux.form?receptionId=" +
                                returnBackProductExisting.getProductOperationId();
                    }
                    modelMap.addAttribute("subTitle", "Réception - Retour de Produits <i class=\"fa fa-play\"></i> Saisie entête");
                }

            } else {
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
                    productReceptionForm.setIncidence(Incidence.POSITIVE);
                    productReceptionForm.setProductProgramId(program.getProductProgramId());
                    productReceptionForm.setLocationId(OperationUtils.getUserLocation().getLocationId());

                    modelMap.addAttribute("program", program);
                }
                modelMap.addAttribute("productReception", receptionService().getOneProductReceptionById(id));
                modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
                modelMap.addAttribute("subTitle", "Réception <i class=\"fa fa-play\"></i> Saisie entête");
            }

            modelMap.addAttribute("productReceptionForm", productReceptionForm);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                       @RequestParam(value = "receptionId", defaultValue = "0", required = false) Integer receptionId,
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
            if (receptionId != 0) {
                modelMap.addAttribute("reception", receptionService().getOneProductReceptionById(receptionId));
                modelMap.addAttribute("subTitle", "Réception - Retour de Produits <i class=\"fa fa-play\"></i> Saisie entête");
            } else {
                modelMap.addAttribute("program", programService().getOneProductProgramById(productReceptionForm.getProductProgramId()));
                modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
                modelMap.addAttribute("subTitle", "Réception <i class=\"fa fa-play\"></i> Saisie entête");
            }
            modelMap.addAttribute("receptionHeaderForm", productReceptionForm);
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                           @RequestParam(value = "receptionId") Integer receptionId,
                           @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                           @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
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

            selectProduct(modelMap, selectedProductId, receptionAttributeFluxForm, productReception);
            //modelMappingForView(modelMap, receptionAttributeFluxForm, productReception);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/reception/editFlux.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           HttpServletRequest request,
                           @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                           ReceptionAttributeFluxForm receptionAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductReceptionAttributeFluxFormValidation().validate(receptionAttributeFluxForm, result);
            ProductReception productReception = receptionService().getOneProductReceptionById(receptionAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = attributeService().saveProductAttribute(receptionAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux flux = receptionAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    if (productReception.getReceptionQuantityMode().equals(ReceptionQuantityMode.WHOLESALE)) {
                        double fluxQuantity = flux.getQuantity() * productAttribute.getProduct().getUnitConversion();
                        flux.setQuantity((int) fluxQuantity);
                    }
                    flux.setStatus(productReception.getOperationStatus());

                    if (attributeFluxService().saveProductAttributeFlux(flux) != null) {
                        if (productReception.getIncidence().equals(Incidence.POSITIVE)) {
                            ProductAttributeOtherFlux otherFlux = receptionAttributeFluxForm.getProductAttributeOtherFlux();
                            if (productReception.getReceptionQuantityMode().equals(ReceptionQuantityMode.WHOLESALE)) {
                                double otherFluxQuantity = otherFlux.getQuantity() * productAttribute.getProduct().getUnitConversion();
                                otherFlux.setQuantity(otherFluxQuantity);
                            }
                            attributeFluxService().saveProductAttributeOtherFlux(otherFlux);
                        }
                    }

                    if (receptionAttributeFluxForm.getProductAttributeFluxId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/reception/editFlux.form?receptionId="
                            + receptionAttributeFluxForm.getProductOperationId();
                }
            }
            selectProduct(modelMap, selectedProductId, receptionAttributeFluxForm, productReception);
            //modelMappingForView(modelMap, receptionAttributeFluxForm, productReception);
        }

        return null;
    }

    private void selectProduct(ModelMap modelMap, @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId, ReceptionAttributeFluxForm receptionAttributeFluxForm, ProductReception productReception) {
        ProductReceptionReturnDTO receptionReturnDTO = null;
        if (selectedProductId != 0) {
             receptionReturnDTO =  receptionService()
                    .getOneProductReceptionReturnDTO(
                            productReception,
                            attributeFluxService().getOneProductAttributeFluxById(selectedProductId).getProductAttribute()
                    );

        } else {
            if (receptionAttributeFluxForm.getProductAttributeFluxId() != null) {
                receptionReturnDTO =  receptionService()
                        .getOneProductReceptionReturnDTO(
                                productReception,
                                receptionAttributeFluxForm.getProductAttribute()
                        );
            }
        }
        if (receptionReturnDTO != null) {
            modelMap.addAttribute("stock", receptionReturnDTO);
            if (selectedProductId != 0) {
                receptionAttributeFluxForm.setSelectedProductFluxId(selectedProductId);
            }
            receptionAttributeFluxForm.setProductId(receptionReturnDTO.getProductId());
            receptionAttributeFluxForm.setBatchNumber(receptionReturnDTO.getBatchNumber());
            receptionAttributeFluxForm.setExpiryDate(receptionReturnDTO.getExpiryDate());
            if (receptionReturnDTO.getQuantityInStock() == 0) {
                modelMap.addAttribute("productMessage", "Ce produit n'est plus en stock");
            }
        }


        modelMappingForView(modelMap, receptionAttributeFluxForm, productReception);
    }

    private void modelMappingForView(ModelMap modelMap, ReceptionAttributeFluxForm receptionAttributeFluxForm, ProductReception productReception) {
        modelMap.addAttribute("isFromDistribution", false);
        if (productReception.getIncidence().equals(Incidence.POSITIVE)) {
            if (productReception.getProductAttributeFluxes().size() == 0) {
                ProductOperation operation = service().getOneProductOperationByOperationNumber(productReception.getOperationNumber(), Incidence.NEGATIVE);

                if (operation != null) {
                    ProductReport distribution = Context.getService(ProductReportService.class).getOneProductReportById(operation.getProductOperationId());
                    if (distribution != null && distribution.getReportLocation().equals(OperationUtils.getUserLocation())) {
                        for (ProductAttributeFlux flux : operation.getProductAttributeFluxes()) {
                            ProductAttributeFlux newFlux = new ProductAttributeFlux();
                            newFlux.setStatus(productReception.getOperationStatus());
                            newFlux.setOperationDate(productReception.getOperationDate());
                            newFlux.setLocation(productReception.getLocation());
                            newFlux.setProductAttribute(flux.getProductAttribute());
                            newFlux.setQuantity(flux.getQuantity());
                            productReception.addProductAttributeFlux(newFlux);

                            ProductAttributeOtherFlux newOtherFlux = new ProductAttributeOtherFlux();
                            newOtherFlux.setLocation(productReception.getLocation());
                            newOtherFlux.setLabel("Quantitié livrée");
                            newOtherFlux.setProductAttribute(flux.getProductAttribute());
                            newOtherFlux.setQuantity(flux.getQuantity().doubleValue());
                            productReception.addProductAttributeOtherFlux(newOtherFlux);
                        }
                        productReception.setObservation("Livraison depuis la Distribution faite par le fournisseur le " + OperationUtils.formatDate(operation.getOperationDate()));

                        receptionService().saveProductReception(productReception);
                        modelMap.addAttribute("isFromDistribution", true);
                    }
                }

            }
            List<ProductReceptionFluxDTO> productAttributeFluxes = receptionService().getProductReceptionFluxDTOs(productReception);
//            System.out.println("---------------------------------> After getting all lines ");
//            if (receptionAttributeFluxForm.getProductAttributeFluxId() != null) {
//                for (ProductReceptionFluxDTO fluxDTO : productAttributeFluxes) {
//                    if (fluxDTO.getProductAttributeFluxId().equals(receptionAttributeFluxForm.getProductAttributeFluxId())) {
//                        productAttributeFluxes.remove(fluxDTO);
//                        break;
//                    }
//                }
//            }
            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
            modelMap.addAttribute("products", programService().getOneProductProgramById(productReception.getProductProgram().getProductProgramId()).getProducts());
        } else if (productReception.getIncidence().equals(Incidence.NEGATIVE)){
            ProductReception reception = (ProductReception) service().getOneProductOperationByOperationNumber(productReception.getOperationNumber(), Incidence.POSITIVE);
            List<ProductReceptionReturnDTO>  productAttributeFluxes = receptionService().getProductReceptionReturnDTOs(productReception);
            Set<ProductAttributeFlux> fluxes =  new HashSet<ProductAttributeFlux>();

            for (ProductAttributeFlux flux : reception.getProductAttributeFluxes()) {
                boolean notFound = true;
               // System.out.println("------------------------- flux BatchNumber : " + flux.getProductAttribute().getBatchNumber());
                for (ProductReceptionReturnDTO returnDTO : productAttributeFluxes) {
                    //System.out.println("------------------------- returnDTO batchNumber : " + returnDTO.getBatchNumber());
                    if (flux.getProductAttribute().getBatchNumber().equals(returnDTO.getBatchNumber())) {
                        notFound = false;
                        break;
                    }
                }
                if (notFound) {
                    fluxes.add(flux);
                }
            }
            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
            modelMap.addAttribute("reception", reception);
            modelMap.addAttribute("products", fluxes);
        }

        modelMap.addAttribute("receptionAttributeFluxForm", receptionAttributeFluxForm);
        modelMap.addAttribute("productReception", productReception);
        String title = productReception.getIncidence().equals(Incidence.NEGATIVE) ? "Réception - Retour de Produits" : "Réception";

        if (!productReception.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
            if (productReception.getOperationStatus().equals(OperationStatus.VALIDATED)) {
                modelMap.addAttribute("subTitle", title + " <i class=\"fa fa-play\"></i> APPROUVEE");
            }
            else if (productReception.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
                modelMap.addAttribute("subTitle", title + " <i class=\"fa fa-play\"></i> EN ATTENTE DE VALIDATION");
            }
        } else {
            modelMap.addAttribute("subTitle", title + " <i class=\"fa fa-play\"></i> Ajout de produits");
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
            String message;
            if (reception.getIncidence().equals(Incidence.POSITIVE)) {
                message = "La réception été validée avec succèss !";
            } else {
                message = "Le retour de réception été validé avec succèss !";
            }
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message);
            return "redirect:/module/pharmacy/operations/reception/list.form";
        }
        return null;
    }
}
