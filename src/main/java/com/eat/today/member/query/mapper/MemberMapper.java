package com.eat.today.member.query.mapper;

import com.eat.today.member.query.dto.FindMyLevelDTO;
import com.eat.today.member.query.dto.FindProfileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

   // String selectIdByEmail(String memberEmail);
    String selectIdByEmail(@Param("memberEmail") String memberEmail);

    FindProfileDTO findMyProfile(@Param("memberNo") Integer memberNo);
    
    FindMyLevelDTO findMyLevel(@Param("memberNo") Integer memberNo);
}