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
            });
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
    <div class="row mb-2 pt-2 border-top border-secondary">
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${fct:length(childrenLocation)}
                    </div>
                    <div class="card-title text-info text-center small">Sites/PPS</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${totalReportsToSubmit}
                    </div>
                    <div class="card-title text-info text-center small">Rapports attendus</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${submittedReportCount}
                    </div>
                    <div class="card-title text-info text-center small">Rapports soumis</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        <fmt:formatNumber type="number"
                                          value="${(submittedReportCount / totalReportsToSubmit) * 100}"
                                          maxFractionDigits="1" />%
                    </div>
                    <div class="card-title text-info text-center small">Compl&eacute;tude</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${submittedOnTimeReportCount}
                    </div>
                    <div class="card-title text-info text-center small">Soumis &agrave; temps</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        <fmt:formatNumber type="number"
                                          value="${(submittedOnTimeReportCount / totalReportsToSubmit) * 100}"
                                          maxFractionDigits="1" />%
                    </div>
                    <div class="card-title text-info text-center small">Promptitude</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${treatedReportCount}
                    </div>
                    <div class="card-title text-info text-center small">Rapports trait&eacute;s</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        <c:if test="${submittedReportCount > 0}">
                            <fmt:formatNumber type="number"
                                              value="${(treatedReportCount / submittedReportCount) * 100}"
                                              maxFractionDigits="1" />%
                        </c:if>
                        <c:if test="${submittedReportCount == 0}">
                            NA
                        </c:if>
                    </div>
                    <div class="card-title text-info text-center small">Taux de traitement</div>
                </div>
            </div>
        </div>
    </div>

    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <div class="row">
                <div class="col-8">
                    <div class="h6 text-info font-italic">Liste des distributions du mois</div>
                    <div class="card p-1">
<%--                        <div class="card-body">--%>
<%--                        </div>--%>
                        <table class="table table-striped table-sm">
                            <thead>
                            <tr class="small">
                                <th>
                                    <%--            <spring:message code="pharmacy.inventoryDate"/>--%>
                                    Date de soumission
                                </th>
                                <th>
                                    P&eacute;riode
                                </th>
                                <th>
                                    <%--            <spring:message code="pharmacy.program"/>--%>
                                    Programme
                                </th>
                                <%--                    <th>Type d'inventaire</th>--%>
                                <th>Centre / PPS</th>
                                <th>Lignes de produits</th>
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
                                    <td>${report.reportLocation.name}</td>
                                        <%--                        <td>${inventory.inventoryType == 'FULL' ? 'COMPLET' : 'PARTIEL'}</td>--%>
                                    <c:choose>
                                        <c:when test="${fct:length(report.childLocationReport.otherFluxesProductList) == 0}">
                                            <c:url value="/module/pharmacy/operations/distribution/editFlux.form" var="addLineUrl">
                                                <c:param name="distributionId" value="${report.productOperationId}"/>
                                            </c:url>
                                            <td class="text-danger">
                                                <a href="${addLineUrl}">Ajouter des produits</a>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="text-center">
                                                    ${fct:length(report.childLocationReport.otherFluxesProductList)}
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>
                                        <c:if test="${report.operationStatus == 'NOT_COMPLETED' && report.childLocationReport.operationStatus == 'SUBMITTED'}">
                                        RAPPORT SOUMIS
                                        </c:if>
                                        <c:if test="${report.operationStatus == 'NOT_COMPLETED' && report.childLocationReport.operationStatus == 'NOT_COMPLETED'}">
                                        EN COURS DE SAISIE
                                        </c:if>
                                        <c:if test="${report.operationStatus == 'AWAITING_TREATMENT'}">
                                        EN ATTENTE DE TRATEMENT
                                        </c:if>
                                        <c:if test="${report.operationStatus == 'VALIDATED'}">
                                        RAPPORT TRAIT&Eacute;
                                        </c:if>
                                    <td>
                                        <c:url value="/module/pharmacy/operations/distribution/edit.form" var="editUrl">
                                            <c:param name="id" value="${report.productOperationId}"/>
                                        </c:url>
                                        <a href="${editUrl}" class="text-${report.operationStatus == 'VALIDATED' ? 'info': 'primary'}">
                                            <i class="fa fa-${report.operationStatus == 'VALIDATED' ? 'eye': 'edit'}"></i>
                                        </a>
                                        <c:if test="${report.operationStatus == 'VALIDATED'}">
                                            <c:url value="/module/pharmacy/operations/distribution/editFlux.form"
                                                   var="deliveryUrl">
                                                <c:param name="distributionId" value="${report.productOperationId}"/>
                                                <c:param name="slip" value="${report.productOperationId}"/>
                                            </c:url>
                                            <a href="${deliveryUrl}"
                                               class="text-primary">
                                                <i class="fa fa-file"></i>
                                            </a>
                                        </c:if>
                                        <openmrs:hasPrivilege privilege="Delete Report">
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
                <div class="col-4">
                    <div class="row mb-2">
                        <div class="col-12">
                            <div class="h6 text-info font-italic">Rapports soumis non trait&eacute;s</div>
                            <div class="card">
                                <div class="card-body">
                                    <c:if test="${fct:length(submittedReportsNotTreated) == 0}">
                                        <div class="h5 text-center text-success">Aucune soumission non trait&eacute;e</div>
                                    </c:if>
                                    <c:if test="${fct:length(submittedReportsNotTreated) > 0}">
                                        <table class="table table-sm table-borderless">
                                            <thead>
                                            <tr>
                                                <th>Site / PPS</th>
                                                <th>Programme</th>
                                                <th>Date de soumission</th>
                                                <th>Urgent ?</th>
                                                <th></th>
                                            </tr>
                                            </thead>
                                            <tbody>

                                            </tbody>
                                        </table>
                                    </c:if>
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
