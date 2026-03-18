<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GuitarShop – Account</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main_padding.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/account.css">
</head>
<body>
	<div class="header">
		<h1>
			<a href="${pageContext.request.contextPath}/user/redirectToIndex">GuitarShop</a>
		</h1>
		<div class="header-right">
			<h1>
				<c:if test="${!empty userId && userType != 'guest'}">
					<a href="${pageContext.request.contextPath}/chat/redirectToChats">Chats</a>
				</c:if>
			</h1>
		</div>
	</div>

    <c:choose>
        <c:when test="${!empty userId}">
            <c:if test="${userType == 'admin'}">
                <form action="/GuitarShop/product/redirectToStorage">
                    <input type="submit" value="Storage">
                </form>
            </c:if>

            <form action="changeName" method="post">
                <table>
                    <tr>
                        <td colspan="3">User "${userName}" data:</td>
                    </tr>
                    <tr>
                        <td>Username</td>
                        <td><input type="text" name="userName" value="${userName}"></td>
                        <c:if test="${userType != 'guest'}">
                            <td><input type="submit" value="Change"></td>
                            <td>${updateNameStatus}</td>
                        </c:if>
                    </tr>
                </table>
            </form>

            <c:if test="${userType != 'guest'}">
                <form action="changePass" method="post">
                    <table>
                        <tr>
                            <td>Password</td>
                            <td><input type="password" name="password" value="${userPassword}"></td>
                            <td><input type="submit" value="Change"></td>
                            <td>${updatePassStatus}</td>
                        </tr>
                    </table>
                </form>

                <form action="changeMail" method="post">
                    <table>
                        <tr>
                            <td>Mail</td>
                            <td><input type="text" name="userMail" value="${userMail}"></td>
                            <td><input type="submit" value="Change"></td>
                            <td>${updateMailStatus}</td>
                        </tr>
                    </table>
                </form>
            </c:if>

            <form action="logout">
                <input type="submit" value="Log out">
            </form>
        </c:when>

        <c:otherwise>
            <div>
                <form action="defaultAction" method="post">
                    <table>
                        <tr>
                            <td colspan="2"><input type="text" name="userName" placeholder="Username"></td>
                        </tr>
                        <tr>
                            <td colspan="2"><input type="password" name="userPass" placeholder="Password"></td>
                        </tr>
                        <tr>
                            <td><input type="submit" formaction="login" value="Log in"></td>
                            <td><input type="submit" formaction="register" value="Register"></td>
                        </tr>
                    </table>
                    <input type="hidden" name="type" value="user">
                    <input type="hidden" name="userMail" value="empty@mail.em">
                </form>
                <c:if test="${!empty errorStatus}">
                    <div class="message error">${errorStatus}</div>
                </c:if>
                <c:if test="${!empty regStatus}">
                    <div class="message success">${regStatus}</div>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
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