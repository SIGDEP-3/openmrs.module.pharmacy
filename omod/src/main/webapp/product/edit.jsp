<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../template/localHeader.jsp"%>
<openmrs:require privilege="Save Product"
                 otherwise="/login.htm" redirect="/module/pharmacy/product/edit.form" />

<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/product/list.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
            <i class="fa fa-list"></i>
        </button>
    </div>
</div>
<hr>
<div class="container">
    <form:form modelAttribute="productForm" method="post" action="" id="form">
        <form:hidden path="productId"/>
        <form:hidden path="uuid"/>

        <div class="row">
            <div class="col-2 mb-2">
                <labe>Code <span class="required">*</span></labe>
                <form:input path="code" cssClass="form-control form-control-sm" />
                <form:errors path="code" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-8 mb-2">
                <labe>D&eacute;signation <span class="required">*</span></labe>
                <form:input path="retailName" cssClass="form-control form-control-sm" />
                <form:errors path="retailName" cssClass="error"/>
            </div>
            <div class="col-3 mb-2">
                <labe>Unit&eacute; <span class="required">*</span></labe>
                <form:select path="productRetailUnitId" cssClass="form-control s2" >
                    <form:option value="" label=""/>
                    <form:options items="${availableRetailUnits}" itemValue="productUnitId" itemLabel="name" />
                </form:select>
                <form:errors path="productRetailUnitId" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-8 mb-2">
                <labe>D&eacute;signation (Conditionnement) <span class="required">*</span></labe>
                <form:input path="wholesaleName" cssClass="form-control form-control-sm" />
                <form:errors path="wholesaleName" cssClass="error"/>
            </div>
            <div class="col-3 mb-2">
                <labe>Unit&eacute; (Conditionnement) <span class="required">*</span></labe>
                <form:select path="productWholesaleUnitId" cssClass="form-control s2" >
                    <form:option value="" label=""/>
                    <form:options items="${availableWholesaleUnits}" itemValue="productUnitId" itemLabel="name" />
                </form:select>
                <form:errors path="productWholesaleUnitId" cssClass="error"/>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-2">
                <labe>Unit&eacute; de conversion <span class="required">*</span></labe>
                <form:input path="unitConversion" cssClass="form-control form-control-sm" />
                <form:errors path="unitConversion" cssClass="error"/>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-6">
                <label>Programmes <span class="required">*</span></label>
                <form:select path="productProgramIds" cssClass="form-control s2" multiple="true">
                    <form:option value="" label=""/>
                    <form:options items="${availablePrograms}" itemValue="productProgramId" itemLabel="name" />
                </form:select>
                <form:errors path="productProgramIds" cssClass="error"/>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-6">
                <label>Regimes</label>
                <form:select path="productRegimenIds" cssClass="form-control s2" multiple="true">
                    <form:option value="" label=""/>
                    <form:options items="${availableRegimens}" itemValue="productRegimenId" itemLabel="concept.name.name" />
                </form:select>
                <form:errors path="productRegimenIds" cssClass="error"/>
            </div>
        </div>

        <hr>

        <div class="row">
            <div class="col-12">
                <button class="btn btn-success">
                    <c:if test="${not empty productForm.productId}">
                        <i class="fa fa-edit"></i>
                        <spring:message code="pharmacy.edit" />
                    </c:if>
                    <c:if test="${empty productForm.productId}">
                        <i class="fa fa-save"></i>
                        <spring:message code="pharmacy.save" />
                    </c:if>
                </button>
            </div>
        </div>

    </form:form>
</div>


<%@ include file="../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
