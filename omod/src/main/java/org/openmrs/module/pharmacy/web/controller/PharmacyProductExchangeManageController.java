package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductExchangeEntity;
import org.openmrs.module.pharmacy.api.ProductExchangeEntityService;
import org.openmrs.module.pharmacy.forms.ExchangeEntityForm;
import org.openmrs.module.pharmacy.validators.ProductExchangeFormValidation;
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
public class PharmacyProductExchangeManageController {

    protected final Log log = LogFactory.getLog(getClass());

    private ProductExchangeEntityService service() {
        return Context.getService(ProductExchangeEntityService.class);
    }

    @RequestMapping(value = "/module/pharmacy/product/exchanges/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("exchanges", service().getAllProductExchange());
            modelMap.addAttribute("title", "Liste des Partenaires");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/exchanges/delete.form", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         HttpServletRequest request) {
        if (!Context.isAuthenticated())
            return null;

        if (id != 0) {
            HttpSession session = request.getSession();
            ProductExchangeEntity productExchangeEntity = service().getOneProductExchangeById(id);
            if (productExchangeEntity != null) {
                service().removeProductExchange(productExchangeEntity);
                session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Partenaire supprimé avec succès");
            }
        }
        return "redirect:/module/pharmacy/product/exchanges/list.form";
    }

    @RequestMapping(value = "/module/pharmacy/product/exchanges/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                         @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                         ExchangeEntityForm exchangeEntityForm) {
        if (Context.isAuthenticated()) {

            if (id != 0) {
//                exchangeEntityForm.setProductExchangeEntity(service().getOneProductExchangeById(id));
                exchangeEntityForm.setProductExchangeEntity(service().getOneProductExchangeById(id));
            } else {
                exchangeEntityForm = new ExchangeEntityForm();
            }

            modelMap.addAttribute("exchangeEntityForm", exchangeEntityForm);
            modelMap.addAttribute("locationList", Context.getLocationService().getAllLocations());
            modelMap.addAttribute("title", "Formulaire de saisie des Partenaires");
        }
    }

    @RequestMapping(value = "/module/pharmacy/product/exchanges/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                            HttpServletRequest request,
                            ExchangeEntityForm exchangeEntityForm,
                            BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new ProductExchangeFormValidation().validate(exchangeEntityForm, result);

            if (!result.hasErrors()) {
                boolean idExist = (exchangeEntityForm.getProductExchangeEntityId() != null);
                service().saveProductExchange(exchangeEntityForm.getProductExchangeEntity());
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Partenaire ajouté avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Partenaire modifié avec succès");
                }
                return "redirect:/module/pharmacy/product/exchanges/list.form";
            }

            modelMap.addAttribute("ExchangeEntityForm", exchangeEntityForm);
            modelMap.addAttribute("locationList", Context.getLocationService().getAllLocations());
            modelMap.addAttribute("title", "Formulaire de saisie des Partenaires");
        }

        return null;
    }
}
