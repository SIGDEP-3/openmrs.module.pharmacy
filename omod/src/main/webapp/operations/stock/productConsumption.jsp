<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.report').DataTable();
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
                            <c:forEach var="program" items="${programs}">
                                <option value="${program.productProgramId}">${program.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-2">
                        <label for="startDate" class="sr-only"></label>
                        <input type="text" id="startDate" name="startDate" class="form-control form-control-sm picker" placeholder="debut de periode">
                    </div>
                    <div class="col-2">
                        <label for="endDate" class="sr-only"></label>
                        <input type="text" id="endDate" name="endDate" class="form-control form-control-sm picker" placeholder="fin de periode">
                    </div>
                    <div class="col-2 pt-1">
                        <div class="form-check">
                            <input class="form-check-input custom-checkbox" type="checkbox" name="byWholesaleUnit" id="byWholesale">
                            <label class="form-check-label" for="byWholesale">
                                Unit&eacute; de conditionnement
                            </label>
                        </div>
                    </div>
                    <div class="col-2 pt-1">
                        <div class="form-check">
                            <input class="form-check-input custom-checkbox" type="checkbox" name="bySite" id="bySitePPS">
                            <label class="form-check-label" for="bySitePPS">
                                Par site / PPS
                            </label>
                        </div>
                    </div>
                    <div class="col-2">
                        <button class="btn btn-primary btn-sm"><i class="fa fa-search"></i></button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <c:if test="${dto != null}">
                <table class="table table-borderless border bg-light">
                    <tr>
                        <td>Programme</td>
                        <td class="font-weight-bold text-info align-middle">${dto.productProgramName}</td>
                        <td colspan="1">Unit&eacute;</td>
                        <td class="font-weight-bold text-info align-middle">
                                ${dto.byWholesaleUnit == true ? 'Conditionnement' : 'Dispensation'}
                        </td>
                    </tr>
                    <tr>
                        <td>P&eacute;riode</td>
                        <td class="font-weight-bold text-info align-middle">
                            Du <fmt:formatDate value="${dto.startDate}" pattern="dd/MM/yyyy" type="DATE"/>
                            au <fmt:formatDate value="${dto.endDate}" pattern="dd/MM/yyyy" type="DATE"/>
                        </td>
                        <td colspan="2" class="font-weight-bold text-info align-middle">
                            <c:if test="${dto.byLocation == true}">
                                Consommation par Site/PPS
                            </c:if>
                            <%--<c:if test="${dto.byLocation == false}">
                                Consommation du site
                            </c:if>--%>
                        </td>
                    </tr>
                </table>
                <c:if test="${dto.byLocation == false}">
                    <div class="border p-2">
                        <table class="table report table-striped table-bordered table-sm border">
                            <thead>
                            <tr>
                                <th>Code produit</th>
                                <th>Produit</th>
                                    <%--                            <th>Unit&eacute;</th>--%>
                                <th>Quantit&eacute; consomm&eacute;e</th>
                                <th>Quantit&eacute; consomm&eacute;e <br>(conditionnement)</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="line" items="${dto.locationProductQuantities[0].productQuantities}">
                                <tr>
                                    <td>${line.code}</td>
                                    <td>${line.retailName}</td>
                                    <td>${line.retailQuantity}</td>
                                    <td>${line.wholesaleQuantity}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                </c:if>

                <c:if test="${dto.byLocation == true}">
                    <div class="border p-2 table-responsive">
                        <table class="table report table-striped table-bordered table-sm border small">
                            <thead>
                            <tr>
                                <th rowspan="2" class="text-center align-middle">Site/PPS</th>
                                <c:forEach var="col11" items="${dto.locationProductQuantities[0].productQuantities}">
                                    <th  class="text-center align-middle">${col11.code}</th>
                                </c:forEach>
                            </tr>
                            <tr>
                                <c:forEach var="col12" items="${dto.locationProductQuantities[0].productQuantities}">
                                    <th class="align-middle">
                                        <c:if test="${dto.byWholesaleUnit == false}">${col12.retailName}</c:if>
                                        <c:if test="${dto.byWholesaleUnit == true}">${col12.wholesaleName}</c:if>
                                    </th>
                                </c:forEach>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="line" items="${dto.locationProductQuantities}">
                                <tr>
                                    <td>${line.locationName}</td>
                                    <c:forEach var="col" items="${line.productQuantities}">
                                        <td class="text-center align-middle font-weight-bold text-info">
                                            <c:if test="${dto.byWholesaleUnit == false}">${col.retailQuantity}</c:if>
                                            <c:if test="${dto.byWholesaleUnit == true}">${col.wholesaleQuantity}</c:if>
                                        </td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>

            </c:if>

        </div>
    </div>
</div>
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
