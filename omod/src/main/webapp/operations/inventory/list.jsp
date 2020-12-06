<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacy/operations/inventory/list.form" />

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
                location.href = "${pageContext.request.contextPath}/module/pharmacy/operations/inventory/edit.form?programId="+ programId
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
            <select name="program" class="s2 form-control-sm mr-3" id="program">
                <option value=""></option>
                <c:forEach var="program" items="${programs}">
                    <option value="${program.productProgramId}">${program.name}</option>
                </c:forEach>
            </select>
<%--            <c:url value="/module/pharmacy/operations/inventory/edit.form" var="url"/>--%>
            <button class="btn btn-primary btn-sm" onclick="create()" title="Créer nouveau">
                <i class="fa fa-plus"></i> Nouvel inventaire
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <table class="table table-striped table-sm">
                <thead>
                <tr>
                    <th>
                        <%--            <spring:message code="pharmacy.inventoryDate"/>--%>
                        Date de l'inventaire
                    </th>
                    <th>
                        Num&eacute;ro de pi&egrave;ce
                    </th>
                    <th>
                        <%--            <spring:message code="pharmacy.program"/>--%>
                        Programme
                    </th>
                    <th>Type d'inventaire</th>
                    <th>Nombre de produits</th>
                    <th>
                        <%--            <spring:message code="pharmacy.status"/>--%>
                        Etat
                    </th>
                    <th style="width: 30px"></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="inventory" items="${ inventories }">
                    <tr>
                        <td><fmt:formatDate value="${inventory.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                        <td>${inventory.operationNumber}</td>
                        <td>${inventory.productProgram.name}</td>
                        <td>${inventory.inventoryType == 'FULL' ? 'COMPLET' : 'PARTIEL'}</td>
                        <c:choose>
                            <c:when test="${fct:length(inventory.productAttributeFluxes) == 0}">
                                <c:url value="/module/pharmacy/operations/inventory/editFlux.form" var="addLineUrl">
                                    <c:param name="inventoryId" value="${inventory.productOperationId}"/>
                                </c:url>
                                <td class="text-danger">
                                    <a href="${addLineUrl}">Ajouter des produits</a>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="text-center">
                                        ${fct:length(inventory.productAttributeFluxes)}
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td>${inventory.operationStatus == 'NOT_COMPLETED' ? 'EN COURS DE SAISIE' : (inventory.operationStatus == 'VALIDATED' ? 'VALIDE' : 'EN ATTENTE DE VALIDATION')}</td>
                        <td>
                            <c:url value="/module/pharmacy/operations/inventory/edit.form" var="editUrl">
                                <c:param name="id" value="${inventory.productOperationId}"/>
                            </c:url>
                            <a href="${editUrl}" class="text-${inventory.operationStatus == 'VALIDATED' ? 'info': 'primary'}">
                                <i class="fa fa-${inventory.operationStatus == 'VALIDATED' ? 'eye': 'edit'}"></i>
                            </a>
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
