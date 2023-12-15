package com.memomo.repository;

import com.memomo.dto.PostDTO;
import com.memomo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query("SELECT p from Post p WHERE p.bno = :bno AND p.pstatus='HEAD'")
    public Post postHeadGet(@Param("bno") Integer bno);

}
