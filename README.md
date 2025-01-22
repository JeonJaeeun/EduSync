# 📚 EduSync

기본 개발구조
```java
project-root/
 ├── HELP.md                        # 프로젝트 도움말 파일
 ├── README.md                      # 프로젝트 설명 및 가이드
 ├── build.gradle                   # Gradle 빌드 스크립트
 ├── settings.gradle                # Gradle 설정 파일
 ├── gradle/
 │    └── wrapper/                  # Gradle Wrapper 관련 파일
 │         ├── gradle-wrapper.jar
 │         └── gradle-wrapper.properties
 ├── src/
 │    ├── main/
 │    │    ├── java/                # Java 소스 코드 디렉터리
 │    │    │    └── org/
 │    │    │         └── edusync/
 │    │    │              ├── tutor/                # 도메인 관련 소스 코드
 │    │    │              │    ├── controller/      # 컨트롤러 계층
 │    │    │              │    ├── service/         # 서비스 계층
 │    │    │              │    ├── repository/      # 리포지토리 계층
 │    │    │              │    ├── dto/             # 데이터 전송 객체 (DTO)
 │    │    │              │    └── model/           # 엔티티 클래스
 │    │    │              ├── config/               # 설정 클래스
 │    │    │              └── common/               # 공통 유틸리티, 예외 처리 등
 │    │    ├── resources/           # 리소스 파일 디렉터리
 │    │    │    ├── application.yml              # Spring 설정 파일
 │    │    │    ├── schema.sql                  # H2 데이터베이스 초기화 스크립트 (테이블 정의)
 │    │    │    ├── data.sql                    # H2 데이터베이스 초기화 스크립트 (샘플 데이터)
 │    │    │    ├── static/                     # 정적 리소스 (HTML, CSS, JS)
 │    │    │    └── templates/                  # Thymeleaf 템플릿
 │    │    └── webapp/                          # JSP 뷰 파일 (필요 시)
 │    └── test/
 │         ├── java/                            # 테스트 클래스
 │         │    └── org/
 │         │         └── edusync/
 │         │              ├── tutor/                # 테스트 소스
 │         │              │    ├── TutorApplicationTests.java ... 등.
 │         └── resources/                       # 테스트 리소스
 ├── gradlew                            # Gradle 실행 스크립트 (Linux/Mac)
 ├── gradlew.bat                        # Gradle 실행 스크립트 (Windows)
 └── .gitignore                         # Git에 포함하지 않을 파일 정의
```