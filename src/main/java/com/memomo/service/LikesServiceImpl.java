package com.memomo.service;

import com.memomo.entity.Likes;
import com.memomo.repository.LikesRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class LikesServiceImpl implements LikesService{
    @Autowired
    private LikesRepository likesRepo;

    @Override
    public int toggleLikes(Integer bno, Long pno, String author) {
        if(likesRepo.hasLikes(pno, author)==0){
            Likes likes = new Likes();
            likes.setBno(bno);
            likes.setPno(pno);
            likes.setAuthor(author);
            likesRepo.save(likes);
        } else {
            likesRepo.deleteLike(pno, author);
        }
        return likesRepo.countLikeByPno(pno);
    }

    @Override
    public List<Likes> myLikes(Integer bno, String author) {
        return likesRepo.findLikesByBnoAndAuthor(bno, author);
    }
}
