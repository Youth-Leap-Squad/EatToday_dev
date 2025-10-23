/* 술BTI검사 참여자 추가 기능은 새로운 회원이 설문내용에 관한 참여 기록만 남기고
 *  이후에 새로운 회원에 관한 술BTI유형은 뷰테이블을 이용한 자동계산 조회 코드를 사용하면됨.
* */

package com.eat.today.event.albti.command.application.controller;

import com.eat.today.event.albti.command.application.dto.AlbtiMemberAddRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiMemberAddResponseDTO;
import com.eat.today.event.albti.command.application.service.AlbtiMemberAddService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albti/member")
@RequiredArgsConstructor
public class AlbtiMemberAddController {

    private final AlbtiMemberAddService albtiMemberAddService;

    @PostMapping("/add")
    public ResponseEntity<AlbtiMemberAddResponseDTO> addAlbtiMember(@RequestBody AlbtiMemberAddRequestDTO albtiMemberAddRequestDTO) {
        AlbtiMemberAddResponseDTO albtiMemberAddResponseDTO = albtiMemberAddService.addAlbtiMember(albtiMemberAddRequestDTO);
        return ResponseEntity.ok(albtiMemberAddResponseDTO);
    }
}
