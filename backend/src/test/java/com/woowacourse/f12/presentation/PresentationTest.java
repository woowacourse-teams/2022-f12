package com.woowacourse.f12.presentation;

import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.config.LoggingConfig;
import com.woowacourse.f12.logging.ApiQueryCounter;
import com.woowacourse.f12.support.AuthTokenExtractor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import({AuthTokenExtractor.class, JwtProvider.class, RestDocsConfig.class, LoggingConfig.class, ApiQueryCounter.class})
public class PresentationTest {
}
