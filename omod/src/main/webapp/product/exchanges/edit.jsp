<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>
<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacy/product/exchanges/edit.form" />
<script>
    if (jQuery) {
        jQuery(document).ready(function () {

        });
    }
</script>
<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/product/exchanges/list.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
            <i class="fa fa-list"></i>
        </button>
    </div>
</div>
<hr>
<div class="container">
    <form:form modelAttribute="exchangeEntityForm" method="post" action="" id="form">
        <form:hidden path="productExchangeEntityId"/>
        <form:hidden path="locationId"/>
        <form:hidden path="uuid"/>

        <div class="row">
            <div class="col-8 mb-2">
                <label><spring:message code="pharmacy.nameOrInLocation"/> <span class="required">*</span></label>
                <form:select path="locationInList" cssClass="form-control s2" >
                    <form:option value="" label=""/>
                    <form:options items="${locationList}" itemValue="locationId" itemLabel="name" />
                </form:select>
                <form:errors path="locationInList" cssClass="error"/>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-2">
                <label>Nom Partenaire <span class="required">*</span></label>
                <form:input path="name" cssClass="form-control-s2" />
                <form:errors path="name" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-8 mb-2">
                <label><spring:message code="pharmacy.description"/></label>
                <form:textarea path="description" cssClass="form-control" />
                <form:errors path="description" cssClass="error"/>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-12">
                <button class="btn btn-success">
                    <c:if test="${not empty exchangeEntityForm.productExchangeEntityId}">
                        <i class="fa fa-edit"></i>
                        <spring:message code="pharmacy.edit" />
                    </c:if>
                    <c:if test="${empty exchangeEntityForm.productExchangeEntityId}">
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
