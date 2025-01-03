package ru.rutmiit.dto.instructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

public class InstructorOutputDTO implements Serializable {

    private String id;
    private String name;
    private String certificate;
    private String photoUrl;
    private boolean isDeleted;

    public InstructorOutputDTO(String id, String name, String certificate, String photoUrl, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.certificate = certificate;
        this.photoUrl = photoUrl;
        this.isDeleted = isDeleted;
    }

    protected InstructorOutputDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return name;
    }
}
