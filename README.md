# 📦 [Spring 2기] CH 4 클라우드 아키텍처 설계 & 배포

Spring Boot 애플리케이션을 AWS 환경에 배포하고,  
비용 관리 · 네트워크 구성 · 보안 설정 · 외부 서비스 연동까지  
**운영 가능한 클라우드 아키텍처**를 구축하는 것을 목표로 한 과제입니다.

---

## 🧭 목차
1. LV 0 – 요금 폭탄 방지 AWS Budget 설정  
2. LV 1 – 네트워크 구축 및 EC2 배포  
3. LV 2 – DB 분리 및 보안 연결 (RDS & Parameter Store)  
4. LV 3 – 프로필 사진 기능 추가 및 권한 관리  

---

## 1️⃣ LV 0 – 요금 폭탄 방지 AWS Budget 설정

클라우드 실습 중 발생할 수 있는 **예상치 못한 비용 문제를 방지**하기 위해  
AWS Budgets를 활용하여 사전 비용 관리 설정을 진행했습니다.

### ✅ 설정 내용
- 월 예산: **$100**
- 예산 사용률 **80% 도달 시 이메일 알림 설정**

### 📸 설정 완료 화면
<img width="1280" height="660" alt="KakaoTalk_20260130_172538284" src="https://github.com/user-attachments/assets/f386be9c-1316-419e-8544-13f6c6b101d5" />

---

## 2️⃣ LV 1 – 네트워크 구축 및 EC2 배포

퍼블릭 네트워크 환경에서 접근 가능한 EC2 인스턴스를 생성하고  
Spring Boot 애플리케이션을 배포하여 **운영 상태를 검증**했습니다.

### ✅ 배포 및 검증
- EC2에 애플리케이션 배포 및 실행 완료
- Actuator Health 엔드포인트를 통해 서버 정상 동작 확인

### 🔍 Health Check 결과 (로컬)
```http
GET http://localhost:8080/actuator/health
```
<img width="1148" height="959" alt="image" src="https://github.com/user-attachments/assets/e1b6dc1c-8e5d-4e0b-b5ee-bcbe6ed94edb" />

### 🌐 EC2 퍼블릭 IP
```
3.38.210.31
```

---

## 3️⃣ LV 2 – DB 분리 및 보안 연결하기

애플리케이션 설정 정보를 코드에서 분리하고  
AWS Parameter Store와 RDS를 활용하여 **보안성과 확장성을 강화**했습니다.

### ✅ 구현 목표
- DB 접속 정보 및 앱 설정 값을 Parameter Store로 관리
- EC2에서 해당 값을 안전하게 조회하여 애플리케이션 실행
- RDS를 퍼블릭 접근이 아닌 보안 그룹 기반 접근으로 제한

### ✅ 검증 항목
- EC2 환경에서 Actuator Info 엔드포인트 호출 시  
  Parameter Store에 저장된 값이 정상적으로 출력됨

### 🔍 Actuator Info 엔드포인트
```http
GET http://3.38.210.31:8080/actuator/info
```

### 📸 Actuator Info 출력 결과
<img width="1079" height="1057" alt="image" src="https://github.com/user-attachments/assets/5bd05d61-549b-47d9-88b3-bafaa5ec82ce" />

### 🔐 RDS 보안 그룹 설정
- EC2 보안 그룹에서만 접근 가능하도록 인바운드 규칙 설정
- 외부 IP 직접 접근 차단
<img width="2557" height="1218" alt="image" src="https://github.com/user-attachments/assets/6fa7fe5e-a208-4968-8c63-a83ede25aa5e" />

---

## 4️⃣ LV 3 – 프로필 사진 기능 추가와 권한 관리

AWS S3를 활용하여 **프로필 사진 업로드 기능**을 구현하고,  
IAM 권한 설정을 통해 **최소 권한 원칙(Least Privilege)** 기반의 접근 제어를 적용했습니다.

### ✅ 구현 목표
- 사용자 프로필 사진을 S3에 업로드
- 서버는 직접 파일을 저장하지 않고 S3를 스토리지로 사용
- IAM 정책을 통해 필요한 권한만 허용하여 보안 강화

### ✅ 구현 내용
- S3 버킷 생성 및 업로드 경로(`uploads/`) 구성
- IAM 정책을 통한 S3 접근 권한 관리
- `MultipartFile` 기반 이미지 업로드 기능 구현
- 업로드된 이미지 정상 조회 확인

### 🔐 권한 관리
- EC2 IAM Role에 S3 접근 권한 부여
- `s3:PutObject`, `s3:GetObject` 권한만 허용
- 불필요한 전체 권한(`s3:*`) 사용 지양

### 📸 구현 결과

<img width="1146" height="880" alt="image" src="https://github.com/user-attachments/assets/d18f4cc9-7d9a-471f-9269-fdcc1aea8de9" />

<img width="1156" height="1329" alt="image" src="https://github.com/user-attachments/assets/ffdc8af0-1561-4736-8e1a-681378d0eb96" />

---

### ✨ 정리
- 애플리케이션과 스토리지 역할을 분리하여 확장성과 안정성 확보
- IAM 최소 권한 원칙을 적용하여 보안 사고 가능성 최소화
- 실무 환경과 유사한 파일 업로드 아키텍처 경험
