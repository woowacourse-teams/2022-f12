package com.woowacourse.f12.domain.product;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product",
        uniqueConstraints = {@UniqueConstraint(name = "NAME_UNIQUE", columnNames = {"name"})})
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
    private double rating;

    @Column(name = "category", nullable = false, length = 8)
    @Enumerated(value = EnumType.STRING)
    private Category category;

    protected Product() {
    }

    private Product(final Long id, final String name, final String imageUrl, final int reviewCount,
                    final int totalRating, final double rating, final Category category) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.reviewCount = reviewCount;
        this.totalRating = totalRating;
        this.rating = rating;
        this.category = category;
    }

    public boolean isSameCategory(final Category category) {
        return this.category == category;
    }

    public void increaseReviewCount() {
        reviewCount++;
    }

    public void decreaseReviewCount() {
        reviewCount--;
    }

    public void increaseRating(final int rating) {
        this.totalRating += rating;
        calculateRating();
    }

    public void decreaseRating(final int rating) {
        this.totalRating -= rating;
        calculateRating();
    }

    private void calculateRating() {
        if (reviewCount == 0) {
            rating = 0;
            return;
        }
        rating = (double) totalRating / reviewCount;
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
