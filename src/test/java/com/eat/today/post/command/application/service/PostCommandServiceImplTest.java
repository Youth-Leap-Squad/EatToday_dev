//package com.eat.today.post.command.application.service;
//
//import com.eat.today.member.command.application.service.MemberPointService;
//import com.eat.today.member.command.domain.aggregate.PointPolicy;
//import com.eat.today.post.command.application.dto.CreateFoodPostRequest;
//import com.eat.today.post.command.application.dto.FoodPostResponse;
//import com.eat.today.post.command.application.dto.UpdateFoodPostRequest;
//import com.eat.today.post.command.domain.aggregate.*;
//import com.eat.today.post.command.domain.repository.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PostCommandServiceImplTest {
//
//    @InjectMocks
//    PostCommandServiceImpl service;
//
//    @Mock
//    AlcoholRepository alcoholRepo;
//
//    @Mock
//    FoodPostRepository postRepo;
//
//    @Mock
//    FoodPostLikeRepository likeRepo;
//
//    @Mock
//    FoodCommentRepository commentRepo;
//
//    @Mock
//    BookmarkRepository bookmarkRepo;
//
//    @Mock
//    ImageStorageService imageStorageService;
//
//    @Mock
//    MemberPointService memberPointService;
//
//    @Mock
//    ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("createPost: 성공 시 FoodPostResponse를 반환한다")
//    void createPost_success_returnsResponse() {
//        // given
//        Integer alcoholNo = 1;
//        Integer memberNo  = 99;
//
//        Alcohol alcohol = mock(Alcohol.class);
//        when(alcohol.getAlcoholNo()).thenReturn(alcoholNo);
//        when(alcoholRepo.findById(alcoholNo)).thenReturn(Optional.of(alcohol));
//
//        // postRepo.save(...)가 반환할 "저장된" 엔티티 모킹
//        // 서비스는 save 결과로부터 getter를 호출해 응답을 만든다.
//        FoodPost saved = mock(FoodPost.class);
//        when(saved.getBoardNo()).thenReturn(10);
//        when(saved.getAlcohol()).thenReturn(alcohol);
//        when(saved.getMember()).thenReturn(Member.onlyId(memberNo));
//        when(saved.getBoardTitle()).thenReturn("오늘의 안주: 참치회");
//        when(saved.getBoardContent()).thenReturn("비 오는 날엔 최고...");
//        when(saved.getFoodExplain()).thenReturn("매콤새콤, 소주와도 잘 맞아요");
//        when(saved.getFoodPicture()).thenReturn("https://img.example.com/food/101.jpg");
//        when(saved.getBoardDate()).thenReturn("2025-09-29 09:15:49");
//        when(saved.getBoardSeq()).thenReturn(10);
//        when(saved.getConfirmedYn()).thenReturn(false);
//        when(saved.getLikeNo1()).thenReturn(1);
//        when(saved.getLikeNo2()).thenReturn(3);
//        when(saved.getLikeNo3()).thenReturn(5);
//        when(saved.getLikeNo4()).thenReturn(16);
//
//        when(postRepo.save(any(FoodPost.class))).thenReturn(saved);
//
//        CreateFoodPostRequest req = new CreateFoodPostRequest();
//        req.setAlcoholNo(alcoholNo);
//        req.setMemberNo(memberNo);
//        req.setBoardTitle("오늘의 안주: 참치회");
//        req.setBoardContent("비 오는 날엔 최고...");
//        req.setFoodExplain("매콤새콤, 소주와도 잘 맞아요");
//        req.setFoodPicture("https://img.example.com/food/101.jpg");
//        req.setBoardSeq(10);
//
//        // when
//        FoodPostResponse resp = service.createPost(req);
//
//        // then
//        assertThat(resp.getBoardNo()).isEqualTo(10);
//        assertThat(resp.getAlcoholNo()).isEqualTo(alcoholNo);
//        assertThat(resp.getMemberNo()).isEqualTo(memberNo);
//        assertThat(resp.getBoardTitle()).isEqualTo("오늘의 안주: 참치회");
//        assertThat(resp.getBoardContent()).isEqualTo("비 오는 날엔 최고...");
//        assertThat(resp.getFoodExplain()).isEqualTo("매콤새콤, 소주와도 잘 맞아요");
//        assertThat(resp.getFoodPicture()).isEqualTo("https://img.example.com/food/101.jpg");
//        assertThat(resp.getBoardDate()).isEqualTo("2025-09-29 09:15:49");
//        assertThat(resp.getBoardSeq()).isEqualTo(10);
//        assertThat(resp.getConfirmedYn()).isFalse();
//        assertThat(resp.getLikeNo1()).isEqualTo(1);
//        assertThat(resp.getLikeNo2()).isEqualTo(3);
//        assertThat(resp.getLikeNo3()).isEqualTo(5);
//        assertThat(resp.getLikeNo4()).isEqualTo(16);
//
//        verify(alcoholRepo).findById(alcoholNo);
//        verify(postRepo).save(any(FoodPost.class));
//    }
//
//    @Test
//    @DisplayName("updatePost: 승인된(confirmed_yn=true) 글은 수정할 수 없고 예외가 발생한다")
//    void updatePost_blocked_whenApproved() {
//        // given
//        Integer boardNo = 123;
//
//        FoodPost approved = mock(FoodPost.class);
//        when(approved.getConfirmedYn()).thenReturn(true); // 승인됨
//        when(postRepo.findById(boardNo)).thenReturn(Optional.of(approved));
//
//        UpdateFoodPostRequest req = new UpdateFoodPostRequest();
//        req.setBoardTitle("수정 제목");
//        req.setBoardContent("수정 내용");
//        req.setFoodExplain("수정 설명");
//        req.setFoodPicture("/img.jpg");
//
//        // when & then
//        assertThatThrownBy(() -> service.updatePost(boardNo, req))
//                .isInstanceOf(IllegalStateException.class);
//
//        verify(postRepo).findById(boardNo);
//        verify(postRepo, never()).save(any()); // 저장 시도 없음
//    }
//
//    @Test
//    @DisplayName("createPost: 게시물 등록 시 회원에게 포인트가 지급된다")
//    void createPost_grantsPointsToMember() {
//        // given
//        Integer alcoholNo = 1;
//        Integer memberNo = 99;
//
//        Alcohol alcohol = mock(Alcohol.class);
//        when(alcohol.getAlcoholNo()).thenReturn(alcoholNo);
//        when(alcoholRepo.findById(alcoholNo)).thenReturn(Optional.of(alcohol));
//
//        FoodPost saved = mock(FoodPost.class);
//        when(saved.getBoardNo()).thenReturn(10);
//        when(saved.getAlcohol()).thenReturn(alcohol);
//        when(saved.getMember()).thenReturn(Member.onlyId(memberNo));
//        when(saved.getBoardTitle()).thenReturn("치킨과 맥주");
//        when(saved.getBoardContent()).thenReturn("치맥 최고");
//        when(saved.getFoodExplain()).thenReturn("바삭바삭");
//        when(saved.getFoodPicture()).thenReturn("/image.jpg");
//        when(saved.getBoardDate()).thenReturn("2025-10-17");
//        when(saved.getBoardSeq()).thenReturn(1);
//        when(saved.getConfirmedYn()).thenReturn(false);
//        when(saved.getLikeNo1()).thenReturn(0);
//        when(saved.getLikeNo2()).thenReturn(0);
//        when(saved.getLikeNo3()).thenReturn(0);
//        when(saved.getLikeNo4()).thenReturn(0);
//
//        when(postRepo.save(any(FoodPost.class))).thenReturn(saved);
//
//        CreateFoodPostRequest req = new CreateFoodPostRequest();
//        req.setAlcoholNo(alcoholNo);
//        req.setMemberNo(memberNo);
//        req.setBoardTitle("치킨과 맥주");
//        req.setBoardContent("치맥 최고");
//        req.setFoodExplain("바삭바삭");
//        req.setFoodPicture("/image.jpg");
//        req.setBoardSeq(1);
//
//        // when
//        FoodPostResponse resp = service.createPost(req);
//
//        // then
//        assertThat(resp.getBoardNo()).isEqualTo(10);
//        assertThat(resp.getMemberNo()).isEqualTo(memberNo);
//
//        // 포인트 지급이 POST_CREATE 정책으로 호출되었는지 검증
//        verify(memberPointService).grantPoints(eq(memberNo), eq(PointPolicy.POST_CREATE));
//        verify(alcoholRepo).findById(alcoholNo);
//        verify(postRepo).save(any(FoodPost.class));
//    }
//}
