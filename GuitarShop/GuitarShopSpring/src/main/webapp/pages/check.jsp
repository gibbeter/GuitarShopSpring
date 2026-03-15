<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1><a href="${pageContext.request.contextPath}/cart/redirectToCart?userId=${userId}">Cart</a></h1>
	
	<img>
	<form action="downloadCheck">
		<input type="submit" value="Download check">
	</form>
</body>
</html>