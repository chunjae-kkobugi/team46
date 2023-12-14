package com.memomo.dto;

import com.memomo.entity.Comment;
import com.memomo.entity.Layout;
import com.memomo.entity.PostFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Data
public class PostDTO {
    // Post 영역
    private Long pno;
    private Integer bno;
    private String author;
    private String content;
    private String bgColor;
    private Long bgImage;
    private String pstatus;
    private Boolean vote;

    private PostFile file;              // 본 포스트잇의 파일
    private Layout layout;              // 본 포스트잇의 레이아웃

    private String action;      // 소켓 ctrl (add, edit, move, remove)
    private MultipartFile postFile;
}
