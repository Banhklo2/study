package com.example.study.service;

import com.example.study.entity.Member;
import com.example.study.dto.MemberCreateRequest;
import com.example.study.dto.MemberGetResponse;
import com.example.study.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 팀원 저장
     */
    public void createMember(MemberCreateRequest request) {
        Member member = new Member(
                request.getName(),
                request.getAge(),
                request.getMbti()
        );

        memberRepository.save(member);
    }

    /**
     * 팀원 단건 조회
     */
    public MemberGetResponse getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다."));

        return new MemberGetResponse(
                member.getId(),
                member.getName(),
                member.getAge(),
                member.getMbti()
        );
    }
}
