package com.woowacourse.f12.presentation;

import com.woowacourse.f12.dto.response.ExceptionResponse;
import com.woowacourse.f12.exception.InvalidValueException;
import com.woowacourse.f12.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버 오류가 발생했습니다";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidValueException(final InvalidValueException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnhandledException(Exception e) {
        log.error("[ERROR]", e);
        return ResponseEntity.internalServerError().body(ExceptionResponse.from(INTERNAL_SERVER_ERROR_MESSAGE));
    }
}
