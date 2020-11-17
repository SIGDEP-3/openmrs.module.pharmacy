<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fct" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="includeStyle.jsp"%>

<script type="application/javascript" >
	if (jQuery) {
		jQuery(document).ready(function (){
			jQuery('.s2').select2();

			jQuery.datepicker.setDefaults({
				showOn: "both",
				buttonImageOnly: false,
				//buttonImage: "${pageContext.request.contextPath}/moduleResources/ptme/images/calendar.gif",
				//buttonText: "Calendar"
			});

			jQuery.datepicker.setDefaults( $.datepicker.regional[ "fr" ] );

			jQuery(".picker").datepicker({
				dateFormat: 'dd/mm/yy',
				dayNamesShort: [ "Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam" ],
				monthNamesShort: [ "Jan", "Fev", "Mar", "Avr", "Mai", "Jui", "Juil", "Aou", "Sep", "Oct", "Nov", "Dec" ],
				changeMonth: true,
				changeYear: true,
			});

			jQuery('.ui-datepicker-trigger').css("display","none");
		});
	}
</script>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="container-fluid">
	<h5>
		<spring:message code="pharmacy.title" /> :
		Dispensation & Gestion de Stock
	</h5>
	<div class="row bg-light border-top border-bottom border-info">
		<div class="col-12 pl-0">
			<div class="btn-toolbar m-1 pl-1">
				<div class="btn-group mr-3">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/manage.form'"
							class="btn btn-outline-info">
						Vue sur le stock
					</button>
				</div>

				<div class="btn-group mr-3">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/dispensing/list.form'"
					   class="btn btn-outline-info">
						Dispensation aux patients
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/distribution/list.form'"
					   class="btn btn-secondary">
						Distribution
					</button>
				</div>

				<div class="btn-group mr-3">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/reception/list.form'"
					   class="btn btn-secondary">
						Reception de produits
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/list.form'"
					   class="btn btn-secondary">
						Perte & ajustement
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/inventory/list.form'"
					   class="btn btn-secondary">
						Inventaire
					</button>
				</div>

				<div class="btn-group">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/list.form'"
					   class="btn btn-secondary">
						Etat du stock
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/report/list.form'"
					   class="btn btn-secondary">
						Rapport d'activite
					</button>
				</div>

			</div>
		</div>
	</div>
<%--	<div class="row mb-1 mt-1">--%>
<%--		<div class="col-2">--%>
<%--			<a href="${pageContext.request.contextPath}/module/pharmacy/operations/dispensing/list.form" class="text-decoration-none">--%>
<%--				<div class="card bg-joust-blue">--%>
<%--					<div class="card-body text-center text-white">Dispensation</div>--%>
<%--				</div>--%>
<%--			</a>--%>
<%--		</div>--%>
<%--		<div class="col-2">--%>
<%--			<a href="${pageContext.request.contextPath}/module/pharmacy/operations/reception/list.form" class="text-decoration-none">--%>
<%--				<div class="card bg-belize-hole">--%>
<%--					<div class="card-body text-center text-white">Reception</div>--%>
<%--				</div>--%>
<%--			</a>--%>
<%--		</div>--%>
<%--		<div class="col-2">--%>
<%--			<a href="${pageContext.request.contextPath}/module/pharmacy/operations/movement/list.form" class="text-decoration-none">--%>
<%--				<div class="card bg-summer-sky">--%>
<%--					<div class="card-body text-center text-white">Perte & ajustement</div>--%>
<%--				</div>--%>
<%--			</a>--%>
<%--		</div>--%>
<%--		<div class="col-2">--%>
<%--			<a href="${pageContext.request.contextPath}/module/pharmacy/operations/distribution/list.form" class="text-decoration-none">--%>
<%--				<div class="card bg-emerald">--%>
<%--					<div class="card-body text-center text-white">Distribution</div>--%>
<%--				</div>--%>
<%--			</a>--%>
<%--		</div>--%>
<%--		<div class="col-2">--%>
<%--			<a href="${pageContext.request.contextPath}/module/pharmacy/operations/inventory/list.form" class="text-decoration-none">--%>
<%--				<div class="card bg-blue-de-france">--%>
<%--					<div class="card-body text-center text-white">Inventaire</div>--%>
<%--				</div>--%>
<%--			</a>--%>
<%--		</div>--%>
<%--		<div class="col-2">--%>
<%--			<a href="${pageContext.request.contextPath}/module/pharmacy/operations/stock/list.form" class="text-decoration-none">--%>
<%--				<div class="card bg-cyanite">--%>
<%--					<div class="card-body text-center text-white">Etat du stock</div>--%>
<%--				</div>--%>
<%--			</a>--%>
<%--		</div>--%>
<%--	</div>--%>
	<div class="row mt-0 mb-1 border-bottom border-primary">
		<c:if test='<%= request.getRequestURI().contains("/dispensing") %>'>
			<div class="col-12 m-0 ">
				<div class="text-primary"><h6>Dispensation</h6></div>
			</div>
		</c:if>
		<c:if test='<%= request.getRequestURI().contains("/reception") %>'>
			<div class="col-12 m-0 p-0 pb-1">
				<div class="text-uppercase text-white"><h6 class="m-0">Reception de produits</h6></div>
			</div>
		</c:if>
		<c:if test='<%= request.getRequestURI().contains("/stock") %>'>
			<div class="col-12 m-0">
				<div class="alert bg-summer-sky text-uppercase text-white"><h5>Perte & Ajustement</h5></div>
			</div>
		</c:if>
		<c:if test='<%= request.getRequestURI().contains("/distribution") %>'>
			<div class="col-12 m-0">
				<div class="alert bg-emerald text-uppercase text-white"><h5>Distribution</h5></div>
			</div>
		</c:if>
		<c:if test='<%= request.getRequestURI().contains("/inventory") %>'>
			<div class="col-12 m-0">
				<div class="alert bg-blue-de-france text-uppercase text-white"><h5>Inventaire</h5></div>
			</div>
		</c:if>
		<c:if test='<%= request.getRequestURI().contains("/stock") %>'>
			<div class="col-12 m-0">
				<div class="alert bg-cyanite text-uppercase text-white"><h5>Etat des stock</h5></div>
			</div>
		</c:if>

	</div>
</div>
