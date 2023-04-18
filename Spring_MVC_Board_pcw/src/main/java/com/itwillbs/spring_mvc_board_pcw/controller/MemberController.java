package com.itwillbs.spring_mvc_board_pcw.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.spring_mvc_board_pcw.service.MemberService;
import com.itwillbs.spring_mvc_board_pcw.vo.MemberVO;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService service;
	
	// "/MemberJoinForm.me" 요청에 대해 "member/member_join_form.jsp" 페이지 포워딩
	// => GET 방식 요청, Dispatch 방식 포워딩
	@GetMapping(value = "/MemberJoinForm.me")
	public String joinForm() {
		return "member/member_join_form";
	}
	
	// "/MemberJoinPro.me" 요청에 대해 MemberService 객체 비즈니스 로직 수행
	// => POST 방식 요청, Redirect 방식
	// => 폼 파라미터로 전달되는 가입 정보를 파라미터로 전달받기
	// => 가입 완료 후 이동할 페이지 : 메인페이지(index.jsp)
	// => 가입 실패 시 오류 페이지(fail_back)를 통해 "회원 가입 실패!" 출력 후 이전페이지로 돌아가기
	@PostMapping(value = "/MemberJoinPro.me")
	public String joinPro(MemberVO member, Model model) {
//		System.out.println(member);
		
		// ------------ BCryptPasswordEncoder 객체 활용한 패스워드 암호화(해싱) -------------
		// 입력받은 패스워드는 암호화(해싱) 필요 => 해싱 후 MemberVO 객체 패스워드에 덮어쓰기
		// => 스프링에서 암호화는 org.springframework.security.crypto.bcrypt 패키지의
		//    BCryptPasswordEncoder 클래스 활용(spring-security-web 라이브러리 추가 필요)
		// => BCryptPasswordEncoder 클래스 활용하여 해싱할 경우 Salting(솔팅) 기능을 통해
		//    동일한 평문(원본 암호)이라도 매번 다른 결과값을 갖는 해싱이 가능하다!
		// 1. BCryptPasswordEncoder 객체 생성
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// 2. BCryptPasswordEncoder 객체의 encode() 메서드를 호출하여 해싱 후 결과값 리턴
		// => 파라미터 : 평문 암호    리턴타입 : String(해싱된 암호)
		String securePasswd = passwordEncoder.encode(member.getPasswd());
//		System.out.println("평문 : " + member.getPasswd()); // 평문 암호 출력
//		System.out.println("암호문 : " + securePasswd); // 해싱된 암호 출력
		// 3. MemberVO 객체의 패스워드에 암호화 된 패스워드 저장(덮어쓰기)
		member.setPasswd(securePasswd);
		// ----------------------------------------------------------------------------------
		// MemberService - registMember() 메서드 호출을 통해 회원 가입 작업 요청
		// => 파라미터 : MemberVO 객체    리턴타입 : int
		int insertCount = service.registMember(member);
		
		if(insertCount > 0) { // 가입 성공
			// 리다이렉트 방식으로 "/" 요청(HomeController - index.jsp)
			return "redirect:/";
		} else { // 가입 실패
			// Model 객체의 "msg" 속성으로 "회원 가입 실패!" 문자열 저장 후 fail_back.jsp 포워딩
			model.addAttribute("msg", "회원 가입 실패!");
			return "fail_back";
		}
		
	}
	
	@GetMapping("/MemberLoginForm.me")
	public String loginForm() {
		return "member/member_login_form";
	}
	
	// "/MemberLoginPro.me" 요청에 대한 로그인 비즈니스 로직 처리 - POST
	// => 파라미터 : 아이디, 패스워드 저장을 위한 MemberVO 타입 변수
	//               처리 실패 시 fail_back.jsp 페이지로 메세지 전달할 Model 타입 변수
	//               처리 성공 시 세션 처리를 위한 HttpSession 타입 변수
	@PostMapping("/MemberLoginPro.me")
	public String loginPro(MemberVO member, Model model, HttpSession session) {
		System.out.println(member);
		
		// ------------------------------ 가장 기본적인 로그인 ---------------------------------
		// 입력받은 패스워드는 암호화(해싱) 필요 => 해싱 후 MemberVO 객체 패스워드에 덮어쓰기
		// => 이미 암호화되어 저장되어 있는 기존 패스워드와 암호화 된 입력 패스워드를 비교해야함
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		System.out.println(passwordEncoder.encode(member.getPasswd()));
		// $2a$10$vkoPyIVPRAxgyUzbOHUdteTp3QeQZEI7NffgwJOlGoWOA4cXeyIe2 // 입력받은 패스워드(해싱)
		// $2a$10$GUjiwRQDIgg/5GPDbUvKb./43EWOrNPVX.ovgTwLcmPEfCsmRcXWy // 저장된 패스워드(해싱)
		// => 두 패스워드가 서로 다른 문자열이기 때문에 equals() 메서드로 비교 시 다른 패스워드이다!
		// ------------ BCryptPasswordEncoder 객체 활용한 로그인(해싱된 암호 비교) -------------
		// 입력받은 패스워드는 평문 그대로 두고, DB 에 저장된 해싱된 패스워드(암호문)를 가져와서
		// BCryptPasswordEncoder 객체의 matches() 메서드를 통해 비교를 수행해야한다!
		// 1. BCryptPasswordEncoder 객체 생성
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		// 2. member 테이블에서 id 가 일치하는 레코드의 해싱된 패스워드 조회하기 위해
		//    MemberService - getPasswd() 메서드 호출
		//    => 파라미터 : 아이디(또는 MemberVO 객체)   리턴타입 : String(또는 MemberVO 객체)
		String passwd = service.getPasswd(member.getId());
//		System.out.println(passwd);
		
		// 3. DB로부터 조회된 기존 패스워드(암호문)을 입력받은 패스워드(평문)과 비교하여
		//    로그인 성공 여부 판별 - BCryptPasswordEncoder 객체의 matches() 메서드 활용
		//    => boolean matches(평문, 암호문)
		//    1) 입력받은 아이디가 존재하지 않을 경우(passwd 가 null) - 실패
		//    2) 입력받은 아이디가 존재할 경우(passwd 가 null 이 아님)
		//       2-1) 두 패스워드가 일치하지 않을 경우 - 실패
		//       2-2) 두 패스워드가 일치할 경우 - 성공
		//    => 아이디 없음과 패스워드 틀림을 하나로 묶어서 판별
		if(passwd == null || !passwordEncoder.matches(member.getPasswd(), passwd)) { // 로그인 실패
			// "로그인 실패!" 메세지 저장 후 "fail_back.jsp" 페이지로 포워딩
			model.addAttribute("msg", "로그인 실패!");
			return "fail_back";
		} else { // 로그인 성공(= 패스워드 일치)
			// 세션 객체에 아이디 저장(속성명 sId) 후 메인페이지로 리다이렉트
			session.setAttribute("sId", member.getId());
			return "redirect:/";
		}
		
	}
	
	@GetMapping("/MemberLogout.me")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	// "/MemberInfo.me" 서블릿 요청에 대해 회원 정보 조회 비즈니스 로직 수행
	// => 세션 아이디를 가져와서 아이디로 활용
	@GetMapping("/MemberInfo.me")
	public String info(HttpSession session, Model model) {
		String id = (String)session.getAttribute("sId");
		
		// 세션 아이디가 존재하지 않으면(null) fail_back.jsp 를 활용하여 "잘못된 접근입니다" 출력
		if(id == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		} 
		
		// if 문 내에서 return 문이 사용되면 메서드가 종료되므로 else 문 없이도 처리 가능
		// MemberService - getMemberInfo() 메서드를 호출하여 회원 정보 조회
		// => 파라미터 : 아이디    리턴타입 : MemberVO(member)
		MemberVO member = service.getMemberInfo(id);
//		System.out.println(member.getEmail());
		
		// 단, email 의 경우 계정명과 도메인을 분리해야하므로
		// 문자열 분리(split)를 통해 email1, email2 에 나누어 저장하는 작업 추가
//		member.setEmail1(member.getEmail().split("@")[0]);
//		member.setEmail2(member.getEmail().split("@")[1]);
		// => 단, 뷰페이지에서 JSTL 의 functions 라이브러리로도 동일한 작업 가능
		
		// 조회 결과(MemberVO 객체)를 Model 객체에 "member" 속성명으로 저장
		model.addAttribute("member", member);
		
		// member_info.jsp 페이지로 포워딩
		return "member/member_info";
	}
	
	@PostMapping("/MemberUpdate.me")
	public String update(MemberVO member, @RequestParam String newPasswd, HttpSession session, Model model) {
		// 세션 아이디가 존재하지 않을 경우 fail_back 페이지 활용하여 "잘못된 접근입니다!" 출력
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		}
		
		// 입력받은 기존 비밀번호(passwd)와 
		// 세션 아이디와 일치하는 데이터베이스 내의 레코드의 비밀번호 비교 수행
		// MemberService - getPasswd() 활용
		// BCryptPasswordEncoder - matches() 메서드를 통해 패스워드 비교 수행
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String passwd = service.getPasswd(id);
		
		if(passwd == null || !passwordEncoder.matches(member.getPasswd(), passwd)) { // 불일치
			// "수정 권한 없음!" 메세지 저장 후 "fail_back.jsp" 페이지로 포워딩
			model.addAttribute("msg", "수정 권한 없음!");
			return "fail_back";
		}
		
		// 만약, newPasswd 파라미터가 존재할 경우 암호화(해싱) 수행하여 덮어쓰기
		if(!newPasswd.equals("")) {
			newPasswd = passwordEncoder.encode(newPasswd);
		}
		
		// MemberService - updateMemberInfo() 메서드를 호출하여 회원 정보 수정 작업 요청
		// => 파라미터 : MemberVO 객체, 새 패스워드(newPasswd)   리턴타입 : int(updateCount)
		int updateCount = service.updateMemberInfo(member, newPasswd);
		
		// 수정/실패에 따른 포워딩 작업 수행
		// 실패 시 Model 객체에 "회원 정보 수정 실패" 저장 후 fail_back.jsp 포워딩 
		// 성공 시 Model 객체의 "msg" 속성으로 "회원 정보 수정 성공!" 저장 및
		// "target" 속성으로 이동할 페이지("MemberInfo.me") 저장 후 success.jsp 페이지로 포워딩
		if(updateCount > 0) {
			model.addAttribute("msg", "회원 정보 수정 성공!");
			model.addAttribute("target", "MemberInfo.me");
			return "success";
		} else {
			model.addAttribute("msg", "회원 정보 수정 실패!");
			return "fail_back";
		}
	}
	
	@GetMapping("/MemberQuitForm.me")
	public String quitForm() {
		return "member/member_quit_form";
	}
	
	// 회원 탈퇴를 위한 비즈니스 로직 수행
	// => 파라미터 : 패스워드, HttpSession, Model
	@PostMapping("/MemberQuitPro.me")
	public String quitPro(@RequestParam String passwd, HttpSession session, Model model) {
		// 세션 아이디 꺼내서 저장
		String id = (String)session.getAttribute("sId");
		
		// 해당 아이디와 일치하는 레코드의 패스워드 조회
		// => MemberService - getPasswd() 재사용
		String dbPasswd = service.getPasswd(id);

		System.out.println("평문 암호 : " + passwd + ", 해싱 암호 : " + dbPasswd);
		
		// BCryptPasswordEncoder - matches() 메서드를 활용하여 평문 암호와 저장된 암호 비교
		// => 일치할 경우 MemberService - quitMember() 메서드 호출
		//    (파라미터 : 아이디   리턴타입 : int(deleteCount)
		// => 일치하지 않을 경우 Model 객체의 "msg" 속성으로 "권한이 없습니다!" 저장 후 fail_back 으로 이동
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(passwordEncoder.matches(passwd, dbPasswd)) {
			int deleteCount = service.quitMember(id);
			
			// 탈퇴 결과(deleteCount) 판별
			// => 성공 시 세션 초기화 및 Model 객체의 "msg" 속성으로 
			//    "탈퇴가 완료되었습니다!" 저장, "target" 속성으로 메인페이지 ("./") 지정 후 
			//    success 로 이동
			// => 실패 시 Model 객체의 "msg" 속성으로 "탈퇴 실패!" 저장 후 fail_back 으로 이동
			if(deleteCount > 0) {
				session.invalidate();
				
				model.addAttribute("msg", "탈퇴가 완료되었습니다!");
				model.addAttribute("target", "./");
				return "success";
			} else {
				model.addAttribute("msg", "탈퇴 실패!");
				return "fail_back";
			}
			
		} else {
			model.addAttribute("msg", "권한이 없습니다!");
			return "fail_back";
		}
		
	}
	
}















