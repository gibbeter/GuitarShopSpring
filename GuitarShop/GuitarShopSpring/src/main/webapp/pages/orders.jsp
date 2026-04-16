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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/orders.css">
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
		<div class="del-container">
			<span class="delStatus" data-content="${delStatus}">${delStatus}</span>
		</div>
	</c:if>
	
	<c:if test="${!empty modStatus}">
		<div class="mod-conteiner">
			<span class="modStatus" data-content="${modStatus}">${modStatus}</span>
		</div>
	</c:if>
		
	<form action="filterOrdersByStatus">
		<div>
			<input type="submit" value="Filter">
		</div>
	
	
	<select name="filterStatus">
		<option selected hidden="" value="${filter}">${filter}</option>
		<option value="ALL">All</option>
		<option value="NEW">NEW</option>
		<option value="SHIPPING">SHIPPING</option>
		<option value="COMPLETE">COMPLETE</option>
	</select>
	</form>
		
	<table>
		<tr>
			<th>ID</th>
			<th>Status</th>
			<th>User ID</th>
			<th>Name</th>
			<th>Surname</th>
			<th>Order date</th>
			<th>Order type</th>
			<th>Pickup adress</th>
			<th>Shipping adress</th>
			<th>Completion date</th>
			<th>Price</th>
			<th></th>
		</tr>

		<c:if test="${!empty orders}">
			<c:forEach items="${orders}" var="o">
				<tr>
					<td><b>${o.orderId}</b></td>
					<td class="status" title="${o.orderStatus}">${o.orderStatus}</td>
					<td>${o.userId}</td>
					<td>${o.name}</td>
					<td>${o.surname}</td>
					<td>${o.orderDate}</td>
					<td>${o.orderType}</td>
					<td>${o.pickupAdress}</td>
					<td>${o.shippingAdress}</td>
					<td>${o.completionTime}</td>
					<td>${o.summ}</td>
					<td>
						<a class="modify-link" href="${pageContext.request.contextPath}/order/redirectToModifyOrderPage?orderId=${o.orderId}">Modify</a>
					</td>
				</tr>
			</c:forEach>
		</c:if>
		</table>
</body>
</html>