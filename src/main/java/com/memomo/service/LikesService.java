package com.memomo.service;

public interface LikesService {
    public int addLike(Long pno, String author);
    public int removeLikes(Long pno, String author);
}
