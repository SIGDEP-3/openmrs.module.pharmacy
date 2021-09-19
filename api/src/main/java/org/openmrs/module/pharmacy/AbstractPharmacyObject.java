package org.openmrs.module.pharmacy;

import org.openmrs.BaseOpenmrsObject;

import javax.persistence.Access;
import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractPharmacyObject extends BaseOpenmrsObject implements Serializable {
    public AbstractPharmacyObject() {
        setUuid(UUID.randomUUID().toString());
    }

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer integer) {
    }

    //    @Basic
    //    @Access(AccessType.PROPERTY)
    //    @Column(name = "uuid", length = 38, unique = true, nullable = false)
    //    @Override
    //    public String getUuid() {
    //        return super.getUuid();
    //    }
    //
    //    @Override
    //    public void setUuid(String uuid) {
    //        super.setUuid(uuid);
    //    }

}
