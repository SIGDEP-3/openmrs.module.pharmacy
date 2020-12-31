<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../../template/operationHeader.jsp"%>
<openmrs:require privilege="View Transfer" otherwise="/login.htm" redirect="/module/pharmacy/operations/movement/transfer/list.form" />

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
            const transferType = jQuery("input[name='transfer']:checked").val();
            const programId = selection.val();
            if (programId === undefined || programId === null || programId === '' ) {
                jQuery('#selectMe').html('<i class="fa fa-hand-point-right fa-2x text-danger"></i>');
            } else {
                if(transferType === undefined || transferType === null || transferType === '') {
                    jQuery('#selectMe').html('<i class="fa fa-hand-point-right fa-2x text-danger"></i>');
                } else {
                    location.href = "${pageContext.request.contextPath}/module/pharmacy/operations/movement/transfer/edit.form?programId=" + programId + "&type=" + transferType
                }
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
            <label>
                <input type="radio" name="transfer" value="IN"> Transfert Entrant
            </label>
            <label>
                <input type="radio" name="transfer" value="OUT"> Transfert Sortant
            </label>
            <%--            <c:url value="/module/pharmacy/operations/transfer/edit.form" var="url"/>--%>
            <button class="btn btn-primary btn-sm" onclick="create()" title="Nouveau">
                <i class="fa fa-plus"></i> Nouveau
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <table class="table table-striped table-sm">
                <thead>
                <tr>
                    <th>
                        <%--            <spring:message code="pharmacy.transferDate"/>--%>
                        Date du transfert
                    </th>
                    <th>
                        Type de transfert
                    </th>
                    <th>
                        <%--            <spring:message code="pharmacy.program"/>--%>
                        Programme
                    </th>
<%--                    <th>Type d'inventaire</th>--%>
                    <th>Destinataire / Exp&eacute;diteur</th>
                    <th>Nombre de produits</th>
                    <th>
                        <%--            <spring:message code="pharmacy.status"/>--%>
                        Etat
                    </th>
                    <th style="width: 30px"></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="transfer" items="${ transfers }">
                    <tr>
                        <td><fmt:formatDate value="${transfer.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                        <td>${transfer.transferType == 'IN' ? 'ENTRANT' : 'SORTANT' }</td>
                        <td>${transfer.productProgram.name}</td>
                        <td>${transfer.exchangeLocation.name}</td>
                        <c:choose>
                            <c:when test="${fct:length(transfer.productAttributeFluxes) == 0}">
                                <c:url value="/module/pharmacy/operations/movement/transfer/editFlux.form" var="addLineUrl">
                                    <c:param name="transferId" value="${transfer.productOperationId}"/>
                                </c:url>
                                <td class="text-danger">
                                    <a href="${addLineUrl}">Ajouter des produits</a>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="text-center">
                                        ${fct:length(transfer.productAttributeFluxes)}
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td>${transfer.operationStatus == 'NOT_COMPLETED' ? 'EN COURS DE SAISIE' : (transfer.operationStatus == 'VALIDATED' ? 'VALIDE' : 'EN ATTENTE DE VALIDATION')}</td>
                        <td>
                            <c:url value="/module/pharmacy/operations/movement/transfer/edit.form" var="editUrl">
                                <c:param name="id" value="${transfer.productOperationId}"/>
                            </c:url>
                            <a href="${editUrl}" class="text-${transfer.operationStatus == 'VALIDATED' ? 'info': 'primary'}">
                                <i class="fa fa-${transfer.operationStatus == 'VALIDATED' ? 'eye': 'edit'}"></i>
                            </a>
                            <openmrs:hasPrivilege privilege="Delete Transfer">
                                <c:if test="${transfer.operationStatus != 'VALIDATED'}">
                                    <c:url value="/module/pharmacy/operations/movement/transfer/delete.form"
                                           var="delUrl">
                                        <c:param name="id" value="${transfer.productOperationId}"/>
                                    </c:url>
                                    <a href="${delUrl}"
                                       onclick="return confirm('Vous êtes sur le point de supprimer le transfert, Voulez-vous continuer ?')"
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
<%@ include file="../../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
