package com.memomo;

import com.memomo.dto.PostDTO;
import com.memomo.entity.Post;
import com.memomo.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
@Log4j2
public class PostTest {
    @Autowired
    private PostService postService;
    @Autowired
    private ModelMapper mapper;

//    @Test
//    public void postAddTest(){
//        Random random = new Random();
//        for(int i=0; i<100; i++){
//            Post post = new Post();
//            post.setBno((i%10)+10);
//            post.setAuthor("nickname "+(random.nextInt(10) + 1));
//            post.setContent("content "+(random.nextInt(10) + 1));
//
//            PostDTO dto = mapper.map(post, PostDTO.class);
//            postService.postAdd(dto);
//        }
//    }
}
