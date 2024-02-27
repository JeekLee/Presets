package org.example.presets.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.presets.core.security.CustomUserDetails;
import org.example.presets.member.dto.LogInDto;
import org.example.presets.member.dto.SignUpDto;
import org.example.presets.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class RestMemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Validated SignUpDto signUpDto) {
        memberService.signUp(signUpDto.getNickname(), signUpDto.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody @Validated LogInDto logInDto, HttpServletResponse httpServletResponse) {
        memberService.logIn(logInDto.getNickname(), logInDto.getPassword(), httpServletResponse);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logOut(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.logOut(userDetails.getMemberId());
        return ResponseEntity.ok().build();
    }
}
