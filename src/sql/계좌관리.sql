-- #1. 계좌 테이블 생성
CREATE TABLE account (
    account_num    VARCHAR2(4),
    name                 VARCHAR2(10) NOT NULL,
    password          VARCHAR2(10) NOT NULL,
    rest_money       NUMBER(20),
    borrow_money NUMBER(20),
    type_id             NUMBER(1) NOT NULL
);

-- #2. 제약조건 추가
ALTER TABLE account 
    ADD (
    CONSTRAINT account_num_pk PRIMARY KEY ( account_num ),
    CONSTRAINT type_id_ck CHECK ( type_id IN ( '0', '1' ) )
);

-- 시퀀스
CREATE SEQUENCE account_num_seq 
    START WITH 1000 INCREMENT BY 1;
    
-- #3. 개발의 편의성을 위해 제약조건 활성화/비활성화
-- 비활성화
ALTER TABLE account 
    DISABLE CONSTRAINT account_num_pk

-- 활성화
ALTER TABLE account 
    ENABLE CONSTRAINT account_num_pk;

-- #4. 더미데이터 추가, 수정, 삭제
-- 전체 계좌 목록 수
SELECT
    COUNT(*) cnt
FROM
    account;

-- 전체 계좌 목록 조회
SELECT
    account_num,
    name,
    password,
    rest_money,
    borrow_money,
    type_id
FROM
    account
ORDER BY
    account_num;

-- 신규계좌 등록
INSERT INTO account (
    account_num,
    name,
    password,
    rest_money,
    borrow_money,
    type_id) 
VALUES ( account_num_seq.NEXTVAL, '김대출', 1234, 10000, 100000000, 1);

commit;

select * from account;

-- 계좌번호로 계좌 검색
SELECT
    account_num,
    name,
    password,
    rest_money,
    borrow_money,
    type_id
FROM
    account
WHERE
    account_num = ?;

-- 이름으로 계좌 검색
SELECT
    account_num,
    name,
    password,
    rest_money,
    borrow_money,
    type_id
FROM
    account
WHERE
    name = ?;

-- 계좌번호로 계좌 삭제
DELETE FROM account
WHERE
    account_num = ?;