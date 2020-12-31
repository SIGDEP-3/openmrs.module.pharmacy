<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>
<openmrs:require privilege="Save Supplier" otherwise="/login.htm" redirect="/module/pharmacy/product/suppliers/edit.form" />

<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/product/suppliers/list.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
            <i class="fa fa-list"></i>
        </button>
    </div>
</div>
<hr>
<div class="container">
    <form:form modelAttribute="supplierForm" method="post" action="" id="form">
        <form:hidden path="productSupplierId"/>
        <form:hidden path="locationId"/>
        <form:hidden path="uuid"/>

        <div class="row">
            <div class="col-8 mb-2">
                <labe><spring:message code="pharmacy.nameOrInLocation"/> <span class="required">*</span></labe>
                <form:select path="name" cssClass="form-control s2" >
                    <form:option value="" label=""/>
                    <form:options items="${locationList}" itemValue="name" itemLabel="name" />
                </form:select>
<%--                <form:errors path="locationName" cssClass="error"/>--%>
<%--                <form:input path="name" cssClass="form-control form-control-sm" />--%>
                <form:errors path="name" cssClass="error"/>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-2">
                <labe><spring:message code="pharmacy.shortName"/></labe>
                <form:input path="shortName" cssClass="form-control form-control-sm" />
                <form:errors path="shortName" cssClass="error"/>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-2">
                <labe><spring:message code="pharmacy.phoneNumber"/></labe>
                <form:input path="phoneNumber" cssClass="form-control form-control-sm" />
                <form:errors path="phoneNumber" cssClass="error"/>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-2">
                <labe><spring:message code="pharmacy.email"/></labe>
                <form:input path="email" cssClass="form-control form-control-sm" />
                <form:errors path="email" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-8 mb-2">
                <labe><spring:message code="pharmacy.description"/></labe>
                <form:textarea path="description" cssClass="form-control" />
                <form:errors path="description" cssClass="error"/>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-12">
                <button class="btn btn-success">
                    <c:if test="${not empty supplierForm.productSupplierId}">
                        <i class="fa fa-edit"></i>
                        <spring:message code="pharmacy.edit" />
                    </c:if>
                    <c:if test="${empty supplierForm.productSupplierId}">
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
