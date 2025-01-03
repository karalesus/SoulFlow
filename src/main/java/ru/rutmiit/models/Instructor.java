package ru.rutmiit.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "instructor")
public class Instructor extends BaseEntity {

    private String name;
    private String certificate;
    private String photoUrl;
    private List<Session> sessionList;
    private boolean isDeleted;

    public Instructor(String name, String certificate, String photoUrl) {
        this.name = name;
        this.certificate = certificate;
        this.photoUrl = photoUrl;
        this.isDeleted = false;
    }

    protected Instructor() {}

    @Column(name = "name", length = 127, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "certificate", columnDefinition = "TEXT", nullable = false)
    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Column(name = "photo_url", length = 512)
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @OneToMany(mappedBy = "instructor", targetEntity = Session.class, fetch = FetchType.LAZY)
    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    @Column(name = "is_deleted", nullable = false)
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return name;
    }
}
