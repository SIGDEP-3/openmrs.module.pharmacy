<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../template/operationHeader.jsp"%>

<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacy/operations/transfer/edit.form" />
<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('#form').on('submit', function (e){
                e.preventDefault();
            });

            jQuery(".periodSelector").monthpicker({
                //pattern: "mm/yyyy"
            });

            jQuery('.periodSelector').on('change',function (e) {
                console.log(e);
                console.log(jQuery(this).val());
                jQuery(this).val(getNumber(jQuery(this).val()));
            });
        });

        function saveOnly(e) {
            e.preventDefault();
            const forms = document.getElementsByTagName('form');
            let form = forms[0];
            // if (form.getAttribute('action').includes('action=save')) {
            //     form.submit();
            // } else
            if (form.getAttribute('action').includes('action=addLine')) {
                form.setAttribute('action', form.getAttribute('action').replace('addLine', 'save'));
            } else {
                if (form.getAttribute('action').includes('?id=')) {
                    form.setAttribute('action', form.getAttribute('action') + '&action=save');
                } else {
                    form.setAttribute('action', form.getAttribute('action') + '?action=save');
                }
            }

            form.submit();
        }

        function saveAndAddProducts(e) {
            e.preventDefault();
            const forms = document.getElementsByTagName('form');
            let form = forms[0];
            if (form.getAttribute('action').includes('action=save')) {
                form.setAttribute('action', form.getAttribute('action').replace('save', 'addLine'));
            } else {
                if (form.getAttribute('action').includes('?id=')) {
                    form.setAttribute('action', form.getAttribute('action') + '&action=addLine');
                } else {
                    form.setAttribute('action', form.getAttribute('action') + '?action=addLine');
                }
            }
            form.submit();
        }
    }
</script>

<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6">
            <div class="h5 pt-2"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">
            <c:url value="/module/pharmacy/operations/transfer/list.form" var="url"/>
            <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <form:form modelAttribute="productTransferForm" method="post" action="" id="form">
                <form:hidden path="productOperationId"/>
                <form:hidden path="uuid"/>
                <form:hidden path="locationId"/>
                <form:hidden path="incidence"/>
                <form:hidden path="operationStatus"/>
                <form:hidden path="productProgramId"/>
                <form:hidden path="transferType"/>
<%--                <c:if test="${fct:length(productTransfer.productAttributeFluxes) != 0}">--%>
<%--                    <form:hidden path="productProgramId"/>--%>
<%--                </c:if>--%>
                
                <div class="row">
                    <div class="col-6">
                        <div class="row">
                            <div class="col-6 mb-2">
                                <labe>Programme <span class="required">*</span></labe>
                                <div class="form-control form-control-sm">
                                        ${program.name}
                                </div>
<%--                                <c:if test="${fct:length(productTransfer.productAttributeFluxes) == 0}">--%>
<%--                                    <form:select path="productProgramId" cssClass="form-control s2" >--%>
<%--                                        <form:option value="" label=""/>--%>
<%--                                        <form:options items="${programs}" itemValue="productProgramId" itemLabel="name" />--%>
<%--                                    </form:select>--%>
<%--                                    <form:errors path="productProgramId" cssClass="error"/>--%>
<%--                                </c:if>--%>
<%--                                <c:if test="${fct:length(productTransfer.productAttributeFluxes) != 0}">--%>
<%--                                </c:if>--%>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row mb-2">
                            <div class="col-12">
                                <label class="mb-1">
                                    <c:if test="${productTransferForm.transferType == 'OUT'}">
                                        Destinataire
                                    </c:if>
                                    <c:if test="${productTransferForm.transferType == 'IN'}">
                                        Exp&eacute;diteur
                                    </c:if> <span class="required">*</span>
                                </label>
                                <form:select path="exchangeLocationId" cssClass="form-control s2" >
                                    <form:option value="" label=""/>
                                    <form:options items="${clientLocations}" itemValue="locationId" itemLabel="name" />
                                </form:select>
                                <form:errors path="exchangeLocationId" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-6">
                        <div class="row mb-2">
                            <div class="col-4">
                                <label>Date du transfert <span class="required">*</span></label>
                                <form:input path="operationDate" cssClass="form-control form-control-sm picker" />
                                <form:errors path="operationDate" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <div class="col-12 mb-2">
                                <label>Raison de transfert</label>
                                <form:select path="observation" cssClass="form-control s2">
                                    <form:option label="Appareil en panne"
                                                 value="Appareil en panne"/>
                                    <form:option label="Augmentation de la consommation"
                                                 value="Augmentation de la consommation"/>
                                    <form:option label="Baisse de la consommation"
                                                 value="Baisse de la consommation"/>
                                    <form:option label="Commande non satisfaite pour le produit durant la période"
                                                 value="Commande non satisfaite pour le produit durant la période"/>
                                    <form:option label="Consommation atypique"
                                                 value="Consommation atypique"/>
                                    <form:option label="Contraintes liées au conditionnement (1 diluant pour 1 kit)'"
                                                 value="Contraintes liées au conditionnement (1 diluant pour 1 kit)"/>
                                    <form:option label="Echange de produit à peremption proche pour consommation"
                                                 value="Echange de produit à peremption proche pour consommation"/>
                                    <form:option label="Erreur sur les unités de comptage"
                                                 value="Erreur sur les unités de comptage"/>
                                    <form:option label="Livraison Commande Normale + Commande urgente"
                                                 value="Livraison Commande Normale + Commande urgente"/>
                                    <form:option label="Manque de communication entre clients de la même aire sanitaire"
                                                 value="Manque de communication entre clients de la même aire sanitaire"/>
                                    <form:option label="Mauvaise estimation de la CMM"
                                                 value="Mauvaise estimation de la CMM"/>
                                    <form:option label="Mauvaise estimation de la quantité à commander"
                                                 value="Mauvaise estimation de la quantité à commander"/>
                                    <form:option label="Mauvaise tenue des fiches de stock (erreur dans les données sources)"
                                                 value="Mauvaise tenue des fiches de stock (erreur dans les données sources)"/>
                                    <form:option label="Non promptitude des RM des ESPC"
                                                 value="Non promptitude des RM des ESPC"/>
                                    <form:option label="Non suivi des dates de péremption"
                                                 value="Non suivi des dates de péremption"/>
                                    <form:option label="Peremption proche"
                                                 value="Peremption proche"/>
                                    <form:option label="Probleme lié au conditionnement"
                                                 value="Probleme lié au conditionnement"/>
                                    <form:option label="Produit à faible rotation"
                                                 value="Produit à faible rotation"/>
                                    <form:option label="Produit en vente ne reponds pas aux normes de gestion des stocks prescrites"
                                                 value="Produit en vente ne reponds pas aux normes de gestion des stocks prescrites"/>
                                    <form:option label="Rationnalisation des stock aux sites ou aux services"
                                                 value="Rationnalisation des stock aux sites ou aux services"/>
                                    <form:option label="Retard de livraison par la NPSP"
                                                 value="Retard de livraison par la NPSP"/>
                                    <form:option label="Rupture nationale"
                                                 value="Rupture nationale"/>
                                    <form:option label="Situation dû à un système d'allocation"
                                                 value="Situation dû à un système d'allocation"/>
                                    <form:option label="Situation dû à une livraision après un transfert in"
                                                 value="Situation dû à une livraision après un transfert in"/>
                                    <form:option label="Structure non habilité à commander ce produit"
                                                 value="Structure non habilité à commander ce produit"/>
                                    <form:option label="Transfert sans évaluation du MSD"
                                                 value="Transfert sans évaluation du MSD"/>
                                    <form:option label="Unité de comptage non adaptée"
                                                 value="Unité de comptage non adaptée"/>
                                    <form:option label="Utilisation des produits PNLS pour d'autres programmes"
                                                 value="Utilisation des produits PNLS pour d'autres programmes"/>
                                    <form:option label="Autres"
                                                 value="Autres"/>
                                </form:select>
<%--                                <form:textarea path="observation" cssClass="form-control form-control-sm" />--%>
                                <form:errors path="observation" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-12">
                        <c:if test="${not empty productTransferForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveOnly(event)">
                                <i class="fa fa-save"></i> Modifier
                            </button>
                        </c:if>
                        <c:if test="${ empty productTransferForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveOnly(event)">
                                <i class="fa fa-edit"></i> Sauvegarder
                            </button>
                        </c:if>

                        <button class="btn btn-primary" onclick="saveAndAddProducts(event)">
                            <i class="fa fa-tablets"></i> Saisie des produits
                        </button>

<%--                        <c:if test="${productTransferForm.operationStatus == 'AWAITING_VALIDATION'}">--%>
<%--                            <button class="btn btn-success" name="action" value="add">--%>
<%--                                <i class="fa fa-edit"></i>--%>
<%--                                <spring:message code="pharmacy.validate" />--%>
<%--                            </button>--%>
<%--                        </c:if>--%>
                    </div>
                </div>

            </form:form>
        </div>
    </div>
</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
