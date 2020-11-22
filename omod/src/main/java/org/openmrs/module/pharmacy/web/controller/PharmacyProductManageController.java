package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductRegimen;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.forms.ProductForm;
import org.openmrs.module.pharmacy.validators.ProductFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class PharmacyProductManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private PharmacyService service() {
        return Context.getService(PharmacyService.class);
    }

    @RequestMapping(value = "/module/pharmacy/product/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("products", service().getAllProduct());
            modelMap.addAttribute("title", "Liste des Produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                     @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                     ProductForm productForm) {
        if (Context.isAuthenticated()) {
            Product product = new Product();
            if (id != 0) {
                product = service().getOneProductById(id);
                productForm.setProduct(product);
            } else {
                productForm = new ProductForm();
            }

            modelMap.addAttribute("productForm", productForm);
            modelMap.addAttribute("product", product);
            modelMap.addAttribute("availablePrograms", service().getAllProductProgram());
            modelMap.addAttribute("availableRetailUnits", service().getAllProductUnit());
            modelMap.addAttribute("availableWholesaleUnits", service().getAllProductUnit());
            modelMap.addAttribute("availableRegimens", service().getAllProductRegimen());
            modelMap.addAttribute("title", "Formulaire de saisie des Produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       ProductForm productForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductFormValidation().validate(productForm, result);

            if (!result.hasErrors()) {
                boolean idExist = (productForm.getProductId() != null);
                service().saveProduct(productForm.getProduct());
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit ajouté avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                }
                return "redirect:/module/pharmacy/product/list.form";
            }
            modelMap.addAttribute("productForm", productForm);
            modelMap.addAttribute("product", productForm.getProduct());
            modelMap.addAttribute("availablePrograms", service().getAllProductProgram());
            modelMap.addAttribute("availableRetailUnits", service().getAllProductUnit());
            modelMap.addAttribute("availableWholesaleUnits", service().getAllProductUnit());
            modelMap.addAttribute("availableRegimens", service().getAllProductRegimen());
            modelMap.addAttribute("title", "Formulaire de saisie des Programmes");
        }

        return null;
    }
}
