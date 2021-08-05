<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/operationHeader.jsp"%>
<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').dataTable({
                columnDefs: [
                    { type: 'date-uk', targets: [4, 8] }
                ],
                "order": [[ 1, "asc" ], [ 4, "asc" ], [ 8, "asc" ]],
            });
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
                            <option value="">S&eacute;lectioner un programme</option>
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
</div>
<c:if test="${fct:length(stocks) != 0}">
    <div class="card p-2">
        <div class="card-header mb-2">
            <div class="card-title mb-0">
                <div class="form-inline">
                    <label class="col-form-label">PROGRAMME : </label>
                    <div class="form-control mr-4 text-info">${selectedProgram.name}</div>
                    <label>Date de d&eacute;but de p&eacute;riode : </label>
                    <div class="form-control mr-4 text-info">
                        <fmt:formatDate value="${selectedStartDate}" pattern="dd/MM/yyyy" type="DATE"/>
                    </div>
                    <label>Date de fin de p&eacute;riode : </label>
                    <div class="form-control text-info" text-info>
                        <fmt:formatDate value="${selectedEndDate}" pattern="dd/MM/yyyy" type="DATE"/>
                    </div>
                </div>
            </div>
        </div>
        <table class="table table-sm table-striped">
            <thead>
            <tr>
                <th>Code</th>
                <th>D&eacute;signation</th>
                <th>Num&eacute;ro de lot</th>
                <th>Type d'op&eacute;ration</th>
                <th>Date de l'op&eacute;ration</th>
                <th>Quantity</th>
                <th>Quantity en stock</th>
                <th>Cr&eacute;&eacute;e par</th>
                <th>Date de Cr&eacute;ation</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="stock" items="${stocks}">
                <tr>
                    <td class="align-middle text-center">${stock.code}</td>
                    <td class="align-middle">${stock.productName}</td>
                    <td class="align-middle text-center">${stock.batchNumber}</td>
                    <td class="align-middle">${stock.operationType}</td>
                    <td class="align-middle text-center"><fmt:formatDate value="${stock.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                    <td class="align-middle text-center">${stock.quantity}</td>
                    <td class="align-middle">${stock.quantityInStock}</td>
                    <td class="align-middle">${stock.createdBy}</td>
                    <td class="align-middle text-center"><fmt:formatDate value="${stock.dateCreated}" pattern="dd/MM/yyyy HH:mm:ss" type="BOTH"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
