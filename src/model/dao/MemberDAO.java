package model.dao;

import java.util.List;

import model.vo.MemberVO;

/**
 * MemberDAO 클래스
 * @author indi
 *
 */
public class MemberDAO extends CommonDAO{
	
	/**
	 * MemberDAO 생성자
	 */
	public MemberDAO() {
		
	}
	
	/**
	 * 회원가입
	 * @param memberVo
	 * @return boolean
	 */
	public boolean insert(MemberVO memberVo) {
		return executeUpdate("insertMember", memberVo.getUserId(), memberVo.getUserPw(), memberVo.getUserName());
	}
	
	/**
	 * 로그인
	 * @param memberVo
	 * @return List<MemberVO>
	 */
	public List<MemberVO> select(MemberVO memberVo){
		return executeQuery("selectMember", MemberVO.class, memberVo.getUserId(), memberVo.getUserPw());
	}
	
	/**
	 * 아이디 중복 값 체크
	 * @param memberVo
	 * @return List<MemberVO>
	 */
	public List<MemberVO> selectId(MemberVO memberVo){
		return executeQuery("selectIdMember", MemberVO.class, memberVo.getUserId());
	}
}
