package com.woowacourse.f12.support;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.woowacourse.f12.exception.ErrorCode;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class ErrorCodeDocumentationSupport {

    public static ResponseFieldsSnippet makeErrorCodeSnippets(ErrorCode... errorCodes) {
        return responseFields(
                fieldWithPath("errorCode").description(codesToString(errorCodes)),
                fieldWithPath("message").ignored());
    }

    private static String codesToString(ErrorCode... errorCodes) {
        final StringBuilder builder = new StringBuilder();
        for (ErrorCode code : errorCodes) {
            builder.append(code.getValue()).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length() - 1);
        return builder.toString();
    }
}
