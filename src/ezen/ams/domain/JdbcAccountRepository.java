package ezen.ams.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * RDB를 통해 은행계좌 목록 저장 및 관리(검색, 수정, 삭제) 구현체 (2023-06-13)
 * 
 * @author 이희영
 */
public class JdbcAccountRepository implements AccountRepository {

	private static String diver = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private static String userid = "hr";
	private static String password = "hr";

	private Connection con;

	public JdbcAccountRepository() throws Exception {
		Class.forName(diver);
		con = DriverManager.getConnection(url, userid, password);
	}

	/**
	 * 전체 계좌 목록 수 반환
	 * 
	 * @return 목록수
	 */
	public int getCount() {
		int count = 0;
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT")
		  .append("    COUNT(*) cnt")
		  .append(" FROM")
		  .append("    account");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			
			if (rs.next()) { // 행이 1개이기 때문에 loop 돌 필요가 없음
				count = rs.getInt("cnt");
			}
		} catch (Exception e) {
			// 컴파일 예외를 런타임 예외로 변환
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				
				if (pstmt != null)
					pstmt.close();
				
				if (con != null)
					con.close();
			} catch (Exception e) {}
		}
		return count;
	}

	/**
	 * 전체 계좌 목록 조회
	 * 
	 * @return 전체계좌 목록
	 */
	public List<Account> getAccounts() {
		List<Account> list = null;
		Account account = null;
		StringBuilder sb = new StringBuilder();
		// type_id -> Foreign key로 나중에 계좌 종류별 테이블 구분 작업 필요
		sb.append(" SELECT")
		  .append("    account_num,")
		  .append("    name,")
		  .append("    password,")
		  .append("    rest_money,")
		  .append("    borrow_money,")
		  .append("    type_id")
		  .append(" FROM")
		  .append("    account")
		  .append(" ORDER BY")
		  .append("    account_num");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			list = new ArrayList<Account>();
			
			while (rs.next()) {
				String accountNum = rs.getString("account_num");
				String accountOwner = rs.getString("name");
				int password = rs.getInt("password");
				long restMoney = rs.getLong("rest_money");
				long borrowMoney = rs.getLong("borrow_money");
				int accountType = rs.getInt("type_id");

				if (accountType == 0) {
					account = new Account();
				} else if (accountType == 1) {
					account = new MinusAccount();
				}
				
				account.setAccountNum(accountNum);
				account.setAccountOwner(accountOwner);
				account.setPasswd(password);
				
				if (accountType == 1) {
					account.setRestMoney(restMoney + borrowMoney);
					((MinusAccount) account).setBorrowMoney(borrowMoney);
				} else {
					account.setRestMoney(restMoney);
				}
				
				list.add(account);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				
				if (pstmt != null)
					pstmt.close();
				
			} catch (Exception e) {}
		}
		return list;
	}

	/**
	 * 신규계좌 등록
	 * 
	 * @param account 신규계좌
	 * @return 등록 여부
	 */
	public boolean addAccount(Account account) {
		StringBuilder sb = new StringBuilder();
		sb.append(" INSERT INTO account (")
		  .append("     account_num,")
		  .append("     name,")
		  .append("     password,")
		  .append("     rest_money,")
		  .append("     borrow_money,")
		  .append("    type_id) ")
		  .append(" VALUES (account_num_seq.NEXTVAL, ?, ?, ?, ?, ?)");

		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, account.getAccountOwner());
			pstmt.setInt(2, account.getPasswd());
			pstmt.setLong(3, account.getRestMoney());
			
			if (account instanceof MinusAccount) {
				pstmt.setLong(4, ((MinusAccount) account).getBorrowMoney());
				pstmt.setInt(5, 1);
			} else {
				pstmt.setLong(4, 0);
				pstmt.setInt(5, 0);
			}
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {}
		}
		return true;
	}

	/**
	 * 계좌번호로 계좌 검색 기능
	 * 
	 * @param accountNum 검색 계좌번호
	 * @return 검색된 계좌
	 */
	public Account searchAccount(String accountNum) {
		Account account = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT")
		  .append("     account_num,")
		  .append("     name,")
		  .append("     password,")
		  .append("     rest_money,")
		  .append("     borrow_money,")
		  .append("     type_id")
		  .append(" FROM")
		  .append("     account")
		  .append(" WHERE")
		  .append("     account_num = ?");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, accountNum);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String accNum = rs.getString("account_num");
				String accountOwner = rs.getString("name");
				int password = rs.getInt("password");
				long restMoney = rs.getLong("rest_money");
				long borrowMoney = rs.getLong("borrow_money");
				int accountType = rs.getInt("type_id");

				if (accountType == 0) {
					account = new Account();
				} else if (accountType == 1) {
					account = new MinusAccount();
				}
				
				account.setAccountNum(accNum);
				account.setAccountOwner(accountOwner);
				account.setPasswd(password);
				account.setRestMoney(restMoney);
				
				if (accountType == 1) {
					((MinusAccount) account).setBorrowMoney(borrowMoney);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				
				if (pstmt != null)
					pstmt.close();
				
			} catch (Exception e) {}
		}
		return account;
	}

	/**
	 * 예금주명으로 계좌 검색 기능
	 * 
	 * @param accountOwner 검색 예금주명
	 * @return 검색된 계좌목록
	 */
	public List<Account> searchAccountByOwner(String accountOwner) {
		List<Account> list = null;
		Account account = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT")
		  .append("     account_num,")
		  .append("     name,")
		  .append("     password,")
		  .append("     rest_money,")
		  .append("     borrow_money,")
		  .append("     type_id")
		  .append(" FROM")
		  .append("     account")
		  .append(" WHERE")
		  .append("     name = ?");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, accountOwner);
			rs = pstmt.executeQuery();
			list = new ArrayList<Account>();
			
			while (rs.next()) {
				String accountNum = rs.getString("account_num");
				String accOwner = rs.getString("name");
				int password = rs.getInt("password");
				long restMoney = rs.getLong("rest_money");
				long borrowMoney = rs.getLong("borrow_money");
				int accountType = rs.getInt("type_id");

				if (accountType == 0) {
					account = new Account();
				} else if (accountType == 1) {
					account = new MinusAccount();
				}
				
				account.setAccountNum(accountNum);
				account.setAccountOwner(accOwner);
				account.setPasswd(password);
				account.setRestMoney(restMoney);
				
				if (accountType == 1) {
					((MinusAccount) account).setBorrowMoney(borrowMoney);
				}
				list.add(account);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {}
		}
		return list;
	}

	/**
	 * 계좌번호로 계좌 삭제 기능
	 * 
	 * @param accountOwner 검색 계좌번호
	 * @return 계좌 삭제 여부
	 */
	public boolean removeAccount(String accountNum) {
		StringBuilder sb = new StringBuilder();
		sb.append(" DELETE FROM account").append(" WHERE").append("     account_num = ?");
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, accountNum);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
		}
		return true;
	}

	public void close() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

//	// 테스트용 main
//	public static void main(String[] args) throws Exception {
//
//		AccountRepository accountRepository = new JdbcAccountRepository();
//		List<Account> list = null;
//		Account account = null;
//
//		// 계좌 등록 테스트
//		System.out.println("########## 계좌 등록 테스트  ##########");
//		account = new Account();
//		account.setAccountOwner("이희영");
//		account.setPasswd(1111);
//		account.setRestMoney(10000);
//		accountRepository.addAccount(account);
//
//		account = new MinusAccount();
//		account.setAccountOwner("김희영");
//		account.setPasswd(1111);
//		account.setRestMoney(0);
//		((MinusAccount) account).setBorrowMoney(1000000);
//		accountRepository.addAccount(account);
//
//		account = new Account();
//		account.setAccountOwner("이희영");
//		account.setPasswd(1111);
//		account.setRestMoney(5000000);
//		accountRepository.addAccount(account);
//
//		System.out.println("계좌 등록 완료");
//
//		// 전체 계좌 조회 테스트
//		list = accountRepository.getAccounts();
//		System.out.println("########## 전체 계좌 조회 테스트 ##########");
//		for (Account account2 : list) {
//			System.out.println(account2);
//		}
//
////		// 계좌번호 검색 테스트
////		account = accountRepository.searchAccount("1000");
////		System.out.println("########## 계좌번호 검색 테스트 ##########");
////		System.out.println(account);
////			
////		// 예금주명 검색 테스트
////		List<Account> searchList = accountRepository.searchAccountByOwner("이희영");
////		System.out.println("########## 예금주명 검색 테스트 ##########");
////		for (Account searchAccount : searchList) {
////			System.out.println(searchAccount);
////		}
////			
////		// 계좌번호 삭제 테스트
////		accountRepository.removeAccount("1002");
////		System.out.println("########## 계좌번호 삭제 테스트 ##########");
////		list = accountRepository.getAccounts();
////		for (Account account2 : list) {
////			System.out.println(account2);
////		}
//	}
}