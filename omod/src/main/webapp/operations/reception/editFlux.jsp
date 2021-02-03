<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<openmrs:require privilege="Save Reception" otherwise="/login.htm" redirect="/module/pharmacy/operations/reception/editFlux.form" />
<script>
    if (jQuery) {
        <c:if test="${productReception.incidence == 'NEGATIVE'}">
        jQuery(document).ready(function () {
            getRemainingQuantity();

            jQuery('#quantityToReturn').on('change', function (e) {
                getRemainingQuantity();
            });
        });
        function getRemainingQuantity() {
            let quantityToReturn = parseInt(jQuery('#quantity').val());
            let quantityRemaining = jQuery('#quantityRemaining');
            let quantityInStock = parseInt(jQuery('#quantityInStock').text());
            let quantityReceived = parseInt(jQuery('#quantityReceived').text());
            let buttonSubmit =  jQuery('#button-submit');
            if (quantityToReturn) {
                let quantity = quantityInStock <= quantityReceived ? quantityInStock - quantityToReturn : quantityReceived - quantityToReturn;
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
                quantityRemaining.text(quantityInStock <= quantityReceived ? quantityInStock : quantityReceived);
            }
        }

        function goToSelectedProduct() {
            let productId = '', input = jQuery('#selectedProductFluxId').val().match(/\d+/g);
            if (input.length > 0) {
                for (let i = 0; i < input.length; i++) {
                    productId += input[i];
                }
                location.href = '${pageContext.request.contextPath}/module/pharmacy/operations/reception/editFlux.form?' +
                    'receptionId=' + ${productReception.productOperationId} + '&selectedProductId=' + productId
            }
        }
        </c:if>
    }

</script>
<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6 text-uppercase font-italic text-secondary">
            <div class="h6"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">

            <c:if test="${productReception.operationStatus != 'VALIDATED' &&
                      productReception.operationStatus != 'DISABLED'}">

                <c:if test="${productReception.operationStatus == 'NOT_COMPLETED' && fct:length(productAttributeFluxes) != 0}">
                    <c:url value="/module/pharmacy/operations/reception/complete.form" var="completeUrl">
                        <c:param name="receptionId" value="${productReception.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-success btn-sm mr-2" onclick="window.location='${completeUrl}'">
                        <i class="fa fa-save"></i> Terminer
                    </button>
                </c:if>
                <c:if test="${productReception.operationStatus != 'NOT_COMPLETED'}">
                    <c:url value="/module/pharmacy/operations/reception/incomplete.form" var="incompleteUrl">
                        <c:param name="receptionId" value="${productReception.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-primary btn-sm mr-2" onclick="window.location='${incompleteUrl}'">
                        <i class="fa fa-pen"></i>
                        <c:if test="${productReception.incidence == 'NEGATIVE'}">
                            Editer le retour de r&eacute;ception
                        </c:if>
                        <c:if test="${productReception.incidence == 'POSITIVE'}">
                            Editer la r&eacute;ception
                        </c:if>
                    </button>
                    <openmrs:hasPrivilege privilege="Validate Reception">
                        <c:url value="/module/pharmacy/operations/reception/validate.form" var="validationUrl">
                            <c:param name="receptionId" value="${productReception.productOperationId}"/>
                        </c:url>
                        <button class="btn btn-success btn-sm mr-2" onclick="window.location='${validationUrl}'">
                            <i class="fa fa-pen"></i> Valider
                            <c:if test="${productReception.incidence == 'NEGATIVE'}">
                                le retour de r&eacute;ception
                            </c:if>
                            <c:if test="${productReception.incidence == 'POSITIVE'}">
                                la r&eacute;ception
                            </c:if>
                        </button>
                    </openmrs:hasPrivilege>
                </c:if>
            </c:if>
            <c:if test="${productReception.operationStatus == 'NOT_COMPLETED'}">
                <c:url value="/module/pharmacy/operations/reception/edit.form" var="editUrl">
                    <c:param name="id" value="${productReception.productOperationId}"/>
                </c:url>
                <button class="btn btn-primary btn-sm" onclick="window.location='${editUrl}'" title="Voir la liste">
                    <i class="fa fa-edit"></i> Editer l'entete
                </button>
            </c:if>
            <c:url value="/module/pharmacy/operations/reception/list.form" var="url"/>
            <button class="btn btn-primary btn-sm" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-warning">
        <div class="col-12">
            <table class="bg-light table table-borderless table-light border">
                <thead class="thead-light">
                <tr>
                    <th>
                        Fournisseur
                        <c:if test="${productReception.incidence == 'NEGATIVE'}">
                            de la r&eacute;ception
                        </c:if>
                    </th>
                    <th class="font-weight-bold text-info">${productReception.productSupplier.name}</th>
                    <th>Date de
                        <c:if test="${productReception.incidence == 'NEGATIVE'}">
                            retour de r&eacute;ception
                            <span class="font-italic text-primary">(Date de r&eacute;ception)</span>
                        </c:if>
                        <c:if test="${productReception.incidence == 'POSITIVE'}">
                            la r&eacute;ception
                        </c:if>
                    </th>
                    <th class="font-weight-bold text-info">
                        <fmt:formatDate value="${productReception.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                        <c:if test="${productReception.incidence == 'NEGATIVE'}">
                        <span class="font-italic text-primary">
                            (<fmt:formatDate value="${reception.operationDate}"
                                             pattern="dd/MM/yyyy"
                                             type="DATE"/>)
                        </span>
                        </c:if>
                    </th>
                </tr>
                <tr>
                    <th>Programme :</th>
                    <th class="font-weight-bold text-info">${productReception.productProgram.name}</th>
                    <th>
                        Bordereau de livraison
                        <c:if test="${productReception.incidence == 'NEGATIVE'}">
                            de la r&eacute;ception
                        </c:if>
                    </th>
                    <th class="font-weight-bold text-info">${productReception.operationNumber}</th>
                </tr>
                <tr>
                    <th>Type de saisie</th>
                    <th class="font-weight-bold text-info">${productReception.receptionQuantityMode == 'RETAIL' ? 'DETAIL' : 'CONDITIONNEMENT'}</th>
                    <th>Observation</th>
                    <th class="font-weight-bold text-info">${productReception.observation}</th>
                </tr>
                </thead>
            </table>
            <c:if test="${productReception.operationStatus == 'NOT_COMPLETED'}">
                <form:form modelAttribute="receptionAttributeFluxForm" method="post" action="" id="form">
                    <form:hidden path="productAttributeFluxId"/>
                    <form:hidden path="productOperationId"/>
                    <form:hidden path="locationId"/>
                    <c:if test="${productReception.incidence == 'NEGATIVE'}">
                        <form:hidden path="batchNumber"/>
                        <form:hidden path="expiryDate"/>
                        <form:hidden path="productId"/>
                    </c:if>
                    <div>
                        <form:errors path="productId" cssClass="error"/>
                        <form:errors path="batchNumber" cssClass="error"/>
                        <form:errors path="expiryDate" cssClass="error"/>
                        <form:errors path="quantity" cssClass="error"/>
                        <c:if test="${productReception.incidence == 'POSITIVE'}">
                            <form:errors path="quantityToDeliver" cssClass="error"/>
                        </c:if>
                        <form:errors path="observation" cssClass="error"/>
                    </div>
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                        <c:if test="${(productReception.incidence == 'NEGATIVE' && (receptionAttributeFluxForm.selectedProductFluxId == null ||
                    (receptionAttributeFluxForm.selectedProductFluxId != null && receptionAttributeFluxForm.productId != null )) && fct:length(products) > 0)  && receptionAttributeFluxForm.productAttributeFluxId == null }">
                            <tr>
                                <td colspan="5">
                                    <table class="table table-borderless table-sm mb-1">
                                        <tr>
                                            <td>
                                                <form:select path="selectedProductFluxId" cssClass="form-control s2">
                                                    <form:option value="" label=""/>
                                                    <form:options items="${products}" itemValue="productAttributeFluxId" itemLabel="productWithAttribute" />
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
                            <c:if test="${productReception.incidence == 'POSITIVE'}">
                                <th colspan="3" style="width: 250px">Produit <span class="required">*</span></th>
                                <th style="width: 120px">Num&eacute;ro <br>de lot <span class="required">*</span></th>
                                <th style="width: 150px">Date de <br>p&eacute;remption <span class="required">*</span></th>
                                <th style="width: 60px">Quantit&eacute; <br>livr&eacute;e <span class="required">*</span></th>
                                <th style="width: 60px">Quantit&eacute; <br>re&ccedil;ue <span class="required">*</span></th>
                            </c:if>
                            <c:if test="${productReception.incidence == 'NEGATIVE'}">
                                <th colspan="3" style="width: 200px">Produit</th>
                                <th style="width: 120px">Num&eacute;ro <br>de lot</th>
                                <th style="width: 120px">Date de <br>p&eacute;remption</th>
                                <th style="width: 60px">Quantit&eacute; <br>re&ccedil;ue</th>
                                <th style="width: 60px">Quantit&eacute; <br>en stock </th>
                                <th style="width: 60px" class="text-center">Quantit&eacute; &agrave;<br>retourner <span class="required">*</span></th>
                                <th style="width: 60px">Quantit&eacute;<br>restant </th>
                            </c:if>
                            <th style="width: 250px">observation</th>
                            <th style="width: 50px"></th>
                        </tr>
                        </thead>
                        <c:if test="${productReception.operationStatus == 'NOT_COMPLETED'}">
                            <c:if test="${productReception.incidence == 'POSITIVE'}">
                                <td colspan="3">
                                    <form:select path="productId" cssClass="form-control s2">
                                        <form:option value="" label=""/>
                                        <form:options items="${products}" itemValue="productId"
                                                      itemLabel="retailNameWithCode"/>
                                    </form:select>
                                </td>
                                <td>
                                    <c:if test="${empty receptionAttributeFluxForm.productAttributeFluxId}">
                                        <form:input path="batchNumber" cssClass="form-control form-control-sm"  />
                                    </c:if>
                                    <c:if test="${not empty receptionAttributeFluxForm.productAttributeFluxId}">
                                        <form:input path="batchNumber" cssClass="form-control form-control-sm" readonly="true"  />
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${empty receptionAttributeFluxForm.productAttributeFluxId}">
                                        <form:input path="expiryDate" cssClass="form-control form-control-sm picker" />
                                    </c:if>
                                    <c:if test="${not empty receptionAttributeFluxForm.productAttributeFluxId}">
                                        <form:input path="expiryDate" cssClass="form-control form-control-sm picker" readonly="true"  />
                                    </c:if>
                                </td>
                                <td><form:input path="quantityToDeliver" cssClass="form-control form-control-sm" /></td>
                                <td><form:input path="quantity" cssClass="form-control form-control-sm" /></td>
                                <td><form:input path="observation" cssClass="form-control form-control-sm" /></td>
                                <td>
                                    <button class="btn btn-success">
                                        <c:if test="${not empty receptionAttributeFluxForm.productAttributeFluxId}">
                                            <i class="fa fa-edit"></i>
                                        </c:if>
                                        <c:if test="${empty receptionAttributeFluxForm.productAttributeFluxId}">
                                            <i class="fa fa-plus"></i>
                                        </c:if>
                                    </button>
                                </td>
                                <tr><td colspan="9"></td></tr>
                            </c:if>
                            <c:if test="${productReception.incidence == 'NEGATIVE'}">
                                <c:if test="${(receptionAttributeFluxForm.selectedProductFluxId != null && productMessage == null) || receptionAttributeFluxForm.productAttributeFluxId != null}">
                                    <tr>
                                        <td>${stock.code}</td>
                                        <td>${productReception.receptionQuantityMode == 'RETAIL' ? stock.retailName : stock.wholesaleName}</td>
                                        <td>${productReception.receptionQuantityMode == 'RETAIL' ? stock.retailUnit : stock.wholesaleUnit}</td>
                                        <td>${stock.batchNumber}</td>
                                        <td><fmt:formatDate value="${stock.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                        <td class="text-center font-weight-bold">
                                            <span id="quantityReceived">
                                            <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? stock.quantityReceived : stock.quantityReceived / stock.unitConversion}" />
                                            </span>
                                        </td>
                                        <td id="quantityInStock" class="text-center">
                                            <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? stock.quantityInStock : stock.quantityInStock / stock.unitConversion}" />
                                        </td>
                                        <td id="quantityToReturn"><form:input path="quantity" cssClass="form-control form-control-sm text-center" /></td>
                                        <td class="text-center"><span class="text-success" id="quantityRemaining">0</span></td>
                                        <td><form:input path="observation" cssClass="form-control form-control-sm"/></td>
                                        <td>
                                            <button class="btn btn-success" id="button-submit">
                                                <c:if test="${not empty transferAttributeFluxForm.productAttributeFluxId}">
                                                    <i class="fa fa-edit"></i>
                                                </c:if>
                                                <c:if test="${empty transferAttributeFluxForm.productAttributeFluxId}">
                                                    <i class="fa fa-plus"></i>
                                                </c:if>
                                            </button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="11"></td>
                                    </tr>
                                </c:if>
                            </c:if>
                        </c:if>

                        <c:forEach var="productFlux" items="${productAttributeFluxes}">
                            <tr>
                                <td>${productFlux.code}</td>
                                <td>
                                        ${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.retailName : productFlux.wholesaleName}
                                </td>
                                <td>
                                        ${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.retailUnit : productFlux.wholesaleUnit}
                                </td>
                                <td class="text-center">${productFlux.batchNumber}</td>
                                <td class="text-center">
                                    <fmt:formatDate value="${productFlux.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                </td>
                                <c:if test="${productReception.incidence == 'POSITIVE'}">
                                    <td class="text-center">
                                        <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.quantityToDeliver : productFlux.quantityToDeliver / productFlux.unitConversion}" />
                                    </td>
                                    <td class="text-center">
                                        <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.quantity : productFlux.quantity / productFlux.unitConversion}" />
                                    </td>
                                </c:if>
                                <c:if test="${productReception.incidence == 'NEGATIVE'}">
                                    <td class="text-center">
                                        <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.quantityReceived : productFlux.quantityReceived / productFlux.unitConversion}" />
                                    </td>
                                    <td class="text-center">
                                        <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.quantityInStock : productFlux.quantityInStock / productFlux.unitConversion}" />
                                    </td>
                                    <td class="text-center">
                                        <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.quantityToReturn : productFlux.quantityToReturn / productFlux.unitConversion}" />
                                    </td>
                                    <td class="text-center">
                                        <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ?
                                        (productFlux.quantityInStock <= productFlux.quantityReceived ? productFlux.quantityInStock - productFlux.quantityToReturn : productFlux.quantityReceived - productFlux.quantityToReturn) :
                                        (productFlux.quantityInStock <= productFlux.quantityReceived ? productFlux.quantityInStock - productFlux.quantityToReturn : productFlux.quantityReceived - productFlux.quantityToReturn) / productFlux.unitConversion
                                        }" />
                                    </td>
                                </c:if>

                                <td>${productFlux.observation}</td>
                                <td>
                                    <c:if test="${productReception.operationStatus == 'NOT_COMPLETED'}">
                                        <c:url value="/module/pharmacy/operations/reception/editFlux.form" var="editUrl">
                                            <c:param name="receptionId" value="${productReception.productOperationId}"/>
                                            <c:param name="fluxId" value="${productFlux.productAttributeFluxId}"/>
                                        </c:url>
                                        <a href="${editUrl}" class="text-info"><i class="fa fa-edit"></i></a>
                                        <c:url value="/module/pharmacy/operations/reception/deleteFlux.form" var="deleteUrl">
                                            <c:param name="receptionId" value="${productReception.productOperationId}"/>
                                            <c:param name="fluxId" value="${productFlux.productAttributeFluxId}"/>
                                        </c:url>
                                        <a href="${deleteUrl}" onclick="return confirm('Voulez vous supprimer ce regime ?')" class="text-danger"><i class="fa fa-trash"></i></a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${fct:length(productAttributeFluxes) == 0}">
                            <c:if test="${productReception.incidence == 'POSITIVE'}">
                                <tr><td colspan="9" class="text-center text-warning h5">Aucun produit dans la liste</td></tr>
                            </c:if>
                            <c:if test="${productReception.incidence == 'NEGATIVE'}">
                                <tr><td colspan="11" class="text-center text-warning h5">Aucun produit dans la liste</td></tr>
                            </c:if>
                        </c:if>
                    </table>
                </form:form>
            </c:if>
            <c:if test="${productReception.operationStatus != 'NOT_COMPLETED'}">
                <c:if test="${productReception.incidence == 'POSITIVE'}">
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                        <tr class="bg-belize-hole">
                            <th colspan="3" style="width: 250px">Produit</th>
                            <th style="width: 200px">Num&eacute;ro de lot </th>
                            <th style="width: 150px">Date de p&eacute;remption </th>
                            <th style="width: 60px">Quantit&eacute; livr&eacute;e </th>
                            <th style="width: 60px">Quantit&eacute; re&ccedil;ue</th>
                            <th style="width: 250px">observation</th>
                        </tr>
                        </thead>
                        <c:forEach var="productFlux" items="${productAttributeFluxes}">
                            <tr>
                                <td>${productFlux.code}</td>
                                <td>
                                        ${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.retailName : productFlux.wholesaleName}
                                </td>
                                <td>
                                        ${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.retailUnit : productFlux.wholesaleUnit}
                                </td>
                                <td class="text-center">${productFlux.batchNumber}</td>
                                <td class="text-center">
                                    <fmt:formatDate value="${productFlux.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                </td>
                                <td class="text-center">
                                    <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.quantityToDeliver : productFlux.quantityToDeliver / productFlux.unitConversion}" />
                                </td>
                                <td class="text-center">
                                    <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.quantity : productFlux.quantity / productFlux.unitConversion}" />
                                </td>
                                <td>${productFlux.observation}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>
                <c:if test="${productReception.incidence == 'NEGATIVE'}">
                    <table class="table table-condensed table-striped table-sm table-bordered">
                        <thead class="thead-light">
                        <tr class="bg-belize-hole">
                            <th colspan="3" style="width: 250px">Produit</th>
                            <th style="width: 200px">Num&eacute;ro de lot </th>
                            <th style="width: 150px">Date de p&eacute;remption </th>
                            <th style="width: 60px">Quantit&eacute; re&ccedil;ue</th>
                            <th style="width: 60px">Quantit&eacute; retourn&eacute;e </th>
                            <th style="width: 250px">observation</th>
                        </tr>
                        </thead>
                        <c:forEach var="productFlux" items="${productAttributeFluxes}">
                            <tr>
                                <td>${productFlux.code}</td>
                                <td>${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.retailName : productFlux.wholesaleName}</td>
                                <td>${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.retailUnit : productFlux.wholesaleUnit}</td>
                                <td class="text-center">${productFlux.batchNumber}</td>
                                <td class="text-center"><fmt:formatDate value="${productFlux.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                <td class="text-center">
                                    <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.quantityReceived : productFlux.quantityReceived / productFlux.unitConversion}" />
                                </td>
                                <td class="text-center">
                                    <fmt:parseNumber integerOnly = "true" type="number" value="${productReception.receptionQuantityMode == 'RETAIL' ? productFlux.quantityToReturn : productFlux.quantityToReturn / productFlux.unitConversion}" />
                                </td>
                                <td>${productFlux.observation}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>

            </c:if>
        </div>
    </div>

</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
