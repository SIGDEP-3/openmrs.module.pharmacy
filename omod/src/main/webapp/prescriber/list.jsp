<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Unit" otherwise="/login.htm" redirect="/module/pharmacy/proescriber/list.form" />
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
        <c:url value="/module/pharmacy/prescriber/edit.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Créer nouveau prescripteur">
            <i class="fa fa-plus"></i>
        </button>
    </div>
</div>
<hr>

<table class="table table-striped table-sm">
    <thead>
    <tr>
        <th>Nom et pr&eacute;noms</th>
        <th>Identifiant</th>
        <th style="width: 30px"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="prescriber" items="${ prescribers }">
        <tr>
            <td>${prescriber.name}</td>
            <td>${prescriber.identifier}</td>
            <td>
                <c:url value="/module/pharmacy/prescriber/edit.form" var="editUrl">
                    <c:param name="id" value="${prescriber.providerId}"/>
                </c:url>
                <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>

                <c:url value="/module/pharmacy/prescribers/delete.form" var="deleteUrl">
                    <c:param name="id" value="${prescriber.providerId}"/>
                </c:url>
                <a href="${deleteUrl}" onclick="return confirm('Voulez vous supprimer ce prescripteur ?')"
                   class="text-danger"><i class="fa fa-trash"></i></a>
            </td>
        </tr>
    </c:forEach>
    </tbody>

</table>

<%@ include file="../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
