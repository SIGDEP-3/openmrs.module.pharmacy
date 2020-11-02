<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>

<hr>
<h4>${title}</h4>
<hr>

<form:form modelAttribute="unitForm" method="post" action="" id="form">
    <form:hidden path="productUnitId"/>
    <form:hidden path="uuid"/>

    <table>
        <tr>
            <td><spring:message code="pharmacy.name"/> <span class="required">*</span> : </td>
            <td><form:input path="name" cssClass="form-control" /></td>
            <td><form:errors path="name" cssClass="error"/></td>
        </tr>
        <tr>
            <td><spring:message code="pharmacy.description"/> : </td>
            <td><form:textarea path="description" cssClass="form-control"/></td>
            <td><form:errors path="description" cssClass="error"/></td>
        </tr>
        <tr>
            <td></td>
            <td>
                <button>
                    <c:if test="${not empty unitForm.productUnitId}">
                        <spring:message code="pharmacy.edit" />
                    </c:if>
                    <c:if test="${empty unitForm.productUnitId}">
                        <spring:message code="pharmacy.save" />
                    </c:if>
                </button>
            </td>
        </tr>
    </table>

</form:form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
