<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../../template/operationHeader.jsp"%>

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
            form.setAttribute('action', form.getAttribute('action') + "&action=save");
            form.submit();
        }

        function saveAndAddProducts(e) {
            e.preventDefault();
            const forms = document.getElementsByTagName('form');
            let form = forms[0];
             form.setAttribute('action', form.getAttribute('action') + "&action=addLine");
            // if (form.getAttribute('action').includes('action=addLine')) {
            //     form.setAttribute('action', form.getAttribute('action').replace('addLine', 'save'));
            // } else {
            //     if (form.getAttribute('action').includes('?id=')) {
            //         form.setAttribute('action', form.getAttribute('action') + '&action=save');
            //     } else {
            //         form.setAttribute('action', form.getAttribute('action') + '?action=save');
            //     }
            // }
            form.submit();
        }
    }
</script>

<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6">
            <div class="h6"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">
            <c:url value="/module/pharmacy/operations/movement/other/list.form" var="url"/>
            <button class="btn btn-primary btn-sm" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <form:form modelAttribute="productMovementForm" method="post" action="" id="form">
                <form:hidden path="productOperationId"/>
                <form:hidden path="uuid"/>
                <form:hidden path="locationId"/>
                <form:hidden path="incidence"/>
                <form:hidden path="productProgramId"/>
                <form:hidden path="operationStatus"/>
                <c:if test="${movementType == 'entry'}">
                    <form:hidden path="stockEntryType"/>
                </c:if>
                <c:if test="${movementType == 'out'}">
                    <form:hidden path="stockOutType"/>
                </c:if>

                <div class="row">
                    <div class="col-6">
                        <div class="row">
                            <div class="col-6 mb-2">
                                <label>Programme<span class="required">*</span></label>
                                <div class="form-control form-control-sm">
                                        ${program.name}
                                </div>
                                    <%--                                <form:select path="productProgramId" cssClass="form-control s2" >--%>
                                    <%--                                    <form:option value="" label=""/>--%>
                                    <%--                                    <form:options items="${programs}" itemValue="productProgramId" itemLabel="name" />--%>
                                    <%--                                </form:select>--%>
                                    <%--                                <form:errors path="productProgramId" cssClass="error"/>--%>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <div class="col-4 mb-2">
                                <label>Date de mouvement <span class="required">*</span></label>
                                <form:input path="operationDate" cssClass="form-control form-control-sm picker" />
                                <form:errors path="operationDate" cssClass="error"/>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="row">
                    <c:if test="${fct:contains(type, 'TRANSFER') || type == 'DONATION'}">
                        <div class="col-6">
                            <div class="row">
                                <div class="col-10 mb-2">
                                    <label>
                                        <c:if test="${movementType == 'entry'}">Exp&eacute;diteur</c:if>
                                        <c:if test="${movementType == 'out'}">Destinataire</c:if>
                                    </label>
                                    <form:select path="entityId" cssClass="form-control s2 form-control-sm" >
                                        <form:option value="" label=""/>
                                        <form:options items="${exchanges}" itemValue="productExchangeEntityId" itemLabel="name" />
                                    </form:select>
                                    <form:errors path="entityId" cssClass="error"/>

                                </div>
                            </div>
                        </div>

                    </c:if>
                    <div class="col-6">
                        <div class="row">
                            <div class="col-12">
                                <label>Observations</label>
                                <form:textarea path="observation" cssClass="form-control form-control-sm" />
                                <form:errors path="observation" cssClass="error"/>
                            </div>
                        </div>
                    </div>

                </div>
                <hr>

                <div class="row">
                    <div class="col-12">
                        <c:if test="${not empty productMovementForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveOnly(event)">
                                <i class="fa fa-save"></i> Modifier
                            </button>
                        </c:if>
                        <c:if test="${ empty productMovementForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveOnly(event)">
                                <i class="fa fa-edit"></i> Sauvegarder
                            </button>
                        </c:if>

                        <button class="btn btn-primary" onclick="saveAndAddProducts(event)">Saisie des produits <i class="fa fa-tablets"></i></button>

<%--                        <c:if test="${productMovementForm.operationStatus == 'AWAITING_VALIDATION'}">--%>
<%--                            <button class="btn btn-success" name="action" value="add">--%>
<%--                                <i class="fa fa-edit"></i>--%>
<%--                                <spring:message code="pharmacy.validate" />--%>
<%--                            </button>--%>
<%--                        </c:if>--%>
                    </div>
                </div>

            </form:form>
        </div>
    </div>
</div>

<%@ include file="../../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
