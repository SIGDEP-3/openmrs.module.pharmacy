<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').DataTable();
        });
    }
</script>

<div class="container-fluid mt-2">
    <div class="row border-bottom border-secondary mb-2 pb-2 align-items-center align-items-center">
        <div class="col-12">
            <form action="" method="get">
                <div class="form-row">
                    <div class="col-2">
                        <label for="programId" class="sr-only">Programme</label>
                        <select name="programId" id="programId" class="s2 form-control form-control-sm">
                            <option value=""></option>
                            <c:forEach var="program" items="${programs}">
                                <option value="${program.productProgramId}">${program.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-2">
                        <input type="text" name="startDate" class="form-control form-control-sm picker" placeholder="debut de periode">
                    </div>
                    <div class="col-2">
                        <input type="text" name="endDate" class="form-control form-control-sm picker" placeholder="fin de periode">
                    </div>
                    <button class="btn btn-primary btn-sm">Afficher</button>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <table class="table table-striped table-bordered table-sm">
                <thead>
                <tr>
                    <th>Programme</th>
                    <th>R&eacute;gion</th>
                    <th>District</th>
                    <th>Structure</th>
                    <th>Cat&eacute;gorie</th>
                    <th>Code produit</th>
                    <th>Produit</th>
                    <th>Unit&eacute; de rapportage</th>
                    <th>SDU</th>
                    <th>CMM</th>
                    <th>Etat du stock</th>
                    <th>Besoin Transfert IN</th>
                    <th>Besoin Transfert OUT</th>
                </tr>
                </thead>
                <tbody>
                <%--                    <c:forEach var="stock" items="${stockStatuses}">--%>
                <%--&lt;%&ndash;                        <tr>&ndash;%&gt;--%>
                <%--&lt;%&ndash;                            <td>${stock.productAttribute.product.code}</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;                            <td>${stock.productAttribute.product.retailName}</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;                            <td>${stock.productAttribute.product.productRetailUnit.name}</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;                            <td>${stock.productAttribute.batchNumber}</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;                            <td>${stock.productAttribute.expiryDate}</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;                            <td>${stock.quantityInStock}</td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;                        </tr>&ndash;%&gt;--%>
                <%--                    </c:forEach>--%>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
