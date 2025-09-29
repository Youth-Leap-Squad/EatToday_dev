package com.eat.today.event.albti.command.application.controller;

import com.eat.today.event.albti.command.application.dto.AlbtiJoinMemberAddRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiJoinMemberAddResponseDTO;
import com.eat.today.event.albti.command.application.service.AlbtiJoinMemberServiceAdd;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/albti/member")
public class AlbtiMemberControllerAdd {
    private final AlbtiJoinMemberServiceAdd joinMemberService;

//    @PostMapping("/member/add")
//    public String addAlbtiMember(@RequestBody AlbtiJoinMemberAddRequestDTO albtiMemberAddDTO){
//        albtiMemberServiceAdd.addAlbtiMember(
//                albtiMemberAddDTO.getMemberNo(),
//                albtiMemberAddDTO.getAlbtiNo()
//        );
//        return "술BTI 참여가 완료되었습니다!";
//    }

    @PostMapping("/add")
    public ResponseEntity<AlbtiJoinMemberAddResponseDTO> addParticipant(
            @RequestBody AlbtiJoinMemberAddRequestDTO requestDTO) {

        AlbtiJoinMemberAddResponseDTO responseDTO = joinMemberService.addParticipant(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
