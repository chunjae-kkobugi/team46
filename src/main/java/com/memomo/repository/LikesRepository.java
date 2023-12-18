package com.memomo.repository;

import com.memomo.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    public int countLikeByPno(Long pno);    // 포스트잇 별 좋아요 개수
    public Long deleteLikeByPnoAndAuthor(Long pno, String author);  // 좋아요 해제
    @Query("SELECT COUNT(*) FROM Likes l WHERE l.pno = :pno AND l.author = :author")
    public int hasLikes(@Param("pno") Long pno, @Param("author") String author);
}
