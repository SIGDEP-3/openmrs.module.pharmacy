<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<style>
    .card {
        border-radius: 0;
    }
</style>
<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.list').DataTable();
            jQuery("#program").change(function () {
                if (jQuery(this).val()){
                    jQuery('#selectMe').text('');
                } else {
                    jQuery('#selectMe').html('<i class="fa fa-hand-point-right fa-2x text-danger"></i>');
                }
            });


            <c:if test="${findPatientForm.dispensationType != 'HIV_PATIENT'}">
            jQuery("#patientIdentifier").attr('disabled', 'disabled');
            </c:if>

            jQuery("#dispensationType1,#dispensationType2,#dispensationType3").on('click', function (e) {
                if (jQuery(this).val() !== 'HIV_PATIENT') {
                    jQuery("#patientIdentifier").attr('disabled', 'disabled');
                } else {
                    jQuery("#patientIdentifier").removeAttr('disabled');
                }
            })
        });

        function create() {
            const selection = jQuery("#program");
            const programId = selection.val();
            if (programId === undefined || programId === null || programId === '') {
                jQuery('#selectMe').html('<i class="fa fa-hand-point-right fa-2x text-danger"></i>');
            } else {
                location.href = "${pageContext.request.contextPath}/module/pharmacy/operations/dispensation/edit.form?programId="+ programId
            }
        }
    }
</script>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacy/operations/dispensation/list.form" />
<div class="container-fluid mt-2">
    <div class="row mt-3">
        <div class="col-12">
            <div class="h5 text-info"><i class="fa fa-calculator"></i> Statistique du mois</div>
        </div>
    </div>
    <div class="row mt-1 mb-3 pb-2 pt-2 border-bottom border-top border-secondary">
        <div class="col">
            <div class="card m-0 text-white bg-secondary">
                <div class="card-header text-center m-0 p-0">Patients du site</div>
                <div class="card-body m-0 p-0 h1 text-center">0</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-white bg-secondary">
                <div class="card-header text-center m-0 p-0">Mobile</div>
                <div class="card-body m-0 p-0 h1 text-center">0</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-white bg-secondary">
                <div class="card-header text-center m-0 p-0">Adulte</div>
                <div class="card-body m-0 p-0 h1 text-center">0</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-white bg-secondary">
                <div class="card-header text-center m-0 p-0">Enfant</div>
                <div class="card-body m-0 p-0 h1 text-center">0</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-white bg-secondary">
                <div class="card-header text-center m-0 p-0">Masculin</div>
                <div class="card-body m-0 p-0 h1 text-center">0</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-white bg-secondary">
                <div class="card-header text-center m-0 p-0">F&eacute;minin</div>
                <div class="card-body m-0 p-0 h1 text-center">0</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-white bg-secondary">
                <div class="card-header text-center m-0 p-0">PEC</div>
                <div class="card-body m-0 p-0 h1 text-center">0</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-white bg-secondary">
                <div class="card-header text-center m-0 p-0">PTME</div>
                <div class="card-body m-0 p-0 h1 text-center">0</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-white bg-secondary">
                <div class="card-header text-center m-0 p-0">AES</div>
                <div class="card-body m-0 p-0 h1 text-center">0</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-white bg-secondary">
                <div class="card-header text-center m-0 p-0">NA</div>
                <div class="card-body m-0 p-0 h1 text-center">0</div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-6">
            <div class="h5 text-info"><i class="fa fa-medkit"></i> Saisie de dispensation</div>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <form:form modelAttribute="findPatientForm" method="post" action="" id="form">
                <form:errors path="productProgramId" cssClass="error"/>
                <form:errors path="patientIdentifier" cssClass="error"/>
                <form:errors path="dispensationType" cssClass="error"/>
                <form:errors path="patientType" cssClass="error"/>
                <div class="row align-items-center">
                    <div class="col-11">
                        <div class="form-row">
                            <div class="col-8">
                                <div class="row">
                                    <label class="col-2 pt-1">Programme : </label>
                                    <div class="col-4">
                                        <form:select path="productProgramId" cssClass="form-control s2">
                                            <form:option value="" label=""/>
                                            <form:options items="${programs}" itemValue="productProgramId"
                                                          itemLabel="name"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-row mt-2">
                            <div class="col-8 pt-1 pb-1 bg-gray">
                                <div class="row">
                                    <div class="col-2 pt-1">
                                        <form:radiobutton path="dispensationType" value="HIV_PATIENT" label="Patient VIH" cssClass="mr-2"/>
                                    </div>
                                    <div class="col-4">
                                        <form:input path="patientIdentifier" cssClass="form-control form-control-sm" />
                                    </div>
                                    <div class="col-2 pt-1">
                                        <form:radiobutton path="patientType" value="ON_SITE" label="Site" cssClass="form-check-input mr-2"/>
                                    </div>
                                    <div class="col-2 pt-1">
                                        <form:radiobutton path="patientType" value="MOBILE" label="Mobile" cssClass="mr-2"/>
                                    </div>
                                    <div class="col-2 pt-1">
                                        <form:radiobutton path="patientType" value="OTHER_HIV" label="Autre VIH" cssClass="mr-2"/>
                                    </div>

                                </div>
                            </div>
                            <div class="col-4 mb-1 align-items-center">
                                <div class="row">
                                    <div class="offset-2 col-4 pt-2">
                                        <form:radiobutton path="dispensationType" value="OTHER_PATIENT" label=" Autre Patient" cssClass="mr-2"/>
                                    </div>
                                    <div class="col-4 pt-2">
                                        <form:radiobutton path="dispensationType" value="OTHER_DISPENSATION" label=" Autre vente" cssClass="mr-2"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-1 text-right">
                        <button type="submit" class="btn btn-primary btn-sm" title="Créer nouveau">
                            <i class="fa fa-plus"></i> Dispenser
                        </button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
    <div class="row mt-3">
        <div class="col-sm-6">
            <div class="h5 text-info"><i class="fa fa-list"></i> ${subTitle}</div>
        </div>
    </div>
    <div class="row bg-light border border-secondary align-items-center">
        <div class="col-12">
            <div class="row border-bottom mb-2 border-secondary">
                <div class="col-sm-6 offset-6 text-right ">
                    <table class="table table-sm table-borderless m-0 p-0">
                        <tr>
                            <td class="m-1 p-1"><input type="text" placeholder="Numero patient"
                                                       class="form-control form-control-sm"></td>
                            <td class="m-1 p-1"><input type="text" placeholder="Date de debut"
                                                       class="form-control form-control-sm"></td>
                            <td class="m-1 p-1"><input type="text" placeholder="Date de fin"
                                                       class="form-control form-control-sm"></td>
                            <td class="m-1 p-1">
                                <button class="btn btn-sm btn-primary"><i class="fa fa-search"></i> Rechercher</button>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-12 mt-2">
            <table class="table table-striped table-sm bg-light list">
                <thead>
                <tr>
                    <th>
                        <%--            <spring:message code="pharmacy.dispensationDate"/>--%>
                        Date de dispensation
                    </th>
                    <th>
                        <%--            <spring:message code="pharmacy.program"/>--%>
                        Programme
                    </th>
                    <th>Type de patient</th>
                    <th>
                        Num&eacute;ro du patient
                    </th>
                    <th>
                        <%--            <spring:message code="pharmacy.status"/>--%>
                        Regime
                    </th>
                    <th>Nombre de jours de TTT</th>
                    <th>Date de prescription</th>
                    <th style="width: 30px"></th>
                </tr>
                </thead>
                <tbody>
                <%--                <c:forEach var="dispensation" items="${ inventories }">--%>
                <%--                    <tr>--%>
                <%--                        <td><fmt:formatDate value="${dispensation.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>--%>
                <%--                        <td>${dispensation.operationNumber}</td>--%>
                <%--                        <td>${dispensation.productProgram.name}</td>--%>
                <%--                        <td>${dispensation.dispensationType == 'FULL' ? 'COMPLET' : 'PARTIEL'}</td>--%>
                <%--                        <c:choose>--%>
                <%--                            <c:when test="${fct:length(dispensation.productAttributeFluxes) == 0}">--%>
                <%--                                <c:url value="/module/pharmacy/operations/dispensation/editFlux.form" var="addLineUrl">--%>
                <%--                                    <c:param name="dispensationId" value="${dispensation.productOperationId}"/>--%>
                <%--                                </c:url>--%>
                <%--                                <td class="text-danger">--%>
                <%--                                    <a href="${addLineUrl}">Ajouter des produits</a>--%>
                <%--                                </td>--%>
                <%--                            </c:when>--%>
                <%--                            <c:otherwise>--%>
                <%--                                <td class="text-center">--%>
                <%--                                        ${fct:length(dispensation.productAttributeFluxes)}--%>
                <%--                                </td>--%>
                <%--                            </c:otherwise>--%>
                <%--                        </c:choose>--%>
                <%--                        <td>${dispensation.operationStatus}</td>--%>
                <%--                        <td>--%>
                <%--                            <c:url value="/module/pharmacy/operations/dispensation/edit.form" var="editUrl">--%>
                <%--                                <c:param name="id" value="${dispensation.productOperationId}"/>--%>
                <%--                            </c:url>--%>
                <%--                            <a href="${editUrl}" class="text-${dispensation.operationStatus == 'VALIDATED' ? 'info': 'primary'}">--%>
                <%--                                <i class="fa fa-${dispensation.operationStatus == 'VALIDATED' ? 'eye': 'edit'}"></i>--%>
                <%--                            </a>--%>
                <%--                        </td>--%>
                <%--                    </tr>--%>
                <%--                </c:forEach>--%>
                </tbody>

            </table>
        </div>
    </div>
    <%--    <div class="row bg-light pb-2 mt-2">--%>
    <%--        <div class="col-12">--%>
    <%--            <h4>Statistiques du mois</h4>--%>
    <%--            <div class="row">--%>
    <%--                <div class="col">--%>
    <%--                    <div class="row">--%>
    <%--                        <div class="col-12"><div class="h6 text-info">Par Type de patient</div></div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header">Patients du site</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">Patients Mobile</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">Patients Mobile</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--                <div class="col">--%>
    <%--                    <div class="row">--%>
    <%--                        <div class="col-12"><div class="h6 text-info">Par Genre</div></div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header">Masculin</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">F&eacute;minin</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">Inconnu</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--                <div class="col">--%>
    <%--                    <div class="row">--%>
    <%--                        <div class="col-12"><div class="h6 text-info">Par But</div></div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header">PEC</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">PTME</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">AES</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">NA</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--                <div class="col">--%>
    <%--                    <div class="row">--%>
    <%--                        <div class="col-12"><div class="h6 text-info">Par Age</div></div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header">Adulte</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">Enfant</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">Adulte f&eacute;minin</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">Adulte masculin</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">Enfant f&eacute;minin</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                        <div class="col">--%>
    <%--                            <div class="card">--%>
    <%--                                <div class="card-header text-center">Enfant masculin</div>--%>
    <%--                                <div class="card-body h1 text-center">0</div>--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--            </div>--%>
    <%--        </div>--%>
    <%--        &lt;%&ndash;        <div class="col-12">&ndash;%&gt;--%>
    <%--        &lt;%&ndash;            <table class="table table-striped table-sm">&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                <thead>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                <tr>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    <th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        &lt;%&ndash;            <spring:message code="pharmacy.dispensationDate"/>&ndash;%&gt;&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        Date de l'inventaire&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    </th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    <th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        Num&eacute;ro de pi&egrave;ce&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    </th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    <th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        &lt;%&ndash;            <spring:message code="pharmacy.program"/>&ndash;%&gt;&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        Programme&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    </th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    <th>Type d'inventaire</th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    <th>Nombre de produits</th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    <th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        &lt;%&ndash;            <spring:message code="pharmacy.status"/>&ndash;%&gt;&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        Etat&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    </th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    <th style="width: 30px"></th>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                </tr>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                </thead>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                <tbody>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                <c:forEach var="dispensation" items="${ inventories }">&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    <tr>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        <td><fmt:formatDate value="${dispensation.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        <td>${dispensation.operationNumber}</td>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        <td>${dispensation.productProgram.name}</td>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        <td>${dispensation.dispensationType == 'FULL' ? 'COMPLET' : 'PARTIEL'}</td>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        <c:choose>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                            <c:when test="${fct:length(dispensation.productAttributeFluxes) == 0}">&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                <c:url value="/module/pharmacy/operations/dispensation/editFlux.form" var="addLineUrl">&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                    <c:param name="dispensationId" value="${dispensation.productOperationId}"/>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                </c:url>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                <td class="text-danger">&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                    <a href="${addLineUrl}">Ajouter des produits</a>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                </td>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                            </c:when>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                            <c:otherwise>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                <td class="text-center">&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                        ${fct:length(dispensation.productAttributeFluxes)}&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                </td>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                            </c:otherwise>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        </c:choose>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        <td>${dispensation.operationStatus}</td>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        <td>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                            <c:url value="/module/pharmacy/operations/dispensation/edit.form" var="editUrl">&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                <c:param name="id" value="${dispensation.productOperationId}"/>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                            </c:url>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                            <a href="${editUrl}" class="text-${dispensation.operationStatus == 'VALIDATED' ? 'info': 'primary'}">&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                                <i class="fa fa-${dispensation.operationStatus == 'VALIDATED' ? 'eye': 'edit'}"></i>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                            </a>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                        </td>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                    </tr>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                </c:forEach>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;                </tbody>&ndash;%&gt;--%>

    <%--        &lt;%&ndash;            </table>&ndash;%&gt;--%>
    <%--        &lt;%&ndash;        </div>&ndash;%&gt;--%>
    <%--    </div>--%>

</div>
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
