<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<openmrs:require privilege="View Dispensation" otherwise="/login.htm" redirect="/module/pharmacy/operations/dispensation/list.form" />

<style>
    .card {
        border-radius: 0;
    }
</style>
<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            var identifierElement = jQuery("#patientIdentifier");
            var programElement = jQuery("#program");

            jQuery('.list').DataTable();
            programElement.change(function () {
                if (jQuery(this).val()){
                    jQuery('#selectMe').text('');
                } else {
                    jQuery('#selectMe').html('<i class="fa fa-hand-point-right fa-2x text-danger"></i>');
                }
            });

            <c:if test="${findPatientForm.dispensationType != 'HIV_PATIENT'}">
                identifierElement.attr('disabled', 'disabled');
            </c:if>

            jQuery('input[name="dispensationType"]').on('click', function (e) {
                if (jQuery(this).val() !== 'HIV_PATIENT') {
                    identifierElement.attr('disabled', 'disabled');
                    identifierElement.val('');
                    jQuery('input[name="patientType"]').val('');
                } else {
                    identifierElement.removeAttr('disabled');
                }
            });

            // jQuery('#form').on('submit', function (e){
            //     e.preventDefault();
            // });
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

        function transformDispensation() {
            jQuery.ajax({
                type: 'GET',
                url: '${pageContext.request.contextPath}/transform-dispensation.form',
                dataType : "text",
                crossDomain:true,
                success : function(data) {
                    console.log(data);
                    //jQuery('.' + batchNumber).parent().html(data.quantity)
                    //alert(data.toString());
                },
                error : function(data) {
                    console.log(data)
                }
            })
        }
    }
</script>
<div class="container-fluid mt-2">
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <form:form modelAttribute="findPatientForm" method="post" action="" id="form">
                <form:errors path="productProgramId" cssClass="error"/>
                <form:errors path="patientIdentifier" cssClass="error"/>
                <form:errors path="dispensationType" cssClass="error"/>
                <form:errors path="patientType" cssClass="error"/>
                <div class="d-flex justify-content-between align-middle">
                    <div class="d-flex justify-content-start">
                        <div class="form-inline pr-2">
                            <i class="fa fa-medkit"></i> &nbsp;<label class="col-form-label">
                            Nouvelle dispensation
                        </label>
                        </div>
                        <div class="form-inline pl-1 pr-2 bg-summer-sky">
                            <label for="program" class="col-form-label mr-2">Programme : </label>
                            <form:select path="productProgramId" cssClass="form-control s2" id="program">
                                <form:option value="" label=""/>
                                <form:options items="${programs}" itemValue="productProgramId"
                                              itemLabel="name"/>
                            </form:select>
                        </div>
                        <div class="form-inline pl-2 pr-2 bg-info border-left border-dark">
                            <form:radiobutton path="dispensationType" value="HIV_PATIENT" label=" Patient VIH" cssClass="mr-2 ml-1"/>

                            <form:input path="patientIdentifier" cssClass="form-control form-control-sm mr-2 ml-3" />
                            <form:radiobutton path="patientType" value="ON_SITE" label="PEC" cssClass="form-check-input mr-2 ml-3"/>
                            <form:radiobutton path="patientType" value="OTHER_HIV" label="Autre VIH" cssClass="mr-2 ml-3"/>
                        </div>
                        <div class="form-inline pl-3 pr-2 bg-emerald border-left border-dark">
                            <form:radiobutton path="dispensationType" value="OTHER_PATIENT" label=" Autre Patient" cssClass="mr-2 ml-2"/>
                        </div>
                        <div class="form-inline pl-3 pr-1 bg-emerald border-left border-dark">
                            <form:radiobutton path="dispensationType" value="OTHER_DISPENSATION" label=" Autre dispensation" cssClass="mr-2"/>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary btn-sm" title="Creer nouveau">
                        <i class="fa fa-plus"></i> Ajouter
                    </button>
                </div>

                <%--  <div class="row align-items-center">
                      <div class="col-11">
                          <div class="form-row">
                              <div class="col-4 bg-summer-sky">
                                  <div class="row align-items-center">
                                      <label class="col-4 pt-2">Programme : </label>
                                      <div class="col-8 pt-1">
                                          <form:select path="productProgramId" cssClass="form-control s2">
                                              <form:option value="" label=""/>
                                              <form:options items="${programs}" itemValue="productProgramId"
                                                            itemLabel="name"/>
                                          </form:select>
                                      </div>
                                  </div>
                              </div>
  &lt;%&ndash;                        </div>&ndash;%&gt;
  &lt;%&ndash;                        <div class="form-row align-items-center">&ndash;%&gt;
                              <div class="col-5 bg-gray">
                                  <div class="row align-items-center">
                                      <div class="col-2 pt-2">
                                          <form:radiobutton path="dispensationType" value="HIV_PATIENT" label="VIH" cssClass="mr-2"/>
                                      </div>
                                      <div class="col-5">
                                          <form:input path="patientIdentifier" cssClass="form-control form-control-sm" />
                                      </div>
                                      <div class="col-2 pt-2">
                                          <form:radiobutton path="patientType" value="ON_SITE" label="PEC" cssClass="form-check-input mr-2"/>
                                      </div>
  &lt;%&ndash;                                    <div class="col-2 pt-1">&ndash;%&gt;
  &lt;%&ndash;                                        <form:radiobutton path="patientType" value="MOBILE" label="Mobile" cssClass="mr-2"/>&ndash;%&gt;
  &lt;%&ndash;                                    </div>&ndash;%&gt;
                                      <div class="col-3 pt-2">
                                          <form:radiobutton path="patientType" value="OTHER_HIV" label="Autre VIH" cssClass="mr-2"/>
                                      </div>

                                  </div>
                              </div>
                              <div class="col-3 align-items-center bg-cyanite">
                                  <div class="row align-items-center text-center">
                                      <div class="col-6 pt-2">
                                          <form:radiobutton path="dispensationType" value="OTHER_PATIENT" label=" Autre Patient" cssClass="mr-2"/>
                                      </div>
                                      <div class="col-6 pt-2">
                                          <form:radiobutton path="dispensationType" value="OTHER_DISPENSATION" label=" Autre vente" cssClass="mr-2"/>
                                      </div>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="col-1 text-right">
                          <button type="submit" class="btn btn-primary btn-sm" title="Creer nouveau">
                              <i class="fa fa-plus"></i>
                          </button>
                      </div>
                  </div>--%>
            </form:form>
        </div>
    </div>
    <div class="row mt-3">
        <div class="col-12">
            <div class="h6 text-info"><i class="fa fa-calculator"></i> Statistique des dispensations ARV</div>
        </div>
    </div>
    <div class="row mt-1 mb-3 pb-2 pt-2 border-bottom border-top border-secondary">
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${dispensationResult.total}
                    </div>
                    <div class="card-title text-info text-center small mb-0">Total</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${dispensationResult.onSite}
                    </div>
                    <div class="card-title text-info text-center small mb-0">Patients du Site</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${dispensationResult.adult}
                    </div>
                    <div class="card-title text-info text-center small mb-0">Adultes</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${dispensationResult.child}
                    </div>
                    <div class="card-title text-info text-center small mb-0">Enfants</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${dispensationResult.male}
                    </div>
                    <div class="card-title text-info text-center small mb-0">Masculin</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${dispensationResult.female}
                    </div>
                    <div class="card-title text-info text-center small mb-0">F&eacute;minin</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${dispensationResult.pec}
                    </div>
                    <div class="card-title text-info text-center small mb-0">PEC</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${dispensationResult.ptme}
                    </div>
                    <div class="card-title text-info text-center small mb-0">PTME</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${dispensationResult.aes + dispensationResult.prep}
                    </div>
                    <div class="card-title text-info text-center small mb-0">AES & PREP</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card card-fluid p-0 border-info bg-light" style="border-radius: 0">
                <div class="card-body m-0 p-1">
                    <div class="h2 text-center mb-0 text-primary">
                        ${dispensationResult.notApplicable}
                    </div>
                    <div class="card-title text-info text-center small mb-0">NA</div>
                </div>
            </div>
        </div>
    </div>
<%--    <div class="row">--%>
<%--        <div class="col-sm-6">--%>
<%--            <div class="h6 text-info"><i class="fa fa-medkit"></i> Saisie de dispensation</div>--%>
<%--        </div>--%>
<%--    </div>--%>
    <%--<div class="row bg-light border border-secondary align-items-center">
        <div class="col-12">
            <div class="row border-bottom mb-2 border-secondary align-items-center">
                <div class="col-sm-6">
                </div>
                <div class="col-sm-6 text-right ">
                    <table class="table table-sm table-borderless m-0 p-0">
                        <tr>
                            <td class="m-1 p-1">
                                <input type="text" placeholder="Numero patient"
                                                       class="form-control form-control-sm">
                            </td>
                            <td class="m-1 p-1">
                                <input type="text" placeholder="Date de debut"
                                                       class="form-control form-control-sm"></td>
                            <td class="m-1 p-1">
                                <input type="text" placeholder="Date de fin"
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
                        &lt;%&ndash;            <spring:message code="pharmacy.dispensationDate"/>&ndash;%&gt;
                        Date de dispensation
                    </th>
                    <th>Date de prescription</th>
                    <th>
                        &lt;%&ndash;            <spring:message code="pharmacy.program"/>&ndash;%&gt;
                        Programme
                    </th>
                    <th>Type de patient</th>
                    <th>
                        Num&eacute;ro du patient
                    </th>
                    <th>
                        &lt;%&ndash;            <spring:message code="pharmacy.status"/>&ndash;%&gt;
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
                        <td class="align-middle"><fmt:formatDate value="${dispensation.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
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
                            <openmrs:hasPrivilege privilege="Delete Dispensation">
                                <c:if test="${dispensation.operationStatus == 'NOT_COMPLETED'}">
                                    <c:url value="/module/pharmacy/operations/dispensation/delete.form" var="delUrl">
                                        <c:param name="dispensationId" value="${dispensation.productOperationId}"/>
                                    </c:url>
                                    <a href="${delUrl}"
                                       onclick="return confirm('Voulez-vous supprimer la dispensation en cours de saisie ?')"
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
    </div>--%>
</div>
<div class="row mt-3">
    <div class="col-sm-6">
        <div class="h6 text-info"><i class="fa fa-list"></i> ${subTitle}</div>
    </div>
</div>
<div class="card m-0">
    <div class="card-header p-2">
        <div class="d-flex justify-content-between">
            <div class="text-left">
                <c:if test="${numberPatientToTransform > 0}">
                    <button type="button" class="btn btn-sm btn-primary" onclick="transformDispensation()">
                        Transformer dispensation (${numberPatientToTransform})
                    </button>
                </c:if>
            </div>
            <div class="form-inline">
                <input type="text" placeholder="Numero patient"
                       class="form-control form-control-sm">
                <input type="text" placeholder="Date de debut"
                       class="form-control form-control-sm ml-2">
                <input type="text" placeholder="Date de fin"
                       class="form-control form-control-sm ml-2">
                <button class="btn btn-sm btn-primary ml-2">
                    <i class="fa fa-search"></i> Rechercher</button>
            </div>
        </div>
    </div>
    <div class="card-body p-2">
        <table class="table table-striped table-sm bg-light list">
            <thead class="small">
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
                    <td class="align-middle"><fmt:formatDate value="${dispensation.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
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
                        <openmrs:hasPrivilege privilege="Delete Dispensation">
                            <c:if test="${dispensation.operationStatus == 'NOT_COMPLETED'}">
                                <c:url value="/module/pharmacy/operations/dispensation/delete.form" var="delUrl">
                                    <c:param name="dispensationId" value="${dispensation.productOperationId}"/>
                                </c:url>
                                <a href="${delUrl}"
                                   onclick="return confirm('Voulez-vous supprimer la dispensation en cours de saisie ?')"
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
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
