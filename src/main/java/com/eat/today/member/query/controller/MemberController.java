package com.eat.today.member.query.controller;

import com.eat.today.member.query.dto.FindMyLevelDTO;
import com.eat.today.member.query.dto.FindProfileDTO;
import com.eat.today.member.query.dto.MemberDTO;
import com.eat.today.member.query.service.MemberService;
import com.eat.today.member.query.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    //private final MemberServiceImpl memberServiceImpl;
    private final MemberService memberService;

    @GetMapping("/getid")
    public String getIdByEmail(@RequestParam String email) {
        return memberService.getIdByEmail(email);  // ex) "user@example.com"
    }

        @GetMapping("/getprofile-by-email")
    public FindProfileDTO findMyProfileByEmail(@RequestParam String email) {
        return memberService.findMyProfileByEmail(email);  // ex> "user@example.com"
    }
    
    @GetMapping("/findmylevel")
    public FindMyLevelDTO findMyLevel(@RequestParam Integer memberNo) {
        return memberService.findMyLevel(memberNo);
    }
}
