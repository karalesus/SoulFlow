package ru.rutmiit.dto.Session;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UpcomingSessionOutputDTO {

    private String id;
    private String name;
    private int duration;
    private String description;
    private LocalDateTime dateTime;
    private Long availableSpots;
    private BigDecimal price;
    private String difficulty;
    private String type;
    private String instructorName;

    public UpcomingSessionOutputDTO(String id, String name, int duration, String description, LocalDateTime dateTime, String instructorName, String type, String difficulty, Long availableSpots) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.dateTime = dateTime;
        this.instructorName = instructorName;
        this.type = type;
        this.difficulty = difficulty;
        this.availableSpots = availableSpots;
    }

    protected UpcomingSessionOutputDTO() {
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

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Long getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Long availableSpots) {
        this.availableSpots = availableSpots;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
