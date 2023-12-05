package com.memomo.repository;

import com.memomo.dto.PageDTO;
import com.memomo.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    public Page<Board> searchPage(Pageable pageable, PageDTO pageDTO);
}
