package org.example.presets.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.presets.member.dto.LogInDto;
import org.example.presets.member.dto.SignUpDto;
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

import static org.example.presets.core.security.JwtUtil.HEADER_ACCESS;
import static org.example.presets.core.security.JwtUtil.HEADER_REFRESH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestMemberControllerTest {
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
    @DisplayName("회원 가입(ID, Password Validation)")
    @Order(1)
    void signUp() throws Exception {
        SignUpDto withInvalidNickname = SignUpDto.builder().nickname("JeekLee").password("1q2w3e4r!@#").build();

        SignUpDto withInvalidPassword = SignUpDto.builder().nickname("JeekLeee").password("1234").build();

        SignUpDto validRequest = SignUpDto.builder().nickname("JeekLeee").password("1q2w3e4r!@#").build();


        this.mockMvc
                .perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withInvalidNickname))
                )
                .andExpect(status().is4xxClientError());

        this.mockMvc
                .perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withInvalidPassword))
                )
                .andExpect(status().is4xxClientError());

        this.mockMvc
                .perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인")
    @Order(2)
    void logIn() throws Exception{
        LogInDto validRequest = LogInDto.builder().nickname("JeekLeee").password("1q2w3e4r!@#").build();
        LogInDto invalidRequest = LogInDto.builder().nickname("JeekLeee").password("1234").build();

        this.mockMvc
                .perform(post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().is4xxClientError());

        this.mockMvc
                .perform(post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 및 재발급 요청")
    @Order(3)
    void reissuance() throws Exception {
        LogInDto validRequest = LogInDto.builder().nickname("JeekLeee").password("1q2w3e4r!@#").build();

        MvcResult result = this.mockMvc
                .perform(post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest))
                )
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = result.getResponse().getHeader(HEADER_ACCESS);
        String refreshToken = result.getResponse().getHeader(HEADER_REFRESH);

        this.mockMvc
                .perform(post("/member/reissuance")
                        .header(HEADER_REFRESH, refreshToken)
                        .header(HEADER_ACCESS, accessToken)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 및 로그아웃, 재발급 요청")
    @Order(4)
    void logOut() throws Exception {
        LogInDto validRequest = LogInDto.builder().nickname("JeekLeee").password("1q2w3e4r!@#").build();

        MvcResult result = this.mockMvc
                .perform(post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest))
                )
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = result.getResponse().getHeader(HEADER_ACCESS);
        String refreshToken = result.getResponse().getHeader(HEADER_REFRESH);

        this.mockMvc
                .perform(post("/member/logout")
                        .header(HEADER_ACCESS, accessToken)
                )
                .andExpect(status().isOk());

        this.mockMvc
                .perform(post("/member/reissuance")
                        .header(HEADER_REFRESH, refreshToken)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}