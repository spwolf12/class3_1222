<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>      
<script>
	function confirmLogout() {
		let result = confirm("로그아웃하시겠습니까?");
		return result;
	}
</script>    
<div id="member_area">
	<a href="./">홈</a> |
	<!-- 세션 아이디가 없을 경우 로그인, 회원가입 링크 표시 -->
	<!-- 아니면, 아이디 표시, 로그아웃 링크 표시. 단, 관리자는 관리자페이지 링크도 표시 -->
	<c:choose>
		<c:when test="${empty sessionScope.sId }">
			<a href="MemberLoginForm.me">로그인</a> | 
			<a href="MemberJoinForm.me">회원가입</a>
		</c:when>
		<c:otherwise>
			<a href="MemberInfo.me">${sessionScope.sId }</a> 님 | 
			<a href="MemberLogout.me" id="logout" onclick="return confirmLogout()">로그아웃</a>
			<c:if test="${sessionScope.sId eq 'admin' }">
				| <a href="AdminMain.me">관리자페이지</a>
			</c:if>
		</c:otherwise>
	</c:choose>
</div>
















