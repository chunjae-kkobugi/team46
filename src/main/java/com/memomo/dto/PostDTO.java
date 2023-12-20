package com.memomo.dto;

import com.memomo.entity.Comment;
import com.memomo.entity.Layout;
import com.memomo.entity.Post;
import com.memomo.entity.PostFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    private Layout layout;              // 본 포스트잇의 레이아웃
    private PostFile file;              // 본 포스트잇의 파일
    private Long likes;                 // 좋아요 수
    private Long comments;              // 댓글 개수
    private List<Comment> commentList;  // 댓글 목록

    public PostDTO(Post p, Layout l, PostFile pf, Long likes, Long comments) {
        this.pno = p.getPno();
        this.bno = p.getBno();
        this.author = p.getAuthor();
        this.content = p.getContent();
        this.bgColor = p.getBgColor();
        this.bgImage = p.getBgImage();
        this.pstatus = p.getPstatus();
        this.vote = p.getVote();

        this.layout = l;
        this.file = pf;
        this.likes = likes;
        this.comments = comments;
    }
}