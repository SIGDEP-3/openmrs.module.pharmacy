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
import org.openmrs.module.pharmacy.forms.DistributionAttributeFluxForm;
import org.openmrs.module.pharmacy.forms.ProductDistributionForm;
//import org.openmrs.module.pharmacy.models.ProductDistributionFluxDTO;
import org.openmrs.module.pharmacy.models.ProductReportLineDTO;
import org.openmrs.module.pharmacy.models.ProductReportLineExtendedDTO;
import org.openmrs.module.pharmacy.models.ProductUploadResumeDTO;
import org.openmrs.module.pharmacy.utils.CSVHelper;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.validators.ProductDistributionAttributeFluxFormValidation;
//import org.openmrs.module.pharmacy.validators.ProductDistributionFormValidation;
import org.openmrs.module.pharmacy.validators.ProductDistributionFormValidation;
import org.openmrs.util.OpenmrsUtil;
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
import java.util.*;

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

    private ProductService productService(){
        return Context.getService(ProductService.class);
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
            int totalReportsToSubmit = 0;
            for (Location location : OperationUtils.getUserLocation().getChildLocations()) {
                if (OperationUtils.getLocationPrograms(location).size() != 0) {
                    totalReportsToSubmit = totalReportsToSubmit + OperationUtils.getLocationPrograms(location).size();
                }
            }

            modelMap.addAttribute("reports", reportService().getAllProductDistributionReports(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("submittedReports", reportService().getAllSubmittedChildProductReports(OperationUtils.getUserLocation(), false));
            modelMap.addAttribute("programs", programService().getAllProductProgram());
            modelMap.addAttribute("childrenLocation", OperationUtils.getUserLocation().getChildLocations());
            modelMap.addAttribute("totalReportsToSubmit", totalReportsToSubmit);
            modelMap.addAttribute("subTitle", "Liste des Distributions");
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/edit.form", method = RequestMethod.GET)
    public String edit(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                       @RequestParam(value = "programId", defaultValue = "0", required = false) Integer programId,
                       ProductDistributionForm productDistributionForm) {
        if (Context.isAuthenticated()) {
            ProductProgram program = null;
            if (id != 0) {
                ProductReport productDistribution = reportService().getOneProductReportById(id);
                if (productDistribution != null) {
                    if (!productDistribution.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {
                        return "redirect:/module/pharmacy/operations/distribution/editFlux.form?distributionId=" +
                            productDistribution.getProductOperationId();
                    }
                    productDistributionForm.setProductReport(productDistribution);
                    program = productDistribution.getProductProgram();
                    modelMap.addAttribute("program", productDistribution.getProductProgram());
                }
            } else {
                program = programService().getOneProductProgramById(programId);
                if (program == null) {
                    HttpSession session = request.getSession();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous devez sélectionner un programme !");
                    return "redirect:/module/pharmacy/operations/distribution/list.form";
                }
                productDistributionForm = new ProductDistributionForm();
                productDistributionForm.setProductProgramId(program.getProductProgramId());
                productDistributionForm.setLocationId(OperationUtils.getUserLocation().getLocationId());

            }

            modelMap.addAttribute("program", program);

//            modelMap.addAttribute("latestDistribution", latestDistribution(productDistributionForm.getProductDistribution()));
            modelMap.addAttribute("productDistributionForm", productDistributionForm);
//            modelMap.addAttribute("productDistribution", reportService().getOneProductReportById(id));
            modelMap.addAttribute("childrenLocation", OperationUtils.getUserChildLocationsByProgram(program));
            modelMap.addAttribute("subTitle", "Distribution - Entête");
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       @RequestParam(value = "action", defaultValue = "addLine", required = false) String action,
                       ProductDistributionForm productDistributionForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductDistributionFormValidation().validate(productDistributionForm, result);

            if (!result.hasErrors()) {

                ProductReport distribution = productDistributionForm.getProductReport();
                if (distribution.getChildLocationReport() == null) {
                    ProductReport childReport = reportService().saveProductReport(productDistributionForm.getChildReport());
                    distribution.setChildLocationReport(childReport);
                }
                distribution.setOperationStatus(OperationStatus.NOT_COMPLETED);
                reportService().saveProductReport(distribution);
                if (action.equals("addLine")) {
                    if (distribution.getProductAttributeFluxes().size() == 0) {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez maintenant ajouter les produits !");
                    } else {
                        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Vous pouvez continuer à ajouter les produits !");
                    }
                    return "redirect:/module/pharmacy/operations/distribution/editFlux.form?distributionId=" +
                            distribution.getProductOperationId();
                } else {
                    return "redirect:/module/pharmacy/operations/distribution/list.form";
                }
            }

            ProductProgram program = programService().getOneProductProgramById(productDistributionForm.getProductProgramId());
            modelMap.addAttribute("program", programService().getOneProductProgramById(productDistributionForm.getProductProgramId()));
            modelMap.addAttribute("productDistributionForm", productDistributionForm);
            modelMap.addAttribute("subTitle", "Distribution - Entête");modelMap.addAttribute("childrenLocation", OperationUtils.getUserChildLocationsByProgram(program));
        }

        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/editFlux.form", method = RequestMethod.GET)
    public String editFlux(ModelMap modelMap,
                         @RequestParam(value = "distributionId") Integer distributionId,
                         @RequestParam(value = "productId", defaultValue = "0", required = false) Integer productId,
                         @RequestParam(value = "slip", defaultValue = "0", required = false) Integer slip,
                         DistributionAttributeFluxForm distributionAttributeFluxForm) {
        if (Context.isAuthenticated()) {
            ProductReport productDistribution = reportService().getOneProductReportById(distributionId);
            if (productId != 0) {
                distributionAttributeFluxForm.setProductId(productId);
                distributionAttributeFluxForm.setProductOperationId(distributionId);
                distributionAttributeFluxForm.setProductAttributeOtherFluxes(productId);
            } else {
                distributionAttributeFluxForm = new DistributionAttributeFluxForm();
                distributionAttributeFluxForm.setProductOperationId(productDistribution.getProductOperationId());
            }
            modelMappingForView(modelMap, distributionAttributeFluxForm, productDistribution, slip);
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/editFlux.form", method = RequestMethod.POST)
    public String saveFlux(ModelMap modelMap,
                           HttpServletRequest request,
                           DistributionAttributeFluxForm distributionAttributeFluxForm,
                           BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductDistributionAttributeFluxFormValidation().validate(distributionAttributeFluxForm, result);
            ProductReport productDistribution = reportService().getOneProductReportById(distributionAttributeFluxForm.getProductOperationId());

            if (!result.hasErrors()) {

                for (ProductAttributeOtherFlux otherFlux : distributionAttributeFluxForm.getAllOtherFluxes()) {
                    attributeFluxService().saveProductAttributeOtherFlux(otherFlux);
                }
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Ligne insérées avec succès !");

                return "redirect:/module/pharmacy/operations/distribution/editFlux.form?distributionId="
                        + distributionAttributeFluxForm.getProductOperationId() + "&imported=1";
//                ProductAttribute productAttribute = attributeService().saveProductAttribute(distributionAttributeFluxForm.getProductAttribute());
//                if (productAttribute != null) {
//                    ProductAttributeFlux productAttributeFlux = distributionAttributeFluxForm.getProductAttributeFlux(productAttribute);
//                    productAttributeFlux.setStatus(productDistribution.getOperationStatus());
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
            }

            modelMappingForView(modelMap, distributionAttributeFluxForm, productDistribution, 0);
        }

        return null;
    }
//
    private void modelMappingForView(ModelMap modelMap, DistributionAttributeFluxForm distributionAttributeFluxForm, ProductReport productDistribution, Integer slip) {
        modelMap.addAttribute("distributionAttributeFluxForm", distributionAttributeFluxForm);
        modelMap.addAttribute("productDistribution", productDistribution);

        if (productDistribution.getOperationStatus().equals(OperationStatus.NOT_COMPLETED)) {

            List<ProductReportLineDTO> reportLineDTOS = reportService().getReportDistributionLines(productDistribution.getChildLocationReport());
            modelMap.addAttribute("reportData", reportLineDTOS);

            modelMap.addAttribute("subTitle", "Distribution <i class=\"fa fa-play\"></i> ajout de produits");

            ProductReport report = productDistribution.getChildLocationReport();
            modelMap.addAttribute("countReports", reportService().getAllProductReports(report.getLocation(), report.getProductProgram(), false).size() == 0 ? "false" : "true" );
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
            if (report.getOperationNumber() != null && report.getOperationNumber().contains("IMPORTED BY PARENT")) {
                if (asserted) {
                    ProductReport childReport = productDistribution.getChildLocationReport();
                    childReport.setOperationStatus(OperationStatus.SUBMITTED);
                    reportService().saveProductReport(childReport);
                } else {
                    modelMap.addAttribute("canImport", true);
                }
            }

            Set<Product> products = programService().getOneProductProgramById(productDistribution.getProductProgram().getProductProgramId()).getProducts();
            if (reportLineDTOS.size() != 0) {
                for (ProductReportLineDTO lineDTO : reportLineDTOS) {
                    products.remove(productService().getOneProductByCode(lineDTO.getCode()));
                }
            }
            modelMap.addAttribute("products", products);
        } else {
            List<ProductReportLineExtendedDTO> reportLineDTOS = new ArrayList<ProductReportLineExtendedDTO>();

            for (ProductReportLineDTO reportLineDTO : reportService().getReportDistributionLines(productDistribution.getChildLocationReport())) {
                Product product = productService().getOneProductByCode(reportLineDTO.getCode());

                ProductReportLineExtendedDTO reportLineExtendedDTO = new ProductReportLineExtendedDTO();
                reportLineExtendedDTO.getProductLineDto(reportLineDTO);
                reportLineExtendedDTO.setParentQuantityInStock(stockService().getAllProductAttributeStockByProductCount(
                        product,
                        productDistribution.getLocation(),
                        false
                ));
                Double cmm = (reportLineExtendedDTO.getDistributedQuantity().doubleValue() +
                        reportLineExtendedDTO.getQuantityDistributed1monthAgo().doubleValue() +
                        reportLineExtendedDTO.getQuantityDistributed2monthAgo().doubleValue()) / 3;
                if (cmm.intValue() != cmm) {
                    cmm = cmm.intValue() + 1.0;
                }
                reportLineExtendedDTO.setCalculatedAverageMonthlyConsumption(cmm);
                Double stockMax = OperationUtils.getLocationStockMax(productDistribution.getReportLocation());
                double quantityProposed = ( stockMax * cmm ) - reportLineExtendedDTO.getQuantityInStock();
                if ((int) quantityProposed != quantityProposed) {
                    quantityProposed = (int) quantityProposed + 1.0;
                }
                reportLineExtendedDTO.setQuantityToOrder(quantityProposed);
                reportLineExtendedDTO.setAccordedQuantity(attributeFluxService().getAllProductAttributeFluxByOperationAndProductCount(productDistribution, product));

                if (reportLineExtendedDTO.getAccordedQuantity() == 0 && quantityProposed != 0 && reportLineExtendedDTO.getParentQuantityInStock() != 0) {{
                        List<ProductAttributeFlux> fluxes = OperationUtils.createProductAttributeFluxes(product, productDistribution, (int) quantityProposed);
                        for (ProductAttributeFlux flux : fluxes) {
                            flux.setStatus(OperationStatus.NOT_COMPLETED);
                            attributeFluxService().saveProductAttributeFlux(flux);
                        }
                        reportLineExtendedDTO.setAccordedQuantity((int) quantityProposed);
                    }
                }

                reportLineDTOS.add(reportLineExtendedDTO);
            }
            modelMap.addAttribute("reportData", reportLineDTOS);
            if (productDistribution.getOperationStatus().equals(OperationStatus.AWAITING_TREATMENT)) {
                modelMap.addAttribute("subTitle", "Distribution <i class=\"fa fa-play\"></i> EN COURS DE TRAITEMENT");
            } else if (productDistribution.getOperationStatus().equals(OperationStatus.VALIDATED)) {
                if (slip != 0) {
                    modelMap.addAttribute("subTitle", "Distribution <i class=\"fa fa-play\"></i> BORDEREAU DE LIVRAISON");
                    modelMap.addAttribute("slip", "true");
                    modelMap.addAttribute("productAttributeFluxes", productDistribution.getProductAttributeFluxes());
                } else
                modelMap.addAttribute("subTitle", "Distribution <i class=\"fa fa-play\"></i> APPROUVEE");
            } else if (productDistribution.getOperationStatus().equals(OperationStatus.TREATED)) {
                modelMap.addAttribute("subTitle", "Distribution <i class=\"fa fa-play\"></i> TRAITE");
            }
        }
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/upload.form", method = RequestMethod.POST)
    public String uploadProduct(HttpServletRequest request,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam(value = "distributionId") Integer distributionId) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();
            String message = "";
            if (CSVHelper.hasCSVFormat(file)) {
                try {
                    ProductReport report = reportService().getOneProductReportById(distributionId);
                    ProductReport childReport = report.getChildLocationReport();
                    List<ProductAttributeOtherFlux> otherFluxes = CSVHelper.csvReport(file.getInputStream(), childReport);

                    for (ProductAttributeOtherFlux otherFlux : otherFluxes) {
                        System.out.println("|---------------------------> Product : " + otherFlux.getProduct().getCode() + ", Label : " +
                                otherFlux.getLabel() + ", Quantity : " + otherFlux.getQuantity() );
                        attributeFluxService().saveProductAttributeOtherFlux(otherFlux);
                    }
                    if (childReport.getOperationNumber() == null || childReport.getOperationNumber().isEmpty()
                            || !childReport.getOperationNumber().contains("IMPORTED BY PARENT")) {
                        childReport.setOperationNumber("IMPORTED BY PARENT");
                        reportService().saveProductReport(childReport);
                    }

                    message = "Données du rapport importés avec succès : " + file.getOriginalFilename();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message);
                } catch (Exception e) {
                    message = "Erreur d'importation du fichier (" + file.getOriginalFilename() + ") : "  + e.getMessage();
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, message);
                }
            } else
                session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "S'il vous plait importez un fichier CSV !");
            return "redirect:/module/pharmacy/operations/distribution/editFlux.form?" +
                    "distributionId=" + distributionId;
        }
        return null;
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/complete.form", method = RequestMethod.GET)
    public String complete(HttpServletRequest request,
                           @RequestParam(value = "distributionId") Integer distributionId){
        if (!Context.isAuthenticated())
            return null;
        HttpSession session = request.getSession();
        ProductReport distribution = reportService().getOneProductReportById(distributionId);
        distribution.setOperationStatus(OperationStatus.AWAITING_TREATMENT);
        reportService().saveProductReport(distribution);
        if (!distribution.getChildLocationReport().getOperationStatus().equals(OperationStatus.SUBMITTED)) {
            ProductReport childProductReport = distribution.getChildLocationReport();
            childProductReport.setOperationStatus(OperationStatus.SUBMITTED);
            reportService().saveProductReport(childProductReport);
        }
        //attributeService().purgeUnusedAttributes();
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
        ProductReport childReport = report.getChildLocationReport();

//        reportService().removeProductReport(report);

        if (report.getCreator().equals(childReport.getCreator())) {
            for (ProductAttributeOtherFlux flux : childReport.getProductAttributeOtherFluxes()) {
                attributeFluxService().removeProductAttributeOtherFlux(flux);
            }
            reportService().removeProductReport(report);
            reportService().removeProductReport(childReport);
        } else {
            reportService().removeProductReport(report);
        }

//        for (ProductAttributeOtherFlux otherFlux : attributeFluxService().getAllProductAttributeOtherFluxByOperation(report, false)) {
//            attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
//        }
//        for (ProductAttributeFlux flux : attributeFluxService().getAllProductAttributeFluxByOperation(report, false)){
//            attributeFluxService().removeProductAttributeFlux(flux);
//        }

        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La distribution a été supprimé avec succès !");
        return "redirect:/module/pharmacy/operations/distribution/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/deleteFlux.form", method = RequestMethod.GET)
    public String deleteFlux(HttpServletRequest request,
                             @RequestParam(value = "distributionId") Integer distributionId,
                             @RequestParam(value = "productId") Integer productId){
        if (!Context.isAuthenticated())
            return null;

        ProductReport report = reportService().getOneProductReportById(distributionId).getChildLocationReport();
        Product product = productService().getOneProductById(productId);
        System.out.println("Location ID :------------------------------------> " + report.getLocation().getLocationId());

        List<ProductAttributeOtherFlux> fluxes = attributeFluxService().getAllProductAttributeOtherFluxByProductAndOperation(product, report, report.getLocation());
        for (ProductAttributeOtherFlux otherFlux : fluxes) {
            System.out.println("Ligne trouvée :------------------------------------> " + otherFlux.getProductAttributeOtherFluxId());
            attributeFluxService().removeProductAttributeOtherFlux(otherFlux);
        }

        HttpSession session = request.getSession();
        session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "La ligne du produit a été supprimée avec succès !");

        return "redirect:/module/pharmacy/operations/distribution/editFlux.form?distributionId=" + distributionId;
    }

    @RequestMapping(value = "/module/pharmacy/operations/distribution/validate.form", method = RequestMethod.GET)
    public String validate(HttpServletRequest request,
                           @RequestParam(value = "distributionId") Integer distributionId){
        if (!Context.isAuthenticated())
            return null;
        ProductReport operation = reportService().getOneProductReportById(distributionId);

        if (operation != null) {
            OperationUtils.emptyStock(OperationUtils.getUserLocation(), operation.getProductProgram());
            operation.setOperationNumber(OperationUtils.generateNumber());
            operation.setTreatmentDate(new Date());
            ProductReport childReport = operation.getChildLocationReport();
            childReport.setOperationStatus(OperationStatus.TREATED);
            reportService().saveProductReport(childReport);

            if (OperationUtils.validateOperation(operation)) {
                HttpSession session = request.getSession();
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Votre distribution a été validée avec succès !");
                return "redirect:/module/pharmacy/operations/distribution/list.form";
            }
        }
        return null;
    }

}
