package com.memomo.service;

import com.memomo.dto.BoardPostDTO;
import com.memomo.dto.PostDTO;
import com.memomo.entity.Layout;
import com.memomo.entity.Post;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import java.util.LinkedList;
import java.util.List;

public interface PostService {
    public Long postAdd(PostDTO dto, MultipartFile postFile, String uploadDir);
    public Long postEdit(Post post);
    public Long postRemove(Long pno);
    public void postMove(Layout layout);

    public void postDelete(Long pno);
    public List<PostDTO> postList(Integer bno);

    public void postSort(Long oldBefore, Long oldNext, Long newBefore, Long newNext, Long changed, Integer bno);
    public PostDTO postGet(Long pno);
}
