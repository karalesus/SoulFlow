package ru.rutmiit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.rutmiit.domain.compositeKeys.MemberSessionKeys;

@Entity
@Table(name = "review")
public class Review {

    private MemberSessionKeys id;

    private int rate; // ограничения 1..5
    private String comment;

    public Review(MemberSessionKeys id, int rate, String comment) {
        this.id = id;
        this.rate = rate;
        this.comment = comment;
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
}
