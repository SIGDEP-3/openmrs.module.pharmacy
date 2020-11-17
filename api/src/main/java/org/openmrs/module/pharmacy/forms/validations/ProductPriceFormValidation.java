package org.openmrs.module.pharmacy.forms.validations;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.ProductPrice;
import org.openmrs.module.pharmacy.ProductProgram;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {ProductProgram.class}, order = 50)
public class ProductPriceFormValidation implements Validator {
    @Override
    public boolean supports(Class aClass) {
        return aClass.equals(ProductPrice.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductPrice price = (ProductPrice) o;

        if (price == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "name", null, "Ce champ est requis");

//            if (!price.getSalePrice()) {
//                ProductPrice otherPrice = Context.getService(PharmacyService.class).getOneProductProgramByName(price.getSalePrice());
//                if (otherPrice != null) {
//                    if (price.getProductPriceId() == null) {
//                        errors.rejectValue("name", "Ce programme existe déjà !");
//                    } else {
//                        if (!otherPrice.getProductPriceId().equals(price.getProductPriceId())) {
//                            errors.rejectValue("name", "Ce programme existe déjà !");
//                        }
//                    }
//                }
//            }
        }
    }
}
