package com.memomo.service;

import com.memomo.dto.MemberFormDTO;
import com.memomo.entity.Member;

public interface MemberService {
    void join(MemberFormDTO member);

    boolean idCheck(String id);

    String login(String id, String pw);

    void memberEdit(MemberFormDTO member);

    String memberRemove(String id);

    Member memberDetail(String id);

    Member saveMember(Member member);

    String getLoginId();
}
