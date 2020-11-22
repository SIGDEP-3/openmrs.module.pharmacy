<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../template/operationHeader.jsp"%>

<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6">
            <div class="h5"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">

            <c:if test="${productInventory.operationStatus != 'VALIDATED' &&
                      productInventory.operationStatus != 'DISABLED'}">

                <c:if test="${productInventory.operationStatus == 'NOT_COMPLETED' && fct:length(productAttributeFluxes) != 0}">
                    <c:url value="/module/pharmacy/operations/inventory/complete.form" var="completeUrl">
                        <c:param name="inventoryId" value="${productInventory.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-success mr-2" onclick="window.location='${completeUrl}'">
                        <i class="fa fa-save"></i> Terminer
                    </button>
                </c:if>
                <c:if test="${productInventory.operationStatus != 'NOT_COMPLETED'}">
                    <c:url value="/module/pharmacy/operations/inventory/incomplete.form" var="incompleteUrl">
                        <c:param name="inventoryId" value="${productInventory.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-primary mr-2" onclick="window.location='${incompleteUrl}'">
                        <i class="fa fa-pen"></i> Editer la r&eacute;ception
                    </button>
                    <c:url value="/module/pharmacy/operations/inventory/validate.form" var="validationUrl">
                        <c:param name="inventoryId" value="${productInventory.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-success mr-2" onclick="window.location='${validationUrl}'">
                        <i class="fa fa-pen"></i> Valider la r&eacute;ception
                    </button>
                </c:if>
            </c:if>
            <c:if test="${productInventory.operationStatus == 'NOT_COMPLETED'}">
                <c:url value="/module/pharmacy/operations/inventory/edit.form" var="editUrl">
                    <c:param name="id" value="${productInventory.productOperationId}"/>
                </c:url>
                <button class="btn btn-primary" onclick="window.location='${editUrl}'" title="Voir la liste">
                    <i class="fa fa-edit"></i> Editer l'ent&ecirc;te
                </button>
            </c:if>
            <c:url value="/module/pharmacy/operations/inventory/list.form" var="url"/>
            <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <table class="bg-light table table-borderless table-light border">
            <thead class="thead-light">
            <tr>
                <td>Programme :</td>
                <td class="font-weight-bold text-info">${productInventory.productProgram.name}</td>
                <td>Date de inventory</td>
                <td class="font-weight-bold text-info">
                    <fmt:formatDate value="${productInventory.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                </td>
            </tr>
            <tr>
                <td>Observation</td>
                <td class="font-weight-bold text-info">${productInventory.observation}</td>
            </tr>
            </thead>
        </table>
        <c:if test="${productInventory.operationStatus == 'NOT_COMPLETED'}">
            <form:form modelAttribute="inventoryAttributeFluxForm" method="post" action="" id="form">
                <form:hidden path="productAttributeFluxId"/>
                <form:hidden path="productOperationId"/>
                <form:hidden path="locationId"/>
                <div>
                    <form:errors path="productId" cssClass="error"/>
                    <form:errors path="batchNumber" cssClass="error"/>
                    <form:errors path="expiryDate" cssClass="error"/> <br>
                    <form:errors path="quantity" cssClass="error"/>
                    <form:errors path="observation" cssClass="error"/>
                </div>
                <table class="table table-condensed table-striped table-sm table-bordered">
                    <thead class="thead-light">
                    <tr class="bg-belize-hole">
                        <th colspan="3" style="width: 250px">Produit <span class="required">*</span></th>
                        <th style="width: 200px">Numero <br>de lot <span class="required">*</span></th>
                        <th style="width: 150px">Date de <br>peremption <span class="required">*</span></th>
                        <th style="width: 60px">Quantite <br>physique <span class="required">*</span></th>
                        <th style="width: 250px">observation</th>
                        <th style="width: 50px"></th>
                    </tr>
                    </thead>
                    <c:if test="${productInventory.operationStatus == 'NOT_COMPLETED'}">
                        <tr>
                            <td colspan="3">
                                <form:select path="productId" cssClass="form-control s2" >
                                    <form:option value="" label=""/>
                                    <form:options items="${products}" itemValue="productId" itemLabel="retailNameWithCode" />
                                </form:select>
                            </td>
                            <td>
                                <c:if test="${empty inventoryAttributeFluxForm.productAttributeFluxId}">
                                    <form:input path="batchNumber" cssClass="form-control form-control-sm"  />
                                </c:if>
                                <c:if test="${not empty inventoryAttributeFluxForm.productAttributeFluxId}">
                                    <form:input path="batchNumber" cssClass="form-control form-control-sm" readonly="true"  />
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${empty inventoryAttributeFluxForm.productAttributeFluxId}">
                                    <form:input path="expiryDate" cssClass="form-control form-control-sm picker" />
                                </c:if>
                                <c:if test="${not empty inventoryAttributeFluxForm.productAttributeFluxId}">
                                    <form:input path="expiryDate" cssClass="form-control form-control-sm picker" readonly="true"  />
                                </c:if>
                            </td>
                            <td>
                                <form:input path="quantity" cssClass="form-control form-control-sm" />
                            </td>
                            <td>
                                <form:input path="observation" cssClass="form-control form-control-sm" />
                            </td>
                            <td>
                                <button class="btn btn-success">
                                    <c:if test="${not empty inventoryAttributeFluxForm.productAttributeFluxId}">
                                        <i class="fa fa-edit"></i>
                                    </c:if>
                                    <c:if test="${empty inventoryAttributeFluxForm.productAttributeFluxId}">
                                        <i class="fa fa-plus"></i>
                                    </c:if>
                                </button>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td colspan="8"></td>
                    </tr>
                    <c:forEach var="productFlux" items="${productAttributeFluxes}">
                        <tr>
                            <td>${productFlux.code}</td>
                            <td>
                                    ${ productFlux.retailName}
                            </td>
                            <td>
                                    ${productFlux.retailUnit}
                            </td>
                            <td class="text-center">${productFlux.batchNumber}</td>
                            <td class="text-center">
                                <fmt:formatDate value="${productFlux.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>
                            </td>
                            <td class="text-center">
                                    ${productFlux.quantity}
                            </td>
                            <td>${productFlux.observation}</td>
                            <td>
                                <c:if test="${productInventory.operationStatus == 'NOT_COMPLETED'}">
                                    <c:url value="/module/pharmacy/operations/inventory/editFlux.form" var="editUrl">
                                        <c:param name="inventoryId" value="${productInventory.productOperationId}"/>
                                        <c:param name="fluxId" value="${productFlux.productAttributeFluxId}"/>
                                    </c:url>
                                    <a href="${editUrl}" class="text-info"><i class="fa fa-edit"></i></a>
                                    <c:url value="/module/pharmacy/operations/inventory/deleteFlux.form" var="deleteUrl">
                                        <c:param name="inventoryId" value="${productInventory.productOperationId}"/>
                                        <c:param name="fluxId" value="${productFlux.productAttributeFluxId}"/>
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
        <c:if test="${productInventory.operationStatus != 'NOT_COMPLETED'}">
            <table class="table table-condensed table-striped table-sm table-bordered">
                <thead class="thead-light">
                <tr class="bg-belize-hole">
                    <th colspan="3" style="width: 250px">Produit</th>
                    <th style="width: 200px">Num&eacute;ro de lot </th>
                    <th style="width: 150px">Date de p&eacute;remption </th>
                    <th style="width: 60px">Quantit&eacute; physique</th>
                    <th style="width: 250px">observation</th>
                </tr>
                </thead>
                <c:forEach var="productFlux" items="${productAttributeFluxes}">
                    <tr>
                        <td>${productFlux.code}</td>
                        <td>
                                ${productFlux.retailName}
                        </td>
                        <td>
                                ${productFlux.retailUnit}
                        </td>
                        <td class="text-center">${productFlux.batchNumber}</td>
                        <td class="text-center">
                            <fmt:formatDate value="${productFlux.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>
                        </td>
                        <td class="text-center">
                                ${productFlux.quantity}
                        </td>
                        <td>${productFlux.observation}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </div>

</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
