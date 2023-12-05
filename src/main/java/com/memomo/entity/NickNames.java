package com.memomo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nickname", "bno"}) // 유니크 키 설정
        },
        indexes = {@Index(name="idx_board_student", columnList = "bno")}
)
public class NickNames {
    // 게시판 생성 시 자동으로 선생님 추가

    @Id
    @Column(length=100)
    private String cookie;          // 학생 참여자 쿠키(고유값) OR 선생님 경우 'TEACHER'
    @Column(length=50, nullable = false)
    private String nickname;        // 학생 참여자 별칭
    @Column(nullable = false)
    private Integer bno;            // 게시판 번호
}