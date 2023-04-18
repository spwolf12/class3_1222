package com.itwillbs.spring_mvc_board_pcw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.spring_mvc_board_pcw.mapper.MemberMapper;
import com.itwillbs.spring_mvc_board_pcw.vo.MemberVO;

@Service
public class MemberService {

	@Autowired
	private MemberMapper mapper;

	// 회원 가입 요청 비즈니스 로직 수행할 registMember() 메서드 정의
	// => 파라미터 : MemberVO 객체    리턴타입 : int
	// => MemberMapper - insertMember()
	public int registMember(MemberVO member) {
		return mapper.insertMember(member);
	}

	// 패스워드 조회 비즈니스 로직 수행할 getPasswd() 메서드 정의
	// => 파라미터 : 아이디(또는 MemberVO 객체)   리턴타입 : String(또는 MemberVO 객체)
	// => MemberMapper - selectPasswd()
	public String getPasswd(String id) {
		return mapper.selectPasswd(id);
	}

	// 회원 정보 조회
	// => 파라미터 : 아이디    리턴타입 : MemberVO(member)
	// => MemberMapper - selectMemberInfo()
	public MemberVO getMemberInfo(String id) {
		return mapper.selectMemberInfo(id);
	}

	// 회원 정보 수정 작업 요청
	// => 파라미터 : MemberVO 객체, 새 패스워드(newPasswd)   리턴타입 : int(updateCount)
	// => MemberMapper - updateMemberInfo()
	public int updateMemberInfo(MemberVO member, String newPasswd) {
		return mapper.updateMemberInfo(member, newPasswd);
	}

	// 회원 탈퇴
	// => 파라미터 : 아이디   리턴타입 : int(deleteCount)
	// => MemberMapper - deleteMember()
	public int quitMember(String id) {
		return mapper.deleteMember(id);
	}
	
}














