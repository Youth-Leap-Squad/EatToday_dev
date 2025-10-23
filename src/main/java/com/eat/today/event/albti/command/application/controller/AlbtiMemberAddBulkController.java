package com.eat.today.event.albti.command.application.controller;

import com.eat.today.event.albti.command.application.dto.AlbtiMemberAddBulkRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiMemberAddBulkResponseDTO;
import com.eat.today.event.albti.command.application.service.AlbtiMemberAddBulkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albti/member")
@RequiredArgsConstructor
public class AlbtiMemberAddBulkController {

    private final AlbtiMemberAddBulkService albtiMemberAddBulkService;

    @PostMapping("/add-bulk")
    public ResponseEntity<AlbtiMemberAddBulkResponseDTO> addAlbtiMemberBulk(
            @RequestBody AlbtiMemberAddBulkRequestDTO requestDTO
    ) {
        AlbtiMemberAddBulkResponseDTO responseDTO = albtiMemberAddBulkService.saveAllAnswers(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}