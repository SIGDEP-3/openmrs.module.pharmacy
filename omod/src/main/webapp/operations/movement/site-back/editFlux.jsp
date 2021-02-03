<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../../template/operationHeader.jsp"%>
<openmrs:require privilege="Save Product Back Supplier" otherwise="/login.htm" redirect="/module/pharmacy/operations/movement/site-back/editFlux.form" />
<script>
    if (jQuery) {
        jQuery(document).ready(function () {
            getRemainingQuantity();

            jQuery('#returnedQuantity').on('change', function (e) {
                getRemainingQuantity();
            });
        });
        function getRemainingQuantity() {
            let returnedQuantity = parseInt(jQuery('#quantity').val());
            let quantityRemaining = jQuery('#quantityRemaining');
            let quantityInStock = parseInt(jQuery('#quantityInStock').text());
            let buttonSubmit =  jQuery('#button-submit');
            if (returnedQuantity) {
                let quantity = quantityInStock - returnedQuantity;
                quantityRemaining.removeClass('text-danger');
                quantityRemaining.removeClass('text-warning');
                quantityRemaining.addClass('text-success');
                buttonSubmit.show();
                quantityRemaining.text(quantity)
                if (quantity === 0) {
                    quantityRemaining.removeClass('text-success');
                    quantityRemaining.addClass('text-warning');
                } else if (quantity < 0) {
                    quantityRemaining.removeClass('text-success');
                    quantityRemaining.addClass('text-danger');
                    buttonSubmit.hide()
                }
            } else {
                quantityRemaining.text(quantityInStock);
            }
        }
        function goToSelectedProduct() {
            let productId = '', input = jQuery('#selectedProductStockId').val().match(/\d+/g);
            if (input.length > 0) {
                for (let i = 0; i < input.length; i++) {
                    productId += input[i];
                }
                location.href = '${pageContext.request.contextPath}/module/pharmacy/operations/movement/site-back/editFlux.form?' +
                    'transferId=' + ${productBackSupplier.productOperationId} + '&selectedProductId=' + productId
            }
        }
    }

</script>
<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6 text-uppercase font-italic text-secondary">
            <div class="h6"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">
            <c:if test="${productBackSupplier.operationStatus != 'VALIDATED' &&
                      productBackSupplier.operationStatus != 'DISABLED'}">

                <c:if test="${productBackSupplier.operationStatus == 'NOT_COMPLETED' && fct:length(productAttributeFluxes) != 0}">
                    <c:url value="/module/pharmacy/operations/movement/back/complete.form" var="completeUrl">
                        <c:param name="backSupplierId" value="${productBackSupplier.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-success btn-sm mr-2" onclick="window.location='${completeUrl}'">
                        <i class="fa fa-save"></i> Terminer
                    </button>
                </c:if>
                <c:if test="${productBackSupplier.operationStatus != 'NOT_COMPLETED'}">
                    <c:url value="/module/pharmacy/operations/movement/back/incomplete.form" var="incompleteUrl">
                        <c:param name="backSupplierId" value="${productBackSupplier.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-primary btn-sm mr-2" onclick="window.location='${incompleteUrl}'">
                        <i class="fa fa-pen"></i> Editer l'inventaire
                    </button>
                    <openmrs:hasPrivilege privilege="Save Product Back Supplier">
                        <c:url value="/module/pharmacy/operations/movement/back/validate.form" var="validationUrl">
                            <c:param name="backSupplierId" value="${productBackSupplier.productOperationId}"/>
                        </c:url>
                        <button class="btn btn-success btn-sm mr-2" onclick="window.location='${validationUrl}'">
                            <i class="fa fa-pen"></i> Valider
                        </button>
                    </openmrs:hasPrivilege>
                </c:if>
            </c:if>
            <c:if test="${productBackSupplier.operationStatus == 'NOT_COMPLETED'}">
                <c:url value="/module/pharmacy/operations/movement/site-back/edit.form" var="editUrl">
                    <c:param name="id" value="${productBackSupplier.productOperationId}"/>
                </c:url>
                <button class="btn btn-primary btn-sm" onclick="window.location='${editUrl}'" title="Voir la liste">
                    <i class="fa fa-edit"></i> Editer l'ent&ecirc;te
                </button>
            </c:if>
            <c:url value="/module/pharmacy/operations/movement/site-back/list.form" var="url"/>
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
                    <td class="font-weight-bold text-info">${productBackSupplier.productProgram.name}</td>
                    <td>Fournisseur</td>
                    <td class="font-weight-bold text-info">
                        ${productBackSupplier.exchangeLocation.name}
                    </td>
                </tr>
                <tr>
                    <td>Date de retour du produit</td>
                    <td class="font-weight-bold text-info">
                        <fmt:formatDate value="${productBackSupplier.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                    </td>
                    <td>Observation</td>
                    <td class="font-weight-bold text-info">${productBackSupplier.observation}</td>
                </tr>
                </thead>
            </table>

            <c:if test="${productBackSupplier.operationStatus == 'NOT_COMPLETED'}">
                <form:form modelAttribute="backSupplierAttributeFluxForm" method="post" action="" id="form">
                    <form:hidden path="productAttributeFluxId"/>
                    <form:hidden path="productOperationId"/>
                    <form:hidden path="locationId"/>
                    <form:hidden path="batchNumber"/>
                    <form:hidden path="expiryDate"/>
                    <form:hidden path="productId"/>
                    <div>
                        <form:errors path="productId" cssClass="error"/>
                        <form:errors path="batchNumber" cssClass="error"/>
                        <form:errors path="expiryDate" cssClass="error"/>
                        <form:errors path="quantity" cssClass="error"/>
                    </div>
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                        <c:if test="${backSupplierAttributeFluxForm.selectedProductStockId == null ||
                    (backSupplierAttributeFluxForm.selectedProductStockId != null && backSupplierAttributeFluxForm.productId != null ) }">
                            <tr>
                                <td colspan="4">
                                    <table class="table table-borderless table-sm mb-1">
                                        <tr>
                                            <td>
                                                <form:select path="selectedProductStockId" cssClass="form-control s2">
                                                    <form:option value="" label=""/>
                                                    <form:options items="${stocks}" itemValue="productAttributeStockId" itemLabel="productInStock" />
                                                </form:select>
                                            </td>
                                            <td style="width: 10px">
                                                <button class="btn btn-primary" type="button" onclick="goToSelectedProduct()"><i class="fa fa-arrow-circle-down"></i></button>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td colspan="6" class="align-items-lg-center">
                                    <div class="row html-editor-align-center">
                                        <div class="col-12">
                                            <span class="text-danger text-lg-left font-italic font-weight-bold">
                                                    ${productMessage}
                                            </span>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                        <tr class="bg-belize-hole">
                            <th colspan="3" style="width: 250px">Produit <span class="required">*</span></th>
                            <th style="width: 200px">Num&eacute;ro <br>de lot <span class="required">*</span></th>
                            <th style="width: 150px">Date de <br>peremption <span class="required">*</span></th>
                            <th style="width: 60px">Quantit&eacute; <br>en stock</th>
                            <th style="width: 60px">Quantit&eacute; <br>retourn&eacute;e<span class="required">*</span></th>
                            <th style="width: 60px">Quantit&eacute; <br>restant</th>
                            <th style="width: 250px">Observation</th>
                            <th style="width: 50px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${productBackSupplier.operationStatus == 'NOT_COMPLETED'}">
                            <c:if test="${backSupplierAttributeFluxForm.selectedProductStockId != null && productMessage == null}">
                                <tr>
                                    <td colspan="">${stock.productAttribute.product.code}</td>
                                    <td colspan="">${stock.productAttribute.product.retailName}</td>
                                    <td colspan="">${stock.productAttribute.product.productRetailUnit.name}</td>
                                    <td>${stock.productAttribute.batchNumber}</td>
                                    <td>${stock.productAttribute.expiryDate}</td>
                                    <td id="quantityInStock">${stock.quantityInStock}</td>
                                    <td id="returnedQuantity"><form:input path="quantity" cssClass="form-control form-control-sm text-center" /></td>
                                    <td class="text-center"><span class="text-success" id="quantityRemaining">0</span></td>
                                    <td><form:input path="observation" cssClass="form-control form-control-sm" /></td>
                                    <td>
                                        <button class="btn btn-success" id="button-submit">
                                            <c:if test="${not empty backSupplierAttributeFluxForm.productAttributeFluxId}">
                                                <i class="fa fa-edit"></i>
                                            </c:if>
                                            <c:if test="${empty backSupplierAttributeFluxForm.productAttributeFluxId}">
                                                <i class="fa fa-plus"></i>
                                            </c:if>
                                        </button>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="10"></td>
                                </tr>
                            </c:if>
                            <c:forEach var="productFlux" items="${productAttributeFluxes}">
                                <tr><td>${productFlux.code}</td>
                                    <td>${productFlux.retailName}</td>
                                    <td>${productFlux.retailUnit}</td>
                                    <td class="text-center">
                                        <fmt:formatDate value="${productFlux.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                    </td>
                                    <td class="text-center">${productFlux.batchNumber}</td>
                                    <td class="text-center">${productFlux.quantityInStock}</td>
                                    <td class="text-center">${productFlux.quantity}</td>
                                    <td class="text-center">
                                            ${productFlux.quantityInStock - productFlux.quantity}
                                    </td>
                                    <td>${productFlux.observation}</td>
                                    <td>
                                        <c:if test="${productBackSupplier.operationStatus == 'NOT_COMPLETED'}">
                                            <c:url value="/module/pharmacy/operations/movement/site-back/editFlux.form" var="editUrl">
                                                <c:param name="backSupplierId" value="${productBackSupplier.productOperationId}"/>
                                                <c:param name="fluxId" value="${productFlux.productAttributeFluxId}"/>
                                            </c:url>
                                            <a href="${editUrl}" class="text-info"><i class="fa fa-edit"></i></a>
                                            <c:url value="/module/pharmacy/operations/movement/back/deleteFlux.form" var="deleteUrl">
                                                <c:param name="backId" value="${productBackSupplier.productOperationId}"/>
                                                <c:param name="fluxId" value="${productFlux.productAttributeFluxId}"/>
                                            </c:url>
                                            <a href="${deleteUrl}" onclick="return confirm('Voulez vous supprimer ce regime ?')" class="text-danger"><i class="fa fa-trash"></i></a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${fct:length(productAttributeFluxes) == 0}">
                                <tr><td colspan="10" class="text-center text-warning h5">Aucun produit dans la liste</td></tr>
                            </c:if>
                        </c:if>
                        </tbody>
                    </table>
                </form:form>
            </c:if>
            <c:if test="${productBackSupplier.operationStatus != 'NOT_COMPLETED'}">
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
        </div>
    </div>
</div>

<%@ include file="../../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
