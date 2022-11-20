package com.woowacourse.f12.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.auth.AuthService;
import com.woowacourse.f12.application.auth.token.JwtProvider;
import com.woowacourse.f12.application.inventoryproduct.InventoryProductService;
import com.woowacourse.f12.application.member.MemberService;
import com.woowacourse.f12.application.product.ProductService;
import com.woowacourse.f12.application.review.ReviewService;
import com.woowacourse.f12.config.LoggingConfig;
import com.woowacourse.f12.logging.ApiQueryCounter;
import com.woowacourse.f12.presentation.auth.AuthController;
import com.woowacourse.f12.presentation.auth.RefreshTokenCookieProvider;
import com.woowacourse.f12.presentation.inventoryproduct.InventoryProductController;
import com.woowacourse.f12.presentation.member.MemberController;
import com.woowacourse.f12.presentation.product.ProductController;
import com.woowacourse.f12.presentation.review.ReviewController;
import com.woowacourse.f12.support.AuthTokenExtractor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import({AuthTokenExtractor.class, JwtProvider.class, RestDocsConfig.class, LoggingConfig.class, ApiQueryCounter.class,
        RefreshTokenCookieProvider.class})
@WebMvcTest({AuthController.class, ReviewController.class, ProductController.class, InventoryProductController.class,
        MemberController.class, ReviewController.class})
public class PresentationTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected ReviewService reviewService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected InventoryProductService inventoryProductService;

    @MockBean
    protected MemberService memberService;

    protected final ObjectMapper objectMapper;

    public PresentationTest() {
        this.objectMapper = new ObjectMapper();
    }
}
