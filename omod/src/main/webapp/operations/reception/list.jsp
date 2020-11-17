<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').DataTable();
        });
    }
</script>
<div class="container-fluid mt-1">

    <div class="row">
        <div class="col-6">
            <h4>${title}</h4>
        </div>
        <div class="col-6 text-right">
            <c:url value="/module/pharmacy/operations/reception/edit.form" var="url"/>
            <button class="btn btn-primary" onclick="window.location='${url}'" title="Créer nouveau produit">
                <i class="fa fa-plus"></i>
            </button>
        </div>
    </div>
    <hr>

    <table class="table table-striped table-sm">
        <thead>
        <tr>
            <th>
                <%--            <spring:message code="pharmacy.supplier"/>--%>
                Fournisseur
            </th>
            <th>
                <%--            <spring:message code="pharmacy.receptionDate"/>--%>
                Date de reception
            </th>
            <th>
                <%--            <spring:message code="pharmacy.receptionCode"/>--%>
                BL
            </th>
            <th>
                <%--            <spring:message code="pharmacy.program"/>--%>
                Programme
            </th>
            <th>
                <%--            <spring:message code="pharmacy.program"/>--%>
                Type de saisie
            </th>
            <th>
                <%--            <spring:message code="pharmacy.status"/>--%>
                Etat
            </th>
            <th style="width: 30px"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="reception" items="${ receptions }">
            <tr>
                <td>${reception.productSupplier.name}</td>
                <td>${reception.operationDate}</td>
                <td>${reception.operationNumber}</td>
                <td>${reception.productProgram.name}</td>
                <td>${reception.receptionQuantityMode}</td>
                <td>${reception.operationStatus}</td>
                <td>
                    <c:url value="/module/pharmacy/operations/reception/edit.form" var="editUrl">
                        <c:param name="id" value="${reception.productOperationId}"/>
                    </c:url>
                    <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
</div>
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
