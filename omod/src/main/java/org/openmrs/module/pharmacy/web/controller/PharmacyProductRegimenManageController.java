package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductRegimen;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductRegimenService;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.forms.ProductRegimenForm;
import org.openmrs.module.pharmacy.validators.ProductRegimenFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PharmacyProductRegimenManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private ProductRegimenService service() {
        return Context.getService(ProductRegimenService.class);
    }

    @RequestMapping(value = "/module/pharmacy/product/regimens/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("regimens", service().getAllProductRegimen());
            modelMap.addAttribute("title", "Liste des Régimes");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/regimens/delete.form", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         HttpServletRequest request) {
        if (!Context.isAuthenticated())
            return null;

        if (id != 0) {
            HttpSession session = request.getSession();
            ProductRegimen productRegimen = service().getOneProductRegimenById(id);
            if (productRegimen != null) {
                service().removeProductRegimen(productRegimen);
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Regime supprimé avec succès");
            }
        }

        return "redirect:/module/pharmacy/product/regimens/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/product/regimens/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                     @RequestParam(value = "id", defaultValue = "0", required = false) Integer id) {
        if (Context.isAuthenticated()) {
            ProductRegimenForm productRegimenForm = new ProductRegimenForm();
            if (id != 0) {
                productRegimenForm.setProductRegimen(service().getOneProductRegimenById(id));
            }

            modelMap.addAttribute("regimenForm", productRegimenForm);
            modelMap.addAttribute("conceptList", getConceptMapList());
            modelMap.addAttribute("title", "Formulaire de saisie des Regimes");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/regimens/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       ProductRegimenForm regimenForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductRegimenFormValidation().validate(regimenForm, result);

            if (!result.hasErrors()) {
                boolean idExist = (regimenForm.getProductRegimenId() != null);
                service().saveProductRegimen(regimenForm.getProductRegimen());
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Regimenme ajouté avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Regimenme modifié avec succès");
                }
                return "redirect:/module/pharmacy/product/regimens/list.form";
            }
            modelMap.addAttribute("regimenForm", regimenForm);
            modelMap.addAttribute("conceptList", getConceptMapList());
            modelMap.addAttribute("title", "Formulaire de saisie des Regimes");
        }

        return null;
    }

    private Map<Integer, String> getConceptMapList() {
        String conceptId = Context.getAdministrationService().getGlobalProperty("pharmacy.regimenConcept");
        List<Concept> concepts = getConceptFromRegimen(service().getAllProductRegimen());
        Map<Integer, String> conceptList = new LinkedHashMap<Integer, String>();

        for (ConceptAnswer conceptAnswer :
                Context.getConceptService().getConcept(Integer.parseInt(conceptId)).getAnswers()) {
            if (!concepts.contains(conceptAnswer.getAnswerConcept())) {
                conceptList.put(conceptAnswer.getAnswerConcept().getConceptId(), conceptAnswer.getAnswerConcept().getName().getName());
            }
        }
        return conceptList;
    }

    List<Concept> getConceptFromRegimen(List<ProductRegimen> regimenList) {
        List<Concept> concepts = new ArrayList<Concept>();
        for (ProductRegimen regimen : regimenList) {
            concepts.add(regimen.getConcept());
        }
        return concepts;
    }
}
