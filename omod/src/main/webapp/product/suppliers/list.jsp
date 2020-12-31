<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>
<openmrs:require privilege="View Supplier" otherwise="/login.htm" redirect="/module/pharmacy/product/suppliers/list.form" />

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
        <c:url value="/module/pharmacy/product/suppliers/edit.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Créer nouveau fournisseur">
            <i class="fa fa-plus"></i>
        </button>
    </div>
</div>
<hr>

<table class="table table-striped table-sm">
    <thead>
    <tr>
        <th>Id</th>
        <th><spring:message code="pharmacy.name"/></th>
        <th><spring:message code="pharmacy.phoneNumber"/></th>
        <th><spring:message code="pharmacy.email"/></th>
        <th style="width: 30px"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="supplier" items="${ suppliers }">
        <tr>
            <td>${supplier.productSupplierId}</td>
            <td>${supplier.name}</td>
            <td>${supplier.phoneNumber}</td>
            <td>${supplier.email}</td>
            <td>
                <c:url value="/module/pharmacy/product/suppliers/edit.form" var="editUrl">
                    <c:param name="id" value="${supplier.productSupplierId}"/>
                </c:url>
                <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>
                <openmrs:hasPrivilege privilege="Delete Supplier">
                    <c:url value="/module/pharmacy/product/suppliers/delete.form" var="deleteUrl">
                        <c:param name="id" value="${supplier.productSupplierId}"/>
                    </c:url>
                    <a href="${deleteUrl}" onclick="return confirm('Voulez vous supprimer ce regime ?')"
                       class="text-danger"><i class="fa fa-trash"></i></a>
                </openmrs:hasPrivilege>
            </td>
        </tr>
    </c:forEach>
    </tbody>

</table>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
