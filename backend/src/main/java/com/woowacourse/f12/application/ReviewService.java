package com.woowacourse.f12.application;

import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.domain.KeyboardRepository;
import com.woowacourse.f12.domain.Member;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.domain.Review;
import com.woowacourse.f12.domain.ReviewRepository;
import com.woowacourse.f12.dto.request.ReviewRequest;
import com.woowacourse.f12.dto.response.ReviewPageResponse;
import com.woowacourse.f12.dto.response.ReviewWithProductPageResponse;
import com.woowacourse.f12.exception.KeyboardNotFoundException;
import com.woowacourse.f12.exception.MemberNotFoundException;
import com.woowacourse.f12.exception.NotAuthorException;
import com.woowacourse.f12.exception.ReviewNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final KeyboardRepository keyboardRepository;
    private final MemberRepository memberRepository;

    public ReviewService(final ReviewRepository reviewRepository, final KeyboardRepository keyboardRepository,
                         final MemberRepository memberRepository) {
        this.reviewRepository = reviewRepository;
        this.keyboardRepository = keyboardRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long save(final Long productId, final Long memberId, final ReviewRequest reviewRequest) {
        final Keyboard keyboard = keyboardRepository.findById(productId)
                .orElseThrow(KeyboardNotFoundException::new);
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final Review review = reviewRequest.toReview(keyboard, member);
        return reviewRepository.save(review)
                .getId();
    }

    public ReviewPageResponse findPageByProductId(final Long productId, final Pageable pageable) {
        validateKeyboardExists(productId);
        final Slice<Review> page = reviewRepository.findPageByProductId(productId, pageable);
        return ReviewPageResponse.from(page);
    }

    private void validateKeyboardExists(final Long productId) {
        if (!keyboardRepository.existsById(productId)) {
            throw new KeyboardNotFoundException();
        }
    }

    public ReviewWithProductPageResponse findPage(final Pageable pageable) {
        final Slice<Review> page = reviewRepository.findPageBy(pageable);
        return ReviewWithProductPageResponse.from(page);
    }

    @Transactional
    public void update(final Long reviewId, final Long memberId, final ReviewRequest updateRequest) {
        final Review target = findTarget(reviewId, memberId);
        final Review updateReview = updateRequest.toReview(target.getKeyboard(), target.getMember());
        target.update(updateReview);
    }

    @Transactional
    public void delete(final Long reviewId, final Long memberId) {
        final Review target = findTarget(reviewId, memberId);
        reviewRepository.delete(target);
    }

    private Review findTarget(final Long reviewId, final Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final Review target = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        validateAuthor(member, target);
        return target;
    }

    private void validateAuthor(final Member member, final Review target) {
        if (!target.isWrittenBy(member)) {
            throw new NotAuthorException();
        }
    }
}
