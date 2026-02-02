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

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request) {
        memberService.createMember(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberGetResponse> getMember(@PathVariable Long id) {
        MemberGetResponse response = memberService.getMember(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<Void> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        memberService.uploadProfileImage(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/profile-image")
    public ResponseEntity<ProfileImageResponse> getProfileImage(
            @PathVariable Long id
    ) {
        String url = memberService.getProfileImagePresignedUrl(id);
        return ResponseEntity.ok(new ProfileImageResponse(url));
    }

    public record ProfileImageResponse(String url) {}
}
