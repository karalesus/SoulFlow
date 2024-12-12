package ru.rutmiit.models;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    private UserRoles name;

    public Role(UserRoles name) {
        this.name = name;
    }

    protected Role() {
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true)
    public UserRoles getName() {
        return name;
    }

    public void setName(UserRoles name) {
        this.name = name;
    }
}
