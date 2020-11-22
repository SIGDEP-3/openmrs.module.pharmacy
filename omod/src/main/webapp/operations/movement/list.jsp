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
    <div class="row">
        <div class="col-6">
            <div class="row mb-2 ml-1 mr-1">
                <div class="col-6">
                    <div class="h5 pt-2"><i class="fa fa-list"></i> Movement d'entre</div>
                </div>
                <div class="col-6 text-right">
                    <select name="stockEntryType" id="stockEntryType" class="s2">
<%--                        <option value=""></option>--%>
<%--                        <c:forEach var="product" items="${availableProduct}">--%>
<%--                            <option value="${product.productId}">${product.retailNameWithCode}</option>--%>
<%--                        </c:forEach>--%>

                    </select>
                    <c:url value="/module/pharmacy/operations/movement/edit.form" var="url"/>
                    <button class="btn btn-primary" onclick="window.location='${url}'" title="Créer nouveau">
                        <i class="fa fa-plus"></i> Nouvelle entré
                    </button>
                </div>
            </div>
            <div class="row bg-light pt-2 pb-2 border border-secondary">
                <div class="col-12">
                    <table class="table table-striped table-sm">
                        <thead>
                        <tr>
                            <th>
                                Type de mouvement
                            </th>
                            <th>
                                Date de mouvement
                            </th>
                            <th>
                                Origine
                            </th>
                            <th>
                                Programme
                            </th>
                            <th>
                                Motifs
                            </th>
<%--                            <th>Nombre de produits</th>--%>
<%--                            <th>--%>
<%--                                Etat--%>
<%--                            </th>--%>
                            <th style="width: 30px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="entry" items="${ entries }">
                            <tr>
                                <td>${ entry.productMovementEntry.stockEntryType}</td>
                                <td><fmt:formatDate value="${entry.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                <td>${entry.operationNumber}</td>
                                <td>${entry.productProgram.name}</td>
                                <td>${entry.receptionQuantityMode}</td>
<%--                                <c:choose>--%>
<%--                                    <c:when test="${fct:length(entry.productAttributeFluxes) == 0}">--%>
<%--                                        <c:url value="/module/pharmacy/operations/movement/editFlux.form" var="addLineUrl">--%>
<%--                                            <c:param name="receptionId" value="${entry.productOperationId}"/>--%>
<%--                                        </c:url>--%>
<%--                                        <td class="text-danger">--%>
<%--                                            <a href="${addLineUrl}">Ajouter des produits</a>--%>
<%--                                        </td>--%>
<%--                                    </c:when>--%>
<%--                                    <c:otherwise>--%>
<%--                                        <td class="text-center">--%>
<%--                                                ${fct:length(reception.productAttributeFluxes)}--%>
<%--                                        </td>--%>
<%--                                    </c:otherwise>--%>
<%--                                </c:choose>--%>
<%--                                <td>${reception.operationStatus}</td>--%>
                                <td>
                                    <c:url value="/module/pharmacy/operations/movement/edit.form" var="editUrl">
                                        <c:param name="id" value="${entry.productOperationId}"/>
                                    </c:url>
                                    <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>

                    </table>
                </div>
            </div>
        </div>
        <div class="col-6">
            <div class="row mb-2 ml-1 mr-1">
                <div class="col-6">
                    <div class="h5 pt-2"><i class="fa fa-list"></i> Movement de sortir</div>
                </div>
                <div class="col-6 text-right">
                    <c:url value="/module/pharmacy/operations/movement/edit.form" var="url"/>
                    <button class="btn btn-primary" onclick="window.location='${url}'" title="Créer nouveau">
                        <i class="fa fa-plus"></i> Nouvelle Sortie
                    </button>
                </div>
            </div>
            <div class="row bg-light pt-2 pb-2 border border-secondary">
                <div class="col-12">
                    <table class="table table-striped table-sm">
                        <thead>
                        <tr>
                            <th>
                                Type de mouvement
                            </th>
                            <th>
                                Date de mouvement
                            </th>
                            <th>
                                Destinataire
                            </th>
                            <th>
                                Programme
                            </th>
<%--                            <th>Nombre de produits</th>--%>
                            <th>
                                Motifs
                            </th>
                            <th style="width: 30px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="out" items="${ outs }">
                            <tr>
                                <td>${out.productSupplier.name}</td>
                                <td><fmt:formatDate value="${out.operationDate}" pattern="dd/MM/yyyy" type="DATE"/></td>
                                <td>${out.operationNumber}</td>
                                <td>${out.productProgram.name}</td>
                                <td>${out.receptionQuantityMode}</td>
<%--                                <c:choose>--%>
<%--                                    <c:when test="${fct:length(out.productAttributeFluxes) == 0}">--%>
<%--                                        <c:url value="/module/pharmacy/operations/movement/editFlux.form" var="addLineUrl">--%>
<%--                                            <c:param name="receptionId" value="${out.productOperationId}"/>--%>
<%--                                        </c:url>--%>
<%--                                        <td class="text-danger">--%>
<%--                                            <a href="${addLineUrl}">Ajouter des produits</a>--%>
<%--                                        </td>--%>
<%--                                    </c:when>--%>
<%--                                    <c:otherwise>--%>
<%--                                        <td class="text-center">--%>
<%--                                                ${fct:length(reception.productAttributeFluxes)}--%>
<%--                                        </td>--%>
<%--                                    </c:otherwise>--%>
<%--                                </c:choose>--%>
<%--                                <td>${out.operationStatus}</td>--%>
                                <td>
                                    <c:url value="/module/pharmacy/operations/movement/edit.form" var="editUrl">
                                        <c:param name="id" value="${out.productOperationId}"/>
                                    </c:url>
                                    <a href="${editUrl}" class="text-info mr-2"><i class="fa fa-edit"></i></a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>

                    </table>
                </div>
            </div>
        </div>

    </div>
</div>
<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
