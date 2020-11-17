package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductProgram;
import org.openmrs.module.pharmacy.ProductSupplier;
import org.openmrs.module.pharmacy.api.PharmacyService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {ProductProgram.class}, order = 50)
public class ProductSupplierFormValidation implements Validator {
    @Override
    public boolean supports(Class aClass) {
        return aClass.equals(ProductSupplier.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductSupplier supplier = (ProductSupplier) o;

        if (supplier == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "name", null, "Ce champ est requis");

            if (!supplier.getName().isEmpty()) {
                ProductSupplier otherSupplier = Context.getService(PharmacyService.class).getOneProductSupplierByName(supplier.getName());
                if (otherSupplier != null) {
                    if (supplier.getProductSupplierId() == null) {
                        errors.rejectValue("name", "Ce fournisseur existe déjà !");
                    } else {
                        if (!otherSupplier.getProductSupplierId().equals(supplier.getProductSupplierId())) {
                            errors.rejectValue("name", "Ce fournisseur existe déjà !");
                        }
                    }
                }
            }
        }
    }
}
