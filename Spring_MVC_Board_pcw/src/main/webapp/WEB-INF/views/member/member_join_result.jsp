<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- css/main.css 파일 불러오기 -->
<link href="css/main.css" rel="stylesheet" type="text/css">
<link href="css/inc.css" rel="stylesheet" type="text/css">
<script src="js/jquery-3.6.4.js"></script>
<script type="text/javascript">
	$(function() {
		// 페이지 로딩 시 AJAX 요청 수행
		// 파라미터로 전달받은 아이디와 이메일 정보를 포함하여 인증메일 발송 요청
		// => 서블릿 주소 : "MemberSendAuthMail.me" 
		$.ajax({
			url: "MemberSendAuthMail.me",
			data: {
				id: "${param.id}",
				email: "${param.email}"
			},
			dataType: "text"
		});
	});
</script>
</head>
<body>
	<header>
		<%-- inc/top.jsp 페이지 삽입(jsp:include 액션태그 사용 시 / 경로는 webapp 가리킴) --%>
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>회원 가입 완료</h1>
		<h4>가입을 축하합니다. 포인트 1000점 적립 완료.</h4>
		<h4>이메일 인증을 반드시 수행한 후 로그인 가능합니다.</h4>
		<h3>
			<input type="button" value="홈으로" onclick="location.href='./'">
			<input type="button" value="로그인" onclick="location.href='MemberLoginForm.me'">
		</h3>
	</article>
</body>
</html>














