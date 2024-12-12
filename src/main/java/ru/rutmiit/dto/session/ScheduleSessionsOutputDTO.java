package ru.rutmiit.dto.session;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ScheduleSessionsOutputDTO {

    private String id;
    private String name;
    private int duration;
    private String description;
    private LocalDateTime dateTime;
    private Long availableSpots;
    private BigDecimal priceBeforeDiscount;
    private BigDecimal priceAfterDiscount;
    private String difficulty;
    private String type;
    private String instructorName;

    public ScheduleSessionsOutputDTO(String id, String name, int duration, String description, LocalDateTime dateTime, Long availableSpots, BigDecimal priceBeforeDiscount, BigDecimal priceAfterDiscount, String difficulty, String type, String instructorName) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.dateTime = dateTime;
        this.availableSpots = availableSpots;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.priceAfterDiscount = priceAfterDiscount;
        this.difficulty = difficulty;
        this.type = type;
        this.instructorName = instructorName;
    }

    protected ScheduleSessionsOutputDTO() {
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

    public Long getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Long availableSpots) {
        this.availableSpots = availableSpots;
    }

    public BigDecimal getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public void setPriceBeforeDiscount(BigDecimal priceBeforeDiscount) {
        this.priceBeforeDiscount = priceBeforeDiscount;
    }

    public BigDecimal getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(BigDecimal priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
}
