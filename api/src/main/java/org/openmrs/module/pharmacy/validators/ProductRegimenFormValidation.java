package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductRegimen;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductRegimenService;
import org.openmrs.module.pharmacy.forms.ProductRegimenForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {ProductRegimenForm.class}, order = 50)
public class ProductRegimenFormValidation implements Validator {
    @Override
    public boolean supports(Class aClass) {
        return aClass.equals(ProductRegimenForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductRegimenForm regimen = (ProductRegimenForm) o;

        if (regimen == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "conceptId", null, "Ce champ est requis");

            if (regimen.getConceptId() != null) {
                ProductRegimen otherRegimen = Context.getService(ProductRegimenService.class).getOneProductRegimenByConceptId(regimen.getConceptId());
                if (otherRegimen != null) {
                    if (regimen.getProductRegimenId() == null) {
                        errors.rejectValue("conceptId", "Ce Régime existe déjà !");
                    } else {
                        if (!otherRegimen.getConcept().getConceptId().equals(regimen.getConceptId())) {
                            errors.rejectValue("conceptId", "Ce Régime existe déjà !");
                        }
                    }
                }
            }
        }
    }
}
