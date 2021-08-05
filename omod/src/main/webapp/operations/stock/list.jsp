<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').dataTable();
        });
    }
</script>
<div class="container-fluid mt-2">
    <div class="row border-bottom border-secondary mb-2 pb-2">
        <div class="col-12">
            <form action="" method="get">
                <div class="form-row">
                    <div class="col-5">
                        <label for="programId">Programme</label>
                        <select name="programId" id="programId" class="s2">
                            <c:forEach var="program" items="${programs}">
                                <option value="${program.productProgramId}" <c:if test='${program.productProgramId == programId}'>selected="selected"</c:if>
                                >${program.name}</option>
                            </c:forEach>
                        </select>
                        <button class="btn btn-primary btn-sm">Afficher</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <table class="table table-striped table-bordered table-sm">
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
                        <td>
                            <fmt:formatDate value="${stock.productAttribute.expiryDate}" pattern="dd/MM/yyyy" type="DATE"/>
                        </td>
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
