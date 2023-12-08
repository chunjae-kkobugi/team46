package com.memomo.service;

import com.memomo.dto.BoardDTO;
import com.memomo.dto.BoardPostDTO;
import com.memomo.dto.PageDTO;
import com.memomo.entity.Board;
import com.memomo.entity.BoardFile;
import com.memomo.repository.BoardFileRepository;
import com.memomo.repository.BoardRepository;
import com.memomo.repository.PostRepository;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
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
import java.text.SimpleDateFormat;
import java.util.Date;
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
        String imageSavePath = "";

        if (boardFile != null && !boardFile.isEmpty()) {
            MultipartFile multipartFile = boardFile;
            ServletContext application = request.getSession().getServletContext();
//            // 서버경로
//            String uploadDir = application.getRealPath("/upload/");
            Resource resource = new ClassPathResource("/static/images/boardImage");
            String uploadDir = resource.getFile().getAbsolutePath();
//            String uploadDir = "C://upload/";
            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = uploadDir +'\\'+ today;

            File uploadPath = new File(saveFolder);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            imageSavePath = saveFolder;

            String originalName = boardFile.getOriginalFilename();
            imageOriginName = originalName;

            String uuid = UUID.randomUUID().toString();

            String saveName = uuid + "_" + originalName;
            imageSaveName = saveName;

            try {
                multipartFile.transferTo(new File(uploadPath, saveName));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("업로드 실패" + e.getMessage());
            }
            BoardFile image = new BoardFile();
            image.setOriginName(imageOriginName);
            image.setSaveName(imageSaveName);
            image.setSavePath(imageSavePath);
            image.setFileType("bgImage");
            image.setStatus("ACTIVE");

            Board board = modelMapper.map(boardDTO, Board.class);
            boardRepo.save(board);
            image.setBno(board.getBno());
            boardFileRepo.save(image);
            board.setBgImage(image.getFno());
            boardDTO.setFile(image);
            boardRepo.save(board);
            log.info("--------------------------------------------------------------------------------------------- board" + boardDTO);

            return boardRepo.save(board).getBno();
        }
        return null;
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
    public BoardPostDTO boardDetail(Integer  bno) {
        Optional<Board> result = boardRepo.findById(bno);
        Board board = result.orElseThrow();
        BoardPostDTO dto = modelMapper.map(board, BoardPostDTO.class);
        return dto;
    }
}
