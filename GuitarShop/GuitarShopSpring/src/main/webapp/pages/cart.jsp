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
<c:if test="${!empty guestStatus}">${guestStatus}</c:if>
<h2>Cart:</h2>
<h2>Summary [${cartDTO.summ}$]</h2>
		<table>
			<tr>
				<td>Items:</td>
				<td></td>
				<td></td>
				<td></td>
				<td>
					<form action="purchaseCart" method="post">
						<c:forEach items="${items}" var="item" varStatus="status">
							<input type="hidden" name="cartIds" value="${item.id.cartId}" />
							<input type="hidden" name="productIds" value="${item.id.prodId}" />
							<input type="hidden" name="quantities" value="${item.quantity}" />
							<input type="hidden" name="products"
								value="${item.product.prodId}" />
						</c:forEach>
						<c:choose>
							<c:when test="${!empty items}"><input type="submit" value="Buy"></c:when>
							<c:otherwise><input type="button" value="Buy"></c:otherwise>
						</c:choose>
						
						
					</form>
				</td>
			</tr>
			<c:if test="${!empty items}">
			<c:forEach items="${items}" var="item">
					<tr>
						<td><img src="/GuitarShop/prod_img/${item.product.productTypeName}/prod_${item.product.prodId}/main.jpg"></td>
						<td><a href="${pageContext.request.contextPath}/product/redirectToProductPage?prodId=${item.product.prodId}">${item.product.productName}</a></td>
						<td>[${item.quantity * item.product.productPrice}$]</td>
						<td>
							<form action="changeQuantity" method="post">
								<select name="quantity">
										<c:forEach begin="1" end="${item.product.productStock}" var="i">
											<c:choose>
												<c:when test="${i == item.quantity}"><option selected value="${i}">${i}</option></c:when>
												<c:otherwise><option value="${i}">${i}</option></c:otherwise>
											</c:choose>
											
										</c:forEach>
								</select>
								<input type= "hidden" name="cartId" value="${item.id.cartId}">
								<input type= "hidden" name="productId" value="${item.id.prodId}">
								<input type="submit" formaction="changeQuantity" value="Change quanttity">
							</form>
						</td>
					</tr>
			</c:forEach>
		</c:if>
		</table>
		
</body>
</html>