package com.woowacourse.f12.domain.member;

import static com.woowacourse.f12.domain.member.Role.ADMIN;
import static com.woowacourse.f12.domain.member.Role.USER;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProducts;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    @Column(name = "career_level")
    @Enumerated(EnumType.STRING)
    private CareerLevel careerLevel;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Builder.Default
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = USER;

    @Builder.Default
    @Embedded
    private InventoryProducts inventoryProducts = new InventoryProducts();

    @Formula("(SELECT COUNT(1) FROM following f WHERE f.following_id = id)")
    private int followerCount;

    protected Member() {
    }

    private Member(final Long id, final String gitHubId, final String name, final String imageUrl,
                   final CareerLevel careerLevel, final JobType jobType,
                   final Role role, final InventoryProducts inventoryProducts, final int followerCount) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.role = role;
        this.inventoryProducts = inventoryProducts;
        this.followerCount = followerCount;
    }

    public void update(final Member updateMember) {
        updateName(updateMember.name);
        updateImageUrl(updateMember.imageUrl);
        updateCareerLevel(updateMember.careerLevel);
        updateJobType(updateMember.jobType);
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

    public boolean isRegisterCompleted() {
        return Objects.nonNull(this.careerLevel) && Objects.nonNull(this.jobType);
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

    public boolean isAdmin() {
        return this.role == ADMIN;
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
