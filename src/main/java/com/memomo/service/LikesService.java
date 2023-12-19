package com.memomo.service;

import com.memomo.entity.Likes;

import java.util.List;

public interface LikesService {
    public int toggleLikes(Integer bno, Long pno, String author);
    public List<Likes> myLikes(Integer bno, String author);
}
