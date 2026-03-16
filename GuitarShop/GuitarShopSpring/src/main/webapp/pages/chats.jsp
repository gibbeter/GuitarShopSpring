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
	<h1><a href="${pageContext.request.contextPath}/user/redirectToIndex">GuitarShop</a></h1>
	<c:if test="${!empty chats}">
		<table>
			<c:forEach items="${chats}" var="c">
				<tr>
					<td>
						<c:choose>
						
							<c:when test="${c.user1Id == userId}">
								<a href="${pageContext.request.contextPath}/chat/createChat?user1Id=${userId}&user2Id=${c.user2Id}">Chat with ${c.user2Id}</a>
	
							</c:when>
							<c:otherwise>
								<a href="${pageContext.request.contextPath}/chat/createChat?user1Id=${userId}&user2Id=${c.user1Id}">Chat with ${c.user1Id}</a>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
		
	</c:if>

</body>
</html>