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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/products.css">
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
	
	<h2>${type}</h2>
	<c:if test="${!empty itemStatus}">
		<div class="added-cont">
			<span class="added-item">${itemStatus}</span>
		</div>
	</c:if>
		<table>
			<c:if test="${!empty products}">
				<c:forEach items="${products}" var="p">
					<tr>
						<th>
							<a href="${pageContext.request.contextPath}/product/redirectToProductPage?prodId=${p.prodId}">
								<img src="/GuitarShop/prod_img/${p.productTypeName}/prod_${p.prodId}/main.webp">
							</a>
						</th>
						<th colspan="5">
							<form action="${pageContext.request.contextPath}/cart/addItem?prodId=${p.prodId}&stock=${p.productStock}&type=${filter}" method="post">
							<div class="product-card">
								<div class="product-name">
									<a href="${pageContext.request.contextPath}/product/redirectToProductPage?prodId=${p.prodId}">${p.productName}</a>
								</div>
								<div class="product-desc">${p.productDesc}</div>
								<div class="product-meta">
									<span class="product-type">${p.productTypeName}</span>
									<span class="product-stock">📦 ${p.productStock}</span>
									<span class="product-price">${p.productPrice}$</span>
								</div>
								<div class="product-add">
									<c:if test="${!empty itemStatus && !empty addedProdId && addedProdId == p.prodId}">
										<div class="added-cont">
											<span class="added-item">${itemStatus}</span>
										</div>
									</c:if>
									<button type="submit" class="add-to-cart">Add to cart</button>
								</div>
							</div>
							</form>
						</th>
					</tr>
				</c:forEach>
			</c:if>
		</table>

	
	<%@ include file="/pages/contacts.jsp" %>
    
    <script>
	    (function() {
	        // Save current scroll position before page unload (reload, form submit, navigation)
	        window.addEventListener('beforeunload', function() {
	            sessionStorage.setItem('scrollPos', window.scrollY);
	        });
	
	        // Restore scroll position after page has fully loaded
	        window.addEventListener('load', function() {
	            var savedScroll = sessionStorage.getItem('scrollPos');
	            if (savedScroll !== null) {
	                window.scrollTo({
	                    top: parseInt(savedScroll, 10),
	                    behavior: 'auto'   // 'instant' or 'smooth'
	                });
	                // Optional: remove the saved position after restoring
	                sessionStorage.removeItem('scrollPos');
	            }
	        });
	    })();
	</script>
</body>
</html>