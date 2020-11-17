<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../template/operationHeader.jsp"%>

<div class="container-fluid mt-0">
    <div class="row">
        <div class="col-6">
            <h5>${title}</h5>
        </div>
        <div class="col-6 text-right">
            <c:url value="/module/pharmacy/operations/reception/list.form" var="url"/>
            <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i>
            </button>
        </div>
    </div>
    <hr>

    <div class="card mb-0">
        <div class="card-body">
            <table class="bg-light table table-borderless table-sm table-striped m-1">
                <tr>
                    <td>Date de reception</td>
                    <td class="font-weight-bold text-info">
                        <fmt:formatDate value="${productReception.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                        </td>
                    <td>Type de saisie</td>
                    <td class="font-weight-bold text-info">${productReception.receptionQuantityMode}</td>
                </tr>
                <tr>
                    <td>Fournisseur</td>
                    <td class="font-weight-bold text-info">${productReception.productSupplier.name}</td>
                    <td>Bordereau de livraison</td>
                    <td class="font-weight-bold text-info">${productReception.operationNumber}</td>
                </tr>
                <tr>
                    <td>Programme : </td>
                    <td class="font-weight-bold text-info">${productReception.productProgram.name}</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <form:form modelAttribute="receptionAttributeFluxForm" method="post" action="" id="form">
                <form:hidden path="productAttributeFluxId"/>
                <form:hidden path="productOperationId"/>
                <form:hidden path="locationId"/>
                <div>
                    <form:errors path="productId" cssClass="error"/>
                    <form:errors path="batchNumber" cssClass="error"/>
                    <form:errors path="expiryDate" cssClass="error"/> <br>
                    <form:errors path="receptionQuantity" cssClass="error"/>
                    <form:errors path="receivedQuantity" cssClass="error"/>
                    <form:errors path="observation" cssClass="error"/>
                </div>
                <table class="table table-condensed table-striped table-sm table-bordered">
                    <thead class="thead-light">
                    <tr class="bg-belize-hole">
                        <th colspan="3" style="width: 250px">Produit <span class="required">*</span></th>
                        <th style="width: 200px">Numero <br>de lot <span class="required">*</span></th>
                        <th style="width: 150px">Date de <br>peremption <span class="required">*</span></th>
                        <th style="width: 60px">Quantite <br>livree <span class="required">*</span></th>
                        <th style="width: 60px">Quantite <br>recue <span class="required">*</span></th>
                        <th style="width: 250px">observation</th>
                        <th style="width: 50px"></th>
                    </tr>
                    </thead>
                    <tr>
                        <td colspan="3">
                            <form:select path="productId" cssClass="form-control s2" >
                                <form:option value="" label=""/>
                                <form:options items="${products}" itemValue="productId" itemLabel="retailNameWithCode" />
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
                        <td>
                            <form:input path="receptionQuantity" cssClass="form-control form-control-sm" />
                        </td>
                        <td>
                            <form:input path="receivedQuantity" cssClass="form-control form-control-sm" />
                        </td>
                        <td>
                            <form:input path="observation" cssClass="form-control form-control-sm" />
                        </td>
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
                    </tr>
                    <tr>
                        <td colspan="7">
                        </td>
                    </tr>
                    <c:forEach var="productFlux" items="${productAttributeFluxes}">
                        <tr>
                            <td>${productFlux.code}</td>
                            <td>${productFlux.retailName}</td>
                            <td>${productFlux.retailUnit}</td>
                            <td class="text-center">${productFlux.batchNumber}</td>
                            <td class="text-center">
                                <fmt:formatDate value="${productFlux.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>
                            </td>
                            <td class="text-center">${productFlux.deliveredQuantity}</td>
                            <td class="text-center">${productFlux.receivedQuantity}</td>
                            <td>${productFlux.observation}</td>
                            <td>
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
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${fct:length(productAttributeFluxes) == 0}">
                        <tr><td colspan="7" class="text-center text-warning">Aucun produit dans la liste</td></tr>
                    </c:if>
                </table>
            </form:form>
        </div>
    </div>

</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
