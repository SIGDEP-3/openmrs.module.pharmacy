package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.LocationAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.forms.InventoryAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ProductInventoryForm;
import org.openmrs.module.pharmacy.models.ProductInventoryFluxDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.validators.ProductInventoryAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.validators.ProductInventoryFormValidation;
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
import java.util.Collections;
import java.util.List;

@Controller
public class PharmacyProductDistributionManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    private ProductAttributeStockService stockService() {
        return Context.getService(ProductAttributeStockService.class);
    }

    private ProductReportService reportService() {
        return Context.getService(ProductReportService.class);
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
        return "Distribution de produits";
    }

    @ModelAttribute("isDirectClient")
    public Boolean isDirectClient() {
        return OperationUtils.isDirectClient(OperationUtils.getUserLocation());
    }

    @ModelAttribute("canDistribute")
    public Boolean canDistribute() {
        return OperationUtils.canDistribute(OperationUtils.getUserLocation());
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("reports", reportService().getAllProductDistributionReports(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("submittedReports", reportService().getAllSubmittedChildProductReports(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("childrenLocation", OperationUtils.getUserLocation().getChildLocations());
            modelMap.addAttribute("subTitle", "Liste des Distributions");
        }
    }
//
//    @RequestMapping(value = "/module/pharmacy/operations/distribution/edit.form", method = RequestMethod.GET)
//    public String edit(ModelMap modelMap,
//                       HttpServletRequest request,
//                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
//                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
//                       ProductInventoryForm productInventoryForm) {
//        if (Context.isAuthenticated()) {
//            if (id != 0) {
//                ProductInventory productInventory = reportService().getOneProductInventoryById(id);
//                if (productInventory != null) {
//                    if (!productInventory.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
//                        return "redirect:/module/pharmacy/operations/distribution/editFlux.form?distributionId=" +
//                            productInventory.getProductOperationId();
//                    }
//                    productInventoryForm.setProductInventory(productInventory);
//                    modelMap.addAttribute("program", productInventory.getProductProgram());
//                }
//            } else {
//                ProductProgram program = programService().getOneProductProgramById(programId);
//                if (program == null) {
//                    HttpSession session = request.getSession();
//                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez sélectionner un programme !");
//                    return "redirect:/module/pharmacy/operations/distribution/list.form";
//                }
//                productInventoryForm = new ProductInventoryForm();
//                productInventoryForm.setProductProgramId(program.getProductProgramId());
//                productInventoryForm.setLocationId(OperationUtils.getUserLocation().getLocationId());
//
//                modelMap.addAttribute("program", program);
//            }
//
//            modelMap.addAttribute("latestInventory", latestInventory(productInventoryForm.getProductInventory()));
//            modelMap.addAttribute("productInventoryForm", productInventoryForm);
//            modelMap.addAttribute("productInventory", reportService().getOneProductInventoryById(id));
//            modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
//            modelMap.addAttribute("subTitle", "Inventaire - Entête");
//        }
//        return null;
//    }
//
//    @RequestMapping(value = "/module/pharmacy/operations/distribution/edit.form", method = RequestMethod.POST)
//    public String save(ModelMap modelMap,
//                       HttpServletRequest request,
//                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
//                       ProductInventoryForm productInventoryForm,
//                       BindingResult result) {
//        if (Context.isAuthenticated()) {
//            HttpSession session = request.getSession();
//
//            new ProductInventoryFormValidation().validate(productInventoryForm, result);
//
//            if (!result.hasErrors()) {
////                boolean idExist = (distributionHeaderForm.getProductOperationId() != null);
//                ProductInventory distribution = reportService().saveProductInventory(productInventoryForm.getProductInventory());
//                distribution.setIncidence(Incidence.EQUAL);
//                if (action.equals("addLine")) {
//                    if (distribution.getProductAttributeFluxes().size() == 0) {
//                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
//                    } else {
//                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
//                    }
//                    return "redirect:/module/pharmacy/operations/distribution/editFlux.form?distributionId=" +
//                            distribution.getProductOperationId();
//                } else {
//                    return "redirect:/module/pharmacy/operations/distribution/list.form";
//                }
//            }
//            modelMap.addAttribute("distributionHeaderForm", productInventoryForm);
//            modelMap.addAttribute("latestInventory", latestInventory(productInventoryForm.getProductInventory()));
//            modelMap.addAttribute("program", programService().getOneProductProgramById(productInventoryForm.getProductProgramId()));
//            modelMap.addAttribute("suppliers", supplierService().getAllProductSuppliers());
//            modelMap.addAttribute("subTitle", "Inventaire - entête");
//        }
//
//        return null;
//    }
//
//    @RequestMapping(value = "/module/pharmacy/operations/distribution/editFlux.form", method = RequestMethod.GET)
//    public String editFlux(ModelMap modelMap,
//                         @RequestParam(value = "distributionId") Integer distributionId,
//                         @RequestParam(value = "fluxId", defaultValue = "0", required = false) Integer fluxId,
//                         InventoryAttributeFluxForm distributionAttributeFluxForm) {
//        if (Context.isAuthenticated()) {
//            ProductInventory productInventory = reportService().getOneProductInventoryById(distributionId);
//            if (fluxId != 0) {
//                ProductAttributeFlux productAttributeFlux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
//                if (productAttributeFlux != null) {
//                    distributionAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productInventory);
//                } else {
//                    distributionAttributeFluxForm = new InventoryAttributeFluxForm();
//                    distributionAttributeFluxForm.setProductOperationId(distributionId);
//                }
//            } else {
//                distributionAttributeFluxForm = new InventoryAttributeFluxForm();
//                distributionAttributeFluxForm.setProductOperationId(productInventory.getProductOperationId());
//            }
//            modelMappingForView(modelMap, distributionAttributeFluxForm, productInventory);
//        }
//        return null;
//    }
//
//    @RequestMapping(value = "/module/pharmacy/operations/distribution/editFlux.form", method = RequestMethod.POST)
//    public String saveFlux(ModelMap modelMap,
//                           HttpServletRequest request,
//                           InventoryAttributeFluxForm distributionAttributeFluxForm,
//                           BindingResult result) {
//        if (Context.isAuthenticated()) {
//            HttpSession session = request.getSession();
//
//            new ProductInventoryAttributeFluxFormValidation().validate(distributionAttributeFluxForm, result);
//            ProductInventory productInventory = reportService().getOneProductInventoryById(distributionAttributeFluxForm.getProductOperationId());
//
//            if (!result.hasErrors()) {
//                ProductAttribute productAttribute = attributeService().saveProductAttribute(distributionAttributeFluxForm.getProductAttribute());
//                if (productAttribute != null) {
//                    ProductAttributeFlux productAttributeFlux = distributionAttributeFluxForm.getProductAttributeFlux(productAttribute);
//                    productAttributeFlux.setStatus(productInventory.getOperationStatus());
//                    attributeFluxService().saveProductAttributeFlux(productAttributeFlux);
//
//                    if (distributionAttributeFluxForm.getProductAttributeFluxId() == null) {
//                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
//                    } else {
//                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
//                    }
//
//                    return "redirect:/module/pharmacy/operations/distribution/editFlux.form?distributionId="
//                            + distributionAttributeFluxForm.getProductOperationId();
//                }
//            }
//
//            modelMappingForView(modelMap, distributionAttributeFluxForm, productInventory);
//        }
//
//        return null;
//    }
//
//    private void modelMappingForView(ModelMap modelMap, InventoryAttributeFluxForm distributionAttributeFluxForm, ProductInventory productInventory) {
//        modelMap.addAttribute("latestInventory", getLatestInventory(productInventory));
//        modelMap.addAttribute("distributionAttributeFluxForm", distributionAttributeFluxForm);
//        modelMap.addAttribute("productInventory", productInventory);
//        modelMap.addAttribute("products", programService().getOneProductProgramById(productInventory.getProductProgram().getProductProgramId()).getProducts());
//
//        if (productInventory.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
//            List<ProductInventoryFluxDTO> productAttributeFluxes = reportService().getProductInventoryFluxDTOs(productInventory);
//            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
//            modelMap.addAttribute("subTitle", "Inventaire <i class=\"fa fa-play\"></i> EN ATTENTE DE VALIDATION");
//        } else if (productInventory.getOperationStatus().equals(OperationStatus.VALIDATED)) {
//            List<ProductInventoryFluxDTO> productAttributeFluxes = reportService().getProductInventoryFluxValidatedDTO(productInventory);
//            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
//            modelMap.addAttribute("subTitle", "Inventaire <i class=\"fa fa-play\"></i> APPROUVEE");
//        } else if (productInventory.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)){
//            if (getLatestInventory(productInventory) != null) {
//                List<ProductAttributeStock> stocks = stockService().getAllProductAttributeStocks(OperationUtils.getUserLocation(), false);
//                for (ProductAttributeStock stock : stocks) {
//                    if (productInventory.getProductProgram().getProducts().contains(stock.getProductAttribute().getProduct())) {
//                        if (!stock.getQuantityInStock().equals(0)) {
//                            ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxByAttributeAndOperation(
//                                    stock.getProductAttribute(), productInventory
//                            );
//
//                            if (flux == null) {
//                                flux = new ProductAttributeFlux();
//                                flux.setQuantity(0);
//                                flux.setOperationDate(productInventory.getOperationDate());
//                                flux.setLocation(OperationUtils.getUserLocation());
//                                flux.setProductAttribute(stock.getProductAttribute());
//                                flux.setProductOperation(productInventory);
//                                flux.setStatus(productInventory.getOperationStatus());
//                                attributeFluxService().saveProductAttributeFlux(flux);
//                            }
//                        }
//                    }
//                }
//            }
//            List<ProductAttributeFlux> productAttributeFluxes = attributeFluxService().getAllProductAttributeFluxByOperation(productInventory, false);
//            if (productAttributeFluxes.size() != 0) {
//                Collections.sort(productAttributeFluxes, Collections.<ProductAttributeFlux>reverseOrder());
//            }
//            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
//
//            modelMap.addAttribute("subTitle", "Distribution <i class=\"fa fa-play\"></i> ajout de produits");
//        }
//    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "distributionId") Integer distributionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReport distribution = reportService().getOneProductReportById(distributionId);
        distribution.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
        reportService().saveProductReport(distribution);
        attributeService().purgeUnusedAttributes();
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La distributiona été enregistrée avec " +
                "succès et est en attente de traitement !");
        return "redirect:/module/pharmacy/operations/distribution/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/incomplete.form", method = RequestMethod.GET)
    public String incomplete(HttpServletRequest request,
                             @RequestParam(value = "distributionId") Integer distributionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReport report = reportService().getOneProductReportById(distributionId);
        report.setOperationStatus(OperationStatus.NOT_COMPLETED);
        reportService().saveProductReport(report);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                "continuer à modifier la distribution !");
        return "redirect:/module/pharmacy/operations/distribution/editFlux.form?distributionId=" + distributionId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/delete.form", method = RequestMethod.GET)
    public String deleteOperation(HttpServletRequest request,
                                  @RequestParam(value = "id") Integer id){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReport report = reportService().getOneProductReportById(id);
        for (ProductAttributeOtherFlux otherFlux : attributeFluxService().getAllProductAttributeOtherFluxByOperation(report, false)) {
            attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
        }
        for (ProductAttributeFlux flux : attributeFluxService().getAllProductAttributeFluxByOperation(report, false)){
            attributeFluxService().removeProductAttributeFlux(flux);
        }
        reportService().removeProductReport(report);
        attributeService().purgeUnusedAttributes();
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "L'inventaire a été supprimé avec succès !");
        return "redirect:/module/pharmacy/operations/distribution/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "distributionId") Integer distributionId,
                             @RequestParam(value = "fluxId") Integer fluxId){
        if (!Context.isAuthenticated())
            return null;
        ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
        if (flux != null) {
            HttpSession session = request.getSession();
            attributeFluxService().removeProductAttributeFlux(flux);
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");
        }
        return "redirect:/module/pharmacy/operations/distribution/editFlux.form?distributionId=" + distributionId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "distributionId") Integer distributionId){
        if (!Context.isAuthenticated())
            return null;
        ProductOperation operation = service().getOneProductOperationById(distributionId);

        if (operation != null) {
            for (ProductAttributeFlux flux : operation.getProductAttributeFluxes()) {
                ProductAttributeOtherFlux otherFlux = new ProductAttributeOtherFlux();
                otherFlux.setProductOperation(operation);
                otherFlux.setProductAttribute(flux.getProductAttribute());
                otherFlux.setLabel("Gap");
                otherFlux.setLocation(OperationUtils.getUserLocation());
                ProductAttributeStock stock = stockService().getOneProductAttributeStockByAttribute(flux.getProductAttribute(), OperationUtils.getUserLocation(), false);
                if (stock != null) {
                    otherFlux.setQuantity(flux.getQuantity() - stock.getQuantityInStock());
                } else {
                    otherFlux.setQuantity(flux.getQuantity());
                }
                attributeFluxService().saveProductAttributeOtherFlux(otherFlux);
            }

            OperationUtils.emptyStock(OperationUtils.getUserLocation(), operation.getProductProgram());

            if (OperationUtils.validateOperation(operation)) {
                HttpSession session = request.getSession();
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Votre distribution a été validée avec succès !");
                return "redirect:/module/pharmacy/operations/distribution/list.form";
            }
        }
        return null;
    }

}
