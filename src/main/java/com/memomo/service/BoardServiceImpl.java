package com.memomo.service;

import com.memomo.dto.BoardDTO;
import com.memomo.dto.PageDTO;
import com.memomo.entity.Board;
import com.memomo.entity.BoardFile;
import com.memomo.repository.BoardFileRepository;
import com.memomo.repository.BoardRepository;
import com.memomo.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
//            String uploadDir = "D:\\kim\\project\\tproj\\project06\\team46\\src\\main\\resources\\static\\images\\boardImage\\";
            String uploadDir = "C:\\upload\\";

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
    public void boardEdit(BoardDTO boardDTO, MultipartFile boardFile, HttpServletRequest request) {

        // 파일 업로드
        String imageOriginName = "";
        String imageSaveName = "";

        if (boardFile != null && !boardFile.isEmpty()) {
            MultipartFile multipartFile = boardFile;

//            // 서버 경로
//            ServletContext application = request.getSession().getServletContext();
//            String uploadDir = application.getRealPath("/images/boardImage/");

            // 로컬 경로
//            String uploadDir = "D:\\kim\\project\\tproj\\project06\\team46\\src\\main\\resources\\static\\images\\boardImage\\";

            String uploadDir = "C:\\upload\\";
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
                if (Files.probeContentType(savePath).startsWith("image")) {
                    File thumbnail = new File(uploadPath, "s_" + saveName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 411, 255);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("업로드 실패" + e.getMessage());
            }
            BoardFile fileResult = boardFileRepo.findBoardFileByFno(boardDTO.getBgImage());
            fileResult.change("REMOVE");

            BoardFile image = new BoardFile();
            image.setBno(boardDTO.getBno());
            image.setOriginName(imageOriginName);
            image.setSaveName(imageSaveName);
            image.setSavePath(today);
            // 파일 확장자를 fileType 에 저장
            image.setFileType(fileExtension);
            image.setStatus("ACTIVE");

            Optional<Board> result = boardRepo.findById(boardDTO.getBno());
            Board board = result.orElseThrow();

            BoardFile existingFile = boardDTO.getFile(); // 실제 필드명으로 변경
            if (existingFile != null) {
                existingFile.setStatus("REMOVE");
            }

            board.change(boardDTO.getTitle(), boardDTO.getBpw(), boardDTO.getMaxStudent(), boardDTO.getBgColor(), boardDTO.getBgImage(), boardDTO.getStatus(), boardDTO.getLayout());
//            board.change(board.getTitle(), boardDTO. getBpw(), boardDTO.getMaxStudent(), boardDTO.getBgColor(), boardDTO.getBgImage());
            // 이미지 정보를 게시판 정보에 연결
            boardDTO.setFile(image);
            boardRepo.save(board);

            // 파일 저장
            boardFileRepo.save(image);
            board.setBgImage(image.getFno());
            boardRepo.save(board);
            log.info("-------------------------------------------------------------------------------" + image.getFno());
            log.info("---------------------------------------------board : " + board);
        } else {
            // 파일이 첨부되지 않은 경우에 실행되는 코드 블록
            // 파일이 첨부되지 않았으므로 파일 정보를 건드리지 않고 게시판 정보만 수정
            Optional<Board> result = boardRepo.findById(boardDTO.getBno());
            Board board = result.orElseThrow();

            // 게시판 정보만 업데이트 (파일 정보는 건드리지 않음)
            if (boardFile == null || boardFile.isEmpty()) {
                // 파일이 첨부되지 않은 경우, 기존 bgImage 값을 유지
                board.change(boardDTO.getTitle(), boardDTO.getBpw(), boardDTO.getMaxStudent(), boardDTO.getBgColor(), board.getBgImage(), boardDTO.getStatus(), boardDTO.getLayout());
            } else {
                // 파일이 첨부된 경우, 새로운 bgImage 값을 사용
                board.change(boardDTO.getTitle(), boardDTO.getBpw(), boardDTO.getMaxStudent(), boardDTO.getBgColor(), boardDTO.getBgImage(), boardDTO.getStatus(), boardDTO.getLayout());
            }

            boardRepo.save(board);
            log.info("게시판 정보 수정 완료: " + board);
        }
    }



    @Override
    public Integer boardRemove(Integer bno) {
        Optional<Board> result = boardRepo.findById(bno);
        Board board = result.orElseThrow();
        board.remove("REMOVE");

        boardFileRepo.boardFileRemove(bno);
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
    public BoardDTO boardDetail(Integer  bno) {
        Optional<Board> result = boardRepo.findById(bno);
        Board board = result.orElseThrow();
        BoardFile boardFile = boardFileRepo.findBoardFileByFno(board.getBgImage());
        BoardDTO dto = new BoardDTO();
        dto.setBno(board.getBno());
        dto.setTitle(board.getTitle());
        dto.setBpw(board.getBpw());
        dto.setStatus(board.getStatus());
        dto.setBgImage(board.getBgImage());
        dto.setBgColor(board.getBgColor());
        dto.setCreateAt(board.getCreateAt());
        dto.setLayout(board.getLayout());
        dto.setMaxStudent(board.getMaxStudent());
        dto.setTeacher(board.getTeacher());
        dto.setFile(boardFile);
        return dto;
    }

    @Override
    public BoardFile getBoardFile(Integer bno) {
        return boardFileRepo.findBoardFileByFno(bno);
    }

}
