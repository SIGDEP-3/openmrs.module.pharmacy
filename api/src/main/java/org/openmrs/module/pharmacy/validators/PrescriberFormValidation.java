package org.openmrs.module.pharmacy.validators;

import org.openmrs.Provider;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.forms.PrescriberForm;
import org.openmrs.module.pharmacy.forms.ProductForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {PrescriberForm.class}, order = 50)
public class PrescriberFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(PrescriberForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PrescriberForm form = (PrescriberForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "name", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "identifier", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "location", null, "Ce champ est requis");

            if (!form.getIdentifier().isEmpty()) {
                Provider provider = Context.getProviderService().getProviderByIdentifier(form.getIdentifier());
                if (provider != null) {
                    if (form.getProviderId() == null || (!form.getProviderId().equals(provider.getProviderId()))) {
                        errors.rejectValue("identifier", "Un Prescripteur possède déjà cet identifiant !");
                    }
                }
            }

        }
    }
}
