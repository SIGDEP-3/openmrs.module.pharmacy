<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../template/operationHeader.jsp"%>
<openmrs:require privilege="Save Report" otherwise="/login.htm" redirect="/module/pharmacy/reports/editFluxOther.form" />

<script>
    if (jQuery) {

        jQuery(document).ready(function (){
            jQuery('#customFile').on('change',function(){
                //get the file name
                const fileName = $(this).val();
                if (fileName) {
                    let tFileName = fileName.split('\\')
                    jQuery(this).next('.custom-file-label').html(tFileName[tFileName.length - 1]);
                } else {
                    jQuery(this).next('.custom-file-label').html("Choisir le fichier pour importation");
                }
            });
        });
    }
</script>

<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-4 text-uppercase font-italic text-secondary">
            <div class="h6"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-8 text-right">

            <c:if test="${productReport.operationStatus != 'VALIDATED' &&
                      productReport.operationStatus != 'DISABLED'}">
                <c:if test="${invalidReport != true}">
                    <c:if test="${productReport.operationStatus == 'NOT_COMPLETED' &&
                                    isAsserted == true && fct:length(reportData) != 0 &&
                                    ((productReport.urgent == true && fct:length(reportData) <= 5) ||
                                        productReport.urgent == false)}">
                        <c:url value="/module/pharmacy/reports/complete.form" var="completeUrl">
                            <c:param name="reportId" value="${productReport.productOperationId}"/>
                        </c:url>
                        <button class="btn btn-success btn-sm mr-2" onclick="window.location='${completeUrl}'">
                            <i class="fa fa-save"></i> Terminer
                        </button>

                        <openmrs:hasPrivilege privilege="Delete Report">
                            <c:url value="/module/pharmacy/reports/delete.form"
                                   var="delUrl">
                                <c:param name="id" value="${headerDTO.productOperationId}"/>
                            </c:url>
                            <button type="button" class="btn btn-warning btn-sm"
                                    onclick="window.location='${delUrl}'" tabindex="-1">
                                <i class="fa fa-eject"></i> Annuler
                            </button>
                        </openmrs:hasPrivilege>
                    </c:if>
                </c:if>

                <c:if test="${productReport.operationStatus != 'NOT_COMPLETED'}">
                    <c:url value="/module/pharmacy/reports/incomplete.form" var="incompleteUrl">
                        <c:param name="reportId" value="${productReport.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-primary btn-sm mr-2" onclick="window.location='${incompleteUrl}'">
                        <i class="fa fa-pen"></i> Editer le rapport
                    </button>
                    <c:if test="${productReport.operationStatus == 'AWAITING_VALIDATION'}">
                        <openmes:hasPrivilege privilege="Validate Report">

                            <c:url value="/module/pharmacy/reports/validate.form" var="validationUrl">
                                <c:param name="reportId" value="${productReport.productOperationId}"/>
                            </c:url>
                            <button class="btn btn-success btn-sm mr-2" onclick="window.location='${validationUrl}'">
                                <i class="fa fa-pen"></i> Valider
                            </button>
                        </openmes:hasPrivilege>
                    </c:if>
                </c:if>
            </c:if>
            <c:if test="${productReport.operationStatus == 'NOT_COMPLETED'}">
                <c:url value="/module/pharmacy/reports/edit.form" var="editUrl">
                    <c:param name="id" value="${productReport.productOperationId}"/>
                </c:url>
                <button class="btn btn-primary btn-sm" onclick="window.location='${editUrl}'">
                    <i class="fa fa-edit"></i> Editer l'ent&ecirc;te
                </button>
            </c:if>
            <c:url value="/module/pharmacy/reports/list.form" var="url"/>
            <button class="btn btn-primary btn-sm" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <table class="bg-light table table-borderless table-light border">
                <thead class="thead-light">
                <tr>
                    <td>Programme </td>
                    <td class="font-weight-bold text-info">${productReport.productProgram.name}</td>
                    <td>P&eacute;riode </td>
                    <td class="font-weight-bold text-info">${productReport.reportPeriod}</td>
                </tr>
                <tr>
                    <td>Date du rapport</td>
                    <td class="font-weight-bold text-info">
                        <fmt:formatDate value="${productReport.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                    </td>
                    <td>Urgent</td>
                    <td class="font-weight-bold text-info">${productReport.urgent ==true ? 'Oui' : 'Non'}</td>
                </tr>
                </thead>
            </table>
            <c:if test="${((fct:length(reportData) == 0 || canImport == true)
                            && productReport.operationStatus == 'NOT_COMPLETED') && isPlatformUser == false}">
                <openmrs:hasPrivilege privilege="Import Report">
                    <form method="POST" enctype="multipart/form-data" class="mb-3"
                          action="${pageContext.request.contextPath}/module/pharmacy/reports/upload.form">
                        <input type="hidden" name="reportId" value="${productReport.productOperationId}">
                        <div class="row">
                            <div class="offset-2 col-6">
                                <div class="custom-file">
                                    <input type="file" class="custom-file-input" id="customFile" name="file">
                                    <label class="custom-file-label" for="customFile">Choisir le fichier pour importation</label>
                                </div>
                            </div>
                            <div class="col-2">
                                <button class="btn btn-success"><i class="fa fa-upload"></i> Importer</button>
                            </div>
                        </div>
                    </form>
                </openmrs:hasPrivilege>
            </c:if>
            <c:if test="${productReport.operationStatus == 'NOT_COMPLETED'}">
                <form:form modelAttribute="reportEntryAttributeFluxForm" method="post" action="" id="form">
                    <form:hidden path="productOperationId"/>
                    <form:hidden path="locationId"/>
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                            <tr class="bg-belize-hole" style="font-size: 11px;">
                                <th colspan="3" style="width: 250px">Produit <span class="required">*</span></th>
                                <th class="text-center">Stock <br>Initial <span class="required">*</span></th>
                                <th class="text-center">Quantite <br>re&ccedil;ue <span class="required">*</span></th>
                                <th class="text-center">Quantite <br>utilis&eacute;e <span class="required">*</span></th>
                                <th class="text-center">Pertes <span class="required">*</span></th>
                                <th class="text-center">Ajustments <span class="required">*</span></th>
                                <th class="text-center">Stock <br>Disponible <span class="required">*</span></th>
                                <th class="text-center">Nombre de <br>jours de <br>rupture <span class="required">*</span></th>
                                <c:if test="${productReport.reportType == 'CLIENT_REPORT'}">
                                    <th style="width: 50px" class="text-center">Nombre de <br>sites ayant <br>connu une<br> rupture</th>
                                    <th style="width: 50px" class="text-center">Consommation <br>Moyenne <br>Mensuelle<br> (CMM)</th>
                                    <th style="width: 50px" class="text-center">Mois de <br>Stock <br>Disponible<br> (MSD)</th>
                                    <th style="width: 60px" class="text-center">Quantite &agrave;<br>commander</th>
                                </c:if>
                                <c:if test="${productReport.reportType == 'NOT_CLIENT_REPORT'}">
                                    <th style="width: 50px" class="text-center">Quantit&eacute; <br>distribu&eacute;e M-1 <span class="required">*</span></th>
                                    <th style="width: 50px" class="text-center">Quantit&eacute; <br>distribu&eacute;e M-2 <span class="required">*</span></th>
                                </c:if>
                                <th style="width: 30px"></th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:if test="${productReport.operationStatus == 'NOT_COMPLETED'}">
                            <tr>
                                <td colspan="13">
                                    <form:errors path="productId"/>
                                    <form:errors path="initialQuantity"/>
                                    <form:errors path="receivedQuantity"/>
                                    <form:errors path="distributedQuantity"/>
                                    <form:errors path="lostQuantity"/>
                                    <form:errors path="adjustmentQuantity"/>
                                    <form:errors path="quantityInStock"/>
                                    <form:errors path="numDaysOfRupture"/>
                                    <c:if test="${productReport.reportType == 'CLIENT_REPORT'}">
                                        <form:errors path="numSitesInRupture" cssClass=""/>
                                        <form:errors path="calculatedAverageMonthlyConsumption" cssClass=""/>
                                        <form:errors path="monthOfStockAvailable" cssClass=""/>
                                        <form:errors path="quantityToOrder" cssClass=""/>
                                    </c:if>
                                    <c:if test="${productReport.reportType == 'NOT_CLIENT_REPORT'}">
                                        <form:errors path="quantityDistributed1monthAgo"/>
                                        <form:errors path="quantityDistributed2monthAgo"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3">
                                    <form:select path="productId" cssClass="form-control s2">
                                        <form:option value="" label=""/>
                                        <form:options items="${products}" itemValue="productId" itemLabel="retailNameWithCode"/>
                                    </form:select>
                                </td>
                                <td><form:input path="initialQuantity" cssClass="text-center form-control form-control-sm" readonly="${countReports}" /></td>
                                <td><form:input path="receivedQuantity" cssClass="text-center form-control form-control-sm"/></td>
                                <td><form:input path="distributedQuantity" cssClass="text-center form-control form-control-sm"/></td>
                                <td><form:input path="lostQuantity" cssClass="text-center form-control form-control-sm"/></td>
                                <td><form:input path="adjustmentQuantity" cssClass="text-center form-control form-control-sm"/></td>
                                <td><form:input path="quantityInStock" cssClass="text-center form-control form-control-sm"/></td>
                                <td><form:input path="numDaysOfRupture" cssClass="text-center form-control form-control-sm"/></td>
                                <c:if test="${productReport.reportType == 'CLIENT_REPORT'}">
                                    <td><form:input path="numSitesInRupture" cssClass="text-center form-control form-control-sm"/></td>
                                    <td><form:input path="calculatedAverageMonthlyConsumption" cssClass="text-center form-control form-control-sm"/></td>
                                    <td><form:input path="monthOfStockAvailable" cssClass="text-center form-control form-control-sm"/></td>
                                    <td><form:input path="proposedQuantity" cssClass="text-center form-control form-control-sm"/></td>
                                </c:if>
                                <c:if test="${productReport.reportType == 'NOT_CLIENT_REPORT'}">
                                    <td><form:input path="quantityDistributed1monthAgo" cssClass="text-center form-control form-control-sm" readonly="${countReports}"/></td>
                                    <td><form:input path="quantityDistributed2monthAgo" cssClass="text-center form-control form-control-sm" readonly="${countReports}"/></td>
                                </c:if>
                                <td>
                                    <button class="btn btn-success">
                                        <c:if test="${not empty reportEntryAttributeFluxForm.productId}">
                                            <i class="fa fa-edit"></i>
                                        </c:if>
                                        <c:if test="${empty reportEntryAttributeFluxForm.productId}">
                                            <i class="fa fa-plus"></i>
                                        </c:if>
                                    </button>
                                </td>
                            </tr>
                        </c:if>
                        <c:forEach var="reportLine" items="${reportData}">
                            <tr class="${reportLine.asserted == false ? 'text-danger font-weight-bold' : ''}" style="font-size: 12px">
                                <td class="align-middle">${reportLine.code}</td>
                                <td class="align-middle">${reportLine.retailName}</td>
                                <td class="align-middle">${reportLine.retailUnit}</td>
                                <td class="text-center align-middle">${reportLine.initialQuantity}</td>
                                <td class="text-center align-middle">${reportLine.receivedQuantity}</td>
                                <td class="text-center align-middle">${reportLine.distributedQuantity}</td>
                                <td class="text-center align-middle">${reportLine.lostQuantity}</td>
                                <td class="text-center align-middle">${reportLine.adjustmentQuantity}</td>
                                <td class="text-center align-middle">${reportLine.quantityInStock}</td>
                                <td class="text-center align-middle">${reportLine.numDaysOfRupture}</td>
                                <c:if test="${productReport.reportType == 'CLIENT_REPORT'}">
                                    <td class="text-center align-middle">${reportLine.numSitesInRupture}</td>
                                    <td class="text-center align-middle">
                                        <fmt:formatNumber type = "number" value = "${reportLine.calculatedAverageMonthlyConsumption}" maxFractionDigits="0" />
                                    </td>
                                    <td class="text-center align-middle" id="${reportLine.code}-MSD"><fmt:formatNumber type = "number" value = "${reportLine.monthOfStockAvailable}" maxFractionDigits="1" /></td>
                                    <td class="text-center align-middle"><span id="${reportLine.code}-QTO" class="badge badge-primary" style="font-size: 13px">${reportLine.proposedQuantity}</span></td>
                                </c:if>
                                <c:if test="${productReport.reportType == 'NOT_CLIENT_REPORT'}">
                                    <td class="text-center align-middle">${reportLine.quantityDistributed1monthAgo}</td>
                                    <td class="text-center align-middle">${reportLine.quantityDistributed2monthAgo}</td>
                                </c:if>
                                    <%--                                Add treatment part hier--%>
                                <c:if test="${productReport.operationStatus == 'NOT_COMPLETED'}">
                                    <td class="align-middle text-center">
                                        <c:url value="/module/pharmacy/reports/editFlux.form" var="editUrl">
                                            <c:param name="reportId" value="${productReport.productOperationId}"/>
                                            <c:param name="productId" value="${reportLine.productId}"/>
                                        </c:url>
                                        <a href="${editUrl}" class="text-info"><i class="fa fa-edit"></i></a>
                                        <c:url value="/module/pharmacy/reports/deleteFlux.form" var="deleteUrl">
                                            <c:param name="reportId" value="${productReport.productOperationId}"/>
                                            <c:param name="productId" value="${reportLine.productId}"/>
                                        </c:url>
                                        <a href="${deleteUrl}" onclick="return confirm('Voulez vous supprimer ce regime ?')" class="text-danger"><i class="fa fa-trash"></i></a>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </form:form>
            </c:if>

            <c:if test="${productReport.operationStatus != 'NOT_COMPLETED'}">
                <table class="table table-condensed table-striped table-sm table-bordered">
                    <thead class="thead-light">
                        <tr class="bg-belize-hole" style="font-size: 11px;">
                            <th colspan="3" style="width: 250px">Produit</th>
                            <th class="text-center">Stock <br>Initial</th>
                            <th class="text-center">Quantite <br>re&ccedil;ue</th>
                            <th class="text-center">Quantite <br>utilis&eacute;e</th>
                            <th class="text-center">Pertes</th>
                            <th class="text-center">Ajustments</th>
                            <th class="text-center">Stock <br>Disponible</th>
                            <th class="text-center">Nombre de <br>jours de <br>rupture</th>
                            <th class="text-center">Nombre de <br>Sites ayant <br>connu une <br>rupture</th>
                            <c:if test="${productReport.reportType == 'CLIENT_REPORT'}">
                                <th style="width: 50px" class="text-center">Nombre de <br>sites ayant <br>connu une<br> rupture</th>
                                <th style="width: 50px" class="text-center">Consommation <br>Moyenne <br>Mensuelle<br> (CMM)</th>
                                <th style="width: 50px" class="text-center">Mois de <br>Stock <br>Disponible<br> (MSD)</th>
                                <th style="width: 60px" class="text-center">Quantite &agrave;<br>commander</th>
                            </c:if>
                            <c:if test="${productReport.reportType == 'NOT_CLIENT_REPORT'}">
                                <th style="width: 50px" class="text-center">Quantit&eacute; <br>distribu&eacute;e M-1</th>
                                <th style="width: 50px" class="text-center">Quantit&eacute; <br>distribu&eacute;e M-2</th>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="reportLine" items="${reportData}">
                        <tr class="${reportLine.asserted == false ? 'text-danger font-weight-bold' : ''}" style="font-size: 12px">
                            <td class="align-middle">${reportLine.code}</td>
                            <td class="align-middle">${reportLine.retailName}</td>
                            <td class="align-middle">${reportLine.retailUnit}</td>
                            <td class="text-center align-middle">${reportLine.initialQuantity}</td>
                            <td class="text-center align-middle">${reportLine.receivedQuantity}</td>
                            <td class="text-center align-middle">${reportLine.distributedQuantity}</td>
                            <td class="text-center align-middle">${reportLine.lostQuantity}</td>
                            <td class="text-center align-middle">${reportLine.adjustmentQuantity}</td>
                            <td class="text-center align-middle">${reportLine.quantityInStock}</td>
                            <td class="text-center align-middle">${reportLine.numDaysOfRupture}</td>
                            <c:if test="${productReport.reportType == 'CLIENT_REPORT'}">
                                <td class="text-center align-middle">${reportLine.numSitesInRupture}</td>
                                <td class="text-center align-middle">
                                    <fmt:formatNumber type = "number" value = "${reportLine.calculatedAverageMonthlyConsumption}" maxFractionDigits="0" />
                                </td>
                                <td class="text-center align-middle" id="${reportLine.code}-MSD"><fmt:formatNumber type = "number" value = "${reportLine.monthOfStockAvailable}" maxFractionDigits="1" /></td>
                                <td class="text-center align-middle"><span id="${reportLine.code}-QTO" class="badge badge-primary" style="font-size: 13px">${reportLine.quantityToOrder}</span></td>
                            </c:if>
                            <c:if test="${productReport.reportType == 'NOT_CLIENT_REPORT'}">
                                <td class="text-center align-middle">${reportLine.quantityDistributed1monthAgo}</td>
                                <td class="text-center align-middle">${reportLine.quantityDistributed2monthAgo}</td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>

</div>

<%@ include file="../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
