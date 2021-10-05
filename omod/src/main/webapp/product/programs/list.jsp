<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>
<openmrs:require privilege="View Program" otherwise="/login.htm" redirect="/module/pharmacy/product/programs/list.form" />

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
<%--    <div class="col-4">--%>
<%--        <c:url value="/module/pharmacy/product/programs/location.form" var="locationUrl"/>--%>
<%--        <button class="btn btn-primary" onclick="window.location='${locationUrl}'" title="">--%>
<%--            <i class="fa fa-plus"></i> Gestion des programmes du centre--%>
<%--        </button>--%>
<%--    </div>--%>
    <div class="col-6 text-right">
        <openmrs:hasPrivilege privilege="Manage Program">
            <c:url value="/module/pharmacy/product/programs/edit.form" var="url"/>
            <button class="btn btn-primary" onclick="window.location='${url}'" title="Créer nouveau programme">
                <i class="fa fa-plus"></i>
            </button>
        </openmrs:hasPrivilege>

    </div>
</div>
<hr>

<table class="table table-striped table-sm">
    <thead>
    <tr>
        <th>Id</th>
        <th><spring:message code="pharmacy.name"/></th>
        <th><spring:message code="pharmacy.programProductNumber"/></th>
        <th style="width: 30px"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="program" items="${ programs }">
        <tr>
            <td>${program.productProgramId}</td>
            <td>${program.name}</td>
            <td>${fct:length(program.products)}</td>
            <td>
                <c:url value="/module/pharmacy/product/programs/edit.form" var="editUrl">
                    <c:param name="id" value="${program.productProgramId}"/>
                </c:url>
                <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>
                <openmrs:hasPrivilege privilege="Delete Program">
                    <c:url value="/module/pharmacy/product/programs/delete.form" var="deleteUrl">
                        <c:param name="id" value="${program.productProgramId}"/>
                    </c:url>
                    <a href="${deleteUrl}" onclick="return confirm('Voulez vous supprimer ce programme ?')"
                       class="text-danger"><i class="fa fa-trash"></i></a>
                </openmrs:hasPrivilege>
            </td>
        </tr>
    </c:forEach>
    </tbody>

</table>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
