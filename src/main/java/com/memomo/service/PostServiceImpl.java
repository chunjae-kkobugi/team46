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
    public Long postAdd(PostDTO dto, MultipartFile postFile, HttpServletRequest request){

        if (postFile != null && ! postFile.isEmpty()) {
            MultipartFile multipartFile = postFile;

//            // 서버 경로
//            ServletContext application = request.getSession().getServletContext();
//            String uploadDir = application.getRealPath("/images/postImage");

            // 로컬 경로
            String uploadDir = "C://upload/postImage/";

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
            image.setStatus("ACTIVE");

            Post post = mapper.map(dto, Post.class);
            postRepo.save(post);
            image.setPno(post.getPno());
            fileRepo.save(image);
            post.setBgImage(image.getFno());
            dto.setFile(image);
            postRepo.save(post);
            log.info("---------------------------------------- post : " + dto);

            return postRepo.save(post).getPno();
        } else {
            // 파일이 업로드 되지 않은 경우
            Post post = mapper.map(dto, Post.class);
            postRepo.save(post);
            log.info("포스트에 업로드 된 파일이 없습니다" + dto);
            return postRepo.save(post).getPno();
        }
    }

    @Override
    public Long postEdit(Post post) {
        return postRepo.save(post).getPno();
    }

    @Override
    public Long postRemove(Long pno) {
        Post post = postRepo.findById(pno).orElseThrow();
        post.setStatus("REMOVE");

        return postRepo.save(post).getPno();
    }

    @Override
    public void postDelete(Long pno) {
        postRepo.deleteById(pno);
    }

}
