package org.openmrs.module.pharmacy.forms.dispensation.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.enumerations.DispensationType;
import org.openmrs.module.pharmacy.enumerations.PatientType;
import org.openmrs.module.pharmacy.forms.dispensation.FindPatientForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

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

            if (form.getDispensationType() != null) {
                if (form.getDispensationType().equals(DispensationType.HIV_PATIENT)) {
                    if (form.getPatientIdentifier() == null) {
                        errors.rejectValue("patientIdentifier", null, "Renseigner un numéro indentifiant pour le patient VIH SVP !");
                    } else if (form.getPatientType() == null) {
                        errors.rejectValue("patientType", null, "Sélectionner le type de patient : Site ou mMobile SIV !");

                    } else {
                        if (form.getPatientIdentifier() != null && !form.getPatientIdentifier().isEmpty()) {
                            if (form.getPatientType() != null && (form.getPatientType().equals(PatientType.ON_SITE) || form.getPatientType().equals(PatientType.MOBILE))) {
                                Pattern pattern = Pattern.compile("^[0-9]{4}/.{2}/[0-9]{2}/[0-9]{5}[E[1-9]]?$", Pattern.CASE_INSENSITIVE);
                                if (!pattern.matcher(form.getPatientIdentifier()).matches()) {
                                    errors.rejectValue("patientIdentifier", null, "Le numéro patient ne correspond pas à celui d'un patient VIH");
                                }
                            }
                        }
                    }
                }
            }

            if (form.getPatientType() != null) {
                if (form.getPatientType().equals(PatientType.ON_SITE)
                        || form.getPatientType().equals(PatientType.MOBILE)
                        || form.getPatientType().equals(PatientType.OTHER_HIV)) {
                    if (form.getPatientIdentifier() == null || form.getPatientIdentifier().isEmpty()) {
                        errors.rejectValue("patientIdentifier", null, "Le numéro patient est requis pour les patients mobiles et du site !");
                    }
                }
            }
        }

    }
}
