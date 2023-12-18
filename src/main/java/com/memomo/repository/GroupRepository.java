package com.memomo.repository;

import com.memomo.entity.BoardGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<BoardGroup, Integer> {
    public List<BoardGroup> findBoardGroupByBno();
}
