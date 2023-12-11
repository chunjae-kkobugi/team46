package com.memomo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(
        indexes = {@Index(name = "idx_board_file", columnList = "bno")}
)
@Entity
@Data
public class BoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fno;                    // 파일 고유번호

    @Column(nullable = false)
    private Integer bno;                    // 파일의 게시판 번호

    private String originName;              // 파일 업로드 이름
    private String saveName;                // 파일 저장 이름
    private String savePath;                // 파일 저장 경로
    private String fileType;                // 파일 유형

    @Column(length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'ACTIVE'")
    private String status = "ACTIVE";       // 파일 상태

}