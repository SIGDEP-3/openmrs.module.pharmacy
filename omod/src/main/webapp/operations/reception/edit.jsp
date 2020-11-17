<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../template/operationHeader.jsp"%>

<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/operations/reception/list.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
            <i class="fa fa-list"></i>
        </button>
    </div>
</div>
<hr>
<div class="container">
    <form:form modelAttribute="receptionHeaderForm" method="post" action="" id="form">
        <form:hidden path="productOperationId"/>
        <form:hidden path="uuid"/>
        <form:hidden path="locationId"/>
        <form:hidden path="incidence"/>
        <form:hidden path="operationStatus"/>

        <div class="row">
            <div class="col-3 mb-2">
                <labe>Date de reception <span class="required">*</span></labe>
                <form:input path="operationDate" cssClass="form-control form-control-sm picker" />
                <form:errors path="operationDate" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-6 mb-2">
                <labe>Fournisseur <span class="required">*</span></labe>
                <form:select path="productSupplierId" cssClass="form-control s2" >
                    <form:option value="" label=""/>
                    <form:options items="${suppliers}" itemValue="productSupplierId" itemLabel="name" />
                </form:select>
                <form:errors path="productSupplierId" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-4 mb-2">
                <labe>Programme <span class="required">*</span></labe>
                <form:select path="productProgramId" cssClass="form-control s2" >
                    <form:option value="" label=""/>
                    <form:options items="${programs}" itemValue="productProgramId" itemLabel="name" />
                </form:select>
                <form:errors path="productProgramId" cssClass="error"/>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-4">
                <labe>Numero de reception</labe>
                <form:input path="operationNumber" cssClass="form-control form-control-sm" />
                <form:errors path="operationNumber" cssClass="error"/>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-10">
                <labe>Type de saisie</labe>
                <br>
                <form:radiobutton path="receptionQuantityMode" value="RETAIL" cssClass=""/> Unite de dispensation
                <form:radiobutton path="receptionQuantityMode" value="WHOLESALE" cssClass=""/> Unite de conditionnement
                <form:errors path="receptionQuantityMode" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-8 mb-2">
                <labe>Observations</labe>
                <form:textarea path="observation" cssClass="form-control form-control-sm" />
                <form:errors path="observation" cssClass="error"/>
            </div>
        </div>

        <hr>

        <div class="row">
            <div class="col-12">
                <button class="btn btn-primary"> Saisie des produits <i class="fa fa-arrow-right"></i></button>
                <c:if test="${receptionHeaderForm.operationStatus == OperationStatus.AWAITING_VALIDATION}">
                    <button class="btn btn-success">
                        <i class="fa fa-edit"></i>
                        <spring:message code="pharmacy.validate" />
                    </button>
                </c:if>
            </div>
        </div>

    </form:form>
</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
