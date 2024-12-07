package ru.rutmiit.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "session")
public class Session extends BaseEntity {

    private String name;
    private int duration;
    private String description;
    private LocalDateTime dateTime;
    private int maxCapacity;
    private BigDecimal price;
    private Difficulty difficulty;
    private Type type;
    private Instructor instructor;

    public Session(String name, int duration, String description, LocalDateTime dateTime, int maxCapacity, BigDecimal price, Difficulty difficulty, Type type, Instructor instructor) {
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.dateTime = dateTime;
        this.maxCapacity = maxCapacity;
        this.price = price;
        this.difficulty = difficulty;
        this.type = type;
        this.instructor = instructor;
    }

    protected Session() {
    }

    @Column(name = "name", length = 127, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "duration", nullable = false)
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Column(name = "description",  columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "date_time", nullable = false)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "max_capacity", nullable = false)
    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Column(name = "price", nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ManyToOne
    @JoinColumn(name = "difficulty_id", nullable = false)
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "Session{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", maxCapacity=" + maxCapacity +
                ", price=" + price +
                ", difficulty=" + difficulty +
                ", type=" + type +
                ", instructor=" + instructor +
                '}';
    }
}
