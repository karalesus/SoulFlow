package ru.rutmiit.domain.compositeKeys;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ru.rutmiit.domain.User;
import ru.rutmiit.domain.Session;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MemberSessionKeys implements Serializable {

    private User member;
    private Session session;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    public User getMember() {
        return member;
    }

    public void setMember(User user) {
        this.member = user;
    }

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "MemberSessionKeys{" +
                "member=" + member +
                ", session=" + session +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberSessionKeys that = (MemberSessionKeys) o;
        return Objects.equals(member, that.member) && Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, session);
    }
}
