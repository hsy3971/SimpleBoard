# SpringBoot-Project-BOARD
게시판 프로젝트입니다.

# 목차

## 들어가며

### 1. 프로젝트 소개

프로젝트를 시작하게 된 계기는 SpringBoot와 JPA를 독학으로 학습하면서 직접 만들며 배우고자 시작하게 되었습니다.


### 2. 프로젝트 기능

프로젝트의 주요기능은 아래와 같습니다.

● 게시판 - CRUD기능(이미지, 첨부파일 등), 조회수(중복카운트 방지), 좋아요기능, 페이징, 검색

● 사용자 - Spring Security 회원가입 및 로그인, OAuth 2.0 구글 로그인 연동, 네이버 로그인 연동, 회원가입시 유효성 검사 및 중복 검사

● 댓글 - CRUD기능, 대댓글기능, 페이징


### 3. 사용 기술


#### 3-1 백엔드


##### 주요 프레임워크 / 라이브러리


● Java 11

● SpringBoot 2.7.3

● JPA(Spring Data JPA)

● Spring Security

● OAuth 2.0


##### Build Tool


● Gradle 7.2

##### DataBase

● MySQL 8.0

● H2 DataBase


#### 3-2 프론트엔드


● Html/Css

● JavaScript

● Thymeleaf

● Bootstrap 3.4.1

### 4. 실행 화면

<details markdown="1">
<summary>게시글</summary>

#### 메인 페이지(홈)
<img width="800" src="https://github.com/user-attachments/assets/791c45e5-f60f-4070-9314-cf61990976b2">

- 상단 바에는 홈과  로그인, 회원가입, 게시판으로 갈 수 있는 버튼이 있다.
- 시작하기 버튼을 누르면 회원가입 페이지로 이동하게 된다.
#### 게시판 페이지
<img width="800" src="https://github.com/user-attachments/assets/77316d2c-b3cf-410a-8fbd-85dae86d6b5b">

- 전체 게시판에 대해 조회할 수 있다.
- 제목으로 검색하여 특정 게시판을 검색 할 수 있다.
- 글 작성 버튼을 누르면 게시글을 등록 할 수 있다.
#### 게시글 등록
<img width="800" src="https://github.com/user-attachments/assets/13333d1d-70f1-4a33-a7db-26a43c95b1ce">

- 제목과 내용을 입력한다.
- 이미지 파일과 첨부 파일을 여러 개 등록할 수 있다.
- 이미지와 첨부 파일은 특정 폴더인 로컬에 저장된다.
#### 게시글 상세보기
<img width="800" src="https://github.com/user-attachments/assets/d2bb1e4f-de1b-4373-802b-23f1cc95f594">

- 해당 글의 상세 페이지로 들어가면 등록했던 이미지가 출력이 되고 첨부파일은 다운로드 받을 수 있다.
- 본인이 등록했던 게시글이면 수정과 삭제 버튼이 활성화된다.
- 왼쪽 상단에 빈 하트를 누르게 되면 좋아요의 횟수가 +1이 되며 꽉 채운 하트로 활성화 된다.
#### 게시글 수정
<img width="800" src="https://github.com/user-attachments/assets/7cdede04-9845-4475-a88f-a95b4ede1700">

- 기존에 등록했던 제목과 내용이 출력이 되고 이를 수정할 수 있다.
- 첨부했던 이미지와 파일은 박스 형태로 출력이 되는데 X버튼을 통해 기존에 있던 파일을 지우고 수정할 수 있다.
- 또 파일선택을 통해 새롭게 이미지와 파일을 등록할 수 있다.
</details>

<details markdown="1">
<summary>회원</summary>

#### 회원가입
<img width="400" src="https://github.com/user-attachments/assets/7cad07d5-f5a1-431a-9cbd-397d7a422f84">
<img width="400" src="https://github.com/user-attachments/assets/06f809be-5489-4ef7-818e-d82e92a564c9">

- 회원가입 시 아이디 중복체크를 한다.
- 아이디의 중복체크가 확인되지 않으면 가입버튼이 활성화되지 않는다.
- 아이디 중복체크가 완료되면 가입버튼이 활성화된다.
- 아이디 중복시 이미 사용중인 아이디, 사용 가능할 때엔 사용가능한 아이디라고 출력한다.
- 비밀번호, 이름, 이메일은 유효성 검사를 통해 입력을 확인한다.
#### 로그인
<img width="400" src="https://github.com/user-attachments/assets/441222b8-63d3-4b42-a290-961ecb5b0abd">
<img width="400" src="https://github.com/user-attachments/assets/a9129e2c-56e7-4d69-8d72-82208ff834e6">

- 일반 로그인과 구글,네이버를 연동 로그인 할 수 있다.
- 아이디와 비밀번호가 맞지 않을 경우 로그인 실패 문구가 뜬다.
#### 구글로그인
<img width="800" src="https://github.com/user-attachments/assets/5f5b3a5f-fc9a-47d5-942c-5068c3df1eae">

#### 네이버로그인
<img width="500" src="https://github.com/user-attachments/assets/46b4a126-c599-4205-b92e-7a33ca704eef">

- 구글과 네이버 계정을 통해 로그인 할 수 있다.
- 아이디가 해당 이메일로 미리 가입이 되어 있다면 바로 로그인 할 수 있다.
- 아이디가 해당 이메일로 가입이 안되어 있어도 해당 소셜(구글, 네이버)의 정보를 가지고 자동으로 가입된다.
</details>

<details markdown="1">
<summary>댓글</summary>

#### 비로그인 사용자 댓글작성
<img width="500" src="https://github.com/user-attachments/assets/8c651d0e-9855-403a-af6a-27dfdeff2629">

#### 로그인 사용자 댓글작성
<img width="500" src="https://github.com/user-attachments/assets/f388bdda-f617-49a1-bea9-7af1730a2de7">

#### 댓글목록
<img width="500" src="https://github.com/user-attachments/assets/1534a6f8-99f3-4c6e-a979-25e1613ad988">

- 로그인을 하지 않은 경우 댓글을 등록할 수 없기 때문에 로그인을 요청한다.
- 댓글 작성란에서 댓글을 등록하면 댓글 목록에 출력이 된다.
- 댓글을 작성한 사람이라면 본인의 댓글을 수정 또는 삭제할 수 있다.
#### 대댓글 작성 
<img width="500" src="https://github.com/user-attachments/assets/90a45280-250e-4feb-b58d-6449a4d638b6">

#### 댓글목록(대댓글 작성 후)
<img width="500" src="https://github.com/user-attachments/assets/fa1ff877-c701-470f-aabf-56f859be23fc">

- 댓글목록에 있는 댓글에 답글 버튼을 클릭하면 텍스트 박스가 나오고 대댓글을 작성할 수 있다.
- 대댓글은 무한계층형의 형태로 대댓글을 계속해서 달 수 있다. 대댓글의 깊이는 화살표의 개수로 구분한다.
- 대댓글이 달린 것을 수정 또는 삭제할 수 있다.
</details>

## 구조 및 설계

### 1. 패키지 구조

### 2. DB 설계
<img width="800" src="https://user-images.githubusercontent.com/85252989/217453913-56df4ceb-ed32-4786-846f-b38739a4c75c.PNG">
<img width="800" src="https://user-images.githubusercontent.com/85252989/217438795-677cd303-74a5-4ce9-bbf7-8f0977486332.PNG">
<img width="800" src="https://user-images.githubusercontent.com/85252989/217438815-f936a74c-c38d-47b1-9b66-b46c2f227eca.PNG">
<img width="800" src="https://user-images.githubusercontent.com/85252989/217438832-4401ee11-cc9e-43ae-aaf3-5fb43b991f7b.PNG">
<img width="800" src="https://user-images.githubusercontent.com/85252989/217438855-2a0122a6-e567-4163-8847-eb76fd6b6de4.PNG">
<img width="800" src="https://user-images.githubusercontent.com/85252989/217438875-c7ae5c22-f234-43e8-9143-b2d3c1dd2497.PNG">

### 3. API 설계
<img width="800" src="https://user-images.githubusercontent.com/85252989/218687820-0417d922-7a07-4fec-9dff-ea3fd6754348.PNG">
<img width="800" src="https://user-images.githubusercontent.com/85252989/218688863-40b44363-3e8e-4717-9e4f-18fd1625cb0d.PNG">
<img width="800" src="https://user-images.githubusercontent.com/85252989/218687955-6b72e8b5-9e33-4415-963c-27af36998a26.PNG">
<img width="800" src="https://user-images.githubusercontent.com/85252989/218687992-4a64a56e-6a17-4a64-96e2-bc3d04dfcbcc.PNG">

## 개발 내용

● [구글 로그인 구현](https://flaxen-cucumber-c3a.notion.site/4fd4fb06db154c79810cacab9a30b790)


● [네이버 로그인 구현](https://flaxen-cucumber-c3a.notion.site/40775649d8ec40d5949c156969882856)


● [회원가입과 로그인 기능 구현](https://flaxen-cucumber-c3a.notion.site/b439fd634516460487a0d341fc2d7abc)


● [댓글 페이징 구현](https://flaxen-cucumber-c3a.notion.site/e4841c2dab89421888482734393a9743)


● [게시판 페이징 처리와 검색처리 구현](https://flaxen-cucumber-c3a.notion.site/195a051fc9584f868f2a9ec5d33b3b5a)


● [게시글 등록 구현](https://flaxen-cucumber-c3a.notion.site/2cac292f44d246ee98305ed4ece8c13a)


● [게시글 수정과 삭제 구현](https://flaxen-cucumber-c3a.notion.site/2e409411341c49a4ab1e1ba6cee98f48)


● [댓글 수정과 삭제 구현](https://flaxen-cucumber-c3a.notion.site/09efea51199a4ffbb2317fb26dca7edd)


● [좋아요 기능 구현](https://flaxen-cucumber-c3a.notion.site/72edd38ba0d94675b6c765356a74206f)


● [댓글작성과 대댓글 기능 구현](https://flaxen-cucumber-c3a.notion.site/08a8273dc01848e4b6f2f5ff663b4ac5)


● [조회수 기능 구현(중복방지)](https://flaxen-cucumber-c3a.notion.site/79d06b80e4614f82aee7838ba861e049)


## 마치며

### 1. 프로젝트 보완사항
