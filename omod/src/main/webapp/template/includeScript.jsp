<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/jQuery/jquery-3.3.1.min.js"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/jqueryui/jquery-ui.min.js"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/bootstrap/js/bootstrap.min.js"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/bootstrap/js/bootstrap.bundle.min.js"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/datatables.min.js"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/DataTables/js/dataTables.bootstrap4.min.js"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/DataTables/js/date-ue.js"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/fontawesome-free/js/all.js"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/select2/js/select2.min.js"/>
<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/jquery.monthpicker.js"/>
<%--<openmrs:htmlInclude file="${pageContext.request.contextPath}/moduleResources/pharmacy/themes/theme.min.js"/>--%>

<script type="application/javascript" >
    if (jQuery) {

        jQuery.extend( true, jQuery.fn.dataTable.defaults, {
            language: {
                "lengthMenu": "Afficher _MENU_ lignes",
                "zeroRecords": "Aucune ligne",
                "info": "Page _PAGE_ sur _PAGES_ (_TOTAL_ lignes)",
                "infoEmpty": "Aucun enregistrement",
                "infoFiltered": "(lignes sur _MAX_ )",
                "loadingRecords": "Chargement...",
                "search": "Recherche",
                "oPaginate": {
                    "sNext": '<i class="fa fa-angle-right"></i>',
                    "sPrevious": '<i class="fa fa-angle-left"></i>',
                    "sFirst": '<i class="fa fa-angle-double-left"></i>',
                    "sLast": '<i class="fa fa-angle-double-right"></i>'
                }
            }
        } );

        jQuery.extend( jQuery.fn.dataTableExt.oSort, {
            "date-uk-pre": function ( a ) {
                if (a == null || a === "") {
                    return 0;
                }
                var ukDatea = a.split('/');
                var ukDatea2 = ukDatea[2].split(' ');
                if (ukDatea2.length === 1){
                    return (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
                } else {
                    var ukTimea = ukDatea2[1].split(':');
                    return (ukDatea[2] + ukDatea[1] + ukDatea[0] + ukTimea[0] + ukTimea[1] + ukTimea[2]) * 1;
                }
            },

            "date-uk-asc": function ( a, b ) {
                return ((a < b) ? -1 : ((a > b) ? 1 : 0));
            },

            "date-uk-desc": function ( a, b ) {
                return ((a < b) ? 1 : ((a > b) ? -1 : 0));
            }
        } );

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
