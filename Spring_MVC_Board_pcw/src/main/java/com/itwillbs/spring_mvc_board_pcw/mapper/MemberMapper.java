package com.itwillbs.spring_mvc_board_pcw.mapper;

import org.apache.ibatis.annotations.Param;

import com.itwillbs.spring_mvc_board_pcw.vo.MemberVO;

public interface MemberMapper {
	
	// 회원 가입
	int insertMember(MemberVO member);

	// 패스워드 조회
	String selectPasswd(String id);

	// 회원 정보 조회
	MemberVO selectMemberInfo(String id);

	// 회원 정보 수정
	// 주의! 메서드 파라미터 2개 이상을 XML 에서 접근하려면
	// @Param 어노테이션을 통해 각 파라미터의 변수명을 직접 지정해야한다!
//	int updateMemberInfo(MemberVO member, String newPasswd); // 오류 발생
	int updateMemberInfo(@Param("member") MemberVO member, @Param("newPasswd") String newPasswd);

	// 회원 탈퇴
	int deleteMember(String id);

}














