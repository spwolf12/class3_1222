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
	<%-- top.jsp 페이지를 현재 페이지에 삽입 --%>
	<jsp:include page="../inc/top.jsp" />
	<div align="center">
		<h1>회원 목록 조회</h1>
		<table border="1">
			<tr>
				<th>번호</th>
				<th>이름</th>
				<th>아이디</th>
				<th>E-Mail</th>
				<th>성별</th>
				<th>가입일</th>
				<th></th>
			</tr>	
			<%-- JSTL 을 활용한 List<MemberBean> 객체 순차접근 후 회원목록 출력 --%>
			<%-- 단, memberList 객체가 없을 경우 "회원 목록이 존재하지 않습니다" 출력 --%>
			<c:choose>
				<c:when test="${empty memberList }">
					<tr>
						<td colspan="7">회원 목록이 존재하지 않습니다.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<%-- <c:forEach> 문을 사용하여 회원목록 저장 객체 반복하면서 출력 --%>
					<c:forEach var="member" items="${memberList }">
						<tr>
							<td>${member.idx }</td>
							<td>${member.name }</td>
							<td>${member.id }</td>
							<td>${member.email }</td>
							<td>${member.gender }</td>
							<td>${member.date }</td>
							<td>
								<input type="button" value="수정" onclick="">
								<input type="button" value="삭제">
							</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>
	</div>
</body>
</html>


















