package org.openmrs.module.pharmacy.dto;

public class ProductUploadResumeDTO {
    private Integer totalToImport;
    private Integer totalImported;
    private Integer newImported;
    private Integer updateImported;

    public ProductUploadResumeDTO() {
        totalImported = 0;
        totalToImport = 0;
        newImported = 0;
        updateImported = 0;
    }

    public Integer getTotalToImport() {
        return totalToImport;
    }

    public void setTotalToImport(Integer totalToImport) {
        this.totalToImport = totalToImport;
    }

    public Integer getTotalImported() {
        return totalImported;
    }

    public void setTotalImported(Integer totalImported) {
        this.totalImported = totalImported;
    }

    public Integer getNewImported() {
        return newImported;
    }

    public void setNewImported(Integer newImported) {
        this.newImported = newImported;
    }

    public Integer getUpdateImported() {
        return updateImported;
    }

    public void setUpdateImported(Integer updateImported) {
        this.updateImported = updateImported;
    }

    public void addOneTotalImported() {
        this.totalImported += 1;
    }

    public void addOneNewImported() {
        this.newImported += 1;
    }

    public void addOneUpdateImported() {
        this.updateImported += 1;
    }

    @Override
    public String toString() {
        return "Resumé importation {" +
                "Total à importer =" + totalToImport +
                ", Importés =" + totalImported +
                ", Ajout =" + newImported +
                ", Mise à jour =" + updateImported +
                '}';
    }
}
