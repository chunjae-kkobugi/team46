package com.memomo.dto;

import com.memomo.entity.BoardFile;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDTO {
    private Integer bno;
    private String teacher;
    private String title;
    private String bpw;
    private Integer maxStudent;
    private String bgColor;
    private Integer bgImage;
    private String status = "ACTIVE";
    private String layout = "GRID";
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private BoardFile file;             // 배경 이미지
}