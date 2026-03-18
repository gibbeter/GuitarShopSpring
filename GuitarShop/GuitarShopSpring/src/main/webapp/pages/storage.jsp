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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/storage.css">
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
		
	<form action="addProduct" method="post">
	<div>
		<input type="submit" formaction="filterStoreByType" value="Filter">
	</div>
	
	<select name="filterType">
		<option>All</option>
		<c:forEach items="${types}" var="t">
			<option value="${t.typeName}">${t.typeName}</option>
		</c:forEach>
	</select>
		
	<c:if test="${!empty filter}">Products filtered by: ${filter}</c:if>
		
	<table>
		
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Description</th>
			<th>Type</th>
			<th>Stock</th>
			<th>Price</th>
			<th></th>
		</tr>
		
		<tr>
			<td></td>
			<td><input type="text" name="productName" value="Name"></td>
			<td><input type="text" name="productDesc" value="Description"></td>
			<td>
				<input type="text" name="typeName" value="New or existing type">
				<select name="selectedType">
					<option selected disabled hidden="">Types</option>
					<c:forEach items="${types}" var="t">
						<option disabled value="${t.typeId}">${t.typeName}</option>
					</c:forEach>
				</select>
			
			</td>
			<td><input type="number" name="productStock" value="0"></td>
			<td><input type="number" name="productPrice" value="0"></td>
			<td><input type="submit" value="Add"></td>
		
		</tr>
		<c:if test="${!empty products}">
			<c:forEach items="${products}" var="p">
				<tr>
					<th>${p.prodId}</th>
					<td><a href="${pageContext.request.contextPath}/product/redirectToProductPage?prodId=${p.prodId}">${p.productName}</a></td>
					<td class="prod-desc" title="${p.productDesc}">${p.productDesc}</td>
					<td>${p.productType} | ${p.productTypeName}</td>
					<td>${p.productStock}</td>
					<td>${p.productPrice}</td>
					<td>
						<a class="modify-link" href="${pageContext.request.contextPath}/product/redirectToModifyProductPage?prodId=${p.prodId}">Modify</a>
					</td>
				</tr>
			</c:forEach>
		</c:if>
		</table>
	</form>
</body>
</html>