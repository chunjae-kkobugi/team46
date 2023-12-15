package com.memomo.repository;

import com.memomo.dto.PageDTO;
import com.memomo.entity.Board;
import com.memomo.entity.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        // 부모 클래스인 QuerydslRepositorySupport 의 생성자를 호출
        // QuerydslRepositorySupport 의 생성자에는 Class 로 Board 를 주고 있다.
        super(Board.class);
    }

    @Override
    public Page<Board> searchPage(Pageable pageable, PageDTO pageDTO) {
        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);

        String[] types = pageDTO.getTypes();
        String keyword = pageDTO.getKeyword();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(types!=null && keyword!=null && !keyword.isEmpty()){
            for(String type: types){
                System.out.println(type);
                switch(type){
                    case "title":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "teacher":
                        booleanBuilder.or(board.teacher.contains(keyword));
                        break;
                }
            }
        }

        if(pageDTO.getTeacher() != "") {
            booleanBuilder.or(board.teacher.contains(pageDTO.getTeacher()));
        }

        if(pageDTO.getStatus() != "") {
            booleanBuilder.or(board.status.contains(pageDTO.getStatus()));
        }

        query.where(booleanBuilder);

        // 부모 클래스인 QuerydslRepositorySupport 를 상속하면서 BoardSearchImpl(this)에  querydsl 필드가 있음
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();

        return new PageImpl<>(list, pageable, query.fetchCount());
    }
}
