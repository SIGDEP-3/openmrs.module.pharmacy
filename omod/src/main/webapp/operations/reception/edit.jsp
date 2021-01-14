<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../template/operationHeader.jsp"%>
<openmrs:require privilege="Save Reception" otherwise="/login.htm" redirect="/module/pharmacy/operations/reception/edit.form" />

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('#form').on('submit', function (e){
                e.preventDefault();

            })
        });
        function saveOnly(e) {
            e.preventDefault();
            const forms = document.getElementsByTagName('form');
            let form = forms[0];
            // if (form.getAttribute('action').includes('action=save')) {
            //     form.submit();
            // } else
            if (form.getAttribute('action').includes('action=addLine')) {
                form.setAttribute('action', form.getAttribute('action').replace('addLine', 'save'));
            } else {
                if (form.getAttribute('action').includes('?id=')) {
                    form.setAttribute('action', form.getAttribute('action') + '&action=save');
                } else {
                    form.setAttribute('action', form.getAttribute('action') + '?action=save');
                }
            }

            form.submit();
        }

        function saveAndAddProducts(e) {
            e.preventDefault();
            const forms = document.getElementsByTagName('form');
            let form = forms[0];
            if (form.getAttribute('action').includes('action=save')) {
                form.setAttribute('action', form.getAttribute('action').replace('save', 'addLine'));
            } else {
                if (form.getAttribute('action').includes('?id=')) {
                    form.setAttribute('action', form.getAttribute('action') + '&action=addLine');
                } else {
                    form.setAttribute('action', form.getAttribute('action') + '&action=addLine');
                }
            }
            form.submit();
        }
    }
</script>

<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6 text-uppercase font-italic text-secondary">
            <div class="h6"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">
            <c:url value="/module/pharmacy/operations/reception/list.form" var="url"/>
            <button class="btn btn-primary btn-sm" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border ${productReceptionForm.incidence == 'NEGATIVE' ? 'border-warning' : 'border-secondary'}">
        <div class="col-12">
            <form:form modelAttribute="productReceptionForm" method="post" action="" id="form">
                <form:hidden path="productOperationId"/>
                <form:hidden path="uuid"/>
                <form:hidden path="locationId"/>
                <form:hidden path="incidence"/>
                <form:hidden path="operationStatus"/>
                <form:hidden path="productProgramId"/>
                <c:if test="${productReceptionForm.incidence == 'NEGATIVE'}">
                    <form:hidden path="productSupplierId"/>
                </c:if>
                <div class="row">
                    <div class="col-6">
                        <div class="row">
                            <div class="col-10 mb-2">
                                <c:if test="${productReceptionForm.incidence == 'NEGATIVE'}">
                                    <label>Fournisseur de la r&eacute;ception</label>
                                    <div class="form-control form-control-sm">${reception.productSupplier.name}</div>
                                </c:if>
                                <c:if test="${productReceptionForm.incidence == 'POSITIVE'}">
                                    <label>Fournisseur <span class="required">*</span></label>
                                    <br>
                                    <form:select path="productSupplierId" cssClass="form-control s2" >
                                        <form:option value="" label=""/>
                                        <form:options items="${suppliers}" itemValue="productSupplierId" itemLabel="name" />
                                    </form:select>
                                    <form:errors path="productSupplierId" cssClass="error"/>
                                </c:if>

                            </div>
                        </div>
                        <div class="row">
                            <div class="col-6 mb-2">
                                <label>Programme <span class="required">*</span></label>
                                <div class="form-control form-control-sm">
                                    <c:if test="${productReceptionForm.incidence == 'NEGATIVE'}">
                                        ${reception.productProgram.name}
                                    </c:if>
                                    <c:if test="${productReceptionForm.incidence == 'POSITIVE'}">
                                        ${program.name}
                                    </c:if>
                                </div>
<%--                                <c:if test="${fct:length(productReception.productAttributeFluxes) == 0}">--%>
<%--                                    <form:select path="productProgramId" cssClass="form-control s2" >--%>
<%--                                        <form:option value="" label=""/>--%>
<%--                                        <form:options items="${programs}" itemValue="productProgramId" itemLabel="name" />--%>
<%--                                    </form:select>--%>
<%--                                    <form:errors path="productProgramId" cssClass="error"/>--%>
<%--                                </c:if>--%>
<%--                                <c:if test="${fct:length(productReception.productAttributeFluxes) != 0}">--%>
<%--                                    <form:select path="productProgramId" cssClass="form-control s2" disabled="true" title="" >--%>
<%--                                        <form:option value="" label=""/>--%>
<%--                                        <form:options items="${programs}" itemValue="productProgramId" itemLabel="name" />--%>
<%--                                    </form:select>--%>
<%--                                </c:if>--%>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row mb-2">
                            <c:if test="${productReceptionForm.incidence == 'NEGATIVE'}">
                                <div class="col-4">
                                    <label>Date de retour <span class="required">*</span></label>
                                    <form:input path="operationDate" cssClass="form-control form-control-sm picker" />
                                    <form:errors path="operationDate" cssClass="error"/>
                                </div>
                                <div class="col-4">
                                    <label>Date de la reception </label>
                                    <div class="form-control form-control-sm bg-info text-white">
                                        <fmt:formatDate value="${reception.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${productReceptionForm.incidence == 'POSITIVE'}">
                                <div class="col-4">
                                    <label>Date de r&eacute;ception <span class="required">*</span></label>
                                    <form:input path="operationDate" cssClass="form-control form-control-sm picker" />
                                    <form:errors path="operationDate" cssClass="error"/>
                                </div>
                            </c:if>
                        </div>
                        <div class="row mb-2">
                            <div class="col-4">
                                <label>Numero de reception</label>
                                <c:if test="${productReceptionForm.incidence == 'NEGATIVE'}">
                                    <form:input path="operationNumber" cssClass="form-control form-control-sm" readonly="true" />
                                </c:if>
                                <c:if test="${productReceptionForm.incidence == 'POSITIVE'}">
                                    <form:input path="operationNumber" cssClass="form-control form-control-sm" />
                                </c:if>
                                <form:errors path="operationNumber" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-6">
                        <div class="row mb-2">
                            <div class="col-10">
                                <label class="mb-2">Unite de saisie</label> <br>
                                <form:radiobutton path="receptionQuantityMode" value="RETAIL" label="Dispensation" cssClass="mr-2"/>
                                <form:radiobutton path="receptionQuantityMode" value="WHOLESALE"  label="Conditionnement" cssClass="mr-2"/>
                                <form:errors path="receptionQuantityMode" cssClass="error"/>
                            </div>

                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <div class="col-12 mb-2">
                                <label>
                                    <c:if test="${productDistributionForm.incidence == 'NEGATIVE'}">
                                        Motifs de retour de produits
                                    </c:if>
                                    <c:if test="${productDistributionForm.incidence == 'POSITIVE'}">
                                        Obseration
                                    </c:if>
                                </label>
                                <form:textarea path="observation" cssClass="form-control form-control-sm" />
                                <form:errors path="observation" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-12">
                        <c:if test="${not empty productReceptionForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveOnly(event)">
                                <i class="fa fa-save"></i> Modifier
                            </button>
                        </c:if>
                        <c:if test="${ empty productReceptionForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveOnly(event)">
                                <i class="fa fa-edit"></i> Sauvegarder
                            </button>
                        </c:if>

                        <button class="btn btn-primary" onclick="saveAndAddProducts(event)">Saisie des produits <i class="fa fa-tablets"></i></button>

                        <c:if test="${productReceptionForm.operationStatus == 'AWAITING_VALIDATION'}">
                            <button class="btn btn-success" name="action" value="add">
                                <i class="fa fa-edit"></i>
                                <spring:message code="pharmacy.validate" />
                            </button>
                        </c:if>
                    </div>
                </div>

            </form:form>
        </div>
    </div>
</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
