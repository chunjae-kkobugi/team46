package com.memomo.repository;

import com.memomo.entity.Layout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
    Optional<Layout> findByPno(Long pno);

    @Transactional
    @Modifying
    @Query("UPDATE Layout l SET l.priority = :priority WHERE l.pno = :pno")
    public void layoutPriority(@Param("priority") Long priority, @Param("pno") Long pno);
}
