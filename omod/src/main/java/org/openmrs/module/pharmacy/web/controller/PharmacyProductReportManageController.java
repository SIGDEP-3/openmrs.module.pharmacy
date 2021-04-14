package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.*;
import org.openmrs.module.pharmacy.api.*;
import org.openmrs.module.pharmacy.enumerations.Incidence;
import org.openmrs.module.pharmacy.enumerations.InventoryType;
import org.openmrs.module.pharmacy.enumerations.OperationStatus;
import org.openmrs.module.pharmacy.enumerations.ReportType;
import org.openmrs.module.pharmacy.forms.ProductReportForm;
import org.openmrs.module.pharmacy.forms.ReportAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ReportEntryAttributeFluxForm;
import org.openmrs.module.pharmacy.models.ProductReportLineDTO;
import org.openmrs.module.pharmacy.utils.CSVHelper;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.validators.ProductReportEntryAttributeFluxFormValidation;
import org.openmrs.module.pharmacy.validators.ProductReportFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    private ProductService productService(){
        return Context.getService(ProductService.class);
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

    @ModelAttribute("isPlatformUser")
    private Boolean isPlatformUser() {
        return inventoryService().getAllProductInventories(OperationUtils.getUserLocation(), false).size() > 0;
    }

    @RequestMapping(value = "/module/pharmacy/reports/list.form", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("programs", OperationUtils.getUserLocationPrograms());
            modelMap.addAttribute("reports", reportService().getAllProductReports(
                    OperationUtils.getUserLocation(),
                    false/*,
                    OperationUtils.getCurrentMonthRange().getStartDate(),
                    OperationUtils.getCurrentMonthRange().getEndDate()*/
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

    @RequestMapping(value = "/module/pharmacy/reports/upload.form", method = RequestMethod.POST)
    public String uploadProduct(HttpServletRequest request,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam(value = "distributionId") Integer reportId) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();
            String message = "";
            if (CSVHelper.hasCSVFormat(file)) {
                try {
                    ProductReport report = reportService().getOneProductReportById(reportId);
                    List<ProductAttributeOtherFlux> otherFluxes = CSVHelper.csvReport(file.getInputStream(), report);

                    for (ProductAttributeOtherFlux otherFlux : otherFluxes) {
                        double quantity = 0.;
                        if (otherFlux.getLabel().equals("QR")) {
                            ProductInventory inventory = inventoryService().getLastProductInventoryByDate(report.getLocation(), report.getProductProgram(), report.getOperationDate(), InventoryType.TOTAL);

                            List<ProductReport> treatedProductReports;
                            if (report.getUrgent()) {
                                treatedProductReports = reportService().getPeriodTreatedChildProductReports(
                                        report.getLocation(), inventory, false, report.getOperationDate()
                                );
                            } else {
                                ProductInventory inventoryBeforeLast = inventoryService().getLastProductInventoryByDate(report.getLocation(), report.getProductProgram(), inventory.getOperationDate(), InventoryType.TOTAL);
                                treatedProductReports = reportService().getPeriodTreatedChildProductReports(
                                        report.getLocation(), inventoryBeforeLast, false, inventory.getOperationDate()
                                );
                            }

                            if (treatedProductReports != null) {
                                for (ProductReport productReport : treatedProductReports) {
                                    quantity += reportService().getCountProductQuantityInPeriodTreatment(report.getReportLocation(), inventory, false, productReport.getOperationDate(), otherFlux.getProduct()).doubleValue();
                                }
                                otherFlux.setQuantity(quantity);
                            }

                        } else if (otherFlux.getLabel().equals("DM1")) {
                            ProductReport lastProductReport = reportService().getLastProductReport(
                                    report.getReportLocation(), report.getProductProgram(), report.getUrgent());
                            if (lastProductReport != null) {
                                ProductAttributeOtherFlux lastOtherFlux = attributeFluxService().getOneProductAttributeOtherFluxByProductAndOperationAndLabel(
                                        otherFlux.getProduct(), lastProductReport, "QD", lastProductReport.getLocation()
                                );
                                otherFlux.setQuantity(lastOtherFlux.getQuantity());
                            }
                        } else if (otherFlux.getLabel().equals("DM2")) {
                            ProductReport lastProductReport = reportService().getLastProductReport(
                                    report.getReportLocation(), report.getProductProgram(), report.getUrgent());
                            if (lastProductReport != null) {
                                ProductAttributeOtherFlux lastOtherFlux = attributeFluxService().getOneProductAttributeOtherFluxByProductAndOperationAndLabel(
                                        otherFlux.getProduct(), lastProductReport, "DM1", lastProductReport.getLocation()
                                );
                                otherFlux.setQuantity(lastOtherFlux.getQuantity());
                            }
                        }
                        attributeFluxService().saveProductAttributeOtherFlux(otherFlux);
                    }
                    if (report.getReportInfo() == null || report.getReportInfo().isEmpty()
                            || !report.getReportInfo().contains("IMPORTED BY PARENT")) {
                        report.setReportInfo("IMPORTED BY PARENT");
                        reportService().saveProductReport(report);
                    }

                    message = "Données du rapport importés avec succès : " + file.getOriginalFilename();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message);
                } catch (Exception e) {
                    message = "Erreur d'importation du fichier (" + file.getOriginalFilename() + ") : "  + e.getMessage();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message);
                }
            } else
                session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "S'il vous plait, importez un fichier CSV !");
            return "redirect:/module/pharmacy/reports/editFluxOther.form?" +
                    "reportId=" + reportId;
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
            ProductProgram program = programService().getOneProductProgramById(programId);

            if (id != 0) {
                ProductReport productReport = reportService().getOneProductReportById(id);
                if (program == null) {
                    program = productReport.getProductProgram();
                }
                if (productReport != null) {
                    if (!productReport.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        String fluxLink = isPlatformUser() ? "editFlux" : "editFluxOther";
                        return "redirect:/module/pharmacy/reports/" + fluxLink + ".form?reportId=" +
                                productReport.getProductOperationId();
                    }
                    productReportForm.setProductReport(productReport);
                    modelMap.addAttribute("program", productReport.getProductProgram());
                }
            } else {

                productReportForm = new ProductReportForm();
                productReportForm.setProductProgramId(programId);
                productReportForm.setIncidence(Incidence.NONE);
                productReportForm.setProductProgramId(program.getProductProgramId());
                productReportForm.setLocationId(OperationUtils.getUserLocation().getLocationId());
                if (isDirectClient()) {
                    productReportForm.setReportType(ReportType.CLIENT_REPORT);
                } else {
                    productReportForm.setReportType(ReportType.NOT_CLIENT_REPORT);
                }
            }

            List<Product> urgentProducts = new ArrayList<Product>();

            if (isPlatformUser()) {
                ProductInventory lastIntermediateInventory = inventoryService().getLastProductInventory(
                        OperationUtils.getUserLocation(), program, InventoryType.PARTIAL);

                ProductInventory lastInventory = inventoryService().getLastProductInventory(
                        OperationUtils.getUserLocation(), program, InventoryType.TOTAL);

                if (lastIntermediateInventory != null && lastInventory != null
                        && lastIntermediateInventory.getOperationDate().after(lastInventory.getOperationDate())) {
                    urgentProducts = lastIntermediateInventory.getFluxesProductList();
                }
            } else {
                urgentProducts.addAll(program.getProducts());
            }
            modelMap.addAttribute("program", program);
            modelMap.addAttribute("products", urgentProducts);
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
                ProductReport report = productReportForm.getProductReport();
                boolean canSave = true;
                if (isPlatformUser() && !productReportForm.getUrgent()) {
                    ProductInventory lastInventory = inventoryService().getLastProductInventory(report.getLocation(), report.getProductProgram(), InventoryType.TOTAL);
                    if (lastInventory == null) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez effectuer un inventaire de \"" + report.getReportPeriod() + "\" avant le rapport de cette période !");
                        canSave = false;
                    } else {
                        if (!lastInventory.getOperationNumber().contains(report.getReportPeriod())) {
                            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez effectuer un inventaire de \"" + report.getReportPeriod() + "\" avant le rapport de cette période !");
                            canSave = false;
                        }
                    }
                }
                if (canSave) {
                    reportService().saveProductReport(report);
                    if (action.equals("addLine")) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le rapport est généré avec succès !");

                        String fluxLink = isPlatformUser() ? "editFlux" : "editFluxOther";
                        return "redirect:/module/pharmacy/reports/" + fluxLink + ".form?reportId=" +
                                report.getProductOperationId();
                    } else {
                        return "redirect:/module/pharmacy/reports/list.form";
                    }
                } else {
                    modelMap.addAttribute("productReport", report);
                }
            }
            List<Product> urgentProducts = new ArrayList<Product>();

            if (isPlatformUser()) {
                ProductInventory lastIntermediateInventory = inventoryService().getLastProductInventory(
                        OperationUtils.getUserLocation(), productReportForm.getProductReport().getProductProgram(), InventoryType.PARTIAL);

                ProductInventory lastInventory = inventoryService().getLastProductInventory(
                        OperationUtils.getUserLocation(), productReportForm.getProductReport().getProductProgram(), InventoryType.TOTAL);

                if (lastIntermediateInventory != null && lastInventory != null
                        && lastIntermediateInventory.getOperationDate().after(lastInventory.getOperationDate())) {
                    urgentProducts = lastIntermediateInventory.getFluxesProductList();
                }
            } else {
                urgentProducts.addAll(productReportForm.getProductReport().getProductProgram().getProducts());
            }

            modelMap.addAttribute("products", urgentProducts);

            modelMap.addAttribute("program", programService().getOneProductProgramById(productReportForm.getProductProgramId()));
            if (isDirectClient()) {
                modelMap.addAttribute("subTitle", "Rapport commande mensuel <i class=\"fa fa-play\"></i> Saisie entête");
            } else {
                modelMap.addAttribute("subTitle", "Rapport mensuel <i class=\"fa fa-play\"></i> Saisie entête");
            }

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
            if (productReport != null) {
                reportAttributeFluxForm.setProductOperationId(productReport.getProductOperationId());
                reportAttributeFluxForm.setInventory(inventoryService().getLastProductInventory(productReport.getLocation(), productReport.getProductProgram(), InventoryType.TOTAL));
//                System.out.println("-----------------------------> Last inventory  period ");

                List<ProductReportLineDTO> reportLineDTOS = reportAttributeFluxForm.createProductReportOtherFluxMap();
                for (ProductReportLineDTO dto : reportLineDTOS) {
                    if (!dto.getAsserted()) {
                        modelMap.addAttribute("invalidReport", true);
                        break;
                    }
                }
//                System.out.println("-----------------------------> Last inventory  period ");
                modelMap.addAttribute("productReport", productReport);
                modelMap.addAttribute("stockMax", OperationUtils.getUserLocationStockMax());
                modelMap.addAttribute("reportData", reportLineDTOS);
                if (productReport.getReportType().equals(ReportType.CLIENT_REPORT)) {
                    modelMap.addAttribute("subTitle", "Rapport commande mensuel <i class=\"fa fa-play\"></i> Visualisation");
                } else {
                    modelMap.addAttribute("subTitle", "Rapport mensuel <i class=\"fa fa-play\"></i> Visualisation");
                }
            }
        }
        return null;
    }


    @RequestMapping(value = "/module/pharmacy/reports/editFluxOther.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                           @RequestParam(value = "reportId") Integer reportId,
                           @RequestParam(value = "productId", defaultValue = "0", required = false) Integer productId,
                           ReportEntryAttributeFluxForm reportEntryAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductReport productReport = reportService().getOneProductReportById(reportId);
            if (productId != 0) {
                reportEntryAttributeFluxForm.setProductId(productId);
                reportEntryAttributeFluxForm.setProductOperationId(reportId);
                reportEntryAttributeFluxForm.setProductAttributeOtherFluxes(productId);
            } else {
                reportEntryAttributeFluxForm = new ReportEntryAttributeFluxForm();
                reportEntryAttributeFluxForm.setProductOperationId(productReport.getProductOperationId());
            }
            modelMappingForView(modelMap, reportEntryAttributeFluxForm, productReport);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/reports/editFluxOther.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           HttpServletRequest request,
                           ReportEntryAttributeFluxForm reportEntryAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductReportEntryAttributeFluxFormValidation().validate(reportEntryAttributeFluxForm, result);
            ProductReport productReport = reportService().getOneProductReportById(reportEntryAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {

                for (ProductAttributeOtherFlux otherFlux : reportEntryAttributeFluxForm.getAllOtherFluxes()) {
                    attributeFluxService().saveProductAttributeOtherFlux(otherFlux);
                }
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Ligne insérées avec succès !");

                return "redirect:/module/pharmacy/reports/editFluxOther.form?distributionId="
                        + reportEntryAttributeFluxForm.getProductOperationId();
            }

            modelMappingForView(modelMap, reportEntryAttributeFluxForm, productReport);
        }

        return null;
    }
    //
    private void modelMappingForView(ModelMap modelMap, ReportEntryAttributeFluxForm reportEntryAttributeFluxForm, ProductReport report) {
        modelMap.addAttribute("reportEntryAttributeFluxForm", reportEntryAttributeFluxForm);
        modelMap.addAttribute("productReport", report);

        if (report.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {

            List<ProductReportLineDTO> reportLineDTOS = reportService().getReportDistributionLines(report);
            modelMap.addAttribute("reportData", reportLineDTOS);

            modelMap.addAttribute("subTitle", "RAPPORT MENSUEL <i class=\"fa fa-play\"></i> ajout de produits");

//            modelMap.addAttribute("countReports", reportService().getAllProductReports(report.getLocation(), report.getProductProgram(), false).size() == 0 ? "false" : "true" );
//            System.out.println("|-----------------------------------> all child reports : " +
//                    reportService().getAllProductReports(report.getLocation(), report.getProductProgram(), false).get(0).getProductOperationId());

            boolean asserted = true;

            for (ProductReportLineDTO reportLineDTO : reportLineDTOS) {
                if (!reportLineDTO.getAsserted()) {
                    asserted = false;
                    break;
                }
            }
            modelMap.addAttribute("isAsserted", asserted);
            if (report.getReportInfo() != null) {
                if (asserted && report.getReportInfo().contains("IMPORT COMPLETED")) {
                    report.setOperationStatus(OperationStatus.SUBMITTED);
                    reportService().saveProductReport(report);
                } else {
                    modelMap.addAttribute("canImport", true);
                }
            }

            Set<Product> products = programService().getOneProductProgramById(report.getProductProgram().getProductProgramId()).getProducts();
            if (reportLineDTOS.size() != 0) {
                for (ProductReportLineDTO lineDTO : reportLineDTOS) {
                    products.remove(productService().getOneProductByCode(lineDTO.getCode()));
                }
            }
            modelMap.addAttribute("products", products);
        } else {
            List<ProductReportLineDTO> reportLineDTOS = reportService().getReportDistributionLines(report);

            modelMap.addAttribute("reportData", reportLineDTOS);
            if (report.getOperationStatus().equals(OperationStatus.SUBMITTED)) {
                modelMap.addAttribute("subTitle", "RAPPORT MENSUEL <i class=\"fa fa-play\"></i> SOUMIS");
            } else if (report.getOperationStatus().equals(OperationStatus.VALIDATED)) {
                modelMap.addAttribute("subTitle", "RAPPORT MENSUEL <i class=\"fa fa-play\"></i> APPROUVEE");
            } else if (report.getOperationStatus().equals(OperationStatus.TREATED)) {
                modelMap.addAttribute("subTitle", "Distribution <i class=\"fa fa-play\"></i> TRAITE");
            }
        }
    }

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
        String editFlux = isPlatformUser() ? "editFlux" : "editFluxOther";
        return "redirect:/module/pharmacy/reports/"+ editFlux + ".form?reportId=" + reportId;
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
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le rapport a été supprimé avec succès !");
        return "redirect:/module/pharmacy/reports/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/reports/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "reportId") Integer reportId,
                             @RequestParam(value = "productId") Integer productId){
        if (!Context.isAuthenticated())
            return null;

        ProductReport report = reportService().getOneProductReportById(reportId);
        Product product = productService().getOneProductById(productId);
//        System.out.println("Location ID :------------------------------------> " + report.getLocation().getLocationId());

        List<ProductAttributeOtherFlux> fluxes = attributeFluxService().getAllProductAttributeOtherFluxByProductAndOperation(product, report, report.getLocation());
        for (ProductAttributeOtherFlux otherFlux : fluxes) {
//            System.out.println("Ligne trouvée :------------------------------------> " + otherFlux.getProductAttributeOtherFluxId());
            attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
        }

        HttpSession session = request.getSession();
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");

        String editFlux = isPlatformUser() ? "editFlux" : "editFluxOther";
        return "redirect:/module/pharmacy/reports/"+ editFlux + ".form?reportId=" + reportId;
    }

    @RequestMapping(value = "/module/pharmacy/reports/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "reportId") Integer reportId){
        if (!Context.isAuthenticated()) {
            return null;
        }
        System.out.println("------------------------ Entered in validation !!!!");
        HttpSession session = request.getSession();
        ProductReport report = reportService().getOneProductReportById(reportId);
        if (OperationUtils.validateOperation(report)) {
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Le rapport été validée avec succèss !");
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
                    message = "Le rapport a été soumis au fournisseur. Veuillez patienter pour que le rapport soit traité !";
                }
            }
            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message);
            return "redirect:/module/pharmacy/reports/list.form";
        }
        return null;
    }
}
