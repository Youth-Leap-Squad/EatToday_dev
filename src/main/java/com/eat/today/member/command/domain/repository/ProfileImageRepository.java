package com.eat.today.member.command.domain.repository;

import com.eat.today.member.command.domain.aggregate.ProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImageEntity, Long> {

    /**
     * 회원 이메일로 활성화된 프로필 사진 조회
     */
    Optional<ProfileImageEntity> findByMemberEmailAndIsActiveTrue(String memberEmail);

    /**
     * 회원 이메일로 모든 프로필 사진 조회 (최신순)
     */
    List<ProfileImageEntity> findByMemberEmailOrderByUploadedAtDesc(String memberEmail);

    /**
     * 회원 이메일로 활성화된 프로필 사진이 있는지 확인
     */
    boolean existsByMemberEmailAndIsActiveTrue(String memberEmail);

    /**
     * 회원 이메일로 모든 프로필 사진 삭제
     */
    void deleteByMemberEmail(String memberEmail);
}

