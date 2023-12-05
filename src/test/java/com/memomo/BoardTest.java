package com.memomo;

import com.memomo.dto.BoardDTO;
import com.memomo.dto.BoardPostDTO;
import com.memomo.entity.Board;
import com.memomo.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
@Log4j2
public class BoardTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private ModelMapper mapper;

    @Test
    public void boardTestAll(){
        Random random = new Random();
        // 게시판 추가하기
        for(int i=0; i<100; i++){
            Board board = new Board();
            board.setTitle("title "+(random.nextInt(10) + 1));
            board.setTeacher("teacher "+(random.nextInt(10) + 1));
            BoardDTO dto = mapper.map(board, BoardDTO.class);
            boardService.boardAdd(dto);
        }


        BoardPostDTO bpdto = boardService.boardDetail(10);
        log.info(bpdto);

        bpdto.setMaxStudent(10);
        BoardDTO bdto = mapper.map(bpdto, BoardDTO.class);
        boardService.boardEdit(bdto);

        boardService.boardRemove(1);

        boardService.boardDelete(100);
    }

    @Test
    public void boardPostRemoveTest(){
        boardService.boardRemove(10);
    }
}
