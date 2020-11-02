package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PharmacyProductRegimenManageController {
    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/module/pharmacy/product-regimen", method = RequestMethod.GET)
    public void productRegimens(ModelMap modelMap) {

    }

    @RequestMapping(value = "/module/pharmacy/product-regimen", method = RequestMethod.POST)
    public String saveProductRegimen(ModelMap modelMap) {
        return null;
    }
}
