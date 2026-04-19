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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/product.css">
</head>
<body>
	<div class="header">
		<h1>
			<a href="${pageContext.request.contextPath}/user/redirectToIndex">GuitarShop</a>
		</h1>
		<div class="header-right">
			<h1>
				<a class="cart-link"
					href="${pageContext.request.contextPath}/cart/redirectToCart">🛒Cart</a>
			</h1>
		</div>
	</div>
	
	<c:if test="${!empty itemStatus}">
		<div class="added-cont">
			<span class="added-item">${itemStatus}</span>
		</div>
	</c:if>

	<c:if test="${!empty product}">
		<form
			action="${pageContext.request.contextPath}/cart/addItem?prodId=${product.prodId}&stock=${product.productStock}"
			method="post" class="product-form">
			<div class="product-card">
				<c:if test="${!empty productImages}">
					<div class="product-image carousel">
						<%-- Радио-кнопки переключения --%>
						<c:forEach items="${productImages}" var="img" varStatus="status">
							<input type="radio" name="carousel" id="slide${status.index + 1}"
								${status.first ? 'checked' : ''}>
						</c:forEach>

						<%-- Контейнер слайдов --%>
						<div class="slides">
							<c:forEach items="${productImages}" var="img" varStatus="status">
								<div class="slide">
									<img src="/GuitarShop/prod_img/${product.productTypeName}/prod_${product.prodId}/${img}" alt="${product.productName}">
								</div>
							</c:forEach>
						</div>

						<%-- Навигационные точки --%>
						<div class="carousel-nav">
							<c:forEach items="${productImages}" var="img" varStatus="status">
								<label for="slide${status.index + 1}"></label>
							</c:forEach>
						</div>
					</div>
				</c:if>
				<div class="product-info">
					<div class="product-row">
						<!--<span class="label">Product name:</span>-->
						<span class="value product-name">${product.productName}</span>
						<button type="submit" class="add-to-cart">Add to cart</button>
					</div>
					<div class="product-row">
						<!--<span class="label">Product description:</span>-->
						<span class="value">${product.productDesc}</span>
					</div>
					<div class="product-row">
						<span class="label price-label">${product.productPrice}$</span>
						<span class="value stock">Stock: ${product.productStock}</span>
					</div>
				</div>
			</div>
		</form>
	</c:if>
	<form action="postOverview" method="post">
			<p>Overview:</p>
			<textarea name="text" rows="4" cols="50" placeholder="Your thoughts on this product..."></textarea>
			<br>
			Rating
			<input type="number" name="rating" value="5" min="1" max="5" step="1">
			<input type="hidden" name="userId" value="${userId}">
			<input type="hidden" name="prodId" value="${product.prodId}">
			<input type="submit" value="Post">
			<c:if test="${!empty overStatus}"><span class="status-message">${overStatus}</span></c:if>
	</form>
	
	<c:if test="${!empty overviews}">
		<h3>Overviews of our clients:</h3>
		<c:if test="${!empty chatStatus}"><span class="status-message">${chatStatus}</span></c:if>
		<table>
			<c:forEach items="${overviews}" var="o">
				<tr>
					<td>
						${o.userName}
					</td>
					<td>
						${o.rating}
					</td>
					<td>
						${o.text}
					</td>
					<td>
						<c:if test="${!empty userType && userType != 'guest'}">
							<a href="${pageContext.request.contextPath}/chat/createChat?user2Username=${o.userName}&prodId=${product.prodId}">Ask user a question(Chat)</a>
						</c:if>
						<c:if test="${empty userType || userType == 'guest'}">
							<a href="${pageContext.request.contextPath}/user/redirectToAccount">To chat with users please log in</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	
	<%@ include file="/pages/contacts.jsp" %>
</body>
</html>