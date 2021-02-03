<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>

<%@ include file="../template/operationHeader.jsp"%>

<openmrs:require privilege="Save Report" otherwise="/login.htm" redirect="/module/pharmacy/reports/edit.form" />
<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery('#form').on('submit', function (e){
                e.preventDefault();
            });

            jQuery(".periodSelector").monthpicker({
                //pattern: "mm/yyyy"
            });

            const urgent = jQuery('input[name=urgent]');
            const urgentProduct = jQuery('#urgentProduct');
            urgentProduct.hide();

            if (urgent.is(':checked')) {
                urgentProduct.show();
            }

            jQuery('.periodSelector').on('change',function (e) {
                let periodNumber = getNumber(jQuery(this).val());
                if (urgent.is(":checked"))
                    jQuery(this).val(periodNumber + ' U');
                else
                    jQuery(this).val(periodNumber);
                // jQuery(this).val(getNumber(jQuery(this).val()));
            });

            urgent.click(function (e) {
                // console.log('clicked')
                let periodSelector = jQuery('.periodSelector');
                let period = periodSelector.val();

                if (period) {
                    if (jQuery(this).is(':checked')) {
                        periodSelector.val(period + " U");
                        urgentProduct.show();
                    } else {
                        if (period.includes(" U")) {
                            periodSelector.val(period.replace(" U", ""))
                        }
                        urgentProduct.hide();
                    }
                } else {
                    if (jQuery(this).is(':checked')) {
                        urgentProduct.show();
                    } else {
                        urgentProduct.hide();
                    }
                }
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
            return period.replace(period.split('/')[0] + '/', months[period.split('/')[0]] + ' ');
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
            <c:if test="${isPlatformUser == false}">
                form.setAttribute('action', form.getAttribute('action').replace('editFlux', 'editFluxOther'));
            </c:if>
            form.submit();
        }
    }
</script>

<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6 text-uppercase font-italic text-secondary">
            <div class="h6"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <div class="col-6 text-right">
            <c:url value="/module/pharmacy/reports/list.form" var="url"/>
            <button class="btn btn-primary btn-sm" onclick="window.location='${url}'" title="Voir la liste">
                <i class="fa fa-list"></i> Voir la liste
            </button>
        </div>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <form:form modelAttribute="productReportForm" method="post"
                       action="${isPlatformUser == false ? '' : ''}" id="form">
                <form:hidden path="productOperationId"/>
                <form:hidden path="uuid"/>
                <form:hidden path="locationId"/>
                <form:hidden path="incidence"/>
                <form:hidden path="operationStatus"/>
                <form:hidden path="reportType"/>
                <form:hidden path="productProgramId"/>
<%--                <c:if test="${fct:length(productReport.productAttributeFluxes) != 0}">--%>
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
                            <div class="col-4">
                                <label>P&eacute;riode <span class="required">*</span></label>
                                <form:input path="reportPeriod" cssClass="form-control form-control-sm periodSelector" />
                                <form:errors path="reportPeriod" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-6">
                        <div class="row mb-2">
                            <div class="col-4">
                                <label>Date de soumission <span class="required">*</span></label>
                                <form:input path="operationDate" cssClass="form-control form-control-sm picker" readonly="true" />
                                <form:errors path="operationDate" cssClass="error"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <div class="col-3">
                                <label class="mb-2">Rapport urgent </label> <br>
                                <form:checkbox path="urgent" cssClass="mr-2"/>
                                <form:errors path="urgent" cssClass="error"/>
                            </div>
                            <div class="col-9" id="urgentProduct">
                                <label>Produits <span class="required">*</span></label>
                                <form:select path="productIds" cssClass="form-control s2" multiple="true">
                                    <form:option value="" label=""/>
                                    <form:options items="${products}" itemValue="productId" itemLabel="retailNameWithCode" />
                                </form:select>
                                <form:errors path="productIds" cssClass="error"/>
                                <c:if test="${fct:length(products) == 0}">
                                    <span class="text-danger">Vous devez faire un inventaire partiel avant SVP</span>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-12">
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
                        <c:if test="${not empty productReportForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveAndAddProducts(event)">
                                <i class="fa fa-save"></i> Modifier
                            </button>
                        </c:if>
                        <c:if test="${ empty productReportForm.productOperationId}">
                            <button class="btn btn-success" onclick="saveAndAddProducts(event)">
                                <i class="fa fa-edit"></i> Enregistrer
                            </button>
                        </c:if>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>

<%@ include file="../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
