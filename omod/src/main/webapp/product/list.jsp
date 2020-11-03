<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../template/localHeader.jsp"%>

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').DataTable();
        });
    }
</script>

<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/product/edit.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Créer nouveau produit">
            <i class="fa fa-plus"></i>
        </button>
    </div>
</div>
<hr>

<table class="table table-striped table-sm">
    <thead>
    <tr>
        <th>Id</th>
        <th>
<%--            <spring:message code="pharmacy.name"/>--%>
            Code
        </th>
        <th>
<%--            <spring:message code="pharmacy.name"/>--%>
            Nom (detail)
        </th>
        <th>
<%--            <spring:message code="pharmacy.name"/>--%>
            Unite (detail)
        </th>
        <th>
<%--            <spring:message code="pharmacy.name"/>--%>
            Nom (Gros)
        </th>
        <th>
<%--            <spring:message code="pharmacy.name"/>--%>
            Unite (Gros)
        </th>
        <th>
<%--            <spring:message code="pharmacy.regimenProductNumber"/>--%>
            Unite de conversion
        </th>
        <th style="width: 30px"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="product" items="${ products }">
        <tr>
            <td>${product.productId}</td>
            <td>${product.code}</td>
            <td>${product.retailName}</td>
            <td>${product.productRetailUnit.name}</td>
            <td>${product.wholesaleName}</td>
            <td>${product.productWholesaleUnit.name}</td>
            <td>${product.unitConversion}</td>
            <td>
                <c:url value="/module/pharmacy/product/edit.form" var="editUrl">
                    <c:param name="id" value="${product.productId}"/>
                </c:url>
                <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>
            </td>
        </tr>
    </c:forEach>
    </tbody>

</table>

<%@ include file="../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
