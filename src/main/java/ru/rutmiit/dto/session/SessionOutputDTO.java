package ru.rutmiit.dto.session;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SessionOutputDTO implements Serializable {

    private String id;
    private String name;
    private int duration;
    private String description;
    private LocalDateTime dateTime;
    private int maxCapacity;
    private BigDecimal price;
    private String difficultyName;
    private String typeName;
    private String instructor;

    public SessionOutputDTO(String id, String name, int duration, String description, LocalDateTime dateTime, int maxCapacity, BigDecimal price, String difficultyName, String typeName, String instructor) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.dateTime = dateTime;
        this.maxCapacity = maxCapacity;
        this.price = price;
        this.difficultyName = difficultyName;
        this.typeName = typeName;
        this.instructor = instructor;
    }

    protected SessionOutputDTO() {
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDifficultyName() {
        return difficultyName;
    }

    public void setDifficultyName(String difficultyName) {
        this.difficultyName = difficultyName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
