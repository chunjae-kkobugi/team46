package com.memomo.service;

import com.memomo.dto.BoardPostDTO;
import com.memomo.dto.PostDTO;
import com.memomo.entity.Post;

public interface PostService {
    public Long postAdd(PostDTO dto);
    public Long postEdit(Post post);
    public Long postRemove(Long pno);

    public void postDelete(Long pno);
}
