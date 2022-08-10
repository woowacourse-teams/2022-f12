package com.woowacourse.f12.domain.member;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.product.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;
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

    @Column(name = "career_level")
    @Enumerated(EnumType.STRING)
    private CareerLevel careerLevel;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Builder.Default
    @BatchSize(size = 150)
    @OneToMany(mappedBy = "member")
    private List<InventoryProduct> inventoryProducts = new ArrayList<>();

    protected Member() {
    }

    private Member(final Long id, final String gitHubId, final String name, final String imageUrl,
                   final CareerLevel careerLevel, final JobType jobType,
                   final List<InventoryProduct> inventoryProducts) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.inventoryProducts = inventoryProducts;
    }

    public void updateName(final String name) {
        this.name = name;
    }

    public void updateCareerLevel(final CareerLevel careerLevel) {
        this.careerLevel = careerLevel;
    }

    public void updateJobType(final JobType jobType) {
        this.jobType = jobType;
    }

    public boolean isRegisterCompleted() {
        return Objects.nonNull(this.careerLevel) && Objects.nonNull(this.jobType);
    }

    public List<Product> getProfileProducts() {
        return this.inventoryProducts.stream()
                .filter(InventoryProduct::isSelected)
                .map(InventoryProduct::getProduct)
                .collect(Collectors.toList());
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
