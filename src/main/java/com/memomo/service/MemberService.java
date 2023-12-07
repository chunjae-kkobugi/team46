package com.memomo.service;

import com.memomo.dto.MemberDTO;
import com.memomo.entity.Member;

public interface MemberService {
    public void join(MemberDTO member);
    public boolean idCheck(String id);
    public String login(String id, String pw);
    public Member memberEdit(Member member);
    public String memberRemove(String id);
    public Member memberDetail(String id);
}
