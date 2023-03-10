package com.woowacourse.f12.application.inventoryproduct;

import static org.mockito.BDDMockito.verify;

import com.woowacourse.f12.application.product.ProductDeletedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class InventoryProductEventListenerTest {

    @Autowired
    ApplicationEventPublisher publisher;

    @MockBean
    InventoryProductEventListener inventoryProductEventListener;

    @Test
    void 장비삭제_이벤트가_발생하면_이벤트_리스너가_수행된다() {
        // given
        final ProductDeletedEvent event = new ProductDeletedEvent(this, 1L);

        // when
        publisher.publishEvent(event);

        // then
        verify(inventoryProductEventListener).handle(event);
    }
}
