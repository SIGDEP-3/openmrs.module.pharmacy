package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.module.pharmacy.forms.ProductPriceForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {ProductPriceForm.class}, order = 50)
public class ProductPriceFormValidation implements Validator {
    @Override
    public boolean supports(Class aClass) {
        return aClass.equals(ProductPriceForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductPriceForm price = (ProductPriceForm) o;

        if (price == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "salePrice", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "productProgramId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "productId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "purchasePrice", null, "Ce champ est requis");

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
