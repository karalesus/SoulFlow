package ru.rutmiit.models;

import jakarta.persistence.*;
import ru.rutmiit.models.compositeKeys.MemberSessionKeys;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "session_registration")
public class SessionRegistration {

    private MemberSessionKeys id;

    private LocalDateTime registrationDate;

    private Status status;
    private BigDecimal finalPrice;

    @EmbeddedId
    public MemberSessionKeys getId() {
        return id;
    }

    public void setId(MemberSessionKeys id) {
        this.id = id;
    }

    @Column(name = "registration_date", nullable = false)
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column(name = "final_price", nullable = false)
    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public String toString() {
        return "SessionRegistration{" +
                "sessionId=" + id +
                ", registrationDate=" + registrationDate +
                ", status=" + status +
                '}';
    }
}
