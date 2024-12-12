package ru.rutmiit.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "type")
public class Type extends BaseEntity{

    private String name;

    private List<Session> sessionList;

    public Type(String name) {
        this.name = name;
    }

    protected Type() {
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "type", targetEntity = Session.class, fetch = FetchType.LAZY)
    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    @Override
    public String toString() {
        return name;
    }
}
