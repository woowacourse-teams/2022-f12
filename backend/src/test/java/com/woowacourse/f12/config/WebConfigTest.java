package com.woowacourse.f12.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WebConfigTest {

    private static final String CORS_ALLOWED_METHODS = "GET,POST,HEAD,PUT,PATCH,DELETE,TRACE,OPTIONS";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void CORS가_허용되어있다() throws Exception {
        mockMvc.perform(
                        options("/api/v1/keyboards")
                                .header(HttpHeaders.ORIGIN, "http://localhost:8080")
                                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                )
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, CORS_ALLOWED_METHODS))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
                .andDo(print());
    }
}
