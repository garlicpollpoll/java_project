# 스터디 관리 사이트

# Introduce

스터디를 설정한 상태에 따라 자동으로 관리해주는 프로젝트입니다. 과제를 제출하지 못한 사람은 강퇴를 당하고 모두 강퇴를 당한다면 스터디 방이 폐쇄됩니다. 

# Tools
* 언어 : Java
* 빌드 : Gradle
* 프레임워크 : spring boot, spring security, spring batch
* 데이터베이스 : MySQL
* ORM : Spring Data JPA, QueryDSL
* IDEA : IntelliJ, VSCode

# 주요 기능
## 참여한 스터디 관리
### Step 1
마감 기한이 넘어서도 제출하지 않았는가? -> 경고 1회 추가

### Step 2
스터디방에 설정된 경고 횟수에 도달했는가? -> 강퇴

### Step 3
해당 스터디 방에 모든 인원이 강퇴되었는가? -> 스터디 방 폐쇄

## 알람 기능
만약 경고 1회가 추가되었다면 경고 1회가 추가되었다는 알람이 발송됩니다. 만약 스터디가 정한 최대 경고에 도달하면 강퇴 되었다는 메시지가 전송됩니다. 

메시지는 1주일동안 지속되고 사라집니다. 

## 스터디 게시판
스터디 게시판을 만들고 그 안에서 커뮤니티를 이룰 수 있습니다. 

혹시 궁금한 것이 생기거나 모르는 것이 생기면 해당 게시판을 이용해서 스터디원들과 소통을 진행할 수 있습니다.

## 회원 강퇴 기능
물을 흐리는 사람이 있는 경우 스터디장에 한해 스터디원을 강퇴할 수 있는 기능이 부여됩니다. 

한번 강퇴된 스터디원은 다시 들어오고싶어도 못들어옵니다. 
