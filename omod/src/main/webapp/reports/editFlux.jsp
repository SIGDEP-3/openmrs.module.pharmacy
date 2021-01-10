<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../template/operationHeader.jsp"%>
<openmrs:require privilege="Save Report" otherwise="/login.htm" redirect="/module/pharmacy/reports/editFlux.form" />

<script>
    if (jQuery) {

        jQuery(document).ready(function (){
            jQuery('.input-value-flux').on('change', function () {
                const inputSelected = jQuery(this);
                const quantity = inputSelected.val();
                const product = inputSelected.attr('id');
                const code = product.split('-')[0];
                const label = product.split('-')[1];
                saveOtherFlux(code, quantity, label);

                if (label === "CMM") {
                    let quantityInStock = jQuery("#" + code + "-SDU").text();
                    let quantityToOrder = (${stockMax} * quantity) - quantityInStock;
                    saveOtherFlux(code, quantityToOrder, "QTO");
                    jQuery("#" + code + "-QTO").text(quantityToOrder);
                    if (quantity > 0) {
                        jQuery("#" + code + "-MSD").text(Math.round(quantityInStock / quantity).toFixed(1));
                    }
                }
            });

            jQuery('.input-observation').on('change', function () {
                const inputSelected = jQuery(this);
                const observation = inputSelected.val();
                const batchNumber = inputSelected.attr('id');
                addObservation(batchNumber, observation);
            });

        });

        function saveOtherFlux(code, quantity, label) {
            if (quantity !== 0) {
                jQuery.ajax({
                    type: 'GET',
                    url: '${pageContext.request.contextPath}/save-other-flux.form?code=' +
                        code + '&operationId=${productReport.productOperationId}&quantity='+ quantity +'&label=' + label,
                    dataType : "json",
                    crossDomain:true,
                    success : function(data) {
                        //console.log(data);
                        //jQuery('.' + batchNumber).parent().html(data.quantity)
                        //alert(data.toString());
                    },
                    error : function(data) {
                        console.log(data)
                    }
                });
            }

        }
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
                    <c:if test="${productReport.operationStatus == 'NOT_COMPLETED'}">
                        <c:url value="/module/pharmacy/rports/complete.form" var="completeUrl">
                            <c:param name="reportId" value="${productReport.productOperationId}"/>
                        </c:url>
                        <button class="btn btn-success btn-sm mr-2" onclick="window.location='${completeUrl}'">
                            <i class="fa fa-save"></i> Terminer
                        </button>
                    </c:if>
                </c:if>


                <c:if test="${productReport.operationStatus != 'NOT_COMPLETED'}">
                    <c:url value="/module/pharmacy/reports/incomplete.form" var="incompleteUrl">
                        <c:param name="reportId" value="${productReport.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-primary btn-sm mr-2" onclick="window.location='${incompleteUrl}'">
                        <i class="fa fa-pen"></i> Editer le rapport
                    </button>
                    <c:if test="${invalidReport != true}">
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
            <c:if test="${productReport.operationStatus == 'VALIDATED' && isDirectClient == false}">
                <openmes:hasPrivilege privilege="Submit Report">
                    <c:url value="/module/pharmacy/reports/submit.form" var="submissionUrl">
                        <c:param name="reportId" value="${productReport.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-info btn-sm mr-2" onclick="window.location='${submissionUrl}'">
                        <i class="fa fa-pen"></i> Soumettre
                    </button>
                </openmes:hasPrivilege>

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
                <td class="font-weight-bold text-info">${productReport.urgent}</td>
            </tr>
            <tr>

                <td colspan="2">Observation</td>
                <td class="font-weight-bold text-info">${productReport.observation}</td>
            </tr>
            </thead>
        </table>
        <table class="table table-condensed table-striped table-sm table-bordered">
            <thead class="thead-light">
            <tr class="bg-belize-hole" style="font-size: 11px;">
                <th style="width: 50px" class="text-center">Code</th>
                <th style="width: 200px">Produit</th>
                <th style="width: 50px" class="text-center">Unit&eacute;</th>
                <th style="width: 50px" class="text-center">Stock <br>Initial</th>
                <th style="width: 60px" class="text-center">Quantite <br>re&ccedil;ue</th>
                <th style="width: 60px" class="text-center">Quantite <br>utilis&eacute;e</th>
                <th style="width: 50px" class="text-center">Pertes</th>
                <th style="width: 50px" class="text-center">Ajustments</th>
                <th style="width: 50px" class="text-center">Stock <br>Disponible</th>
                <th style="width: 50px" class="text-center">Nombre de <br>jours de <br>rupture</th>
                <c:if test="${productReport.reportType == 'CLIENT_REPORT'}">
                    <th style="width: 50px" class="text-center">Nombre de <br>sites ayant <br>connu une<br> rupture</th>
                </c:if>
                <c:if test="${productReport.reportType == 'NOT_CLIENT_REPORT'}">
                    <th style="width: 50px" class="text-center">Quantit&eacute; <br>distribu&eacute;e M-2</th>
                    <th style="width: 50px" class="text-center">Quantit&eacute; <br>distribu&eacute;e M-1</th>
                </c:if>
                <c:if test="${productReport.reportType == 'CLIENT_REPORT'}">
                    <th style="width: 50px" class="text-center">Consommation <br>Moyenne <br>Mensuelle<br> (CMM)</th>
                    <th style="width: 50px" class="text-center">Mois de <br>Stock <br>Disponible<br> (MSD)</th>
                    <th style="width: 60px" class="text-center">Quantite &agrave;<br>commander</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="reportLine" items="${reportData}">
                <tr class="${reportLine.asserted == false ? 'bg-danger text-white' : ''}" style="font-size: 12px">
                    <td class="align-middle">${reportLine.code}</td>
                    <td class="align-middle">${reportLine.retailName}</td>
                    <td class="align-middle">${reportLine.retailUnit}</td>
                    <td class="text-center align-middle">${reportLine.initialQuantity}</td>
                    <td class="text-center align-middle">${reportLine.receivedQuantity}</td>
                    <td class="text-center align-middle">${reportLine.distributedQuantity}</td>
                    <td class="text-center align-middle">${reportLine.lostQuantity}</td>
                    <td class="text-center align-middle">${reportLine.adjustmentQuantity}</td>
                    <td class="text-center align-middle" id="${reportLine.code}-SDU">${reportLine.quantityInStock}</td>
                    <td class="text-center align-middle">
                        <c:if test="${productReport.operationStatus == 'NOT_COMPLETED'}">
                            <label for="${reportLine.code}-NDR" class="sr-only"></label>
                            <input id="${reportLine.code}-NDR" value="${reportLine.numDaysOfRupture}"
                                                                               type="text" name="${reportLine.numDaysOfRupture}-NDR"
                                                                               class=".input-value-flux form-control form-control-sm text-center">
<%--                            <c:if test="${reportLine.numDaysOfRupture == 0 }">--%>
<%--                            </c:if>--%>
<%--                            <c:if test="${reportLine.numDaysOfRupture != 0}">--%>
<%--                                ${reportLine.numDaysOfRupture}--%>
<%--                            </c:if>--%>
                        </c:if>
                        <c:if test="${productReport.operationStatus != 'NOT_COMPLETED'}">
                            ${reportLine.numDaysOfRupture}
                        </c:if>
                    </td>
                    <c:if test="${productReport.reportType == 'CLIENT_REPORT'}">
                        <td class="text-center align-middle">
                            <c:if test="${productReport.operationStatus == 'NOT_COMPLETED'}">
                                <c:if test="${reportLine.numSitesInRupture == 0}">
                                    <label for="${reportLine.code}-NSR" class="sr-only"></label>
                                    <input id="${reportLine.code}-NSR" type="text"
                                           name="${reportLine.code}-NSR"
                                           value="${reportLine.numSitesInRupture}"
                                           class="input-value-flux form-control form-control-sm text-center">
                                </c:if>
                                <c:if test="${reportLine.numSitesInRupture != 0}">
                                    ${reportLine.numSitesInRupture}
                                </c:if>
                            </c:if>
                            <c:if test="${productReport.operationStatus != 'NOT_COMPLETED'}">
                                ${reportLine.numSitesInRupture}
                            </c:if>
                        </td>
                    </c:if>
                    <c:if test="${productReport.reportType == 'NOT_CLIENT_REPORT'}">
                        <td class="text-center align-middle">
                            <c:if test="${productReport.operationStatus == 'NOT_COMPLETED'}">
                                <c:if test="${reportLine.quantityDistributed2monthAgo == 0}">
                                    <input id="${reportLine.code}-DM2" type="text" name="${reportLine.code}-DM2" class="input-value-flux form-control form-control-sm text-center">
                                </c:if>
                                <c:if test="${reportLine.quantityDistributed2monthAgo != 0}">
                                    ${reportLine.quantityDistributed2monthAgo}
                                </c:if>
                            </c:if>
                            <c:if test="${productReport.operationStatus != 'NOT_COMPLETED'}">
                                ${reportLine.quantityDistributed2monthAgo}
                            </c:if>
                        </td>
                        <td class="text-center align-middle">
                            <c:if test="${productReport.operationStatus == 'NOT_COMPLETED'}">
                                <c:if test="${reportLine.quantityDistributed1monthAgo == 0}">
                                    <input id="${reportLine.code}-DM1" type="text" name="${reportLine.code}-DM1" class="input-value-flux form-control form-control-sm text-center">
                                </c:if>
                                <c:if test="${reportLine.quantityDistributed1monthAgo != 0}">
                                    ${reportLine.quantityDistributed1monthAgo}
                                </c:if>
                            </c:if>
                            <c:if test="${productReport.operationStatus != 'NOT_COMPLETED'}">
                                ${reportLine.quantityDistributed1monthAgo}
                            </c:if>
                        </td>
                    </c:if>
                    <c:if test="${productReport.reportType == 'CLIENT_REPORT'}">
                        <td class="text-center align-middle">
                            <label for="${reportLine.code}-CMM" class="sr-only">CMM</label>
                            <div class="input-group input-group-sm">
                                <div class="input-group-prepend">
                                    <span class="input-group-text bg-primary text-white" id="${reportLine.code}" title="CMM auto">${reportLine.calculatedAverageMonthlyConsumption}</span>
                                </div>
                                <input type="text"
                                       id="${reportLine.code}-CMM"
                                       class="input-value-flux form-control form-control-sm text-center"
                                       value="${reportLine.averageMonthlyConsumption}" size="15"
                                       aria-describedby="${reportLine.code}-CMM">
                            </div>
                        </td>
                        <td class="text-center align-middle" id="${reportLine.code}-MSD"><fmt:formatNumber type = "number" value = "${reportLine.monthOfStockAvailable}" maxFractionDigits="1" /></td>
                        <td class="text-center align-middle"><span id="${reportLine.code}-QTO" class="badge badge-primary" style="font-size: 13px">${reportLine.quantityToOrder}</span></td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</div>

<%@ include file="../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
