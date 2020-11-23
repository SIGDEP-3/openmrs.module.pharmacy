package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductSupplier;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.forms.SupplierForm;
import org.openmrs.module.pharmacy.validators.ProductProgramFormValidation;
import org.openmrs.module.pharmacy.validators.ProductSupplierFormValidation;
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
public class PharmacyProductSupplierManageController {

    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    @RequestMapping(value = "/module/pharmacy/product/suppliers/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("suppliers", service().getAllProductSuppliers());
            modelMap.addAttribute("title", "Liste des Fournisseurs");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/suppliers/delete.form", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         HttpServletRequest request) {
        if (!Context.isAuthenticated())
            return null;

        if (id != 0) {
            HttpSession session = request.getSession();
            ProductSupplier productSupplier = service().getOneProductSupplierById(id);
            if (productSupplier != null) {
                service().removeProductSupplier(productSupplier);
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Fournisseur ajouté avec succès");
            }
        }

        return "redirect:/module/pharmacy/product/suppliers/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/product/suppliers/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                         @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         SupplierForm supplierForm) {
        if (Context.isAuthenticated()) {

            if (id != 0) {
                supplierForm.setProductSupplier(service().getOneProductSupplierById(id));
            } else {
                supplierForm = new SupplierForm();
            }

            modelMap.addAttribute("supplierForm", supplierForm);
            modelMap.addAttribute("locationList", Context.getLocationService().getAllLocations());
            modelMap.addAttribute("title", "Formulaire de saisie des Fournisseurs");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/suppliers/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                            HttpServletRequest request,
                            SupplierForm supplierForm,
                            BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductSupplierFormValidation().validate(supplierForm, result);

            if (!result.hasErrors()) {
                boolean idExist = (supplierForm.getProductSupplierId() != null);
                service().saveProductSupplier(supplierForm.getProductSupplier());
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Fournisseur ajouté avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Fournisseur modifié avec succès");
                }
                return "redirect:/module/pharmacy/product/suppliers/list.form";
            }
            modelMap.addAttribute("supplierForm", supplierForm);
            modelMap.addAttribute("locationList", Context.getLocationService().getAllLocations());
            modelMap.addAttribute("title", "Formulaire de saisie des Fournisseurs");
        }

        return null;
    }
}
