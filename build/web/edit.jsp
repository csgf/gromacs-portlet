<%
/**************************************************************************
Copyright (c) 2011-2015:
Istituto Nazionale di Fisica Nucleare (INFN), Italy
Consorzio COMETA (COMETA), Italy

See http://www.infn.it and and http://www.consorzio-cometa.it for details on 
the copyright holders.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

@author <a href="mailto:giuseppe.larocca@ct.infn.it">Giuseppe La Rocca</a>
****************************************************************************/
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="javax.portlet.*"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<portlet:defineObjects/>

<%
  //
  // GROMACS 2.0.8 portlet preferences for the GirdEngine interaction
  //
  // These parameters are:  
  // o *_gromacs_INFRASTRUCTURE  - The Infrastructure Acronym to be enabled
  // o *_gromacs_TOPBDII         - The TopBDII hostname for accessing the Infrastructure
  // o *_gromacs_WMS             - The WMProxy hostname for accessing the Infrastructure
  // o *_gromacs_VONAME          - The VO name
  // o *_gromacs_ETOKENSERVER    - The eTokenServer hostname to be contacted for 
  //                            requesting grid proxies
  // o *_gromacs_MYPROXYSERVER   - The MyProxyServer hostname for requesting 
  //                            long-term grid proxies
  // o *_gromacs_PORT            - The port on which the eTokenServer is listening
  // o *_gromacs_ROBOTID         - The robotID to generate the grid proxy
  // o *_gromacs_WEBDAV          - The WEBDAV Server for the Infrastructure
  // o *_gromacs_ROLE            - The FQAN for the grid proxy (if any)
  // o *_gromacs_REWAL           - Enable the creation of a long-term proxy to a 
  //                            MyProxy Server (default 'yes')
  // o *_gromacs_DISABLEVOMS     - Disable the creation of a VOMS proxy (default 'no')
  //
  // o gromacs_APPID             - The ApplicationID for the GROMACS application
  // o gromacs_LOGLEVEL          - The portlet log level (INFO, VERBOSE)
  // o gromacs_METADATA_HOST     - The Metadata hostname
  // o gromacs_OUTPUT_PATH       - The ApplicationID for the GROMACS results
  // o gromacs_SOFTWARE          - The Application Software requested by the application
  // o gromacs_LOCAL_PROXY       - The local proxy file path
  // o TRACKING_DB_HOSTNAME   - The Tracking DB server hostname 
  // o TRACKING_DB_USERNAME   - The username credential for login
  // o TRACKING_DB_PASSWORD   - The password for login
%>

<jsp:useBean id="lato_gromacs_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_gromacs_PASSWD" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_gromacs_WMS" class="java.lang.String[]" scope="request"/>
<jsp:useBean id="lato_gromacs_LOGIN" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_gromacs_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_gromacs_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_gromacs_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_gromacs_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_gromacs_WEBDAV" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_gromacs_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_gromacs_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="lato_gromacs_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="garuda_gromacs_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_gromacs_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_gromacs_WMS" class="java.lang.String[]" scope="request"/>
<jsp:useBean id="garuda_gromacs_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_gromacs_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_gromacs_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_gromacs_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_gromacs_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_gromacs_WEBDAV" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_gromacs_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_gromacs_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="garuda_gromacs_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="eumed_gromacs_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_gromacs_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_gromacs_WMS" class="java.lang.String[]" scope="request"/>
<jsp:useBean id="eumed_gromacs_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_gromacs_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_gromacs_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_gromacs_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_gromacs_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_gromacs_WEBDAV" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_gromacs_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_gromacs_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="eumed_gromacs_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="sagrid_gromacs_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_gromacs_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_gromacs_WMS" class="java.lang.String[]" scope="request"/>
<jsp:useBean id="sagrid_gromacs_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_gromacs_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_gromacs_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_gromacs_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_gromacs_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_gromacs_WEBDAV" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_gromacs_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_gromacs_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="sagrid_gromacs_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="see_gromacs_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_gromacs_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_gromacs_WMS" class="java.lang.String[]" scope="request"/>
<jsp:useBean id="see_gromacs_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_gromacs_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_gromacs_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_gromacs_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_gromacs_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_gromacs_WEBDAV" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_gromacs_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_gromacs_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="see_gromacs_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="gisela_gromacs_INFRASTRUCTURE" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_gromacs_TOPBDII" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_gromacs_WMS" class="java.lang.String[]" scope="request"/>
<jsp:useBean id="gisela_gromacs_VONAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_gromacs_ETOKENSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_gromacs_MYPROXYSERVER" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_gromacs_PORT" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_gromacs_ROBOTID" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_gromacs_WEBDAV" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_gromacs_ROLE" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_gromacs_RENEWAL" class="java.lang.String" scope="request"/>
<jsp:useBean id="gisela_gromacs_DISABLEVOMS" class="java.lang.String" scope="request"/>

<jsp:useBean id="gromacs_ENABLEINFRASTRUCTURE" class="java.lang.String[]" scope="request"/>
<jsp:useBean id="gromacs_APPID" class="java.lang.String" scope="request"/>
<jsp:useBean id="gromacs_LOGLEVEL" class="java.lang.String" scope="request"/>
<jsp:useBean id="gromacs_METADATA_HOST" class="java.lang.String" scope="request"/>
<jsp:useBean id="gromacs_OUTPUT_PATH" class="java.lang.String" scope="request"/>
<jsp:useBean id="gromacs_SOFTWARE" class="java.lang.String" scope="request"/>
<jsp:useBean id="gromacs_LOCAL_PROXY" class="java.lang.String" scope="request"/>
<jsp:useBean id="TRACKING_DB_HOSTNAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="TRACKING_DB_USERNAME" class="java.lang.String" scope="request"/>
<jsp:useBean id="TRACKING_DB_PASSWORD" class="java.lang.String" scope="request"/>

<jsp:useBean id="SMTP_HOST" class="java.lang.String" scope="request"/>
<jsp:useBean id="SENDER_MAIL" class="java.lang.String" scope="request"/>

<script type="text/javascript">
    
    //var EnabledInfrastructure = "<%= gromacs_ENABLEINFRASTRUCTURE %>";    
    //console.log(EnabledInfrastructure);        
            
    $(document).ready(function() { 
        
        var EnabledInfrastructure = null;           
        var infrastructures = new Array();  
        var i = 0;    
    
        <c:forEach items="<%= gromacs_ENABLEINFRASTRUCTURE %>" var="current">
        <c:choose>
        <c:when test="${current!=null}">
            infrastructures[i] = '<c:out value='${current}'/>';       
            i++;  
        </c:when>
        </c:choose>
        </c:forEach>
        
        for (var i = 0; i < infrastructures.length; i++) {
        console.log("Reading array = " + infrastructures[i]);
        if (infrastructures[i]=="lato") EnabledInfrastructure='lato';        
        if (infrastructures[i]=="garuda") EnabledInfrastructure='garuda';        
        if (infrastructures[i]=="eumed")  EnabledInfrastructure='eumed';
        if (infrastructures[i]=="sagrid") EnabledInfrastructure='sagrid';
        if (infrastructures[i]=="see") EnabledInfrastructure='see';
        if (infrastructures[i]=="gisela") EnabledInfrastructure='gisela';
        }
        
        var NMAX = infrastructures.length;
        console.log (NMAX);
        console.log (EnabledInfrastructure);
                
        var lato_inputs = 1;        
        // ADDING a new WMS enpoint for the LATO (MAX. 5)
        $('#adding_WMS_lato').click(function() {        
            ++lato_inputs;        
            if (lato_inputs>1 && lato_inputs<6) {
            var c = $('.cloned_lato_gromacs_WMS:first').clone(true);            
            c.children(':text').attr('name','lato_gromacs_WMS' );
            c.children(':text').attr('id','lato_gromacs_WMS' );
            $('.cloned_lato_gromacs_WMS:last').after(c);
        }        
        });
        
        // REMOVING a new WMS enpoint for LATO
        $('.btnDel_lato').click(function() {
        if (lato_inputs > 1)
            if (confirm('Do you really want to delete the item ?')) {
            --lato_inputs;
            $(this).closest('.cloned_lato_gromacs_WMS').remove();
            $('.btnDel_lato').attr('disabled',($('.cloned_lato_gromacs_WMS').length  < 2));
        }
        });
        
        $('.btnDel_lato2').click(function() {            
            if (confirm('Do you really want to delete the item ?')) {
            --lato_inputs;
            $(this).closest('.cloned_cached_latoWMS').remove();
            $('.btnDel_lato2').attr('disabled',($('.cloned_cached_latoWMS').length  < 2));
        }
        });
                
        var garuda_inputs = 1;        
        // ADDING a new WMS enpoint for the GARUDA infrastructure (MAX. 5)
        $('#adding_WMS_garuda').click(function() {        
            ++garuda_inputs;        
            if (garuda_inputs>1 && garuda_inputs<6) {
            var c = $('.cloned_garuda_gromacs_WMS:first').clone(true);            
            c.children(':text').attr('name','garuda_gromacs_WMS' );
            c.children(':text').attr('id','garuda_gromacs_WMS' );
            $('.cloned_garuda_gromacs_WMS:last').after(c);
        }        
        });
        
        // REMOVING a new WMS enpoint for the GARUDA infrastructure
        $('.btnDel_garuda').click(function() {
        if (garuda_inputs > 1)
            if (confirm('Do you really want to delete the item ?')) {
            --garuda_inputs;
            $(this).closest('.cloned_garuda_gromacs_WMS').remove();
            $('.btnDel_garuda').attr('disabled',($('.cloned_garuda_gromacs_WMS').length  < 2));
        }
        });
        
        $('.btnDel_garuda2').click(function() {            
            if (confirm('Do you really want to delete the item ?')) {
            --garuda_inputs;
            $(this).closest('.cloned_cached_garudaWMS').remove();
            $('.btnDel_garuda2').attr('disabled',($('.cloned_cached_garudaWMS').length  < 2));
        }
        });                        
        
        var eumed_inputs = 1;        
        // ADDING a new WMS enpoint for the EUMED infrastructure (MAX. 5)
        $('#adding_WMS_eumed').click(function() {        
            ++eumed_inputs;        
            if (eumed_inputs>1 && eumed_inputs<6) {
            var c = $('.cloned_eumed_gromacs_WMS:first').clone(true);            
            c.children(':text').attr('name','eumed_gromacs_WMS' );
            c.children(':text').attr('id','eumed_gromacs_WMS' );
            $('.cloned_eumed_gromacs_WMS:last').after(c);
        }        
        });
        
        // REMOVING a new WMS enpoint for the EUMED infrastructure
        $('.btnDel_eumed').click(function() {
        if (eumed_inputs > 1)
            if (confirm('Do you really want to delete the item ?')) {
            --eumed_inputs;
            $(this).closest('.cloned_eumed_gromacs_WMS').remove();
            $('.btnDel_eumed').attr('disabled',($('.cloned_eumed_gromacs_WMS').length  < 2));
        }
        });
                
        $('.btnDel_eumed2').click(function() {            
            if (confirm('Do you really want to delete the item ?')) {
            --eumed_inputs;
            $(this).closest('.cloned_cached_eumedWMS').remove();
            $('.btnDel_eumed2').attr('disabled',($('.cloned_cached_eumedWMS').length  < 2));
        }
        });
        
        var sagrid_inputs = 1;        
        // ADDING a new WMS enpoint for the SAGRID infrastructure (MAX. 5)
        $('#adding_WMS_sagrid').click(function() {        
            ++sagrid_inputs;        
            if (sagrid_inputs>1 && sagrid_inputs<6) {
            var c = $('.cloned_sagrid_gromacs_WMS:first').clone(true);            
            c.children(':text').attr('name','sagrid_gromacs_WMS' );
            c.children(':text').attr('id','sagrid_gromacs_WMS' );
            $('.cloned_sagrid_gromacs_WMS:last').after(c);
        }        
        });
        
        // REMOVING a new WMS enpoint for the SAGRID infrastructure
        $('.btnDel_sagrid').click(function() {
        if (sagrid_inputs > 1)
            if (confirm('Do you really want to delete the item ?')) {
            --sagrid_inputs;
            $(this).closest('.cloned_sagrid_gromacs_WMS').remove();
            $('.btnDel_sagrid').attr('disabled',($('.cloned_sagrid_gromacs_WMS').length  < 2));
        }
        });
        
        $('.btnDel_sagrid2').click(function() {            
            if (confirm('Do you really want to delete the item ?')) {
            --sagrid_inputs;
            $(this).closest('.cloned_cached_sagridWMS').remove();
            $('.btnDel_sagrid2').attr('disabled',($('.cloned_cached_sagridWMS').length  < 2));
        }
        });                
        
        var see_inputs = 1;        
        // ADDING a new WMS enpoint for the SEE infrastructure (MAX. 5)
        $('#adding_WMS_see').click(function() {        
            ++see_inputs;        
            if (see_inputs>1 && see_inputs<6) {
            var c = $('.cloned_see_gromacs_WMS:first').clone(true);            
            c.children(':text').attr('name','see_gromacs_WMS' );
            c.children(':text').attr('id','see_gromacs_WMS' );
            $('.cloned_see_gromacs_WMS:last').after(c);
        }        
        });
        
        // REMOVING a new WMS enpoint for the SEE infrastructure
        $('.btnDel_see').click(function() {
        if (see_inputs > 1)
            if (confirm('Do you really want to delete the item ?')) {
            --see_inputs;
            $(this).closest('.cloned_see_gromacs_WMS').remove();
            $('.btnDel_see').attr('disabled',($('.cloned_see_gromacs_WMS').length  < 2));
        }
        });
        
        $('.btnDel_see2').click(function() {            
            if (confirm('Do you really want to delete the item ?')) {
            --see_inputs;
            $(this).closest('.cloned_cached_seeWMS').remove();
            $('.btnDel_see2').attr('disabled',($('.cloned_cached_seeWMS').length  < 2));
        }
        });
        
        var gisela_inputs = 1;        
        // ADDING a new WMS enpoint for the GISELA infrastructure (MAX. 5)
        $('#adding_WMS_gisela').click(function() {        
            ++gisela_inputs;        
            if (gisela_inputs>1 && gisela_inputs<6) {
            var c = $('.cloned_gisela_gromacs_WMS:first').clone(true);            
            c.children(':text').attr('name','gisela_gromacs_WMS' );
            c.children(':text').attr('id','gisela_gromacs_WMS' );
            $('.cloned_gisela_gromacs_WMS:last').after(c);
        }        
        });
        
        // REMOVING a new WMS enpoint for the GISELA infrastructure
        $('.btnDel_gisela').click(function() {
        if (gisela_inputs > 1)
            if (confirm('Do you really want to delete the item ?')) {
            --gisela_inputs;
            $(this).closest('.cloned_gisela_gromacs_WMS').remove();
            $('.btnDel_gisela').attr('disabled',($('.cloned_gisela_gromacs_WMS').length  < 2));
        }
        });
        
        $('.btnDel_gisela2').click(function() {            
            if (confirm('Do you really want to delete the item ?')) {
            --gisela_inputs;
            $(this).closest('.cloned_cached_giselaWMS').remove();
            $('.btnDel_gisela2').attr('disabled',($('.cloned_cached_giselaWMS').length  < 2));
        }
        });                      

        // Validate input form
        $('#GromacsEditForm').validate({
            rules: {
                lato_gromacs_INFRASTRUCTURE: { required: true },
                lato_gromacs_LOGIN: { required: true },
                lato_gromacs_PASSWD: { required: true },
                lato_gromacs_WMS: { required: true },                
                
                garuda_gromacs_INFRASTRUCTURE: { required: true },
                garuda_gromacs_VONAME: { required: true },
                garuda_gromacs_WMS: { required: true },
                garuda_gromacs_MYPROXYSERVER: { required: true },
                garuda_gromacs_ETOKENSERVER: { required: true },                
                garuda_gromacs_PORT: { required: true },
                garuda_gromacs_ROBOTID: { required: true },
                garuda_gromacs_WEBDAV: { required: true },
                                                
                eumed_gromacs_INFRASTRUCTURE: { required: true },
                eumed_gromacs_VONAME: { required: true },
                eumed_gromacs_TOPBDII: { required: true },
                eumed_gromacs_WMS: { required: true },
                eumed_gromacs_MYPROXYSERVER: { required: true },
                eumed_gromacs_ETOKENSERVER: { required: true },                
                eumed_gromacs_PORT: { required: true },
                eumed_gromacs_ROBOTID: { required: true },
                eumed_gromacs_WEBDAV: { required: true },
                                
                sagrid_gromacs_INFRASTRUCTURE: { required: true },
                sagrid_gromacs_VONAME: { required: true },
                sagrid_gromacs_TOPBDII: { required: true },
                sagrid_gromacs_WMS: { required: true },
                sagrid_gromacs_MYPROXYSERVER: { required: true },
                sagrid_gromacs_ETOKENSERVER: { required: true },                
                sagrid_gromacs_PORT: { required: true },
                sagrid_gromacs_ROBOTID: { required: true },
                sagrid_gromacs_WEBDAV: { required: true },
                                
                see_gromacs_INFRASTRUCTURE: { required: true },
                see_gromacs_VONAME: { required: true },
                see_gromacs_TOPBDII: { required: true },
                see_gromacs_WMS: { required: true },
                see_gromacs_MYPROXYSERVER: { required: true },
                see_gromacs_ETOKENSERVER: { required: true },                
                see_gromacs_PORT: { required: true },
                see_gromacs_ROBOTID: { required: true },
                see_gromacs_WEBDAV: { required: true },

                gisela_gromacs_INFRASTRUCTURE: { required: true },
                gisela_gromacs_VONAME: { required: true },
                gisela_gromacs_TOPBDII: { required: true },
                gisela_gromacs_WMS: { required: true },
                gisela_gromacs_MYPROXYSERVER: { required: true },
                gisela_gromacs_ETOKENSERVER: { required: true },                
                gisela_gromacs_PORT: { required: true },
                gisela_gromacs_ROBOTID: { required: true },
                gisela_gromacs_WEBDAV: { required: true },
                
                gromacs_APPID: { required: true },
                gromacs_LOGLEVEL: { required: true },
                gromacs_METADATA_HOST: { required: true },
                gromacs_OUTPUT_PATH: { required: true }
            },
            
            invalidHandler: function(form, validator) {
                var errors = validator.numberOfInvalids();
                if (errors) {
                    $("#error_message").empty();
                    var message = errors == 1
                    ? ' You missed 1 field. It has been highlighted'
                    : ' You missed ' + errors + ' fields. They have been highlighted';                    
                    $('#error_message').append("<img width='30' border='0'>"+message);
                    $("#error_message").show();
                } else $("#error_message").hide();                
            },
            
            submitHandler: function(form) {
                   form.submit();
            }
        });
        
        $("#GromacsEditForm").bind('submit', function () {            
            // Check if the GROMACS OPTIONS are NULL
            if ( !$('#lato_gromacs_RENEWAL').is(':checked') && 
                 !$('#lato_gromacs_DISABLEVOMS').is(':checked') 
             ) $('#lato_gromacs_OPTIONS').val('NULL');
                 
            if ( !$('#garuda_gromacs_RENEWAL').is(':checked') && 
                 !$('#garuda_gromacs_DISABLEVOMS').is(':checked') 
             ) $('#garuda_gromacs_OPTIONS').val('NULL');                 
                 
            if ( !$('#eumed_gromacs_RENEWAL').is(':checked') && 
                  !$('#eumed_gromacs_DISABLEVOMS').is(':checked') 
             ) $('#eumed_gromacs_OPTIONS').val('NULL');
                 
            if ( !$('#sagrid_gromacs_RENEWAL').is(':checked') && 
                  !$('#sagrid_gromacs_DISABLEVOMS').is(':checked') 
             ) $('#sagrid_gromacs_OPTIONS').val('NULL');
                 
            if ( !$('#see_gromacs_RENEWAL').is(':checked') && 
                  !$('#see_gromacs_DISABLEVOMS').is(':checked') 
             ) $('#see_gromacs_OPTIONS').val('NULL');
                 
            if ( !$('#gisela_gromacs_RENEWAL').is(':checked') && 
                  !$('#gisela_gromacs_DISABLEVOMS').is(':checked') 
             ) $('#gisela_gromacs_OPTIONS').val('NULL');                             
       });                
    });    
            
</script>

<br/>
<p style="width:690px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;">
    Please, select the Infrastructure(s) settings before to start</p>  

<!DOCTYPE html>
<form id="GromacsEditForm"
      name="GromacsEditForm"
      action="<portlet:actionURL><portlet:param name="ActionEvent" value="CONFIG_GROMACS_PORTLET"/></portlet:actionURL>" 
      method="POST">

<fieldset>
<legend>Portlet Settings</legend>
<div style="margin-left:15px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;" id="error_message"></div>
<br/>
<table id="results" border="0" width="620" style="width:690px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;">

<!-- LATO -->
<tr></tr>
<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%=renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the Infrastructure Acronym" />
   
        <label for="gromacs_ENABLEINFRASTRUCTURE">Enabled<em>*</em></label>
    </td>
    
    <td>        
        <c:forEach var="enabled" items="<%= gromacs_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='lato'}">
                <c:set var="results_lato" value="true"></c:set>
            </c:if>
        </c:forEach>
                
        <c:choose>
        <c:when test="${results_lato=='true'}">
        <input type="checkbox" 
               id="lato_gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="lato"
               checked="checked"/>
        </c:when>
        <c:otherwise>
        <input type="checkbox" 
               id="lato_gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="lato"/>
        </c:otherwise>
        </c:choose>
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%=renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The Infrastructure Acronym" />
   
        <label for="lato_gromacs_INFRASTRUCTURE">Infrastructure<em>*</em></label>
    </td>    
    <td>
        <input type="text" 
               id="lato_gromacs_INFRASTRUCTURE"
               name="lato_gromacs_INFRASTRUCTURE"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value="LATO" />        
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The login credential to access the LATO Infrastructure" />
   
        <label for="lato_gromacs_LOGIN">Login<em>*</em></label> 
    </td>
    <td>
        <input type="text" 
               id="lato_gromacs_LOGIN"
               name="lato_gromacs_LOGIN"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= lato_gromacs_LOGIN %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The password credential to access the LATO Infrastructure" />
   
        <label for="lato_gromacs_PASSWD">Password<em>*</em></label>
    </td>    
    <td>
        <input type="password" 
               id="lato_gromacs_PASSWD"
               name="lato_gromacs_PASSWD"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= lato_gromacs_PASSWD %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"         
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The Cluster server hostname to access the Infrastructure" />
   
        <label for="lato_gromacs_WMS">Cluster<em>*</em></label>
    </td>
    <td>          
        <c:forEach var="wms" items="<%= lato_gromacs_WMS %>">
            <c:if test="${(!empty wms && wms!='N/A')}">
            <div style="margin-bottom:4px;" class="cloned_cached_latoWMS">
            <input type="text"                
                   name="lato_gromacs_WMS"
                   class="textfield ui-widget ui-widget-content ui-state-focus required"
                   size="50px;"               
                   value=" <c:out value="${wms}"/>" />
            <img type="button" class="btnDel_lato2" width="18"
                 src="<%= renderRequest.getContextPath()%>/images/remove.png" 
                 border="0" title="Remove a WMS Endopoint" />
            </div>
            </c:if>
        </c:forEach>        
        
        <div style="margin-bottom:4px;" class="cloned_lato_gromacs_WMS">
        <input type="text"                
               name="lato_gromacs_WMS"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;"               
               value=" N/A"/>
        <img type="button" id="adding_WMS_lato" width="18"
             src="<%= renderRequest.getContextPath()%>/images/plus_orange.png" 
             border="0" title="Add a new WMS Endopoint" />
        <img type="button" class="btnDel_lato" width="18"
             src="<%= renderRequest.getContextPath()%>/images/remove.png" 
             border="0" title="Remove a WMS Endopoint" />
        </div>
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The MyProxyServer hostname for requesting long-term grid proxies" />
   
        <label for="lato_gromacs_MYPROXYSERVER">MyProxyServer</label>
    </td>
    <td>
        <input type="text" 
               id="lato_gromacs_MYPROXYSERVER"
               name="lato_gromacs_MYPROXYSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= lato_gromacs_MYPROXYSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer hostname to be contacted for requesting grid proxies" />
   
        <label for="lato_gromacs_ETOKENSERVER">eTokenServer</label>
    </td>
    <td>
        <input type="text" 
               id="lato_gromacs_ETOKENSERVER"
               name="lato_gromacs_ETOKENSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= lato_gromacs_ETOKENSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer port" />
   
        <label for="lato_gromacs_PORT">Port</label>
    </td>
    <td>
        <input type="text" 
               id="lato_gromacs_PORT"
               name="lato_gromacs_PORT"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="15px;" 
               value=" <%= lato_gromacs_PORT %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The certificate serial number to generate proxies" />
   
        <label for="lato_gromacs_ROBOTID">Serial Number</label>
    </td>
    <td>
        <input type="text" 
               id="lato_gromacs_ROBOTID"
               name="lato_gromacs_ROBOTID"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= lato_gromacs_ROBOTID %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WEBDAV Server for the Infrastructure" />
   
        <label for="lato_gromacs_WEBDAV">WebDAV<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="lato_gromacs_WEBDAV"
               name="lato_gromacs_WEBDAV"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= lato_gromacs_WEBDAV %>" />
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The FQANs for the grid proxy (if any)" />
   
        <label for="lato_gromacs_ROLE">Role</label>
    </td>
    <td>
        <input type="text" 
               id="lato_gromacs_ROLE"
               name="lato_gromacs_ROLE"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= lato_gromacs_ROLE %>" />            
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the creation of a long-term proxy to a MyProxy Server" />
   
        <label for="lato_gromacs_RENEWAL">Proxy Renewal</label>
    </td>
    <td>
        <input type="checkbox" 
               id="lato_gromacs_RENEWAL"
               name="lato_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               <%= lato_gromacs_RENEWAL %> 
               value="enableRENEWAL" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Disable the creation of a VOMS proxy" />
   
        <label for="lato_gromacs_DISABLEVOMS">Disable VOMS</label>
    </td>
    <td>
        <input type="checkbox" 
               id="lato_gromacs_DISABLEVOMS"
               name="lato_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               <%= lato_gromacs_DISABLEVOMS %>
               size="50px;" 
               value="disableVOMS" />
    </td>    
</tr>
    
<!-- GARUDA -->  
<tr></tr>
<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%=renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the Infrastructure Acronym" />
   
        <label for="gromacs_ENABLEINFRASTRUCTURE">Enabled<em>*</em></label>
    </td>    
    <td>
        
        <c:forEach var="enabled" items="<%= gromacs_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='garuda'}">
                <c:set var="results_garuda" value="true"></c:set>
            </c:if>
        </c:forEach>
        
        <c:choose>
        <c:when test="${results_garuda=='true'}">
        <input type="checkbox" 
               id="gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="garuda"               
               checked="checked"/>
        </c:when>
        <c:otherwise>
        <input type="checkbox" 
               id="gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="garuda"/>
        </c:otherwise>
        </c:choose>
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%=renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The Infrastructure Acronym" />
   
        <label for="garuda_gromacs_INFRASTRUCTURE">Infrastructure<em>*</em></label>
    </td>    
    <td>
        <input type="text" 
               id="garuda_gromacs_INFRASTRUCTURE"
               name="garuda_gromacs_INFRASTRUCTURE"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value="GARUDA" />        
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The VO name" />
   
        <label for="garuda_gromacs_VONAME">VOname<em>*</em></label> 
    </td>
    <td>
        <input type="text" 
               id="garuda_gromacs_VONAME"
               name="garuda_gromacs_VONAME"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= garuda_gromacs_VONAME %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The TopBDII hostname for accessing the Infrastructure" />
   
        <label for="garuda_gromacs_TOPBDII">TopBDII</label>
    </td>    
    <td>
        <input type="text" 
               id="garuda_gromacs_TOPBDII"
               name="garuda_gromacs_TOPBDII"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= garuda_gromacs_TOPBDII %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"         
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WMProxy hostname for accessing the Infrastructure" />
   
        <label for="garuda_gromacs_WMS">WMS Endpoint<em>*</em></label>
    </td>
    <td>          
        <c:forEach var="wms" items="<%= garuda_gromacs_WMS %>">
            <c:if test="${(!empty wms && wms!='N/A')}">
            <div style="margin-bottom:4px;" class="cloned_cached_garudaWMS">
            <input type="text"                
                   name="garuda_gromacs_WMS"
                   class="textfield ui-widget ui-widget-content ui-state-focus required"
                   size="50px;"               
                   value=" <c:out value="${wms}"/>" />
            <img type="button" class="btnDel_garuda2" width="18"
                 src="<%= renderRequest.getContextPath()%>/images/remove.png" 
                 border="0" title="Remove a WMS Endopoint" />
            </div>
            </c:if>
        </c:forEach>        
        
        <div style="margin-bottom:4px;" class="cloned_garuda_gromacs_WMS">
        <input type="text"                
               name="garuda_gromacs_WMS"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;"               
               value=" N/A"/>
        <img type="button" id="adding_WMS_garuda" width="18"
             src="<%= renderRequest.getContextPath()%>/images/plus_orange.png" 
             border="0" title="Add a new WMS Endopoint" />
        <img type="button" class="btnDel_garuda" width="18"
             src="<%= renderRequest.getContextPath()%>/images/remove.png" 
             border="0" title="Remove a WMS Endopoint" />
        </div>
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The MyProxyServer hostname for requesting long-term grid proxies" />
   
        <label for="garuda_gromacs_MYPROXYSERVER">MyProxyServer<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="garuda_gromacs_MYPROXYSERVER"
               name="garuda_gromacs_MYPROXYSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= garuda_gromacs_MYPROXYSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer hostname to be contacted for requesting grid proxies" />
   
        <label for="garuda_gromacs_ETOKENSERVER">eTokenServer<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="garuda_gromacs_ETOKENSERVER"
               name="garuda_gromacs_ETOKENSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= garuda_gromacs_ETOKENSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer port" />
   
        <label for="garuda_gromacs_PORT">Port<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="garuda_gromacs_PORT"
               name="garuda_gromacs_PORT"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= garuda_gromacs_PORT %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The certificate serial number to generate proxies" />
   
        <label for="garuda_gromacs_ROBOTID">Serial Number<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="garuda_gromacs_ROBOTID"
               name="garuda_gromacs_ROBOTID"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= garuda_gromacs_ROBOTID %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WEBDAV Server for the Infrastructure" />
   
        <label for="garuda_gromacs_WEBDAV">WebDAV<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="garuda_gromacs_WEBDAV"
               name="garuda_gromacs_WEBDAV"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= garuda_gromacs_WEBDAV %>" />
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The FQANs for the grid proxy (if any)" />
   
        <label for="garuda_gromacs_ROLE">Role</label>
    </td>
    <td>
        <input type="text" 
               id="garuda_gromacs_ROLE"
               name="garuda_gromacs_ROLE"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= garuda_gromacs_ROLE %>" />            
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the creation of a long-term proxy to a MyProxy Server" />
   
        <label for="garuda_gromacs_RENEWAL">Proxy Renewal</label>
    </td>
    <td>
        <input type="checkbox" 
               id="garuda_gromacs_RENEWAL"
               name="garuda_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               <%= garuda_gromacs_RENEWAL %> 
               value="enableRENEWAL" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Disable the creation of a VOMS proxy" />
   
        <label for="garuda_gromacs_DISABLEVOMS">Disable VOMS</label>
    </td>
    <td>
        <input type="checkbox" 
               id="garuda_gromacs_DISABLEVOMS"
               name="garuda_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               <%= garuda_gromacs_DISABLEVOMS %>
               size="50px;" 
               value="disableVOMS" />
    </td>    
</tr>

<!-- EUMED -->
<tr></tr>
<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the Infrastructure Acronym" />
   
        <label for="gromacs_ENABLEINFRASTRUCTURE">Enabled<em>*</em></label>
    </td>
    
    <td>        
        <c:forEach var="enabled" items="<%= gromacs_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='eumed'}">
                <c:set var="results_eumed" value="true"></c:set>
            </c:if>
        </c:forEach>
                
        <c:choose>
        <c:when test="${results_eumed=='true'}">
        <input type="checkbox" 
               id="gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="eumed"               
               checked="checked"/>
        </c:when>
        <c:otherwise>
        <input type="checkbox" 
               id="gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="eumed"/>
        </c:otherwise>
        </c:choose>
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The Infrastructure Acronym" />
   
        <label for="EUMED_gromacs_INFRASTRUCTURE">Infrastructure<em>*</em></label>
    </td>    
    <td>
        <input type="text" 
               id="eumed_gromacs_INFRASTRUCTURE"
               name="eumed_gromacs_INFRASTRUCTURE"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value="EUMED" />        
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The VO name" />
   
        <label for="eumed_gromacs_VONAME">VOname<em>*</em></label> 
    </td>
    <td>
        <input type="text" 
               id="eumed_gromacs_VONAME"
               name="eumed_gromacs_VONAME"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= eumed_gromacs_VONAME %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The TopBDII hostname for accessing the Infrastructure" />
   
        <label for="eumed_gromacs_TOPBDII">TopBDII<em>*</em></label>
    </td>    
    <td>
        <input type="text" 
               id="eumed_gromacs_TOPBDII"
               name="eumed_gromacs_TOPBDII"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= eumed_gromacs_TOPBDII %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WMProxy hostname for accessing the Infrastructure" />
   
        <label for="eumed_gromacs_WMS">WMS Endpoint<em>*</em></label>
    </td>
    <td>
        <c:forEach var="wms" items="<%= eumed_gromacs_WMS %>">
            <c:if test="${(!empty wms && wms!='N/A')}">
            <div style="margin-bottom:4px;" class="cloned_cached_eumedWMS">
            <input type="text"                
                   name="eumed_gromacs_WMS"
                   id="eumed_gromacs_WMS"
                   class="textfield ui-widget ui-widget-content ui-state-focus required"
                   size="50px;"               
                   value=" <c:out value="${wms}"/>" />
            <img type="button" class="btnDel_eumed2" width="18"
                 src="<%= renderRequest.getContextPath()%>/images/remove.png" 
                 border="0" title="Remove a WMS Endopoint" />
            </div>
            </c:if>
        </c:forEach>        
        
        <div style="margin-bottom:4px;" class="cloned_eumed_gromacs_WMS">
        <input type="text" 
               id="eumed_gromacs_WMS"
               name="eumed_gromacs_WMS"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;"               
               value=" N/A"/>
        <img type="button" id="adding_WMS_eumed" width="18"
             src="<%= renderRequest.getContextPath()%>/images/plus_orange.png" 
             border="0" title="Add a new WMS Endopoint" />
        <img type="button" class="btnDel_eumed" width="18"
             src="<%= renderRequest.getContextPath()%>/images/remove.png" 
             border="0" title="Remove a WMS Endopoint" />
        </div>                     
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The MyProxyServer hostname for requesting long-term grid proxies" />
   
        <label for="eumed_gromacs_MYPROXYSERVER">MyProxyServer<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="eumed_gromacs_MYPROXYSERVER"
               name="eumed_gromacs_MYPROXYSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= eumed_gromacs_MYPROXYSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer hostname to be contacted for requesting grid proxies" />
   
        <label for="eumed_gromacs_ETOKENSERVER">eTokenServer<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="eumed_gromacs_ETOKENSERVER"
               name="eumed_gromacs_ETOKENSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= eumed_gromacs_ETOKENSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer port" />
   
        <label for="eumed_gromacs_PORT">Port<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="eumed_gromacs_PORT"
               name="eumed_gromacs_PORT"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= eumed_gromacs_PORT %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The certificate serial number to generate proxies" />
   
        <label for="eumed_gromacs_ROBOTID">Serial Number<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="eumed_gromacs_ROBOTID"
               name="eumed_gromacs_ROBOTID"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= eumed_gromacs_ROBOTID %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WEBDAV Server for the Infrastructure" />
   
        <label for="eumed_gromacs_WEBDAV">WebDAV<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="eumed_gromacs_WEBDAV"
               name="eumed_gromacs_WEBDAV"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= eumed_gromacs_WEBDAV %>" />
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The FQANs for the grid proxy (if any)" />
   
        <label for="eumed_gromacs_ROLE">Role</label>
    </td>
    <td>
        <input type="text" 
               id="eumed_gromacs_ROLE"
               name="eumed_gromacs_ROLE"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= eumed_gromacs_ROLE %>" />            
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the creation of a long-term proxy to a MyProxy Server" />
   
        <label for="eumed_gromacs_RENEWAL">Proxy Renewal</label>
    </td>
    <td>
        <input type="checkbox" 
               id="eumed_gromacs_RENEWAL"
               name="eumed_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               <%= eumed_gromacs_RENEWAL %> 
               value="enableRENEWAL" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Disable the creation of a VOMS proxy" />
   
        <label for="eumed_gromacs_DISABLEVOMS">Disable VOMS</label>
    </td>
    <td>
        <input type="checkbox" 
               id="eumed_gromacs_DISABLEVOMS"
               name="eumed_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               <%= eumed_gromacs_DISABLEVOMS %>
               size="50px;" 
               value="disableVOMS" />
    </td>    
</tr>

<!-- SAGRID -->
<tr></tr>
<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the Infrastructure Acroym" />
   
        <label for="gromacs_ENABLEINFRASTRUCTURE">Enabled<em>*</em></label>
    </td>
    
    <td>
        <c:forEach var="enabled" items="<%= gromacs_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='sagrid'}">
                <c:set var="results_sagrid" value="true"></c:set>
            </c:if>
        </c:forEach>
        
        <c:choose>
        <c:when test="${results_sagrid=='true'}">
        <input type="checkbox" 
               id="gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="sagrid"               
               checked="checked"/>
        </c:when>
        <c:otherwise>
        <input type="checkbox" 
               id="gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="sagrid"/>
        </c:otherwise>
        </c:choose>
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The Infrastructure Acronym" />
   
        <label for="SAGRID_gromacs_INFRASTRUCTURE">Infrastructure<em>*</em></label>
    </td>    
    <td>
        <input type="text" 
               id="sagrid_gromacs_INFRASTRUCTURE"
               name="sagrid_gromacs_INFRASTRUCTURE"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value="SAGRID" />
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The VO name" />
   
        <label for="sagrid_gromacs_VONAME">VOname<em>*</em></label> 
    </td>
    <td>
        <input type="text" 
               id="sagrid_gromacs_VONAME"
               name="sagrid_gromacs_VONAME"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= sagrid_gromacs_VONAME %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The TopBDII hostname for accessing the Infrastructure" />
   
        <label for="sagrid_gromacs_TOPBDII">TopBDII<em>*</em></label>
    </td>    
    <td>
        <input type="text" 
               id="sagrid_gromacs_TOPBDII"
               name="sagrid_gromacs_TOPBDII"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= sagrid_gromacs_TOPBDII %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WMProxy hostname for accessing the Infrastructure" />
   
        <label for="sagrid_gromacs_WMS">WMS Endpoint<em>*</em></label>
    </td>
    <td>
        
        <c:forEach var="wms" items="<%= sagrid_gromacs_WMS %>">
            <c:if test="${(!empty wms && wms!='N/A')}">
            <div style="margin-bottom:4px;" class="cloned_cached_sagridWMS">
            <input type="text"                
                   name="sagrid_gromacs_WMS"
                   id="sagrid_gromacs_WMS"
                   class="textfield ui-widget ui-widget-content ui-state-focus required"
                   size="50px;"               
                   value=" <c:out value="${wms}"/>" />
            <img type="button" class="btnDel_sagrid2" width="18"
                 src="<%= renderRequest.getContextPath()%>/images/remove.png" 
                 border="0" title="Remove a WMS Endopoint" />
            </div>
            </c:if>
        </c:forEach>
        
        <div style="margin-bottom:4px;" class="cloned_sagrid_gromacs_WMS">
        <input type="text" 
               id="sagrid_gromacs_WMS"
               name="sagrid_gromacs_WMS"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;"               
               value=" N/A"/>
        <img type="button" id="adding_WMS_sagrid" width="18"
             src="<%= renderRequest.getContextPath()%>/images/plus_orange.png" 
             border="0" title="Add a new WMS Endopoint" />
        <img type="button" class="btnDel_sagrid" width="18"
             src="<%= renderRequest.getContextPath()%>/images/remove.png" 
             border="0" title="Remove a WMS Endopoint" />
        </div>                     
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The MyProxyServer hostname for requesting long-term grid proxies" />
   
        <label for="sagrid_gromacs_MYPROXYSERVER">MyProxyServer<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="sagrid_gromacs_MYPROXYSERVER"
               name="sagrid_gromacs_MYPROXYSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= sagrid_gromacs_MYPROXYSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer hostname to be contacted for requesting grid proxies" />
   
        <label for="sagrid_gromacs_ETOKENSERVER">eTokenServer<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="sagrid_gromacs_ETOKENSERVER"
               name="sagrid_gromacs_ETOKENSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= sagrid_gromacs_ETOKENSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer port" />
   
        <label for="sagrid_gromacs_PORT">Port<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="sagrid_gromacs_PORT"
               name="sagrid_gromacs_PORT"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= sagrid_gromacs_PORT %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The certificate serial number to generate proxies" />
   
        <label for="sagrid_gromacs_ROBOTID">Serial Number<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="sagrid_gromacs_ROBOTID"
               name="sagrid_gromacs_ROBOTID"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= sagrid_gromacs_ROBOTID %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WEBDAV Server for the Infrastructure" />
   
        <label for="sagrid_gromacs_WEBDAV">WebDAV<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="sagrid_gromacs_WEBDAV"
               name="sagrid_gromacs_WEBDAV"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= sagrid_gromacs_WEBDAV %>" />
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The FQANs for the grid proxy (if any)" />
   
        <label for="sagrid_gromacs_ROLE">Role</label>
    </td>
    <td>
        <input type="text" 
               id="sagrid_gromacs_ROLE"
               name="sagrid_gromacs_ROLE"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= sagrid_gromacs_ROLE %>" />            
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the creation of a long-term proxy to a MyProxy Server" />
   
        <label for="sagrid_gromacs_RENEWAL">Proxy Renewal</label>
    </td>
    <td>
        <input type="checkbox" 
               id="sagrid_gromacs_RENEWAL"
               name="sagrid_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               <%= sagrid_gromacs_RENEWAL %> 
               value="enableRENEWAL" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Disable the creation of a VOMS proxy" />
   
        <label for="sagrid_gromacs_DISABLEVOMS">Disable VOMS</label>
    </td>
    <td>
        <input type="checkbox" 
               id="sagrid_gromacs_DISABLEVOMS"
               name="sagrid_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               <%= sagrid_gromacs_DISABLEVOMS %>
               size="50px;" 
               value="disableVOMS" />
    </td>    
</tr>

<!-- SEE -->
<tr></tr>
<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the Infrastructure Acroym" />
   
        <label for="gromacs_ENABLEINFRASTRUCTURE">Enabled<em>*</em></label>
    </td>
    
    <td>
        <c:forEach var="enabled" items="<%= gromacs_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='see'}">
                <c:set var="results_see" value="true"></c:set>
            </c:if>
        </c:forEach>
        
        <c:choose>
        <c:when test="${results_see=='true'}">
        <input type="checkbox" 
               id="gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="see"               
               checked="checked"/>
        </c:when>
        <c:otherwise>
        <input type="checkbox" 
               id="gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="see"/>
        </c:otherwise>
        </c:choose>
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The Infrastructure Acronym" />
   
        <label for="SEE_gromacs_INFRASTRUCTURE">Infrastructure<em>*</em></label>
    </td>    
    <td>
        <input type="text" 
               id="see_gromacs_INFRASTRUCTURE"
               name="see_gromacs_INFRASTRUCTURE"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value="SEE" />
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The VO name" />
   
        <label for="see_gromacs_VONAME">VOname<em>*</em></label> 
    </td>
    <td>
        <input type="text" 
               id="see_gromacs_VONAME"
               name="see_gromacs_VONAME"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= see_gromacs_VONAME %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The TopBDII hostname for accessing the Infrastructure" />
   
        <label for="see_gromacs_TOPBDII">TopBDII<em>*</em></label>
    </td>    
    <td>
        <input type="text" 
               id="see_gromacs_TOPBDII"
               name="see_gromacs_TOPBDII"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= see_gromacs_TOPBDII %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WMProxy hostname for accessing the Infrastructure" />
   
        <label for="see_gromacs_WMS">WMS Endpoint<em>*</em></label>
    </td>
    <td>
        
        <c:forEach var="wms" items="<%= see_gromacs_WMS %>">
            <c:if test="${(!empty wms && wms!='N/A')}">
            <div style="margin-bottom:4px;" class="cloned_cached_seeWMS">
            <input type="text"                
                   name="see_gromacs_WMS"
                   id="see_gromacs_WMS"
                   class="textfield ui-widget ui-widget-content ui-state-focus required"
                   size="50px;"               
                   value=" <c:out value="${wms}"/>" />
            <img type="button" class="btnDel_see2" width="18"
                 src="<%= renderRequest.getContextPath()%>/images/remove.png" 
                 border="0" title="Remove a WMS Endopoint" />
            </div>
            </c:if>
        </c:forEach>
        
        <div style="margin-bottom:4px;" class="cloned_see_gromacs_WMS">
        <input type="text" 
               id="see_gromacs_WMS"
               name="see_gromacs_WMS"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;"               
               value=" N/A"/>
        <img type="button" id="adding_WMS_see" width="18"
             src="<%= renderRequest.getContextPath()%>/images/plus_orange.png" 
             border="0" title="Add a new WMS Endopoint" />
        <img type="button" class="btnDel_see" width="18"
             src="<%= renderRequest.getContextPath()%>/images/remove.png" 
             border="0" title="Remove a WMS Endopoint" />
        </div>                     
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The MyProxyServer hostname for requesting long-term grid proxies" />
   
        <label for="see_gromacs_MYPROXYSERVER">MyProxyServer<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="see_gromacs_MYPROXYSERVER"
               name="see_gromacs_MYPROXYSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= see_gromacs_MYPROXYSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer hostname to be contacted for requesting grid proxies" />
   
        <label for="see_gromacs_ETOKENSERVER">eTokenServer<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="see_gromacs_ETOKENSERVER"
               name="see_gromacs_ETOKENSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= see_gromacs_ETOKENSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer port" />
   
        <label for="see_gromacs_PORT">Port<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="see_gromacs_PORT"
               name="see_gromacs_PORT"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= see_gromacs_PORT %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The certificate serial number to generate proxies" />
   
        <label for="see_gromacs_ROBOTID">Serial Number<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="see_gromacs_ROBOTID"
               name="see_gromacs_ROBOTID"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= see_gromacs_ROBOTID %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WEBDAV Server for the Infrastructure" />
   
        <label for="see_gromacs_WEBDAV">WebDAV<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="see_gromacs_WEBDAV"
               name="see_gromacs_WEBDAV"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= see_gromacs_WEBDAV %>" />
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The FQANs for the grid proxy (if any)" />
   
        <label for="see_gromacs_ROLE">Role</label>
    </td>
    <td>
        <input type="text" 
               id="see_gromacs_ROLE"
               name="see_gromacs_ROLE"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= see_gromacs_ROLE %>" />            
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the creation of a long-term proxy to a MyProxy Server" />
   
        <label for="see_gromacs_RENEWAL">Proxy Renewal</label>
    </td>
    <td>
        <input type="checkbox" 
               id="see_gromacs_RENEWAL"
               name="see_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               <%= see_gromacs_RENEWAL %> 
               value="enableRENEWAL" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Disable the creation of a VOMS proxy" />
   
        <label for="see_gromacs_DISABLEVOMS">Disable VOMS</label>
    </td>
    <td>
        <input type="checkbox" 
               id="see_gromacs_DISABLEVOMS"
               name="see_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               <%= see_gromacs_DISABLEVOMS %>
               size="50px;" 
               value="disableVOMS" />
    </td>    
</tr>

<!-- GISELA -->
<tr></tr>
<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the Infrastructure Acroym" />
   
        <label for="gromacs_ENABLEINFRASTRUCTURE">Enabled<em>*</em></label>
    </td>
    
    <td>
        <c:forEach var="enabled" items="<%= gromacs_ENABLEINFRASTRUCTURE %>">
            <c:if test="${enabled=='gisela'}">
                <c:set var="results_gisela" value="true"></c:set>
            </c:if>
        </c:forEach>
        
        <c:choose>
        <c:when test="${results_gisela=='true'}">
        <input type="checkbox" 
               id="gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="gisela"               
               checked="checked"/>
        </c:when>
        <c:otherwise>
        <input type="checkbox" 
               id="gromacs_ENABLEINFRASTRUCTURE"
               name="gromacs_ENABLEINFRASTRUCTURES"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="48px;"
               value="gisela"/>
        </c:otherwise>
        </c:choose>
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The Infrastructure Acronym" />
   
        <label for="GISELA_gromacs_INFRASTRUCTURE">Infrastructure<em>*</em></label>
    </td>    
    <td>
        <input type="text" 
               id="gisela_gromacs_INFRASTRUCTURE"
               name="gisela_gromacs_INFRASTRUCTURE"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value="GISELA" />
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The VO name" />
   
        <label for="gisela_gromacs_VONAME">VOname<em>*</em></label> 
    </td>
    <td>
        <input type="text" 
               id="gisela_gromacs_VONAME"
               name="gisela_gromacs_VONAME"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= gisela_gromacs_VONAME %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The TopBDII hostname for accessing the Infrastructure" />
   
        <label for="gisela_gromacs_TOPBDII">TopBDII<em>*</em></label>
    </td>    
    <td>
        <input type="text" 
               id="gisela_gromacs_TOPBDII"
               name="gisela_gromacs_TOPBDII"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= gisela_gromacs_TOPBDII %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WMProxy hostname for accessing the Infrastructure" />
   
        <label for="gisela_gromacs_WMS">WMS Endpoint<em>*</em></label>
    </td>
    <td>
        
        <c:forEach var="wms" items="<%= gisela_gromacs_WMS %>">
            <c:if test="${(!empty wms && wms!='N/A')}">
            <div style="margin-bottom:4px;" class="cloned_cached_giselaWMS">
            <input type="text"                
                   name="gisela_gromacs_WMS"
                   id="gisela_gromacs_WMS"
                   class="textfield ui-widget ui-widget-content ui-state-focus required"
                   size="50px;"               
                   value=" <c:out value="${wms}"/>" />
            <img type="button" class="btnDel_gisela2" width="18"
                 src="<%= renderRequest.getContextPath()%>/images/remove.png" 
                 border="0" title="Remove a WMS Endopoint" />
            </div>
            </c:if>
        </c:forEach>
        
        <div style="margin-bottom:4px;" class="cloned_gisela_gromacs_WMS">
        <input type="text" 
               id="gisela_gromacs_WMS"
               name="gisela_gromacs_WMS"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;"               
               value=" N/A"/>
        <img type="button" id="adding_WMS_gisela" width="18"
             src="<%= renderRequest.getContextPath()%>/images/plus_orange.png" 
             border="0" title="Add a new WMS Endopoint" />
        <img type="button" class="btnDel_gisela" width="18"
             src="<%= renderRequest.getContextPath()%>/images/remove.png" 
             border="0" title="Remove a WMS Endopoint" />
        </div>                     
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The MyProxyServer hostname for requesting long-term grid proxies" />
   
        <label for="gisela_gromacs_MYPROXYSERVER">MyProxyServer<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="gisela_gromacs_MYPROXYSERVER"
               name="gisela_gromacs_MYPROXYSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= gisela_gromacs_MYPROXYSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer hostname to be contacted for requesting grid proxies" />
   
        <label for="gisela_gromacs_ETOKENSERVER">eTokenServer<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="gisela_gromacs_ETOKENSERVER"
               name="gisela_gromacs_ETOKENSERVER"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= gisela_gromacs_ETOKENSERVER %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The eTokenServer port" />
   
        <label for="gisela_gromacs_PORT">Port<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="gisela_gromacs_PORT"
               name="gisela_gromacs_PORT"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= gisela_gromacs_PORT %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The certificate serial number to generate proxies" />
   
        <label for="gisela_gromacs_ROBOTID">Serial Number<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="gisela_gromacs_ROBOTID"
               name="gisela_gromacs_ROBOTID"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= gisela_gromacs_ROBOTID %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The WEBDAV Server for the Infrastructure" />
   
        <label for="gisela_gromacs_WEBDAV">WebDAV<em>*</em></label>
    </td>
    <td>
        <input type="text" 
               id="gisela_gromacs_WEBDAV"
               name="gisela_gromacs_WEBDAV"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= gisela_gromacs_WEBDAV %>" />
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The FQANs for the grid proxy (if any)" />
   
        <label for="gisela_gromacs_ROLE">Role</label>
    </td>
    <td>
        <input type="text" 
               id="gisela_gromacs_ROLE"
               name="gisela_gromacs_ROLE"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= gisela_gromacs_ROLE %>" />            
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Enable the creation of a long-term proxy to a MyProxy Server" />
   
        <label for="gisela_gromacs_RENEWAL">Proxy Renewal</label>
    </td>
    <td>
        <input type="checkbox" 
               id="gisela_gromacs_RENEWAL"
               name="gisela_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               <%= gisela_gromacs_RENEWAL %> 
               value="enableRENEWAL" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="Disable the creation of a VOMS proxy" />
   
        <label for="gisela_gromacs_DISABLEVOMS">Disable VOMS</label>
    </td>
    <td>
        <input type="checkbox" 
               id="gisela_gromacs_DISABLEVOMS"
               name="gisela_gromacs_OPTIONS"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               <%= gisela_gromacs_DISABLEVOMS %>
               size="50px;" 
               value="disableVOMS" />
    </td>    
</tr>

<!-- LAST -->
<tr></tr>
<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The ApplicationID for GROMACS" />
   
        <label for="gromacs_APPID">AppID<em>*</em></label> 
    </td>
    <td>
        <input type="text" 
               id="gromacs_APPID"
               name="gromacs_APPID"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= gromacs_APPID %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The Log Level of the portlet (E.g.: VERBOSE, INFO)" />
   
        <label for="gromacs_LOGLEVEL">Log Level<em>*</em></label> 
    </td>
    <td>
        <input type="text" 
               id="gromacs_LOGLEVEL"
               name="gromacs_LOGLEVEL"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= gromacs_LOGLEVEL %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The Metadata hostname" />
   
        <label for="gromacs_METADATA_HOST">Metadata Host<em>*</em></label> 
    </td>
    <td>
        <input type="text" 
               id="gromacs_METADATA_HOST"
               name="gromacs_METADATA_HOST"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="15px;" 
               value=" <%= gromacs_METADATA_HOST %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The output path of the server's file-system where download results" />
   
        <label for="gromacs_OUTPUT_PATH">Output Path<em>*</em></label> 
    </td>
    <td>
        <input type="text" 
               id="gromacs_OUTPUT_PATH"
               name="gromacs_OUTPUT_PATH"
               class="textfield ui-widget ui-widget-content ui-state-focus required"
               size="50px;" 
               value=" <%= gromacs_OUTPUT_PATH %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The list of application software TAG (separated by ',')" />
   
        <label for="gromacs_SOFTWARE">SoftwareTAG</label>
    </td>
    <td>
        <input type="text" 
               id="gromacs_SOFTWARE"
               name="gromacs_SOFTWARE"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= gromacs_SOFTWARE %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The local proxy file path" />
   
        <label for="gromacs_LOCAL_PROXY">Proxy Path</label>
    </td>
    <td>
        <input type="text" 
               id="gromacs_LOCAL_PROXY"
               name="gromacs_LOCAL_PROXY"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= gromacs_LOCAL_PROXY %>" />    
    </td>
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The Tracking DB Server Hostname" />
   
        <label for="TRACKING_DB_HOSTNAME">HostName</label>
    </td>
    <td>
        <input type="text" 
               id="TRACKING_DB_HOSTNAME"
               name="TRACKING_DB_HOSTNAME"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= TRACKING_DB_HOSTNAME %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The username credential for login the Tracking DB" />
   
        <label for="TRACKING_DB_USERNAME">UserName</label>
    </td>
    <td>
        <input type="text" 
               id="TRACKING_DB_USERNAME"
               name="TRACKING_DB_USERNAME"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= TRACKING_DB_USERNAME %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The password credential for login  the Tracking DB" />
   
        <label for="TRACKING_DB_PASSWORD">Password</label>
    </td>
    <td>
        <input type="password" 
               id="TRACKING_DB_PASSWORD"
               name="TRACKING_DB_PASSWORD"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= TRACKING_DB_PASSWORD %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The SMTP Server for sending notification" />
   
        <label for="SMTP_HOST">SMTP</label>
    </td>
    <td>
        <input type="text" 
               id="SMTP_HOST"
               name="SMTP_HOST"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= SMTP_HOST %>" />    
    </td>    
</tr>

<tr>    
    <td width="150">
    <img width="30" 
         align="absmiddle"
         src="<%= renderRequest.getContextPath()%>/images/question.png"  
         border="0" title="The email address for sending notification" />
   
        <label for="Sender">Sender</label>
    </td>
    <td>
        <input type="text" 
               id="SENDER_MAIL"
               name="SENDER_MAIL"
               class="textfield ui-widget ui-widget-content ui-state-focus"
               size="50px;" 
               value=" <%= SENDER_MAIL %>" />
    </td>    
</tr>

<!-- Buttons -->
<tr>            
    <tr><td>&nbsp;</td></tr>
    <td align="left">    
    <input type="image" src="<%= renderRequest.getContextPath()%>/images/save.png"
           width="50"
           name="Submit" title="Save the portlet settings" />        
    </td>
</tr>  

</table>
<br/>
<div id="pageNavPosition" style="width:690px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;">   
</div>
</fieldset>
           
<script type="text/javascript">
    var pager = new Pager('results', 14); 
    pager.init(); 
    pager.showPageNav('pager', 'pageNavPosition'); 
    pager.showPage(1);
</script>
</form>
