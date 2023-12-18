package com.memomo;

import com.memomo.dto.PostDTO;
import com.memomo.entity.Layout;
import com.memomo.entity.Post;
import com.memomo.repository.PostRepository;
import com.memomo.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

@SpringBootTest
@Log4j2
public class PostTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private ModelMapper mapper;

    @Test
    public void postAddTest(){
        Random random = new Random();
        String uploadDir = "D:\\sangmin0816\\kkobugi\\team46\\src\\main\\resources\\static\\images\\postImage";

        for(int i=2; i<=12; i++){
            Post post = new Post();
            post.setBno(1);

            post.setAuthor("nickname "+random.nextInt(1, 6));
            post.setContent("content "+i);

            PostDTO dto = mapper.map(post, PostDTO.class);

            Layout layout = new Layout();
            dto.setLayout(layout);

            Long oldTail = (long) i-1;
            postService.postAdd(dto, null, uploadDir);
        }
    }

    @Test
    public void postMoveTest(){
        Layout layout = new Layout();
        layout.setPno(1L);
        layout.setPriority(3L);
        postService.postMove(layout);
    }

    @Test
    public void postSelect(){
        List<PostDTO> postDTOs = postService.postList(1);
    }
}
