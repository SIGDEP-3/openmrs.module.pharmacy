<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../../template/operationHeader.jsp"%>

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('.table').dataTable({
                ordering: false,
                paging: false,
                info: false,
                dom: 'Bfrtip',
                buttons: [
                    'excel',
                    'print'
                ]
                // columnDefs: [
                //     { type: 'date-uk', targets: [4, 8] }
                // ],
                // "order": [[ 1, "asc" ], [ 4, "asc" ], [ 8, "asc" ]],
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
<c:if test="${fct:length(report.regimenReportIndicatorDTOList) != 0}">
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
                    <div class="form-control text-info">
                        <fmt:formatDate value="${selectedEndDate}" pattern="dd/MM/yyyy" type="DATE"/>
                    </div>
                </div>
            </div>
        </div>
        <table class="table table-sm table-condensed ">
            <thead>
            <tr>
                <th class="align-middle small">R&eacute;gime</th>
                <th style="width: 7%" class="align-middle small">Nombre de patients ADULTES re&ccedil;us</th>
                <th style="width: 7%" class="align-middle small">Nombre de patients ENFANTS re&ccedil;us</th>
                <th style="width: 7%" class="align-middle small">Nombre de patients ADULTES naifs (Inclusions)</th>
                <th style="width: 7%" class="align-middle small">Nombre de patients ENFANTS naifs (Inclusions)</th>
                <th style="width: 7%" class="align-middle small">Nombre de patients stables Adulte ( 15 ans) re&ccedil;us</th>
                <th style="width: 7%" class="align-middle small">Nombre de patients stables Enfant (< 15 ans) re&ccedil;us</th>
                <th style="width: 7%" class="align-middle small">Nombre de patients non stables Adulte ( 15 ans) re&ccedil;us</th>
                <th style="width: 7%" class="align-middle small">Nombre de patients non stables Enfant (< 15 ans) re&ccedil;us</th>
                <th style="width: 7%" class="align-middle small">R&eacute;f&eacute;r&eacute;s IN</th>
                <th style="width: 7%" class="align-middle small">Nombre de patients transfer&eacute;s</th>
                <th style="width: 7%" class="align-middle small">nombre de patient DCD</th>
            </tr>
            </thead>
            <tbody>
            <tr class="bg-light">
                <td colspan="1">1<sup>&egrave;re</sup> ligne</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <c:forEach var="line" items="${report.regimenReportIndicatorDTOList}">
                <c:if test="${line.regimenLine == 1}">
                    <tr>
                        <td class="align-middle">${line.regimen}</td>
                        <td class="align-middle text-center">${line.adult}</td>
                        <td class="align-middle text-center">${line.child}</td>
                        <td class="align-middle text-center">${line.adultInclusion}</td>
                        <td class="align-middle text-center">${line.childInclusion}</td>
                        <td class="align-middle text-center">${line.adultStable}</td>
                        <td class="align-middle text-center">${line.childStable}</td>
                        <td class="align-middle text-center">${line.adultNotStable}</td>
                        <td class="align-middle text-center">${line.childNotStable}</td>
                        <td class="align-middle text-center">${line.referred}</td>
                        <td class="align-middle text-center">${line.transferred}</td>
                        <td class="align-middle text-center">${line.dead}</td>
                    </tr>
                </c:if>
            </c:forEach>
            <tr class="bg-light">
                <td colspan="1">2<sup>&egrave;me</sup> ligne</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <c:forEach var="line" items="${report.regimenReportIndicatorDTOList}">
                <c:if test="${line.regimenLine == 2}">
                    <tr>
                        <td class="align-middle">${line.regimen}</td>
                        <td class="align-middle text-center">${line.adult}</td>
                        <td class="align-middle text-center">${line.child}</td>
                        <td class="align-middle text-center">${line.adultInclusion}</td>
                        <td class="align-middle text-center">${line.childInclusion}</td>
                        <td class="align-middle text-center">${line.adultStable}</td>
                        <td class="align-middle text-center">${line.childStable}</td>
                        <td class="align-middle text-center">${line.adultNotStable}</td>
                        <td class="align-middle text-center">${line.childNotStable}</td>
                        <td class="align-middle text-center">${line.referred}</td>
                        <td class="align-middle text-center">${line.transferred}</td>
                        <td class="align-middle text-center">${line.dead}</td>
                    </tr>
                </c:if>
            </c:forEach>
<%--            <tr class="bg-light">--%>
<%--                <td colspan="12">3eme ligne</td>--%>
<%--            </tr>--%>
<%--            <c:forEach var="line" items="${report.regimenReportIndicatorDTOList}">--%>
<%--                <c:if test="${line.regimenLine == 3}">--%>
<%--                    <tr>--%>
<%--                        <td class="align-middle">${line.regimen}</td>--%>
<%--                        <td class="align-middle text-center">${line.adult}</td>--%>
<%--                        <td class="align-middle text-center">${line.child}</td>--%>
<%--                        <td class="align-middle text-center">${line.adultInclusion}</td>--%>
<%--                        <td class="align-middle text-center">${line.childInclusion}</td>--%>
<%--                        <td class="align-middle text-center">${line.adultStable}</td>--%>
<%--                        <td class="align-middle text-center">${line.childStable}</td>--%>
<%--                        <td class="align-middle text-center">${line.adultNotStable}</td>--%>
<%--                        <td class="align-middle text-center">${line.childNotStable}</td>--%>
<%--                        <td class="align-middle text-center">${line.referred}</td>--%>
<%--                        <td class="align-middle text-center">${line.transferred}</td>--%>
<%--                        <td class="align-middle text-center">${line.dead}</td>--%>
<%--                    </tr>--%>
<%--                </c:if>--%>
<%--            </c:forEach>--%>
            <tr class="bg-light">
                <td colspan="1">Autres Informations du programme ARV</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td class="align-middle">Cotrimoxazole</td>
                <td class="align-middle text-center">${report.adultCtx}</td>
                <td class="align-middle text-center">${report.childCtx}</td>
                <td class="align-middle text-center">${report.adultInclusionCtx}</td>
                <td class="align-middle text-center">${report.childInclusionCtx}</td>
                <td class="align-middle text-center">${report.adultStableCtx}</td>
                <td class="align-middle text-center">${report.childStableCtx}</td>
                <td class="align-middle text-center">${report.adultNotStableCtx}</td>
                <td class="align-middle text-center">${report.childNotStableCtx}</td>
                <td class="align-middle text-center">${report.referredCtx}</td>
                <td class="align-middle text-center">${report.transferredCtx}</td>
                <td class="align-middle text-center">${report.deadCtx}</td>
            </tr>
            <tr>
                <td class="align-middle">ANEMIE</td>
                <td class="align-middle text-center">${report.adultAnemia}</td>
                <td class="align-middle text-center">${report.childAnemia}</td>
                <td class="align-middle text-center">${report.adultInclusionAnemia}</td>
                <td class="align-middle text-center">${report.childInclusionAnemia}</td>
                <td class="align-middle text-center">${report.adultStableAnemia}</td>
                <td class="align-middle text-center">${report.childStableAnemia}</td>
                <td class="align-middle text-center">${report.adultNotStableAnemia}</td>
                <td class="align-middle text-center">${report.childNotStableAnemia}</td>
                <td class="align-middle text-center">${report.referredAnemia}</td>
                <td class="align-middle text-center">${report.transferredAnemia}</td>
                <td class="align-middle text-center">${report.deadAnemia}</td>
            </tr>
            <tr>
                <td class="align-middle">TB/VIH</td>
                <td class="align-middle text-center">${report.adultTB}</td>
                <td class="align-middle text-center">${report.childTB}</td>
                <td class="align-middle text-center">${report.adultInclusionTB}</td>
                <td class="align-middle text-center">${report.childInclusionTB}</td>
                <td class="align-middle text-center">${report.adultStableTB}</td>
                <td class="align-middle text-center">${report.childStableTB}</td>
                <td class="align-middle text-center">${report.adultNotStableTB}</td>
                <td class="align-middle text-center">${report.childNotStableTB}</td>
                <td class="align-middle text-center">${report.referredTB}</td>
                <td class="align-middle text-center">${report.transferredTB}</td>
                <td class="align-middle text-center">${report.deadTB}</td>
            </tr>
            <tr>
                <td class="align-middle">VIH/HEPATITE B</td>
                <td class="align-middle text-center">${report.adultHepB}</td>
                <td class="align-middle text-center">${report.childHepB}</td>
                <td class="align-middle text-center">${report.adultInclusionHepB}</td>
                <td class="align-middle text-center">${report.childInclusionHepB}</td>
                <td class="align-middle text-center">${report.adultStableHepB}</td>
                <td class="align-middle text-center">${report.childStableHepB}</td>
                <td class="align-middle text-center">${report.adultNotStableHepB}</td>
                <td class="align-middle text-center">${report.childNotStableHepB}</td>
                <td class="align-middle text-center">${report.referredHepB}</td>
                <td class="align-middle text-center">${report.transferredHepB}</td>
                <td class="align-middle text-center">${report.deadHepB}</td>
            </tr>
            <tr>
                <td class="align-middle">PTME</td>
                <td class="align-middle text-center">${report.adultMPTC}</td>
                <td class="align-middle text-center">${report.childMPTC}</td>
                <td class="align-middle text-center">${report.adultInclusionMPTC}</td>
                <td class="align-middle text-center">${report.childInclusionMPTC}</td>
                <td class="align-middle text-center">${report.adultStableMPTC}</td>
                <td class="align-middle text-center">${report.childStableMPTC}</td>
                <td class="align-middle text-center">${report.adultNotStableMPTC}</td>
                <td class="align-middle text-center">${report.childNotStableMPTC}</td>
                <td class="align-middle text-center">${report.referredMPTC}</td>
                <td class="align-middle text-center">${report.transferredMPTC}</td>
                <td class="align-middle text-center">${report.deadMPTC}</td>
            </tr>
            <tr>
                <td class="align-middle">AES</td>
                <td class="align-middle text-center">${report.adultAES}</td>
                <td class="align-middle text-center">${report.childAES}</td>
                <td class="align-middle text-center">${report.adultInclusionAES}</td>
                <td class="align-middle text-center">${report.childInclusionAES}</td>
                <td class="align-middle text-center">${report.adultStableAES}</td>
                <td class="align-middle text-center">${report.childStableAES}</td>
                <td class="align-middle text-center">${report.adultNotStableAES}</td>
                <td class="align-middle text-center">${report.childNotStableAES}</td>
                <td class="align-middle text-center">${report.referredAES}</td>
                <td class="align-middle text-center">${report.transferredAES}</td>
                <td class="align-middle text-center">${report.deadAES}</td>
            </tr>
            </tbody>
        </table>
    </div>
</c:if>
<%@ include file="../../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>