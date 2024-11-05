package ru.rutmiit.dto;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseDTO {

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
