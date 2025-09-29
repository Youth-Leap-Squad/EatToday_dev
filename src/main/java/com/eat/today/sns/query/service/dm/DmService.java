package com.eat.today.sns.query.service.dm;

import com.eat.today.sns.query.dto.dm.DmDTO;

import java.util.List;

public interface DmService {
    List<DmDTO> getByMessageNo(int sendMemberId, int receiveMemberId);
}
