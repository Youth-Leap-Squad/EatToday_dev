package com.eat.today.member.command.infrastructure.persistence;

import com.eat.today.member.command.domain.aggregate.MemberEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface SpringDataMemberRepository extends JpaRepository<MemberEntity, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update MemberEntity m
              set m.reportCount   = m.reportCount + 1,
                  m.memberStatus = case
                                     when m.reportCount  >= 5 then '영구정지'
                                     when m.reportCount  >= 3 then '7일 정지'
                                     when m.reportCount  >= 1 then '1일 정지'
                                     else 'normal'
                                   end
            where m.memberNo = :id
           """)
    int bumpCountAndStatus(@Param("id") Integer id);
}