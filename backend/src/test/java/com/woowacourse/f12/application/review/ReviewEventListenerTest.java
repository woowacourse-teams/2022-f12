package com.woowacourse.f12.application.review;

import static org.mockito.Mockito.verify;

import com.woowacourse.f12.application.product.ProductDeletedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReviewEventListenerTest {

    @Autowired
    ApplicationEventPublisher publisher;

    @SpyBean
    ReviewEventListener reviewEventListener;

    @Test
    void 장비삭제_이벤트가_발생하면_이벤트_리스너가_수행된다() {
        // given
        final ProductDeletedEvent event = new ProductDeletedEvent(this, 1L);

        // when
        publisher.publishEvent(event);

        // then
        verify(reviewEventListener).handle(event);
    }

}
