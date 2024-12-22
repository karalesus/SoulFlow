package ru.rutmiit.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.rutmiit.models.compositeKeys.MemberSessionKeys;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class Review {

    private MemberSessionKeys id;

    private int rate;
    private String comment;

    private LocalDateTime date;

    public Review(MemberSessionKeys id, int rate, String comment, LocalDateTime date) {
        this.id = id;
        this.rate = rate;
        this.comment = comment;
        this.date = date;
    }

    protected Review() {
    }

    @EmbeddedId
    public MemberSessionKeys getId() {
        return id;
    }


    public void setId(MemberSessionKeys id) {
        this.id = id;
    }

    @Column(name = "rate", nullable = false)
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Column(name = "comment", columnDefinition = "TEXT")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
