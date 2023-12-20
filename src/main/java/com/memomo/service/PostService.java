package com.memomo.service;

import com.memomo.dto.PostDTO;
import com.memomo.entity.Layout;
import com.memomo.entity.PostFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    public Long postAdd(PostDTO dto, MultipartFile postFile, String uploadDir);
    public Long postEdit(PostDTO dto, MultipartFile postFile, String uploadDir);
    public Long postRemove(Long pno, Long oldLeft, Long oldRight, Integer bno);
    public void postMove(Layout layout);

    public void postDelete(Long pno);
    public List<PostDTO> postList(Integer bno);
    public void postSort(Long oldBefore, Long oldNext, Long newBefore, Long newNext, Long changed, Integer bno);

    public PostDTO postGet(Long pno);
    public Long postAddFile(PostFile image);
    public List<PostDTO> postListAll(Integer bno);
    public PostDTO getPost(Long pno);
}
