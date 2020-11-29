package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.api.ProductRegimenService;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.api.ProductUnitService;
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

@Controller
public class PharmacyProductManageController {
    protected final Log log = LogFactory.getLog(getClass());

    private ProductService productService() {
        return Context.getService(ProductService.class);
    }

    private ProductProgramService programService(){
        return Context.getService(ProductProgramService.class);
    }

    private ProductUnitService unitService(){
        return Context.getService(ProductUnitService.class);
    }

    private ProductRegimenService regimenService(){
        return Context.getService(ProductRegimenService.class);
    }

    @RequestMapping(value = "/module/pharmacy/product/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("products", productService().getAllProduct());
            modelMap.addAttribute("title", "Liste des Produits");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                     @RequestParam(value = "id", defaultValue = "0", required = false) Integer id) {
        if (Context.isAuthenticated()) {
            ProductForm productForm = new ProductForm();
            Product product = new Product();
            if (id != 0) {
                product = productService().getOneProductById(id);
                productForm.setProduct(product);
            }

            modelMap.addAttribute("productForm", productForm);
            modelMap.addAttribute("product", product);
            modelMap.addAttribute("availablePrograms", programService().getAllProductProgram());
            modelMap.addAttribute("availableRetailUnits", unitService().getAllProductUnit());
            modelMap.addAttribute("availableWholesaleUnits", unitService().getAllProductUnit());
            modelMap.addAttribute("availableRegimens", regimenService().getAllProductRegimen());
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
                productService().saveProduct(productForm.getProduct());
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit ajouté avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Produit modifié avec succès");
                }
                return "redirect:/module/pharmacy/product/list.form";
            }
            modelMap.addAttribute("productForm", productForm);
            modelMap.addAttribute("product", productForm.getProduct());
            modelMap.addAttribute("availablePrograms", programService().getAllProductProgram());
            modelMap.addAttribute("availableRetailUnits", unitService().getAllProductUnit());
            modelMap.addAttribute("availableWholesaleUnits", unitService().getAllProductUnit());
            modelMap.addAttribute("availableRegimens", regimenService().getAllProductRegimen());
            modelMap.addAttribute("title", "Formulaire de saisie des Programmes");
        }

        return null;
    }
}
