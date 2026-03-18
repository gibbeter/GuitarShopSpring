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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
	<div class="header">
		<h1>
			<span class="nameTag">GuitarShop</span>
		</h1>
		<div class="header-right">
			<h1>
				<a class="cart-link"
					href="${pageContext.request.contextPath}/cart/redirectToCart">🛒Cart</a>
			</h1>
		</div>
	</div>

	<div class="user-link">
		<c:choose>
			<c:when test="${!empty userId}">
				<a href="/GuitarShop/user/redirectToAccount">👤 ${userName}</a>
			</c:when>
			<c:otherwise>
				<a href="/GuitarShop/user/redirectToAccount">🔑 Account</a>
			</c:otherwise>
		</c:choose>
	</div>

	<table>
    	<tr>
    		<td colspan="3"><a href="${pageContext.request.contextPath}/product/redirectToType" class="all-products-link">← All Products</a></td>
    	</tr>
        <tr>
            <td><a href="${pageContext.request.contextPath}/product/redirectToType?type=guitar">🎸 Guitars</a></td>
            <td><a href="${pageContext.request.contextPath}/product/redirectToType?userId=${userId}&type=drums">🥁 Drums</a></td>
            <td><a href="${pageContext.request.contextPath}/product/redirectToType?userId=${userId}&type=keys">🎹 Keys</a></td>
        </tr>
        <tr>
            <td><a href="${pageContext.request.contextPath}/product/redirectToType?type=microphone">🎤 Microphones</a></td>
            <td><a href="${pageContext.request.contextPath}/product/redirectToType?type=cable">🔌 Cables & Connectors</a></td>
            <td><a href="${pageContext.request.contextPath}/product/redirectToType?type=soft">💻 Software</a></td>
        </tr>
    </table>

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