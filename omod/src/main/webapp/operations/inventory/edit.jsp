<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../../template/operationHeader.jsp"%>

<openmrs:require privilege="Manage Pharmacy" otherwise="/login.htm" redirect="/module/pharmacy/operations/inventory/edit.form" />
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

        function getNumber(period) {
            const months = {
                '01': 'Janvier',
                '02': 'Fevrier',
                '03': 'Mars',
                '04': 'Avril',
                '05': 'Mai',
                '06': 'Juin',
                '07': 'Juillet',
                '08': 'Aout',
                '09': 'Septembre',
                '10': 'Octobre',
                '11': 'Novembre',
                '12': 'Decembre',
            };
            return period.replace(period.split('/')[0] + '/', 'INVC-' + months[period.split('/')[0]] + ' ');
        }

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
            <c:url value="/module/pharmacy/operations/inventory/list.form" var="url"/>
            <button class="btn btn-primary" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <form:form modelAttribute="productInventoryForm" method="post" action="" id="form">
                <form:hidden path="productOperationId"/>
                <form:hidden path="uuid"/>
                <form:hidden path="locationId"/>
                <form:hidden path="incidence"/>
                <form:hidden path="operationStatus"/>
                <form:hidden path="inventoryStartDate"/>
                <form:hidden path="productProgramId"/>
                <form:hidden path="inventoryType"/>
<%--                <c:if test="${fct:length(productInventory.productAttributeFluxes) != 0}">--%>
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
<%--                                <c:if test="${fct:length(productInventory.productAttributeFluxes) == 0}">--%>
<%--                                    <form:select path="productProgramId" cssClass="form-control s2" >--%>
<%--                                        <form:option value="" label=""/>--%>
<%--                                        <form:options items="${programs}" itemValue="productProgramId" itemLabel="name" />--%>
<%--                                    </form:select>--%>
<%--                                    <form:errors path="productProgramId" cssClass="error"/>--%>
<%--                                </c:if>--%>
<%--                                <c:if test="${fct:length(productInventory.productAttributeFluxes) != 0}">--%>
<%--                                </c:if>--%>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row mb-2">
                            <div class="col-5">
                                <label class="mb-1">Date du dernier inventaire</label>
                                <div class="form-control form-control-sm bg-info text-white">
                                    <c:if test="${latestInventory != null}">
                                        <fmt:formatDate value="${latestInventory.operationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                    </c:if>
                                    <c:if test="${latestInventory == null}">
                                        C'est votre premier inventaire
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-6">
                        <div class="row mb-2">
                            <div class="col-4">
                                <label>Date de l'inventaire <span class="required">*</span></label>
                                <form:input path="operationDate" cssClass="form-control form-control-sm picker" />
                                <form:errors path="operationDate" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row mb-2">
                            <div class="col-4">
                                <label>Num&eacute;ro d'inventaire <span class="required">*</span></label>
                                <form:input path="operationNumber" cssClass="form-control form-control-sm periodSelector" />
                                <form:errors path="operationNumber" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
<%--                    <div class="col-6">--%>
<%--                        <div class="row">--%>
<%--                            <div class="col-10">--%>
<%--                                <label class="mb-2">Unite de saisie</label> <br>--%>
<%--                                <form:radiobutton path="inventoryType" value="PARTIAL" label="Partiel" cssClass="mr-2"/>--%>
<%--                                <form:radiobutton path="inventoryType" value="FULL" label="Complet" cssClass="mr-2"/>--%>
<%--                                <form:errors path="inventoryType" cssClass="error"/>--%>
<%--                            </div>--%>

<%--                        </div>--%>
<%--                    </div>--%>
                    <div class="col-6">
                        <div class="row">
                            <div class="col-12 mb-2">
                                <label>Observations</label>
                                <form:textarea path="observation" cssClass="form-control form-control-sm" />
                                <form:errors path="observation" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                </div>
                <hr>

                <div class="row">
                    <div class="col-12">
                        <c:if test="${not empty productInventoryForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveOnly(event)">
                                <i class="fa fa-save"></i> Modifier
                            </button>
                        </c:if>
                        <c:if test="${ empty productInventoryForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveOnly(event)">
                                <i class="fa fa-edit"></i> Sauvegarder
                            </button>
                        </c:if>

                        <button class="btn btn-primary" onclick="saveAndAddProducts(event)">
                            <i class="fa fa-tablets"></i> Saisie des produits
                        </button>

                        <c:if test="${productInventoryForm.operationStatus == 'AWAITING_VALIDATION'}">
                            <button class="btn btn-success" name="action" value="add">
                                <i class="fa fa-edit"></i>
                                <spring:message code="pharmacy.validate" />
                            </button>
                        </c:if>
                    </div>
                </div>

            </form:form>
        </div>
    </div>
</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
