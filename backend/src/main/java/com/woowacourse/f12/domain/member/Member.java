package com.woowacourse.f12.domain.member;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProducts;
import com.woowacourse.f12.exception.badrequest.InvalidFollowerCountException;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "member")
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
    @Embedded
    private InventoryProducts inventoryProducts = new InventoryProducts();

    @Builder.Default
    @Enumerated
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;

    protected Member() {
    }

    private Member(final Long id, final String gitHubId, final String name, final String imageUrl,
                   final boolean registered, final CareerLevel careerLevel, final JobType jobType,
                   final int followerCount, final InventoryProducts inventoryProducts, final Role role) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.registered = registered;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.followerCount = followerCount;
        this.inventoryProducts = inventoryProducts;
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
        if (Objects.nonNull(name)) {
            this.name = name;
        }
    }

    private void updateImageUrl(String imageUrl) {
        if (Objects.nonNull(imageUrl)) {
            this.imageUrl = imageUrl;
        }
    }

    private void updateCareerLevel(final CareerLevel careerLevel) {
        if (Objects.nonNull(careerLevel)) {
            this.careerLevel = careerLevel;
        }
    }

    private void updateJobType(final JobType jobType) {
        if (Objects.nonNull(jobType)) {
            this.jobType = jobType;
        }
    }

    public void increaseFollowerCount() {
        this.followerCount += 1;
    }

    public void decreaseFollowerCount() {
        if (this.followerCount == 0) {
            throw new InvalidFollowerCountException();
        }
        this.followerCount -= 1;
    }

    private void updateRegistered(final boolean registered) {
        if (this.registered) {
            return;
        }
        this.registered = registered;
    }

    public List<InventoryProduct> getProfileProduct() {
        return this.inventoryProducts.getProfileProducts();
    }

    public boolean contains(final InventoryProducts inventoryProducts) {
        return this.inventoryProducts.contains(inventoryProducts);
    }

    public boolean isSameId(final Long id) {
        return Objects.equals(id, this.id);
    }

    public void updateInventoryProducts(final List<InventoryProduct> values) {
        this.inventoryProducts = new InventoryProducts(values);
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
