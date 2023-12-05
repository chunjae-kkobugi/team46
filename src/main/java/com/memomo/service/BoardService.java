package com.memomo.service;

import com.memomo.dto.BoardDTO;
import com.memomo.dto.BoardPostDTO;
import com.memomo.dto.PageDTO;
import com.memomo.entity.Board;

public interface BoardService {
    public Integer boardAdd(BoardDTO boardDTO);
    public Integer boardEdit(BoardDTO boardDTO);
    public Integer boardRemove(Integer bno);

    public void boardDelete(Integer bno);

    public PageDTO<Board, BoardDTO> boardList(PageDTO<Board, BoardDTO> pageDTO);
    public BoardPostDTO boardDetail(Integer bno);
}
