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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/modify_order.css">
</head>
<body>
	<div class="header">
		<h1>
			<a href="${pageContext.request.contextPath}/user/redirectToIndex">GuitarShop</a>
		</h1>
		<div class="header-right">
			<h1>
				<c:if test="${!empty userId && userType != 'guest'}">
					<a href="${pageContext.request.contextPath}/order/redirectToOrders">Orders</a>
				</c:if>
			</h1>
		</div>
	</div>

	<c:if test="${!empty modStatus}">
		<div class="mod-conteiner">
			<span class="modStatus" data-content="${modStatus}">${modStatus}</span>
		</div>
	</c:if>
	<c:if test="${!empty validMessage}">
		<div class="mod-conteiner">
			<span class="validMessage" data-content="${validMessage}">${validMessage}</span>
		</div>
	</c:if>
	
	
	<form action="dummy" method="post">
	<c:if test="${!empty order}">
		<div class="form-conteiner">
			<input type="hidden" name ="orderId" value="${order.orderId}">
			<div class="fields-grid">
				<div class="field-pair">
					<span class="tag">User ID</span><span class="meta"><input type="number" name="userId" value="${order.userId}" disabled></span>
					<input type="hidden" name="userId" value="${order.userId}">
				</div>
				<div class="field-pair">
					<span class="tag">Name</span><span class="meta"><input type="text" name="name" value="${order.name}"></span>
				</div>
				<div class="field-pair">
					<span class="tag">Surname</span><span class="meta"><input type="text" name="surname" value="${order.surname}"></span>
				</div>
				<div class="field-pair">
					<span class="tag">Phone number</span><span class="meta"><input type="number" name="phoneNumber" value="${order.phoneNumber}"></span>
				</div>
				<div class="field-pair">
					<span class="tag">Order Date</span><span class="meta"><input type="datetime-local" name="orderDate" value="${order.orderDate}" disabled></span>
					<input type="datetime-local" name="orderDate" value="${order.orderDate}" hidden="">
				</div>
				<div class="field-pair">
					<span class="tag">OrderStatus</span><span class="meta">
						<select class="selection" name="orderStatus">
							<option selected hidden="">${order.orderStatus}</option>
							<option value="NEW">NEW</option>
							<option value="SHIPPING">SHIPPING</option>
							<option value="COMPLETE">COMPLETE</option>
						</select>
					</span>
				</div>
				<div class="field-pair">
					<span class="tag">Price</span>
					<span class="meta"><input type="number" name="summ" value="${order.summ}"></span>
				</div>
				<div class="field-pair">
					<span class="tag">Completion Date (estimated)</span>
					<span class="meta"><input type="datetime-local" name="completionTime" value="${order.completionTime}"></span>
				</div>
				<div class="field-pair">
					<span class="tag">Order type</span>
					<span class="meta">
						<select class="selection" name="orderType">
							<option selected hidden="">${order.orderType}</option>
							<option value="SP">Shipping (DHL)</option>
							<option value="PU">Pick-Up</option>
						</select>
					</span>
				</div>
				<div class="field-pair">
					<span class="tag">Pick-Up Adress</span>
					<span class="meta"><textarea name="pickupAdress" class="auto-resize">${order.pickupAdress}</textarea></span>
				</div>
				<div class="field-pair">
					<span class="tag">Shipping Adress</span>
					<span class="meta"><textarea name="shippingAdress" class="auto-resize">${order.shippingAdress}</textarea></span>
				</div>
			</div>
			<div class="field-row">
				
			</div>
			<div class="field-row">
				
			</div>
		</div>
		<div class="buttons">
			<button class="modify" formaction="modifyOrderData" type="submit">Modify</button>
			<button class="delete" formaction="deleteOrderData" type="submit">Delete</button>
		</div>
		</c:if>
	</form>


	<script>
		function autoResize(textarea) {
		    textarea.style.height = 'auto';          
		    textarea.style.height = textarea.scrollHeight + 'px';
		}
	
		document.querySelectorAll('textarea.auto-resize').forEach(textarea => {
		    autoResize(textarea);
		    textarea.addEventListener('input', function() { autoResize(this); });
		});
	</script>
</body>
</html>