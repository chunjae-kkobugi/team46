package com.memomo.repository;

import com.memomo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Transactional // 데이터 변경 작업(CUD)은 트랜잭션 안에서 이루어져야 함
    @Modifying // JPA에게 데이터 수정 작업이 이뤄지는 것을 알려줌
    @Query("UPDATE Post p SET p.status = 'REMOVE' WHERE p.bno = :bno")
    public void boardPostRemove(@Param("bno") Integer bno); // board 제거시 해당 bno 가진 post 도 제거
    // @Param 은 @Query 안의 :변수와 함수의 매개변수를 연결해주는 역할.
    // 두 이름이 동일하다면 생략 가능
}
