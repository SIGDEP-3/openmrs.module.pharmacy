<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<openmrs:require privilege="View Report" otherwise="/login.htm" redirect="/module/pharmacy/operations/distribution/list.form" />

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').DataTable();

            jQuery("#program").change(function () {
                if (jQuery(this).val()){
                    jQuery('#selectMe').text('');
                } else {
                    jQuery('#selectMe').html('<i class="fa fa-hand-point-right fa-2x text-danger"></i>');
                }
            })
        });

        function create() {
            const selection = jQuery("#program");
            const programId = selection.val();
            if (programId === undefined || programId === null || programId === '') {
                jQuery('#selectMe').html('<i class="fa fa-hand-point-right fa-2x text-danger"></i>');
            } else {
                location.href = "${pageContext.request.contextPath}/module/pharmacy/operations/distribution/edit.form?programId="+ programId
            }
        }
    }
</script>
<div class="container-fluid mt-2">

    <div class="row mb-2">
        <div class="col-4 text-uppercase font-italic text-secondary">
            <div class="h6"><i class="fa fa-list"></i> ${subTitle}</div>
        </div>
        <div class="col-8 text-right">
            <span id="selectMe"></span>
            <label for="program">Programme : </label>
            <select name="program" class="s2 form-control-sm mr-3" id="program">
                <option value=""></option>
                <c:forEach var="program" items="${programs}">
                    <option value="${program.productProgramId}" class="text-left">${program.name}</option>
                </c:forEach>
            </select>
<%--            <c:url value="/module/pharmacy/operations/inventory/edit.form" var="url"/>--%>
            <button class="btn btn-primary btn-sm" onclick="create()" title="Créer nouveau">
                <i class="fa fa-plus"></i> Nouveau
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <div class="row">
                <div class="col-7">
                    <div class="h6 text-info font-italic">Liste des distributions du mois</div>
                    <div class="card">
                        <div class="card-body">
                            <table class="table table-striped table-sm">
                                <thead>
                                <tr>
                                    <th>
                                        <%--            <spring:message code="pharmacy.inventoryDate"/>--%>
                                        Date de l'operation
                                    </th>
                                    <th>
                                        P&eacute;riode
                                    </th>
                                    <th>
                                        <%--            <spring:message code="pharmacy.program"/>--%>
                                        Programme
                                    </th>
                                    <%--                    <th>Type d'inventaire</th>--%>
                                    <th>Nombre de produits</th>
                                    <th>
                                        <%--            <spring:message code="pharmacy.status"/>--%>
                                        Etat
                                    </th>
                                    <th style="width: 30px"></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="report" items="${ reports }">
                                    <tr>
                                        <td><fmt:formatDate value="${report.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                        <td>${report.reportPeriod}</td>
                                        <td>${report.productProgram.name}</td>
                                            <%--                        <td>${inventory.inventoryType == 'FULL' ? 'COMPLET' : 'PARTIEL'}</td>--%>
                                        <c:choose>
                                            <c:when test="${fct:length(report.productAttributeFluxes) == 0}">
                                                <c:url value="/module/pharmacy/operations/distribution/editFlux.form" var="addLineUrl">
                                                    <c:param name="inventoryId" value="${report.productOperationId}"/>
                                                </c:url>
                                                <td class="text-danger">
                                                    <a href="${addLineUrl}">Ajouter des produits</a>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="text-center">
                                                        ${fct:length(report.productAttributeFluxes)}
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                        <td>${report.operationStatus == 'NOT_COMPLETED' ? 'EN COURS DE SAISIE' : (report.operationStatus == 'VALIDATED' ? 'VALIDE' : 'EN ATTENTE DE VALIDATION')}</td>
                                        <td>
                                            <c:url value="/module/pharmacy/operations/distribution/edit.form" var="editUrl">
                                                <c:param name="id" value="${report.productOperationId}"/>
                                            </c:url>
                                            <a href="${editUrl}" class="text-${report.operationStatus == 'VALIDATED' ? 'info': 'primary'}">
                                                <i class="fa fa-${report.operationStatus == 'VALIDATED' ? 'eye': 'edit'}"></i>
                                            </a>
                                            <openmrs:hasPrivilege privilege="Delete inventory">
                                                <c:if test="${report.operationStatus != 'VALIDATED'}">
                                                    <c:url value="/module/pharmacy/operations/distribution/delete.form" var="delUrl">
                                                        <c:param name="id" value="${report.productOperationId}"/>
                                                    </c:url>
                                                    <a href="${delUrl}"
                                                       onclick="return confirm('Vous etes sur le point de supprimer l\'inventaire, Voulez-vous continuer ?')"
                                                       class="text-danger">
                                                        <i class="fa fa-trash"></i>
                                                    </a>
                                                </c:if>
                                            </openmrs:hasPrivilege>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>

                            </table>
                        </div>
                    </div>
                </div>
                <div class="col-5">
                    <div class="row mb-2">
                        <div class="col-12">
                            <div class="h6 text-info font-italic">Rapports soumis non trait&eacute;s</div>
                            <div class="card">
                                <div class="card-body">
                                    <div class="h5 text-center text-success">Aucune soumission non trait&eacute;e</div>
<%--                                    <table class="table table-striped table-sm">--%>
<%--                                        <thead>--%>
<%--                                        <tr>--%>
<%--                                            <th>Site / PPS</th>--%>
<%--                                            <th>P&eacute;riode</th>--%>
<%--                                        </tr>--%>
<%--                                        </thead>--%>
<%--                                        <tbody>--%>
<%--                                        </tbody>--%>
<%--                                    </table>--%>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-2">
                        <div class="col-12">
                            <div class="h6 text-info font-italic">&nbsp;</div>
                            <div class="row">
                                <div class="col-8">
                                    <div class="row">
                                        <div class="col-12">
                                            <div class="card">
                                                <div class="card-body p-1">
                                                    <div class="row">
                                                        <div class="col">
                                                            <div class="text-center text-secondary border-bottom border-secondary">Total</div>
                                                            <div class="h1 text-center text-secondary font-weight-bold">${totalReportsToSubmit}</div>
                                                        </div>
                                                        <div class="col">
                                                            <div class="text-center text-warning border-bottom border-warning">Soumis</div>
                                                            <div class="h1 text-center text-warning font-weight-bold">${fct:length(submittedReports)}</div>
                                                        </div>
                                                        <div class="col">
                                                            <div class="text-center text-success border-bottom border-success">Trait&eacute;s</div>
                                                            <div class="h1 text-center text-success font-weight-bold">0</div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">
                                    <div class="card">
                                        <div class="card-body p-1">
                                            <div class="row">
                                                <div class="col">
                                                    <div class="text-center text-info border-bottom border-info">Compl&eacute;tude</div>
                                                    <div class="h1 text-center text-info font-weight-bold">100%</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="h6 text-info font-italic font-weight-bold">Sites/PPS n'ayant pas soumis</div>
                            <div class="card">
                                <div class="card-body">
                                    <div class="h5 text-center text-success">Tous vos Sites / PPS ont soumis</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
