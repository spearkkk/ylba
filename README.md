# YLBA

스프링부트를 이용해 구성하였습니다.
- [x] 회원가입: 구현 완료
- [x] 로그인: 구현 완료
- [x] 내 정보 보기: 구현 완료
- [x] 비밀번호 재설정: 구현 완료

### 구현 방법
회원 가입과 전화번호 인증은 퍼블릭으로 오픈해서 구현하였습니다.  
로그인은 스프링 시큐리티를 사용했습니다. `json`을 지원하기 위해 `CustomUsernamePasswordAuthenticationFilter.class` 를 도입하여 구현했습니다.  
내 정보 보기를 위해 로그인한 사용자를 서버의 메모리에 담아서 사용하고 있고, 권한 체크를 성공해야 조회할 수 있도록 했습니다.  

### 로컬에서 실행하는 방법
*JDK 11가 필요함*

```shell
# project root directory
 ./gradlew clean build
```

```shell
# project root directory
java -jar ./build/libs/ylba-0.0.1-SNAPSHOT.jar
```

### 로컬에서 확인하는 방법
[http://localhost:10001/swagger-ui/index.html#/](http://localhost:10001/swagger-ui/index.html#/)에서 기본적인 API 명세를 확인한다.

### 1. 전환번호 인증을 위해 인증 코드 요청하기
```shell
curl -X "POST" "http://localhost:10001/request/code" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "phone": "019-010-1113"
}'
```

### 2. 전환번호 인증을 위해 인증 코드 확인하기
```shell
curl -X "POST" "http://localhost:10001/verify/code" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "phone": "019-010-1113",
  "code": "[인증코드]"
}'
```

### 3. 회원 가입하기
```shell
curl -X "POST" "http://localhost:10001/sign-up" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "email": "[이메일]",
  "phone": "019-010-1113",
  "nickname": "",
  "name": "name",
  "encryptedPassword": "[비밀번호]"
}'

```

### 4. 로그인 하기
```shell
curl -v -X "POST" "http://localhost:10001/sign-in" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "email": "[이메일]",
  "password": "[비밀번호]"
}'
```
요청 후에 쿠키 값 중 `JSESSIONID=[세션 값]` 값을 이용하여 api를 요청한다.

### 5. 내 정보 보기
```shell
curl "http://localhost:10001/api/users" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -H 'Cookie: JSESSIONID=[세션 값]' \
     -d $'{}'
```

---
### 1. 전환번호 인증을 위해 인증 코드 요청하기
```shell
curl -X "POST" "http://localhost:10001/request/code" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "phone": "019-010-1113"
}'
```

### 2. 전환번호 인증을 위해 인증 코드 확인하기
```shell
curl -X "POST" "http://localhost:10001/verify/code" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "phone": "019-010-1113",
  "code": "[인증코드]"
}'
```

### 3. 비밀번호 재설정하기
```shell
curl -X "POST" "http://localhost:10001/change-password" \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
  "phone": "019-010-1113",
  "encrypted_password": "[새로운 비밀번호]"
}'
```