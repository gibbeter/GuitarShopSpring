<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main_padding.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/check.css">
</head>
<body>
<h1><a href="${pageContext.request.contextPath}/cart/redirectToCart">Cart</a></h1>
	
	<c:if test="${!empty checkStatus}">
		${checkStatus}
	</c:if>
	<c:if test="${empty checkStatus}">
		<img src="/GuitarShop/img/check.svg">
		<form action="downloadCheck">
			<input type="submit" value="Download check">
		</form>
	</c:if>
	
	<div class="contacts">
        <p>📞 Contacts</p>
        <span>📷 Instagram</span>
        <span>📘 Facebook</span>
        <span>💬 Viber</span>
        <span>📱 WhatsApp</span>
        <span>📞 +8888888888</span>
    </div>
</body>
</html>