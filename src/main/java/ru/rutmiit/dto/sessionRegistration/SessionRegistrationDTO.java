package ru.rutmiit.dto.sessionRegistration;

import java.math.BigDecimal;
import java.util.UUID;

public class SessionRegistrationDTO {

    private UUID memberId;
    private UUID sessionId;
    private BigDecimal finalPrice;

    public SessionRegistrationDTO(UUID memberId, UUID sessionId, BigDecimal finalPrice) {
        this.memberId = memberId;
        this.sessionId = sessionId;
        this.finalPrice = finalPrice;
    }

    protected SessionRegistrationDTO() {
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public String toString() {
        return "SessionRegistrationDTO{" +
                "memberId='" + memberId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
