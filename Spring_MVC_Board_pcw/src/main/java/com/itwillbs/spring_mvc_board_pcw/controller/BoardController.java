package com.itwillbs.spring_mvc_board_pcw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.spring_mvc_board_pcw.service.BoardService;
import com.itwillbs.spring_mvc_board_pcw.vo.BoardVO;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	// 글쓰기 폼
	// => 세션 아이디가 존재하지 않으면 "로그인 필수!", "MemberLoginForm.me" 저장 후 success 로 이동
	@GetMapping("/BoardWriteForm.bo")
	public String writeForm(HttpSession session, Model model) {
		if(session.getAttribute("sId") == null) {
			model.addAttribute("msg", "로그인 필수!");
			model.addAttribute("target", "MemberLoginForm.me");
			return "success";
		}
		
		return "board/board_write_form";
	}
	
	// -----------------------------------------------------------------------------------
	// 파일 업로드 기능이 포함된(enctype="multipart/form-data") 폼 파라미터 처리할 경우
	// 1) 각 파라미터를 각각의 변수로 처리하면서 
	//    업로드 파일을 매핑 메서드의 MultipartFile 타입으로 직접 처리하는 경우
//	@PostMapping("/BoardWritePro.bo")
//	public String writePro(
//			String board_name, String board_subject, String board_content, MultipartFile file) {
//		System.out.println(board_name + ", " + board_subject + ", " + board_content);
//		System.out.println("업로드 파일명 : " + file.getOriginalFilename());
//		
//		
//		return "";
//	}
	
	// 2) 파일을 제외한 나머지 파라미터를 Map 타입으로 처리하고, 파일은 MultipartFile 타입 변수로 처리
	//    => 주의! Map 타입 파라미터 선언 시 @RequestParam 어노테이션 필수!
//	@PostMapping("/BoardWritePro.bo")
//	public String writePro(
//			@RequestParam Map<String, String> map, MultipartFile file) {
//		System.out.println(map.get("board_name") + " " + map.get("board_subject") + " " + map.get("board_content"));
//		System.out.println("업로드 파일명 : " + file.getOriginalFilename());
//		
//		
//		return "";
//	}
	
	// 3) MultipartFile 타입 멤버변수를 포함하는 BoardVO 타입으로 모든 파라미터를 한꺼번에 처리
	// => BoardVO 클래스에 MultipartFile 타입 멤버변수 선언 시
	//    반드시 <input type="file"> 태그의 name 속성명과 동일한 이름의 멤버변수를 선언하고
	//    Getter/Setter 정의 필수!
	@PostMapping("/BoardWritePro.bo")
	public String writePro(BoardVO board, HttpSession session, Model model) {
//		System.out.println(board);
//		System.out.println("업로드 파일명 : " + board.getFile().getOriginalFilename());
		
		// 이클립스 프로젝트 상에 업로드 폴더(upload) 생성 필요 - resources 폴더에 생성(외부 접근용)
		// 이클립스가 관리하는 프로젝트 상의 가상 업로드 경로에 대한 실제 업로드 경로 알아내기
		// => request 또는 session 객체의 getServletContext() 메서드를 호출하여 서블릿 컨텍스트 객체를 얻어낸 후
		//    다시 getRealPath() 메서드를 호출하여 실제 업로드 경로 알아낼 수 있다!
		//    (JSP 일 경우 request 객체로 getRealPath() 메서드 호출이 가능함)
		String uploadDir = "/resources/upload"; // 프로젝트 상의 업로드 경로
//		String saveDir = request.getServletContext().getRealPath(uploadDir);
		String saveDir = session.getServletContext().getRealPath(uploadDir);
		// => D:\Shared\Spring\workspace_spring3\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Spring_MVC_Board\resources\ upload
//		System.out.println("실제 업로드 경로 : " + saveDir);
		
		try {
			// -----------------------------------------------------------------------
			// 업로드 디렉토리를 날짜별 디렉토리로 분류하기
			// => 하나의 디렉토리에 너무 많은 파일이 존재하면 로딩 시간 길어짐
			//    따라서, 날짜별로 디렉토리를 구별하기 위해 java.util.Date 클래스 활용
			Date date = new Date();
			// SimpleDateFormat 클래스를 활용하여 날짜 형식을 "연연연연-월월-일일" 로 지정
			// => 단, 편의상 디렉토리 구조를 그대로 나타내기 위해 - 대신 / 기호 사용
			//    (가장 정확히 표현하려면 디렉토리 구분자를 File.seperator 상수로 사용)
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			board.setBoard_file_path("/" + sdf.format(date));
			
			// 기본 업로드 경로와 서브 디렉토리 경로 결합하여 저장
			saveDir = saveDir + board.getBoard_file_path();
			// -----------------------------------------------------------------------
			
			// java.nio.file.Paths 클래스의 get() 메서드를 호출하여  
			// 실제 경로를 관리하는 java.nio.file.Path 타입 객체를 리턴받기(파라미터 : 실제 업로드 경로)
			Path path = Paths.get(saveDir);
			// Files 클래스의 createDirectories() 메서드를 호출하여 Path 객체가 관리하는 경로 없으면 생성
			// => 거쳐가는 경로들 중 없는 경로는 모두 생성
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// BoardVO 객체에 전달된 MultipartFile 객체 꺼내기
		// => 단, 복수개의 파일(파라미터)이 관리되는 경우 MultipartFile[] 타입으로 꺼내기
		MultipartFile mFile = board.getFile(); // 단일 파일
//		MultipartFile[] mFiles = board.getFiles(); // 복수 파일
		
		String originalFileName = mFile.getOriginalFilename();
//		System.out.println("원본 파일명 : " + originalFileName);
		
		// 파일명 중복 방지를 위한 대책
		// 현재 시스템(서버)에서 랜덤ID 값을 추출하여 파일명 앞에 붙여
		// "랜덤ID값_파일명.확장자" 형식으로 중복 파일명 처리
		// => 랜덤ID 생성은 java.util.UUID 클래스 활용(UUID = 범용 고유 식별자)
		String uuid = UUID.randomUUID().toString();
//		System.out.println("UUID : " + uuid);
		
		// 생성된 UUID 값을 원본 파일명 앞에 결합(파일명과 구분을 위해 _ 기호 추가)
		// => 나중에 사용자에게 다운로드 파일명 표시할 때 원래 파일명 표시를 위해 분리할 때 사용
		//    (가장 먼저 만나는 _ 기호를 기준으로 문자열 분리하여 처리)
		// => 단, 파일명 길이 조절을 위해 UUID 중 맨 앞의 8자리 문자열만 활용
		//    (substring(0, 8) 메서드 활용)
//		originalFileName = UUID.randomUUID().toString() + "_" + originalFileName;
		// => 생성된 파일명을 BoardVO 객체의 board_file 변수에 저장
		board.setBoard_file(uuid.substring(0, 8) + "_" + originalFileName);
		System.out.println("실제 업로드 될 파일명 : " + board.getBoard_file());
		
		// -------------------------------------------------------------------------------
		
		// BoardService - registBoard() 메서드를 호출하여 게시물 등록 작업 요청
		// => 파라미터 : BoardVO 객체    리턴타입 : int(insertCount)
		int insertCount = service.registBoard(board);
		
		// 게시물 등록 작업 결과 판별
		// => 성공 시 업로드 파일을 실제 폴더에 이동시킨 후 BoardList.bo 서블릿 리다이렉트
		// => 실패 시 "글 쓰기 실패!" 메세지를 저장 후 fail_back.jsp 페이지로 포워딩
		if(insertCount > 0) { // 성공
			// 업로드 된 파일은 MultipartFile 객체에 의해 임시 폴더에 저장되어 있으며
			// 글쓰기 작업 성공 시 임시 위치에 저장된 파일을 실제 폴더로 옮기는 작업 필요
			// => MultipartFile 객체의 transferTo() 메서드를 호출하여 실제 위치로 이동(업로드)
			//    (파라미터 : java.io.File 객체 => new File(업로드 경로, 업로드 파일명))
			try {
				mFile.transferTo(new File(saveDir, board.getBoard_file()));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return "redirect:/BoardList.bo";
		} else { // 실패
			model.addAttribute("msg", "글 쓰기 실패!");
			return "fail_back";
		}
		
	}
	
}













