<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chat</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main_padding.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/chat.css">
</head>
<body>
    <h1><a href="${pageContext.request.contextPath}/user/redirectToIndex?userId=${userId}">GuitarShop</a></h1>
    <h1><a class="chat-link" href="${pageContext.request.contextPath}/chat/redirectToChats?userId=${userId}">Chats</a></h1>

	<c:choose>
		<c:when test="${chat.user1Id == userId}">
			<h2>Chat with ${chat.user2Username}</h2>
		</c:when>
		<c:otherwise>
			<h2>Chat with ${chat.user1Username}</h2>
		</c:otherwise>
	</c:choose>

	<c:if test="${!empty messages}">
        <div class="message-container">
            <c:forEach items="${messages}" var="m">
                <c:choose>
                    <c:when test="${m.senderId == userId}">
                        <div class="message message-own">
                            <div class="message-indicator">●</div>
                            <div class="message-text">${m.messageText}</div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="message message-other">
                            <div class="message-indicator">●</div>
                            <div class="message-text">${m.messageText}</div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </c:if>

    <form action="sendMessage" method="post">
        <textarea name="messageText" rows="4" cols="50" placeholder="New message"></textarea>
        <br>
        <input type="hidden" name="senderId" value="${userId}">
        <input type="hidden" name="chatId" value="${chat.chatId}">
        <input type="hidden" name="user1Id" value="${chat.user1Id}">
        <input type="hidden" name="user2Id" value="${chat.user2Id}">
        <input type="submit" value="Send">
    </form>
    <c:if test="${!empty messageStatus}">
        <div class="message-status">${messageStatus}</div>
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