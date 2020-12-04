package org.openmrs.module.pharmacy.validators;

import org.apache.commons.validator.util.ValidatorUtils;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.Product;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.openmrs.module.pharmacy.api.ProductInventoryService;
import org.openmrs.module.pharmacy.api.ProductService;
import org.openmrs.module.pharmacy.forms.ProductForm;
import org.openmrs.module.pharmacy.utils.OperationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {ProductForm.class}, order = 50)
public class ProductFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductForm form = (ProductForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "code", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "code", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "retailName", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "wholesaleName", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "productRetailUnitId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "productWholesaleUnitId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "unitConversion", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "productProgramIds", null, "Ce champ est requis");

            if (!form.getCode().isEmpty()) {
                Product product = Context.getService(ProductService.class).getOneProductByCode(form.getCode());
                if (product != null) {
                    if (form.getProductId() == null) {
                        errors.rejectValue("code", "Un produit possédant ce code existe déjà !");
                    } else {
                        if (!form.getProductId().equals(product.getProductId())) {
                            errors.rejectValue("code", "Un produit possédant ce code existe déjà !");
                        }

                    }
                }
            }

        }
    }
}
