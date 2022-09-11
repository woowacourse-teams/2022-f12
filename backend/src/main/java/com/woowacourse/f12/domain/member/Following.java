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

    @Column(name = "following_id", nullable = false)
    private Long followingId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Following() {
    }

    private Following(final Long id, final Long followerId, final Long followingId, final LocalDateTime createdAt) {
        validateNotSelfFollow(followerId, followingId);
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
        this.createdAt = createdAt;
    }

    private void validateNotSelfFollow(final Long followerId, final Long followingId) {
        if (validateBothNotNull(followerId, followingId) && followerId.equals(followingId)) {
            throw new SelfFollowException();
        }
    }

    private boolean validateBothNotNull(final Long followerId, final Long followingId) {
        return Objects.nonNull(followerId) && Objects.nonNull(followingId);
    }

    public boolean isFollowing(final Long memberId) {
        return followingId.equals(memberId);
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
