<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<!-- css/main.css 파일 불러오기 -->
<link href="${pageContext.request.contextPath }/resources/css/inc.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath }/resources/css/subpage.css" rel="stylesheet" type="text/css">
</head>
<body>
	<header>
		<%-- inc/top.jsp 페이지 삽입(jsp:include 액션태그 사용 시 / 경로는 webapp 가리킴) --%>
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<!-- 게시판 등록 -->
	<article id="writeForm">
		<h1>게시판 글 등록</h1>
		<!-- 
		파일 업로드 기능을 사용하려면 form 태그의 enctype 속성을 사용하여 업로드 기능 설정(POST 방식 필수)
		=> 업로드를 위한 enctype 속성값 : multipart/form-data(모든 데이터 인코딩하지 않음)
		=> 기본 enctype 속성값 : application/x-www-form-urlencoded(모든 데이터를 서버로 전송 전 인코딩)
		   만약, 기본 상태로 파일 업로드 시 파일명만 서버로 전송됨
		-----------------------------------------------------------------
		업로드 기능을 사용하려면 cos.jar 라이브러리나 commons-io, commons-fileupload 라이브러리 필요
		-->
		<form action="BoardWritePro.bo" name="writeForm" method="post" enctype="multipart/form-data">
			<!-- board_name 값은 세션 아이디를 전달(hidden) -->
			<input type="hidden" name="board_name" value="${sessionScope.sId }" />
			<table>
				<tr>
					<td class="write_td_left"><label for="board_subject">제목</label></td>
					<td class="write_td_right"><input type="text" id="board_subject" name="board_subject" required="required" /></td>
				</tr>
				<tr>
					<td class="write_td_left"><label for="board_content">내용</label></td>
					<td class="write_td_right">
						<textarea id="board_content" name="board_content" required="required"></textarea>
					</td>
				</tr>
				<tr>
					<td class="write_td_left"><label for="file">파일 첨부</label></td>
					<!-- 파일 첨부 형식은 input 태그의 type="file" 속성 사용 -->
					<td class="write_td_right">
						<input type="file" name="file" required="required" />
					</td>
				</tr>
			</table>
			<section id="btnArea">
				<input type="submit" value="등록">&nbsp;&nbsp;
				<input type="reset" value="다시쓰기">&nbsp;&nbsp;
				<input type="button" value="취소" onclick="history.back()">
			</section>
		</form>
	</article>
</body>
</html>








