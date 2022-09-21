package com.woowacourse.f12.domain.product;

import com.woowacourse.f12.domain.review.Review;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(name = "NAME_UNIQUE", columnNames = {"name"})})
@Builder
@Getter
public class Product {

    private static final int MAXIMUM_IMAGE_URL_LENGTH = 15000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url", nullable = false, length = MAXIMUM_IMAGE_URL_LENGTH)
    private String imageUrl;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Column(name = "total_rating", nullable = false)
    private int totalRating;

    @Column(name = "avg_rating", nullable = false)
    private double avgRating;

    @Column(name = "category", nullable = false, length = 8)
    @Enumerated(value = EnumType.STRING)
    private Category category;

    protected Product() {
    }

    private Product(final Long id, final String name, final String imageUrl, final int reviewCount,
                    final int totalRating, final double avgRating, final Category category) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.reviewCount = reviewCount;
        this.totalRating = totalRating;
        this.avgRating = avgRating;
        this.category = category;
    }

    public boolean isSameCategory(final Category category) {
        return this.category == category;
    }

    public void reflectReview(final Review review) {
        reviewCount++;
        totalRating += review.getRating();
        avgRating = (double) totalRating / reviewCount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        final Product product = (Product) o;
        return Objects.equals(id, product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
