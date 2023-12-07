package com.memomo.ctrl;

import com.memomo.dto.BoardDTO;
import com.memomo.entity.BoardFile;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Log4j2
public class FileCotroller {

    @Value("org.zerock.upload.path")
    private String uploadPath;

    @PostMapping( value = "/boardUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BoardDTO upload(BoardDTO boardDTO) {
        log.info(boardDTO);
        if (boardDTO.getFile() != null){
            final BoardFile boardFile = new BoardFile();
            MultipartFile multipartFile = (MultipartFile) boardDTO.getFile();
            String originalName = multipartFile.getOriginalFilename();
            log.info(originalName);
            String saveName = UUID.randomUUID().toString() + "_" + originalName;
            Path savePath = Paths.get(uploadPath, saveName);
            boolean image = false;
            try {
                multipartFile.transferTo(savePath);
                // 이미지 파일이라면
                if (Files.probeContentType(savePath).startsWith("image")){
                    image = true;
                    File thumbnail = new File(uploadPath, "s_" + saveName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 200, 200);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            boardFile.setBno(boardDTO.getBno());
            boardFile.setOriginName(originalName);
            boardFile.setSaveName(saveName);
            boardFile.setSavePath(String.valueOf(savePath));
            boardFile.setFileType("image");
            boardFile.setStatus("ACTIVE");
        }
        return null;
    }

    @GetMapping("/view/{saveName}")
    public ResponseEntity<Resource> viewImage(@PathVariable String saveName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + saveName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @DeleteMapping("/remove/{saveName}")
    public Map<String, Boolean> removeFile(@PathVariable String saveName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + saveName);
        String resourceName = resource.getFilename();
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;
        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();
            if (contentType.startsWith("image")){
                File thumbnail = new File(uploadPath + File.separator + "s_" + saveName);
                thumbnail.delete();
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        resultMap.put("result", removed);
        return resultMap;
    }
}
