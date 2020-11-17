<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fct" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="includeStyle.jsp"%>

<script type="application/javascript" >
	if (jQuery) {
		jQuery(document).ready(function (){
			jQuery('.s2').select2();
		});
	}
</script>

<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin">
			<spring:message code="admin.title.short" />
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/product/") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/pharmacy/product/list.form">
			<spring:message code="pharmacy.productManagement" />
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/programs") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/pharmacy/product/programs/list.form">
			<spring:message code="pharmacy.programManagement" />
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/regimens") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/pharmacy/product/regimens/list.form">
			<spring:message code="pharmacy.regimenManagement" />
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/prices") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/pharmacy/product/prices/list.form">
			<spring:message code="pharmacy.priceManagement" />
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/units") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/pharmacy/product/units/list.form">
			<spring:message code="pharmacy.unitManagement" />
		</a>
	</li>
	<li <c:if test='<%= request.getRequestURI().contains("/suppliers") %>'>class="active"</c:if>>
		<a href="${pageContext.request.contextPath}/module/pharmacy/product/suppliers/list.form">
			<spring:message code="pharmacy.supplierManagement" />
		</a>
	</li>
	
	<!-- Add further links here -->
</ul>
<h2>
	<spring:message code="pharmacy.title" />
</h2>
