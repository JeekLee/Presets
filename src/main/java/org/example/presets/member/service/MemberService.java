package org.example.presets.member.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
    void signUp(String nickname, String password);
    void logIn(String nickname, String password, HttpServletResponse httpServletResponse);
    void logOut(Long memberId);
    void tokenReissuance(HttpServletRequest request, HttpServletResponse response);
}
