<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacy/product/regimens/edit.form" />

<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/product/regimens/list.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
            <i class="fa fa-list"></i>
        </button>
    </div>
</div>
<hr>
<div class="container">
    <form:form modelAttribute="regimenForm" method="post" action="" id="form">
        <form:hidden path="productRegimenId"/>
        <form:hidden path="uuid"/>

        <div class="row">
            <div class="col-6 mb-2">
                <labe>Concept <span class="required">*</span></labe>
                <form:select path="conceptId" cssClass="form-control s2" >
                    <form:option value="" label=""/>
                    <form:options items="${conceptList}" itemValue="conceptId" itemLabel="name.name" />
                </form:select>
                <form:errors path="conceptId" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-8 mb-2">
                <label>Produits</label>
                <form:select path="productIds" cssClass="form-control s2" multiple="true" >
                    <form:option value="" label=""/>
                    <form:options items="${products}" itemValue="productId" itemLabel="retailNameWithCode" />
                </form:select>
                <form:errors path="productIds" cssClass="error"/>

            </div>
        </div>
        <div class="row">
            <div class="col-12">
                <button class="btn btn-success">
                    <c:if test="${not empty regimenForm.productRegimenId}">
                        <i class="fa fa-edit"></i>
                        <spring:message code="pharmacy.edit" />
                    </c:if>
                    <c:if test="${empty regimenForm.productRegimenId}">
                        <i class="fa fa-save"></i>
                        <spring:message code="pharmacy.save" />
                    </c:if>
                </button>
            </div>
        </div>

    </form:form>
</div>


<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
