package com.memomo.repository;

import com.memomo.entity.Layout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
    Optional<Layout> findByPno(Long pno);
}
