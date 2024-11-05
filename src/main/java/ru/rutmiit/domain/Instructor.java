package ru.rutmiit.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "instructor")
public class Instructor extends BaseEntity {

    private String name;
    private String certificate;
//    private BigDecimal rating; // ограничение 1..5. Вероятно убрать
    private String photoUrl;
    private List<Session> sessionList;

    public Instructor(String name, String certificate, String photoUrl) {
        this.name = name;
        this.certificate = certificate;
        this.photoUrl = photoUrl;
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

//    @Column(name = "rating", precision = 3, scale = 2, nullable = false)
//    public BigDecimal getRating() {
//        return rating;
//    }
//
//    public void setRating(BigDecimal rating) {
//        this.rating = rating;
//    }

    @Column(name = "photo_url", length = 512)
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @OneToMany(mappedBy = "instructor", targetEntity = Session.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
    }
}
