package org.openmrs.module.pharmacy.forms.product.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.entities.ProductProgram;
import org.openmrs.module.pharmacy.api.ProductProgramService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {ProductProgram.class}, order = 50)
public class ProductProgramFormValidation implements Validator {
    @Override
    public boolean supports(Class aClass) {
        return aClass.equals(ProductProgram.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductProgram program = (ProductProgram) o;

        if (program == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "name", null, "Ce champ est requis");

            if (!program.getName().isEmpty()) {
                ProductProgram otherProgram = Context.getService(ProductProgramService.class).getOneProductProgramByName(program.getName());
                if (otherProgram != null) {
                    if (program.getProductProgramId() == null) {
                        errors.rejectValue("name", "Ce programme existe déjà !");
                    } else {
                        if (!otherProgram.getProductProgramId().equals(program.getProductProgramId())) {
                            errors.rejectValue("name", "Ce programme existe déjà !");
                        }
                    }
                }
            }
        }
    }
}
