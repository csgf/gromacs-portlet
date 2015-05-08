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
<%@ page import="javax.portlet.*"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<portlet:defineObjects/>

<script type="text/javascript">
    $(document).ready(function() {
              
    $('.slideshow').cycle({
	fx: 'fade' // choose your transition type (fade, scrollUp, shuffle, etc)
    });
    
    // Roller
    $('#gromacs_footer').rollchildren({
        delay_time         : 3000,
        loop               : true,
        pause_on_mouseover : true,
        roll_up_old_item   : true,
        speed              : 'slow'   
    });
    
    //var $tumblelog = $('#tumblelog');  
    $('#tumblelog').imagesLoaded( function() {
      $tumblelog.masonry({
        columnWidth: 440
      });
    });
});
</script>
                    
<br/>

<fieldset>
<legend>About GROMACS</legend>

<section id="content">

<div id="tumblelog" class="clearfix">
    
  <div class="story col3">
  <a href="http://www.gromacs.org">
  <img width="250" src="<%=renderRequest.getContextPath()%>/images/GROMACS_logo.png"/></a><br/>
  <p style="text-align:justify; position: relative;">
  A versatile package to perform molecular dynamics, i.e. simulate the Newtonian equations of motion 
  for systems with hundreds to millions of particles.<br/>
  It is primarily designed for biochemical molecules like proteins, lipids and nucleic acids that have a lot of 
  complicated bonded interactions, but since GROMACS is extremely fast at calculating the non-bonded interactions 
  (that usually dominate simulations) many groups are also using it for research on non-biological systems, e.g. 
  polymers.<br/><br/>
  <img width="250" src="<%=renderRequest.getContextPath()%>/images/0.png"/>
  <img width="250" src="<%=renderRequest.getContextPath()%>/images/1.png"/>  
  </p>      
  </div>
                                     
  <div class="story col3" style="font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;">
      <h2>
      <a href="mailto:info@sg-licence@ct.infn.it">
      <img width="100" 
           src="<%= renderRequest.getContextPath()%>/images/contact6.jpg" 
           title="Get in touch with the project"/></a>Contacts
      </h2>
      <p style="text-align:justify;">Giuseppe LA ROCCA (INFN)<i> &mdash; (Responsible for deployment)</i></p>
      <p style="text-align:justify;">Riccardo ROTONDO (INFN)</p>
      <p style="text-align:justify;">Mario TORRISI (UniCT)</p>
  </div>               
    
  <div class="story col3" style="font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 13px;">
        <h2>Sponsors & Credits</h2>
        <table border="0">                        
            <tr>                
            <td>
            <p align="justify">
            <a href="http://www.ct.infn.it/">
                <img align="center" width="150"                      
                     src="<%= renderRequest.getContextPath()%>/images/Infn_Logo.jpg" 
                     border="0" title="The Italian National Institute of Nuclear Physics (INFN)" />
            </a>
            </p>
            </td>
            
            <td>&nbsp;&nbsp;</td>
            
            <td>
            <p align="justify">
            <a href="https://www.chain-project.eu/">
                <img align="center" width="150"                      
                     src="<%= renderRequest.getContextPath()%>/images/chain-logo-220x124.png" 
                     border="0" title="The CHAIN-REDS Project Home Page" />
            </a>
            </p>
            </td>
            
            <td>&nbsp;&nbsp;</td>
            <td>&nbsp;&nbsp;</td>
            
            <td>
            <p align="justify">
            <a href="http://www.gromacs.org/">
                <img align="center" width="250"                      
                     src="<%= renderRequest.getContextPath()%>/images/GROMACS_logo.png" 
                     border="0" title="The GROMACS Official Home Page" />
            </a>
            </p>
            </td>
                        
            </tr>                                  
        </table>   
  </div>
</div>
</section>
</fieldset>           
                     
<div id="gromacs_footer" style="width:690px; font-family: Tahoma,Verdana,sans-serif,Arial; font-size: 14px;">
    <div>GROMACS portlet v2.0.8</div>
    <div>The Italian National Institute of Nuclear Physics (INFN), division of Catania, Italy</div>
    <div>Copyright © 2014 - 2015. All rights reserved</div>    
    <div>This work has been partially supported by
    <a href="http://www.chain-project.eu/">
    <img width="45" 
         border="0"
         src="<%= renderRequest.getContextPath()%>/images/chain-logo-220x124.png" 
         title="The CHAIN-REDS EU FP7 Project"/>
    </a>
    </div>
</div>