<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/operationHeader.jsp"%>
<c:if test="${defaultLocation != null}">
    Bienvenue &agrave; la pharmacie : <span class="badge badge-info">${defaultLocation}</span>
</c:if>

<%@ include file="template/localFooter.jsp"%>

<%@ include file="/WEB-INF/template/footer.jsp"%>
