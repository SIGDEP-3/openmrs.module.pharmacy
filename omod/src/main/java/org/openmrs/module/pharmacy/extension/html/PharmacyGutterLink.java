package org.openmrs.module.pharmacy.extension.html;

import org.openmrs.api.context.Context;
import org.openmrs.module.web.extension.LinkExt;

public class PharmacyGutterLink extends LinkExt {
    @Override
    public String getLabel() {
        return Context.getMessageSourceService().getMessage("pharmacy.gutterTitle");
    }

    @Override
    public String getUrl() {
        return "module/pharmacy/manage.form";
    }

    @Override
    public String getRequiredPrivilege() {
        return "Manage Pharmacy";
    }
}
