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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/chats.css">
</head>
<body>
	<div class="header">
		<h1>
			<a href="${pageContext.request.contextPath}/user/redirectToIndex">GuitarShop</a>
		</h1>
		<div class="header-right">
			<h1>
				<c:if test="${!empty userId && userType != 'guest'}">
					<a href="${pageContext.request.contextPath}/user/redirectToAccount">Account</a>
				</c:if>
			</h1>
		</div>
	</div>
	
	<c:if test="${!empty delStatus}">
		<div class="del-conteiner">
			<span class="delStatus" data-content="${delStatus}">${delStatus}</span>
		</div>
	</c:if>
	
	<c:if test="${!empty chats}">
		<table>
			<c:forEach items="${chats}" var="c">
				<tr>
					<td>
						<div class="chat-container">
							<c:choose>
								<c:when test="${c.user1Id == userId}">
									<a class="chat-text" href="${pageContext.request.contextPath}/chat/redirectToChat?chatId=${c.chatId}">Chat with ${c.user2Username}</a>
								</c:when>
								<c:otherwise>
									<a class="chat-text" href="${pageContext.request.contextPath}/chat/redirectToChat?chatId=${c.chatId}">Chat with ${c.user1Username}</a>
								</c:otherwise>
							</c:choose>
							<form action="deleteChat?chatId=${c.chatId}" method="post" class="delete-form">
		                        <button type="submit" class="delete-btn">Delete</button>
	                    	</form>
	                   	</div>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	
	<c:if test="${empty chats}">You have no active chats</c:if>
	
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