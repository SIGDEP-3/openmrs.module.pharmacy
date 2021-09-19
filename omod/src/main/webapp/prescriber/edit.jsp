<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../template/localHeader.jsp"%>
<openmrs:require privilege="Manage Providers"
                 otherwise="/login.htm" redirect="/module/pharmacy/prescriber/edit.form" />

<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/prescriber/list.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
            <i class="fa fa-list"></i>
        </button>
    </div>
</div>
<hr>
<div class="container">
    <form:form modelAttribute="prescriberForm" method="post" action="" id="form">
        <form:hidden path="providerId"/>

        <div class="row">
            <div class="col-4 mb-2">
                <labe>Nom<span class="required">*</span></labe>
                <form:input path="familyName" cssClass="form-control form-control-sm" />
                <form:errors path="familyName" cssClass="error"/>
            </div>
            <div class="col-6 mb-2">
                <labe>Pr&eacute;noms <span class="required">*</span></labe>
                <form:input path="givenName" cssClass="form-control form-control-sm" />
                <form:errors path="givenName" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-3 mb-2">
                <labe>Sexe <span class="required">*</span></labe>
                <form:radiobutton path="gender" tabindex="true" value="M" label="M" cssClass="mr-2"/>
                <form:radiobutton path="gender" tabindex="true" value="F" label="F" cssClass="mr-2"/>
                <form:errors path="gender" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-3 mb-2">
                <labe>Identiant <span class="required">*</span></labe>
                <form:input path="identifier" cssClass="form-control form-control-sm" />
                <form:errors path="identifier" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-8 mb-2">
                <labe>Site de prise en charge <span class="required">*</span></labe>
                <form:select path="location" cssClass="form-control s2" >
                    <form:option value="" label=""/>
                    <form:options items="${locations}" itemValue="name" itemLabel="name" />
                </form:select>
                <form:errors path="location" cssClass="error"/>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-12">
                <button class="btn btn-success">
                    <c:if test="${not empty prescriberForm.providerId}">
                        <i class="fa fa-edit"></i>
                        <spring:message code="pharmacy.edit" />
                    </c:if>
                    <c:if test="${empty prescriberForm.providerId}">
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
