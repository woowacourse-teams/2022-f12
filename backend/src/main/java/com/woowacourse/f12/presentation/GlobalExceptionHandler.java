package com.woowacourse.f12.presentation;

import com.woowacourse.f12.dto.response.ExceptionResponse;
import com.woowacourse.f12.exception.badrequest.InvalidValueException;
import com.woowacourse.f12.exception.forbidden.ForbiddenMemberException;
import com.woowacourse.f12.exception.notfound.NotFoundException;
import com.woowacourse.f12.exception.unauthorized.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String REQUEST_DATA_FORMAT_ERROR_MESSAGE = "요청으로 넘어온 값이 형식에 맞지 않습니다.";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "서버 오류가 발생했습니다";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidValueException(final InvalidValueException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException() {
        return ResponseEntity.badRequest().body(ExceptionResponse.from(REQUEST_DATA_FORMAT_ERROR_MESSAGE));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(
            final MethodArgumentNotValidException exception) {
        final StringBuilder stringBuilder = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach((error) -> stringBuilder.append(error.getDefaultMessage())
                .append(System.lineSeparator()));
        return ResponseEntity.badRequest().body(ExceptionResponse.from(stringBuilder.toString()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(final UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(ForbiddenMemberException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenMemberException(final ForbiddenMemberException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionResponse.from(e));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.from(REQUEST_DATA_FORMAT_ERROR_MESSAGE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnhandledException(final Exception e) {
        log.error("[ERROR]", e);
        return ResponseEntity.internalServerError().body(ExceptionResponse.from(INTERNAL_SERVER_ERROR_MESSAGE));
    }
}
