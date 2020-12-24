<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacy/operations/reception/list.form" />

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
                location.href = "${pageContext.request.contextPath}/module/pharmacy/operations/reception/edit.form?programId="+ programId
            }
        }
    }
</script>
<div class="container-fluid mt-2">

    <div class="row mb-2">
        <div class="col-6">
            <div class="h5"><i class="fa fa-list"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">
            <span id="selectMe"></span>
            <label for="program">Programme : </label>
            <select name="program" class="s2 form-control-sm" id="program">
                <option value=""></option>
                <c:forEach var="program" items="${programs}">
                    <option value="${program.productProgramId}">${program.name}</option>
                </c:forEach>
            </select>
            <button class="btn btn-primary btn-sm" onclick="create()">
                <i class="fa fa-plus"></i> Nouvelle r&eacute;ception
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
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
                    <th>Nombre de produits</th>
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
                        <td>${reception.productSupplier}</td>
                        <td><fmt:formatDate value="${reception.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                        <td>${reception.operationNumber}</td>
                        <td>${reception.programName}</td>
                        <td>${reception.receptionMode == 'RETAIL' ? 'DETAIL' : 'EN GROS'}</td>
                        <c:choose>
                            <c:when test="${reception.numberOfLine == 0}">
                                <c:url value="/module/pharmacy/operations/reception/editFlux.form" var="addLineUrl">
                                    <c:param name="receptionId" value="${reception.productOperationId}"/>
                                </c:url>
                                <td class="text-danger">
                                    <a href="${addLineUrl}">Ajouter des produits</a>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="text-center">
                                    <div class="btn-group">
                                        <div class="btn btn-sm btn-primary">
                                                ${reception.numberOfLine}
                                        </div>
                                        <c:if test="${reception.operationStatus == 'VALIDATED'}">
                                            <c:if test="${reception.canReturn == true || reception.productReturnedOperationId != null}">
                                                <c:url value="/module/pharmacy/operations/reception/edit.form" var="backUrl">
                                                    <c:param name="receptionId" value="${reception.productReturnedOperationId != null ? reception.productReturnedOperationId : reception.productOpertaionId}"/>
                                                </c:url>
                                                <a href="${backUrl}"
                                                   class="btn btn-sm
                                                        ${reception.productReturnedOperationStatus == null ? 'btn-primary' :
                                                          (reception.productReturnedOperationStatus == 'NOT_COMPLETED' ? 'btn-info' :
                                                            (reception.productReturnedOperationStatus == 'AWAITING_VALIDATION' ? 'btn-warning' :
                                                              (reception.productReturnedOperationStatus == 'DISABLED' ? 'btn-danger' :
                                                                (reception.productReturnedOperationStatus == 'DISABLED' ? 'btn-success' : ''))))}
                                                text-decoration-none text-white">
                                                    <c:if test="${reception.canReturn == true}">
                                                        <c:choose>
                                                            <c:when test="${reception.productReturnedOperationStatus == null || reception.productReturnedOperationStatus == 'NOT_COMPLETED'}">
                                                                Retourner des produits
                                                            </c:when>
                                                            <c:when test="${reception.productReturnedOperationStatus == 'AWAITING_VALIDATION'}">
                                                                Retour &agrave; valider
                                                            </c:when>
                                                            <c:when test="${reception.productReturnedOperationStatus == 'VALIDATED'}">
                                                                Produits retourn&eacute;s
                                                            </c:when>
                                                        </c:choose>
                                                    </c:if>
                                                    <c:if test="${reception.canReturn == false && reception.productReturnedOperationId != null}">
                                                        Produits retourn&eacute;s
                                                    </c:if>
                                                </a>
                                            </c:if>
                                        </c:if>
                                    </div>

                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td>${reception.operationStatus == 'NOT_COMPLETED' ? 'EN COURS DE SAISIE' : (reception.operationStatus == 'VALIDATED' ? 'VALIDE' : 'EN ATTENTE DE VALIDATION')}</td>
                        <td>
                            <c:url value="/module/pharmacy/operations/reception/edit.form" var="editUrl">
                                <c:param name="id" value="${reception.productOperationId}"/>
                            </c:url>
                            <a href="${editUrl}" class="text-${reception.operationStatus == 'VALIDATED' ? 'info': 'primary'}">
                                <i class="fa fa-${reception.operationStatus == 'VALIDATED' ? 'eye': 'edit'}"></i>
                            </a>
                            <c:if test="${reception.operationStatus != 'VALIDATED'}">
                                <c:url value="/module/pharmacy/operations/reception/delete.form" var="delUrl">
                                    <c:param name="id" value="${reception.productOperationId}"/>
                                </c:url>
                                <a href="${delUrl}" onclick="return confirm('Vous etes sur le point de supprimer la réception, Voulez-vous continuer ?')" class="text-danger">
                                    <i class="fa fa-trash"></i>
                                </a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>

            </table>
        </div>
    </div>
</div>
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
