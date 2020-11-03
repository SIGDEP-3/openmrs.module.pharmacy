<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>

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
        <c:url value="/module/pharmacy/product/units/edit.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Créer nouvelle unité">
            <i class="fa fa-plus"></i>
        </button>
    </div>
</div>
<hr>

<table class="table table-striped table-sm">
    <thead>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th style="width: 30px"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="unit" items="${ units }">
        <tr>
            <td>${unit.productUnitId}</td>
            <td>${unit.name}</td>
            <td>
                <c:url value="/module/pharmacy/product/units/edit.form" var="editUrl">
                    <c:param name="id" value="${unit.productUnitId}"/>
                </c:url>
                <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>
                <c:url value="/module/pharmacy/product/units/delete.form" var="deleteUrl">
                    <c:param name="id" value="${unit.productUnitId}"/>
                </c:url>
                <a href="${deleteUrl}" onclick="return confirm('Voulez vous supprimer cette unité ?')" class="text-danger"><i class="fa fa-trash"></i></a>
            </td>
        </tr>
    </c:forEach>
    </tbody>

</table>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
