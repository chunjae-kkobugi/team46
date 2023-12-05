package com.memomo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(
        indexes = {@Index(name = "idx_post_comment", columnList = "pno")}
)
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;                // 댓글 고유 번호

    @Column(nullable = false)
    private Long pno;                  // 댓글이 달린 게시글
    @Column(length = 50, nullable = false)
    private String author;             // 작성자
    @Column(length = 1000, nullable = false)
    private String content;             // 내용
}