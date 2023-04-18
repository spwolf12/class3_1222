<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 전달받은 오류메세지(msg 속성) 출력 후 이전 페이지로 돌아가기 - 자바스크립트 --%>
<script>
	alert("${msg}");
	history.back();
</script>