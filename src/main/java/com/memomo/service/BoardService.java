package com.memomo.service;

import com.memomo.dto.BoardDTO;
import com.memomo.dto.BoardPostDTO;
import com.memomo.dto.PageDTO;
import com.memomo.entity.Board;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BoardService {
    public Integer boardAdd(BoardDTO boardDTO, MultipartFile boardFile, HttpServletRequest request) throws IOException;
    public Integer boardEdit(BoardDTO boardDTO);
    public Integer boardRemove(Integer bno);

    public void boardDelete(Integer bno);

    public PageDTO<Board, BoardDTO> boardList(PageDTO<Board, BoardDTO> pageDTO);
    public BoardPostDTO boardDetail(Integer bno);
}
