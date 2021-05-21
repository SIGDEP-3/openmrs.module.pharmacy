<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../../template/operationHeader.jsp"%>

<openmrs:require privilege="Save Edit Product Back Supplier" otherwise="/login.htm" redirect="/module/pharmacy/operations/movement/site-back/edit.form" />
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
        <div class="col-6 text-uppercase font-italic text-secondary">
            <div class="h6 pt-2"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">
            <c:url value="/module/pharmacy/operations/movement/site-back/list.form" var="url"/>
            <button class="btn btn-primary btn-sm" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <form:form modelAttribute="productBackSupplierForm" method="post" action="" id="form">
                <form:hidden path="productOperationId"/>
                <form:hidden path="uuid"/>
                <form:hidden path="locationId"/>
                <form:hidden path="incidence"/>
                <form:hidden path="operationStatus"/>
                <form:hidden path="productProgramId"/>
                <form:hidden path="exchangeLocationId"/>
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
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row mb-2">
                            <div class="col-12">
                                <label class="mb-1">
                                    Fournisseur
                                </label>
                                <div class="form-control form-control-sm">
                                    ${supplier.name}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-6">
                        <div class="row mb-2">
                            <div class="col-4">
                                <label>Date de retour <span class="required">*</span></label>
                                <form:input path="operationDate" cssClass="form-control form-control-sm picker" />
                                <form:errors path="operationDate" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <div class="col-6 mb-2">
                                <label>Num&eacute;ro BL</label>
                                <form:input path="operationNumber" cssClass="form-control form-control-sm" readonly="true" />
                                <form:errors path="operationNumber" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-6">
                        <div class="row">
                            <div class="col-12 mb-2">
                                <label>Observation</label>
                                <form:textarea path="observation" cssClass="form-control form-control-sm" />
                                <form:errors path="observation" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-12">
                        <c:if test="${not empty productBackSupplierForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveOnly(event)">
                                <i class="fa fa-save"></i> Modifier
                            </button>
                        </c:if>
                        <c:if test="${ empty productBackSupplierForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveOnly(event)">
                                <i class="fa fa-edit"></i> Sauvegarder
                            </button>
                        </c:if>

                        <button class="btn btn-primary" onclick="saveAndAddProducts(event)">
                            <i class="fa fa-tablets"></i> Saisie des produits
                        </button>

                    </div>
                </div>

            </form:form>
        </div>
    </div>
</div>

<%@ include file="../../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
