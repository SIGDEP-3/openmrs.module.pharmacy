<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fct" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="includeStyle.jsp"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="container-fluid mb-0 pb-0">
<%--	<div class="row mb-0 bg-green-sea align-items-center">--%>
<%--		<div class="col-12 h6 text-white pt-2">--%>
<%--			<spring:message code="pharmacy.title" /> :--%>
<%--			Dispensation & Gestion de Stock--%>
<%--		</div>--%>
<%--	</div>--%>

	<c:if test="${defaultLocation == null}">
		Bienvenue &agrave; votre pharmacie
		<div class="alert alert-warning h2 text-center">
			Veuillez demander &agrave; votre administrateur la configuration de votre site ou district d'activit&eacute;
		</div>
	</c:if>
	<c:if test="${defaultLocation != null}">
		<c:if test="${canOperate}">
			<c:if test="${defaultLocation.postalCode == null || fct:length(defaultLocation.postalCode) != 8}">
				<div class="alert alert-warning h5 text-center">
					Veuillez demander &agrave; votre administrateur la configuration du pr&eacute;fixe du site ou du code du district
				</div>
			</c:if>
		</c:if>
		<div class="row bg-info border-top border-bottom border-secondary align-items-center">
			<div class="col-11 pl-0">
				<div class="btn-toolbar m-1 pl-0">
					<div class="btn-group btn-group-sm">
						<div class="h5 mb-0 font-italic mr-3 text-white ml-1 mt-1">
							PHARMACIE
						</div>
					</div>
					<div class="btn-group btn-group-sm mr-3 ml-0">
						<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/manage.form'"
								<c:choose>
									<c:when test='<%= request.getRequestURI().contains("/manage") %>'>
										class=" btn btn-secondary btn-sm"
									</c:when>
									<c:otherwise>
										class="btn btn-outline-secondary text-white btn-sm"
									</c:otherwise>
								</c:choose>
						>
							<i class="fa fa-home"></i> Accueil
						</button>
					</div>

					<div class="btn-group btn-group-sm mr-3">
						<openmrs:hasPrivilege privilege="View Dispensation">
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/dispensation/list.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/dispensation") %>'>
											class=" btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary text-white btn-sm"
										</c:otherwise>
									</c:choose>>
								Dispensation
							</button>
						</openmrs:hasPrivilege>
					</div>

					<div class="btn-group btn-group-sm mr-3">
						<openmrs:hasPrivilege privilege="View Reception">
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/reception/list.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/reception") %>'>
											class=" btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary text-white btn-sm"
										</c:otherwise>
									</c:choose>>
								Reception de produits
							</button>
						</openmrs:hasPrivilege>
						<openmrs:hasPrivilege privilege="View Movement">
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/index.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/movement") %>'>
											class=" btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary text-white btn-sm"
										</c:otherwise>
									</c:choose>>
								Pertes & ajustements
							</button>
						</openmrs:hasPrivilege>
					</div>

					<div class="btn-group btn-group-sm">
						<openmrs:hasPrivilege privilege="View Inventory">
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/inventory/list.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/inventory") %>'>
											class=" btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary text-white btn-sm"
										</c:otherwise>
									</c:choose>>
								Inventaire
							</button>
						</openmrs:hasPrivilege>
						<c:if test="${canDistribute == true}">
							<openmrs:hasPrivilege privilege="View Distribution">
								<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/distribution/list.form'"
										<c:choose>
											<c:when test='<%= request.getRequestURI().contains("/distribution") %>'>
												class=" btn btn-secondary btn-sm"
											</c:when>
											<c:otherwise>
												class="btn btn-outline-secondary text-white btn-sm"
											</c:otherwise>
										</c:choose>>
									Distribution
								</button>
							</openmrs:hasPrivilege>
						</c:if>
						<openmrs:hasPrivilege privilege="View Report">
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/reports/list.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/report") %>'>
											class=" btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary text-white btn-sm"
										</c:otherwise>
									</c:choose>>
								Rapport d'activit&eacute;
							</button>
						</openmrs:hasPrivilege>
					</div>
					<div class="btn-group btn-group-sm ml-3">
						<openmrs:hasPrivilege privilege="View Stock">
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/list.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/stock") %>'>
											class="btn btn-secondary"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary text-white btn-sm"
										</c:otherwise>
									</c:choose>>
								Mes Etats
							</button>
						</openmrs:hasPrivilege>
					</div>
				</div>
			</div>
			<div class="col-1 text-right">
				<openmrs:hasPrivilege privilege="View Parameters">
					<div class="btn-group">
						<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/product/list.form'"
								class="btn btn-outline-secondary text-white btn-sm">
							<i class="fa fa-cog"></i>
						</button>
					</div>
				</openmrs:hasPrivilege>
			</div>
		</div>
		<div class="row mt-0 mb-1 pb-1 pt-1 pl-0 border-bottom border-secondary bg-light">
			<div class="col-12">
				<div class="d-flex justify-content-between">
					<div class="text-uppercase pt-1 pb-1 text-info">${title}</div>
					<c:if test='<%= request.getRequestURI().contains("/movement") %>'>
						<div class="btn-group btn-group-sm">
							<c:if test="${isDirectClient}">
								<openmrs:hasPrivilege privilege="View Transfer">
									<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/transfer/list.form'"
											<c:choose>
												<c:when test='<%= request.getRequestURI().contains("/transfer/") %>'>
													class=" btn btn-secondary btn-sm"
												</c:when>
												<c:otherwise>
													class="btn btn-outline-secondary btn-sm"
												</c:otherwise>
											</c:choose>>
										Transfert de produits
									</button>
								</openmrs:hasPrivilege>
							</c:if>
							<openmrs:hasPrivilege privilege="View Edit Product Back Supplier">
								<c:if test="${!isDirectClient}">
									<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/site-back/list.form'"
											<c:choose>
												<c:when test='<%= request.getRequestURI().contains("/site-back") %>'>
													class=" btn btn-secondary btn-sm"
												</c:when>
												<c:otherwise>
													class="btn btn-outline-secondary btn-sm"
												</c:otherwise>
											</c:choose>>
										Retour au fournisseur
									</button>
								</c:if>
								<c:if test="${canDistribute == true}">
									<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/supplier-back/list.form'"
											<c:choose>
												<c:when test='<%= request.getRequestURI().contains("/supplier-back") %>'>
													class=" btn btn-secondary btn-sm"
												</c:when>
												<c:otherwise>
													class="btn btn-outline-secondary btn-sm"
												</c:otherwise>
											</c:choose>>
										Retour des sites / PPS
									</button>
								</c:if>
							</openmrs:hasPrivilege>
							<openmrs:hasPrivilege privilege="View Movement">
								<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/other/list.form'"
										<c:choose>
											<c:when test='<%= request.getRequestURI().contains("/other") %>'>
												class=" btn btn-secondary btn-sm"
											</c:when>
											<c:otherwise>
												class="btn btn-outline-secondary btn-sm"
											</c:otherwise>
										</c:choose>>
									Autres Pertes & ajustements
								</button>
							</openmrs:hasPrivilege>
						</div>
					</c:if>
					<c:if test='<%= request.getRequestURI().contains("/stock") %>'>
						<div class="btn-group btn-group-sm">
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/list.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/stock/list") %>'>
											class="btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary btn-sm"
										</c:otherwise>
									</c:choose>>
								Stock de produits par lots
							</button>
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/productConsumption.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/productConsumption") %>'>
											class="btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary btn-sm"
										</c:otherwise>
									</c:choose>>
								Consommation de produits
							</button>
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/stockStatus.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/stockStatus") %>'>
											class="btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary btn-sm"
										</c:otherwise>
									</c:choose>>
								Etat de Stock
							</button>
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/operationStatus.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/operationStatus") %>'>
											class="btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary btn-sm"
										</c:otherwise>
									</c:choose>>
								Etat des Operations
							</button>
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/transferStatus.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/transferStatus") %>'>
											class="btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary btn-sm"
										</c:otherwise>
									</c:choose>>
								Etat de transfert
							</button>
						</div>
					</c:if>
					<c:if test='<%= request.getRequestURI().contains("/dispensation") %>'>
						<div class="btn-group btn-group-sm">
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/dispensation/list.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/dispensation/list") %>'>
											class="btn btn-secondary btn-sm"
										</c:when>
										<c:when test='<%= request.getRequestURI().contains("/dispensation/edit") %>'>
											class="btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary btn-sm"
										</c:otherwise>
									</c:choose>>
								Saisie dispensation
							</button>
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/dispensation/indicators/dispensationHistory.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/dispensationHistory") %>'>
											class="btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary btn-sm"
										</c:otherwise>
									</c:choose>>
								Historique des dispensations
							</button>
							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/dispensation/indicators/regimenIndicator.form'"
									<c:choose>
										<c:when test='<%= request.getRequestURI().contains("/regimenIndicator") %>'>
											class="btn btn-secondary btn-sm"
										</c:when>
										<c:otherwise>
											class="btn btn-outline-secondary btn-sm"
										</c:otherwise>
									</c:choose>>
								Regimes et indicateurs
							</button>
						</div>
					</c:if>
				</div>
			</div>

				<%--		<div class="col-12">--%>
				<%--			<div class="text-uppercase text-secondary font-weight-bold">--%>
				<%--				<div class="row align-items-center mt-0 mb-0">--%>
				<%--					<div class="col-5 pt-1">--%>
				<%--						<div class="h5 mb-0">${title}</div>--%>
				<%--					</div>--%>
				<%--					<c:if test='<%= request.getRequestURI().contains("/movement") %>'>--%>
				<%--						<div class="col-7 text-right">--%>
				<%--							<div class="btn-group btn-group-sm">--%>
				<%--                                <c:if test="${isDirectClient}">--%>
				<%--                                    <openmrs:hasPrivilege privilege="View Transfer">--%>
				<%--                                        <button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/transfer/list.form'"--%>
				<%--                                                <c:choose>--%>
				<%--                                                    <c:when test='<%= request.getRequestURI().contains("/transfer/") %>'>--%>
				<%--                                                        class=" btn btn-secondary btn-sm"--%>
				<%--                                                    </c:when>--%>
				<%--                                                    <c:otherwise>--%>
				<%--                                                        class="btn btn-outline-secondary btn-sm"--%>
				<%--                                                    </c:otherwise>--%>
				<%--                                                </c:choose>>--%>
				<%--                                            Transfert de produits--%>
				<%--                                        </button>--%>
				<%--                                    </openmrs:hasPrivilege>--%>
				<%--                                </c:if>--%>
				<%--								<openmrs:hasPrivilege privilege="View Edit Product Back Supplier">--%>
				<%--                                    <c:if test="${!isDirectClient}">--%>
				<%--                                        <button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/site-back/list.form'"--%>
				<%--                                                <c:choose>--%>
				<%--                                                    <c:when test='<%= request.getRequestURI().contains("/site-back") %>'>--%>
				<%--                                                        class=" btn btn-secondary btn-sm"--%>
				<%--                                                    </c:when>--%>
				<%--                                                    <c:otherwise>--%>
				<%--                                                        class="btn btn-outline-secondary btn-sm"--%>
				<%--                                                    </c:otherwise>--%>
				<%--                                                </c:choose>>--%>
				<%--                                            Retour au fournisseur--%>
				<%--                                        </button>--%>
				<%--                                    </c:if>--%>
				<%--                                    <c:if test="${canDistribute == true}">--%>
				<%--                                        <button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/supplier-back/list.form'"--%>
				<%--                                                <c:choose>--%>
				<%--                                                    <c:when test='<%= request.getRequestURI().contains("/supplier-back") %>'>--%>
				<%--                                                        class=" btn btn-secondary btn-sm"--%>
				<%--                                                    </c:when>--%>
				<%--                                                    <c:otherwise>--%>
				<%--                                                        class="btn btn-outline-secondary btn-sm"--%>
				<%--                                                    </c:otherwise>--%>
				<%--                                                </c:choose>>--%>
				<%--                                            Retour des sites / PPS--%>
				<%--                                        </button>--%>
				<%--                                    </c:if>--%>
				<%--								</openmrs:hasPrivilege>--%>
				<%--								<openmrs:hasPrivilege privilege="View Movement">--%>
				<%--									<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/movement/other/list.form'"--%>
				<%--											<c:choose>--%>
				<%--												<c:when test='<%= request.getRequestURI().contains("/other") %>'>--%>
				<%--													class=" btn btn-secondary btn-sm"--%>
				<%--												</c:when>--%>
				<%--												<c:otherwise>--%>
				<%--													class="btn btn-outline-secondary btn-sm"--%>
				<%--												</c:otherwise>--%>
				<%--											</c:choose>>--%>
				<%--										Autres Pertes & ajustements--%>
				<%--									</button>--%>
				<%--								</openmrs:hasPrivilege>--%>
				<%--							</div>--%>
				<%--						</div>--%>
				<%--					</c:if>--%>
				<%--					<c:if test='<%= request.getRequestURI().contains("/stock") %>'>--%>
				<%--					<div class="col-7 text-right">--%>
				<%--						<div class="btn-group btn-group-sm">--%>
				<%--							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/list.form'"--%>
				<%--									<c:choose>--%>
				<%--										<c:when test='<%= request.getRequestURI().contains("/stock/list") %>'>--%>
				<%--											class="btn btn-secondary btn-sm"--%>
				<%--										</c:when>--%>
				<%--										<c:otherwise>--%>
				<%--											class="btn btn-outline-secondary btn-sm"--%>
				<%--										</c:otherwise>--%>
				<%--									</c:choose>>--%>
				<%--								Stock de produits par lots--%>
				<%--							</button>--%>
				<%--							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/productConsumption.form'"--%>
				<%--									<c:choose>--%>
				<%--										<c:when test='<%= request.getRequestURI().contains("/productConsumption") %>'>--%>
				<%--											class="btn btn-secondary btn-sm"--%>
				<%--										</c:when>--%>
				<%--										<c:otherwise>--%>
				<%--											class="btn btn-outline-secondary btn-sm"--%>
				<%--										</c:otherwise>--%>
				<%--									</c:choose>>--%>
				<%--								Consommation de produits--%>
				<%--							</button>--%>
				<%--							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/stockStatus.form'"--%>
				<%--									<c:choose>--%>
				<%--										<c:when test='<%= request.getRequestURI().contains("/stockStatus") %>'>--%>
				<%--											class="btn btn-secondary btn-sm"--%>
				<%--										</c:when>--%>
				<%--										<c:otherwise>--%>
				<%--											class="btn btn-outline-secondary btn-sm"--%>
				<%--										</c:otherwise>--%>
				<%--									</c:choose>>--%>
				<%--								Etat de Stock--%>
				<%--							</button>--%>
				<%--							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/operationStatus.form'"--%>
				<%--									<c:choose>--%>
				<%--										<c:when test='<%= request.getRequestURI().contains("/operationStatus") %>'>--%>
				<%--											class="btn btn-secondary btn-sm"--%>
				<%--										</c:when>--%>
				<%--										<c:otherwise>--%>
				<%--											class="btn btn-outline-secondary btn-sm"--%>
				<%--										</c:otherwise>--%>
				<%--									</c:choose>>--%>
				<%--								Etat des Operations--%>
				<%--							</button>--%>
				<%--							<button onclick="window.location='${pageContext.request.contextPath}/module/pharmacy/operations/stock/transferStatus.form'"--%>
				<%--								<c:choose>--%>
				<%--									<c:when test='<%= request.getRequestURI().contains("/transferStatus") %>'>--%>
				<%--										class="btn btn-secondary btn-sm"--%>
				<%--									</c:when>--%>
				<%--									<c:otherwise>--%>
				<%--										class="btn btn-outline-secondary btn-sm"--%>
				<%--									</c:otherwise>--%>
				<%--								</c:choose>>--%>
				<%--								Etat de transfert--%>
				<%--							</button>--%>
				<%--						</div>--%>
				<%--					</div>--%>
				<%--					</c:if>--%>
				<%--				</div>--%>
				<%--				<h5 class="m-0"></h5>--%>
				<%--			</div>--%>
				<%--		</div>--%>
		</div>
	</c:if>


</div>
