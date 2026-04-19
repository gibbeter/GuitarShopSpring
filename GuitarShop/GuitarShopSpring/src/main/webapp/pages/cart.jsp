<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main_padding.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/cart.css">
	<script>
		window.addEventListener('pageshow', function(event) {
			if (event.persisted) {
				window.location.reload();
			}
		});
	</script>
</head>
<body>
    <h1><a href="${pageContext.request.contextPath}/user/redirectToIndex">GuitarShop</a></h1>

    <c:if test="${!empty guestStatus}">
        <div class="guest-status">${guestStatus}</div>
    </c:if>
    <div class="cart-header">
        <h2>Cart</h2>
        <div class="cart-summary">${cartDTO.summ}$</div>
    </div>
    
    <c:if test="${!empty cartErrorStatus}">
		<div class="error-container">
			<span class="errorStatus">${cartErrorStatus}</span>
		</div>
	</c:if>

    <sf:form action="purchaseCart" method="post" class="purchase-form" modelAttribute="pformDTO">
    	<div class="form-header">
	        <c:forEach items="${items}" var="item">
	            <input type="hidden" name="cartIds" value="${item.id.cartId}">
	            <input type="hidden" name="productIds" value="${item.id.prodId}">
	            <input type="hidden" name="quantities" value="${item.quantity}">
	            <input type="hidden" name="products" value="${item.product.prodId}">
	        </c:forEach>
	        <c:choose>
	            <c:when test="${!empty items}">
	                <button type="submit" class="buy-button">Buy</button>
	            </c:when>
	            <c:otherwise>
	                <button type="button" class="buy-button disabled" disabled>Buy</button>
	            </c:otherwise>
	        </c:choose>
        </div>
        <c:if test="${!empty items}">
	        <div class="credits-container">
	        	<span class="tag">Name</span>
	        		<span class="meta">
	        			<input type="text" name="userName" value="${userNameTemp}">
	       			</span>
	        	<span class="tag">Surname</span>
	        		<span class="meta">
	        			<input type="text" name="userSurname" value="${userSurnameTemp}">
	       			</span>
	        	<span class="tag">Phone</span>
	        		<span class="meta">
	        			<input type="text" name="userPhone" value="${userPhoneTemp}">
	       			</span>
	        	<span class="tag">Shipping method</span><span class="meta">
	        		<select name="shippingType" id="shippingMethod">
	        			<option selected hidden="" value="${shippingTypeTemp}">${shippingTypeTemp}</option>
	        			<option value="Pick-Up">Pick-Up</option>
	        			<option value="Shipping (DHL)">Shipping (DHL)</option>
	        		</select>
	       		</span>
	       		<span class="tag">Shipping Adress</span>
	       			<span class="hint">FORMAT: CITY, STREET, NUMBER, APARTMENT</span>
	       				<span class="meta" title="FORMAT">
	       					<input class="shippingAddress" id="shippingAddress" type="text" name="SPAdress" value="${SPAdressTemp}"
	       					pattern="^[A-Za-z '\.\-]+,[ ]*[A-Za-z '\.\-]+,[ ]*[A-Za-z0-9\-]+,[ ]*[A-Za-z0-9\-]+$"
	       					title="FORMAT: CITY, STREET, NUMBER, APARTMENT"
	       					placeholder="FORMAT: CITY, STREET, NUMBER, APARTMENT"
	       					required/>
	    				</span>
	       		<span class="tag">Pick-Up Adress</span><span class="meta">
	       		${PUAdressTemp}Test
	        		<select name="PUAdress" id="pickUpAddress">
	        			<c:forEach items="${pickAdresses}" var="a">
	        				<option selected hidden="" value="${PUAdressTemp}">${PUAdressTemp}</option>
	        				<option value="${a.storeAdress}">${a.storeAdress}</option>
	        			</c:forEach>
	        		</select>
	       		</span>
	        </div>
        </c:if>
    </sf:form>


    <c:if test="${!empty items}">
        <div class="cart-items">
            <c:forEach items="${items}" var="item">
                <div class="cart-item">
                    <div class="item-image">
                        <img src="/GuitarShop/prod_img/${item.product.productTypeName}/prod_${item.product.prodId}/main.webp" alt="${item.product.productName}">
                    </div>

                    <div class="item-info">
                        <a href="${pageContext.request.contextPath}/product/redirectToProductPage?prodId=${item.product.prodId}"
                           class="item-name">${item.product.productName}</a>
                        <div class="item-total">${item.quantity * item.product.productPrice}$</div>
                    </div>

                    <form action="changeQuantity" method="post" class="quantity-form">
                        <select name="quantity">
                            <c:forEach begin="1" end="${item.product.productStock}" var="i">
                                <option value="${i}" ${i == item.quantity ? 'selected' : ''}>${i}</option>
                            </c:forEach>
                        </select>
                        <input type="hidden" name="cartId" value="${item.id.cartId}">
                        <input type="hidden" name="productId" value="${item.id.prodId}">
                        <button type="submit" formaction="changeQuantity" class="change-btn">Change quantity</button>
                    </form>
                    
                    <form action="deleteItem" method="post" class="delete-form">
                    	<input type="hidden" name="cartId" value="${item.id.cartId}">
                        <input type="hidden" name="productId" value="${item.id.prodId}">
                        <button type="submit" formaction="deleteItem" class="delete-btn">Delete</button>
                    </form>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${empty items}">
        <p class="empty-cart">Your cart is empty</p>
    </c:if>
    
    <%@ include file="/pages/contacts.jsp" %>
    
    <script>
		document.addEventListener('DOMContentLoaded', function() {
		    const shippingMethod = document.getElementById('shippingMethod');
		    const shippingAddress = document.getElementById('shippingAddress');
		    const pickUpAddress = document.getElementById('pickUpAddress');
		    
		    function updateFields() {
		        if (shippingMethod.value === 'Shipping (DHL)') { // Shipping
		            // Enable shipping, disable pick-up
		            shippingAddress.disabled = false;
		            shippingAddress.required = true;
		            pickUpAddress.disabled = true;
		            pickUpAddress.required = false;
		            
		            // Visual feedback
		            shippingAddress.style.opacity = '1';
		            shippingAddress.style.backgroundColor = '#ffffff';
		            pickUpAddress.style.opacity = '0.6';
		            pickUpAddress.style.backgroundColor = '#f1f5f9';
		        } else { // Pick-up
		            // Enable pick-up, disable shipping
		            shippingAddress.disabled = true;
		            shippingAddress.required = false;
		            pickUpAddress.disabled = false;
		            pickUpAddress.required = true;
		            
		            // Visual feedback
		            shippingAddress.style.opacity = '0.6';
		            shippingAddress.style.backgroundColor = '#f1f5f9';
		            pickUpAddress.style.opacity = '1';
		            pickUpAddress.style.backgroundColor = '#ffffff';
		        }
		    }
		    
		    shippingMethod.addEventListener('change', updateFields);
		    updateFields(); // Initialize
		});
	</script>
    
</body>
</html>