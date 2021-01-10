<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="../template/localHeader.jsp"%>
<openmrs:require privilege="Save location" otherwise="/login.htm" redirect="/module/pharmacy/center/manage.form" />
<script>
    if (jQuery) {
        jQuery(document).ready(function (){
            jQuery(".location").on('change', function () {
                let id = jQuery(this).attr('id');
                let value =  jQuery(this).val();
                saveLocationPrograms(id, value);
            });

            jQuery(".mySelect2").select2({
                dropdownParent: jQuery("#dialog-form")
            })

            jQuery('input[name=clientInfo]').on('click', function (){
                let clientCheckbox = jQuery(this);
                let id = clientCheckbox.attr('id');
                let idSplit = id.split('-');
                let locationId = idSplit[1];
                let value = clientCheckbox.val();
                saveLocationClientInfo(locationId, value);
            });
        });

        jQuery(function () {
            let dialog, form,
                name = jQuery("#name"),
                nameSelected = jQuery("#nameSelected"),
                prefix = jQuery("#prefix"),
                allFields = jQuery([]).add(name).add(prefix),
                tips = jQuery( ".validateTips" );

            dialog = jQuery( "#dialog-form" ).dialog({
                autoOpen: false,
                height: 400,
                width: 500,
                modal: true,
                buttons: {
                    "Ajouter ": addLocation,
                    Cancel: function() {
                        dialog.dialog( "close" );
                    }
                },
                close: function() {
                    form[ 0 ].reset();
                    allFields.removeClass( "ui-state-error" );
                }
            });

            form = dialog.find( "form" ).on( "submit", function( event ) {
                event.preventDefault();
                addLocation();
            });

            jQuery( "#add-existing" ).button().on( "click", function() {
                name.hide();
                jQuery("#existingNameZone").show();
                dialog.dialog( "open" );
            });

            $( "#add-new" ).button().on( "click", function() {
                name.show();
                jQuery("#existingNameZone").hide();
                dialog.dialog( "open" );
            });

            function updateTips( t ) {
                tips
                    .text( t )
                    .addClass( "ui-state-highlight" );
                setTimeout(function() {
                    tips.removeClass( "ui-state-highlight", 1500 );
                }, 500 );
            }

            function checkLength( o, n, min, max ) {
                if ( o.val().length > max || o.val().length < min ) {
                    o.addClass( "ui-state-error" );
                    updateTips( "La longueur du " + n + " doit etre compris entre " +
                        min + " et " + max + "." );
                    return false;
                } else {
                    return true;
                }
            }

            function checkRegexp( o, regexp, n ) {
                if ( !( regexp.test( o.val() ) ) ) {
                    o.addClass( "ui-state-error" );
                    updateTips( n );
                    return false;
                } else {
                    return true;
                }
            }

            function addLocation() {
                let valid = true;
                allFields.removeClass( "ui-state-error" );
                if (nameSelected.val()) {
                    name.val(nameSelected.val());
                }
                // valid = valid && checkLength( name, "service/centre", 3, 255 );
                valid = valid && checkLength( prefix, "Prefix", 8, 8 );

                // valid = valid && checkRegexp( name, /^[a-z]([0-9A-zÀ-ú_\s])+$/i, "Le nom du centre/service doit etre compose de a-z, 0-9, underscores, espaces et doit commentcer par un lettre." );
                valid = valid && checkRegexp( prefix, /^[0-9]{4}\/.{2}\//i, "Le format du préfixe n'est pas correct : 0000/XX/" );

                if ( valid ) {
                    saveLocation(name.val(), prefix.val());
                    dialog.dialog( "close" );
                }
                return valid;
            }
        });

        function saveLocationPrograms(locationId, programIds) {
            if (locationId) {
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

        function saveLocationClientInfo(locationId, value) {
            if (locationId && value) {
                console.log('locationId', locationId);
                jQuery.ajax({
                    type: 'GET',
                    url: '${pageContext.request.contextPath}/save-location-client-info.form?locationId=' +
                        locationId + '&value='+ value,
                    dataType : "json",
                    crossDomain:true,
                    success : function(data) {
                        //console.log(data);
                        //jQuery('.' + batchNumber).parent().html(data.quantity)
                        //alert(data.toString());
                    },
                    error : function(data) {
                        console.log(data)
                    }
                });
            }
        }

        function retireLocation(locationId) {
            if (confirm("Voulez-vous retirer ce service de votre liste ")) {
                jQuery.ajax({
                    type: 'GET',
                    url: '${pageContext.request.contextPath}/retire-location.form?locationId=' + locationId,
                    dataType: "json",
                    crossDomain: true,
                    success: function (data) {
                    },
                    error: function (data) {
                        // location.href = location.href;
                        window.location.reload();
                    }
                });
            }
        }

        function saveLocation(id, prefix) {
            let type = 'service';
            if (jQuery("#nameSelected").val()){
                type = 'location';
            }
            if (id && prefix) {
                let uri = encodeURI('${pageContext.request.contextPath}/save-location.form?' + type +'Id=' +
                    id + '&prefix='+ prefix);
                // console.log(uri);
                jQuery.ajax({
                    type: 'GET',
                    url: uri,
                    dataType : "json",
                    crossDomain:true,
                    success : function(data) {
                    },
                    error : function(data) {
                        //location.href = location.href
                        window.location.reload()
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
</div>
<hr>
<div class="container">
    <div class="row">
        <div class="col-12">
            <table class="table-sm table table-bordered" id="listLocation">
                <thead class="bg-gray">
                <tr>
                    <th>Centre / PPS</th>
                    <th>Programmes disponibles</th>
                    <th>Client NPSP</th>
                </tr>
                </thead>
                <tbody id="locationListBody">
                <c:forEach var="location" items="${ locations }">
                    <tr id="location-${location.locationId}">
                        <td>${location.name} (${location.postalCode != null ? location.postalCode : 'Definir le Prefixe'})</td>
                        <td id="${location.locationId}">
                            <select id="${location.locationId}" class="form-control s2 location" multiple="multiple">
                                <option value=""></option>
                                <c:forEach var="program" items="${ programs }">
                                    <option value="${program.productProgramId}"
                                        ${fct:contains(locationPrograms[location.locationId], program.productProgramId) ? 'selected="selected"' : ''}>
                                            ${program.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>
                            <c:if test="${location.name == userLocation.name}">
                                <label for="clientInfo-${location.locationId}-false">
                                    <input type="radio" name="clientInfo" id="clientInfo-${location.locationId}-false" value="false" <c:if test="${isDirectClient == false }">checked</c:if>> Non
                                </label>
                                <label for="clientInfo-${location.locationId}-true">
                                    <input type="radio" name="clientInfo" id="clientInfo-${location.locationId}-true" value="true" <c:if test="${isDirectClient == true }">checked</c:if>> Oui
                                </label>
                            </c:if>
                            <c:if test="${location.name != userLocation.name}">
                                <button type="button" class="btn btn-sm btn-danger"
                                        onclick="retireLocation(${location.locationId})">
                                    <i class="fa fa-trash"></i> Retirer
                                </button>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <button id="add-existing" type="button" class="btn btn-sm btn-primary">Ajouter existant</button>
            <button id="add-new" type="button" class="btn btn-sm btn-info">Ajouter nouveau</button>
        </div>
    </div>
</div>
<div id="dialog-form" title="Ajouter Service / Centre">
    <p class="validateTips">Tous les champs sont requis</p>

    <div class="row">
        <div class="col-12">
            <form>
                <fieldset>
                    <label for="name">Nom du centre / Service <span class="required">*</span></label>
                    <select name="name" id="name" style="width: 100%" class="form-control-sm mySelect2">
                        <option value=""></option>
                        <c:forEach var="service" items="${services}">
                            <option value="${service.key}">${service.value}</option>
                        </c:forEach>
                    </select>
                    <div class="row" id="existingNameZone">
                        <div class="col-12">
                            <select name="name" id="nameSelected" style="width: 100%" class="form-control-sm mySelect2">
                                <option value=""></option>
                                <c:forEach var="eLocation" items="${allLocations}">
                                    <option value="${eLocation.locationId}">${eLocation.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <label for="prefix">Pr&eacute;fixe <span class="required">*</span></label>
                    <input type="text" name="prefix" id="prefix" value="" class="form-control form-control-sm" required>
                    <input type="submit" tabindex="-1" style="position:absolute; top:-1000px">

                </fieldset>
            </form>
        </div>
    </div>
</div>

<%@ include file="../template/localFooter.jsp"%>
<%@ include file="/WEB-INF/template/footer.jsp"%>
