package com.memomo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(
        indexes = {@Index(name = "idx_board_member", columnList="teacher")}
)
@EqualsAndHashCode(callSuper = true)
// 상속 관계에서 부모 클래스의 필드들도 고려하여 equals()와 hashCode()를 구현. 즉, 부모 클래스의 필드들도 동등성 비교 및 해시 계산에 포함
// equals(): 두 객체가 동일한지 비교하는 메서드
// hashCode(): 객체가 저장되는 컬렉션과 같은 자료구조에서 사용될 때 객체의 해시코드(식별값)를 반환
@Entity
@Data
public class Board extends BaseEntity {
    @Id
    // @Entity 에는 반드시 해당 엔티티의 PK(기본키)가 있어야 함
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 컬럼의 값 생성 전략. 이 경우 MariaDB의 AUTO_INCREMENT
    private Integer bno;                // 게시판 고유번호

    @Column(nullable = false) // null 비허용
    private String teacher;              // 게시판 개설자(선생님)
    @Column(length = 100, nullable = false)
    private String title;               // 게시판 이름
    @Column(length = 300) // 길이 지정
    private String bpw;                 // 게시판 입장 암호
    @Column(columnDefinition = "INT DEFAULT 1") // default 값 지정
    private Integer maxStudent = 1;     // 최대학생수
    @Column(length = 10)
    private String bgColor;             // 게시판 배경색
    private Integer bgImage;            // 게시판 배경 이미지

    @Column(length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'ACTIVE'")
    // 일부 DB의 경우 columnDefinition 사용 시 length 무시될 수 있음
    private String status = "ACTIVE";   // 게시판 상태

    @Column(length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'GRID'")
    private String layout = "GRID";     // 게시판 최근 레이아웃
    // 격자형(GRID), 자유형(CANVAS), 타임라인형(TIMELINE), 그룹형(GROUP)

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long postHead = 0L; //우선순위 첫 번째 (연결 리스트의 헤드)
}