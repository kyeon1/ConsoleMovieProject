package model.vo;

/**
 * MemberVO 클래스
 * @author indi
 *
 */
public class MemberVO {
	private int userNum;//사용자 PK
	private String userId;//PK: 사용자 아이디
	private String userPw;//사용자 비밀번
	private String userName;//사용자 이름
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPw() {
		return userPw;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "MemberVO [userNum=" + userNum + ", userId=" + userId + ", userPw=" + userPw + ", userName=" + userName
				+ "]";
	}
	
}
