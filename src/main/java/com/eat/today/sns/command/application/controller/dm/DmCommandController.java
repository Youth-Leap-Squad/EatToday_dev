package com.eat.today.sns.command.application.controller.dm;

import com.eat.today.sns.command.application.entity.dm.DmEntity;
import com.eat.today.sns.command.domain.repository.dm.DmRepository;
import com.eat.today.sns.command.domain.service.DmService;
import com.eat.today.sns.query.dto.dm.DmSendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command/dm")
@RequiredArgsConstructor
public class DmCommandController {
    private final DmService dmService;
    private final DmRepository dmRepository;

    @PostMapping("/send")
    public ResponseEntity<DmEntity> sendDm(@RequestBody DmSendDTO req) {
        DmEntity dm = dmService.sendDm(req.getSender(), req.getReceiver(), req.getContent());
        return ResponseEntity.ok(dm);
    }

    @DeleteMapping("/delete/{messageNo}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int messageNo) {
        dmService.deleteMessage(messageNo);
        return ResponseEntity.noContent().build();
    }
}