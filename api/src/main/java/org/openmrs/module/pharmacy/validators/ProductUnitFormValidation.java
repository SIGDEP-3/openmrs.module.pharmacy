package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductUnit;
import org.openmrs.module.pharmacy.api.ProductUnitService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {ProductUnit.class}, order = 50)
public class ProductUnitFormValidation implements Validator {
    @Override
    public boolean supports(Class aClass) {
        return aClass.equals(ProductUnit.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductUnit unit = (ProductUnit) o;

        if (unit == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "name", null, "Ce champ est requis");

            if (!unit.getName().isEmpty()) {
                ProductUnit otherUnit = Context.getService(ProductUnitService.class).getOneProductUnitByName(unit.getName());
                if (otherUnit != null) {
                    if (unit.getProductUnitId() == null) {
                        errors.rejectValue("name", "Cette unité existe déjà !");
                    } else {
                        if (!otherUnit.getProductUnitId().equals(unit.getProductUnitId())) {
                            errors.rejectValue("name", "Cette unité existe déjà !");
                        }
                    }
                }
            }
        }
    }
}
