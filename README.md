# SpringBoot-Project-BOARD
Basic Board Project

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

#### 게시글 전체 목록
<img width="800" src="https://user-images.githubusercontent.com/85252989/217251082-d7a02735-53b2-4109-9ee2-70c4a06d5121.PNG">

게시판 전체 목록을 페이징처리하여 조회가능하다.

#### 게시글 등록
<img width="800" src="https://user-images.githubusercontent.com/85252989/217258038-65a401b3-7137-4093-b7ff-c4d98b0ceb52.PNG">

로그인한 사용자만 게시글을 등록할 수 있으며 저장시 alert문이 뜨고 확인을 누르면 게시글이 등록된다.

#### 게시글 상세보기
<img width="800" src="https://user-images.githubusercontent.com/85252989/217254011-161b6523-d944-4846-a203-213058094e50.PNG">

작성자와 로그인한 유저가 같으면 수정과 삭제 버튼이 뜬다

<img width="800" src="https://user-images.githubusercontent.com/85252989/217254037-71f25082-1a64-4b73-9cce-f90806c0cd07.PNG">

비로그인 or 다른 유저라면 목록버튼만 뜨게 된다.

#### 게시글 수정
<img width="800" src="https://user-images.githubusercontent.com/85252989/217254695-e9558599-b436-4726-8d70-922a3b4d6ccc.PNG">

게시글 수정시 기존에 넣었던데로 입력이 되어있고 텍스트, 이미지, 첨부파일을 수정하면 된다. 특히 이미지와 첨부파일은 옆에 REMOVE라는 버튼이 존재하는데 누를시에 기존에 들어가있던 데이터가 사라지고 새롭게 이미지와 첨부파일을 등록할 수 있다.

<img width="800" src="https://user-images.githubusercontent.com/85252989/217254721-46b3c0ff-1812-462b-b9b3-9d9522d2ef4e.PNG">

수정시 수정하시겠습니까라는 확인창이 뜨게 되고 확인을 누를시 수정된다.

#### 게시글 삭제
<img width="800" src="https://user-images.githubusercontent.com/85252989/217254747-eb3a3a1d-0401-4e42-9acb-f5f9c35f9ef1.PNG">

작성자와 로그인한 유저가 같다면 수정,삭제버튼이 뜨는데 삭제버튼을 누르게되면 삭제하시겠습니까라는 확인창이 뜨게 되고 확인을 누를시 삭제된다.

#### 게시글 검색
<img width="800" src="https://user-images.githubusercontent.com/85252989/217254784-38472554-2495-41ea-a060-bd7dab5eba6e.PNG">

검색키워드에 포함된 모든 글들을 보여줍니다.

#### 게시글 추천
<img width="800" src="https://user-images.githubusercontent.com/85252989/217254805-975be855-1182-40cb-ac4d-410c2b676e64.PNG">
<img width="800" src="https://user-images.githubusercontent.com/85252989/217254824-d3859c26-e09e-46fd-af64-5d1bc8fc5a23.PNG">

본인이 올린 글을 제외한 게시글에만 좋아요를 할 수 있고 본인이 올린글에는 추천을 할수없습니다. 상세화면에서 빈하트가 노말 상태이고 검쟁색하트가 좋아요인 상태입니다. 빈하트를 클릭시에 확인창이 뜨고 확인을 누를시에 좋아요 표시가 되면서 하트옆에 숫자가 +1이 됩니다.

<img width="800" src="https://user-images.githubusercontent.com/85252989/217405395-1bb1a76b-03a5-4651-a308-705eb6ac699c.PNG">

검정색하트를 다시 누르면 취소 확인창이 뜨고 확인을 누를시에 다시 빈하트가 되고 숫자는 -1이 됩니다.

</details>

<details markdown="1">
<summary>회원</summary>

#### 회원가입
<img width="800" src="https://user-images.githubusercontent.com/85252989/217258912-61d4dedc-c53d-4632-9123-0e5a3f2be3a3.PNG">

회원가입시 가입완료버튼은 비활성화되어있고 중복체크가 되어야지만 가입완료버튼이 활성화가 된다.

#### 중복확인 및 유효성검사
<img width="450" src="https://user-images.githubusercontent.com/85252989/217258925-c79707b4-acfc-43bf-b440-2a0ea7add4ba.PNG">
<img width="450" src="https://user-images.githubusercontent.com/85252989/217405519-cf838b16-c94e-4972-a4b3-4a4678b9bef2.PNG">

아이디 중복체크시 이미 사용중인 아이디라면 이미 사용중인 아이디라는 문구가 뜨게 된다. 이름, 비밀번호, 이메일을 입력하지 않을 시 해당 데이터를 입력해주세요라는 문구가 뜨게 된다. 아이디 중복체크시 중복되지 않았다면 사용 가능한 아이디라는 문구가 뜨고 가입완료버튼이 활성화가 된다. 

#### 로그인
<img width="800" src="https://user-images.githubusercontent.com/85252989/217258935-bbe51e17-2dad-4c69-be8f-5c0097e6e33f.PNG">

로그인창에 해당하고 아이디, 비밀번호를 입력하면 된다.

#### 로그인실패
<img width="800" src="https://user-images.githubusercontent.com/85252989/217258950-3ae5b1f8-481f-4405-a5ed-66b6719e58e6.PNG">

아이디가 틀렸거나 비밀번호가 틀렸을시 사용자ID 또는 비밀번호를 확인해주세요라는 문구가 뜨게 된다.

#### 구글로그인
<img width="800" src="https://user-images.githubusercontent.com/85252989/217407448-8f73958c-315b-42ef-bc3f-ab32d87dc74a.png">

#### 네이버로그인
<img width="800" src="https://user-images.githubusercontent.com/85252989/217258976-8990c60b-2756-4eed-9154-7e89d852116a.PNG">

구글로그인과 네이버로그인이 가능하다.

</details>

<details markdown="1">
<summary>댓글</summary>

#### 미로그인 사용자 댓글작성
<img width="800" src="https://user-images.githubusercontent.com/85252989/217266665-0dacd38c-5d8b-4557-945e-2bc237a7aaf3.PNG">

미로그인시 로그인을 해야 댓글을 등록할 수 있다는 문구가 뜨게 됩니다.

#### 로그인 사용자 댓글작성
<img width="800" src="https://user-images.githubusercontent.com/85252989/217266701-fc5d38e2-ec39-4bb4-aa96-83b0acde91e6.PNG">

로그인이 되었을때는 댓글작성을 할 수 있는 텍스트박스와 버튼이 뜨게 됩니다.

#### 댓글목록
<img width="800" src="https://user-images.githubusercontent.com/85252989/217266729-e7eebf3e-1168-4542-b61d-d80a40cefb4b.PNG">

게시글과 마찬가지로 페이징 처리되어 댓글들을 볼 수 있습니다.

#### 댓글수정
<img width="800" src="https://user-images.githubusercontent.com/85252989/217260716-c9a0a599-be71-47fa-b5b1-e80c36eb0be4.PNG">

댓글작성자와 유저가 같아야하고 댓글수정시 수정하시겠습니까?라는 확인창이 뜨고 확인시 수정이 됩니다.

#### 댓글삭제
<img width="800" src="https://user-images.githubusercontent.com/85252989/217260738-006d4587-c26a-4c08-8b0d-b3115bfc822d.PNG">

댓글작성자와 유저가 같아야하고 댓글삭제시 삭제하시겠습니까?라는 확인창이 뜨고 확인시 삭제가 됩니다.

#### 댓글에 답글작성 
<img width="800" src="https://user-images.githubusercontent.com/85252989/217260756-96fc17dd-1cb0-4980-9fc0-62bf05eb1881.PNG">

달린댓글에 답글이라는 버튼을 누를시 텍스트박스가 열리게 되고 답글을 작성후 완료버튼을 누르면 답글이 등록이 됩니다.

#### 답글작성후
<img width="800" src="https://user-images.githubusercontent.com/85252989/217260774-cdfb3252-d6c9-42b9-b509-1b3ff10f419c.PNG">

답글 작성시 텍스트 옆에 화살표로 달리게 되고

#### 대대댓글작성시
<img width="800" src="https://user-images.githubusercontent.com/85252989/217260791-dc09ceb8-4ebc-4e21-90e9-e41a14cb3b5e.PNG">

그 답글에 다시 답글을 작성시 화살표가 또 증가하며 화살표 갯수로 대댓글을 표시하였습니다.

#### 대댓글에서 새로운 댓글 등록시
<img width="800" src="https://user-images.githubusercontent.com/85252989/217260802-7236ed97-6c6d-4647-b007-2b93f821d12d.PNG">

위 상태에서 일반 댓글등록시 윗칸에 새롭게 할당이 되고 그 댓글에 답글이 달릴시 바로 밑에 대댓글이 달리게 된다.

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
