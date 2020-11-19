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

<div class="container-fluid mb-0 pb-0">
	<h5>
		<spring:message code="pharmacy.title" /> :
		Dispensation & Gestion de Stock
	</h5>
	<div class="row bg-info border-top border-bottom border-secondary">
		<div class="col-12 pl-0">
			<div class="btn-toolbar m-1 pl-0">
				<div class="btn-group mr-3 ml-0">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/manage.form'"
						<c:choose>
							<c:when test='<%= request.getRequestURI().contains("/manage") %>'>
								class=" btn btn-secondary"
							</c:when>
							<c:otherwise>
								class="btn btn-outline-secondary text-white"
							</c:otherwise>
						</c:choose>
					>
						Vue sur le stock
					</button>
				</div>

				<div class="btn-group mr-3">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/dispensing/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/dispensing") %>'>
									class=" btn btn-secondary"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white"
								</c:otherwise>
							</c:choose>>
						Dispensation aux patients
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/distribution/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/distribution") %>'>
									class=" btn btn-secondary"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white"
								</c:otherwise>
							</c:choose>>
						Distribution
					</button>
				</div>

				<div class="btn-group mr-3">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/reception/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/reception") %>'>
									class=" btn btn-secondary"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white"
								</c:otherwise>
							</c:choose>>
						Reception de produits
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/movement") %>'>
									class=" btn btn-secondary"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white"
								</c:otherwise>
							</c:choose>>
						Perte & ajustement
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/inventory/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/inventory") %>'>
									class=" btn btn-secondary"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white"
								</c:otherwise>
							</c:choose>>
						Inventaire
					</button>
				</div>

				<div class="btn-group">
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/stock") %>'>
									class="btn btn-secondary"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white"
								</c:otherwise>
							</c:choose>>
						Etat du stock
					</button>
					<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/report/list.form'"
							<c:choose>
								<c:when test='<%= request.getRequestURI().contains("/report") %>'>
									class=" btn btn-secondary"
								</c:when>
								<c:otherwise>
									class="btn btn-outline-secondary text-white"
								</c:otherwise>
							</c:choose>>
						Rapport d'activite
					</button>
				</div>

			</div>
		</div>
	</div>
	<div class="row mt-0 mb-1 pb-3 pt-3 pl-0 border-bottom border-secondary bg-light">
		<div class="col-12">
			<div class="text-uppercase text-secondary font-weight-bold">
				<h5 class="m-0">${title}</h5>
			</div>
		</div>
	</div>
</div>
