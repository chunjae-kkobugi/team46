package com.memomo.repository;

import com.memomo.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.pno = :pno order by c.cno desc")
    public List<Comment> findByPno(@Param("pno") Long pno);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.pno = :pno")
    Long countByPno(@Param("pno") Long pno);
}
