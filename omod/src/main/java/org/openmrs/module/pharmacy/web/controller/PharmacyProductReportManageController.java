package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.ReportType;
import org.openmrs.module.pharmacy.forms.ProductReportForm;
import org.openmrs.module.pharmacy.forms.ReportAttributeFluxForm;
import org.openmrs.module.pharmacy.models.ProductReportLineDTO;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.validators.ProductReportFormValidation;
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
public class PharmacyProductReportManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
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

    private ProductInventoryService inventoryService(){
        return Context.getService(ProductInventoryService.class);
    }

    private ProductAttributeService attributeService(){
        return Context.getService(ProductAttributeService.class);
    }

    @ModelAttribute("title")
    public String getTile() {
        return "Rapport d'activité";
    }

    @ModelAttribute("isDirectClient")
    public Boolean isDirectClient() {
        return OperationUtils.isDirectClient(OperationUtils.getUserLocation());
    }

    @ModelAttribute("canDistribute")
    public Boolean canDistribute() {
        return OperationUtils.canDistribute(OperationUtils.getUserLocation());
    }

    @RequestMapping(value = "/module/pharmacy/reports/list.form", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("reports", reportService().getAllProductReports(
                    OperationUtils.getUserLocation(),
                    false,
                    OperationUtils.getCurrentMonthRange().getStartDate(),
                    OperationUtils.getCurrentMonthRange().getEndDate()
            ));
            modelMap.addAttribute("lastMonthReports", reportService().getAllProductReports(
                    OperationUtils.getUserLocation(),
                    false,
                    OperationUtils.getMonthRange(OperationUtils.getLastMonthOfDate(new Date())).getStartDate(),
                    OperationUtils.getMonthRange(OperationUtils.getLastMonthOfDate(new Date())).getEndDate()
            ));
            modelMap.addAttribute("subTitle", "Liste des Rapports d'activité");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/reports/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       ProductReportForm productReportForm) {
        if (Context.isAuthenticated()) {
            if (id != 0) {
                ProductReport productReport = reportService().getOneProductReportById(id);
                if (productReport != null) {
                    if (!productReport.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        return "redirect:/module/pharmacy/reports/editFlux.form?reportId=" +
                                productReport.getProductOperationId();
                    }
                    productReportForm.setProductReport(productReport);
                    modelMap.addAttribute("program", productReport.getProductProgram());
                }
            } else {
                ProductProgram program = programService().getOneProductProgramById(programId);
                if (program == null) {
                    HttpSession session = request.getSession();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez sélectionner un programme !");
                    return "redirect:/module/pharmacy/reports/list.form";
                }
                productReportForm = new ProductReportForm();
                productReportForm.setProductProgramId(programId);
                productReportForm.setIncidence(Incidence.NONE);
                productReportForm.setProductProgramId(program.getProductProgramId());
                productReportForm.setLocationId(OperationUtils.getUserLocation().getLocationId());
                productReportForm.setReportType(ReportType.NOT_CLIENT_REPORT);
                if (isDirectClient()) {
                    productReportForm.setReportType(ReportType.CLIENT_REPORT);
                }
                modelMap.addAttribute("program", program);
            }

            modelMap.addAttribute("productReport", reportService().getOneProductReportById(id));
            modelMap.addAttribute("subTitle", "Raport mensuel <i class=\"fa fa-play\"></i> Saisie entête");

            modelMap.addAttribute("productReportForm", productReportForm);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/reports/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                       ProductReportForm productReportForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductReportFormValidation().validate(productReportForm, result);

            if (!result.hasErrors()) {
//                boolean idExist = (reportHeaderForm.getProductOperationId() != null);

                ProductReport report = productReportForm.getProductReport();
                ProductInventory lastInventory = inventoryService().getLastProductInventory(report.getLocation(), report.getProductProgram());
                if (lastInventory.getOperationNumber().contains(report.getReportPeriod())) {
                    reportService().saveProductReport(report);
                    if (action.equals("addLine")) {
                        if (report.getProductAttributeFluxes().size() == 0) {
                            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                        } else {
                            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                        }
                        return "redirect:/module/pharmacy/reports/editFlux.form?reportId=" +
                                report.getProductOperationId();
                    } else {
                        return "redirect:/module/pharmacy/reports/list.form";
                    }
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez effectuer un inventaire de \"" + report.getReportPeriod() + "\" avant le rapport de cette période !");
                    modelMap.addAttribute("productReport", report);
                }
            }
            modelMap.addAttribute("program", programService().getOneProductProgramById(productReportForm.getProductProgramId()));
            modelMap.addAttribute("subTitle", "Rapport mensuel <i class=\"fa fa-play\"></i> Saisie entête");

            modelMap.addAttribute("productReportForm", productReportForm);
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/reports/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                           @RequestParam(value = "reportId") Integer reportId,
                         ReportAttributeFluxForm reportAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductReport productReport = reportService().getOneProductReportById(reportId);
            reportAttributeFluxForm.setProductOperationId(productReport.getProductOperationId());
            reportAttributeFluxForm.setInventory(inventoryService().getLastProductInventory(productReport.getLocation(), productReport.getProductProgram()));
            List<ProductReportLineDTO> reportLineDTOS = reportAttributeFluxForm.createProductReportOtherFluxMap();
            for (ProductReportLineDTO dto : reportLineDTOS) {
                if (!dto.getAsserted()) {
                    modelMap.addAttribute("invalidReport", true);
                    break;
                }
            }
            modelMap.addAttribute("productReport", productReport);
            modelMap.addAttribute("stockMax", OperationUtils.getUserLocationStockMax());
            modelMap.addAttribute("reportData", reportLineDTOS);
            modelMap.addAttribute("subTitle", "Rapport mensuel <i class=\"fa fa-play\"></i> Visualisation");

//            if (fluxId != 0) {
//                ProductAttributeFlux productAttributeFlux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
//                if (productAttributeFlux != null) {
//                    reportAttributeFluxForm.setProductAttributeFlux(productAttributeFlux, productReport);
//                } else {
//                    reportAttributeFluxForm = new ReportAttributeFluxForm();
//                    reportAttributeFluxForm.setProductOperationId(reportId);
//                }
//            } else {
//                reportAttributeFluxForm = new ReportAttributeFluxForm();
//                reportAttributeFluxForm.setProductOperationId(productReport.getProductOperationId());
//            }


//            selectProduct(modelMap, selectedProductId, reportAttributeFluxForm, productReport);
            //modelMappingForView(modelMap, reportAttributeFluxForm, productReport);
        }
        return null;
    }

//    @RequestMapping(value = "/module/pharmacy/reports/editFlux.form", method = RequestMethod.POST)
//    public String saveFlux(ModelMap modelMap,
//                           HttpServletRequest request,
//                           @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId,
//                           ReportAttributeFluxForm reportAttributeFluxForm,
//                           BindingResult result) {
//        if (Context.isAuthenticated()) {
//            HttpSession session = request.getSession();
//
//            new ProductReportAttributeFluxFormValidation().validate(reportAttributeFluxForm, result);
//            ProductReport productReport = reportService().getOneProductReportById(reportAttributeFluxForm.getProductOperationId());
//
//            if (!result.hasErrors()) {
//                ProductAttribute productAttribute = attributeService().saveProductAttribute(reportAttributeFluxForm.getProductAttribute());
//                if (productAttribute != null) {
//                    ProductAttributeFlux flux = reportAttributeFluxForm.getProductAttributeFlux(productAttribute);
//                    flux.setStatus(productReport.getOperationStatus());
//
////                    if (attributeFluxService().saveProductAttributeFlux(flux) != null) {
////                        if (productReport.getIncidence().equals(Incidence.POSITIVE)) {
////                            ProductAttributeOtherFlux otherFlux = reportAttributeFluxForm.get();
////                            attributeFluxService().saveProductAttributeOtherFlux(otherFlux);
////                        }
////                    }
//
//                    if (reportAttributeFluxForm.getProductAttributeFluxId() == null) {
//                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit insérés avec succès !");
//                    } else {
//                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
//                    }
//
//                    return "redirect:/module/pharmacy/reports/editFlux.form?reportId="
//                            + reportAttributeFluxForm.getProductOperationId();
//                }
//            }
////            selectProduct(modelMap, selectedProductId, reportAttributeFluxForm, productReport);
//            //modelMappingForView(modelMap, reportAttributeFluxForm, productReport);
//        }
//
//        return null;
//    }

//    private void selectProduct(ModelMap modelMap, @RequestParam(value = "selectedProductId", defaultValue = "0", required = false) Integer selectedProductId, ReportAttributeFluxForm reportAttributeFluxForm, ProductReport productReport) {
//        ProductReportReturnDTO reportReturnDTO = null;
//        if (selectedProductId != 0) {
//             reportReturnDTO =  reportService()
//                    .getOneProductReportReturnDTO(
//                            productReport,
//                            attributeFluxService().getOneProductAttributeFluxById(selectedProductId).getProductAttribute()
//                    );
//
//        } else {
//            if (reportAttributeFluxForm.getProductAttributeFluxId() != null) {
//                reportReturnDTO =  reportService()
//                        .getOneProductReportReturnDTO(
//                                productReport,
//                                reportAttributeFluxForm.getProductAttribute()
//                        );
//            }
//        }
//        if (reportReturnDTO != null) {
//            modelMap.addAttribute("stock", reportReturnDTO);
//            if (selectedProductId != 0) {
//                reportAttributeFluxForm.setSelectedProductFluxId(selectedProductId);
//            }
//            reportAttributeFluxForm.setProductId(reportReturnDTO.getProductId());
//            reportAttributeFluxForm.setBatchNumber(reportReturnDTO.getBatchNumber());
//            reportAttributeFluxForm.setExpiryDate(reportReturnDTO.getExpiryDate());
//            if (reportReturnDTO.getQuantityInStock() == 0) {
//                modelMap.addAttribute("productMessage", "Ce produit n'est plus en stock");
//            }
//        }
//
//
//        modelMappingForView(modelMap, reportAttributeFluxForm, productReport);
//    }
//
//    private void modelMappingForView(ModelMap modelMap, ReportAttributeFluxForm reportAttributeFluxForm, ProductReport productReport) {
//        if (productReport.getIncidence().equals(Incidence.POSITIVE)) {
//            List<ProductReportFluxDTO> productAttributeFluxes = reportService().getProductReportFluxDTOs(productReport);
////        if (productAttributeFluxes.size() != 0) {
////            Collections.sort(productAttributeFluxes, Collections.<ProductReportFluxDTO>reverseOrder());
////        }
//            if (reportAttributeFluxForm.getProductAttributeFluxId() != null) {
//                for (ProductReportFluxDTO fluxDTO : productAttributeFluxes) {
//                    if (fluxDTO.getProductAttributeFluxId().equals(reportAttributeFluxForm.getProductAttributeFluxId())) {
//                        productAttributeFluxes.remove(fluxDTO);
//                        break;
//                    }
//                }
//            }
//            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
//            modelMap.addAttribute("products", programService().getOneProductProgramById(productReport.getProductProgram().getProductProgramId()).getProducts());
//        } else if (productReport.getIncidence().equals(Incidence.NEGATIVE)){
//            ProductReport report = (ProductReport) service().getOneProductOperationByOperationNumber(productReport.getOperationNumber(), Incidence.POSITIVE);
//            List<ProductReportReturnDTO>  productAttributeFluxes = reportService().getProductReportReturnDTOs(productReport);
//            Set<ProductAttributeFlux> fluxes =  new HashSet<ProductAttributeFlux>();
//
//            for (ProductAttributeFlux flux : report.getProductAttributeFluxes()) {
//                boolean notFound = true;
//               // System.out.println("------------------------- flux BatchNumber : " + flux.getProductAttribute().getBatchNumber());
//                for (ProductReportReturnDTO returnDTO : productAttributeFluxes) {
//                    //System.out.println("------------------------- returnDTO batchNumber : " + returnDTO.getBatchNumber());
//                    if (flux.getProductAttribute().getBatchNumber().equals(returnDTO.getBatchNumber())) {
//                        notFound = false;
//                        break;
//                    }
//                }
//                if (notFound) {
//                    fluxes.add(flux);
//                }
//            }
//            modelMap.addAttribute("productAttributeFluxes", productAttributeFluxes);
//            modelMap.addAttribute("report", report);
//            modelMap.addAttribute("products", fluxes);
//        }
//
//        modelMap.addAttribute("reportAttributeFluxForm", reportAttributeFluxForm);
//        modelMap.addAttribute("productReport", productReport);
//        String title = productReport.getIncidence().equals(Incidence.NEGATIVE) ? "Réception - Retour de Produits" : "Réception";
//
//        if (!productReport.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
//            if (productReport.getOperationStatus().equals(OperationStatus.VALIDATED)) {
//                modelMap.addAttribute("subTitle", title + " <i class=\"fa fa-play\"></i> APPROUVEE");
//            }
//            else if (productReport.getOperationStatus().equals(OperationStatus.AWAITING_VALIDATION)) {
//                modelMap.addAttribute("subTitle", title + " <i class=\"fa fa-play\"></i> EN ATTENTE DE VALIDATION");
//            }
//        } else {
//            modelMap.addAttribute("subTitle", title + " <i class=\"fa fa-play\"></i> Ajout de produits");
//        }
//    }

    @RequestMapping(value = "/module/pharmacy/reports/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "reportId") Integer reportId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReport report = reportService().getOneProductReportById(reportId);
        report.setOperationStatus(OperationStatus.AWAITING_VALIDATION);
        reportService().saveProductReport(report);
        attributeService().purgeUnusedAttributes();
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le rapport a été enregistré avec " +
                "succès et est en attente de validation !");
        return "redirect:/module/pharmacy/reports/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/reports/incomplete.form", method = RequestMethod.GET)
    public String incomplete(HttpServletRequest request,
                             @RequestParam(value = "reportId") Integer reportId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReport report = reportService().getOneProductReportById(reportId);
        report.setOperationStatus(OperationStatus.NOT_COMPLETED);
        reportService().saveProductReport(report);
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez " +
                "continuer à modifier le rapport !");
        return "redirect:/module/pharmacy/reports/editFlux.form?reportId=" + reportId;
    }

    @RequestMapping(value = "/module/pharmacy/reports/delete.form", method = RequestMethod.GET)
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
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La réception a été supprimée avec succès !");
        return "redirect:/module/pharmacy/reports/list.form";
    }

//    @RequestMapping(value = "/module/pharmacy/reports/deleteFlux.form", method = RequestMethod.GET)
//    public String deleteFlux(HttpServletRequest request,
//                             @RequestParam(value = "reportId") Integer reportId,
//                             @RequestParam(value = "fluxId") Integer fluxId){
//        if (!Context.isAuthenticated())
//            return null;
//        HttpSession session = request.getSession();
//        ProductAttributeFlux flux = attributeFluxService().getOneProductAttributeFluxById(fluxId);
//        if (flux != null) {
//            attributeFluxService().removeProductAttributeFlux(flux);
//            ProductAttributeOtherFlux otherFlux = attributeFluxService()
//                    .getOneProductAttributeOtherFluxByAttributeAndOperation(flux.getProductAttribute(), flux.getProductOperation(), OperationUtils.getUserLocation());
//            if (otherFlux != null) {
//                attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
//            }
//            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");
//        }
//        return "redirect:/module/pharmacy/reports/editFlux.form?reportId=" + reportId;
//    }

    @RequestMapping(value = "/module/pharmacy/reports/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "reportId") Integer reportId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReport report = reportService().getOneProductReportById(reportId);
        if (OperationUtils.validateOperation(report)) {
            String message;
            if (report.getIncidence().equals(Incidence.POSITIVE)) {
                message = "La réception été validée avec succèss !";
            } else {
                message = "Le retour de réception été validé avec succèss !";
            }
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message);
            return "redirect:/module/pharmacy/reports/list.form";
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/reports/submit.form", method = RequestMethod.GET)
    public String submit(HttpServletRequest request,
                           @RequestParam(value = "reportId") Integer reportId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReport report = reportService().getOneProductReportById(reportId);
        if (report != null) {
            String message;
            if (report.getOperationStatus().equals(OperationStatus.SUBMITTED)) {
                message = "Le rapport a été déjà soumis !";
            } else {
                if (!report.getOperationStatus().equals(OperationStatus.VALIDATED)) {
                    message = "Le rapport a été déjà soumis !";
                } else {
                    report.setOperationStatus(OperationStatus.SUBMITTED);
                    reportService().saveProductReport(report);
                    message = "Le rapport a été soumis au fournisseur veuillez patienter pour que le rapport soit traité !";
                }
            }
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message);
            return "redirect:/module/pharmacy/reports/list.form";
        }
        return null;
    }
}
