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
    private String familyName;
    private String givenName;
    private String gender;
    private String location;
    private Integer personId;

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

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public void setPrescriber(Provider provider) {
        setProviderId(provider.getProviderId());
        setIdentifier(provider.getIdentifier());
        setName(provider.getName());
        setFamilyName(provider.getPerson().getFamilyName());
        setGivenName(provider.getPerson().getGivenName());
        setGender(provider.getPerson().getGender());
        setPersonId(provider.getPerson().getPersonId());
        setLocation(Objects.requireNonNull(OperationUtils.getPrescriberLocation(provider)).getName());
    }

    public Provider getPrescriber() {
        Provider provider = new Provider();
        if (getProviderId() != null) {
            provider = Context.getProviderService().getProvider(getProviderId());
        }

        // provider.setName(getFamilyName() + " " + getGivenName());

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
