package com.eat.today.event.albti.query.service;

import com.eat.today.event.albti.query.dto.AlbtiDTO;
import com.eat.today.event.albti.query.dto.AlbtiOutputDTO;
import com.eat.today.event.albti.query.dto.FoodPostDTO;
import com.eat.today.event.albti.query.dto.SelectAlbtiDTO;
import com.eat.today.event.albti.query.mapper.AlbtiMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AlbtiService {
    private final SqlSession sqlSession;
    @Autowired
    public AlbtiService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public SelectAlbtiDTO selectAlbti(int member_no) {
        return sqlSession.getMapper(AlbtiMapper.class)
                .selectAlbti(member_no);
    }
//    public SelectAlbtiDTO selectAlbti(int memberNo) {
//
//        // 1. DB 조회
//        SelectAlbtiDTO dto = sqlSession.getMapper(AlbtiMapper.class)
//                .selectAlbti(memberNo);

//        // 2. albtiDto 문자열 '/' 구분 -> 리스트로 변환 후 다시 합쳐서 단일 DTO로 저장
//        if (dto.getAlbtiDto() != null) {
//            String[] categories = dto.getAlbtiDto().getAlBTICategory().split("/");
//            String[] details = dto.getAlbtiDto().getAlBTIDetail().split("/");
//
//            String combinedCategory = String.join("/", categories);
//            String combinedDetail = String.join("/", details);
//
//            dto.setAlbtiDto(new AlbtiDTO(combinedCategory, combinedDetail));
//        }
//
//        // 3. albtiOutput 문자열 '/' 구분 -> 단일 DTO로 변환
//        if (dto.getAlbtiOutput() != null) {
//            String[] alcoholExplains = dto.getAlbtiOutput().getAlBTIAlcoholExplain().split("/");
//            dto.setAlbtiOutput(new AlbtiOutputDTO(String.join("/", alcoholExplains)));
//        }
//
//        // 4. foodPostDto 문자열 '/' 구분 -> 단일 DTO로 변환
//        if (dto.getFoodPostDto() != null) {
//            String[] explains = dto.getFoodPostDto().getFoodExplain().split("/");
//            String[] pictures = dto.getFoodPostDto().getFoodPicture().split("/");
//
//            String combinedExplain = String.join("/", explains);
//            String combinedPicture = String.join("/", pictures);
//
//            dto.setFoodPostDto(new FoodPostDTO(combinedExplain, combinedPicture));
//        }
//
//        return dto;
//    }

}
