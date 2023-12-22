package com.memomo.service;

import com.memomo.dto.BoardDTO;
import com.memomo.dto.PageDTO;
import com.memomo.entity.Board;
import com.memomo.entity.BoardFile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BoardService {
    public Integer boardAdd(BoardDTO boardDTO, MultipartFile boardFile, HttpServletRequest request) throws IOException;
    public void boardEdit(BoardDTO boardDTO, MultipartFile boardFile, HttpServletRequest request);
    public Integer boardRemove(Integer bno);

    public void boardDelete(Integer bno);

    public PageDTO<Board, BoardDTO> boardList(PageDTO<Board, BoardDTO> pageDTO);

    public BoardDTO boardDetail(Integer bno); // 굳이 BoardPostDTO 로 받을 필요 없어서 Board 로 변경


    public BoardFile getBoardFile(Integer bno);
}
