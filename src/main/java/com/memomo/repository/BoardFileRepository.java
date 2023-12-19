package com.memomo.repository;

import com.memomo.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile, Integer> {
    BoardFile findBoardFileByFno(Integer fno);

    // 게시판 삭제 시 file 도 삭제
    @Transactional
    @Modifying
    @Query("UPDATE BoardFile f SET f.status = 'REMOVE' WHERE f.bno = :bno")
    public void boardFileRemove(@Param("bno") Integer bno);
}
