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

            int joinMemberNo = worldcupServiceAdd.joinWorldcup(
                    request.getMemberNo(),
                    request.getWorldcupNo(),
                    request.getAlcoholId(),
                    request.getFoodId()
            );

            return new WorldcupJoinResponseAddDTO(
                    request.getMemberNo(),
                    request.getWorldcupNo(),
                    request.getAlcoholId(),
                    request.getFoodId(),
                    joinMemberNo,
                    "월드컵 참여 및 포인트 지급 완료되었습니다!",
                    true   // ✅ 무조건 포인트 지급
            );
        }
    }