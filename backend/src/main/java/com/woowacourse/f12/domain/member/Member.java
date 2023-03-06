package com.woowacourse.f12.domain.member;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "member",
        uniqueConstraints = {@UniqueConstraint(name = "member_github_id_unique", columnNames = {"github_id"})})
@EntityListeners(AuditingEntityListener.class)
@Builder
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "github_id", nullable = false)
    private String gitHubId;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url", length = 65535, nullable = false)
    private String imageUrl;

    @Column(name = "registered", nullable = false)
    private boolean registered;

    @Column(name = "career_level")
    @Enumerated(EnumType.STRING)
    private CareerLevel careerLevel;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(name = "follower_count", nullable = false)
    private int followerCount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;

    protected Member() {
    }

    private Member(final Long id, final String gitHubId, final String name, final String imageUrl,
                   final boolean registered, final CareerLevel careerLevel, final JobType jobType,
                   final int followerCount, final Role role) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.registered = registered;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.followerCount = followerCount;
        this.role = role;
    }

    public void update(final Member updateMember) {
        updateName(updateMember.name);
        updateImageUrl(updateMember.imageUrl);
        updateCareerLevel(updateMember.careerLevel);
        updateJobType(updateMember.jobType);
        updateRegistered(updateMember.registered);
    }

    private void updateName(final String name) {
        if (name != null) {
            this.name = name;
        }
    }

    private void updateImageUrl(String imageUrl) {
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }

    private void updateCareerLevel(final CareerLevel careerLevel) {
        if (careerLevel != null) {
            this.careerLevel = careerLevel;
        }
    }

    private void updateJobType(final JobType jobType) {
        if (jobType != null) {
            this.jobType = jobType;
        }
    }

    private void updateRegistered(final boolean registered) {
        if (this.registered) {
            return;
        }
        this.registered = registered;
    }

    public boolean isSameId(final Long id) {
        return Objects.equals(id, this.id);
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        final Member member = (Member) o;
        return Objects.equals(id, member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
