<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../template/operationHeader.jsp"%>
<openmrs:require privilege="Save Inventory" otherwise="/login.htm" redirect="/module/pharmacy/operations/inventory/editFlux.form" />

<script>
    if (jQuery) {

        jQuery(document).ready(function (){
            jQuery('.input-value-flux').on('change', function () {
                const inputSelected = jQuery(this);
                const quantity = inputSelected.val();
                const batchNumber = inputSelected.attr('id');
                saveFlux(batchNumber, quantity);
            });

            jQuery('.input-observation').on('change', function () {
                const inputSelected = jQuery(this);
                const observation = inputSelected.val();
                const batchNumber = inputSelected.attr('id');
                addObservation(batchNumber, observation);
            });

        });

        function saveFlux(batchNumber, quantity) {
            if (quantity !== 0) {
                jQuery.ajax({
                    type: 'GET',
                    url: '${pageContext.request.contextPath}/save-flux.form?batchNumber=' +
                        batchNumber + '&operationId=${productInventory.productOperationId}&quantity='+ quantity,
                    dataType : "json",
                    crossDomain:true,
                    success : function(data) {
                        // console.log(data);
                        //jQuery('.' + batchNumber).parent().html(data.quantity)
                        //alert(data.toString());
                    },
                    error : function(data) {
                        console.log(data)
                    }
                });
            }

        }

        function addObservation(batchNumber, observation) {
            if (observation) {
                jQuery.ajax({
                    type: 'GET',
                    url: '${pageContext.request.contextPath}/save-observation.form?batchNumber=' +
                        batchNumber + '&operationId=${productInventory.productOperationId}&observation='+ observation,
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
        <div class="col-6 text-uppercase font-italic text-secondary">
            <div class="h6"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">

            <c:if test="${productInventory.operationStatus != 'VALIDATED' &&
                      productInventory.operationStatus != 'DISABLED'}">

                <c:if test="${productInventory.operationStatus == 'NOT_COMPLETED' && fct:length(productAttributeFluxes) != 0}">
                        <c:url value="/module/pharmacy/operations/inventory/complete.form" var="completeUrl">
                        <c:param name="inventoryId" value="${productInventory.productOperationId}"/>
                    </c:url>
                        <button class="btn btn-success btn-sm mr-2" onclick="window.location='${completeUrl}'">
                            <i class="fa fa-save"></i> Terminer
                        </button>
                </c:if>
                <c:if test="${productInventory.operationStatus != 'NOT_COMPLETED'}">
                    <c:url value="/module/pharmacy/operations/inventory/incomplete.form" var="incompleteUrl">
                        <c:param name="inventoryId" value="${productInventory.productOperationId}"/>
                    </c:url>
                    <button class="btn btn-primary btn-sm mr-2" onclick="window.location='${incompleteUrl}'">
                        <i class="fa fa-pen"></i> Editer l'inventaire
                    </button>
                    <openmes:hasPrivilege privilege="Validate Dispensation">
                        <c:url value="/module/pharmacy/operations/inventory/validate.form" var="validationUrl">
                            <c:param name="inventoryId" value="${productInventory.productOperationId}"/>
                        </c:url>
                        <button class="btn btn-success btn-sm mr-2" onclick="window.location='${validationUrl}'">
                            <i class="fa fa-pen"></i> Valider
                        </button>
                    </openmes:hasPrivilege>
                </c:if>
            </c:if>
            <c:if test="${productInventory.operationStatus == 'NOT_COMPLETED'}">
                <c:url value="/module/pharmacy/operations/inventory/edit.form" var="editUrl">
                    <c:param name="id" value="${productInventory.productOperationId}"/>
                </c:url>
                <button class="btn btn-primary btn-sm" onclick="window.location='${editUrl}'" title="Voir la liste">
                    <i class="fa fa-edit"></i> Editer l'ent&ecirc;te
                </button>
            </c:if>
            <c:url value="/module/pharmacy/operations/inventory/list.form" var="url"/>
            <button class="btn btn-primary btn-sm" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light border border-secondary">
        <div class="col-12 m-1">
            <table class="bg-light table table-borderless table-light border">
                <thead class="thead-light">
                <tr>
                    <td>Programme </td>
                    <td class="font-weight-bold text-info">${productInventory.productProgram.name}</td>
                    <td>Date dernier inventaire</td>
                    <td class="font-weight-bold text-info">
                        <c:if test="${latestInventory != null}">
                            <fmt:formatDate value="${latestInventory.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                        </c:if>
                        <c:if test="${latestInventory == null}">
                            C'est votre premier inventaire
                        </c:if>

                    </td>
                </tr>
                <tr>
                    <td>Date d'inventaire</td>
                    <td class="font-weight-bold text-info">
                        <fmt:formatDate value="${productInventory.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                    </td>
                    <td>Num&eacute;ro de la pi&egrave;ce</td>
                    <td class="font-weight-bold text-info">${productInventory.operationNumber}</td>
                </tr>
                <tr>
                    <td>Type d'inventaire</td>
                    <td class="font-weight-bold text-info">${productInventory.inventoryType == 'TOTAL' ? 'TOTAL' : 'PARTIEL'}</td>
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
                        <form:errors path="expiryDate" cssClass="error"/>
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
                        <c:if test="${productInventory.operationStatus == 'NOT_COMPLETED'
                                        && productInventory.inventoryType == 'TOTAL'}">
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
                                <td class="align-middle">${productFlux.productAttribute.product.code}</td>
                                <td class="align-middle">${ productFlux.productAttribute.product.retailName}</td>
                                <td class="align-middle">${productFlux.productAttribute.product.productRetailUnit.name}</td>
                                <td class="text-center align-middle">${productFlux.productAttribute.batchNumber}</td>
                                <td class="text-center align-middle">
                                    <fmt:formatDate value="${productFlux.productAttribute.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                </td>
                                <td class="text-center align-middle">
                                    <c:if test="${productFlux.quantity == 0}">
                                        <input type="text"
                                               class="form-control form-control-sm text-center input-value-flux"
                                               id="${productFlux.productAttribute.batchNumber}"
                                               name="${productFlux.productAttribute.batchNumber}">
                                    </c:if>
                                    <c:if test="${productFlux.quantity != 0}">
                                        ${productFlux.quantity}
                                    </c:if>
                                </td>
                                <td class="align-middle">${productFlux.observation}</td>
                                <td class="align-middle text-center">
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
                        <th style="width: 150px">Num&eacute;ro de lot </th>
                        <th style="width: 150px">Date de p&eacute;remption </th>
                        <th style="width: 60px">Quantit&eacute; physique</th>
                        <th style="width: 60px">Quantit&eacute; th&eacute;orique</th>
                        <th style="width: 100px">info / &eacute;cart</th>
                        <th style="width: 200px">observation</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="productFlux" items="${productAttributeFluxes}">
                        <c:if test="${productInventory.productOperationId == productFlux.operationId && productInventory.inventoryType == 'TOTAL' ||
                    (productInventory.inventoryType == 'PARTIAL' && productFlux.physicalQuantity != 0)}">

                            <tr
                                    <c:if test="${productFlux.physicalQuantity != null && productFlux.theoreticalQuantity != null} ">
                                        <c:if test="${productFlux.physicalQuantity != productFlux.theoreticalQuantity && latestInventory != null}">
                                            class="bg-danger text-white"
                                        </c:if>
                                    </c:if>
                                    <c:if test="${productFlux.physicalQuantity == null || productFlux.theoreticalQuantity == null}">
                                        <c:if test="${productFlux.physicalQuantity == null}">
                                            class="bg-warning"
                                        </c:if>

                                        <c:if test="${productFlux.theoreticalQuantity == null}">
                                            <c:if test="${latestInventory != null}">
                                                class="bg-info"
                                            </c:if>

                                        </c:if>
                                    </c:if>
                            >
                                <td class="align-middle">${productFlux.code}</td>
                                <td class="align-middle">${productFlux.retailName}</td>
                                <td class="align-middle">${productFlux.retailUnit}</td>
                                <td class="text-center align-middle">${productFlux.batchNumber}</td>
                                <td class="text-center align-middle">
                                    <fmt:formatDate value="${productFlux.expiryDate}" pattern="dd/MM/yyyy"
                                                    type="DATE"/>
                                </td>
                                <td class="text-center align-middle">${productFlux.physicalQuantity}</td>
                                <td class="text-center align-middle">${productFlux.theoreticalQuantity}</td>
                                <td class="text-center align-middle">
                                    <c:if test="${productFlux.physicalQuantity != null && productFlux.theoreticalQuantity != null}">
                                        <c:if test="${ productFlux.physicalQuantity != productFlux.theoreticalQuantity}">
                                            <span class="badge badge-danger">${productFlux.physicalQuantity - productFlux.theoreticalQuantity}</span>
                                        </c:if>
                                        <c:if test="${ productFlux.physicalQuantity == productFlux.theoreticalQuantity}">
                                            <span class="badge badge-success">OK</span>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${productFlux.physicalQuantity == null || productFlux.theoreticalQuantity == null}">
                                        <c:if test="${productFlux.physicalQuantity == null}">
                                            Doit faire partie de l'inventaire
                                        </c:if>

                                        <c:if test="${productFlux.theoreticalQuantity == null}">
                                            <c:if test="${latestInventory == null}">
                                                <span class="badge badge-success">OK</span>
                                            </c:if>
                                            <c:if test="${latestInventory != null}">
                                                <span class="badge badge-warning">Pas en stock</span>
                                            </c:if>
                                        </c:if>
                                    </c:if>
                                </td>
                                <td class="align-middle">
                                    <c:if test="${productInventory.operationStatus == 'AWAITING_VALIDATION'}">
                                        <c:if test="${ productFlux.physicalQuantity != productFlux.theoreticalQuantity}">
                                            <input type="text" name="${productFlux.batchNumber}"
                                                   id="${productFlux.batchNumber}"
                                                   value="${productFlux.observation}"
                                                   class="form-control form-control-sm input-observation">
                                        </c:if>
                                        <c:if test="${ productFlux.physicalQuantity == productFlux.theoreticalQuantity}">
                                            ${productFlux.observation}
                                        </c:if>
                                    </c:if>
                                    <c:if test="${productInventory.operationStatus == 'VALIDATED'}">
                                        ${productFlux.observation}
                                    </c:if>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>

</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
