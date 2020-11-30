package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.enumerations.DispensationType;
import org.openmrs.module.pharmacy.forms.FindPatientForm;
import org.openmrs.module.pharmacy.forms.ProductForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {FindPatientForm.class}, order = 50)
public class FindPatientFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(FindPatientForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        FindPatientForm form = (FindPatientForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "productProgramId", null, "Veuillez sélectionner le programme SVP !");
            ValidationUtils.rejectIfEmpty(errors, "dispensationType", null, "Veuillez sélectionner un type de dispensation");

            if (form.getProductProgramId() != null) {
                if (form.getDispensationType() != null) {
                    if (form.getDispensationType().equals(DispensationType.HIV_PATIENT)) {
                        if (form.getPatientIdentifier() == null) {
                            errors.rejectValue("patientIdentifier", "Renseigner un numéro indentifiant pour le patient VIH SVP !");
                        } else if (form.getPatientType() == null) {
                            errors.rejectValue("patientType", "Sélectionner le type de patient : Site ou mMobile SIV !");
                        }
                    }
                }
            }
        }


    }
}
