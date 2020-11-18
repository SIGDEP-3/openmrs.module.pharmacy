<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').DataTable();
        });
        function addPrice(){
            // var productId = jQuery('#productId').val();
            const selector = document.querySelector('#productId')
            if (selector.value !== null && selector.value !== undefined) {
                window.location="${pageContext.request.contextPath}/module/pharmacy/product/prices/edit.form?productId="+selector.value;
            }
            else {
                selector.classList.add('is-invalid');
            }
            // if (productId !== null && productId !== undefined) {
            //     window.location="/module/pharmacy/product/prices/edit.form?productId="+productId;
            // }else {
            //     jQuery('#productId').addClass('is-invalid');
            // }
        }
    }
</script>

<hr>
<div>
    <div class="row">
        <div class="col-6">
            <h4>${title}</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-8">
            <select name="productId" id="productId" class="s2">
                <option value=""></option>
                <c:forEach var="product" items="${availableProduct}">
                    <option value="${product.productId}">${product.retailNameWithCode}</option>
                </c:forEach>

            </select>
        </div>
        <div class="col-4 text-right">
            <button class="btn btn-primary" onclick="addPrice()" title="Créer nouveau prix">
                <i class="fa fa-plus"></i>
            </button>
        </div>
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
    <c:forEach var="price" items="${prices}">
        <tr>
            <td>${price.productPriceId}</td>
            <td>${price.product.retailName}</td>
            <td>${price.productProgram.name}</td>
            <td>${price.purchasePrice}</td>
            <td>${price.salePrice}</td>
            <td>
                <c:url value="/module/pharmacy/product/prices/edit.form" var="editUrl">
                    <c:param name="id" value="${price.productPriceId}"/>
                </c:url>
                <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>
            </td>
        </tr>
    </c:forEach>
    </tbody>

</table>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
