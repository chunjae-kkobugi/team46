package com.memomo.service;

import com.memomo.entity.Likes;
import com.memomo.repository.LikesRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LikesServiceImpl implements LikesService{
    @Autowired
    private LikesRepository likesRepo;

    @Override
    public int addLike(Long pno, String author) {
        if(likesRepo.hasLikes(pno, author)==0){
            Likes likes = new Likes();
            likes.setPno(pno);
            likes.setAuthor(author);
            likesRepo.save(likes);
        }
        return likesRepo.countLikeByPno(pno);
    }

    @Override
    public int removeLikes(Long pno, String author) {
        log.info("=====DELETE: "+likesRepo.deleteLikeByPnoAndAuthor(pno, author));

        return likesRepo.countLikeByPno(pno);
    }
}
