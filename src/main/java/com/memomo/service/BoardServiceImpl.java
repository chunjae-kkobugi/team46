package com.memomo.service;

import com.memomo.dto.BoardDTO;
import com.memomo.dto.BoardPostDTO;
import com.memomo.dto.PageDTO;
import com.memomo.dto.PostDTO;
import com.memomo.entity.Board;
import com.memomo.entity.BoardFile;
import com.memomo.entity.Post;
import com.memomo.repository.BoardFileRepository;
import com.memomo.repository.BoardRepository;
import com.memomo.repository.PostRepository;
import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Log4j2
public class BoardServiceImpl implements BoardService{
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BoardRepository boardRepo;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private BoardFileRepository boardFileRepo;

    @Override
    public Integer boardAdd(BoardDTO boardDTO, MultipartFile boardFile, HttpServletRequest request) throws IOException {
        String imageOriginName = "";
        String imageSaveName = "";

        if (boardFile != null && !boardFile.isEmpty()) {
            MultipartFile multipartFile = boardFile;

//            // 서버 경로
//            ServletContext application = request.getSession().getServletContext();
//            String uploadDir = application.getRealPath("/images/boardImage/");

            // 로컬 경로
            String uploadDir = "D:\\kim\\project\\tproj\\project06\\team46\\src\\main\\resources\\static\\images\\boardImage\\";

            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = uploadDir + today;
            System.out.println(saveFolder);


            File uploadPath = new File(saveFolder);
            // 업로드 날짜의 폴더가 없다면 새로 생성
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            String originalName = boardFile.getOriginalFilename();
            imageOriginName = originalName;

            String uuid = UUID.randomUUID().toString();

            String saveName = uuid + "_" + originalName;
            imageSaveName = saveName;

            String fileExtension = "";

            // 파일 이름에 확장자가 있는지 확인
            if (originalName != null) {
                int lastIndex = originalName.lastIndexOf(".");
                if (lastIndex != -1 && lastIndex < originalName.length() - 1) {
                    fileExtension = originalName.substring(lastIndex + 1);
                }
            }

            Path savePath = Paths.get(String.valueOf(uploadPath), saveName);
            try {
                multipartFile.transferTo(new File(uploadPath, saveName));
                if (Files.probeContentType(savePath).startsWith("image")){
                    File thumbnail = new File(uploadPath, "s_" + saveName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 411, 255);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("업로드 실패" + e.getMessage());
            }
            BoardFile image = new BoardFile();
            image.setOriginName(imageOriginName);
            image.setSaveName(imageSaveName);
            image.setSavePath(today);
            // 파일 확장자를 fileType 에 저장
            image.setFileType(fileExtension);
            image.setStatus("ACTIVE");

            Board board = modelMapper.map(boardDTO, Board.class);
            boardRepo.save(board);
            image.setBno(board.getBno());
            boardFileRepo.save(image);
            board.setBgImage(image.getFno());
            boardDTO.setFile(image);
            boardRepo.save(board);
            log.info("---------------------------------------------------- board : " + boardDTO);

            return boardRepo.save(board).getBno();
        } else {
            // 파일이 업로드되지 않은 경우 처리
            Board board = modelMapper.map(boardDTO, Board.class);
            boardRepo.save(board);
            log.info("게시글에 업로드된 파일이 없습니다: " + boardDTO);
            return boardRepo.save(board).getBno(); // 기본 값 반환하거나 필요한 작업 수행
        }
    }

    @Override
    public Integer boardEdit(BoardDTO boardDTO) {
        Board board = modelMapper.map(boardDTO, Board.class);
        return boardRepo.save(board).getBno();
    }

    @Override
    public Integer boardRemove(Integer bno) {
        Optional<Board> result = boardRepo.findById(bno);
        Board board = result.orElseThrow();
        board.setStatus("REMOVE");

        postRepo.boardPostRemove(bno);

        return boardRepo.save(board).getBno();
    }

    @Override
    public void boardDelete(Integer  bno) {
        boardRepo.deleteById(bno);
    }

    @Override
    public PageDTO<Board, BoardDTO> boardList(PageDTO<Board, BoardDTO> pageDTO) {
        Pageable pageable = pageDTO.getPageable();
        Page<Board> result = boardRepo.searchPage(pageable, pageDTO);
        pageDTO.build(result);
        pageDTO.entity2dto(result, BoardDTO.class);
        return pageDTO;
    }

    @Override
    public Board boardDetail(Integer  bno) {
        Optional<Board> result = boardRepo.findById(bno);
        List<Post> postList = postRepo.postListByBno(bno);
        Board board = result.orElseThrow();
        BoardPostDTO dto = modelMapper.map(board, BoardPostDTO.class);
        dto.setPosts(postList);
        return board;
    }

    @Override
    public BoardFile getBoardFile(Integer bno) {
        return boardFileRepo.findBoardFileByBno(bno);
    }

}
