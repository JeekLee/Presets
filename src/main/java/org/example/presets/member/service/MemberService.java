package org.example.presets.member.service;


import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
    public void signUp(String nickname, String password);

    public void logIn(String nickname, String password, HttpServletResponse httpServletResponse);

    public void logOut(Long memberId);
}
