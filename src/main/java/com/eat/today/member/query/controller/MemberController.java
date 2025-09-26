package com.eat.today.member.query.controller;

import com.eat.today.member.query.dto.MemberDTO;
import com.eat.today.member.query.mapper.MemberMapper;
import com.eat.today.member.query.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/getid")
    public String getIdByPhone(@RequestParam String phone) {
        return memberService.getIdByPhone(phone);  // ex) "010-9999-9999"
    }

    @GetMapping("/members/getprofile")
    public MemberDTO findMyProfile(@RequestParam String memberNo) {
        return memberService.findMyProfile(memberNo);  // ex> member_no가 4
    }
}
