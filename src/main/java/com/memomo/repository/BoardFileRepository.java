package com.memomo.repository;

import com.memomo.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile, Integer> {
    BoardFile findBoardFileByFno(Integer fno);

}
