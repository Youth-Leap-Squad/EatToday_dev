    package com.eat.today.event.worldcup.command.application.controller;


    import com.eat.today.event.worldcup.command.application.dto.WorldcupJoinRequestAddAgainDTO;
    import com.eat.today.event.worldcup.command.application.dto.WorldcupJoinResponseAddDTO;
    import com.eat.today.event.worldcup.command.application.service.WorldcupServiceAdd;
    import lombok.RequiredArgsConstructor;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/worldcup")
    public class WorldcupControllerAdd {
        private final WorldcupServiceAdd worldcupServiceAdd;

        @PostMapping("/join")
        public WorldcupJoinResponseAddDTO joinWorldcup(@RequestBody WorldcupJoinRequestAddAgainDTO request) {
            // 서비스 실행 후 joinMember PK 리턴받기
            int joinMemberNo = worldcupServiceAdd.joinWorldcup(
                    request.getMemberNo(),
                    request.getWorldcupNo(),
                    request.getAlcoholId(),
                    request.getFoodId()
            );

            // 응답 DTO 생성
            return new WorldcupJoinResponseAddDTO(
                    request.getMemberNo(),
                    request.getWorldcupNo(),
                    request.getAlcoholId(),
                    request.getFoodId(),
                    joinMemberNo,
                    "월드컵 참여 및 선택이 완료되었습니다!"
            );
        }

    }
