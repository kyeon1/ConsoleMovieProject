package service;

import java.util.List;
import java.util.Properties;

import model.dao.MemberDAO;
import model.vo.MemberVO;
import util.FileLoaderUtil;
import view.View;

/**
 * MemberService 클래스
 * @author indi
 *
 */
public class MemberService{

	private View view=new View();
	private MemberDAO memberDao=new MemberDAO();
	private static Properties msgProp=FileLoaderUtil.getInstance().getMsgProperties();
	
	/**
	 * 생성자
	 */
	public MemberService() {
		
	}

	/**
	 * 로그인
	 */
	public void login() {
		MemberVO memberVo=new MemberVO();
		
		String id=view.getPromptStr(msgProp.getProperty("id"));
		memberVo.setUserId(id);
		
		String pw=view.getPromptStr(msgProp.getProperty("pw"));
		memberVo.setUserPw(pw);
		
		List<MemberVO> login=memberDao.select(memberVo);
		
		if(!login.isEmpty()) {
			MemberVO loginMember=login.get(0);
			if(loginMember.getUserId().equals("admin")) {
				view.getPropmptMsg(msgProp.getProperty("adminTrue"));
				view.getPromptAction(msgProp.getProperty("adminMenu"));
			}
			view.getPromptAction(msgProp.getProperty("userMenu"));
		}else {
			view.getPropmptMsg(msgProp.getProperty("loginFalse"));
			view.getPromptAction(msgProp.getProperty("mainMenu"));
		}
	}

	/**
	 * 회원가입
	 */
	public void signMember() {
		MemberVO memberVo=new MemberVO();
		// 사용자 입력 검사
		memberVo.setUserId(view.getPromptStr(msgProp.getProperty("id")));
		List<MemberVO> idCheck=memberDao.selectId(memberVo);
		// 중복 ID 검사
		if (!idCheck.isEmpty()) {
			view.getPropmptMsg(msgProp.getProperty("userIdCheckFalse"));
			view.getPromptAction(msgProp.getProperty("mainMenu"));
		}else {
			view.getPropmptMsg(msgProp.getProperty("userIdCheckTrue"));
			String id=view.getPromptStr(msgProp.getProperty("id"));
			memberVo.setUserId(id);
			String pw=view.getPromptStr(msgProp.getProperty("pw"));
			memberVo.setUserPw(pw);
			String name=view.getPromptStr(msgProp.getProperty("name"));
			memberVo.setUserName(name);
			memberDao.insert(memberVo);
			view.getPromptAction(msgProp.getProperty("mainMenu"));
		}
	}

	/**
	 * 로그아웃
	 */
	public void logout() {
		view.getPropmptMsg(msgProp.getProperty("logout"));
		view.getPromptAction(msgProp.getProperty("mainMenu"));
	}
}
