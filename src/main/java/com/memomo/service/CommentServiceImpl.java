package com.memomo.service;

import com.memomo.dto.PageDTO;
import com.memomo.entity.Comment;
import com.memomo.repository.CommentRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public Comment commentAdd(Comment comment) {
        Comment register = commentRepository.save(comment);
        return register;
    }

    @Override
    public void commentDelete(Long cno) {
        commentRepository.deleteById(cno);
    }

    @Override
    public Long commentCount(Long pno) {
        return commentRepository.countByPno(pno);
    }
}
