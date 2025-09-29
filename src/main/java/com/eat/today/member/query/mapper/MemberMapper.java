package com.eat.today.member.query.mapper;

import com.eat.today.member.query.dto.FindMyLevelDTO;
import com.eat.today.member.query.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface MemberMapper {

   // String selectIdByPhone(String memberPhone);
    String selectIdByPhone(@Param("memberPhone") String memberPhone);

    MemberDTO findMyProfile(@Param("memberNo") Integer memberNo);

    FindMyLevelDTO findMyLevel(int memberNo);
}
