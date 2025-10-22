package com.eat.today.sns.query.repository.dm;

import com.eat.today.sns.query.dto.dm.MessageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DmMapper {

    // 받은 쪽지 목록
    List<MessageDTO> selectReceivedList(@Param("memberNo") int memberNo);

    // 보낸 쪽지 목록
    List<MessageDTO> selectSentList(@Param("memberNo") int memberNo);

    // 쪽지 상세 (파일 포함)
    MessageDTO selectDetail(@Param("noteId") int noteId);
}
