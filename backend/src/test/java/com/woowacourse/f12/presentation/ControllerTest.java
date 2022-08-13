package com.woowacourse.f12.presentation;

import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.support.AuthTokenExtractor;
import com.woowacourse.f12.support.RequestLogTimer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import({AuthTokenExtractor.class, JwtProvider.class, RestDocsConfig.class, RequestLogTimer.class})
public class ControllerTest {
}
