package ru.rutmiit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.rutmiit.domain.BaseEntity;

@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    private String name;

    public Role(String name) {
        this.name = name;
    }

    protected Role() {
    }

    @Column(name = "name", unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
