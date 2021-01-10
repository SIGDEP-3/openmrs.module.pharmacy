<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../template/operationHeader.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacy/reports/consumption/list.form" />
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
                location.href = "${pageContext.request.contextPath}/module/pharmacy/reports/edit.form?programId="+ programId
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
            <button class="btn btn-primary btn-sm" onclick="create()" title="Nouveau">
                <i class="fa fa-plus"></i> Nouveau
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-7">
            <div class="h6 font-italic text-secondary">Liste des rapports du mois</div>
            <div class="card">
                <div class="card-body">
                    <table class="table table-bordered table-sm">
                        <thead>
                        <tr>
                            <th>Date du rapport</th>
                            <th>P&eacute;riode</th>
                            <th>Programme</th>
                            <th>Statut</th>
                            <th style="width: 30px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="report" items="${ reports }">
                            <tr>
                                <td><fmt:formatDate value="${report.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                <td>${report.reportPeriod}</td>
                                <td>${report.productProgram.name}</td>
                                <td>${report.operationStatus == 'NOT_COMPLETED' ? 'EN COURS DE SAISIE' : (report.operationStatus == 'VALIDATED' ? 'VALIDE' : 'EN ATTENTE DE VALIDATION')}</td>
                                <td>
                                    <c:url value="/module/pharmacy/reports/edit.form" var="editUrl">
                                        <c:param name="id" value="${report.productOperationId}"/>
                                    </c:url>
                                    <a href="${editUrl}" class="text-${report.operationStatus == 'VALIDATED' ? 'info': 'primary'}">
                                        <i class="fa fa-${report.operationStatus == 'VALIDATED' ? 'eye': 'edit'}"></i>
                                    </a>
                                    <openmrs:hasPrivilege privilege="Delete report">
                                        <c:if test="${report.operationStatus != 'VALIDATED'}">
                                            <c:url value="/module/pharmacy/reports/delete.form" var="delUrl">
                                                <c:param name="id" value="${report.productOperationId}"/>
                                            </c:url>
                                            <a href="${delUrl}"
                                               onclick="return confirm('Vous etes sur le point de supprimer le rapport, Voulez-vous continuer ?')"
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
            <div class="h6 font-italic text-secondary">Derniers rapports pr&eacute;c&eacute;dents par programme</div>
            <div class="card">
                <div class="card-body">
                    <table class="table table-borderless table-sm">
                        <thead>
                        <tr>
                            <th>Date du rapport</th>
                            <th>P&eacute;riode</th>
                            <th>Programme</th>
                            <th>Statut</th>
                            <th style="width: 30px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="report" items="${ lastMonthReports }">
                            <tr>
                                <td><fmt:formatDate value="${report.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                <td>${report.reportPeriod}</td>
                                <td>${report.productProgram.name}</td>
                                <td>${report.operationStatus == 'NOT_COMPLETED' ? 'EN COURS DE SAISIE' : (report.operationStatus == 'VALIDATED' ? 'VALIDE' : 'EN ATTENTE DE VALIDATION')}</td>
                                <td>
                                    <c:url value="/module/pharmacy/reports/editFlux.form" var="editUrl">
                                        <c:param name="id" value="${report.productOperationId}"/>
                                    </c:url>
                                    <a href="${editUrl}" class="text-${report.operationStatus == 'VALIDATED' ? 'info': 'primary'}">
                                        <i class="fa fa-${report.operationStatus == 'VALIDATED' ? 'eye': 'edit'}"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
