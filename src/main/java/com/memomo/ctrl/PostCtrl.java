package com.memomo.ctrl;

import com.memomo.dto.BoardPostDTO;
import com.memomo.dto.PostDTO;
import com.memomo.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/post/")
public class PostCtrl {
    @Autowired
    private PostService postService;

    @PostMapping("register")
    public String postRegister(PostDTO dto, MultipartFile postFile, HttpServletRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        log.info("post register start------------------------------");

        if (bindingResult.hasErrors()) {
            log.info("has error-------------------------------------------");
            redirectAttributes.addFlashAttribute("errror", bindingResult);
            return "redirect:/post/register";
        }
        log.info(dto);
        String sid = (String) session.getAttribute("sid");
        dto.setAuthor(sid);
        dto.setBno(dto.getBno());
        Long pno = postService.postAdd(dto, postFile, request);
        redirectAttributes.addFlashAttribute("result", pno);
        return "redirect:/board/detail?bno="+ dto.getBno();
    }
}
