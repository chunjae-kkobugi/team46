package com.memomo.repository;

import com.memomo.entity.BoardGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<BoardGroup, Integer> {
}
