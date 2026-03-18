<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

    <form action="purchaseCart" method="post" class="purchase-form">
        <c:forEach items="${items}" var="item">
            <input type="hidden" name="cartIds" value="${item.id.cartId}" />
            <input type="hidden" name="productIds" value="${item.id.prodId}" />
            <input type="hidden" name="quantities" value="${item.quantity}" />
            <input type="hidden" name="products" value="${item.product.prodId}" />
        </c:forEach>
        <c:choose>
            <c:when test="${!empty items}">
                <button type="submit" class="buy-button">Buy</button>
            </c:when>
            <c:otherwise>
                <button type="button" class="buy-button disabled" disabled>Buy</button>
            </c:otherwise>
        </c:choose>
    </form>


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