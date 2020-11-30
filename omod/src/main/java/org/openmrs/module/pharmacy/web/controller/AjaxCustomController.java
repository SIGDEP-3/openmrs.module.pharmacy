package org.openmrs.module.pharmacy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxCustomController {
    @ResponseBody
    @RequestMapping(value = "/search/api/getSearchResult")
    public String getProductQuantityInStock() {

        return "il n'y a rien pour l'instant";
    }
}
