package com.eat.today.sns.query.controller.dm;

import com.eat.today.sns.query.dto.dm.MessageDTO;
import com.eat.today.sns.query.repository.dm.DmMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dm")
@RequiredArgsConstructor
public class DmQueryController {

    private final DmMapper mapper;

    // 받은 쪽지함
    @GetMapping("/received/{memberNo}")
    public List<MessageDTO> getReceivedList(@PathVariable int memberNo) {
        return mapper.selectReceivedList(memberNo);
    }

    // 보낸 쪽지함
    @GetMapping("/sent/{memberNo}")
    public List<MessageDTO> getSentList(@PathVariable int memberNo) {
        return mapper.selectSentList(memberNo);
    }

    // 쪽지 상세
    @GetMapping("/{noteId}")
    public MessageDTO getDetail(@PathVariable int noteId) {
        return mapper.selectDetail(noteId);
    }
}
