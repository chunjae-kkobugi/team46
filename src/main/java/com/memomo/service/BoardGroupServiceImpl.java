package com.memomo.service;

import com.memomo.entity.BoardGroup;
import com.memomo.repository.GroupRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class BoardGroupServiceImpl implements BoardGroupService{
    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<BoardGroup> groupList(Integer bno) {
        List<BoardGroup> boardGroupList = groupRepository.findBoardGroupByBno(bno);
        return boardGroupList;
    }

    @Override
    public BoardGroup groupAdd(BoardGroup boardGroup) {
        return groupRepository.save(boardGroup);
    }

    @Override
    public BoardGroup groupEdit(BoardGroup boardGroup) {
        Optional<BoardGroup> result = groupRepository.findById(boardGroup.getGno());
        BoardGroup group = result.orElseThrow();
        group.change(boardGroup.getTitle(), boardGroup.getGColor());
        return groupRepository.save(group);
    }

    @Override
    public void groupRemove(Integer gno) {
        groupRepository.deleteById(gno);
    }
}
