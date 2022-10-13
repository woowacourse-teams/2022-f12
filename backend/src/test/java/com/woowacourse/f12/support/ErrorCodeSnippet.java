package com.woowacourse.f12.support;

import com.woowacourse.f12.exception.ErrorCode;
import java.util.Map;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

public class ErrorCodeSnippet extends TemplatedSnippet {
    public ErrorCodeSnippet(ErrorCode... errorCodes) {
        super("error-code-table", Map.of("error-codes", errorCodes));
    }

    private static String codesToString(ErrorCode... errorCodes) {
        final StringBuilder builder = new StringBuilder();
        for (ErrorCode code : errorCodes) {
            builder.append(code.getValue()).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length() - 1);
        return builder.toString();
    }

    @Override
    protected Map<String, Object> createModel(final Operation operation) {
        return operation.getAttributes();
    }
}
