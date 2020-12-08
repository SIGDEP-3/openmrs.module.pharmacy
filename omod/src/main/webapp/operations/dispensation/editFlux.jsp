<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacy/operations/dispensation/editFlux.form" />

<%@ include file="../../template/operationHeader.jsp"%>
<%--<script src="<openmrs:contextPath/>/module/pharmacy/web/DWRPharmacyService.js"></script>--%>
<script>
    if (jQuery) {
        jQuery(document).ready(function () {
            // jQuery('#productId').on('change', function (e) {
            //     let productId = jQuery(this).val();
            //     if (productId) {
            //         DWRPharmacyService.getProductQuantityInStock(productId, function (data) {
            //             alert(data);
            //         });
            //     }
            // });
           getRemainingQuantity();

            jQuery('#dispensingQuantity').on('change', function (e) {
                getRemainingQuantity();
            });


        });

        function getRemainingQuantity() {
            let dispensingQuantity = jQuery('#dispensingQuantity').val();
            let quantityRemaining = jQuery('#quantityRemaining');
            let quantityInStock = jQuery('#quantityInStock').text();
            if (dispensingQuantity != null) {
                let quantity = quantityInStock - dispensingQuantity;
                quantityRemaining.removeClass('text-danger');
                quantityRemaining.removeClass('text-warning');
                quantityRemaining.addClass('text-success');
                jQuery('#button-submit').show();
                quantityRemaining.text(quantity)
                if (quantity === 0) {
                    quantityRemaining.removeClass('text-success');
                    quantityRemaining.addClass('text-warning');
                } else if (quantity < 0) {
                    quantityRemaining.removeClass('text-success');
                    quantityRemaining.addClass('text-danger');
                    jQuery('#button-submit').hide()
                }
            } else {
                quantityRemaining.text(quantityInStock);
            }
        }

        function goToSelectedProduct() {
            let productId = jQuery('#selectedProductId').val();
            if (productId) {
                location.href = '${pageContext.request.contextPath}/module/pharmacy/operations/dispensation/editFlux.form?' +
                    'dispensationId=' + ${dispensationId} + '&selectedProductId=' + productId
            }
        }
        //jQuery.ajax('/')
    }
</script>

<script>

</script>
<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6">
            <div class="h5"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <div class="row">
                <div class="col-12">
                    <fieldset>
                        <legend>
                            <div class="row">
                                <div class="col-8">
                                    Programme : <span class="text-success text-lg-left font-italic">${headerDTO.productProgram.name}</span> |
                                    Patient (<span class="text-info font-italic">${headerDTO.patientType == 'ON_SITE' ?
                                        'PEC SUR LE SITE' : (headerDTO.patientType == 'MOBILE' ? 'MOBILE' : 'PREVENTION')}</span>)
                                </div>
                                <div class="col-4 text-right">
                                    <button class="btn btn-primary btn-sm" tabindex="-1">
                                        <i class="fa fa-tablets"></i> Enregistrer
                                    </button>
                                    <button type="button" class="btn btn-warning btn-sm" onclick="" tabindex="-1">
                                        <i class="fa fa-eject"></i> Annuler
                                    </button>
                                </div>
                            </div>
                        </legend>
                        <div class="card">
                            <div class="card-body p-2 border border-info">
                                <div class="row">
                                    <div class="col-4">
                                        <h6 class="font-italic text-warning h6">Information du Patient</h6>
                                        <div class="card bg-light">
                                            <div class="card-body p-2">
                                                <div class="row mb-2">
                                                    <div class="col-12">
                                                        <label class="">Num&eacute;ro</label>
                                                        <div class="form-control form-control-sm bg-info text-white">
                                                            ${headerDTO.patientIdentifier}
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row mb-1">
                                                    <div class="col-4">
                                                        <label class="">Age</label>
                                                        <div class="form-control form-control-sm bg-info text-white">
                                                            ${headerDTO.age}
                                                        </div>
                                                    </div>
                                                    <div class="col-8">
                                                        <label class="mb-2">Genre</label>
                                                        <br>
                                                        <span class="mr-3 text-info">
                                                           ${headerDTO.gender == 'M' ? '&ofcir;' : '&cir;'} &nbsp;&nbsp; Masculin </span>
                                                        <span class="text-info">
                                                            ${headerDTO.gender == 'F' ? '&ofcir;' : '&cir;'} &nbsp;&nbsp; F&eacute;minin
                                                        </span>

                                                    </div>
                                                </div>
                                                <div class="row mb-2">
                                                    <div class="col-12">
                                                        <label class="">R&eacute;gime</label>
                                                        <div class="form-control form-control-sm bg-info text-white">
                                                            ${headerDTO.productRegimen.concept.name.name}
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <h6 class="font-italic text-warning">Derni&egrave;re Dispensation</h6>
                                        <div class="card bg-light text-info border border-info">
                                            <div class="card-body p-2">
                                                <c:if test="${headerDTO.patientType == 'OTHER_HIV'}">
                                                    <div class="row align-items-center">
                                                        <div class="h4 text-warning font-italic font-weight-bold">Non Applicable</div>
                                                    </div>
                                                </c:if>
                                                <c:if test="${headerDTO.patientType == 'MOBILE'}">
                                                    <div class="row align-items-center">
                                                        Ce patient a d&eacute;j&agrave; effectu&eacute;
                                                        <span class="font-weight-bold text-primary">${fct:length(mobilePatient.mobilePatientDispensationInfos)}</span>
                                                        visite(s)

                                                    </div>
                                                </c:if>
                                                <c:if test="${headerDTO.patientType == 'ON_SITE'}">
                                                    <div class="row mb-1">
                                                        <div class="col-12">
                                                            <label>Date</label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row mb-2">
                                                        <div class="col-6">
                                                            <label>R&eacute;gime</label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                            </div>
                                                        </div>
                                                        <div class="col-6">
                                                            <label>Nb jours perdus</label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row mb-2">
                                                        <div class="col-5">
                                                            <label>Nb jours TTT</label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                            </div>
                                                        </div>
                                                        <div class="col-6">
                                                            <label>Date de fin de TTT</label>
                                                            <div id="lastTreatmentEndDate" class="form-control form-control-sm bg-info text-white">

                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="col-5">
                                        <h6 class="font-italic text-warning h6">Information de dispensation</h6>
                                        <div class="card bg-light">
                                            <div class="card-body p-2">
                                                <div class="row mb-3">
                                                    <div class="col-12">
                                                        <label class="mb-1">But</label>
                                                        <br>
                                                        <c:if test="${headerDTO.patientType == 'ON_SITE' || headerDTO.patientType == 'MOBILE'}">
                                                        <span class="mr-3 text-info">
                                                            ${headerDTO.goal == 'NOT_APPLICABLE' ? '&ofcir;' : '&cir;'}&nbsp;&nbsp;Non Applicable </span>
                                                            <span class="mr-3 text-info">
                                                            ${headerDTO.goal == 'PEC' ? '&ofcir;' : '&cir;'} &nbsp;&nbsp;PEC </span>
                                                            <span class="mr-3 text-info">
                                                            ${headerDTO.goal == 'PTME' ? '&ofcir;' : '&cir;'} &nbsp;&nbsp;PTME </span>
                                                        </c:if>
                                                        <c:if test="${headerDTO.patientType == 'OTHER_HIV'}">
                                                        <span class="text-info">
                                                            ${headerDTO.goal == 'AES' ? '&ofcir;' : '&cir;'}&nbsp;&nbsp;AES</span>
                                                        <span class="text-info">
                                                            ${headerDTO.goal == 'PREP' ? '&ofcir;' : '&cir;'}&nbsp;&nbsp;PREP</span>
                                                        </c:if>
                                                    </div>
                                                </div>
                                                <div class="row mb-2">
                                                    <c:if test="${headerDTO.patientType == 'ON_SITE'}">
                                                        <div class="col-5">
                                                            <label class="">Date de dispensation</label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                                <fmt:formatDate value="${headerDTO.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                                            </div>
                                                        </div>
                                                        <div class="col-3">
                                                            <label class="">Nb Jours TTT </label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                                    ${headerDTO.treatmentDays}
                                                            </div>
                                                        </div>
                                                        <div class="col-4">
                                                            <label class="">Date de fin de TTT </label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                                <fmt:formatDate value="${headerDTO.treatmentEndDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${headerDTO.patientType == 'OTHER_HIV' || headerDTO.patientType == 'MOBILE'}">
                                                        <div class="col-5">
                                                            <label class="">Date de dispensation</label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                                <fmt:formatDate value="${headerDTO.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                                            </div>
                                                        </div>
                                                        <div class="col-3">
                                                            <label class="">Nb Jours TTT </label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                                    ${headerDTO.treatmentDays}
                                                            </div>
                                                        </div>
                                                    </c:if>
</div>
                                                <div class="row mb-2">
                                                    <c:if test="${headerDTO.patientType == 'ON_SITE'}">
                                                        <div class="col-8">
                                                            <label class="pt-1">Prescripteur : </label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                                    ${headerDTO.provider.name}
                                                            </div>
                                                        </div>
                                                        <div class="col-4">
                                                            <label class="">Date de prescription</label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                                <fmt:formatDate value="${headerDTO.prescriptionDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                                            </div>
                                                        </div>

                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </div>
            </div>
            <c:if test="${headerDTO.operationStatus == 'NOT_COMPLETED'}">
                <form:form modelAttribute="dispensationAttributeFluxForm" method="post" action="" id="form">
                    <form:hidden path="productOperationId"/>
                    <div>
                        <form:errors path="productId" cssClass="error"/>
                        <form:errors path="requestedQuantity" cssClass="error"/>
                        <form:errors path="dispensingQuantity" cssClass="error"/>
                    </div>
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                        <c:if test="${selectedProductId != null || (selectedProductId == null && dispensationAttributeFluxForm.productId == null) }">
                            <tr>
                                <td colspan="3">
                                    <table class="table table-borderless table-sm mb-1">
                                        <tr>
                                            <td>
                                                <form:select path="selectedProductId" cssClass="form-control s2">
                                                    <form:option value="" label=""/>
                                                    <form:options items="${products}" itemValue="productId" itemLabel="retailNameWithCode" />
                                                </form:select>
                                            </td>
                                            <td style="width: 10px">
                                                <button class="btn btn-primary" type="button" onclick="goToSelectedProduct()"><i class="fa fa-arrow-circle-down"></i></button>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td colspan="5" class="align-items-lg-center">
                                    <span class="text-danger text-lg-left font-italic font-weight-bold">${productMessage}</span>
                                </td>
                            </tr>
                        </c:if>

                        <tr class="bg-belize-hole">
                            <th colspan="3" style="width: 250px">Produit <span class="required">*</span></th>
                            <th style="width: 150px">Quantit&eacute; en stock</th>
                            <th style="width: 150px">Quantit&eacute; <br>demand&eacute;e <span class="required">*</span></th>
                            <th style="width: 150px">Quantit&eacute; <br>dispens&eacute;e <span class="required">*</span></th>
                            <th style="width: 150px">Quantit&eacute; restant</th>
                            <th style="width: 50px"></th>
                        </tr>
                        </thead>
                        <c:if test="${selectedProduct != null && productMessage == null}">
                            <tr>
                                <td>${selectedProduct.code}</td>
                                <td>${selectedProduct.retailName}</td>
                                <td>${selectedProduct.productRetailUnit.name}</td>
                                <td class="text-center text-primary font-weight-bold" id="quantityInStock">${selectedProductQuantityInStock}</td>
<%--                                <td colspan="3">--%>
<%--                                    --%>
<%--                                    <form:select path="productId" cssClass="form-control s2" >--%>
<%--                                        <form:option value="" label=""/>--%>
<%--                                        <form:options items="${products}" itemValue="productId" itemLabel="retailNameWithCode" />--%>
<%--                                    </form:select>--%>
<%--                                </td>--%>
                                <td class="text-center ">
                                    <form:input path="requestedQuantity" cssClass="form-control form-control-sm" />
                                </td>
                                <td class="text-center ">
                                    <form:input path="dispensingQuantity" cssClass="form-control form-control-sm" />
                                </td>
                                <td class="text-info text-center font-weight-bold">
                                    <span class="text-success" id="quantityRemaining">0</span>
                                </td>
                                <td>
                                    <button class="btn btn-success" id="button-submit">
                                        <c:if test="${not empty dispensationAttributeFluxForm.productId}">
                                            <i class="fa fa-edit"></i>
                                        </c:if>
                                        <c:if test="${empty dispensationAttributeFluxForm.productId}">
                                            <i class="fa fa-plus"></i>
                                        </c:if>
                                    </button>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td colspan="9">
                            </td>
                        </tr>
                        <c:forEach var="productFlux" items="${productAttributeFluxes}">
                                                    <tr>
                                                        <td>${productFlux.code}</td>
                                                        <td>
                                                                ${productFlux.retailName}
                                                        </td>
                                                        <td>
                                                                ${productFlux.retailUnit}
                                                        </td>
                                                        <td class="text-center">${productFlux.quantityInStock}</td>
                                                        <td class="text-center">
                                                                ${productFlux.requestedQuantity}
                                                        </td>
                                                        <td class="text-center">
                                                                ${productFlux.dispensingQuantity}
                                                        </td>
                                                        <td class="text-center">
                                                                ${productFlux.quantityInStock - productFlux.dispensingQuantity}
                                                        </td>
                                                        <td>
                                                            <c:if test="${headerDTO.operationStatus == 'NOT_COMPLETED'}">
                                                                <c:url value="/module/pharmacy/operations/dispensation/editFlux.form" var="editUrl">
                                                                    <c:param name="dispensationId" value="${headerDTO.productOperationId}"/>
                                                                    <c:param name="dispensationId" value="${productFlux.productId}"/>
                                                                </c:url>
                                                                <a href="${editUrl}" class="text-info"><i class="fa fa-edit"></i></a>
                                                                <c:url value="/module/pharmacy/operations/dispensation/deleteFlux.form" var="deleteUrl">
                                                                    <c:param name="dispensationId" value="${headerDTO.productOperationId}"/>
                                                                    <c:param name="productId" value="${productFlux.productId}"/>
                                                                </c:url>
                                                                <a href="${deleteUrl}" onclick="return confirm('Voulez vous supprimer ce regime ?')" class="text-danger"><i class="fa fa-trash"></i></a>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                        </c:forEach>

                        <c:if test="${fct:length(productAttributeFluxes) == 0}">
                            <tr><td colspan="9" class="text-center text-warning h5">Aucun produit dans la liste</td></tr>
                        </c:if>
                    </table>
                </form:form>
            </c:if>
        </div>


<%--        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////      --%>
        <div class="row">

        </div>

<%--        <c:if test="${productDispensation.operationStatus != 'NOT_COMPLETED'}">--%>
<%--            <table class="table table-condensed table-striped table-sm table-bordered">--%>
<%--                <thead class="thead-light">--%>
<%--                <tr class="bg-belize-hole">--%>
<%--                    <th colspan="3" style="width: 250px">Produit</th>--%>
<%--                    <th style="width: 200px">Num&eacute;ro de lot </th>--%>
<%--                    <th style="width: 150px">Date de p&eacute;remption </th>--%>
<%--                    <th style="width: 60px">Quantit&eacute; livr&eacute;e </th>--%>
<%--                    <th style="width: 60px">Quantit&eacute; re&ccedil;ue</th>--%>
<%--                    <th style="width: 250px">observation</th>--%>
<%--                </tr>--%>
<%--                </thead>--%>
<%--                <c:forEach var="productFlux" items="${productAttributeFluxes}">--%>
<%--                    <tr>--%>
<%--                        <td>${productFlux.code}</td>--%>
<%--                        <td>--%>
<%--                                ${productDispensation.dispensationQuantityMode == 'RETAIL' ? productFlux.retailName : productFlux.wholesaleName}--%>
<%--                        </td>--%>
<%--                        <td>--%>
<%--                                ${productDispensation.dispensationQuantityMode == 'RETAIL' ? productFlux.retailUnit : productFlux.wholesaleUnit}--%>
<%--                        </td>--%>
<%--                        <td class="text-center">${productFlux.batchNumber}</td>--%>
<%--                        <td class="text-center">--%>
<%--                            <fmt:formatDate value="${productFlux.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>--%>
<%--                        </td>--%>
<%--                        <td class="text-center">--%>
<%--                                ${productDispensation.dispensationQuantityMode == 'RETAIL' ? productFlux.quantityToDeliver : productFlux.quantityToDeliver / productFlux.unitConversion}--%>
<%--                        </td>--%>
<%--                        <td class="text-center">--%>
<%--                                ${productDispensation.dispensationQuantityMode == 'RETAIL' ? productFlux.quantity : productFlux.quantity / productFlux.unitConversion}--%>
<%--                        </td>--%>
<%--                        <td>${productFlux.observation}</td>--%>
<%--                    </tr>--%>
<%--                </c:forEach>--%>
<%--            </table>--%>
<%--        </c:if>--%>
    </div>

</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
