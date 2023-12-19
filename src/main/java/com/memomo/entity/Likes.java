package com.memomo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long lno;           // 좋아요 고유 번호
    @Column(nullable = false) // null 비허용
    Long pno;           // 좋아요한 포스트잇 고유 번호
    @Column(nullable = false) // null 비허용
    String author;      // 좋아요를 누른 사람
}
