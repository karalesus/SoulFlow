package ru.rutmiit.dto.Review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewInputDTO {

    private String memberId;

    private String sessionId;

    private Integer rate; // ограничения 1..5
    private String comment;

    public ReviewInputDTO(String memberId, String sessionId, Integer rate, String comment) {
        this.memberId = memberId;
        this.sessionId = sessionId;
        this.rate = rate;
        this.comment = comment;
    }

    protected ReviewInputDTO() {
    }
    @NotNull(message = "ID занятия не может быть пустым")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    @NotNull(message = "ID участника не может быть пустым")
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @NotNull(message = "Поставьте рейтинг инструктору!")
    @Min(1)
    @Max(5)
    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
    @Size(min = 5, max = 255, message = "Отзыв должен содержать минимум 2 символа")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
