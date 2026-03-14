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
	<h1><a href="${pageContext.request.contextPath}/cart/redirectToCart?userId=${userId}">Cart</a></h1>

	<c:if test="${!empty product}">
		<form action="${pageContext.request.contextPath}/cart/addItem?prodId=${product.prodId}&stock=${product.productStock}" method="post">
			<table>
				<tr>
					<td><img src="/GuitarShop/prod_img/${product.productTypeName}/prod_${product.prodId}/main.jpg"></td>
					<td>${product.productName}</td>
				</tr>
				<tr>
					<td></td>
					<td>${product.productDesc}</td>
					<td>Stock: ${product.productStock}</td>
				</tr>
				<tr>
					<td></td>	
					<td>
						<input type="submit" value="Add to cart">
					</td>	
				</tr>
			</table>
		</form>
	</c:if>

</body>
</html>