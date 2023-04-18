<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- css/main.css 파일 불러오기 -->
<link href="css/inc.css" rel="stylesheet" type="text/css">
<link href="css/main.css" rel="stylesheet" type="text/css">
</head>
<body>
	<%-- 세션 아이디가 비어있거나 "admin" 이 아닐 경우 메인페이지로 돌려보내기 --%>
	<%-- JSTL 에서는 "admin" 이 아닐 경우의 조건만 설정해도 모두 적용됨(empty 비교 생략 가능)--%>
	<c:if test="${sessionScope.sId ne 'admin' }">
		<%-- 자바스크립트를 통해 "잘못된 접근입니다!" 출력 후 메인페이지로 포워딩 --%>
		<script type="text/javascript">
			alert("잘못된 접근입니다!");
			location.href = "./";
		</script>
	</c:if>
	<header>
		<%-- inc/top.jsp 페이지 삽입(jsp:include 액션태그 사용 시 / 경로는 webapp 가리킴) --%>
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article id="main">
		<h1>관리자 메인페이지</h1>
		<h3><a href="AdminMemberList.me">회원목록 조회</a></h3>
	</article>
</body>
</html>














