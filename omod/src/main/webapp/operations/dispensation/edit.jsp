<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ page import="org.openmrs.module.pharmacy.enumerations.OperationStatus" %>
<openmrs:require privilege="Save Dispensation" otherwise="/login.htm" redirect="/module/pharmacy/operations/dispensation/edit.form" />

<%@ include file="../../template/operationHeader.jsp"%>

<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            // jQuery('#form').on('submit', function (e){
            //     e.preventDefault();
            // });

            // jQuery(".periodSelector").monthpicker({
            //     //pattern: "mm/yyyy"
            // });

            //
            // jQuery("#operationDate").on('change', function (e) {
            //     alert(jQuery(this).val());
            // });

            // jQuery('.periodSelector').on('change',function (e) {
            //     // console.log(e);
            //     console.log(jQuery(this).val());
            //     jQuery(this).val(getNumber(jQuery(this).val()));
            // });

            dateDiff(new Date());

            getTreatmentDaysLost();
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

        function checkPatientInfo() {
            jQuery.ajax({
                type: 'GET',
                url: '${pageContext.request.contextPath}/transform-dispensation.form',
                dataType : "text",
                crossDomain:true,
                success : function(data) {
                    console.log(data);
                    //jQuery('.' + batchNumber).parent().html(data.quantity)
                    //alert(data.toString());
                },
                error : function(data) {
                    console.log(data)
                }
            })
        }

        function getDate(stringDate) {
            console.log(stringDate);
            if (stringDate) {
                const parts = stringDate.split('/');
                return new Date(parts[2], parts[1] - 1, parts[0]);
            }
            return null;
        }

        function getTreatmentEndDate() {
            const operationDate = getDate(jQuery("#operationDate").val());
            const treatmentDays = jQuery("#treatmentDays").val();

            if (operationDate && treatmentDays) {
                const treatmentEndDateInput = jQuery("#treatmentEndDate");
                const endDateByOperationDate = addDays(operationDate, treatmentDays);

                let lastTreatmentEndDate = jQuery("#lastTreatmentEndDate");
                console.log('------ lastTreatmentEndDate', lastTreatmentEndDate)
                if (lastTreatmentEndDate.text()) {
                    let endDateByLastTreatmentEndDate = addDays(getDate(lastTreatmentEndDate.text()), treatmentDays);
                    let treatmentDaysLost = jQuery("#treatmentDaysLost").val();
                    if (treatmentDaysLost) {
                        endDateByLastTreatmentEndDate = removeDays(new Date(endDateByLastTreatmentEndDate), treatmentDaysLost);
                    }
                    if (endDateByLastTreatmentEndDate > endDateByOperationDate) {
                        treatmentEndDateInput.val(formatDate(endDateByLastTreatmentEndDate));
                    } else {
                        treatmentEndDateInput.val(formatDate(endDateByOperationDate));
                    }
                } else {
                    console.log('Operation date before adding ', operationDate);
                    treatmentEndDateInput.val(formatDate(addDays(operationDate, treatmentDays)));
                }
            }
        }

        function getTreatmentDaysLost() {
            const operationDate = getDate(jQuery("#operationDate").val());
            const treatmentDays = jQuery("#treatmentDays").val();
            const treatmentEndDateInput = jQuery("#treatmentEndDate");
            if (treatmentEndDateInput.val() && treatmentDays && operationDate) {
                let treatmentEndDate = getDate(treatmentEndDateInput.val());
                let startDate = removeDays(treatmentEndDate, treatmentDays);
                if (startDate !== operationDate.getTime()) {
                    let lastTreatmentEndDate = jQuery("#lastTreatmentEndDate");
                    if (lastTreatmentEndDate) {
                        let lastTreatmentEndDateDate = getDate(lastTreatmentEndDate.text());
                        if (startDate !== lastTreatmentEndDateDate.getTime()) {
                            let diffInTime = treatmentEndDate.getTime() - lastTreatmentEndDateDate.getTime();
                            jQuery("#treatmentDaysLost").val(diffInTime / (1000 * 3600 * 24));
                        }
                    }
                }
            }
        }

        function dateDiff(date){
            const treatmentEndDate = jQuery("#lastTreatmentEndDate");
            if (treatmentEndDate.val()) {
                const date2 = getDate(treatmentEndDate.text());
                const diffTime = Math.abs(date2 - date);
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                console.log(diffTime + " milliseconds");
                console.log(diffDays + " days");
                let message = 'dans ' + diffDays + ' jrs';
                if (diffDays < 0) {
                    treatmentEndDate.removeClass('bg-info');
                    treatmentEndDate.addClass('bg-warning');
                    message = 'depuis ' + diffDays + ' jrs';
                }
                jQuery('#remainingDaysToTreatmentEndDate').text(message);
            }
        }

        function formatDate(date) {
            let d = new Date(date),
                month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear();

            if (month.length < 2)
                month = '0' + month;
            if (day.length < 2)
                day = '0' + day;

            return [day, month, year].join('/');
        }

        function addDays(date, days) {
            console.log(date);
            console.log(days);
            return date.getTime() + (days * 24 * 60 * 60 * 1000);
            // return date.setDate(date.getDate() + days);
        }
        function removeDays(date, days) {
            console.log('date to remove', date);
            console.log('days to remove', days);
            // return date.setDate(date.getDate() - days);
            return date.getTime() - (days * 24 * 60 * 60 * 1000);
        }

    }
</script>
<div class="container-fluid mt-2">
    <div class="row mb-2">
        <div class="col-6 text-uppercase font-italic text-secondary">
            <div class="h6"><i class="fa fa-pen-square"></i> ${subTitle}</div>
        </div>
        <%--        <div class="col-6 text-right">--%>
        <%--            <c:url value="/module/pharmacy/operations/dispensation/list.form" var="url"/>--%>
        <%--            <button class="btn btn-primary btn-sm" onclick="window.location='${url}'" title="Voir la liste">--%>
        <%--                <i class="fa fa-list"></i> Voir la liste--%>
        <%--            </button>--%>
        <%--        </div>--%>
    </div>
    <div class="row bg-light pt-2 pb-2 border border-secondary">
        <div class="col-12">
            <form:form modelAttribute="productDispensationForm" method="post" action="" id="form">
                <form:hidden path="productOperationId"/>
                <form:hidden path="uuid"/>
                <form:hidden path="locationId"/>
                <form:hidden path="incidence"/>
                <form:hidden path="operationStatus"/>
                <form:hidden path="productProgramId"/>
                <form:hidden path="patientId"/>
                <form:hidden path="mobilePatientId"/>
                <form:hidden path="encounterId"/>
                <form:hidden path="mobileDispensationInfoId"/>
                <form:hidden path="patientIdentifier"/>
                <form:hidden path="patientType"/>

                <div class="row">
                    <div class="col-12">
                        <fieldset>
                            <legend>
                                <div class="row">
                                    <div class="col-8">
                                        Programme : <span class="text-success text-lg-left font-italic">${program.name}</span>
                                        <c:if test="${program.name == 'PNLSARVIO'}">
                                            | Patient (<span class="text-info font-italic">${productDispensationForm.patientType == 'ON_SITE' ?
                                            'PEC SUR LE SITE' : (productDispensationForm.patientType == 'MOBILE' ? 'MOBILE' : 'PREVENTION')}</span>)
                                        </c:if>

                                        <c:if test="${patientAlert != null}">
                                            <span class="badge">${patientAlert}</span>
                                        </c:if>
                                    </div>
                                    <div class="col-4 text-right">
                                        <c:if test="${productDispensationForm.operationStatus == 'AWAITING_VALIDATION'}">
                                            <button class="btn btn-success btn-sm" tabindex="-1" name="action" value="add">
                                                <i class="fa fa-save"></i>
                                                    <%--                                        <spring:message code="pharmacy.validate" />--%>
                                                Enregistrer
                                            </button>
                                        </c:if>
                                        <button type="submit" class="btn btn-primary btn-sm" tabindex="-1">
                                            <i class="fa fa-tablets"></i> Saisie des produits
                                        </button>
                                        <c:url value="/module/pharmacy/operations/dispensation/deletePatient.form" var="delUrl">
                                            <c:param name="patientId" value="${productDispensationForm.patientId}"/>
                                        </c:url>
                                        <a class="btn btn-warning btn-sm" href="${delUrl}" onclick="return confirm('Voulez vous vraiment supprimer la dispensation en cours de saisie ?')" tabindex="-1">
                                            <i class="fa fa-eject"></i> Annuler
                                        </a>
                                    </div>
                                </div>
                            </legend>
                            <div class="card">
                                <div class="card-body p-2 border border-info">
                                    <div class="row">
                                        <div class="col-4">
                                            <h6 class="font-italic text-secondary h6">Information du Patient</h6>
                                            <div class="card bg-light">
                                                <div class="card-body p-2">
                                                    <div class="row mb-2">
                                                        <div class="col-12">
                                                            <label class="">Num&eacute;ro</label>
                                                            <div class="form-control form-control-sm bg-info text-white">
                                                                    ${productDispensationForm.patientIdentifier}
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row mb-1">
                                                        <div class="col-4">
                                                            <label class="">Age <span class="required">*</span> </label>
                                                            <form:input path="age" cssClass="form-control form-control-sm" readonly="${productDispensationForm.patientType == 'ON_SITE'}" />
                                                            <form:errors path="age" cssClass="error"/>
                                                        </div>
                                                        <div class="col-8">
                                                            <label class="mb-2">Genre <span class="required">*</span></label>
                                                            <br>
                                                            <c:if test="${productDispensationForm.patientType != 'ON_SITE'}">
                                                                <form:radiobutton path="gender" value="M" label=" Masculin" cssClass="mr-2"/>
                                                                <form:radiobutton path="gender" value="F" label=" Feminin" cssClass="ml-2 mr-2"/>
                                                                <form:errors path="gender" cssClass="error"/>
                                                            </c:if>
                                                            <c:if test="${productDispensationForm.patientType == 'ON_SITE'}">
                                                                <span class="mr-3 text-info">
                                                                    ${productDispensationForm.gender == 'M' ? '&ofcir;' : '&cir;'} &nbsp;&nbsp; Masculin
                                                                </span>
                                                                <span class="text-info">
                                                                    ${productDispensationForm.gender == 'F' ? '&ofcir;' : '&cir;'} &nbsp;&nbsp; F&eacute;minin
                                                                </span>
                                                                <form:hidden path="gender"/>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                    <c:if test="${regimens != null && fct:length(regimens) != 0}">
                                                        <div class="row mb-2">
                                                            <div class="col-12">
                                                                <label class="">R&eacute;gime<%-- <span
                                                                        class="required">*</span>--%></label>
                                                                <form:select path="productRegimenId"
                                                                             cssClass="form-control s2">
                                                                    <form:option value="" label="AUCUN REGIME"/>
                                                                    <form:options items="${regimens}"
                                                                                  itemValue="productRegimenId"
                                                                                  itemLabel="concept.name.name"/>
                                                                </form:select>
                                                                <form:errors path="productRegimenId" cssClass="error"/>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-3">
                                            <h6 class="font-italic text-secondary">Derni&egrave;re Dispensation</h6>
                                            <div class="card bg-light text-info border border-info">
                                                <div class="card-body p-2">
                                                    <c:if test="${productDispensationForm.patientType == 'OTHER_HIV'}">
                                                        <div class="row align-items-center">
                                                            <div class="col-12">
                                                                <div class="h4 text-warning font-italic font-weight-bold">
                                                                    Non Applicable
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${productDispensationForm.patientType == 'MOBILE' || productDispensationForm.patientType == 'ON_SITE'}">
                                                        <%--                                                        <div class="row align-items-center">--%>
                                                        <%--                                                            <div class="col-12">--%>
                                                        <%--                                                                Ce patient a d&eacute;j&agrave; effectu&eacute;--%>
                                                        <%--                                                                <span class="font-weight-bold text-primary"> ${fct:length(mobilePatient.mobilePatientDispensationInfos) }</span>--%>
                                                        <%--                                                                visite(s)--%>
                                                        <%--                                                            </div>--%>
                                                        <%--                                                        </div>--%>
                                                        <%--                                                    </c:if>--%>
                                                        <%--                                                    <c:if test="${}">--%>
                                                        <c:if test="${lastDispensation == null}">
                                                            <div class="row align-items-center">
                                                                <div class="col-12">
                                                                    <span class="font-weight-bold text-primary">
                                                                        Ce patient est &agrave; sa premi&egrave;re dispensation dans le centre
                                                                    </span>
                                                                </div>
                                                            </div>
                                                        </c:if>
                                                        <c:if test="${lastDispensation != null}">
                                                            <div class="row mb-1">
                                                                <div class="col-12">
                                                                    <label>Date</label>
                                                                    <div class="form-control form-control-sm bg-info text-white">
                                                                        <fmt:formatDate value="${lastDispensation.dispensationDate}" pattern="dd/MM/yyyy" type="DATE"/>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="row mb-2">
                                                                <div class="col-6">
                                                                    <label>R&eacute;gime</label>
                                                                    <div class="form-control form-control-sm bg-info text-white">
                                                                            ${lastDispensation.regimen}
                                                                    </div>
                                                                </div>
                                                                <div class="col-6">
                                                                    <label>Nb jrs TTT perdus</label>
                                                                    <form:input path="treatmentDaysLost" cssClass="form-control form-control-sm" onchange="getTreatmentEndDate()" />
                                                                    <form:errors path="treatmentDaysLost" cssClass="error"/>
                                                                </div>
                                                            </div>
                                                            <div class="row mb-2">
                                                                <div class="col-5">
                                                                    <label>Nb jours TTT</label>
                                                                    <div class="form-control form-control-sm bg-info text-white">
                                                                            ${lastDispensation.treatmentDays}
                                                                    </div>
                                                                </div>
                                                                <div class="col-7">
                                                                    <label>Date de fin de TTT</label>
                                                                    <div class="form-control form-control-sm bg-info text-white">
                                                                            <span class="" id="lastTreatmentEndDate">
                                                                                <fmt:formatDate
                                                                                        value="${lastDispensation.treatmentEndDate}"
                                                                                        pattern="dd/MM/yyyy"
                                                                                        type="DATE"/>
                                                                            </span>
                                                                        (<span id="remainingDaysToTreatmentEndDate" class=""></span>)
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </c:if>
                                                    </c:if>
                                                </div>
                                            </div>

                                        </div>
                                        <div class="col-5">
                                            <h6 class="font-italic text-secondary h6">Information de dispensation</h6>
                                            <div class="card bg-light">
                                                <div class="card-body p-2">
                                                    <div class="row mb-2">
                                                        <div class="col-8">
                                                            <label class="pt-1">Prescripteur : </label>
                                                            <form:select path="providerId" cssClass="form-control s2">
                                                                <form:option value="" label=""/>
                                                                <form:options items="${providers}" itemValue="providerId" itemLabel="name"/>
                                                            </form:select>
                                                            <form:errors path="providerId" cssClass="error"/>
                                                        </div>
                                                        <div class="col-4">
                                                            <label class="">Date de prescription</label>
                                                            <form:input path="prescriptionDate" tabindex="true" cssClass="form-control form-control-sm picker"/>
                                                            <form:errors path="prescriptionDate" cssClass="error"/>
                                                        </div>
                                                    </div>
                                                        <%--                                                    <c:if test="${productDispensationForm.patientType == 'ON_SITE' || productDispensationForm.patientType == 'MOBILE'}">--%>
                                                        <%--                                                    </c:if>--%>
                                                    <c:if test="${program.name == 'PNLSARVIO'}">
                                                        <div class="row mb-2">
                                                            <div class="col-12">
                                                                <label class="mb-1">But <span class="required">*</span></label>
                                                                <br>
                                                                <c:if test="${productDispensationForm.patientType == 'ON_SITE' || productDispensationForm.patientType == 'MOBILE'}">
                                                                    <form:radiobutton path="goal" tabindex="true" value="NOT_APPLICABLE" label=" Non Applicable" cssClass="mr-2"/>
                                                                    <form:radiobutton path="goal" value="PEC" label=" PEC" cssClass="ml-1 mr-2"/>
                                                                    <form:radiobutton path="goal" value="PTME" label="PTME" cssClass="mr-2"/>
                                                                </c:if>
                                                                <c:if test="${productDispensationForm.patientType == 'OTHER_HIV'}">
<%--                                                                    <span class="font-weight-bold">AES : </span>--%>
<%--                                                                    <form:radiobutton path="goal" value="AES_HEALTH_STAFF" label=" Personnel de sante " cssClass="ml-1 mr-2"/>--%>
<%--                                                                    <form:radiobutton path="goal" value="AES_CONDOM_RUPTURE" label=" Rupture preservatif" cssClass="ml-1 mr-2"/>--%>
<%--                                                                    <form:radiobutton path="goal" value="AES_RAPE" label=" Viol" cssClass="ml-1 mr-2"/>--%>
<%--                                                                    <span class="m-3 font-weight-bold">|</span>--%>
                                                                    <form:radiobutton path="goal" value="AES" label=" AES" cssClass="ml-1 mr-2"/>
                                                                    <form:radiobutton path="goal" value="PREP" label=" PREP" cssClass="ml-1 mr-2"/>
                                                                </c:if>
                                                                <form:errors path="goal" cssClass="error"/>
                                                            </div>
                                                        </div>
                                                    </c:if>

                                                    <div class="row mb-2">
                                                        <c:if test="${productDispensationForm.patientType == 'OTHER_HIV' || productDispensationForm.patientType == 'MOBILE' || productDispensationForm.patientType == 'ON_SITE'}">
                                                            <div class="col-5">
                                                                <label class="">Date de dispensation <span class="required">*</span></label>
                                                                <form:input path="operationDate" cssClass="form-control form-control-sm picker" onchange="getTreatmentEndDate()"/>
                                                                <form:errors path="operationDate" cssClass="error"/>
                                                            </div>
                                                            <div class="col-3">
                                                                <label class="">Nb Jours TTT<span class="required">*</span> </label>
                                                                <form:input path="treatmentDays" cssClass="form-control form-control-sm" onchange="getTreatmentEndDate()"/>
                                                                <form:errors path="treatmentDays" cssClass="error"/>
                                                            </div>
                                                        </c:if>
                                                        <c:if test="${productDispensationForm.patientType == 'ON_SITE' || productDispensationForm.patientType == 'MOBILE'}">
                                                            <div class="col-4">
                                                                <label class="">Date de fin de TTT <span class="required">*</span> </label>
                                                                <form:input path="treatmentEndDate" tabindex="-1" cssClass="form-control form-control-sm" readonly="true"/>
                                                                <form:errors path="treatmentEndDate" cssClass="error"/>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>

<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
