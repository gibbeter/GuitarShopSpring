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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/modify_product.css">
</head>
<body>
	<div class="header">
		<h1>
			<a href="${pageContext.request.contextPath}/user/redirectToIndex">GuitarShop</a>
		</h1>
		<div class="header-right">
			<h1>
				<c:if test="${!empty userId && userType != 'guest'}">
					<a href="${pageContext.request.contextPath}/product/redirectToStorage">Storage</a>
				</c:if>
			</h1>
		</div>
	</div>

	<c:if test="${!empty modStatus}">
		<div class="mod-conteiner">
			<span class="modStatus" data-content="${modStatus}">${modStatus}</span>
		</div>
	</c:if>
	
	<c:if test="${!empty product}">
		<div class="upload-section">
		    <h3>Upload Product Images</h3>
		    <p>Name of the files (.webp):</p>
		    <div class="table-wrapper">
		    <table>
		    	<tr>
			    	<th>main</th>
			    	<th>back</th>
			    	<th>side_l</th>
			    	<th>side_r</th>
			    	<th>additional</th>
		    	</tr>
		    	<tr>
	    			<td><img src="/GuitarShop/prod_img/${product.productTypeName}/prod_${product.prodId}/main.webp" alt="${product.productName}"></td>
	    			<td><img src="/GuitarShop/prod_img/${product.productTypeName}/prod_${product.prodId}/back.webp" alt="${product.productName}"></td>
	    			<td><img src="/GuitarShop/prod_img/${product.productTypeName}/prod_${product.prodId}/side_l.webp" alt="${product.productName}"></td>
	    			<td><img src="/GuitarShop/prod_img/${product.productTypeName}/prod_${product.prodId}/side_r.webp" alt="${product.productName}"></td>
	    			<td><img src="/GuitarShop/prod_img/${product.productTypeName}/prod_${product.prodId}/additional.webp" alt="${product.productName}"></td>
		    	</tr>
		    </table>
		    </div>
		    
		    <form action="${pageContext.request.contextPath}/uploadImages" method="post" enctype="multipart/form-data">
		        <input type="file" name="webpFiles" accept="image/webp" multiple required />
		        <input type="hidden" name="prodId" value="${product.prodId}"/>
		        <input type="hidden" name="prodType" value="${product.productTypeName}"/>
		        <input type="submit" value="Upload" />
		    </form>
		    <c:if test="${!empty uploadState}">
		        <p class="upload-stat">${uploadState}</p>
		    </c:if>
		</div>
	</c:if>
	
	<form action="dummy" method="post">
	<c:if test="${!empty product}">
		<div class="form-conteiner">
			<input type="hidden" name ="prodId" value="${product.prodId}">
			<div class="field-row">
				<span class="tag">Name</span><span class="meta"><textarea name="productName" class="auto-resize">${product.productName}</textarea></span>
			</div>
			<div class="field-row">
				<span class="tag">Description</span><span class="meta"><textarea name="productDesc" class="auto-resize">${product.productDesc}</textarea></span>
			</div>
			<div class="fields-grid">
				<span class="tag">TypeId</span><span class="meta"><input type="number" name="productType" value="${product.productType}"></span>
				<span class="tag">TypeName</span><span class="meta"><input type="text" name="productTypeName" value="${product.productTypeName}"></span>
				<span class="tag">Stock</span><span class="meta"><input type="number" name="productStock" value="${product.productStock}"></span>
				<span class="tag">Price</span><span class="meta"><input type="number" name="productPrice" value="${product.productPrice}"></span>
			</div>
		</div>
		<div class="buttons">
			<button class="modify" formaction="modifyProductData" type="submit">Modify</button>
			<button class="delete" formaction="deleteProductData" type="submit">Delete</button>
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