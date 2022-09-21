package com.woowacourse.f12.domain.review;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.exception.badrequest.BlankContentException;
import com.woowacourse.f12.exception.badrequest.InvalidContentLengthException;
import com.woowacourse.f12.exception.badrequest.InvalidRatingValueException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "review")
@EntityListeners(AuditingEntityListener.class)
@Builder
@Getter
public class Review {

    private static final int MINIMUM_RATING = 1;
    private static final int MAXIMUM_RATING = 5;
    private static final int MAXIMUM_CONTENT_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = MAXIMUM_CONTENT_LENGTH)
    private String content;

    @Column(name = "rating", nullable = false)
    private int rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Review() {
    }

    private Review(final Long id, final String content, final int rating, final Product product, final Member member,
                   final LocalDateTime createdAt) {
        validateContent(content);
        validateRating(rating);
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.product = product;
        this.member = member;
        this.createdAt = createdAt;
    }

    private void validateContent(final String content) {
        if (content.isBlank()) {
            throw new BlankContentException();
        }
        if (content.length() > MAXIMUM_CONTENT_LENGTH) {
            throw new InvalidContentLengthException(MAXIMUM_CONTENT_LENGTH);
        }
    }

    private void validateRating(final int rating) {
        if (rating < MINIMUM_RATING || rating > MAXIMUM_RATING) {
            throw new InvalidRatingValueException();
        }
    }

    public boolean isWrittenBy(final Member member) {
        return this.member.equals(member);
    }

    public boolean isWrittenAbout(final Product product) {
        return this.product.equals(product);
    }

    public void update(final Review updateReview) {
        this.content = updateReview.getContent();
        this.rating = updateReview.getRating();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Review)) {
            return false;
        }
        final Review review = (Review) o;
        return Objects.equals(id, review.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
