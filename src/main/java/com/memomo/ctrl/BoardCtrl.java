package com.memomo.ctrl;

import com.memomo.dto.BoardDTO;
import com.memomo.dto.PageDTO;
import com.memomo.entity.Board;
import com.memomo.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/board/")
public class BoardCtrl {
    @Autowired
    private BoardService boardService;

    @RequestMapping("boardList")
    public String boardList(Model model, HttpServletRequest request){
        PageDTO<Board, BoardDTO> pageDTO = new PageDTO<>();

        String type = request.getParameter("type");
        String keyword = request.getParameter("keyword");
        int pageNow = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        pageDTO.setType(type);
        pageDTO.setKeyword(keyword);
        pageDTO.setPageNow(pageNow);

        pageDTO = boardService.boardList(pageDTO);
        List<BoardDTO> boardList = pageDTO.getListDTO();
        model.addAttribute("boardList", boardList);

        model.addAttribute("pageDTO", pageDTO);

        return "board/boardList";
    }
}
