package com.memomo.repository;

import com.memomo.entity.BoardGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<BoardGroup, Integer> {
    @Query("select g from BoardGroup g where g.bno = :bno")
    public List<BoardGroup> findBoardGroupByBno(Integer bno);
}
