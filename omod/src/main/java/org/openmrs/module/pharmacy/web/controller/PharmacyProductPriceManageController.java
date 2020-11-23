package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductPrice;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductPriceService;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.forms.ProductPriceForm;
import org.openmrs.module.pharmacy.validators.ProductPriceFormValidation;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PharmacyProductPriceManageController {

    protected final Log log = LogFactory.getLog(getClass());

    private ProductPriceService service() {
        return Context.getService(ProductPriceService.class);
    }
    private ProductService productService(){
        return Context.getService(ProductService.class);
    }
    private ProductProgramService programService(){
        return Context.getService(ProductProgramService.class);
    }

    @RequestMapping(value = "/module/pharmacy/product/prices/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("prices", service().getAllProductPrices());
            modelMap.addAttribute("availableProduct", productService().getAllProduct());
            modelMap.addAttribute("title", "Liste des prix");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/prices/delete.form", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         HttpServletRequest request) {
        if (!Context.isAuthenticated())
            return null;

        if (id != 0) {
            HttpSession session = request.getSession();
            ProductPrice productPrice = service().getOneProductPriceById(id);
            if (productPrice != null) {
                service().removeProductPrice(productPrice);
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Prix supprimé avec succès");
            }
        }
        return "redirect:/module/pharmacy/product/prices/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/product/prices/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                         @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         @RequestParam(value = "productId") Integer productId,
                     ProductPriceForm productPriceForm ){
        if (Context.isAuthenticated()) {
            if (productId != 0) {
                Product product = productService().getOneProductById(productId);
                productPriceForm.setProductId(product.getProductId());

                modelMap.addAttribute("priceForm", productPriceForm);
                modelMap.addAttribute("product", product);
                modelMap.addAttribute("availablePrograms", product.getProductPrograms());
                modelMap.addAttribute("title", "Formulaire de saisie des prix");
            }
            else {
                if (id != 0) {
                    productPriceForm.setProductPrice(service().getOneProductPriceById(id));
                } else {
                    productPriceForm = new ProductPriceForm();
                }
            }

        }
    }

    @RequestMapping(value = "/module/pharmacy/product/prices/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                            HttpServletRequest request,
                       ProductPriceForm priceForm,
                            BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductPriceFormValidation().validate(priceForm, result);

            if (!result.hasErrors()) {
                boolean idExist = (priceForm.getProductPriceId() != null);
                service().saveProductPrice(priceForm.getProductPrice());
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Prix ajouté avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Prix modifié avec succès");
                }
                return "redirect:/module/pharmacy/product/prices/list.form";
            }
            modelMap.addAttribute("priceForm", priceForm);
            modelMap.addAttribute("availableProduct", productService().getAllProduct());
            modelMap.addAttribute("availablePrograms", programService().getAllProductProgram());
            modelMap.addAttribute("title", "Formulaire de saisie des Prix");
        }

        return null;
    }
}
