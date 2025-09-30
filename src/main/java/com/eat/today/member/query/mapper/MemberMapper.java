package com.eat.today.member.query.mapper;

import com.eat.today.member.query.dto.FindMyLevelDTO;
import com.eat.today.member.query.dto.FindProfileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

   // String selectIdByPhone(String memberPhone);
    String selectIdByPhone(@Param("memberPhone") String memberPhone);

    FindProfileDTO findMyProfile(@Param("memberNo") Integer memberNo);
    
    FindMyLevelDTO findMyLevel(@Param("memberNo") Integer memberNo);
}