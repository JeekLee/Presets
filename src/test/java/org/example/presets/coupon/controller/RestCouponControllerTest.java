package org.example.presets.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.presets.coupon.dto.CreateCouponDto;
import org.example.presets.member.dto.LogInDto;
import org.example.presets.member.dto.SignUpDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.presets.core.security.jwt.JwtUtil.HEADER_ACCESS;
import static org.example.presets.core.security.jwt.JwtUtil.HEADER_REFRESH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestCouponControllerTest {
    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("관리자 계정 생성, 쿠폰 생성")
    @Order(2)
    void createCoupon() throws Exception {
        SignUpDto signUpRequest = SignUpDto.builder().nickname("JeekLeee").password("1q2w3e4r!@#").build();
        this.mockMvc
                .perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isOk());

        LogInDto loginRequest = LogInDto.builder().nickname("JeekLeee").password("1q2w3e4r!@#").build();
        MvcResult loginResult = this.mockMvc
                .perform(post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String accessToken = loginResult.getResponse().getHeader(HEADER_ACCESS);
        String refreshToken = loginResult.getResponse().getHeader(HEADER_REFRESH);

        CreateCouponDto createCouponDto = CreateCouponDto.builder().couponName("테스트 쿠폰").couponQuantity(10000L).build();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            executorService.submit(()-> {
                try {
                    this.mockMvc
                            .perform(post("/coupon")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(createCouponDto))
                                    .header(HEADER_REFRESH, refreshToken)
                                    .header(HEADER_ACCESS, accessToken)
                            ).andExpect(status().isOk());
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }
}