<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../../template/operationHeader.jsp"%>

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('#list').dataTable({
                ordering: true,
                info: true,
                dom: 'Bfrtip',
                buttons: [
                    'excel'
                ],
                columnDefs: [
                    { type: 'date-uk', targets: [4] }
                ],
                "order": [[ 4, "asc" ]],
            });
            jQuery('.img-fluid').hide();
        });
        function cancelDispensation(dispensationId) {
            if (confirm('Voulez-vous vraiment annuler cette dispensation ?')){
                var img = jQuery('#loading-' + dispensationId);
                img.show();
                jQuery.ajax({
                    type: 'GET',
                    url: '${pageContext.request.contextPath}/cancel-dispensation.form?dispensationId=' + dispensationId,
                    dataType : "json",
                    crossDomain:true,
                    success : function(data) {
                        // console.log(data.responseText);
                        var tr = jQuery('tr#dispenation-' + dispensationId);
                        var table = jQuery('tr.tab-' + dispensationId);
                        var btn = jQuery('button#btn-dispensation-'+ dispensationId);
                        console.log(tr);
                        console.log(btn);
                        tr.addClass('bg-danger');
                        table.addClass('bg-danger');
                        btn.hide();
                        // if (!data.responseText === "Cancelled successfully"){
                        // }
                        //jQuery('.' + batchNumber).parent().html(data.quantity)
                        //alert(data.toString());
                        img.hide();
                    },
                    error : function(data) {
                        // console.log(data.responseText);
                        if (data.responseText === "Cancelled successfully"){
                            var tr = jQuery('tr#dispensation-' + dispensationId);
                            var btn = jQuery('button#btn-dispensation-'+ dispensationId);
                            // console.log(tr);
                            // console.log(btn);
                            tr.addClass('bg-warning');
                            btn.hide();
                            img.hide();
                        }
                    }
                });
            }
        }
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
<c:if test="${fct:length(dispensations) != 0}">
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
        <table class="table table-sm table-condensed " id="list">
            <thead>
            <tr>
                <th class="align-middle">Identifiant</th>
                <th style="" class="align-middle text-center">Sexe</th>
                <th style="" class="align-middle text-center">Age</th>
                <th style="" class="align-middle text-center">Cat&eacute;gorie</th>
                <th style="" class="align-middle text-center">Date de dispensation</th>
                <th style="" class="align-middle text-center">R&eacute;gime</th>
                <th style="" class="align-middle text-center">Date RDV</th>
                <th style="" class="align-middle text-center">Nombre de jours de TTT</th>
                <th style="" class="align-middle text-center">Produits</th>
                <th style="width: 5%" class="align-middle"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="line" items="${dispensations}">
                <tr id="dispensation-${line.operationId}">
                    <td class="align-middle">${line.patientIdentifier}</td>
                    <td class="align-middle text-center">${line.patientGender}</td>
                    <td class="align-middle text-center">${line.patientAge}</td>
                    <td class="align-middle text-center">${line.category}</td>
                    <td class="align-middle text-center"><fmt:formatDate value="${line.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                    <td class="align-middle text-center">${line.regimen}</td>
                    <td class="align-middle text-center"><fmt:formatDate value="${line.treatmentEndDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                    <td class="align-middle text-center">${line.treatmentDays}</td>

                    <td class="align-middle">
                        <table class="table table-sm table-condensed table-borderless table-striped">
                        <c:forEach var="flux" items="${fct:split(line.products, '|')}">
                            <tr class="tab-${line.operationId}">
                                <td>${fct:split(flux, ":")[0]}</td>
                                <td>${fct:split(flux, ":")[1]}</td>
                                <td class="text-right">${fct:split(flux, ":")[2]}</td>
                            </tr>
                        </c:forEach>
                        </table>
                    </td>
                    <td class="align-middle text-center">
<%--                        <c:url value="/module/pharmacy/operations/dispensation/cancel.form" var="cancelUrl">--%>
<%--                            <c:param name="caancelId" value="${line.operationId}"/>--%>
<%--                            <c:param name="programId" value="${selectedProgram.productProgramId}"/>--%>
<%--                            <c:param name="startDate" value="${selectedStartDate}"/>--%>
<%--                            <c:param name="endDate" value="${selectedEndDate}"/>--%>
<%--                        </c:url>--%>
<%--                        <a href="${cancelUrl}"--%>
<%--                           class="btn btn-warning btn-sm mr-2 text-decoration-none text-white"--%>
<%--                           onclick="confirm('Voulez-vous vraiment annuler la dispensation ?')"--%>
<%--                           tabindex="-1">--%>
<%--                            <i class="fa fa-eject"></i> Annuler--%>
<%--                        </a>--%>
                        <c:if test="${latestInventoryDate < line.operationDate}">
                            <button type="button" id="btn-dispensation-${line.operationId}"
                                    class="btn btn-warning btn-sm mr-2 text-decoration-none text-white"
                                    onclick="cancelDispensation(${line.operationId})"
                                    tabindex="-1">
                                <i class="fa fa-eject"></i> Annuler
                            </button>
                            <img src="${pageContext.request.contextPath}/moduleResources/pharmacy/loading.gif" class="img-fluid" id="loading-${line.operationId}" style="width: 40px" alt="loading">
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>
<%@ include file="../../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>