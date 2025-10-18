package com.eat.today.post.command.application.service;

import com.eat.today.member.command.application.service.MemberPointService;
import com.eat.today.member.command.domain.aggregate.PointPolicy;
import com.eat.today.post.command.application.dto.*;
import com.eat.today.post.command.domain.aggregate.*;
import com.eat.today.post.command.domain.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostCommandServiceImpl implements PostCommandService {

    private final AlcoholRepository alcoholRepo;
    private final FoodPostRepository postRepo;
    private final FoodPostLikeRepository likeRepo;
    private final FoodCommentRepository commentRepo;
    private final BookmarkRepository bookmarkRepo;
    private final ImageStorageService imageStorageService;
    private final MemberPointService memberPointService;

    private static String nowString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private final ObjectMapper objectMapper;

    private static List<String> splitCsv(String s) {
        if (s == null || s.isBlank()) return List.of();
        return java.util.Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(v -> !v.isEmpty())
                .toList();
    }

    private List<String> toListFromDb(String raw) {
        try {
            // JSON 배열이면 파싱
            if (raw != null && raw.trim().startsWith("[")) {
                return objectMapper.readValue(raw, new TypeReference<List<String>>() {});
            }
            // 아니면 CSV로 가정
            return splitCsv(raw);
        } catch (Exception e) {
            return splitCsv(raw);
        }
    }

    private FoodPostResponse toResponse(FoodPost p) {
        return FoodPostResponse.builder()
                .boardNo(p.getBoardNo())
                .alcoholNo(p.getAlcohol().getAlcoholNo())
                .memberNo(p.getMember().getMemberNo())
                .boardTitle(p.getBoardTitle())
                .boardContent(p.getBoardContent())
                .foodExplain(p.getFoodExplain())
                .foodPictures(toListFromDb(p.getFoodPicture())) // ← 여기!
                .boardDate(p.getBoardDate())
                .boardSeq(p.getBoardSeq())
                .confirmedYn(p.getConfirmedYn())
                .likeNo1(p.getLikeNo1())
                .likeNo2(p.getLikeNo2())
                .likeNo3(p.getLikeNo3())
                .likeNo4(p.getLikeNo4())
                .build();
    }

    /* ================= 술 종류 ================= */

    private static AlcoholResponse toAlcoholResponse(Alcohol a) {
        return AlcoholResponse.builder()
                .alcoholNo(a.getAlcoholNo())
                .alcoholType(a.getAlcoholType())
                .alcoholExplain(a.getAlcoholExplain())
                .alcoholPicture(a.getAlcoholPicture())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AlcoholResponse createAlcohol(CreateAlcoholRequest req) {
        Alcohol a = Alcohol.builder()
                .alcoholNo(null)
                .alcoholType(req.getAlcoholType())
                .alcoholExplain(req.getAlcoholExplain())
                .alcoholPicture(req.getAlcoholPicture())
                .build();
        Alcohol saved = alcoholRepo.save(a);
        return toAlcoholResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AlcoholResponse updateAlcohol(Integer alcoholNo, UpdateAlcoholRequest req) {
        Alcohol a = alcoholRepo.findById(alcoholNo)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("해당 술 정보를 찾을 수 없습니다."));
        a.update(req.getAlcoholType(), req.getAlcoholExplain(), req.getAlcoholPicture());
        return toAlcoholResponse(a);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAlcohol(Integer alcoholNo) {
        if (!alcoholRepo.existsById(alcoholNo)) throw new EntityNotFoundException("해당 술 정보를 찾을 수 없습니다.");
        alcoholRepo.deleteById(alcoholNo);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AlcoholResponse createAlcoholWithImage(CreateAlcoholRequest req, MultipartFile image) {
        String imageUrl = imageStorageService.store(image, "alcohols");
        if (imageUrl != null) req.setAlcoholPicture(imageUrl);
        return createAlcohol(req);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AlcoholResponse updateAlcoholWithImage(Integer alcoholNo, UpdateAlcoholRequest req, MultipartFile image) {
        String imageUrl = imageStorageService.store(image, "alcohols");
        if (imageUrl != null) req.setAlcoholPicture(imageUrl);
        return updateAlcohol(alcoholNo, req);
    }

    /* ================= 안주(게시글) ================= */

    @Override
    public FoodPostResponse createPost(CreateFoodPostRequest req) {
        Alcohol alcohol = alcoholRepo.findById(req.getAlcoholNo())
                .orElseThrow(() -> new EntityNotFoundException("해당 술 정보를 찾을 수 없습니다."));
        Member member = Member.onlyId(req.getMemberNo());

        FoodPost post = FoodPost.builder()
                .boardNo(null)
                .alcohol(alcohol)
                .member(member)
                .boardTitle(req.getBoardTitle())
                .boardContent(req.getBoardContent())
                .foodExplain(req.getFoodExplain())
                .foodPicture(req.getFoodPicture())
                .boardDate(nowString())
                .boardSeq((req.getBoardSeq() == null) ? 0 : req.getBoardSeq())
                .confirmedYn(Boolean.FALSE)
                .likeNo1(0).likeNo2(0).likeNo3(0).likeNo4(0)
                .build();

        FoodPost saved = postRepo.save(post);
        
        // 게시물 등록 시 포인트 지급
        try {
            memberPointService.grantPoints(req.getMemberNo(), PointPolicy.POST_CREATE);
        } catch (Exception e) {
            log.error("게시물 등록 포인트 지급 실패 - 회원번호: {}, 게시물번호: {}", req.getMemberNo(), saved.getBoardNo(), e);
        }
        
        return toResponse(saved);
    }

    @Override
    public FoodPostResponse updatePost(Integer boardNo, UpdateFoodPostRequest req) {
        FoodPost post = postRepo.findById(boardNo)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (Boolean.TRUE.equals(post.getConfirmedYn())) {
            throw new IllegalStateException("승인된 게시글은 수정할 수 없습니다.");
        }

        post.update(req.getBoardTitle(), req.getBoardContent(), req.getFoodExplain(), req.getFoodPicture());
        post.setBoardDate(nowString());
        return toResponse(post);
    }

    @Override
    public FoodPostResponse createPostWithImage(CreateFoodPostRequest req, MultipartFile image) {
        // 하위호환: 단일 → 복수로 래핑
        MultipartFile[] arr = (image == null) ? null : new MultipartFile[]{image};
        return createPostWithImages(req, arr);
    }

    @Override
    public FoodPostResponse updatePostWithImage(Integer boardNo, UpdateFoodPostRequest req, MultipartFile image) {
        MultipartFile[] arr = (image == null) ? null : new MultipartFile[]{image};
        return updatePostWithImages(boardNo, req, arr);
    }

    @Override
    public FoodPostResponse createPostWithImages(CreateFoodPostRequest req, MultipartFile[] images) {
        List<String> urls = imageStorageService.storeAll(images, "foods");
        if (urls != null && !urls.isEmpty()) {
            req.setFoodPicture(String.join(",", urls)); // CSV 저장(원하면 JSON 배열로 변경 가능)
        }
        return createPost(req);
    }

    @Override
    public FoodPostResponse updatePostWithImages(Integer boardNo, UpdateFoodPostRequest req, MultipartFile[] images) {
        List<String> urls = imageStorageService.storeAll(images, "foods");
        if (urls != null && !urls.isEmpty()) {
            req.setFoodPicture(String.join(",", urls));
        }
        return updatePost(boardNo, req);
    }

    /* ================= 댓글/반응/즐겨찾기 (기존 그대로) ================= */

    @Override
    public void deletePost(Integer boardNo) {
        FoodPost post = postRepo.findById(boardNo)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        postRepo.delete(post);
    }

    @Override
    public void cancelPost(Integer boardNo, Integer memberNo) {
        FoodPost post = postRepo.findById(boardNo)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        if (!post.getMember().getMemberNo().equals(memberNo)) {
            throw new IllegalArgumentException("작성자만 취소할 수 있습니다.");
        }
        postRepo.delete(post);
    }

    @Override
    public FoodPostResponse approve(Integer boardNo, boolean approved) {
        FoodPost post = postRepo.findById(boardNo)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        post.setConfirmedYn(approved);
        return toResponse(post);
    }

    @Override
    public CommentResponse addComment(AddCommentRequest req) {
        FoodPost post = postRepo.findById(req.getBoardNo())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        FoodComment c = FoodComment.builder()
                .foodCommentNo(null)
                .post(post)
                .member(Member.onlyId(req.getMemberNo()))
                .content(req.getContent())
                .createdAt(nowString())
                .updatedAt(null)
                .build();

        FoodComment saved = commentRepo.save(c);
        
        // 댓글 작성 시 포인트 지급
        try {
            memberPointService.grantPoints(req.getMemberNo(), PointPolicy.COMMENT_CREATE);
        } catch (Exception e) {
            log.error("댓글 작성 포인트 지급 실패 - 회원번호: {}, 댓글번호: {}", req.getMemberNo(), saved.getFoodCommentNo(), e);
        }

        return CommentResponse.builder()
                .foodCommentNo(saved.getFoodCommentNo())
                .boardNo(saved.getPost().getBoardNo())
                .memberNo(saved.getMember().getMemberNo())
                .content(saved.getContent())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    public CommentResponse updateCommentById(Integer commentId, Integer memberNo, String content) {
        FoodComment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (!c.getMember().getMemberNo().equals(memberNo)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        c.setContent(content);
        c.setUpdatedAt(nowString());

        return CommentResponse.builder()
                .foodCommentNo(c.getFoodCommentNo())
                .boardNo(c.getPost().getBoardNo())
                .memberNo(c.getMember().getMemberNo())
                .content(c.getContent())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }

    @Override
    public void deleteCommentById(Integer commentId, Integer memberNo) {
        FoodComment c = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        if (!c.getMember().getMemberNo().equals(memberNo)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
        commentRepo.delete(c);
    }

    private ReactionResponse toReactionResponse(FoodPost p) {
        String mid = (p.getMember() != null) ? p.getMember().getMemberId() : null;

        return ReactionResponse.builder()
                .boardNo(p.getBoardNo())
                .boardTitle(p.getBoardTitle())
                .boardContent(p.getBoardContent())
                .foodExplain(p.getFoodExplain())
                .foodPicture(p.getFoodPicture())
                .memberId(mid)
                .boardDate(p.getBoardDate())
                .boardSeq(p.getBoardSeq())
                .likesNo1(p.getLikeNo1())
                .likesNo2(p.getLikeNo2())
                .likesNo3(p.getLikeNo3())
                .likesNo4(p.getLikeNo4())
                .build();
    }

    @Override
    public ReactionResponse addReaction(Integer boardNo, ReactRequest req) {
        return changeReaction(boardNo, req);
    }

    @Override
    public ReactionResponse changeReaction(Integer boardNo, ReactRequest req) {
        FoodPost post = postRepo.findById(boardNo)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("게시글을 찾을 수 없습니다."));

        LikesType newType = LikesType.fromNumber(req.getLikesType());
        Integer memberNo = req.getMemberNo();

        FoodPostLikeId id = new FoodPostLikeId(memberNo, boardNo);
        FoodPostLike current = likeRepo.findById(id).orElse(null);

        if (current == null) {
            FoodPostLike created = FoodPostLike.builder()
                    .id(id)
                    .post(post)
                    .member(Member.onlyId(memberNo))
                    .likesType(newType)
                    .build();
            likeRepo.save(created);
            post.increaseLike(newType);
            return toReactionResponse(post);
        }

        LikesType oldType = current.getLikesType();
        if (oldType == newType) {
            likeRepo.delete(current);
            post.decreaseLike(oldType);
        } else {
            current.setLikesType(newType);
            likeRepo.save(current);
            post.decreaseLike(oldType);
            post.increaseLike(newType);
        }
        return toReactionResponse(post);
    }

    @Override
    public void deleteReaction(Integer boardNo, Integer memberNo) {
        FoodPost post = postRepo.findById(boardNo)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        FoodPostLikeId id = new FoodPostLikeId(memberNo, boardNo);
        FoodPostLike like = likeRepo.findById(id).orElse(null);
        if (like == null) return;
        post.decreaseLike(like.getLikesType());
        likeRepo.delete(like);
    }

    private List<BookmarkResponse> buildBookmarkList(Integer memberNo) {
        return bookmarkRepo.findAllByMember_MemberNo(memberNo).stream()
                .map(b -> {
                    FoodPost post = b.getPost();
                    String author = (post.getMember() != null && safeGetMemberId(post.getMember()) != null)
                            ? safeGetMemberId(post.getMember())
                            : String.valueOf(post.getMember().getMemberNo());

                    return BookmarkResponse.builder()
                            .boardNo(post.getBoardNo())
                            .memberId(author)
                            .boardTitle(post.getBoardTitle())
                            .foodPicture(post.getFoodPicture())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String safeGetMemberId(Member m) {
        try { return (String) Member.class.getMethod("getMemberId").invoke(m); }
        catch (Exception ignore) { return null; }
    }

    @Override
    public List<BookmarkResponse> addBookmark(AddBookmarkRequest req) {
        FoodPost post = postRepo.findById(req.getBoardNo())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("게시글을 찾을 수 없습니다."));
        BookmarkId id = new BookmarkId(req.getMemberNo(), req.getBoardNo());
        if (!bookmarkRepo.existsById(id)) {
            Bookmark b = Bookmark.builder()
                    .id(id)
                    .member(Member.onlyId(req.getMemberNo()))
                    .post(post)
                    .build();
            bookmarkRepo.save(b);
        }
        return buildBookmarkList(req.getMemberNo());
    }

    @Override
    public List<BookmarkResponse> removeBookmark(Integer memberNo, Integer boardNo) {
        BookmarkId id = new BookmarkId(memberNo, boardNo);
        if (bookmarkRepo.existsById(id)) {
            bookmarkRepo.deleteById(id);
        }
        return buildBookmarkList(memberNo);
    }
}
