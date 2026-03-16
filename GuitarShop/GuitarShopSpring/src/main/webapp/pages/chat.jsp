<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1><a href="${pageContext.request.contextPath}/user/redirectToIndex">GuitarShop</a></h1>
	<h1><a href="${pageContext.request.contextPath}/chat/redirectToChats?userId=${userId}">Chats</a></h1>
	<h2>Chat with User${chat.user2Id}</h2>
	<c:if test="${!empty messages}">
		<table>
			<c:forEach items="${messages}" var="m">
				<tr>
					<c:choose>
						
						<c:when test="${m.senderId == userId}">
							<td>[*]</td>
							<td>${m.messageText}</td>

						</c:when>
						<c:otherwise>
							<td>${m.messageText}</td>
							<td>[*]</td>
						</c:otherwise>
					</c:choose>
					
				</tr>
			</c:forEach>
			<tr>
				<td>
					
				</td>
			</tr>
		</table>
	</c:if>
	<form action="sendMessage" method="post">
		<textarea name="messageText" rows="4" cols="50" placeholder="New message"></textarea>
		<br>
		<input type="hidden" name="senderId" value="${userId}">
		<input type="hidden" name="chatId" value="${chat.chatId}">
		<input type="hidden" name="user1Id" value="${chat.user1Id}">
		<input type="hidden" name="user2Id" value="${chat.user2Id}">
		<input type="submit" value="Send">
		<c:if test="${!empty messageStatus}">${messageStatus}</c:if>
	</form>
</body>
</html>