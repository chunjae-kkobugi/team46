package com.memomo.service;

import com.memomo.entity.BoardGroup;

import java.util.List;

public interface BoardGroupService {
    public List<BoardGroup> groupList(Integer bno);
    public Integer groupAdd(BoardGroup boardGroup);
    public void groupEdit(BoardGroup boardGroup);
    public void groupRemove(Integer gno);
}
