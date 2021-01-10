package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.TransferType;
import org.openmrs.module.pharmacy.forms.TransferAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ProductTransferForm;
//import org.openmrs.module.pharmacy.models.ProductTransferFluxDTO;
import org.openmrs.module.pharmacy.models.ProductOutFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.validators.ProductTransferAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.validators.ProductTransferFormValidation;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class PharmacyProductTransferManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }

    private ProductTransferService transferService() {
        return Context.getService(ProductTransferService.class);
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
        return "Transferts de produits";
    }

    @ModelAttribute("isDirectClient")
    public Boolean isDirectClient() {
        return OperationUtils.isDirectClient(OperationUtils.getUserLocation());
    }

    @ModelAttribute("canDistribute")
    public Boolean canDistribute() {
        return OperationUtils.canDistribute(OperationUtils.getUserLocation());
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("transfers", transferService().getAllProductTransfers(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("subTitle", "Liste des Transferts");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       @RequestParam(value = "type", required = false) String type,
                       ProductTransferForm productTransferForm) {
        if (Context.isAuthenticated()) {
            String transferTypeStr = "";
            if (id != 0) {
                ProductTransfer productTransfer = transferService().getOneProductTransferById(id);
                if (productTransfer != null) {
                    if (!productTransfer.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        return "redirect:/module/pharmacy/operations/movement/transfer/editFlux.form?transferId=" +
                                productTransfer.getProductOperationId();
                    }
                    productTransferForm.setProductTransfer(productTransfer);
                    modelMap.addAttribute("program", productTransfer.getProductProgram());
                }
            } else {
                if (type != null) {
                    ProductProgram program = programService().getOneProductProgramById(programId);
                    if (program == null) {
                        HttpSession session = request.getSession();
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez sélectionner un programme !");
                        return "redirect:/module/pharmacy/operations/movement/transfer/list.form";
                    }
                    productTransferForm = new ProductTransferForm();
                    if (type.equals("IN")) {
                        productTransferForm.setIncidence(Incidence.POSITIVE);
                    } else {
                        productTransferForm.setIncidence(Incidence.NEGATIVE);
                        productTransferForm.setOperationNumber(OperationUtils.generateNumber());
                    }
                    productTransferForm.setProductProgramId(program.getProductProgramId());
                    productTransferForm.setLocationId(OperationUtils.getUserLocation().getLocationId());
                    productTransferForm.setTransferType(type.equals("IN") ? TransferType.IN : TransferType.OUT);
                    modelMap.addAttribute("program", program);
                }
            }

            transferTypeStr = productTransferForm.getTransferType().equals(TransferType.IN) ? "Entrant" : "Sortant";
            List<Location> clientLocations = transferService().getAllClientLocation(false);
            clientLocations.remove(OperationUtils.getUserLocation());
            modelMap.addAttribute("productTransferForm", productTransferForm);
            modelMap.addAttribute("type", type);
            modelMap.addAttribute("productTransfer", transferService().getOneProductTransferById(id));
            modelMap.addAttribute("clientLocations", clientLocations);
            modelMap.addAttribute("reasonList", transferReasons());
            modelMap.addAttribute("subTitle", "Saisie de transfert " + transferTypeStr.toUpperCase() + " <i class=\"fa fa-play\"></i> Entête");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                       ProductTransferForm productTransferForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductTransferFormValidation().validate(productTransferForm, result);

            if (!result.hasErrors()) {
                ProductTransfer transfer = transferService().saveProductTransfer(productTransferForm.getProductTransfer());
                if (transfer.getIncidence() == null) {
                    if (transfer.getTransferType().equals(TransferType.IN)) {
                        transfer.setIncidence(Incidence.POSITIVE);
                    } else {
                        transfer.setIncidence(Incidence.NEGATIVE);
                    }
                }
                if (action.equals("addLine")) {
                    if (transfer.getProductAttributeFluxes().size() == 0) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                    }
                    return "redirect:/module/pharmacy/operations/movement/transfer/editFlux.form?transferId=" +
                            transfer.getProductOperationId();
                } else {
                    return "redirect:/module/pharmacy/operations/movement/transfer/list.form";
                }
            }
            String transferTypeStr = productTransferForm.getTransferType().equals(TransferType.IN) ? "Entrant" : "Sortant";
            modelMap.addAttribute("transferHeaderForm", productTransferForm);
//            modelMap.addAttribute("latestTransfer", latestTransfer(productTransferForm.getProductTransfer()));
            modelMap.addAttribute("program", programService().getOneProductProgramById(productTransferForm.getProductProgramId()));
            modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
            modelMap.addAttribute("subTitle", "Transfert "+ transferTypeStr.toUpperCase() + " <i class=\"fa fa-play\"></i> Saisie entête");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                           @RequestParam(value = "transferId") Integer transferId,
                           @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                           @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
                           TransferAttributeFluxForm transferAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductTransfer productTransfer = transferService().getOneProductTransferById(transferId);
            if (fluxId != 0) {
                ProductAttributeFlux productAttributeFlux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
                if (productAttributeFlux != null) {
                    transferAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productTransfer);
                } else {
                    transferAttributeFluxForm = new TransferAttributeFluxForm();
                    transferAttributeFluxForm.setProductOperationId(transferId);
                }
            } else {
                transferAttributeFluxForm = new TransferAttributeFluxForm();
                transferAttributeFluxForm.setProductOperationId(productTransfer.getProductOperationId());
            }
            selectProduct(modelMap, selectedProductId, transferAttributeFluxForm, productTransfer);
            //modelMappingForView(modelMap, transferAttributeFluxForm, productTransfer);
        }
        return null;
    }

    private void selectProduct(ModelMap modelMap, @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId, TransferAttributeFluxForm transferAttributeFluxForm, ProductTransfer productTransfer) {
        if (selectedProductId != 0) {
            ProductAttributeStock stock =  stockService().getOneProductAttributeStockById(selectedProductId);
            modelMap.addAttribute("stock", stock);
            transferAttributeFluxForm.setSelectedProductStockId(selectedProductId);
            transferAttributeFluxForm.setProductId(stock.getProductAttribute().getProduct().getProductId());
            transferAttributeFluxForm.setBatchNumber(stock.getProductAttribute().getBatchNumber());
            transferAttributeFluxForm.setExpiryDate(stock.getProductAttribute().getExpiryDate());
            if (stock.getQuantityInStock() == 0) {
                modelMap.addAttribute("productMessage", "Ce produit est en rupture de stock");
            }
        }

        modelMappingForView(modelMap, transferAttributeFluxForm, productTransfer);
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/editFlux.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           HttpServletRequest request,
                           @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
                           TransferAttributeFluxForm transferAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductTransferAttributeFluxFormValidation().validate(transferAttributeFluxForm, result);
            ProductTransfer productTransfer = transferService().getOneProductTransferById(transferAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {
                ProductAttribute productAttribute = attributeService().saveProductAttribute(transferAttributeFluxForm.getProductAttribute());
                if (productAttribute != null) {
                    ProductAttributeFlux productAttributeFlux = transferAttributeFluxForm.getProductAttributeFlux(productAttribute);
                    productAttributeFlux.setStatus(productTransfer.getOperationStatus());
                    attributeFluxService().saveProductAttributeFlux(productAttributeFlux);

                    if (transferAttributeFluxForm.getProductOperationId() == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit inséré avec succès !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                    }

                    return "redirect:/module/pharmacy/operations/movement/transfer/editFlux.form?transferId="
                            + transferAttributeFluxForm.getProductOperationId();
                }
            }

            selectProduct(modelMap, selectedProductId, transferAttributeFluxForm, productTransfer);
        }

        return null;
    }

    private void modelMappingForView(ModelMap modelMap, TransferAttributeFluxForm transferAttributeFluxForm, ProductTransfer productTransfer) {
        modelMap.addAttribute("transferAttributeFluxForm", transferAttributeFluxForm);
        modelMap.addAttribute("productTransfer", productTransfer);
        if (productTransfer.getTransferType().equals(TransferType.IN)) {
            modelMap.addAttribute("products", programService().getOneProductProgramById(productTransfer.getProductProgram().getProductProgramId()).getProducts());
        } else {
            modelMap.addAttribute("stocks", stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation(), false));
        }
        String transferTypeStr = productTransfer.getTransferType().equals(TransferType.IN) ? "Entrant" : "Sortant";
        if (!productTransfer.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
            if (productTransfer.getOperationStatus().equals(OperationStatus.VALIDATED))
                modelMap.addAttribute("subTitle", "Transfer " + transferTypeStr.toUpperCase() + " - APPROUVEE");
            else if (productTransfer.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
                modelMap.addAttribute("subTitle", "Transfer " + transferTypeStr.toUpperCase() +
                        " - EN ATTENTE DE VALIDATION");
            }
            List<ProductAttributeFlux> productAttributeFluxes = attributeFluxService().getAllProductAttributeFluxByOperation(productTransfer, false);
            if (productAttributeFluxes.size() != 0) {
                Collections.sort(productAttributeFluxes, Collections.<ProductAttributeFlux>reverseOrder());
            }
            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
        } else {
            if (productTransfer.getTransferType().equals(TransferType.IN)) {
                List<ProductAttributeFlux> productAttributeFluxes = attributeFluxService().getAllProductAttributeFluxByOperation(productTransfer, false);
                if (productAttributeFluxes.size() != 0) {
                    Collections.sort(productAttributeFluxes, Collections.<ProductAttributeFlux>reverseOrder());
                }
                modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
            } else {
                List<ProductOutFluxDTO> productAttributeFluxes = service().getProductOutFluxDTOs(productTransfer);
                modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
            }
            modelMap.addAttribute("subTitle", "Transfert " + transferTypeStr.toUpperCase() + " <i class=\"fa fa-play\"></i> ajout de produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "transferId") Integer transferId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductTransfer transfer = transferService().getOneProductTransferById(transferId);
        transfer.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
        transferService().saveProductTransfer(transfer);
        attributeService().purgeUnusedAttributes();
        String transferTypeStr = transfer.getTransferType().equals(TransferType.IN) ? "Entrant" : "Sortant";
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le tranfert " + transferTypeStr.toUpperCase() + " a été enregistré avec " +
                "succès et est en attente de validation !");
        return "redirect:/module/pharmacy/operations/movement/transfer/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/incomplete.form", method = RequestMethod.GET)
    public String incomplete(HttpServletRequest request,
                             @RequestParam(value = "transferId") Integer transferId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductTransfer transfer = transferService().getOneProductTransferById(transferId);
        transfer.setOperationStatus(OperationStatus.NOT_COMPLETED);
        transferService().saveProductTransfer(transfer);
        String transferTypeStr = transfer.getTransferType().equals(TransferType.IN) ? "Entrant" : "Sortant";
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                "continuer à modifier le transfert " + transferTypeStr + " !");
        return "redirect:/module/pharmacy/operations/movement/transfer/editFlux.form?transferId=" + transferId;
    }

//    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/delete.form", method = RequestMethod.GET)
//    public String deleteOperation(HttpServletRequest request,
//                                  @RequestParam(value = "id") Integer id){
//        if (!Context.isAuthenticated())
//            return null;
//        HttpSession session = request.getSession();
//        ProductTransfer transfer = transferService().getOneProductTransferById(id);
//        for (ProductAttributeOtherFlux otherFlux : attributeFluxService().getAllProductAttributeOtherFluxByOperation(transfer, true)) {
//            attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
//        }
//        for (ProductAttributeFlux flux : attributeFluxService().getAllProductAttributeFluxByOperation(transfer, true)){
//            attributeFluxService().removeProductAttributeFlux(flux);
//        }
//        String transferTypeStr = transfer.getTransferType().equals(TransferType.IN) ? "Entrant" : "Sortant";
//        transferService().removeProductTransfer(transfer);
//        attributeService().purgeUnusedAttributes();
//        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Transfert " + transferTypeStr.toUpperCase() +
//                " supprimé avec succès !");
//        return "redirect:/module/pharmacy/operations/movement/transfer/list.form";
//    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "transferId") Integer transferId,
                             @RequestParam(value = "fluxId") Integer fluxId){
        if (!Context.isAuthenticated())
            return null;
        ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
        if (flux != null) {
            HttpSession session = request.getSession();
            attributeFluxService().removeProductAttributeFlux(flux);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");
        }
        return "redirect:/module/pharmacy/operations/movement/transfer/editFlux.form?transferId=" + transferId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "transferId") Integer transferId){
        if (!Context.isAuthenticated())
            return null;
        ProductOperation operation = service().getOneProductOperationById(transferId);

        if (operation != null) {
            if (OperationUtils.validateOperation(operation)) {
                HttpSession session = request.getSession();
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Votre transfert a été validé avec succès !");
                return "redirect:/module/pharmacy/operations/movement/transfer/list.form";
            }
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/movement/transfer/delete.form", method = RequestMethod.GET)
    public String delete(HttpServletRequest request,
                         @RequestParam(value = "id") Integer id) {
        if (id != null) {
            ProductTransfer transfer = transferService().getOneProductTransferById(id);
            if (transfer != null) {
                Set<ProductAttributeFlux> fluxes = transfer.getProductAttributeFluxes();
                for (ProductAttributeFlux flux: fluxes) {
                    attributeFluxService().removeProductAttributeFlux(flux);
                }
                transferService().removeProductTransfer(transfer);
                String transferTypeStr = transfer.getTransferType().equals(TransferType.IN) ? "Entrant" : "Sortant";
                attributeService().purgeUnusedAttributes();
                HttpSession session = request.getSession();
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le transfert " + transferTypeStr +
                        " a été supprimé avec succès");
                return "redirect:/module/pharmacy/operations/movement/transfer/list.form";
            }
        }
        return null;
    }

    private List<String> transferReasons() {
        List<String> reasonList = new ArrayList<java.lang.String>();
        reasonList.add("Appareil en panne");
        reasonList.add("Augmentation de la consommation");
        reasonList.add("Baisse de la consommation");
        reasonList.add("Commande non satisfaite pour le produit durant la période");
        reasonList.add("Consommation atypique");
        reasonList.add("Contraintes liées au conditionnement (1 diluant pour 1 kit)");
        reasonList.add("Echange de produit à peremption proche pour consommation");
        reasonList.add("Erreur sur les unités de comptage");
        reasonList.add("Livraison Commande Normale + Commande urgente");
        reasonList.add("Manque de communication entre clients de la même aire sanitaire");
        reasonList.add("Mauvaise estimation de la CMM");
        reasonList.add("Mauvaise estimation de la quantité à commander");
        reasonList.add("Mauvaise tenue des fiches de stock (erreur dans les données sources)");
        reasonList.add("Non promptitude des RM des ESPC");
        reasonList.add("Non suivi des dates de péremption");
        reasonList.add("Peremption proche");
        reasonList.add("Probleme lié au conditionnement");
        reasonList.add("Produit à faible rotation");
        reasonList.add("Produit en vente ne reponds pas aux normes de gestion des stocks prescrites");
        reasonList.add("Rationnalisation des stock aux sites ou aux services");
        reasonList.add("Retard de livraison par la NPSP");
        reasonList.add("Rupture nationale");
        reasonList.add("Situation dû à un système d'allocation");
        reasonList.add("Situation dû à une livraision après un transfert in");
        reasonList.add("Structure non habilité à commander ce produit");
        reasonList.add("Transfert sans évaluation du MSD");
        reasonList.add("Unité de comptage non adaptée");
        reasonList.add("Utilisation des produits PNLS pour d'autres programmes");
        reasonList.add("Autres");
        return reasonList;
    }
}
