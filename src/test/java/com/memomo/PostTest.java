package com.memomo;

import com.memomo.dto.PostDTO;
import com.memomo.entity.Layout;
import com.memomo.entity.Post;
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
    private ModelMapper mapper;

    @Test
    public void postAddTest(){
        Random random = new Random();
        if(true){
            Post post = new Post();
            post.setBno(1);
            post.setPstatus("HEAD");
            post.setAuthor("admin");
            post.setContent("Head");
            PostDTO dto = mapper.map(post, PostDTO.class);

            Layout layout = new Layout();
            layout.setPriority(2L);

            dto.setLayout(layout);

            postService.postAdd(dto);
        }

        for(int i=2; i<=100; i++){
            Post post = new Post();

            post.setBno(((i-1)/10+1));
            if((i-1)%10==0){
                post.setPstatus("HEAD");
            }

            post.setAuthor("nickname "+random.nextInt(1, 6));
            post.setContent("content "+i%10);

            PostDTO dto = mapper.map(post, PostDTO.class);

            Layout layout = new Layout();
            if(i%10!=0){
                layout.setPriority((long) (i+1));
            }
            dto.setLayout(layout);

            postService.postAdd(dto);
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
