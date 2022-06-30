package com.woowacourse.f12.presentation;

import com.woowacourse.f12.application.KeyboardService;
import com.woowacourse.f12.dto.response.KeyboardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/keyboards")
public class KeyboardController {

    private final KeyboardService keyboardService;

    public KeyboardController(final KeyboardService keyboardService) {
        this.keyboardService = keyboardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeyboardResponse> show(@PathVariable final Long id) {
        return ResponseEntity.ok().body(keyboardService.findById(id));
    }
}
