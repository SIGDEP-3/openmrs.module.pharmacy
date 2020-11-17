<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').DataTable();
        });
    }
</script>

<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/product/prices/edit.form" var="url"/>
        <button class="btn btn-primary" onclick="window.location='${url}'" title="Créer nouveau prix">
            <i class="fa fa-plus"></i>
        </button>
    </div>
</div>
<hr>

<table class="table table-striped table-sm">
    <thead>
    <tr>
        <th>Id</th>
        <th>
            Product
        </th>
        <th>
            Program
        </th>
        <th>
            Purchase price
        </th>
        <th>
            sale price
        </th>
        <th style="width: 30px"></th>
    </tr>
    </thead>
    <tbody>
<%--    <c:forEach var="price" items="${ prices }">--%>
<%--        <tr>--%>
<%--            <td>${price.productPriceId}</td>--%>
<%--            <td>${price.product.name}</td>--%>
<%--            <td>${price.productProgram.name}</td>--%>
<%--            <td>${price.purchasePrice}</td>--%>
<%--            <td>${price.salePrice}</td>--%>
<%--            <td>--%>
<%--                <c:url value="/module/pharmacy/product/prices/edit.form" var="editUrl">--%>
<%--                    <c:param name="id" value="${price.productPriceId}"/>--%>
<%--                </c:url>--%>
<%--                <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>--%>
<%--            </td>--%>
<%--        </tr>--%>
<%--    </c:forEach>--%>
    </tbody>

</table>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
