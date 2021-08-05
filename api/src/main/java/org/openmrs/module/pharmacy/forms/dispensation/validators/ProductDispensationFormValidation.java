package org.openmrs.module.pharmacy.forms.dispensation.validators;

import org.openmrs.Patient;
import org.openmrs.annotation.Handler;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.api.ProductDispensationService;
import org.openmrs.module.pharmacy.forms.dispensation.ProductDispensationForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.openmrs.module.pharmacy.forms.ProductOperationFormValidation;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Handler(supports = {ProductDispensationForm.class}, order = 50)
public class ProductDispensationFormValidation extends ProductOperationFormValidation {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductDispensationForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductDispensationForm form = (ProductDispensationForm) o;

        super.validate(form, errors);

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {

//            ValidationUtils.rejectIfEmpty(errors, "goal", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "treatmentDays", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "providerId", null, "Sélectionnez un prescripteur SVP !");
//            ValidationUtils.rejectIfEmpty(errors, "operationNumber", null, "Ce champ est requis");

            if (form.getOperationDate() != null){
                Patient patient = dispensationService().getPatientByIdentifier(form.getPatientIdentifier());
                if (patient != null) {
                    if (dispensationService().isDead(patient,OperationUtils.getUserLocation())){
                        if (form.getOperationDate().after(dispensationService().deathDate(patientService().getPatient(form.getPatientId()), OperationUtils.getUserLocation()))) {
                            errors.rejectValue("operationDate", null, "Le patient est décédé au moment de cette dispensation ! ");
                        }
                    }
                }
            }

            if (form.getProductProgramId() != null) {
                if (programService().getOneProductProgramById(form.getProductProgramId()).getName().equals("PNLSARVIO")) {
                    if (form.getGoal() == null) {
                        errors.rejectValue("goal", null, "Ce champ est requis");
                    }
                    if (form.getAge() == null) {
                        errors.rejectValue("age", null, "Ce champ est requis");
                    }
//                    if (form.getProductRegimenId() == null) {
//                        errors.rejectValue("productRegimenId", null, "Ce champ est requis");
//                    }
                }
            }

            if (form.getProductRegimenLine() == null) {
                errors.rejectValue("productRegimenLine", null, "Ce champ est requis");
            }

        }
    }

    private PatientService patientService() {
        return Context.getPatientService();
    }

    private ProductDispensationService dispensationService() {
        return Context.getService(ProductDispensationService.class);
    }
}
