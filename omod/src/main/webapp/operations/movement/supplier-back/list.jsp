<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../../template/operationHeader.jsp"%>
<openmrs:require privilege="View Edit Product Back Supplier" otherwise="/login.htm" redirect="/module/pharmacy/operations/movement/supplier-back/list.form" />

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
            if (programId === undefined || programId === null || programId === '' ) {
                jQuery('#selectMe').html('<i class="fa fa-hand-point-right fa-2x text-danger"></i>');
            } else {
                location.href = "${pageContext.request.contextPath}/module/pharmacy/operations/movement/supplier-back/edit.form?programId=" + programId;
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
        <div class="col-12">
            <table class="table table-striped table-sm">
                <thead>
                <tr>
                    <th>
                        <%--            <spring:message code="pharmacy.transferDate"/>--%>
                        Date de r&eacute;ception du retour
                    </th>
                    <th>
                        <%--            <spring:message code="pharmacy.program"/>--%>
                        Programme
                    </th>
<%--                    <th>Type d'inventaire</th>--%>
                    <th>Site / PPS</th>
                    <th>Lignes de produit</th>
                    <th>
                        <%--            <spring:message code="pharmacy.status"/>--%>
                        Etat
                    </th>
                    <th style="width: 30px"></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="back" items="${ supplierBacks }">
                    <tr>
                        <td><fmt:formatDate value="${back.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                        <td>${back.productProgram.name}</td>
                        <td>${back.exchangeLocation.name}</td>
                        <c:choose>
                            <c:when test="${fct:length(back.productAttributeFluxes) == 0}">
                                <c:url value="/module/pharmacy/operations/movement/supplier-back/editFlux.form" var="addLineUrl">
                                    <c:param name="backSupplierId" value="${back.productOperationId}"/>
                                </c:url>
                                <td class="text-danger">
                                    <a href="${addLineUrl}">Ajouter des produits</a>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="text-center">
                                        ${fct:length(back.productAttributeFluxes)}
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td>${back.operationStatus == 'NOT_COMPLETED' ? 'EN COURS DE SAISIE' : (back.operationStatus == 'VALIDATED' ? 'VALIDE' : 'EN ATTENTE DE VALIDATION')}</td>
                        <td>
                            <c:url value="/module/pharmacy/operations/movement/supplier-back/edit.form" var="editUrl">
                                <c:param name="id" value="${back.productOperationId}"/>
                            </c:url>
                            <a href="${editUrl}" class="text-${back.operationStatus == 'VALIDATED' ? 'info': 'primary'}">
                                <i class="fa fa-${back.operationStatus == 'VALIDATED' ? 'eye': 'edit'}"></i>
                            </a>
                            <openmrs:hasPrivilege privilege="Delete Product Back Supplier">
                                <c:if test="${back.operationStatus != 'VALIDATED'}">
                                    <c:url value="/module/pharmacy/operations/movement/back/delete.form" var="delUrl">
                                        <c:param name="id" value="${back.productOperationId}"/>
                                    </c:url>
                                    <a href="${delUrl}"
                                       onclick="return confirm('Vous êtes sur le point de supprimer le retour de produits du site, Voulez-vous continuer ?')"
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
