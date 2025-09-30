package com.eat.today.event.command.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest                 // 스프링 부트를 통째로 띄워서 실제 애플리케이션 환경에서 테스트 (컨트롤러, 서비스, 매퍼, DB 연결까지 다 활성화)
@AutoConfigureMockMvc          // 실제 서버를 띄우지 않고도 MockMvc 객체로 HTTP 요청/응답 가능
class AlbtiResultService_UserTest {

    @Autowired
    private MockMvc mockMvc;    // 이 객체를 이용해 API를 직접 호출하고 응답을 검증

    @Test
    void testGetAlbtiResult() throws Exception {
        //given
        String memberNo = "3";      // 실제 DB에 있는 값


        // when & then (Controller -> Service -> Mapper -> DB)
        mockMvc.perform(get("/albti/getalbtiresult") // Controller 호출
                        .param("member_no", memberNo))           // ?member_no=3
                .andDo(print())                                // 요청/응답 로그를 콘솔에 출력(디버깅용)
                .andExpect(status().isOk())                    // HTTP 응답 코드가 200 Ok인지 확인

                // DTO 속성까지 검증, 응답 JSON에서 특정 경로가 존재하는지 확인(필요시 수정 가능)
                .andExpect(jsonPath("$.albti_dto.alBTI_category").exists())
                // $.albti_dto.alBTI_category → albti_dto 안의 alBTI_category 필드가 있는지
                .andExpect(jsonPath("$.albti_output.alBTI_alcohol_explain").exists());
        // $.albti_output.alBTI_alcohol_explain → albti_output 안의 alBTI_alcohol_explain 필드가 있는지
    }

}
