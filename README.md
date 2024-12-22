Security Simple Project

### Requirement

- spring security 의존성을 제거, 인증인가 프로세스를 단순화한다
- Controller Layer 에서 인증 정보를 AOP로 주입받는다
- CORS 설정과 인증이 필요하지 않은 url 또한 정의가 가능해야한다
- application.yml 에서 설정에 필요0한 변수를 주입받는다

### Implements
- JwtTokenProvider
  1. JWT 토큰 생성
  2. 생성된 토큰 해석
  3. 해석한 정보를 바탕으로 인증정보 반환
  

- SecurityProperties
  1. cors 정보
  2. no auth url 정보
  3. jwt 설정 정보


- Filter
  - CorsFilter
    1. 환경변수로 주입한 CORS 설정을 Filter 에 적용
    2. Header 정보 설정 구체화 필요
  - JwtAuthFilter
    1. 요청 인가를 담당
    2. noAuthUrl에 대해는 허용
    3. 인증 실패 시의 ErrorResponse 설정
    4. 이외의 url에 대해서 검증 후 AuthUser 객체 생성


- AuthProviderArgumentResolver
  1. @AuthProvider annotation 에 대한 설정
  2. AuthContext 에서 AuthUser 객체를 resolve


- WebConfig
  - AuthProviderArgumentResolver를 WebMvcConfigurer로 등록

### To Library
1. 로컬 jar 사용
```groovy
dependencies {
    implementation files('/path/to/app.jar')
}
```
2. 로컬 maven repository 사용
```bash
./gradlew publishToMavenLocal
# ~/.m2/repository/com/yourgroup/auth-library/
```
```groovy
repositories {
    mavenLocal()
}

dependencies {
    implementation 'com.yourgroup:auth-library:0.0.1-SNAPSHOT'
}
```
3. 원격 repository 사용(Maven Central, JitPack, Nexus 등)
```groovy
publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java
        }
    }
    repositories {
        maven {
            name = "MyRepo"
            url = uri("https://my-nexus-repository.com/repository/maven-releases/")
            credentials {
                username = 'your-username'
                password = 'your-password'
            }
        }
    }
}
```
```groovy
repositories {
    maven {
        url 'https://my-nexus-repository.com/repository/maven-releases/'
    }
}

dependencies {
    implementation 'com.yourgroup:auth-library:0.0.1'
}
```