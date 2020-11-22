package org.openmrs.module.pharmacy.validators;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductInventory;
import org.openmrs.module.pharmacy.api.ProductInventoryService;
import org.openmrs.module.pharmacy.forms.ProductInventoryForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Handler(supports = {ProductInventoryForm.class}, order = 50)
public class ProductInventoryHeaderFormValidation implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ProductInventoryForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductInventoryForm form = (ProductInventoryForm) o;

        if (form == null) {
            errors.reject("pharmacy", "general.error");
        } else {
            ValidationUtils.rejectIfEmpty(errors, "productProgramId", null, "Ce champ est requis");
            ValidationUtils.rejectIfEmpty(errors, "operationDate", null, "Ce champ est requis");

            if (form.getOperationDate() != null) {
                List<ProductInventory> productInventories =  Context.getService(ProductInventoryService.class).getAllProductInventories(
                        Context.getLocationService().getDefaultLocation(), false, form.getOperationDate(), form.getOperationDate()
                );
                for (ProductInventory reception : productInventories) {
                    if (!reception.getProductOperationId().equals(form.getProductOperationId())) {
                        if (reception.getOperationDate().equals(form.getOperationDate()) &&
                                reception.getProductProgram().getProductProgramId().equals(form.getProductProgramId())) {
                            errors.rejectValue("operationDate", "Un produit possédant ce code existe déjà !");
                            break;
                        }
                    }
                }
            }
        }


    }
}
