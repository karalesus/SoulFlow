package ru.rutmiit.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "difficulty")
public class Difficulty extends BaseEntity {

    private String name;
    private List<Session> sessionList;

    public Difficulty(String name) {
        this.name = name;
    }

    protected Difficulty() {}

    @Column(name = "name", length = 127, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "difficulty", targetEntity = Session.class, fetch = FetchType.LAZY)
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
