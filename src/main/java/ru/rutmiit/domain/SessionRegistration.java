package ru.rutmiit.domain;

import jakarta.persistence.*;
import ru.rutmiit.domain.compositeKeys.MemberSessionKeys;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_registration")
public class SessionRegistration {

    private MemberSessionKeys id;

    private LocalDateTime registrationDate;

    private Status status;

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
}
