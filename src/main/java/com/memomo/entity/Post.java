package com.memomo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(
        indexes = {@Index(name = "idx_board_post", columnList = "bno")}
)
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;           // 포스트잇 고유번호

    @Column(nullable = false)
    private Integer bno;        // 게시판 번호
    @Column(length = 50, nullable = false)
    private String author;      // 작성자

    @Column(length = 1000, nullable = false)
    private String content;
    @Column(length = 10)
    private String bgColor;
    private Long bgImage;
    @Column(length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'ACTIVE'", name="status")
    private String pstatus = "ACTIVE";
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean vote = false;
}