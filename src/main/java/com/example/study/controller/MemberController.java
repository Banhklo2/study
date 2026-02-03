package com.example.study.controller;

import com.example.study.dto.MemberCreateRequest;
import com.example.study.dto.MemberGetResponse;
import com.example.study.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    // 회원 관련 비즈니스 로직을 처리하는 서비스
    private final MemberService memberService;

    /**
     * 회원 생성 API
     * - 요청 바디로 회원 정보 전달
     */
    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request) {
        memberService.createMember(request);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 단건 조회 API
     * - 회원 ID로 회원 정보 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberGetResponse> getMember(@PathVariable Long id) {
        MemberGetResponse response = memberService.getMember(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 프로필 이미지 업로드 API
     * - multipart/form-data 형식으로 파일 업로드
     * - 업로드된 이미지는 S3에 저장
     */
    @PostMapping("/{id}/profile-image")
    public ResponseEntity<Void> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        memberService.uploadProfileImage(id, file);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 프로필 이미지 조회 API
     * - S3 Presigned URL 반환
     * - 일정 시간 동안 인증 없이 접근 가능
     */
    @GetMapping("/{id}/profile-image")
    public ResponseEntity<ProfileImageResponse> getProfileImage(
            @PathVariable Long id
    ) {
        String url = memberService.getProfileImagePresignedUrl(id);
        return ResponseEntity.ok(new ProfileImageResponse(url));
    }

    /**
     * 프로필 이미지 응답 DTO
     * - Presigned URL만 클라이언트에 전달
     */
    public record ProfileImageResponse(String url) {}
}
