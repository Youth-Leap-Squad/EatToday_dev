package com.eat.today.sns.command.domain.repository.follow;

import com.eat.today.sns.command.application.entity.follow.FollowEntity;
import com.eat.today.sns.command.application.entity.follow.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, FollowId> {
    boolean existsByFollowerNoAndFollowingNo(int followerNo, int followingNo);

    List<FollowEntity> findByFollowerNo(Integer followerNo);    // 내가 팔로우하는 사람
    List<FollowEntity> findByFollowingNo(Integer followingNo);  // 나를 팔로우하는 사람

    int countByFollowerNo(Integer followerNo);           // 팔로잉 수
    int countByFollowingNo(Integer followingNo);         // 팔로워 수

    // 팔로우/팔로잉 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from FollowEntity f " +
            " where f.followerNo = :followerNo and f.followingNo = :followingNo")
    int deleteByFollowerNoAndFollowingNo(@Param("followerNo") int followerNo,
                                         @Param("followingNo") int followingNo);

}
