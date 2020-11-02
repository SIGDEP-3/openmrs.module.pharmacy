package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PharmacyProductProgramManageController {
    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/module/pharmacy/product-program", method = RequestMethod.GET)
    public void productPrograms(ModelMap modelMap) { }

    @RequestMapping(value = "/module/pharmacy/product-program", method = RequestMethod.POST)
    public String saveProductProgram(ModelMap modelMap) {
        return null;
    }
}
