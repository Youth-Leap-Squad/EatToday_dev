package com.eat.today.sns.command.application.controller.dm;

import com.eat.today.post.command.application.dto.FoodPostResponse;
import com.eat.today.post.command.application.dto.UpdateFoodPostRequest;
import com.eat.today.sns.command.application.dto.photoReviewDTO.UpdateRequest;
import com.eat.today.sns.command.application.entity.dm.DmEntity;
import com.eat.today.sns.command.domain.repository.dm.DmRepository;
import com.eat.today.sns.command.domain.service.DmService;
import com.eat.today.sns.query.dto.dm.DmSendDTO;
import com.eat.today.sns.query.dto.dm.DmUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PatchMapping(value = "/{messageNo}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> update(@PathVariable int messageNo,
                                    @RequestBody DmUpdateDTO req) {
        try {
            int affected = dmService.updateDm(messageNo, req);
            return ResponseEntity.ok(Map.of("updated", affected));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage())); // 권한 없음
        }
    }

    @DeleteMapping("/delete/{messageNo}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int messageNo) {
        dmService.deleteMessage(messageNo);
        return ResponseEntity.noContent().build();
    }
}