<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main_padding.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
	<h1><a href="${pageContext.request.contextPath}/user/redirectToIndex">GuitarShop</a></h1>
	<h2>Sorry</h2>
	<p>It seems something went wrong, please return to main page</p>
	<div>
		<c:if test="${!empty exMessage}">
			<span class="tag">Error:</span>
			<span class="message"> ${exMessage} </span>
		</c:if>
	</div>
</body>
</html>