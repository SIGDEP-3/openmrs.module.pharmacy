package org.openmrs.module.pharmacy.forms.other;

import org.openmrs.Provider;
import org.openmrs.ProviderAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.utils.OperationUtils;

import java.util.Objects;

public class PrescriberForm {
    private Integer providerId;
    private String identifier;
    private String name;
    private String location;

    public PrescriberForm() {
        location = OperationUtils.getUserLocation().getName();
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrescriber(Provider provider) {
        setProviderId(provider.getProviderId());
        setIdentifier(provider.getIdentifier());
        setName(provider.getName());
        setLocation(Objects.requireNonNull(OperationUtils.getPrescriberLocation(provider)).getName());
    }

    public Provider getPrescriber() {
        Provider provider = new Provider();
        if (getProviderId() != null) {
            provider = Context.getProviderService().getProvider(getProviderId());
        }
        provider.setName(getName());
        provider.setIdentifier(getIdentifier());

        ProviderAttribute attribute = OperationUtils.getPrescriberLocationAttribute(provider);

        if (attribute != null) {
            attribute.setValue(getLocation());
        } else {
            attribute = new ProviderAttribute();
            attribute.setValue(getLocation());
            attribute.setAttributeType(Context.getProviderService()
                    .getProviderAttributeTypeByUuid("LOCATIONPPPPPPPPPPPPPPPPPPPPPPPPPPPP"));
        }
        provider.addAttribute(attribute);

        return provider;
    }
}
