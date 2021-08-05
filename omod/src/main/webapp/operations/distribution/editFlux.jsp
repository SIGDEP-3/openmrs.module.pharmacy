<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../template/operationHeader.jsp"%>
<openmrs:require privilege="Save Report" otherwise="/login.htm" redirect="/module/pharmacy/operations/distribution/editFlux.form" />

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

            jQuery('.input-value-flux').on('change', function () {
                const inputSelected = jQuery(this);
                const quantity = inputSelected.val();
                const product = inputSelected.attr('id');
                const code = product.split('-')[0];
                const quantityInStock = jQuery("#" + code + "-quantityInStock").text();
                jQuery("#" + code + "-quantityInStockAfter").text(quantityInStock - quantity);
                saveFlux(code, quantity);
            });
        });

        function saveFlux(code, quantity) {
            if (quantity >= 0) {
                jQuery.ajax({
                    type: 'GET',
                    url: '${pageContext.request.contextPath}/save-distribution-flux.form?code=' +
                        code + '&operationId=${productDistribution.productOperationId}&quantity='+ quantity,
                    dataType : "json",
                    crossDomain:true,
                    success : function(data) {
                        console.log(data);
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
            <div class="h6"><i class="fa fa-pen-square"></i> ${subTitle}
                <c:if test="${slip == null}">
                    ${productDistribution.urgent == true ? '<span class="text-danger">(URGENT)</span>' : ''}
                </c:if>
            </div>
        </div>
        <div class="col-8 text-right">

            <c:if test="${productDistribution.operationStatus != 'VALIDATED' &&
                      productDistribution.operationStatus != 'DISABLED'}">
                <c:if test="${invalidReport != true}">
                    <c:if test="${productDistribution.operationStatus == 'NOT_COMPLETED' &&
                                    isAsserted == true && fct:length(reportData) != 0 }">
                        <c:url value="/module/pharmacy/operations/distribution/complete.form" var="completeUrl">
                            <c:param name="distributionId" value="${productDistribution.productOperationId}"/>
                        </c:url>
                        <button class="btn btn-success btn-sm mr-2" onclick="window.location='${completeUrl}'">
                            <i class="fa fa-save"></i> Terminer
                        </button>

                        <openmrs:hasPrivilege privilege="Delete Report">
                            <c:url value="/module/pharmacy/operations/distribution/delete.form"
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


                <c:if test="${productDistribution.operationStatus != 'NOT_COMPLETED' && productDistribution.operationStatus != 'TREATED'}">
                    <c:url value="/module/pharmacy/operations/distribution/incomplete.form" var="incompleteUrl">
                        <c:param name="distributionId" value="${productDistribution.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-primary btn-sm mr-2" onclick="window.location='${incompleteUrl}'">
                        <i class="fa fa-pen"></i> Editer le rapport
                    </button>
                    <%--                    <c:if test="${productDistribution.operationStatus == 'AWAITING_VALIDATION'}">--%>
                    <%--                        <openmes:hasPrivilege privilege="Validate Report">--%>

                    <%--                            <c:url value="/module/pharmacy/operations/distribution/validate.form" var="validationUrl">--%>
                    <%--                                <c:param name="reportId" value="${productDistribution.productOperationId}"/>--%>
                    <%--                            </c:url>--%>
                    <%--                            <button class="btn btn-success btn-sm mr-2" onclick="window.location='${validationUrl}'">--%>
                    <%--                                <i class="fa fa-pen"></i> Valider--%>
                    <%--                            </button>--%>
                    <%--                        </openmes:hasPrivilege>--%>
                    <%--                    </c:if>--%>
                </c:if>
            </c:if>
            <c:if test="${productDistribution.operationStatus == 'AWAITING_TREATMENT' }">
                <openmes:hasPrivilege privilege="Submit Report">
                    <c:url value="/module/pharmacy/operations/distribution/validate.form" var="submissionUrl">
                        <c:param name="distributionId" value="${productDistribution.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-info btn-sm mr-2" onclick="window.location='${submissionUrl}'">
                        <i class="fa fa-pen"></i> Valider le traitement
                    </button>
                </openmes:hasPrivilege>
            </c:if>
            <c:if test="${productDistribution.operationStatus == 'NOT_COMPLETED'}">
                <c:url value="/module/pharmacy/operations/distribution/edit.form" var="editUrl">
                    <c:param name="id" value="${productDistribution.productOperationId}"/>
                </c:url>
                <button class="btn btn-primary btn-sm" onclick="window.location='${editUrl}'">
                    <i class="fa fa-edit"></i> Editer l'ent&ecirc;te
                </button>
            </c:if>
            <c:url value="/module/pharmacy/operations/distribution/list.form" var="url"/>
            <button class="btn btn-primary btn-sm" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border ${productDistribution.urgent == true ? 'border-danger' : 'border-secondary'}">
        <div class="col-12">
            <table class="bg-light table table-borderless table-light border">
                <thead class="thead-light">
                <tr>
                    <td>Programme </td>
                    <td class="font-weight-bold text-info">${productDistribution.productProgram.name}</td>
                    <c:if test="${slip == null}">
                        <td>P&eacute;riode </td>
                        <td class="font-weight-bold text-info">${productDistribution.reportPeriod}</td>
                    </c:if>
                    <c:if test="${slip != null}">
                        <td>Bordereau de livraison </td>
                        <td class="font-weight-bold text-info">${productDistribution.operationNumber}</td>
                    </c:if>
                </tr>
                <tr>
                    <c:if test="${slip == null}">
                        <td>Centre / PPS </td>
                    </c:if>
                    <c:if test="${slip != null}">
                        <td>Centre de destination </td>
                    </c:if>
                    <td class="font-weight-bold text-info">${productDistribution.reportLocation.name}</td>
                    <c:if test="${slip == null}">
                        <td>Date du rapport</td>
                        <td class="font-weight-bold text-info">
                            <fmt:formatDate value="${productDistribution.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                        </td>
                    </c:if>
                    <c:if test="${slip != null}">
                        <td>Date d'&eacute;dition</td>
                        <td class="font-weight-bold text-info">
                            <fmt:formatDate value="${productDistribution.treatmentDate}" pattern="dd/MM/yyyy" type="DATE"/>
                        </td>
                    </c:if>
                </tr>
<%--                <tr>--%>
<%--                    <c:if test="${slip == null}">--%>
<%--                        <td>Urgent</td>--%>
<%--                        <td class="font-weight-bold text-info">${productDistribution.urgent == true ? 'Oui' : 'Non'}</td>--%>
<%--                    </c:if>--%>

<%--                </tr>--%>
                </thead>
            </table>
            <c:if test="${(fct:length(reportData) == 0 || canImport == true)
                            && productDistribution.childLocationReport.operationStatus == 'NOT_COMPLETED'}">
                <openmrs:hasPrivilege privilege="Import Report">
                    <form method="POST" enctype="multipart/form-data" class="mb-3"
                          action="${pageContext.request.contextPath}/module/pharmacy/operations/distribution/upload.form">
                        <input type="hidden" name="distributionId" value="${productDistribution.productOperationId}">
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
            <c:if test="${productDistribution.operationStatus == 'NOT_COMPLETED'}">
                <form:form modelAttribute="distributionAttributeFluxForm" method="post" action="" id="form">
                    <form:hidden path="productOperationId"/>
                    <form:hidden path="locationId"/>
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                        <c:if test="${productDistribution.childLocationReport.operationStatus == 'NOT_COMPLETED'}">
                            <tr class="bg-belize-hole" style="font-size: 11px;">
                                <th colspan="3" style="width: 250px">Produit <span class="required">*</span></th>
                                <th class="text-center">Stock <br>Initial <span class="required">*</span></th>
                                <th class="text-center">Quantite <br>re&ccedil;ue <span class="required">*</span></th>
                                <th class="text-center">Quantite <br>utilis&eacute;e <span class="required">*</span></th>
                                <th class="text-center">Pertes <span class="required">*</span></th>
                                <th class="text-center">Ajustments <span class="required">*</span></th>
                                <th class="text-center">Stock <br>Disponible <span class="required">*</span></th>
                                <th class="text-center">Nombre de <br>jours de <br>rupture <span class="required">*</span></th>
                                <th class="text-center">Quantit&eacute; <br>distribu&eacute;e M-1 <span class="required">*</span></th>
                                <th class="text-center">Quantit&eacute; <br>distribu&eacute;e M-2 <span class="required">*</span></th>
                                <th style="width: 30px"></th>
                            </tr>
                        </c:if>
                        <c:if test="${productDistribution.childLocationReport.operationStatus != 'NOT_COMPLETED'}">
                            <tr class="bg-belize-hole" style="font-size: 11px;">
                                <th colspan="3" style="width: 250px">Produit</th>
                                <th class="text-center">Stock <br>Initial</th>
                                <th class="text-center">Quantite <br>re&ccedil;ue</th>
                                <th class="text-center">Quantite <br>utilis&eacute;e</th>
                                <th class="text-center">Pertes</th>
                                <th class="text-center">Ajustments</th>
                                <th class="text-center">Stock <br>Disponible</th>
                                <th class="text-center">Nombre de <br>jours de <br>rupture</th>
                                <th class="text-center">Quantit&eacute; <br>distribu&eacute;e M-1</th>
                                <th class="text-center">Quantit&eacute; <br>distribu&eacute;e M-2</th>
                            </tr>
                        </c:if>
                        </thead>
                        <tbody>
                        <c:if test="${productDistribution.childLocationReport.operationStatus == 'NOT_COMPLETED'}">
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
                                    <form:errors path="quantityDistributed1monthAgo"/>
                                    <form:errors path="quantityDistributed2monthAgo"/>
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
                                <td><form:input path="receivedQuantity" cssClass="text-center form-control form-control-sm" readonly="${hasPreviousTreatment}"/></td>
                                <td><form:input path="distributedQuantity" cssClass="text-center form-control form-control-sm"/></td>
                                <td><form:input path="lostQuantity" cssClass="text-center form-control form-control-sm"/></td>
                                <td><form:input path="adjustmentQuantity" cssClass="text-center form-control form-control-sm"/></td>
                                <td><form:input path="quantityInStock" cssClass="text-center form-control form-control-sm"/></td>
                                <td><form:input path="numDaysOfRupture" cssClass="text-center form-control form-control-sm"/></td>
                                <td><form:input path="quantityDistributed1monthAgo" cssClass="text-center form-control form-control-sm" readonly="${countReports}"/></td>
                                <td><form:input path="quantityDistributed2monthAgo" cssClass="text-center form-control form-control-sm" readonly="${countReports}"/></td>
                                <td>
                                    <button class="btn btn-success">
                                        <c:if test="${not empty distributionAttributeFluxForm.productId}">
                                            <i class="fa fa-edit"></i>
                                        </c:if>
                                        <c:if test="${empty distributionAttributeFluxForm.productId}">
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
                                <td class="text-center align-middle">${reportLine.quantityDistributed1monthAgo}</td>
                                <td class="text-center align-middle">${reportLine.quantityDistributed2monthAgo}</td>
                                    <%--                                Add treatment part hier--%>
                                <c:if test="${productDistribution.childLocationReport.operationStatus == 'NOT_COMPLETED'}">
                                    <td class="align-middle text-center">
                                        <c:url value="/module/pharmacy/operations/distribution/editFlux.form" var="editUrl">
                                            <c:param name="distributionId" value="${productDistribution.productOperationId}"/>
                                            <c:param name="productId" value="${reportLine.productId}"/>
                                        </c:url>
                                        <a href="${editUrl}" class="text-info"><i class="fa fa-edit"></i></a>
                                        <c:url value="/module/pharmacy/operations/distribution/deleteFlux.form" var="deleteUrl">
                                            <c:param name="distributionId" value="${productDistribution.productOperationId}"/>
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

            <c:if test="${productDistribution.operationStatus != 'NOT_COMPLETED'}">
                <c:if test="${(productDistribution.operationStatus == 'AWAITING_TREATMENT' ||
                               productDistribution.operationStatus == 'TREATED'||
                               productDistribution.operationStatus == 'VALIDATED') && slip == null  }">
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                        <c:if test="${productDistribution.childLocationReport.operationStatus != 'NOT_COMPLETED'}">
                            <tr class="bg-belize-hole" style="font-size: 11px;">
                                <th colspan="3" style="width: 250px">Produit</th>
                                <th class="text-center">Stock <br>Initial</th>
                                <th class="text-center">Quantite <br>re&ccedil;ue</th>
                                <th class="text-center">Quantite <br>utilis&eacute;e</th>
                                <th class="text-center">Pertes</th>
                                <th class="text-center">Ajustments</th>
                                <th class="text-center">Stock <br>Disponible</th>
                                <th class="text-center">Nombre de <br>jours de <br>rupture</th>
                                <th class="text-center">Quantit&eacute; <br>distribu&eacute;e <br>M-1</th>
                                <th class="text-center">Quantit&eacute; <br>distribu&eacute;e <br>M-2</th>
                                <th class="text-center">CMM</th>
                                <th class="text-center">MSD</th>
                                <th class="text-center" style="width: 60px">Quantit&eacute; <br>propos&eacute;e</th>
                                <th class="text-center" style="width: 90px">Quantit&eacute; <br>approuv&eacute;e</th>
                                <c:if test="${productDistribution.operationStatus != 'VALIDATED'}">
                                    <th class="text-center" style="width: 90px">Quantit&eacute; <br>apr&egrave;s <br>validation</th>
                                    <th class="text-center">Quantit&eacute; <br>en stock</th>
                                </c:if>
                            </tr>
                        </c:if>
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
                                <td class="text-center align-middle">${reportLine.quantityDistributed1monthAgo}</td>
                                <td class="text-center align-middle">${reportLine.quantityDistributed2monthAgo}</td>
                                <td class="text-center align-middle"><fmt:formatNumber type = "number" value = "${reportLine.calculatedAverageMonthlyConsumption}" maxFractionDigits="0" /></td>
                                <td class="text-center align-middle">
                                    <fmt:formatNumber type = "number" value = "${reportLine.monthOfStockAvailable}" maxFractionDigits="1" />
                                </td>
                                <td class="text-center align-middle">
                                    <fmt:formatNumber type = "number" value = "${reportLine.proposedQuantity}" maxFractionDigits="0" />
                                </td>
                                <td class="text-center align-middle">
                                    <c:if test="${productDistribution.operationStatus != 'VALIDATED'}">
                                        <input type="text" value="${reportLine.accordedQuantity}"
                                               id="${reportLine.code}-quantityApproved"
                                               class="form-control form-control-sm text-center input-value-flux" ${reportLine.parentQuantityInStock == 0 ? 'readonly="readonly"' : ''}>
                                    </c:if>
                                    <c:if test="${productDistribution.operationStatus == 'VALIDATED'}">
                                        ${reportLine.accordedQuantity}
                                    </c:if>
                                </td>
                                <c:if test="${productDistribution.operationStatus != 'VALIDATED'}">
                                    <td class="text-center align-middle font-weight-bold" id="${reportLine.code}-quantityInStockAfter">${reportLine.parentQuantityInStock - reportLine.accordedQuantity}</td>
                                    <td class="text-center align-middle font-weight-bold" id="${reportLine.code}-quantityInStock">${reportLine.parentQuantityInStock}</td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${(productDistribution.operationStatus == 'VALIDATED') && slip == 'true'  }">
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                        <tr class="bg-belize-hole">
                            <th colspan="3" style="width: 250px">Produit</th>
                            <th style="width: 200px">Num&eacute;ro de lot </th>
                            <th style="width: 150px">Date de p&eacute;remption </th>
                            <th style="width: 60px">Quantit&eacute;</th>
                            <th style="width: 100px">observation</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="productFlux" items="${productAttributeFluxes}">
                            <tr>
                                <td>${productFlux.productAttribute.product.code}</td>
                                <td>${productFlux.productAttribute.product.retailName}</td>
                                <td>${productFlux.productAttribute.product.productRetailUnit.name}</td>
                                <td class="text-center">${productFlux.productAttribute.batchNumber}</td>
                                <td class="text-center">
                                    <fmt:formatDate value="${productFlux.productAttribute.expiryDate}" pattern="dd/MM/yyyy"
                                                    type="DATE"/>
                                </td>
                                <td class="text-center">${productFlux.quantity}</td>
                                <td>${productFlux.observation}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </c:if>
        </div>
    </div>

</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
