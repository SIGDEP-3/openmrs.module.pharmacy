<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>

<hr>
<h4>${title}</h4>
<c:url value="/module/pharmacy/product/units/edit.form" var="createUrl"/>
<button onclick="window.location='${createUrl}'">Nouveau</button>
<hr>

<table class="table">
    <thead>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th style="width: 100px"></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="unit" items="${ units }">
        <tr>
            <td>${unit.productUnitId}</td>
            <td>${unit.name}</td>
            <td>
                <c:url value="/module/pharmacy/product/units/edit.form" var="editUrl">
                    <c:param name="id" value="${unit.productUnitId}"/>
                </c:url>
                <a href="${editUrl}">Edit</a>
                <c:url value="/module/pharmacy/product/units/delete.form" var="deleteUrl">
                    <c:param name="id" value="${unit.productUnitId}"/>
                </c:url>
                <a href="${deleteUrl}">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>

</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>
