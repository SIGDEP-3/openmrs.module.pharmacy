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
            <div class="card m-0 text-info">
                <div class="card-header text-center m-0 p-0">Total</div>
                <div class="card-body m-0 p-0 h1 text-center">${dispensationResult.total}</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-info">
                <div class="card-header text-center m-0 p-0">Patients du Site</div>
                <div class="card-body m-0 p-0 h1 text-center">${dispensationResult.onSite}</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-info">
                <div class="card-header text-center m-0 p-0">Adultes</div>
                <div class="card-body m-0 p-0 h1 text-center">${dispensationResult.adult}</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-info">
                <div class="card-header text-center m-0 p-0">Enfants</div>
                <div class="card-body m-0 p-0 h1 text-center">${dispensationResult.child}</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-info">
                <div class="card-header text-center m-0 p-0">Masculin</div>
                <div class="card-body m-0 p-0 h1 text-center">${dispensationResult.male}</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-info">
                <div class="card-header text-center m-0 p-0">F&eacute;minin</div>
                <div class="card-body m-0 p-0 h1 text-center">${dispensationResult.female}</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-info">
                <div class="card-header text-center m-0 p-0">PEC</div>
                <div class="card-body m-0 p-0 h1 text-center">${dispensationResult.pec}</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-info">
                <div class="card-header text-center m-0 p-0">PTME</div>
                <div class="card-body m-0 p-0 h1 text-center">${dispensationResult.ptme}</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-info">
                <div class="card-header text-center m-0 p-0">AES & PREP</div>
                <div class="card-body m-0 p-0 h1 text-center">${dispensationResult.aes + dispensationResult.prep}</div>
            </div>
        </div>
        <div class="col">
            <div class="card m-0 text-info">
                <div class="card-header text-center m-0 p-0">NA</div>
                <div class="card-body m-0 p-0 h1 text-center">${dispensationResult.notApplicable}</div>
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
                        <button type="submit" class="btn btn-primary btn-sm" title="Creer nouveau">
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
                    <th>Date de prescription</th>
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
                    <th>Status</th>
                    <th style="width: 30px"></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="dispensation" items="${ dispensations }">
                    <tr class="${dispensation.operationStatus == 'DISABLED' ? 'bg-warning' : ''}">
                        <td><fmt:formatDate value="${dispensation.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                        <td><fmt:formatDate value="${dispensation.prescriptionDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                        <td>${dispensation.programName}</td>
                        <td>${dispensation.patientType}</td>
                        <td>${dispensation.patientIdentifier}</td>
                        <td>${dispensation.regimen}</td>
                        <td class="text-center">${dispensation.treatmentDays}</td>
                        <td>${dispensation.operationStatus == 'NOT_COMPLETED' ? 'SAISIE EN COURS' : (dispensation.operationStatus == 'VALIDATED' ? 'OK' : 'ANNULEE')}</td>
                        <td>
                            <c:url value="/module/pharmacy/operations/dispensation/editFlux.form" var="editUrl">
                                <c:param name="dispensationId" value="${dispensation.productOperationId}"/>
                            </c:url>
                            <a href="${editUrl}" class="text-${dispensation.operationStatus == 'VALIDATED' || dispensation.operationStatus == 'DISABLED' ? 'info': 'primary'}">
                                <i class="fa fa-${dispensation.operationStatus == 'VALIDATED' || dispensation.operationStatus == 'DISABLED' ? 'eye': 'edit'}"></i>
                            </a>
                            <c:if test="${dispensation.operationStatus == 'NOT_COMPLETED'}">
                                <c:url value="/module/pharmacy/operations/dispensation/delete.form" var="delUrl">
                                    <c:param name="dispensationId" value="${dispensation.productOperationId}"/>
                                </c:url>
                                <a href="${delUrl}" onclick="return confirm('Voulez-vous supprimer la dispensation en cours de saisie ?')" class="text-danger">
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
