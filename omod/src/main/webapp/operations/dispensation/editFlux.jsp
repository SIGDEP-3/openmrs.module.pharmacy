<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../template/operationHeader.jsp"%>
<script>
    if (jQuery) {
        jQuery(document).ready(function () {

        });

        //jQuery.ajax('/')
    }
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
                                        'PEC SUR LE SITE' : 'MOBILE'}</span>)
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
                                                        <span class="mr-3 text-info">
                                                            ${headerDTO.goal == 'NOT_APPLICABLE' ? '&ofcir;' : '&cir;'}&nbsp;&nbsp;Non Applicable </span>
                                                        <span class="mr-3 text-info">
                                                            ${headerDTO.goal == 'PEC' ? '&ofcir;' : '&cir;'} &nbsp;&nbsp;PEC </span>
                                                        <span class="mr-3 text-info">
                                                            ${headerDTO.goal == 'PTME' ? '&ofcir;' : '&cir;'} &nbsp;&nbsp;PTME </span>
                                                        <span class="text-info">
                                                            ${headerDTO.goal == 'AES' ? '&ofcir;' : '&cir;'}&nbsp;&nbsp;AES</span>
                                                    </div>
                                                </div>
                                                <div class="row mb-2">
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
                                                </div>
                                                <div class="row mb-2">
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
                    <%--                <form:hidden path="productAttributeFluxId"/>--%>
                    <form:hidden path="productOperationId"/>
                    <div>
                        <form:errors path="productId" cssClass="error"/>
                            <%--                    <form:errors path="batchNumber" cssClass="error"/>--%>
                            <%--                    <form:errors path="expiryDate" cssClass="error"/> <br>--%>
                        <form:errors path="requestedQuantity" cssClass="error"/>
                        <form:errors path="dispensingQuantity" cssClass="error"/>
                            <%--                    <form:errors path="observation" cssClass="error"/>--%>
                    </div>
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                        <tr class="bg-belize-hole">
                            <th colspan="3" style="width: 250px">Produit <span class="required">*</span></th>
                            <th style="width: 150px">Quantit&eacute; en stock</th>
                            <th style="width: 150px">Quantit&eacute; <br>demand&eacute;e <span class="required">*</span></th>
                            <th style="width: 150px">Quantit&eacute; <br>dispens&eacute;e <span class="required">*</span></th>
                            <th style="width: 50px"></th>
                        </tr>
                        </thead>
                        <c:if test="${headerDTO.operationStatus == 'NOT_COMPLETED'}">
                            <tr>
                                <td colspan="3">
                                    <form:select path="productId" cssClass="form-control s2" >
                                        <form:option value="" label=""/>
                                        <form:options items="${products}" itemValue="productId" itemLabel="retailNameWithCode" />
                                    </form:select>
                                </td>
                                    <%--                            <td>--%>
                                    <%--                                <c:if test="${empty dispensationAttributeFluxForm.productAttributeFluxId}">--%>
                                    <%--                                    <form:input path="batchNumber" cssClass="form-control form-control-sm"  />--%>
                                    <%--                                </c:if>--%>
                                    <%--                                <c:if test="${not empty dispensationAttributeFluxForm.productAttributeFluxId}">--%>
                                    <%--                                    <form:input path="batchNumber" cssClass="form-control form-control-sm" readonly="true"  />--%>
                                    <%--                                </c:if>--%>
                                    <%--                            </td>--%>
                                    <%--                            <td>--%>
                                    <%--                                <c:if test="${empty dispensationAttributeFluxForm.productAttributeFluxId}">--%>
                                    <%--                                    <form:input path="expiryDate" cssClass="form-control form-control-sm picker" />--%>
                                    <%--                                </c:if>--%>
                                    <%--                                <c:if test="${not empty dispensationAttributeFluxForm.productAttributeFluxId}">--%>
                                    <%--                                    <form:input path="expiryDate" cssClass="form-control form-control-sm picker" readonly="true"  />--%>
                                    <%--                                </c:if>--%>
                                    <%--                            </td>--%>
                                <td id="quantityInStock" class="text-info"></td>
                                <td>
                                    <form:input path="requestedQuantity" cssClass="form-control form-control-sm" />
                                </td>
                                <td>
                                    <form:input path="dispensingQuantity" cssClass="form-control form-control-sm" />
                                </td>
                                <td>
                                    <button class="btn btn-success">
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
                            <%--                        <tr>--%>
                            <%--                            <td>${productFlux.code}</td>--%>
                            <%--                            <td>--%>
                            <%--                                    ${productFlux.retailName}--%>
                            <%--                            </td>--%>
                            <%--                            <td>--%>
                            <%--                                    ${productFlux.retailUnit}--%>
                            <%--                            </td>--%>
                            <%--&lt;%&ndash;                            <td class="text-center">${productFlux.batchNumber}</td>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;                            <td class="text-center">&ndash;%&gt;--%>
                            <%--&lt;%&ndash;&lt;%&ndash;                                <fmt:formatDate value="${productFlux.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>&ndash;%&gt;&ndash;%&gt;--%>
                            <%--&lt;%&ndash;                            </td>&ndash;%&gt;--%>
                            <%--                            <td class="text-center">--%>
                            <%--                                    ${productDispensation.dispensationQuantityMode == 'RETAIL' ? productFlux.quantityToDeliver : productFlux.quantityToDeliver / productFlux.unitConversion}--%>
                            <%--                            </td>--%>
                            <%--                            <td class="text-center">--%>
                            <%--                                    ${productDispensation.dispensationQuantityMode == 'RETAIL' ? productFlux.quantity : productFlux.quantity / productFlux.unitConversion}--%>
                            <%--                            </td>--%>
                            <%--                            <td>${productFlux.observation}</td>--%>
                            <%--                            <td>--%>
                            <%--                                <c:if test="${productDispensation.operationStatus == 'NOT_COMPLETED'}">--%>
                            <%--                                    <c:url value="/module/pharmacy/operations/dispensation/editFlux.form" var="editUrl">--%>
                            <%--                                        <c:param name="dispensationId" value="${productDispensation.productOperationId}"/>--%>
                            <%--                                        <c:param name="fluxId" value="${productFlux.productAttributeFluxId}"/>--%>
                            <%--                                    </c:url>--%>
                            <%--                                    <a href="${editUrl}" class="text-info"><i class="fa fa-edit"></i></a>--%>
                            <%--                                    <c:url value="/module/pharmacy/operations/dispensation/deleteFlux.form" var="deleteUrl">--%>
                            <%--                                        <c:param name="dispensationId" value="${productDispensation.productOperationId}"/>--%>
                            <%--                                        <c:param name="fluxId" value="${productFlux.productAttributeFluxId}"/>--%>
                            <%--                                    </c:url>--%>
                            <%--                                    <a href="${deleteUrl}" onclick="return confirm('Voulez vous supprimer ce regime ?')" class="text-danger"><i class="fa fa-trash"></i></a>--%>
                            <%--                                </c:if>--%>
                            <%--                            </td>--%>
                            <%--                        </tr>--%>
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
