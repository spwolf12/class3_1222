<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<!-- css/main.css 파일 불러오기 -->
<link href="css/inc.css" rel="stylesheet" type="text/css">
<link href="css/subpage.css" rel="stylesheet" type="text/css">
</head>
<body>
	<header>
		<%-- inc/top.jsp 페이지 삽입(jsp:include 액션태그 사용 시 / 경로는 webapp 가리킴) --%>
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<!-- 게시판 답글 작성 폼 -->
	<section id="modifyForm">
		<h1>게시판 답글 쓰기</h1>
		<form action="BoardReplyPro.bo" name="boardForm" method="post">
			<!-- 입력받지 않은 파라미터 데이터(글번호, 페이지번호)는 "hidden" 속성으로 전달 -->
			<input type="hidden" name="board_num" value="${param.board_num }">
			<input type="hidden" name="pageNum" value="${param.pageNum }">
			<!-- 답글 작성에 필요한 정보도 hidden 속성으로 전달(ref, lev, seq 값) -->
			<input type="hidden" name="board_re_ref" value="${board.board_re_ref }">
			<input type="hidden" name="board_re_lev" value="${board.board_re_lev }">
			<input type="hidden" name="board_re_seq" value="${board.board_re_seq }">
			<table>
				<tr>
					<td class="write_td_left"><label for="board_name">글쓴이</label></td>
					<td class="write_td_right"><input type="text" name="board_name" required="required" /></td>
				</tr>
				<tr>
					<td class="write_td_left"><label for="board_pass">비밀번호</label></td>
					<td class="write_td_right">
						<input type="password" name="board_pass" required="required" />
					</td>
				</tr>
				<tr>
					<td class="write_td_left"><label for="board_subject">제목</label></td>
					<td class="write_td_right"><input type="text" id="board_subject" name="board_subject" value="${board.board_subject }" required="required" /></td>
				</tr>
				<tr>
					<td class="write_td_left"><label for="board_content">내용</label></td>
					<td class="write_td_right">
						<textarea id="board_content" name="board_content" required="required">${board.board_content }</textarea>
					</td>
				</tr>
				<tr>
					<td class="write_td_left"><label for="board_file">파일 첨부</label></td>
					<!-- 파일 첨부 형식은 input 태그의 type="file" 속성 사용(파일 업로드 옵션) -->
					<td class="write_td_right">
						<input type="file" name="board_file"/>
					</td>
				</tr>
			</table>
			<section id="commandCell">
				<input type="submit" value="답글등록">&nbsp;&nbsp;
				<input type="reset" value="다시쓰기">&nbsp;&nbsp;
				<input type="button" value="취소" onclick="history.back()">
			</section>
		</form>
	</section>
</body>
</html>








