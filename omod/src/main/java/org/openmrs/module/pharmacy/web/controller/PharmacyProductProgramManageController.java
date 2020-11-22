package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.validators.ProductProgramFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PharmacyProductProgramManageController {

    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    @RequestMapping(value = "/module/pharmacy/product/programs/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("programs", service().getAllProductProgram());
            modelMap.addAttribute("title", "Liste des Programmes");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/programs/delete.form", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         HttpServletRequest request) {
        if (!Context.isAuthenticated())
            return null;

        if (id != 0) {
            HttpSession session = request.getSession();
            ProductProgram productProgram = service().getOneProductProgramById(id);
            if (productProgram != null) {
                service().removeProductProgram(productProgram);
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Programme supprimé avec succès");
            }
        }
        return "redirect:/module/pharmacy/product/programs/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/product/programs/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                         @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         ProductProgram productProgram) {
        if (Context.isAuthenticated()) {

            if (id != 0) {
                productProgram = service().getOneProductProgramById(id);
            } else {
                productProgram = new ProductProgram();
            }

            modelMap.addAttribute("programForm", productProgram);
            modelMap.addAttribute("title", "Formulaire de saisie des Programmes");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/programs/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                            HttpServletRequest request,
                            ProductProgram programForm,
                            BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductProgramFormValidation().validate(programForm, result);

            if (!result.hasErrors()) {
                boolean idExist = (programForm.getProductProgramId() != null);
                service().saveProductProgram(programForm);
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Programme ajouté avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Programme modifié avec succès");
                }
                return "redirect:/module/pharmacy/product/programs/list.form";
            }
            modelMap.addAttribute("programForm", programForm);
            modelMap.addAttribute("title", "Formulaire de saisie des Programmes");
        }

        return null;
    }
}
