package com.memomo.repository;

import com.memomo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmailAndName(@Param("email") String email, @Param("name") String name);

    Optional<Member> findByEmailAndId(@Param("email") String email, @Param("id") String id);
}
