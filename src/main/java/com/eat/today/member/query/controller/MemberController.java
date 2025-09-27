package com.eat.today.member.query.controller;

import com.eat.today.member.query.dto.FindMyLevelDTO;
import com.eat.today.member.query.dto.MemberDTO;
import com.eat.today.member.query.dto.ReportCheckDTO;
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
    public String getIdByPhone(@RequestParam String phone) {
        return memberService.getIdByPhone(phone);  // ex) "010-9999-9999"
    }

        @GetMapping("/getprofile")
    public MemberDTO findMyProfile(@RequestParam Integer memberNo) {
        return memberService.findMyProfile(memberNo);  // ex> member_no가 4
    }

    @GetMapping("/getreport")
    public List<ReportCheckDTO> checkReport() {
        return memberService.checkReport();
    }
    
    @GetMapping("/findmylevel")
    public FindMyLevelDTO findMyLevel(@RequestParam Integer memberNo) {
        return memberService.findMyLevel(memberNo);
    }
}
