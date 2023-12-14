package com.memomo.ctrl;

import com.memomo.entity.Post;
import com.memomo.repository.PostRepository;
import com.memomo.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Controller
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/post/")
public class PostCtrl {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepo;

    /*@PostMapping("addPro")
    @ResponseBody
    public String postAddPro(@RequestParam("postFile") MultipartFile postFile, HttpServletRequest request) {
        HttpSession session = request.getSession();
        log.info("post register start------------------------------");
        *//*if (bindingResult.hasErrors()) {
            log.info("has error-------------------------------------------");
            return 0L;
        }*//*

*//*        String sid = (String) session.getAttribute("sid");
        dto.setAuthor(sid);
        dto.setPstatus("ACTIVE");*//*
        // 로컬 경로
        String uploadDir = "D:\\sangmin0816\\kkobugi\\team46\\src\\main\\resources\\static\\images\\";

//        서버 경로
//            ServletContext application = request.getSession().getServletContext();
//            String uploadDir = application.getRealPath("/images/postImage");
*//*
        Long pno;
        MultipartFile postFile = dto.getPostFile();


        if(postFile==null){
            pno = postService.postAdd(dto, null, uploadDir);
        } else{
            pno = postService.postAdd(dto, postFile, uploadDir);
        }*//*

        Long fno = postService.postAddFile(postFile, uploadDir);
        System.out.println(fno);
        return "ADD SUCCESS";
    }*/

    @PostMapping("/addPro")
    @ResponseBody
    public CompletableFuture<String> postAddPro(@RequestParam("postFile") MultipartFile postFile, HttpServletRequest request) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // 파일 저장 처리를 비동기로 진행
        CompletableFuture.supplyAsync(() -> {
            HttpSession session = request.getSession();
            log.info("post register start------------------------------");

            String uploadDir = "D:\\sangmin0816\\kkobugi\\team46\\src\\main\\resources\\static\\images\\";

            Long fno = postService.postAddFile(postFile, uploadDir);
            System.out.println(fno);

            return "ADD SUCCESS";
        }).thenAccept(result -> {
            // 파일 저장 완료 후 처리할 작업
            future.complete(result); // 완료된 결과를 반환
        });

        return future;
    }

    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        Post post = new Post();
        post.setAuthor("test");
        post.setBno(0);
        post.setContent("test");
        postRepo.save(post);
        return "HELLO";
    }
}
