package com.eat.today.event.command.application.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorldcupResultService_UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetWorldcupResult() throws Exception {
        // given
        String weekNo = "1";        // 실제 DB에 존재하는 주차 번호 사용

        // when & then (Controller -> Service -> Mapper -> DB)
        mockMvc.perform(get("/worldcup/getworldcupresult")
                        .param("weekNo", weekNo))
                .andDo(print())
                .andExpect(status().isOk())

                // 배열(List)이니까 첫 번째 원소 기준으로 검증
                .andExpect(jsonPath("$[0].worldcuptable.worldcup_start_date").exists())
                .andExpect(jsonPath("$[0].worldcuptable.worldcup_finish_date").exists())
                .andExpect(jsonPath("$[0].worldcupeventfood.worldcup_winning_food").exists())
                .andExpect(jsonPath("$[0].worldcupeventfood.num_of_wins").exists());

    }
}
