package com.memomo.service;

import com.memomo.dto.PageDTO;
import com.memomo.entity.Comment;

import java.util.List;

public interface CommentService {
    public Long commentAdd(Comment comment);
    public void commentDelete(Long cno);
}
