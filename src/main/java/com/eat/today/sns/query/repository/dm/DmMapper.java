package com.eat.today.sns.query.repository.dm;

import com.eat.today.sns.query.dto.dm.DmDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DmMapper {
    List<DmDTO> selectByMessageNo(int sendMemberId, int receiveMemberId);
}
