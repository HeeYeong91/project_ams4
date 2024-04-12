# :atm: 계좌 관리 서비스 AMS_4
<br />

## :page_facing_up: 목차
1. 프로젝트 소개
2. 프로젝트 기능
   * [1. 계좌생성](#1-계좌생성)
     * 입출금 통장
     * 마이너스 통장
   * [2. 계좌목록 조회](#2-계좌목록-조회)
   * [3. 계좌번호로 계좌 조회](#3-계좌번호로-계좌-조회)
   * [4. 예금주명으로 계좌 조회](#4-예금주명으로-계좌-조회)
   * [5. 계좌번호로 계좌 삭제](#5-계좌번호로-계좌-삭제)
<br />

## :eyes: 1. 프로젝트 소개
DB(리스트)를 이용한 은행 계좌 목록 저장 및 관리 가능한 간단한 애플리케이션 (화면 : AWT) <br />
AWT는 유니코드를 지원하지 않기 때문에 한글이 깨져서 화면에 출력된다. <br />
이를 해결하기 위해서는 이클립스에서 Run 메뉴에서 Run Configuration 메뉴를 선택해서 <br />
Arguments메뉴에 VM arguments 칸에 -Dfile.encoding=MS949 를 작성하고 Run 버튼을 눌러서 실행하면 한글이 정상적으로 출력된다.
<br /><br />
기존 AMS_1, AMS_2는 메모리에 계좌 정보를 저장했기 때문에 계좌정보가 프로그램을 종료하면 휘발된다는 단점이 있었다. <br />
이러한 문제를 해결하기 위해 AMS_4에서는 DB에 계좌정보를 저장했다.  <br />
화면구현 및 기능은 AMS_2와 일치하며 계좌정보를 저장하고 불러오는 방식에서만 차이가 있다.
DB에 계좌 정보를 저장하기 위해 OracleSQL 언어 사용했으며 Tool은 SQLDeveloper 이용했다.
<br /><br />
사용방법 :  <br />
SQL패키지에 테이블 생성 및 제약조건, 시퀀스, 더미데이터 입력용 sql파일을 넣었습니다. <br />
테스트를 진행하려면 SQLDeveloper에서 테이블 및 제약조건, 시퀀스 생성 후  <br />
JdbcAccountRepository 클래스에서 userid = "SQLDeveloper 사용자이름", password="SQLDeveloper 비밀번호" 입력 후 실행 <br />

<br /><br />

:calendar: 프로젝트 기간 : 2023년 6월 13일 <br />
:hammer: Tools : <img src="https://img.shields.io/badge/Eclipse-FE7A16.svg?style=for-the-badge&logo=Eclipse&logoColor=white" /> ![Oracle](https://img.shields.io/badge/SQLDeveloper-F80000?style=for-the-badge&logoColor=white) <br />
:books: languages : ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Oracle](https://img.shields.io/badge/Oracle%20SQL-F80000?style=for-the-badge&logo=oracle&logoColor=white) <br />
<br />

## :pushpin: 2. 프로젝트 기능
## 1. 계좌생성
* 입출금통장인지 마이너스통장인지 선택 후 계좌 생성을 진행한다.
* 입출금통장 선택 시 예금주명, 비밀번호, 입금금액을 사용자로부터 입력 받은 후 신규등록을 진행한다.
* 마이너스통장 선택 시 예금주명, 비밀번호, 대출금액을 사용자로부터 입력 받은 후 신규등록을 진행한다. <br />

![1 계좌생성](https://github.com/HeeYeong91/project_ams2/assets/139057065/d616640c-e751-42c3-a6f2-77dc204d8c98) <br />
[목차](#page_facing_up-목차)

## 2. 계좌목록 조회
* 계좌목록 조회 시 계좌번호 오름차순으로 입출금통장, 마이너스 통장 모두 화면에 출력한다.
* 계좌종류 / 계좌번호 / 예금주명 / 비밀번호 / 잔액 / 대출금 으로 구분해서 화면에 출력한다.
* 비밀번호는 '*'로 보여준다.
* 마이너스 통장은 대출금 표시 후 잔액에는 마이너스 금액을 보여준다. <br />

![2 계좌목록조회](https://github.com/HeeYeong91/project_ams2/assets/139057065/60199a18-68a3-4c41-bbef-aee5788e7af7) <br />
[목차](#page_facing_up-목차)

## 3. 계좌번호로 계좌 조회
* 계좌번호를 사용자로부터 입력받고 조회 버튼으로 계좌를 조회한다.
* 계좌번호가 존재하면 '0번 계좌를 찾았습니다.' 팝업창이 나타난다.
* 계좌번호가 존재하지 않을 때는 '0번 계좌는 존재하지 않습니다.' 팝업창이 나타나고, 전체계좌목록을 보여준다. <br />

![3 계좌전호로계좌조회](https://github.com/HeeYeong91/project_ams2/assets/139057065/3e0605ea-a993-4544-91df-0b316b44332a) <br />
[목차](#page_facing_up-목차)

## 4. 예금주명으로 계좌 조회
* 예금주명을 사용자로부터 입력받고 검색 버튼으로 계좌를 조회한다.
* 해당 예금주명을 가진 계좌가 1개 이상이면 '000님 계좌를 n개 찾았습니다.' 팝업창과 함께 계좌를 화면에 보여준다.
* 해당 예금주명을 가진 계좌가 없을 때는 '000님 계좌를 0개 찾았습니다.' 팝업창이 나타난다. <br />

![4 예금주명으로계좌조회](https://github.com/HeeYeong91/project_ams2/assets/139057065/db22bffa-adaa-48f7-b7a2-985d0600063c) <br />
[목차](#page_facing_up-목차)

## 5. 계좌번호로 계좌 삭제
* 계좌번호를 사용자로부터 입력받고 삭제 버튼으로 계좌를 삭제한다.
* 해당 계좌번호인 계좌가 존재하면 '0번 계좌가 삭제 되었습니다.' 팝업창이 나타나고 계좌가 삭제된 뒤에 전체계좌목록을 보여준다.
* 해당 계좌번호인 계좌가 존재하지 않으면 '0번 계좌가 존재하지 않습니다.' 팝업창이 나타난다. <br />

![5 계좌번호로계좌삭제](https://github.com/HeeYeong91/project_ams2/assets/139057065/9d8ffa05-d9c7-4311-a6cf-70bc9f32f7bd) <br />
[목차](#page_facing_up-목차)
