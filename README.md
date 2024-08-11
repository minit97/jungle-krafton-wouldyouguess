<div align="center">
  <h4>gif, image 때문에 렌더링 속도가 조금 느립니다..조금만 기다려주세요!!</h4>
  <h2>동료들과 함께 즐길 수 있는 그림&이미지 기반 게임 웹 서비스</h2>
</div>  
</br></br>

# 📜 목차
- [☀️ 서비스 개요](#-서비스-개요)
- [🗓️ 개발 기간](#-개발-기간)
- [⭐ 주요 기능](#-주요-기능)
- [⚙️ 아키텍처](#%EF%B8%8F-아키텍처)
- [🖼️ 포스터](#-포스터)
- [👥 팀원](#-팀원)
- [📹 시연 영상](#-시연-영상)


# ☀️ 서비스 개요
- Game1 : 다른 키워드를 받은 스파이의 그림을 찾아라!
- Game2 : 각자 자신이 올린 사진을 AI를 활용해 틀린그림찾기를 해보자!

</br></br>

# 🗓️ 개발 기간
2024년 6월 27일 ~ 2024년 7월 27일 (약 4주)

</br></br>

# ⭐ 주요 기능
## 메인 화면
### 행성 클릭으로 게임에 진입할 수 있습니다 / URL를 친구들에게 전송하여 함께 게임을 즐길 수 있습니다.
<img width="600" alt="main-page" src="https://github.com/user-attachments/assets/42b11f4f-f3d6-4d60-9673-76b0978b852d">

## Game1
### 1-1. 키워드 확인 후 그림그리기
- WebRTC를 활용한 영상&음성
- WebSocket을 활용한 캔버스 공유
<img width="600" alt="draw1" src="https://github.com/user-attachments/assets/4d3b7cde-d361-4ebc-8674-dc227d3ba37a">
<img width="600" alt="draw2" src="https://github.com/user-attachments/assets/69fc5739-4da2-4697-b522-52b1254ef4ae">

### 1-2. 참여자의 음성말고도 관전자들끼의 캔버스 소통 요소 추가
<img width="600" alt="laser" src="https://github.com/user-attachments/assets/a5be748b-1588-4023-803f-ed7bef942b29">

### 2. 투표하기
<img width="600" alt="vote" src="https://github.com/user-attachments/assets/2a05aac6-b85e-4645-a286-d4a1220e3bd2">


### 3. 결과창
<img width="600" alt="result" src="https://github.com/user-attachments/assets/8662a071-fdbc-4c3b-b0ea-1b5735050fb1">

</br></br>

## Game2
### 1. AI 이미지 생성을 활용한 새로운 이미지 생성
<img width="600" alt="ai-image" src="https://github.com/user-attachments/assets/4e585c8b-40f3-405a-b24d-d3681f35da65">


### 2. 틀린그림찾기 진행
<img width="600" alt="find-difference" src="https://github.com/user-attachments/assets/3c7f0a3a-1290-4e03-9b5a-c31e127b2376">


### 3. 맞춘 여부 & 남은 기회로 점수를 부여 후 랭킹 출력
<img width="600" alt="result" src="https://github.com/user-attachments/assets/d7c98698-bc70-403f-814a-291f343eb155">


</br></br>


# ⚙️ 아키텍처
### Application
<img width="400" alt="application-image" src="https://github.com/user-attachments/assets/d7ef2c79-f28e-4d8d-89aa-f799f3a575e6">

### CI/CD
<img width="400" alt="CICD-image" src="https://github.com/user-attachments/assets/fda5742f-e279-4b11-b6f5-3910c3794703">

### Architecture
<img width="800" alt="total-image" src="https://github.com/user-attachments/assets/f426e8be-64f8-4399-b079-7782d8cb50ef">

</br></br>

# 🖼️ 포스터
<img width="800" alt="Screenshot 2024-05-24 at 4 28 31 PM" src="https://github.com/user-attachments/assets/518d7f8a-0611-462a-9771-92c04a723f64">

</br></br>

# 👥 팀원
- [김광윤(팀장)](https://github.com/leorivk): 로그인, 이미지 생성 AI, AI 서버 관리
- [김채윤](https://github.com/Chaeyun06): UX/UI, Canvas, Animation CSS, zustand
- [박현민](https://github.com/minit97): 게임 관련 API 설계, Socket 관련 Room 설계, socket.io & nestjs, CI/CD, S3, CloudFront
- [차성욱](https://github.com/tjddnr9553): UX/UI, OpenVidu, Canvas, zustand, 

</br></br>

# 📹 영상
- 시연 [유튜브 링크](https://youtu.be/2We4Kfgisso)
- 발표 [유튜브 링크](https://youtu.be/M3kvW-cpoxg)

</br></br></br></br>

# 🗓️ 리펙토링 기간
2024년 7월 29일 ~ 2024년 8월 1일  

# ERD
- https://www.erdcloud.com/p/49gkRcrtAwkbnxAfh
<img width="770" alt="image" src="https://github.com/user-attachments/assets/0c3b17a0-a6e4-42ee-bf61-f4ae185cc874">



