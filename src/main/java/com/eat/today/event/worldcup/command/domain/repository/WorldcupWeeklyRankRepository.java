//package com.eat.today.event.worldcup.command.domain.repository;
//
//import com.eat.today.event.worldcup.command.application.entity.WorldcupWeeklyRank;
//import org.springframework.data.jpa.repository.*;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface WorldcupWeeklyRankRepository extends JpaRepository<WorldcupWeeklyRank, Integer> {
//
//    // ✅ 우승 횟수 +1 (기존에 있으면 update)
//    @Modifying
//    @Query(value = """
//        INSERT INTO worldcup_weekly_rank (week_no, alcohol_id, food_id, win_count)
//        VALUES (:weekNo, :alcoholId, :foodId, 1)
//        ON DUPLICATE KEY UPDATE win_count = win_count + 1
//        """, nativeQuery = true)
//    void incrementWinCount(@Param("weekNo") int weekNo,
//                           @Param("alcoholId") int alcoholId,
//                           @Param("foodId") int foodId);
//
//    // ✅ 특정 주차 + 술별 순위 조회
//    @Query("""
//        SELECT w FROM WorldcupWeeklyRank w
//        WHERE w.weekNo = :weekNo AND w.alcoholId = :alcoholId
//        ORDER BY w.winCount DESC
//        """)
//    List<WorldcupWeeklyRank> findWeeklyRank(@Param("weekNo") int weekNo,
//                                            @Param("alcoholId") int alcoholId);
//}