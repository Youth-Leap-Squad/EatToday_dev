package com.eat.today.event.albti.query.mapper;

import com.eat.today.event.albti.query.dto.SelectAlbtiDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AlbtiMapper {

    SelectAlbtiDTO selectAlbti(@Param("member_no") int member_no);


}
