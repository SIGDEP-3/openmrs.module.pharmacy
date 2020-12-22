<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<div class="container-fluid mt-2">
    <div class="row">
        <div class="col-12">
            <table class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th>Code du produit</th>
                    <th>D&eacute;signation du produit</th>
                    <th>Unit&eacute;</th>
                    <th>Num&eacute;ro de lot</th>
                    <th>Date de p&eacute;remption</th>
                    <th>Quantit&eacute;en stock</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="stock" items="${stocks}">
                    <tr>
                        <td>${stock.productAttribute.product.code}</td>
                        <td>${stock.productAttribute.product.retailName}</td>
                        <td>${stock.productAttribute.product.productRetailUnit.name}</td>
                        <td>${stock.productAttribute.batchNumber}</td>
                        <td>${stock.productAttribute.expiryDate}</td>
                        <td>${stock.quantityInStock}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
