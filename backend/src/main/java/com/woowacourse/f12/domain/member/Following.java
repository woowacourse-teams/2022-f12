package com.woowacourse.f12.domain.member;

import com.woowacourse.f12.exception.badrequest.SelfFollowException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "following")
@EntityListeners(AuditingEntityListener.class)
@Builder
@Getter
public class Following {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "follower_id", nullable = false)
    private Long followerId;

    @Column(name = "followee_id", nullable = false)
    private Long followeeId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Following() {
    }

    private Following(final Long id, final Long followerId, final Long followeeId, final LocalDateTime createdAt) {
        validateNotSelfFollow(followerId, followeeId);
        this.id = id;
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.createdAt = createdAt;
    }

    private void validateNotSelfFollow(final Long followerId, final Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new SelfFollowException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Following)) {
            return false;
        }
        Following following = (Following) o;
        return Objects.equals(id, following.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
