package com.example.study.dto;

import lombok.Getter;

@Getter
public class MemberGetResponse {

    private final Long id;
    private final String name;
    private final int age;
    private final String mbti;

    public MemberGetResponse(Long id, String name, int age, String mbti) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.mbti = mbti;
    }
}
