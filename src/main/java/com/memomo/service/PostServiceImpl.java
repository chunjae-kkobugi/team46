package com.memomo.service;

import com.memomo.dto.BoardPostDTO;
import com.memomo.dto.PostDTO;
import com.memomo.entity.Layout;
import com.memomo.entity.Post;
import com.memomo.entity.PostFile;
import com.memomo.repository.LayoutRepository;
import com.memomo.repository.PostFileRepository;
import com.memomo.repository.PostRepository;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import java.util.*;

@Transactional
@Service
@Log4j2
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
    public Long postAdd(PostDTO dto, MultipartFile postFile, String uploadDir, Long oldTail){
        Post post = mapper.map(dto, Post.class);

        if(oldTail == null || oldTail == 1) {
            // 아예 하나도 없는 경우 내가 head
            Post head = postRepo.postHeadGet(dto.getBno());
            if(head==null){
                head = new Post();
                head.setBno(dto.getBno());
                head.setPstatus("HEAD");
                head.setAuthor("admin");
                head.setContent("This is Head");
                Long p = postRepo.save(head).getPno();
                head.setPno(p);

                Layout layout = new Layout();
                layout.setPno(p);
                layoutRepo.save(layout);
            }
            oldTail = head.getPno();
            System.out.println("=====oldTail"+oldTail);
        }

        // 마지막 꼬리에 현재 추가한 포스트 추가
        Long pno = postRepo.save(post).getPno();
        System.out.println(pno+"========"+oldTail);
        layoutRepo.layoutPriority(pno, oldTail);

        // 내 위치 저장
        Layout layout = mapper.map(dto.getLayout(), Layout.class);
        layout.setPno(pno);
        layoutRepo.save(layout);

        if (postFile != null && ! postFile.isEmpty()) {
            MultipartFile multipartFile = postFile;

            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = uploadDir + today;
            System.out.println(saveFolder);

            File uploadPath = new File(saveFolder);
            // 업로드 날짜의 폴더가 없다면 새로 생성
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            String originalName = postFile.getOriginalFilename();
            String fileExtension = "";

            // 파일 이름에 확장자가 있는지 확인
            if (originalName != null) {
                int lastIndex = originalName.lastIndexOf(".");
                if (lastIndex != -1 && lastIndex < originalName.length() - 1) {
                    fileExtension = originalName.substring(lastIndex + 1);
                }
            }

            String uuid = UUID.randomUUID().toString();
            String saveName = uuid + "_" + originalName;

            Path savePath = Paths.get(String.valueOf(uploadPath), saveName);

            try {
                multipartFile.transferTo(new File(uploadPath, saveName));
                if (Files.probeContentType(savePath).startsWith("image")){
                    File thumbnail = new File(uploadPath, "s_" + saveName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 300, 300);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("업로드 실패" + e.getMessage());
            }

            PostFile image = new PostFile();
            image.setOriginName(originalName);
            image.setSaveName(saveName);
            image.setSavePath(today);
            // 파일 확장자를 fileType 에 저장
            image.setFileType(fileExtension);
            image.setFstatus("ACTIVE");

            image.setPno(pno);
            fileRepo.save(image);

            post.setBgImage(image.getFno());
            postRepo.save(post);
            log.info("---------------------------------------- post : " + post);


            System.out.println(layout);
            layoutRepo.save(layout);
        } else {
            // 파일이 업로드 되지 않은 경우
            log.info("포스트에 업로드 된 파일이 없습니다" + dto);
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
