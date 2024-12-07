package ru.rutmiit.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "status")
public class Status extends BaseEntity{

    private String name;
    private List<SessionRegistration> sessionRegistrationsList;

    public Status(String name) {
        this.name = name;
    }

    protected Status(){}

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "status", targetEntity = SessionRegistration.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<SessionRegistration> getSessionRegistrationsList() {
        return sessionRegistrationsList;
    }

    public void setSessionRegistrationsList(List<SessionRegistration> sessionRegistrationsList) {
        this.sessionRegistrationsList = sessionRegistrationsList;
    }
}
