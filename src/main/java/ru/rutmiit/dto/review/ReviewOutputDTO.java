package ru.rutmiit.dto.review;

import java.time.LocalDateTime;

public class ReviewOutputDTO {

    private String memberId;
    private String sessionId;
    private Integer rate;
    private String comment;

    private LocalDateTime date;

    public ReviewOutputDTO(String memberId, String sessionId, Integer rate, String comment, LocalDateTime date) {
        this.memberId = memberId;
        this.sessionId = sessionId;
        this.rate = rate;
        this.comment = comment;
        this.date = date;
    }

    protected ReviewOutputDTO() {
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
