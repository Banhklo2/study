package com.example.study.controller;

import com.example.study.dto.MemberCreateRequest;
import com.example.study.dto.MemberGetResponse;
import com.example.study.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
