package com.woowacourse.f12.support;

import com.woowacourse.f12.exception.ErrorCode;
import java.util.Map;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

public class ErrorCodeSnippet extends TemplatedSnippet {
    public ErrorCodeSnippet(ErrorCode... errorCodes) {
        super("error-code-table", Map.of("error-codes", errorCodes));
    }

    @Override
    protected Map<String, Object> createModel(final Operation operation) {
        return operation.getAttributes();
    }
}
