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
	${userId}
	${product.prodId}
	<h1><a href="${pageContext.request.contextPath}/user/redirectToIndex">GuitarShop</a></h1>
	<h1><a href="${pageContext.request.contextPath}/cart/redirectToCart?userId=${userId}">Cart</a></h1>

	<c:if test="${!empty product}">
		<form action="${pageContext.request.contextPath}/cart/addItem?prodId=${product.prodId}&stock=${product.productStock}" method="post">
			<table>
				<tr>
					<td><img src="/GuitarShop/prod_img/${product.productTypeName}/prod_${product.prodId}/main.jpg"></td>
					<td>Product name:</td>
					<td>${product.productName}</td>
					<td><input type="submit" value="Add to cart"></td>
				</tr>
				<tr>
					<td></td>
					<td>Product description:</td>
					<td>${product.productDesc}</td>
				</tr>
				<tr>
					<td></td>
					<td>${product.productPrice}$</td>
					<td>Stock: ${product.productStock}</td>
				</tr>
			</table>
		</form>
	</c:if>
	<form action="postOverview" method="post">
			<p>Overview:</p>
			<textarea name="text" rows="4" cols="50" placeholder="Your thoughts on this product..."></textarea>
			<br>
			<input type="number" name="rating" value="5" min="1" max="5" step="1">
			<input type="hidden" name="userId" value="${userId}">
			<input type="hidden" name="prodId" value="${product.prodId}">
			<input type="submit" value="Post">
			<c:if test="${!empty overStatus}">${overStatus}</c:if>
	</form>
	
	<c:if test="${!empty overviews}">
		<h3>Overviews of our clients:</h3>
		<table>
			<c:forEach items="${overviews}" var="o">
				<tr>
					<td>
						User${o.id.userId}
					</td>
					<td>
						${o.rating}
					</td>
					<td>
						${o.text}
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
</body>
</html>