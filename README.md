# YLBA

스프링부트를 이용해 구성하였습니다.
- [x] 회원가입: 구현 완료
- [x] 로그인: 구현 완료
- [x] 내 정보 보기: 구현 완료
- [x] 비밀번호 재설정: 구현 완료

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