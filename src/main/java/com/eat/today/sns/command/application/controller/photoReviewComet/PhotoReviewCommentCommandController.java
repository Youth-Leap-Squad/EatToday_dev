package com.eat.today.sns.command.application.controller.photoReviewComet;

import com.eat.today.sns.command.domain.service.PhotoReviewCommentCommandService;
import com.eat.today.sns.query.dto.photoReviewComment.PrcDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/command/prc")
@RequiredArgsConstructor
public class PhotoReviewCommentCommandController {

    private final PhotoReviewCommentCommandService service;

    // 삽입
    @PostMapping(path = "/reviews/{reviewNo}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<PrcDTO.CreateResponse> create(
            @PathVariable int reviewNo,
            @RequestBody PrcDTO.CreateRequest req) {

        var res = service.create(req.getMemberNo(), reviewNo, req);
        return ResponseEntity
                .created(URI.create("/command/prc/" + res.getPrcNo()))
                .body(res);
    }

    // 수정
    @PatchMapping(path = "/{prcNo}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> update(
            @PathVariable int prcNo,
            @RequestBody PrcDTO.UpdateRequest req) {

        int affected = service.edit(req.getMemberNo(), prcNo, req);
        return affected > 0
                ? ResponseEntity.ok(Map.of("updated", affected))
                : ResponseEntity.notFound().build();
    }


    // /command/prc/{prcNo}/hard?memberNo=1
    @DeleteMapping("/{prcNo}/hard")
    public ResponseEntity<Map<String,Integer>> deleteHard(
            @PathVariable int prcNo,
            @RequestParam int memberNo) {

        int deleted = service.deleteHard(memberNo, prcNo);
        return (deleted > 0)
                ? ResponseEntity.ok(Map.of("deleted", deleted))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{prcNo}")
    public ResponseEntity<Map<String,Integer>> deleteSoft(
            @PathVariable int prcNo,
            @RequestBody Map<String,Integer> body) {

        int memberNo = body.getOrDefault("memberNo", 0);
        int deleted = service.deleteSoft(memberNo, prcNo);
        return (deleted > 0)
                ? ResponseEntity.ok(Map.of("deleted", deleted))
                : ResponseEntity.notFound().build();
    }
}
