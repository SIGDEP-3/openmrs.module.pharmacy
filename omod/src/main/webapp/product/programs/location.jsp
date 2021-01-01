<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../../template/localHeader.jsp"%>
<openmrs:require privilege="Save Program" otherwise="/login.htm" redirect="/module/pharmacy/product/programs/location.form" />
<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            <c:forEach var="location" items="${ locations }">
            jQuery("#productProgramIds-${location.locationId}").on('change', function () {
                // let id = jQuery(this).attr('id');
                let value =  jQuery(this).val();
                saveLocationPrograms(${location.locationId}, value);
            });
            </c:forEach>
            <%--for (let i = 0; i < ${fct:length(locations)}; i++) {--%>
            <%--    --%>
            <%--}--%>
        });

        function saveLocationPrograms(locationId, programIds) {
            if (programIds) {
                jQuery.ajax({
                    type: 'GET',
                    url: '${pageContext.request.contextPath}/save-location-program.form?locationId=' +
                        locationId + '&programIds='+ programIds,
                    dataType : "json",
                    crossDomain:true,
                    success : function(data) {
                        console.log(data);
                        //jQuery('.' + batchNumber).parent().html(data.quantity)
                        //alert(data.toString());
                    },
                    error : function(data) {
                        console.log(data)
                    }
                });
            }
        }
    }
</script>
<hr>
<div class="row">
    <div class="col-6">
        <h4>${title}</h4>
    </div>
    <div class="col-6 text-right">
        <c:url value="/module/pharmacy/product/programs/list.form" var="listUrl"/>
        <button class="btn btn-primary" onclick="window.location='${listUrl}'" title="Voir la liste">
            <i class="fa fa-list"></i>
        </button>
    </div>
</div>
<hr>
<div class="container">
    <div class="row">
        <div class="col-12">
            <table class="table-sm table table-bordered">
                <thead class="bg-gray">
                <tr>
                    <th>Centre / PPS</th>
                    <th>Programmes disponibles</th>
                </tr>
                </thead>
                <c:forEach var="location" items="${ locations }">
                    <tr>
                        <td>${location.name}</td>
                        <td id="${location.locationId}">
                            <select id="productProgramIds-${location.locationId}" class="form-control s2" multiple="multiple">
                                <option value="" label=""></option>
                                <c:forEach var="program" items="${ programs }">
                                    <option value="${program.productProgramId}"
                                            ${fct:contains(locationPrograms[location.locationId], program.productProgramId) ? 'selected="selected"' : ''}>
                                            ${program.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>

                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>


<%@ include file="../../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
