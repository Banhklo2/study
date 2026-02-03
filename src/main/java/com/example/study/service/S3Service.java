package com.example.study.service;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    // Presigned URL 유효 기간 (7일)
    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);

    // AWS S3 작업을 위한 S3Template 빈 주입
    private final S3Template s3Template;

    // application.yml 에 설정한 S3 버킷 이름 주입
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일 업로드
     * - 파일명 충돌 방지를 위해 UUID 사용
     * - uploads/ 경로에 저장
     */
    public String upload(MultipartFile file) {
        try {
            String key = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            s3Template.upload(bucket, key, file.getInputStream());
            return key;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    /**
     * S3 파일 다운로드용 Presigned URL 생성
     * - 인증 없이 일정 시간 동안 접근 가능
     */
    public URL getDownloadUrl(String key) {
        return s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION);
    }
}
