<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../template/operationHeader.jsp"%>

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
            form.setAttribute('action', form.getAttribute('action').split("?")[0] + "?action=save");
            form.submit();
        }

        function saveAndAddProducts(e) {
            e.preventDefault();
            const forms = document.getElementsByTagName('form');
            let form = forms[0];
            form.setAttribute('action', form.getAttribute('action').split("?")[0] + "?action=addLine");
            form.submit();
        }
    }
</script>

<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6">
            <div class="h5"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">
            <c:url value="/module/pharmacy/operations/movement/list.form" var="url"/>
            <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <form:form modelAttribute="productMovementEntryForm" method="post" action="" id="form">
                <form:hidden path="productOperationId"/>
                <form:hidden path="uuid"/>
                <form:hidden path="locationId"/>
                <form:hidden path="incidence"/>
                <form:hidden path="operationStatus"/>
                <div class="row">
                    <div class="col-6">
                        <div class="row">
                            <div class="col-10 mb-2">
                                <labe>Type Mouvement <span class="required">*</span></labe>
                                <form:input path="stockEntryType" cssClass="form-control form-control-sm" />
                                <form:errors path="stockEntryType" cssClass="error"/>
<%--                                <form:select path="stockEntryType" cssClass="form-control s2" >--%>
<%--                                    <form:option value="" label=""/>--%>
<%--                                    <form:options items="${stockEntryType}" itemValue="stockEntryType" itemLabel="stockEntryType" />--%>
<%--                                </form:select>--%>
<%--                                <form:errors path="stockEntryType" cssClass="error"/>--%>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-6 mb-2">
                                <labe>Programme <span class="required">*</span></labe>
                                <form:select path="productProgramId" cssClass="form-control s2" >
                                    <form:option value="" label=""/>
                                    <form:options items="${programs}" itemValue="productProgramId" itemLabel="name" />
                                </form:select>
                                <form:errors path="productProgramId" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <div class="col-3 mb-2">
                                <labe>Date de mouvement <span class="required">*</span></labe>
                                <form:input path="operationDate" cssClass="form-control form-control-sm picker" />
                                <form:errors path="operationDate" cssClass="error"/>
                            </div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-4">
                                <labe>Expéditeur</labe>
                                <form:input path="sender" cssClass="form-control form-control-sm" />
                                <form:errors path="sender" cssClass="error"/>
                            </div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-4">
                                <labe>Destinataire</labe>
                                <form:input path="operationNumber" cssClass="form-control form-control-sm" />
                                <form:errors path="operationNumber" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-4">
                        <labe>Motif</labe>
                        <form:textarea path="operationNumber" cssClass="form-control form-control-sm" />
                        <form:errors path="operationNumber" cssClass="error"/>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <div class="col-12 mb-2">
                                <labe>Bon de Livaison</labe>
                                <form:input path="observation" cssClass="form-control form-control-sm" />
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
