package com.memomo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(
        indexes = {@Index(name = "idx_bno_group", columnList = "bno")}
)
@Entity
@Data
public class BoardGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gno;        // 그룹 고유번호

    @Column(nullable = false)
    private Integer bno;        // 그룹이 속한 게시판
    @Column(length = 100, nullable = false)
    private String title;       // 그룹 이름
    @Column(length = 10)
    private String gColor;      // 그룹 색깔

    public void change(String title, String gColor){
        this.title = title;
        this.gColor = gColor;
    }
}