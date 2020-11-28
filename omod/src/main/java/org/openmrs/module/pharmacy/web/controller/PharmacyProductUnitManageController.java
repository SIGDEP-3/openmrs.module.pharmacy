package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductUnit;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductUnitService;
import org.openmrs.module.pharmacy.validators.ProductUnitFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PharmacyProductUnitManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private ProductUnitService service() {
        return Context.getService(ProductUnitService.class);
    }

    @RequestMapping(value = "/module/pharmacy/product/units/list.form", method = RequestMethod.GET)
    public void units(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("units", service().getAllProductUnit());
            modelMap.addAttribute("title", "Liste des unités de produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/units/edit.form", method = RequestMethod.GET)
    public void unitForm(ModelMap modelMap,
                         @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         ProductUnit unitForm) {
        if (Context.isAuthenticated()) {
            //HttpSession session = request.getSession();
            if (id != 0) {
                unitForm = service().getOneProductUnitById(id);
            }

            modelMap.addAttribute("unitForm", unitForm);
            modelMap.addAttribute("title", "Formulaire de saisie des Unités");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/units/delete.form", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", defaultValue = "0", required = false) Integer id) {
        if (!Context.isAuthenticated())
            return null;

        if (id != 0) {
            ProductUnit unit = service().getOneProductUnitById(id);
            if (unit != null) {
                service().removeProductUnit(unit);
            }
        }

        return "redirect:/module/pharmacy/product/units/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/product/units/edit.form", method = RequestMethod.POST)
    public String saveUnits(ModelMap modelMap,
                            HttpServletRequest request,
                            ProductUnit unitForm,
                            BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductUnitFormValidation().validate(unitForm, result);

            if (!result.hasErrors()) {
                boolean idExist = (unitForm.getProductUnitId() != null);
                service().saveProductUnit(unitForm);
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Unité ajoutée avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Unité modifiée avec succès");
                }
                return "redirect:/module/pharmacy/product/units/list.form";
            }
            modelMap.addAttribute("unitForm", unitForm);
            modelMap.addAttribute("title", "Formulaire de saisie des Unités");
        }

        return null;
    }
}
