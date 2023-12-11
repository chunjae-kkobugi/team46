package com.memomo.service;

import com.memomo.dto.PostDTO;
import com.memomo.entity.Layout;
import com.memomo.entity.Post;
import com.memomo.entity.PostFile;
import com.memomo.repository.LayoutRepository;
import com.memomo.repository.PostFileRepository;
import com.memomo.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private LayoutRepository layoutRepo;
    @Autowired
    private PostFileRepository fileRepo;

    @Override
    public Long postAdd(PostDTO dto){
        // bno, author, content 필수
        Post post = mapper.map(dto, Post.class);
        Long pno = postRepo.save(post).getPno();

        Layout layout = mapper.map(dto.getLayout(), Layout.class);
        layout.setPno(pno);
        System.out.println(layout);
        layoutRepo.save(layout);

        if(dto.getFile()!=null){
            PostFile file = mapper.map(dto.getFile(), PostFile.class);
            file.setPno(pno);
            fileRepo.save(file);
        }

        return pno;
    }

    @Override
    public Long postEdit(Post post) {
        return postRepo.save(post).getPno();
    }

    @Override
    public Long postRemove(Long pno) {
        Post post = postRepo.findById(pno).orElseThrow();
        post.setPstatus("REMOVE");

        return postRepo.save(post).getPno();
    }

    @Override
    public void postMove(Layout layout) {
        Layout origin = layoutRepo.findByPno(layout.getPno()).orElseThrow();
        mapper.map(origin, layout);
        layoutRepo.save(origin);
    }

    @Override
    public void postDelete(Long pno) {
        postRepo.deleteById(pno);
    }

    @Override
    public List<PostDTO> postList(Integer bno) {
        List<PostDTO> postDTOS = new ArrayList<>();
        List<PostDTO> sortedDTO = new ArrayList<>();

        List<Object[]> objects = postRepo.postDTOList(bno);
        for(Object[] o: objects){
            Post p = (Post) o[0];
            Layout l = (Layout) o[1];
            PostDTO dto = new PostDTO();
            dto = mapper.map(p, PostDTO.class);
            dto.setLayout(l);
            postDTOS.add(dto);
        }

        PostDTO head = postDTOS.stream().filter(p->p.getPstatus().equals("HEAD")).findFirst().orElseThrow();
        Long headP = head.getLayout().getPriority();
        while(headP!= 0){
            Long finalHeadP = headP;
            PostDTO post = postDTOS.stream().filter(p->p.getPno().equals(finalHeadP)).findFirst().orElseThrow();
            sortedDTO.add(post);
            headP = post.getLayout().getPriority();
        }

        return sortedDTO;
    }

    @Transactional
    @Override
    public void postSort(Long oldBefore, Long oldNext, Long newBefore, Long newNext, Long changed, Integer bno) {
        // 나의 기존 이전 노드에 나의 기존 다음 노드의 값을 넣어야 함
        if(oldBefore==0){
            // 내가 head 였다면 head 값을 바꿔야 함
            Post head = postRepo.postHeadGet(bno);
            layoutRepo.layoutPriority(oldNext, head.getPno());
        } else {
            layoutRepo.layoutPriority(oldNext, oldBefore);
        }

        // 나의 새로운 이전 노드
        if(newBefore==0){
            // 내가 새로운 head 가 될 때, head 에 나를 넣어줌
            Post head = postRepo.postHeadGet(bno);
            layoutRepo.layoutPriority(changed, head.getPno());
        } else {
            layoutRepo.layoutPriority(changed, newBefore);
        }

        // 나의 새로운 다음 노드(새로운 이전 노드가 원래 가지고 있던 값)
        layoutRepo.layoutPriority(newNext, changed);
    }
}
