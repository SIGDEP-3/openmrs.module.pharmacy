<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../template/localHeader.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacy/product/units/list.form" />

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
    <div class="col-5">
        <form method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/module/pharmacy/product/upload.form">
            <div class="row">
                <div class="col-7">
                    <div class="custom-file">
                        <input type="file" class="custom-file-input" id="customFile" name="file">
                        <label class="custom-file-label" for="customFile">Choisir le fichier CSV</label>
                    </div>
                </div>
                <div class="col-4">
                    <button class="btn btn-success"> <i class="fa fa-upload"></i> Importer</button>
                </div>
            </div>
        </form>
    </div>
    <div class="col-1 text-right">
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
            D&eacute;signation
        </th>
        <th>
<%--            <spring:message code="pharmacy.name"/>--%>
            Unit&eacute;
        </th>
        <th>
<%--            <spring:message code="pharmacy.name"/>--%>
            D&eacute;signation (Conditionnement)
        </th>
        <th>
<%--            <spring:message code="pharmacy.name"/>--%>
            Unit&eacute; (Conditionnement)
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
