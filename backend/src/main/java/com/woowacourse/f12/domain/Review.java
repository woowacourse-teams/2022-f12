package com.woowacourse.f12.domain;

import com.woowacourse.f12.exception.BlankContentException;
import com.woowacourse.f12.exception.InvalidContentLengthException;
import com.woowacourse.f12.exception.InvalidRatingValueException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "review")
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Review {

    private static final int MINIMUM_RATING = 1;
    private static final int MAXIMUM_RATING = 5;
    private static final int MAXIMUM_CONTENT_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "content", nullable = false, length = MAXIMUM_CONTENT_LENGTH)
    private String content;

    @Column(name = "rating", nullable = false)
    private int rating;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Review() {
    }

    @Builder
    private Review(final Long id, final Long productId, final String content, final int rating) {
        validateRating(rating);
        validateContent(content);
        this.id = id;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Review review = (Review) o;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
