package org.openmrs.module.pharmacy.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.openmrs.module.pharmacy.api.ProductRegimenService;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.api.ProductUnitService;
import org.openmrs.module.pharmacy.forms.other.PrescriberForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.forms.other.validators.PrescriberFormValidation;
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
public class PharmacyPrescriberManageController {
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

    @RequestMapping(value = "/module/pharmacy/prescriber/list.form", method = RequestMethod.GET)
    public void list(ModelMap modelMap) {
        if (Context.isAuthenticated()) {
            modelMap.addAttribute("prescribers", OperationUtils.getLocationPrescribers(OperationUtils.getUserLocation()));
            modelMap.addAttribute("title", "Liste des Prescripteurs");
        }
    }

    @RequestMapping(value = "/module/pharmacy/prescriber/edit.form", method = RequestMethod.GET)
    public void edit(ModelMap modelMap,
                     @RequestParam(value = "id", defaultValue = "0", required = false) Integer id,
                     PrescriberForm prescriberForm) {
        if (Context.isAuthenticated()) {
            Provider provider = new Provider();
            if (id != 0) {
                provider = Context.getProviderService().getProvider(id);
                if (provider != null) {
                    prescriberForm.setPrescriber(provider);
                }
            } else {
                prescriberForm = new PrescriberForm();
            }

            modelMap.addAttribute("prescriberForm", prescriberForm);
            modelMap.addAttribute("locations", Context.getLocationService().getAllLocations());
            modelMap.addAttribute("title", "Saisie de Prescripteurs");
        }
    }

    @RequestMapping(value = "/module/pharmacy/prescriber/edit.form", method = RequestMethod.POST)
    public String save(ModelMap modelMap,
                       HttpServletRequest request,
                       PrescriberForm prescriberForm,
                       BindingResult result) {
        if (Context.isAuthenticated()) {
            HttpSession session = request.getSession();

            new PrescriberFormValidation().validate(prescriberForm, result);

            if (!result.hasErrors()) {
                Provider provider = prescriberForm.getPrescriber();

                Person person;
                PersonName personName;
                if (prescriberForm.getPersonId() != null) {
                    person = Context.getPersonService().getPerson(prescriberForm.getPersonId());
                    personName = person.getPersonName();
                    personName.setFamilyName(prescriberForm.getFamilyName());
                    personName.setGivenName(prescriberForm.getGivenName());
                    Context.getPersonService().savePersonName(personName);
                } else {
                    person = new Person();
                    personName = new PersonName();
                    personName.setFamilyName(prescriberForm.getFamilyName());
                    personName.setGivenName(prescriberForm.getGivenName());
                    personName.setPreferred(true);
                    person.addName(personName);
                }
                person.setGender(prescriberForm.getGender());

                provider.setPerson(Context.getPersonService().savePerson(person));

                boolean idExist = (prescriberForm.getPrescriber().getProviderId() != null);
                // System.out.println("--------------- Prescriber " + prescriberForm.getPrescriber());
                Context.getProviderService().saveProvider(provider);
                if (!idExist) {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Precripteur ajouté avec succès");
                } else {
                    session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Precripteur modifié avec succès");
                }
                return "redirect:/module/pharmacy/prescriber/list.form";
            }

            modelMap.addAttribute("prescriberForm", prescriberForm);
            modelMap.addAttribute("locations", OperationUtils.getUserLocations());
            modelMap.addAttribute("title", "Saisie de Prescripteurs");
        }

        return null;
    }


    @RequestMapping(value = "/module/pharmacy/prescriber/delete.form", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", defaultValue = "0", required = false) Integer id) {
        if (!Context.isAuthenticated())
            return null;

        if (id != 0) {
            Provider provider = Context.getProviderService().getProvider(id);
            if (provider != null) {
                Context.getProviderService().retireProvider(provider, "deleted manually by user");
            }
        }

        return "redirect:/module/pharmacy/product/units/list.form";
    }
}
