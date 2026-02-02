package com.example.study.service;

import com.example.study.dto.MemberCreateRequest;
import com.example.study.dto.MemberGetResponse;
import com.example.study.entity.Member;
import com.example.study.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    /**
     * 팀원 저장
     */
    @Transactional
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
    @Transactional(readOnly = true)
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

    /**
     * 프로필 이미지 업로드 (S3 업로드 + key DB 저장)
     */
    @Transactional
    public void uploadProfileImage(Long memberId, MultipartFile file) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다."));

        String key = s3Service.upload(file);

        member.updateProfileImageKey(key);
    }

    /**
     * 프로필 이미지 조회 (Download URL 반환)
     */
    @Transactional(readOnly = true)
    public String getProfileImagePresignedUrl(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다."));

        String key = member.getProfileImageKey();
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("프로필 이미지가 없습니다.");
        }

        return s3Service.getDownloadUrl(key).toString();
    }
}
