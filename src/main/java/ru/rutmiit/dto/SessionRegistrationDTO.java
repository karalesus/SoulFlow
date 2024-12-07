package ru.rutmiit.dto;

import java.util.UUID;

public class SessionRegistrationDTO {

    private UUID memberId;
    private UUID sessionId;

    public SessionRegistrationDTO(UUID memberId, UUID sessionId) {
        this.memberId = memberId;
        this.sessionId = sessionId;
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

    @Override
    public String toString() {
        return "SessionRegistrationDTO{" +
                "memberId='" + memberId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
