package ru.rutmiit.dto.Session;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegisteredSessionsDTO {
    private String memberId;
    private String sessionId;
    private String name;
    private LocalDateTime dateTime;
    private String instructorName;
    private BigDecimal price;

    public RegisteredSessionsDTO(String memberId, String sessionId, String name, LocalDateTime dateTime, String instructorName, BigDecimal price) {
        this.memberId = memberId;
        this.sessionId = sessionId;
        this.name = name;
        this.dateTime = dateTime;
        this.instructorName = instructorName;
        this.price = price;
    }

    protected RegisteredSessionsDTO() {
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
