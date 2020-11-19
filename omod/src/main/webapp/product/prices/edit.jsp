<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>

<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/product/prices/list.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
            <i class="fa fa-list"></i>
        </button>
    </div>
</div>
<hr>
<div class="container">
    <form:form modelAttribute="priceForm" method="post" action="" id="form">
        <form:hidden path="productPriceId"/>
        <form:hidden path="uuid"/>
        <form:hidden path="dateCreated"/>
<%--        <form:hidden path="isActive"/>--%>
        <form:hidden path="productId"/>
        <div class="row mb-2">
            <div class="col-6 mb-2">
                <labe>Produits<span class="required">*</span></labe>
                <div class="form-control">
                    ${product.retailName}
                </div>
<%--                <form:select path="productId" cssClass="form-control s2" >--%>
<%--                    <form:option value="" label=""/>--%>
<%--                    <form:options items="${availableProduct}" itemValue="productId" itemLabel="product" />--%>
<%--                </form:select>--%>
<%--                <form:errors path="productId" cssClass="error"/>--%>
            </div>
        </div>
        <div class="row mb-2">
            <div class="col-4">
                <label>Programmes <span class="required">*</span></label>
                <form:select path="productProgramId" cssClass="form-control s2">
                    <form:option value="" label=""/>
                    <form:options items="${availablePrograms}" itemValue="productProgramId" itemLabel="name" />
                </form:select>
                <form:errors path="productProgramId" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-3 mb-2">
                <labe>Purchase price<span class="required">*</span></labe>
                <form:input path="purchasePrice" cssClass="form-control" />
                <form:errors path="purchasePrice" cssClass="error"/>
            </div>
        </div>
        <div class="row">
            <div class="col-3 mb-2">
                <labe>sale price<span class="required">*</span></labe>
                <form:input path="salePrice" cssClass="form-control" />
                <form:errors path="salePrice" cssClass="error"/>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <button class="btn btn-success">
                    <c:if test="${not empty priceForm.productPriceId}">
                        <i class="fa fa-edit"></i>
                        <spring:message code="pharmacy.edit" />
                    </c:if>
                    <c:if test="${empty priceForm.productPriceId}">
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
