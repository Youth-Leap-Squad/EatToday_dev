package com.eat.today.sns.command.domain.repository.dm;

import com.eat.today.sns.command.application.entity.dm.DmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DmRepository extends JpaRepository<DmEntity, Integer> {

    // 받은 메시지 전체 삭제
    int deleteByReceiveMemberId(int receiveMemberId);

    // 보낸 메시지 전체 삭제
    int deleteBySendMemberId(int sendMemberId);

    // 특정 상대와 주고받은 메시지 삭제도 가능
    int deleteBySendMemberIdAndReceiveMemberId(int sendMemberId, int receiveMemberId);

}
