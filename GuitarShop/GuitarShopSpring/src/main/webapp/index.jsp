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
	
	<c:if test="${!empty exMessage}">
		<div class="ex-conteiner">
			<span class="exMessage" data-content="${exMessage}">${exMessage}</span>
		</div>
	</c:if>

	<table>
    	<tr>
    		<td colspan="3"><a href="${pageContext.request.contextPath}/product/redirectToType" class="all-products-link">← All products</a></td>
    	</tr>
        <tr>
            <td>
            	<a href="${pageContext.request.contextPath}/product/redirectToType?type=guitar" class="img-pan">
            		<img src="/GuitarShop/img/guitars-pan.webp" alt="GUITARS">
            		<span>Guitars</span>
           		</a>
         	</td>
            <td>
            <a href="${pageContext.request.contextPath}/product/redirectToType?type=drums" class="img-pan">
            	<img src="/GuitarShop/img/drums-pan.webp" alt="GUITARS">
            		<span>Drums</span>
            	</a></td>
            <td>
            	<a href="${pageContext.request.contextPath}/product/redirectToType?type=keys" class="img-pan">
            		<img src="/GuitarShop/img/keys-pan.webp" alt="GUITARS">
            		<span>Keys</span>
           		</a>
           	</td>
        </tr>
        <tr>
            <td>
            	<a href="${pageContext.request.contextPath}/product/redirectToType?type=microphone" class="img-pan">
            		<img src="/GuitarShop/img/micro-pan.webp" alt="GUITARS">
            		<span>Microphones</span>
            	</a>
            </td>
            <td>
           		<a href="${pageContext.request.contextPath}/product/redirectToType?type=cable" class="img-pan">
            		<img src="/GuitarShop/img/cables-pan.webp" alt="GUITARS">
            		<span>Cables & Connectors</span>
            	</a>
            </td>
            <td>
            	<a href="${pageContext.request.contextPath}/product/redirectToType?type=soft" class="img-pan">
            		<img src="/GuitarShop/img/soft-pan.webp" alt="GUITARS">
            		<span>Software</span>
            	</a>
            </td>
        </tr>
    </table>
	<!--🎸 🥁 🎹 🎤 🔌 💻 -->
    <%@ include file="/pages/contacts.jsp" %>
</body>
</html>