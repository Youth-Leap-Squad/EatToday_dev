package com.eat.today.sns.query.controller.dm;

import com.eat.today.sns.query.dto.dm.DmDTO;
import com.eat.today.sns.query.service.dm.DmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dm")
@RequiredArgsConstructor
public class DmController {

    private final DmService dmService;

    // 예: GET /dm/12/34
    @GetMapping("/{sendMemberId}/{receiveMemberId}")
    public ResponseEntity<List<DmDTO>> getByMembers(
            @PathVariable("sendMemberId") Integer sendMemberId,    // ✅ @PathVariable + Integer
            @PathVariable("receiveMemberId") Integer receiveMemberId
    ) {
        return ResponseEntity.ok(dmService.getByMessageNo(sendMemberId, receiveMemberId));
    }
}
