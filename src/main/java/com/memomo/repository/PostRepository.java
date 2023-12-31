package com.memomo.repository;

import com.memomo.dto.PostDTO;
import com.memomo.entity.Post;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Transactional // 데이터 변경 작업(CUD)은 트랜잭션 안에서 이루어져야 함
    @Modifying // JPA에게 데이터 수정 작업이 이뤄지는 것을 알려줌
    @Query("UPDATE Post p SET p.pstatus = 'REMOVE' WHERE p.bno = :bno")
//    @Lock(LockModeType.PESSIMISTIC_READ)
    public void boardPostRemove(@Param("bno") Integer bno); // board 제거시 해당 bno 가진 post 도 제거
    // @Param 은 @Query 안의 :변수와 함수의 매개변수를 연결해주는 역할.
    // 두 이름이 동일하다면 생략 가능

    // 게시판 별 포스트잇 보기
    @Query("select p from Post p where p.bno = :bno AND p.pstatus != 'REMOVE' order by p.createAt desc")
    public List<Post> postListByBno(@Param("bno") Integer bno);

    public List<Post> findAllByBnoAndPstatus(Integer bno, String status);

    @Transactional
    @Query("SELECT p, l FROM Post p LEFT JOIN Layout  l ON p.pno = l.pno WHERE p.bno = :bno")
    public List<Object[]> postDTOList(@Param("bno") Integer bno);

    // 포스트 목록을 보다 간편하고 한 번에 가져올 수 있도록 수정
    // 기존의 COUNT(ls.pno), COUNT(c.pno)는 likes, comments left join 상 문제가 생길 수 있어 변경
    // 다음의 쿼리는 SQL 상에서 "SELECT p.pno, l.*, pf.*, COUNT(DISTINCT ls.lno) AS likes_count, COUNT(DISTINCT c.cno) AS comment_count FROM Post p LEFT JOIN Layout l ON p.pno = l.pno LEFT JOIN Likes ls ON p.pno = ls.pno LEFT JOIN PostFile pf ON p.pno = pf.pno LEFT JOIN Comment c ON c.pno = p.pno WHERE p.bno = 1 GROUP BY p.pno" 와 같음.
    @Transactional
    @Query(value = "SELECT new com.memomo.dto.PostDTO(p, l, pf, COUNT(DISTINCT ls.lno), COUNT(DISTINCT c.cno)) " +
            "FROM Post p LEFT JOIN Layout l ON p.pno = l.pno " +
            "LEFT JOIN Likes ls ON p.pno = ls.pno " +
            "LEFT JOIN PostFile pf ON p.pno = pf.pno " +
            "LEFT JOIN Comment c ON c.pno = p.pno WHERE p.bno = :bno GROUP BY p.pno")
    public List<PostDTO> postListAll(@Param("bno") Integer bno);

    @Transactional
    @Query(value = "SELECT new com.memomo.dto.PostDTO(p, l, pf, COUNT(DISTINCT ls.lno), COUNT(DISTINCT c.cno)) " +
            "FROM Post p LEFT JOIN Layout l ON p.pno = l.pno " +
            "LEFT JOIN Likes ls ON p.pno = ls.pno " +
            "LEFT JOIN PostFile pf ON p.pno = pf.pno " +
            "LEFT JOIN Comment c ON c.pno = p.pno WHERE p.pno = :pno GROUP BY p.pno")
    public PostDTO postGetAll(@Param("pno") Long pno);

    @Query("SELECT p from Post p WHERE p.bno = :bno AND p.pstatus='HEAD'")
    public Post postHeadGet(@Param("bno") Integer bno);

    // OMG... JPA는 WHERE, HAVING, SELECT 절에서만 서브 쿼리가 가능하다고 한다. 아래 구문을 불가
    //    SELECT p.*, l.*, ls.likes FROM post p LEFT JOIN layout l ON p.pno = l.pno
    //    LEFT JOIN (SELECT COUNT(*) as likes, pno FROM likes GROUP BY pno) ls ON p.pno = ls.pno WHERE p.bno = 1;

    // 포스트 상세보기
    @Query("select p from Post p where p.pno = :pno")
    public Post getPostByPno(@Param("pno") Long pno);
}
