package com.memomo.service;

import com.memomo.dto.BoardPostDTO;
import com.memomo.dto.PostDTO;
import com.memomo.entity.Post;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    public Long postAdd(PostDTO dto, MultipartFile postFile, HttpServletRequest request);
    public Long postEdit(Post post);
    public Long postRemove(Long pno);

    public void postDelete(Long pno);
}
