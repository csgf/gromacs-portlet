/*
*************************************************************************
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
***************************************************************************
*/
package it.infn.ct.gromacs;

// import liferay libraries
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;

// import DataEngine libraries
import com.liferay.portal.util.PortalUtil;
import it.infn.ct.GridEngine.InformationSystem.BDII;
import it.infn.ct.GridEngine.Job.*;

// import generic Java libraries
import it.infn.ct.GridEngine.UsersTracking.UsersTrackingDBInterface;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URI;

// import portlet libraries
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

// Importing Apache libraries
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Gromacs extends GenericPortlet {

    private static Log log = LogFactory.getLog(Gromacs.class);   

    @Override
    protected void doEdit(RenderRequest request,
            RenderResponse response)
            throws PortletException, IOException
    {

        PortletPreferences portletPreferences =
                (PortletPreferences) request.getPreferences();

        response.setContentType("text/html");
        
        // Getting the LATO INFRASTRUCTURE from the portlet preferences
        String lato_gromacs_INFRASTRUCTURE = portletPreferences.getValue("lato_gromacs_INFRASTRUCTURE", "N/A");
        // Getting the login credential from the portlet preferences for LATO
        String lato_gromacs_LOGIN = portletPreferences.getValue("lato_gromacs_LOGIN", "N/A");
        // Getting the password credential from the portlet preferences for LATO
        String lato_gromacs_PASSWD = portletPreferences.getValue("lato_gromacs_PASSWD", "N/A");
        // Getting the cluster hostname(s) from the portlet preferences for LATO
        String[] lato_gromacs_WMS = portletPreferences.getValues("lato_gromacs_WMS", new String[5]);
        // Getting the ETOKENSERVER from the portlet preferences for LATO
        String lato_gromacs_ETOKENSERVER = portletPreferences.getValue("lato_gromacs_ETOKENSERVER", "N/A");
        // Getting the MYPROXYSERVER from the portlet preferences for LATO
        String lato_gromacs_MYPROXYSERVER = portletPreferences.getValue("lato_gromacs_MYPROXYSERVER", "N/A");
        // Getting the PORT from the portlet preferences for LATO
        String lato_gromacs_PORT = portletPreferences.getValue("lato_gromacs_PORT", "N/A");
        // Getting the ROBOTID from the portlet preferences for LATO
        String lato_gromacs_ROBOTID = portletPreferences.getValue("lato_gromacs_ROBOTID", "N/A");
        // Getting the WEBDAV Server for LATO
        String lato_gromacs_WEBDAV = portletPreferences.getValue("lato_gromacs_WEBDAV", "N/A");
        // Getting the ROLE from the portlet preferences for LATO
        String lato_gromacs_ROLE = portletPreferences.getValue("lato_gromacs_ROLE", "N/A");
        // Getting the RENEWAL from the portlet preferences for LATO
        String lato_gromacs_RENEWAL = portletPreferences.getValue("lato_gromacs_RENEWAL", "checked");
        // Getting the DISABLEVOMS from the portlet preferences for LATO
        String lato_gromacs_DISABLEVOMS = portletPreferences.getValue("lato_gromacs_DISABLEVOMS", "unchecked");

        // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for the GARUDA VO
        String garuda_gromacs_INFRASTRUCTURE = portletPreferences.getValue("garuda_gromacs_INFRASTRUCTURE", "N/A");
        // Getting the GROMACS VONAME from the portlet preferences for the GARUDA VO
        String garuda_gromacs_VONAME = portletPreferences.getValue("garuda_gromacs_VONAME", "N/A");
        // Getting the GROMACS TOPPBDII from the portlet preferences for the GARUDA VO
        String garuda_gromacs_TOPBDII = portletPreferences.getValue("garuda_gromacs_TOPBDII", "N/A");
        // Getting the GROMACS WMS from the portlet preferences for the GARUDA VO
        String[] garuda_gromacs_WMS = portletPreferences.getValues("garuda_gromacs_WMS", new String[5]);
        // Getting the GROMACS ETOKENSERVER from the portlet preferences for the GARUDA VO
        String garuda_gromacs_ETOKENSERVER = portletPreferences.getValue("garuda_gromacs_ETOKENSERVER", "N/A");
        // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the GARUDA VO
        String garuda_gromacs_MYPROXYSERVER = portletPreferences.getValue("garuda_gromacs_MYPROXYSERVER", "N/A");
        // Getting the GROMACS PORT from the portlet preferences for the GARUDA VO
        String garuda_gromacs_PORT = portletPreferences.getValue("garuda_gromacs_PORT", "N/A");
        // Getting the GROMACS ROBOTID from the portlet preferences for the GARUDA VO
        String garuda_gromacs_ROBOTID = portletPreferences.getValue("garuda_gromacs_ROBOTID", "N/A");
        // Getting the WEBDAV Server for GARUDA
        String garuda_gromacs_WEBDAV = portletPreferences.getValue("garuda_gromacs_WEBDAV", "N/A");
        // Getting the GROMACS ROLE from the portlet preferences for the GARUDA VO
        String garuda_gromacs_ROLE = portletPreferences.getValue("garuda_gromacs_ROLE", "N/A");
        // Getting the GROMACS RENEWAL from the portlet preferences for the GARUDA VO
        String garuda_gromacs_RENEWAL = portletPreferences.getValue("garuda_gromacs_RENEWAL", "checked");
        // Getting the GROMACS DISABLEVOMS from the portlet preferences for the GARUDA VO
        String garuda_gromacs_DISABLEVOMS = portletPreferences.getValue("garuda_gromacs_DISABLEVOMS", "unchecked");

        // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for the EUMED VO
        String eumed_gromacs_INFRASTRUCTURE = portletPreferences.getValue("eumed_gromacs_INFRASTRUCTURE", "N/A");
        // Getting the GROMACS VONAME from the portlet preferences for the EUMED VO
        String eumed_gromacs_VONAME = portletPreferences.getValue("eumed_gromacs_VONAME", "N/A");
        // Getting the GROMACS TOPPBDII from the portlet preferences for the EUMED VO
        String eumed_gromacs_TOPBDII = portletPreferences.getValue("eumed_gromacs_TOPBDII", "N/A");
        // Getting the GROMACS WMS from the portlet preferences for the EUMED VO
        String[] eumed_gromacs_WMS = portletPreferences.getValues("eumed_gromacs_WMS", new String[5]);
        // Getting the GROMACS ETOKENSERVER from the portlet preferences for the EUMED VO
        String eumed_gromacs_ETOKENSERVER = portletPreferences.getValue("eumed_gromacs_ETOKENSERVER", "N/A");
        // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the EUMED VO
        String eumed_gromacs_MYPROXYSERVER = portletPreferences.getValue("eumed_gromacs_MYPROXYSERVER", "N/A");
        // Getting the GROMACS PORT from the portlet preferences for the EUMED VO
        String eumed_gromacs_PORT = portletPreferences.getValue("eumed_gromacs_PORT", "N/A");
        // Getting the GROMACS ROBOTID from the portlet preferences for the EUMED VO
        String eumed_gromacs_ROBOTID = portletPreferences.getValue("eumed_gromacs_ROBOTID", "N/A");
        // Getting the WEBDAV Server for EUMED
        String eumed_gromacs_WEBDAV = portletPreferences.getValue("eumed_gromacs_WEBDAV", "N/A");
        // Getting the GROMACS ROLE from the portlet preferences for the EUMED VO
        String eumed_gromacs_ROLE = portletPreferences.getValue("eumed_gromacs_ROLE", "N/A");
        // Getting the GROMACS RENEWAL from the portlet preferences for the EUMED VO
        String eumed_gromacs_RENEWAL = portletPreferences.getValue("eumed_gromacs_RENEWAL", "checked");
        // Getting the GROMACS DISABLEVOMS from the portlet preferences for the EUMED VO
        String eumed_gromacs_DISABLEVOMS = portletPreferences.getValue("eumed_gromacs_DISABLEVOMS", "unchecked");
        
        // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for the SAGRID VO
        String sagrid_gromacs_INFRASTRUCTURE = portletPreferences.getValue("sagrid_gromacs_INFRASTRUCTURE", "N/A");
        // Getting the GROMACS VONAME from the portlet preferences for the SAGRID VO
        String sagrid_gromacs_VONAME = portletPreferences.getValue("sagrid_gromacs_VONAME", "N/A");
        // Getting the GROMACS TOPPBDII from the portlet preferences for the SAGRID VO
        String sagrid_gromacs_TOPBDII = portletPreferences.getValue("sagrid_gromacs_TOPBDII", "N/A");
        // Getting the GROMACS WMS from the portlet preferences for the SAGRID VO
        String[] sagrid_gromacs_WMS = portletPreferences.getValues("sagrid_gromacs_WMS", new String[5]);
        // Getting the GROMACS ETOKENSERVER from the portlet preferences for the SAGRID VO
        String sagrid_gromacs_ETOKENSERVER = portletPreferences.getValue("sagrid_gromacs_ETOKENSERVER", "N/A");
        // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the SAGRID VO
        String sagrid_gromacs_MYPROXYSERVER = portletPreferences.getValue("sagrid_gromacs_MYPROXYSERVER", "N/A");
        // Getting the GROMACS PORT from the portlet preferences for the SAGRID VO
        String sagrid_gromacs_PORT = portletPreferences.getValue("sagrid_gromacs_PORT", "N/A");
        // Getting the GROMACS ROBOTID from the portlet preferences for the SAGRID VO
        String sagrid_gromacs_ROBOTID = portletPreferences.getValue("sagrid_gromacs_ROBOTID", "N/A");
        // Getting the WEBDAV Server for SAGRID
        String sagrid_gromacs_WEBDAV = portletPreferences.getValue("sagrid_gromacs_WEBDAV", "N/A");
        // Getting the GROMACS ROLE from the portlet preferences for the SAGRID VO
        String sagrid_gromacs_ROLE = portletPreferences.getValue("sagrid_gromacs_ROLE", "N/A");
        // Getting the GROMACS RENEWAL from the portlet preferences for the SAGRID VO
        String sagrid_gromacs_RENEWAL = portletPreferences.getValue("sagrid_gromacs_RENEWAL", "checked");
        // Getting the GROMACS DISABLEVOMS from the portlet preferences for the SAGRID VO
        String sagrid_gromacs_DISABLEVOMS = portletPreferences.getValue("sagrid_gromacs_DISABLEVOMS", "unchecked");

        // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for the SEE VO
        String see_gromacs_INFRASTRUCTURE = portletPreferences.getValue("see_gromacs_INFRASTRUCTURE", "N/A");
        // Getting the GROMACS VONAME from the portlet preferences for the SEE VO
        String see_gromacs_VONAME = portletPreferences.getValue("see_gromacs_VONAME", "N/A");
        // Getting the GROMACS TOPPBDII from the portlet preferences for the SEE VO
        String see_gromacs_TOPBDII = portletPreferences.getValue("see_gromacs_TOPBDII", "N/A");
        // Getting the GROMACS WMS from the portlet preferences for the SEE VO
        String[] see_gromacs_WMS = portletPreferences.getValues("see_gromacs_WMS", new String[5]);
        // Getting the GROMACS ETOKENSERVER from the portlet preferences for the SEE VO
        String see_gromacs_ETOKENSERVER = portletPreferences.getValue("see_gromacs_ETOKENSERVER", "N/A");
        // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the SEE VO
        String see_gromacs_MYPROXYSERVER = portletPreferences.getValue("see_gromacs_MYPROXYSERVER", "N/A");
        // Getting the GROMACS PORT from the portlet preferences for the SEE VO
        String see_gromacs_PORT = portletPreferences.getValue("see_gromacs_PORT", "N/A");
        // Getting the GROMACS ROBOTID from the portlet preferences for the SEE VO
        String see_gromacs_ROBOTID = portletPreferences.getValue("see_gromacs_ROBOTID", "N/A");
        // Getting the WEBDAV Server for SEE
        String see_gromacs_WEBDAV = portletPreferences.getValue("see_gromacs_WEBDAV", "N/A");
        // Getting the GROMACS ROLE from the portlet preferences for the SEE VO
        String see_gromacs_ROLE = portletPreferences.getValue("see_gromacs_ROLE", "N/A");
        // Getting the GROMACS RENEWAL from the portlet preferences for the SEE VO
        String see_gromacs_RENEWAL = portletPreferences.getValue("see_gromacs_RENEWAL", "checked");
        // Getting the GROMACS DISABLEVOMS from the portlet preferences for the SEE VO
        String see_gromacs_DISABLEVOMS = portletPreferences.getValue("see_gromacs_DISABLEVOMS", "unchecked");
        
        // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for the GISELA VO
        String gisela_gromacs_INFRASTRUCTURE = portletPreferences.getValue("gisela_gromacs_INFRASTRUCTURE", "N/A");
        // Getting the GROMACS VONAME from the portlet preferences for the GISELA VO
        String gisela_gromacs_VONAME = portletPreferences.getValue("gisela_gromacs_VONAME", "N/A");
        // Getting the GROMACS TOPPBDII from the portlet preferences for the GISELA VO
        String gisela_gromacs_TOPBDII = portletPreferences.getValue("gisela_gromacs_TOPBDII", "N/A");
        // Getting the GROMACS WMS from the portlet preferences for the GISELA VO
        String[] gisela_gromacs_WMS = portletPreferences.getValues("gisela_gromacs_WMS", new String[5]);
        // Getting the GROMACS ETOKENSERVER from the portlet preferences for the GISELA VO
        String gisela_gromacs_ETOKENSERVER = portletPreferences.getValue("gisela_gromacs_ETOKENSERVER", "N/A");
        // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the GISELA VO
        String gisela_gromacs_MYPROXYSERVER = portletPreferences.getValue("gisela_gromacs_MYPROXYSERVER", "N/A");
        // Getting the GROMACS PORT from the portlet preferences for the GISELA VO
        String gisela_gromacs_PORT = portletPreferences.getValue("gisela_gromacs_PORT", "N/A");
        // Getting the GROMACS ROBOTID from the portlet preferences for the GISELA VO
        String gisela_gromacs_ROBOTID = portletPreferences.getValue("gisela_gromacs_ROBOTID", "N/A");
        // Getting the WEBDAV Server for GISELA
        String gisela_gromacs_WEBDAV = portletPreferences.getValue("gisela_gromacs_WEBDAV", "N/A");
        // Getting the GROMACS ROLE from the portlet preferences for the GISELA VO
        String gisela_gromacs_ROLE = portletPreferences.getValue("gisela_gromacs_ROLE", "N/A");
        // Getting the GROMACS RENEWAL from the portlet preferences for the GISELA VO
        String gisela_gromacs_RENEWAL = portletPreferences.getValue("gisela_gromacs_RENEWAL", "checked");
        // Getting the GROMACS DISABLEVOMS from the portlet preferences for the GISELA VO
        String gisela_gromacs_DISABLEVOMS = portletPreferences.getValue("gisela_gromacs_DISABLEVOMS", "unchecked");

        // Getting the GROMACS APPID from the portlet preferences
        String gromacs_APPID = portletPreferences.getValue("gromacs_APPID", "N/A");
        // Getting the LOG LEVEL from the portlet preferences
        String gromacs_LOGLEVEL = portletPreferences.getValue("gromacs_LOGLEVEL", "INFO");
        // Getting the METADATA METADATA_HOST from the portlet preferences
        String gromacs_METADATA_HOST = portletPreferences.getValue("gromacs_METADATA_HOST", "N/A");
        // Getting the GROMACS OUTPUT_PATH from the portlet preferences
        String gromacs_OUTPUT_PATH = portletPreferences.getValue("gromacs_OUTPUT_PATH", "/tmp");
        // Getting the GROMACS SOFTWARE from the portlet preferences
        String gromacs_SOFTWARE = portletPreferences.getValue("gromacs_SOFTWARE", "N/A");
        // Getting the GROMACS LOCAL_PROXY from the portlet preferences
        String gromacs_LOCAL_PROXY = portletPreferences.getValue("gromacs_LOCAL_PROXY", "N/A");
        // Getting the TRACKING_DB_HOSTNAME from the portlet preferences
        String TRACKING_DB_HOSTNAME = portletPreferences.getValue("TRACKING_DB_HOSTNAME", "N/A");
        // Getting the TRACKING_DB_USERNAME from the portlet preferences
        String TRACKING_DB_USERNAME = portletPreferences.getValue("TRACKING_DB_USERNAME", "N/A");
        // Getting the TRACKING_DB_PASSWORD from the portlet preferences
        String TRACKING_DB_PASSWORD = portletPreferences.getValue("TRACKING_DB_PASSWORD", "N/A");
        // Getting the SMTP_HOST from the portlet preferences
        String SMTP_HOST = portletPreferences.getValue("SMTP_HOST", "N/A");
        // Getting the SENDER MAIL from the portlet preferences
        String SENDER_MAIL = portletPreferences.getValue("SENDER_MAIL", "N/A");
        // Get the list of enabled Infrastructures
        String[] infras = portletPreferences.getValues("gromacs_ENABLEINFRASTRUCTURE", new String[3]);

        // Set the default portlet preferences
        request.setAttribute("garuda_gromacs_INFRASTRUCTURE", garuda_gromacs_INFRASTRUCTURE.trim());
        request.setAttribute("garuda_gromacs_VONAME", garuda_gromacs_VONAME.trim());
        request.setAttribute("garuda_gromacs_TOPBDII", garuda_gromacs_TOPBDII.trim());
        request.setAttribute("garuda_gromacs_WMS", garuda_gromacs_WMS);
        request.setAttribute("garuda_gromacs_ETOKENSERVER", garuda_gromacs_ETOKENSERVER.trim());
        request.setAttribute("garuda_gromacs_MYPROXYSERVER", garuda_gromacs_MYPROXYSERVER.trim());
        request.setAttribute("garuda_gromacs_PORT", garuda_gromacs_PORT.trim());
        request.setAttribute("garuda_gromacs_ROBOTID", garuda_gromacs_ROBOTID.trim());
        request.setAttribute("garuda_gromacs_WEBDAV", garuda_gromacs_WEBDAV.trim());
        request.setAttribute("garuda_gromacs_ROLE", garuda_gromacs_ROLE.trim());
        request.setAttribute("garuda_gromacs_RENEWAL", garuda_gromacs_RENEWAL);
        request.setAttribute("garuda_gromacs_DISABLEVOMS", garuda_gromacs_DISABLEVOMS);
        
        request.setAttribute("lato_gromacs_INFRASTRUCTURE", lato_gromacs_INFRASTRUCTURE.trim());
        request.setAttribute("lato_gromacs_LOGIN", lato_gromacs_LOGIN.trim());
        request.setAttribute("lato_gromacs_PASSWD", lato_gromacs_PASSWD.trim());
        request.setAttribute("lato_gromacs_WMS", lato_gromacs_WMS);
        request.setAttribute("lato_gromacs_ETOKENSERVER", lato_gromacs_ETOKENSERVER.trim());
        request.setAttribute("lato_gromacs_MYPROXYSERVER", lato_gromacs_MYPROXYSERVER.trim());
        request.setAttribute("lato_gromacs_PORT", lato_gromacs_PORT.trim());
        request.setAttribute("lato_gromacs_ROBOTID", lato_gromacs_ROBOTID.trim());
        request.setAttribute("lato_gromacs_WEBDAV", lato_gromacs_WEBDAV.trim());
        request.setAttribute("lato_gromacs_ROLE", lato_gromacs_ROLE.trim());
        request.setAttribute("lato_gromacs_RENEWAL", lato_gromacs_RENEWAL);
        request.setAttribute("lato_gromacs_DISABLEVOMS", lato_gromacs_DISABLEVOMS);

        request.setAttribute("eumed_gromacs_INFRASTRUCTURE", eumed_gromacs_INFRASTRUCTURE.trim());
        request.setAttribute("eumed_gromacs_VONAME", eumed_gromacs_VONAME.trim());
        request.setAttribute("eumed_gromacs_TOPBDII", eumed_gromacs_TOPBDII.trim());
        request.setAttribute("eumed_gromacs_WMS", eumed_gromacs_WMS);
        request.setAttribute("eumed_gromacs_ETOKENSERVER", eumed_gromacs_ETOKENSERVER.trim());
        request.setAttribute("eumed_gromacs_MYPROXYSERVER", eumed_gromacs_MYPROXYSERVER.trim());
        request.setAttribute("eumed_gromacs_PORT", eumed_gromacs_PORT.trim());
        request.setAttribute("eumed_gromacs_ROBOTID", eumed_gromacs_ROBOTID.trim());
        request.setAttribute("eumed_gromacs_WEBDAV", eumed_gromacs_WEBDAV.trim());
        request.setAttribute("eumed_gromacs_ROLE", eumed_gromacs_ROLE.trim());
        request.setAttribute("eumed_gromacs_RENEWAL", eumed_gromacs_RENEWAL);
        request.setAttribute("eumed_gromacs_DISABLEVOMS", eumed_gromacs_DISABLEVOMS);
        
        request.setAttribute("sagrid_gromacs_INFRASTRUCTURE", sagrid_gromacs_INFRASTRUCTURE.trim());
        request.setAttribute("sagrid_gromacs_VONAME", sagrid_gromacs_VONAME.trim());
        request.setAttribute("sagrid_gromacs_TOPBDII", sagrid_gromacs_TOPBDII.trim());
        request.setAttribute("sagrid_gromacs_WMS", sagrid_gromacs_WMS);
        request.setAttribute("sagrid_gromacs_ETOKENSERVER", sagrid_gromacs_ETOKENSERVER.trim());
        request.setAttribute("sagrid_gromacs_MYPROXYSERVER", sagrid_gromacs_MYPROXYSERVER.trim());
        request.setAttribute("sagrid_gromacs_PORT", sagrid_gromacs_PORT.trim());
        request.setAttribute("sagrid_gromacs_ROBOTID", sagrid_gromacs_ROBOTID.trim());
        request.setAttribute("sagrid_gromacs_WEBDAV", sagrid_gromacs_WEBDAV.trim());
        request.setAttribute("sagrid_gromacs_ROLE", sagrid_gromacs_ROLE.trim());
        request.setAttribute("sagrid_gromacs_RENEWAL", sagrid_gromacs_RENEWAL);
        request.setAttribute("sagrid_gromacs_DISABLEVOMS", sagrid_gromacs_DISABLEVOMS);

        request.setAttribute("see_gromacs_INFRASTRUCTURE", see_gromacs_INFRASTRUCTURE.trim());
        request.setAttribute("see_gromacs_VONAME", see_gromacs_VONAME.trim());
        request.setAttribute("see_gromacs_TOPBDII", see_gromacs_TOPBDII.trim());
        request.setAttribute("see_gromacs_WMS", see_gromacs_WMS);
        request.setAttribute("see_gromacs_ETOKENSERVER", see_gromacs_ETOKENSERVER.trim());
        request.setAttribute("see_gromacs_MYPROXYSERVER", see_gromacs_MYPROXYSERVER.trim());
        request.setAttribute("see_gromacs_PORT", see_gromacs_PORT.trim());
        request.setAttribute("see_gromacs_ROBOTID", see_gromacs_ROBOTID.trim());
        request.setAttribute("see_gromacs_WEBDAV", see_gromacs_WEBDAV.trim());
        request.setAttribute("see_gromacs_ROLE", see_gromacs_ROLE.trim());
        request.setAttribute("see_gromacs_RENEWAL", see_gromacs_RENEWAL);
        request.setAttribute("see_gromacs_DISABLEVOMS", see_gromacs_DISABLEVOMS);
        
        request.setAttribute("gisela_gromacs_INFRASTRUCTURE", gisela_gromacs_INFRASTRUCTURE.trim());
        request.setAttribute("gisela_gromacs_VONAME", gisela_gromacs_VONAME.trim());
        request.setAttribute("gisela_gromacs_TOPBDII", gisela_gromacs_TOPBDII.trim());
        request.setAttribute("gisela_gromacs_WMS", gisela_gromacs_WMS);
        request.setAttribute("gisela_gromacs_ETOKENSERVER", gisela_gromacs_ETOKENSERVER.trim());
        request.setAttribute("gisela_gromacs_MYPROXYSERVER", gisela_gromacs_MYPROXYSERVER.trim());
        request.setAttribute("gisela_gromacs_PORT", gisela_gromacs_PORT.trim());
        request.setAttribute("gisela_gromacs_ROBOTID", gisela_gromacs_ROBOTID.trim());
        request.setAttribute("gisela_gromacs_WEBDAV", gisela_gromacs_WEBDAV.trim());
        request.setAttribute("gisela_gromacs_ROLE", gisela_gromacs_ROLE.trim());
        request.setAttribute("gisela_gromacs_RENEWAL", gisela_gromacs_RENEWAL);
        request.setAttribute("gisela_gromacs_DISABLEVOMS", gisela_gromacs_DISABLEVOMS);

        request.setAttribute("gromacs_ENABLEINFRASTRUCTURE", infras);
        request.setAttribute("gromacs_APPID", gromacs_APPID.trim());
        request.setAttribute("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
        request.setAttribute("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
        request.setAttribute("gromacs_OUTPUT_PATH", gromacs_OUTPUT_PATH.trim());
        request.setAttribute("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
        request.setAttribute("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
        request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
        request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
        request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
        request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
        request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        
        if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
        log.info("\nStarting the EDIT mode...with this settings"
        + "\nlato_gromacs_INFRASTRUCTURE: " + lato_gromacs_INFRASTRUCTURE
        + "\nlato_gromacs_LOGIN: " + lato_gromacs_LOGIN
        + "\nlato_gromacs_PASSWD: " + lato_gromacs_PASSWD                    
        + "\nlato_gromacs_ETOKENSERVER: " + lato_gromacs_ETOKENSERVER
        + "\nlato_gromacs_MYPROXYSERVER: " + lato_gromacs_MYPROXYSERVER
        + "\nlato_gromacs_PORT: " + lato_gromacs_PORT
        + "\nlato_gromacs_ROBOTID: " + lato_gromacs_ROBOTID
        + "\nlato_gromacs_WEBDAV: " + lato_gromacs_WEBDAV
        + "\nlato_gromacs_ROLE: " + lato_gromacs_ROLE
        + "\nlato_gromacs_RENEWAL: " + lato_gromacs_RENEWAL
        + "\nlato_gromacs_DISABLEVOMS: " + lato_gromacs_DISABLEVOMS
                
        + "\ngaruda_gromacs_INFRASTRUCTURE: " + garuda_gromacs_INFRASTRUCTURE
        + "\ngaruda_gromacs_VONAME: " + garuda_gromacs_VONAME
        + "\ngaruda_gromacs_TOPBDII: " + garuda_gromacs_TOPBDII                    
        + "\ngaruda_gromacs_ETOKENSERVER: " + garuda_gromacs_ETOKENSERVER
        + "\ngaruda_gromacs_MYPROXYSERVER: " + garuda_gromacs_MYPROXYSERVER
        + "\ngaruda_gromacs_PORT: " + garuda_gromacs_PORT
        + "\ngaruda_gromacs_ROBOTID: " + garuda_gromacs_ROBOTID
        + "\ngaruda_gromacs_WEBDAV: " + garuda_gromacs_WEBDAV 
        + "\ngaruda_gromacs_ROLE: " + garuda_gromacs_ROLE
        + "\ngaruda_gromacs_RENEWAL: " + garuda_gromacs_RENEWAL
        + "\ngaruda_gromacs_DISABLEVOMS: " + garuda_gromacs_DISABLEVOMS

        + "\n\neumed_gromacs_INFRASTRUCTURE: " + eumed_gromacs_INFRASTRUCTURE
        + "\neumed_gromacs_VONAME: " + eumed_gromacs_VONAME
        + "\neumed_gromacs_TOPBDII: " + eumed_gromacs_TOPBDII                    
        + "\neumed_gromacs_ETOKENSERVER: " + eumed_gromacs_ETOKENSERVER
        + "\neumed_gromacs_MYPROXYSERVER: " + eumed_gromacs_MYPROXYSERVER
        + "\neumed_gromacs_PORT: " + eumed_gromacs_PORT
        + "\neumed_gromacs_ROBOTID: " + eumed_gromacs_ROBOTID
        + "\neumed_gromacs_WEBDAV: " + eumed_gromacs_WEBDAV
        + "\neumed_gromacs_ROLE: " + eumed_gromacs_ROLE
        + "\neumed_gromacs_RENEWAL: " + eumed_gromacs_RENEWAL
        + "\neumed_gromacs_DISABLEVOMS: " + eumed_gromacs_DISABLEVOMS
                
        + "\n\nsagrid_gromacs_INFRASTRUCTURE: " + sagrid_gromacs_INFRASTRUCTURE
        + "\nsagrid_gromacs_VONAME: " + sagrid_gromacs_VONAME
        + "\nsagrid_gromacs_TOPBDII: " + sagrid_gromacs_TOPBDII                    
        + "\nsagrid_gromacs_ETOKENSERVER: " + sagrid_gromacs_ETOKENSERVER
        + "\nsagrid_gromacs_MYPROXYSERVER: " + sagrid_gromacs_MYPROXYSERVER
        + "\nsagrid_gromacs_PORT: " + sagrid_gromacs_PORT
        + "\nsagrid_gromacs_ROBOTID: " + sagrid_gromacs_ROBOTID
        + "\nsagrid_gromacs_WEBDAV: " + sagrid_gromacs_WEBDAV
        + "\nsagrid_gromacs_ROLE: " + sagrid_gromacs_ROLE
        + "\nsagrid_gromacs_RENEWAL: " + sagrid_gromacs_RENEWAL
        + "\nsagrid_gromacs_DISABLEVOMS: " + sagrid_gromacs_DISABLEVOMS

        + "\n\nsee_gromacs_INFRASTRUCTURE: " + see_gromacs_INFRASTRUCTURE
        + "\nsee_gromacs_VONAME: " + see_gromacs_VONAME
        + "\nsee_gromacs_TOPBDII: " + see_gromacs_TOPBDII                   
        + "\nsee_gromacs_ETOKENSERVER: " + see_gromacs_ETOKENSERVER
        + "\nsee_gromacs_MYPROXYSERVER: " + see_gromacs_MYPROXYSERVER
        + "\nsee_gromacs_PORT: " + see_gromacs_PORT
        + "\nsee_gromacs_ROBOTID: " + see_gromacs_ROBOTID
        + "\nsee_gromacs_WEBDAV: " + see_gromacs_WEBDAV 
        + "\nsee_gromacs_ROLE: " + see_gromacs_ROLE
        + "\nsee_gromacs_RENEWAL: " + see_gromacs_RENEWAL
        + "\nsee_gromacs_DISABLEVOMS: " + see_gromacs_DISABLEVOMS
                
        + "\n\ngisela_gromacs_INFRASTRUCTURE: " + gisela_gromacs_INFRASTRUCTURE
        + "\ngisela_gromacs_VONAME: " + gisela_gromacs_VONAME
        + "\ngisela_gromacs_TOPBDII: " + gisela_gromacs_TOPBDII                   
        + "\ngisela_gromacs_ETOKENSERVER: " + gisela_gromacs_ETOKENSERVER
        + "\ngisela_gromacs_MYPROXYSERVER: " + gisela_gromacs_MYPROXYSERVER
        + "\ngisela_gromacs_PORT: " + gisela_gromacs_PORT
        + "\ngisela_gromacs_ROBOTID: " + gisela_gromacs_ROBOTID
        + "\ngisela_gromacs_WEBDAV: " + gisela_gromacs_WEBDAV 
        + "\ngisela_gromacs_ROLE: " + gisela_gromacs_ROLE
        + "\ngisela_gromacs_RENEWAL: " + gisela_gromacs_RENEWAL
        + "\ngisela_gromacs_DISABLEVOMS: " + gisela_gromacs_DISABLEVOMS                
        
        + "\ngromacs_APPID: " + gromacs_APPID
        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
        + "\ngromacs_SOFTWARE: " +gromacs_SOFTWARE
        + "\ngromacs_LOCAL_PROXY: " +gromacs_LOCAL_PROXY
        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
        + "\nSMTP Server: " + SMTP_HOST
        + "\nSender: " + SENDER_MAIL);
        }

        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/edit.jsp");

        dispatcher.include(request, response);
    }

    @Override
    protected void doHelp(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {
        //super.doHelp(request, response);

        response.setContentType("text/html");

        log.info("\nStarting the HELP mode...");
        PortletRequestDispatcher dispatcher =
                getPortletContext().getRequestDispatcher("/help.jsp");

        dispatcher.include(request, response);
    }

    @Override
    protected void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {

        PortletPreferences portletPreferences =
                (PortletPreferences) request.getPreferences();

        response.setContentType("text/html");

        //java.util.Enumeration listPreferences = portletPreferences.getNames();
        PortletRequestDispatcher dispatcher = null;
        
        String lato_gromacs_PASSWD = "";
        String lato_gromacs_LOGIN = "";
        String garuda_gromacs_TOPBDII = "";
        String garuda_gromacs_VONAME = "";
        String eumed_gromacs_TOPBDII = "";
        String eumed_gromacs_VONAME = "";
        String sagrid_gromacs_TOPBDII = "";
        String sagrid_gromacs_VONAME = "";
        String see_gromacs_TOPBDII = "";
        String see_gromacs_VONAME = "";
        String gisela_gromacs_TOPBDII = "";
        String gisela_gromacs_VONAME = "";
        
        String lato_gromacs_ENABLEINFRASTRUCTURE = "";
        String garuda_gromacs_ENABLEINFRASTRUCTURE = "";
        String eumed_gromacs_ENABLEINFRASTRUCTURE = "";
        String sagrid_gromacs_ENABLEINFRASTRUCTURE = "";
        String see_gromacs_ENABLEINFRASTRUCTURE = "";
        String gisela_gromacs_ENABLEINFRASTRUCTURE = "";
        String[] infras = new String[6];
        
        String[] lato_gromacs_WMS = new String [5];
        
        String[] gromacs_INFRASTRUCTURES = 
                portletPreferences.getValues("gromacs_ENABLEINFRASTRUCTURE", new String[5]);
        
        for (int i=0; i<gromacs_INFRASTRUCTURES.length; i++) {            
            if (gromacs_INFRASTRUCTURES[i]!=null && gromacs_INFRASTRUCTURES[i].equals("lato")) 
                { lato_gromacs_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n LATO!"); }
            if (gromacs_INFRASTRUCTURES[i]!=null && gromacs_INFRASTRUCTURES[i].equals("garuda")) 
                { garuda_gromacs_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n GARUDA!"); }
            if (gromacs_INFRASTRUCTURES[i]!=null && gromacs_INFRASTRUCTURES[i].equals("eumed")) 
                { eumed_gromacs_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n EUMED!"); }
            if (gromacs_INFRASTRUCTURES[i]!=null && gromacs_INFRASTRUCTURES[i].equals("sagrid")) 
                { sagrid_gromacs_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n SAGRID!"); }
            if (gromacs_INFRASTRUCTURES[i]!=null && gromacs_INFRASTRUCTURES[i].equals("see")) 
                { see_gromacs_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n SEE!"); }
            if (gromacs_INFRASTRUCTURES[i]!=null && gromacs_INFRASTRUCTURES[i].equals("gisela")) 
                { gisela_gromacs_ENABLEINFRASTRUCTURE = "checked"; log.info ("\n GISELA!"); }
        }
        
        // Getting the GROMACS ENABLEINFRASTRUCTURE from the portlet preferences
        //gromacs_ENABLEINFRASTRUCTURE = portletPreferences.getValue("gromacs_ENABLEINFRASTRUCTURE", "NULL");
        // Getting the GROMACS APPID from the portlet preferences
        String gromacs_APPID = portletPreferences.getValue("gromacs_APPID", "N/A");
        // Getting the LOGLEVEL from the portlet preferences
        String gromacs_LOGLEVEL = portletPreferences.getValue("gromacs_LOGLEVEL", "INFO");
        // Getting the METADATA METADATA_HOST from the portlet preferences
        String gromacs_METADATA_HOST = portletPreferences.getValue("gromacs_METADATA_HOST", "N/A");
        // Getting the GROMACS OUTPUT_PATH from the portlet preferences
        String gromacs_OUTPUT_PATH = portletPreferences.getValue("gromacs_OUTPUT_PATH", "/tmp");
        // Getting the GROMACS SOFTWARE from the portlet preferences
        String gromacs_SOFTWARE = portletPreferences.getValue("gromacs_SOFTWARE", "N/A");
        // Getting the GROMACS LOCAL_PROXY from the portlet preferences
        String gromacs_LOCAL_PROXY = portletPreferences.getValue("gromacs_LOCAL_PROXY", "N/A");
        // Getting the TRACKING_DB_HOSTNAME from the portlet preferences
        String TRACKING_DB_HOSTNAME = portletPreferences.getValue("TRACKING_DB_HOSTNAME", "N/A");
        // Getting the TRACKING_DB_USERNAME from the portlet preferences
        String TRACKING_DB_USERNAME = portletPreferences.getValue("TRACKING_DB_USERNAME", "N/A");
        // Getting the TRACKING_DB_PASSWORD from the portlet preferences
        String TRACKING_DB_PASSWORD = portletPreferences.getValue("TRACKING_DB_PASSWORD", "N/A");
        // Getting the SMTP_HOST from the portlet preferences
        String SMTP_HOST = portletPreferences.getValue("SMTP_HOST", "N/A");
        // Getting the SENDER_MAIL from the portlet preferences
        String SENDER_MAIL = portletPreferences.getValue("SENDER_MAIL", "N/A");
        
        if (lato_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[0]="lato";
            // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for LATO
            String lato_gromacs_INFRASTRUCTURE = portletPreferences.getValue("lato_gromacs_INFRASTRUCTURE", "N/A");
            // Getting the GROMACS VONAME from the portlet preferences for LATO
            lato_gromacs_LOGIN = portletPreferences.getValue("lato_gromacs_LOGIN", "N/A");
            // Getting the GROMACS TOPPBDII from the portlet preferences for LATO
            lato_gromacs_PASSWD = portletPreferences.getValue("lato_gromacs_PASSWD", "N/A");
            // Getting the GROMACS WMS from the portlet preferences for LATO
            lato_gromacs_WMS = portletPreferences.getValues("lato_gromacs_WMS", new String[5]);
            // Getting the GROMACS ETOKENSERVER from the portlet preferences for LATO
            String lato_gromacs_ETOKENSERVER = portletPreferences.getValue("lato_gromacs_ETOKENSERVER", "N/A");
            // Getting the GROMACS MYPROXYSERVER from the portlet preferences for LATO
            String lato_gromacs_MYPROXYSERVER = portletPreferences.getValue("lato_gromacs_MYPROXYSERVER", "N/A");
            // Getting the GROMACS PORT from the portlet preferences for LATO
            String lato_gromacs_PORT = portletPreferences.getValue("lato_gromacs_PORT", "N/A");
            // Getting the GROMACS ROBOTID from the portlet preferences for LATO
            String lato_gromacs_ROBOTID = portletPreferences.getValue("lato_gromacs_ROBOTID", "N/A");
            // Getting the GROMACS WEBDAV from the portlet preferences for LATO
            String lato_gromacs_WEBDAV = portletPreferences.getValue("lato_gromacs_WEBDAV", "N/A");
            // Getting the GROMACS ROLE from the portlet preferences for LATO
            String lato_gromacs_ROLE = portletPreferences.getValue("lato_gromacs_ROLE", "N/A");
            // Getting the GROMACS RENEWAL from the portlet preferences for LATO
            String lato_gromacs_RENEWAL = portletPreferences.getValue("lato_gromacs_RENEWAL", "checked");
            // Getting the GROMACS DISABLEVOMS from the portlet preferences for LATO
            String lato_gromacs_DISABLEVOMS = portletPreferences.getValue("lato_gromacs_DISABLEVOMS", "unchecked");
            
            // Fetching all the WMS Endpoints for LATO
            String lato_WMS = "";
            if (lato_gromacs_ENABLEINFRASTRUCTURE.equals("checked")) {
                if (lato_gromacs_WMS!=null) {
                    //log.info("length="+lato_gromacs_WMS.length);
                    for (int i = 0; i < lato_gromacs_WMS.length; i++)
                        if (!(lato_gromacs_WMS[i].trim().equals("N/A")) ) 
                            lato_WMS += lato_gromacs_WMS[i] + " ";                        
                } else { log.info("WMS not set for LATO!"); lato_gromacs_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("lato_gromacs_INFRASTRUCTURE", lato_gromacs_INFRASTRUCTURE.trim());
            request.setAttribute("lato_gromacs_LOGIN", lato_gromacs_LOGIN.trim());
            request.setAttribute("lato_gromacs_PASSWD", lato_gromacs_PASSWD.trim());
            request.setAttribute("lato_gromacs_WMS", lato_WMS);
            request.setAttribute("lato_gromacs_ETOKENSERVER", lato_gromacs_ETOKENSERVER.trim());
            request.setAttribute("lato_gromacs_MYPROXYSERVER", lato_gromacs_MYPROXYSERVER.trim());
            request.setAttribute("lato_gromacs_PORT", lato_gromacs_PORT.trim());
            request.setAttribute("lato_gromacs_ROBOTID", lato_gromacs_ROBOTID.trim());
            request.setAttribute("lato_gromacs_WEBDAV", lato_gromacs_WEBDAV.trim());
            request.setAttribute("lato_gromacs_ROLE", lato_gromacs_ROLE.trim());
            request.setAttribute("lato_gromacs_RENEWAL", lato_gromacs_RENEWAL);
            request.setAttribute("lato_gromacs_DISABLEVOMS", lato_gromacs_DISABLEVOMS);
            
            //request.setAttribute("gromacs_ENABLEINFRASTRUCTURE", gromacs_ENABLEINFRASTRUCTURE);
            request.setAttribute("gromacs_APPID", gromacs_APPID.trim());
            request.setAttribute("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
            request.setAttribute("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
            request.setAttribute("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
            request.setAttribute("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (garuda_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[1]="garuda";
            // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for the GARUDA VO
            String garuda_gromacs_INFRASTRUCTURE = portletPreferences.getValue("garuda_gromacs_INFRASTRUCTURE", "N/A");
            // Getting the GROMACS VONAME from the portlet preferences for the GARUDA VO
            garuda_gromacs_VONAME = portletPreferences.getValue("garuda_gromacs_VONAME", "N/A");
            // Getting the GROMACS TOPPBDII from the portlet preferences for the GARUDA VO
            garuda_gromacs_TOPBDII = portletPreferences.getValue("garuda_gromacs_TOPBDII", "N/A");
            // Getting the GROMACS WMS from the portlet preferences for the GARUDA VO
            String[] garuda_gromacs_WMS = portletPreferences.getValues("garuda_gromacs_WMS", new String[5]);
            // Getting the GROMACS ETOKENSERVER from the portlet preferences for the GARUDA VO
            String garuda_gromacs_ETOKENSERVER = portletPreferences.getValue("garuda_gromacs_ETOKENSERVER", "N/A");
            // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the GARUDA VO
            String garuda_gromacs_MYPROXYSERVER = portletPreferences.getValue("garuda_gromacs_MYPROXYSERVER", "N/A");
            // Getting the GROMACS PORT from the portlet preferences for the GARUDA VO
            String garuda_gromacs_PORT = portletPreferences.getValue("garuda_gromacs_PORT", "N/A");
            // Getting the GROMACS ROBOTID from the portlet preferences for the GARUDA VO
            String garuda_gromacs_ROBOTID = portletPreferences.getValue("garuda_gromacs_ROBOTID", "N/A");
            // Getting the GROMACS WEBDAV from the portlet preferences for the GARUDA VO
            String garuda_gromacs_WEBDAV = portletPreferences.getValue("garuda_gromacs_WEBDAV", "N/A");
            // Getting the GROMACS ROLE from the portlet preferences for the GARUDA VO
            String garuda_gromacs_ROLE = portletPreferences.getValue("garuda_gromacs_ROLE", "N/A");
            // Getting the GROMACS RENEWAL from the portlet preferences for the GARUDA VO
            String garuda_gromacs_RENEWAL = portletPreferences.getValue("garuda_gromacs_RENEWAL", "checked");
            // Getting the GROMACS DISABLEVOMS from the portlet preferences for the GARUDA VO
            String garuda_gromacs_DISABLEVOMS = portletPreferences.getValue("garuda_gromacs_DISABLEVOMS", "unchecked");
            
            // Fetching all the WMS Endpoints for the GARUDA VO
            String garuda_WMS = "";
            if (garuda_gromacs_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (garuda_gromacs_WMS!=null) {
                    //log.info("length="+garuda_gromacs_WMS.length);
                    for (int i = 0; i < garuda_gromacs_WMS.length; i++)
                        if (!(garuda_gromacs_WMS[i].trim().equals("N/A")) ) 
                            garuda_WMS += garuda_gromacs_WMS[i] + " ";                        
                } else { log.info("WMS not set for GARUDA!"); garuda_gromacs_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("garuda_gromacs_INFRASTRUCTURE", garuda_gromacs_INFRASTRUCTURE.trim());
            request.setAttribute("garuda_gromacs_VONAME", garuda_gromacs_VONAME.trim());
            request.setAttribute("garuda_gromacs_TOPBDII", garuda_gromacs_TOPBDII.trim());
            request.setAttribute("garuda_gromacs_WMS", garuda_WMS);
            request.setAttribute("garuda_gromacs_ETOKENSERVER", garuda_gromacs_ETOKENSERVER.trim());
            request.setAttribute("garuda_gromacs_MYPROXYSERVER", garuda_gromacs_MYPROXYSERVER.trim());
            request.setAttribute("garuda_gromacs_PORT", garuda_gromacs_PORT.trim());
            request.setAttribute("garuda_gromacs_ROBOTID", garuda_gromacs_ROBOTID.trim());
            request.setAttribute("garuda_gromacs_WEBDAV", garuda_gromacs_WEBDAV.trim());
            request.setAttribute("garuda_gromacs_ROLE", garuda_gromacs_ROLE.trim());
            request.setAttribute("garuda_gromacs_RENEWAL", garuda_gromacs_RENEWAL);
            request.setAttribute("garuda_gromacs_DISABLEVOMS", garuda_gromacs_DISABLEVOMS);
            
            //request.setAttribute("gromacs_ENABLEINFRASTRUCTURE", gromacs_ENABLEINFRASTRUCTURE);
            request.setAttribute("gromacs_APPID", gromacs_APPID.trim());
            request.setAttribute("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
            request.setAttribute("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
            request.setAttribute("gromacs_OUTPUT_PATH", gromacs_OUTPUT_PATH.trim());
            request.setAttribute("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
            request.setAttribute("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (eumed_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[2]="eumed";
            // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for the EUMED VO
            String eumed_gromacs_INFRASTRUCTURE = portletPreferences.getValue("eumed_gromacs_INFRASTRUCTURE", "N/A");
            // Getting the GROMACS VONAME from the portlet preferences for the EUMED VO
            eumed_gromacs_VONAME = portletPreferences.getValue("eumed_gromacs_VONAME", "N/A");
            // Getting the GROMACS TOPPBDII from the portlet preferences for the EUMED VO
            eumed_gromacs_TOPBDII = portletPreferences.getValue("eumed_gromacs_TOPBDII", "N/A");
            // Getting the GROMACS WMS from the portlet preferences for the EUMED VO
            String[] eumed_gromacs_WMS = portletPreferences.getValues("eumed_gromacs_WMS", new String[5]);
            // Getting the GROMACS ETOKENSERVER from the portlet preferences for the EUMED VO
            String eumed_gromacs_ETOKENSERVER = portletPreferences.getValue("eumed_gromacs_ETOKENSERVER", "N/A");
            // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the EUMED VO
            String eumed_gromacs_MYPROXYSERVER = portletPreferences.getValue("eumed_gromacs_MYPROXYSERVER", "N/A");
            // Getting the GROMACS PORT from the portlet preferences for the EUMED VO
            String eumed_gromacs_PORT = portletPreferences.getValue("eumed_gromacs_PORT", "N/A");
            // Getting the GROMACS ROBOTID from the portlet preferences for the EUMED VO
            String eumed_gromacs_ROBOTID = portletPreferences.getValue("eumed_gromacs_ROBOTID", "N/A");
            // Getting the GROMACS WEBDAV from the portlet preferences for the EUMED VO
            String eumed_gromacs_WEBDAV = portletPreferences.getValue("eumed_gromacs_WEBDAV", "N/A");
            // Getting the GROMACS ROLE from the portlet preferences for the EUMED VO
            String eumed_gromacs_ROLE = portletPreferences.getValue("eumed_gromacs_ROLE", "N/A");
            // Getting the GROMACS RENEWAL from the portlet preferences for the EUMED VO
            String eumed_gromacs_RENEWAL = portletPreferences.getValue("eumed_gromacs_RENEWAL", "checked");
            // Getting the GROMACS DISABLEVOMS from the portlet preferences for the EUMED VO
            String eumed_gromacs_DISABLEVOMS = portletPreferences.getValue("eumed_gromacs_DISABLEVOMS", "unchecked");
                                    
            // Fetching all the WMS Endpoints for the EUMED VO
            String eumed_WMS = "";
            if (eumed_gromacs_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (eumed_gromacs_WMS!=null) {
                    //log.info("length="+eumed_gromacs_WMS.length);
                    for (int i = 0; i < eumed_gromacs_WMS.length; i++)
                        if (!(eumed_gromacs_WMS[i].trim().equals("N/A")) ) 
                            eumed_WMS += eumed_gromacs_WMS[i] + " ";                        
                } else { log.info("WMS not set for EUMED!"); eumed_gromacs_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("eumed_gromacs_INFRASTRUCTURE", eumed_gromacs_INFRASTRUCTURE.trim());
            request.setAttribute("eumed_gromacs_VONAME", eumed_gromacs_VONAME.trim());
            request.setAttribute("eumed_gromacs_TOPBDII", eumed_gromacs_TOPBDII.trim());
            request.setAttribute("eumed_gromacs_WMS", eumed_WMS);
            request.setAttribute("eumed_gromacs_ETOKENSERVER", eumed_gromacs_ETOKENSERVER.trim());
            request.setAttribute("eumed_gromacs_MYPROXYSERVER", eumed_gromacs_MYPROXYSERVER.trim());
            request.setAttribute("eumed_gromacs_PORT", eumed_gromacs_PORT.trim());
            request.setAttribute("eumed_gromacs_ROBOTID", eumed_gromacs_ROBOTID.trim());
            request.setAttribute("eumed_gromacs_WEBDAV", eumed_gromacs_WEBDAV.trim());
            request.setAttribute("eumed_gromacs_ROLE", eumed_gromacs_ROLE.trim());
            request.setAttribute("eumed_gromacs_RENEWAL", eumed_gromacs_RENEWAL);
            request.setAttribute("eumed_gromacs_DISABLEVOMS", eumed_gromacs_DISABLEVOMS);

            //request.setAttribute("gromacs_ENABLEINFRASTRUCTURE", gromacs_ENABLEINFRASTRUCTURE);
            request.setAttribute("gromacs_APPID", gromacs_APPID.trim());
            request.setAttribute("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
            request.setAttribute("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
            request.setAttribute("gromacs_OUTPUT_PATH", gromacs_OUTPUT_PATH.trim());
            request.setAttribute("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
            request.setAttribute("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (sagrid_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[3]="sagrid";
            // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for the SAGRID VO
            String sagrid_gromacs_INFRASTRUCTURE = portletPreferences.getValue("sagrid_gromacs_INFRASTRUCTURE", "N/A");
            // Getting the GROMACS VONAME from the portlet preferences for the SAGRID VO
            sagrid_gromacs_VONAME = portletPreferences.getValue("sagrid_gromacs_VONAME", "N/A");
            // Getting the GROMACS TOPPBDII from the portlet preferences for the SAGRID VO
            sagrid_gromacs_TOPBDII = portletPreferences.getValue("sagrid_gromacs_TOPBDII", "N/A");
            // Getting the GROMACS WMS from the portlet preferences for the SAGRID VO
            String[] sagrid_gromacs_WMS = portletPreferences.getValues("sagrid_gromacs_WMS", new String[5]);
            // Getting the GROMACS ETOKENSERVER from the portlet preferences for the SAGRID VO
            String sagrid_gromacs_ETOKENSERVER = portletPreferences.getValue("sagrid_gromacs_ETOKENSERVER", "N/A");
            // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the SAGRID VO
            String sagrid_gromacs_MYPROXYSERVER = portletPreferences.getValue("sagrid_gromacs_MYPROXYSERVER", "N/A");
            // Getting the GROMACS PORT from the portlet preferences for the SAGRID VO
            String sagrid_gromacs_PORT = portletPreferences.getValue("sagrid_gromacs_PORT", "N/A");
            // Getting the GROMACS ROBOTID from the portlet preferences for the SAGRID VO
            String sagrid_gromacs_ROBOTID = portletPreferences.getValue("sagrid_gromacs_ROBOTID", "N/A");
            // Getting the GROMACS WEBDAV from the portlet preferences for the SAGRID VO
            String sagrid_gromacs_WEBDAV = portletPreferences.getValue("sagrid_gromacs_WEBDAV", "N/A");
            // Getting the GROMACS ROLE from the portlet preferences for the SAGRID VO
            String sagrid_gromacs_ROLE = portletPreferences.getValue("sagrid_gromacs_ROLE", "N/A");
            // Getting the GROMACS RENEWAL from the portlet preferences for the SAGRID VO
            String sagrid_gromacs_RENEWAL = portletPreferences.getValue("sagrid_gromacs_RENEWAL", "checked");
            // Getting the GROMACS DISABLEVOMS from the portlet preferences for the SAGRID VO
            String sagrid_gromacs_DISABLEVOMS = portletPreferences.getValue("sagrid_gromacs_DISABLEVOMS", "unchecked");
                                    
            // Fetching all the WMS Endpoints for the EUMED VO
            String sagrid_WMS = "";
            if (sagrid_gromacs_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (sagrid_gromacs_WMS!=null) {
                    //log.info("length="+sagrid_gromacs_WMS.length);
                    for (int i = 0; i < sagrid_gromacs_WMS.length; i++)
                        if (!(sagrid_gromacs_WMS[i].trim().equals("N/A")) ) 
                            sagrid_WMS += sagrid_gromacs_WMS[i] + " ";                        
                } else { log.info("WMS not set for SAGRID!"); sagrid_gromacs_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("sagrid_gromacs_INFRASTRUCTURE", sagrid_gromacs_INFRASTRUCTURE.trim());
            request.setAttribute("sagrid_gromacs_VONAME", sagrid_gromacs_VONAME.trim());
            request.setAttribute("sagrid_gromacs_TOPBDII", sagrid_gromacs_TOPBDII.trim());
            request.setAttribute("sagrid_gromacs_WMS", sagrid_WMS);
            request.setAttribute("sagrid_gromacs_ETOKENSERVER", sagrid_gromacs_ETOKENSERVER.trim());
            request.setAttribute("sagrid_gromacs_MYPROXYSERVER", sagrid_gromacs_MYPROXYSERVER.trim());
            request.setAttribute("sagrid_gromacs_PORT", sagrid_gromacs_PORT.trim());
            request.setAttribute("sagrid_gromacs_ROBOTID", sagrid_gromacs_ROBOTID.trim());
            request.setAttribute("sagrid_gromacs_WEBDAV", sagrid_gromacs_WEBDAV.trim());
            request.setAttribute("sagrid_gromacs_ROLE", sagrid_gromacs_ROLE.trim());
            request.setAttribute("sagrid_gromacs_RENEWAL", sagrid_gromacs_RENEWAL);
            request.setAttribute("sagrid_gromacs_DISABLEVOMS", sagrid_gromacs_DISABLEVOMS);

            //request.setAttribute("gromacs_ENABLEINFRASTRUCTURE", gromacs_ENABLEINFRASTRUCTURE);
            request.setAttribute("gromacs_APPID", gromacs_APPID.trim());
            request.setAttribute("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
            request.setAttribute("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
            request.setAttribute("gromacs_OUTPUT_PATH", gromacs_OUTPUT_PATH.trim());
            request.setAttribute("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
            request.setAttribute("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }        

        if (see_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[4]="see";
            // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for the SEE VO
            String see_gromacs_INFRASTRUCTURE = portletPreferences.getValue("see_gromacs_INFRASTRUCTURE", "N/A");
            // Getting the GROMACS VONAME from the portlet preferences for the see VO
            see_gromacs_VONAME = portletPreferences.getValue("see_gromacs_VONAME", "N/A");
            // Getting the GROMACS TOPPBDII from the portlet preferences for the SEE VO
            see_gromacs_TOPBDII = portletPreferences.getValue("see_gromacs_TOPBDII", "N/A");
            // Getting the GROMACS WMS from the portlet preferences for the SEE VO
            String[] see_gromacs_WMS = portletPreferences.getValues("see_gromacs_WMS", new String[5]);
            // Getting the GROMACS ETOKENSERVER from the portlet preferences for the SEE VO
            String see_gromacs_ETOKENSERVER = portletPreferences.getValue("see_gromacs_ETOKENSERVER", "N/A");
            // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the SEE VO
            String see_gromacs_MYPROXYSERVER = portletPreferences.getValue("see_gromacs_MYPROXYSERVER", "N/A");
            // Getting the GROMACS PORT from the portlet preferences for the SEE VO
            String see_gromacs_PORT = portletPreferences.getValue("see_gromacs_PORT", "N/A");
            // Getting the GROMACS ROBOTID from the portlet preferences for the SEE VO
            String see_gromacs_ROBOTID = portletPreferences.getValue("see_gromacs_ROBOTID", "N/A");
            // Getting the GROMACS WEBDAV from the portlet preferences for the SEE VO
            String see_gromacs_WEBDAV = portletPreferences.getValue("see_gromacs_WEBDAV", "N/A");
            // Getting the GROMACS ROLE from the portlet preferences for the SEE VO
            String see_gromacs_ROLE = portletPreferences.getValue("see_gromacs_ROLE", "N/A");
            // Getting the GROMACS RENEWAL from the portlet preferences for the SEE VO
            String see_gromacs_RENEWAL = portletPreferences.getValue("see_gromacs_RENEWAL", "checked");
            // Getting the GROMACS DISABLEVOMS from the portlet preferences for the SEE VO
            String see_gromacs_DISABLEVOMS = portletPreferences.getValue("see_gromacs_DISABLEVOMS", "unchecked");              
            
            // Fetching all the WMS Endpoints for the SEE VO
            String see_WMS = "";
            if (see_gromacs_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (see_gromacs_WMS!=null) {
                    //log.info("length="+see_gromacs_WMS.length);
                    for (int i = 0; i < see_gromacs_WMS.length; i++)
                        if (!(see_gromacs_WMS[i].trim().equals("N/A")) ) 
                            see_WMS += see_gromacs_WMS[i] + " ";                        
                } else { log.info("WMS not set for SEE!"); see_gromacs_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("see_gromacs_INFRASTRUCTURE", see_gromacs_INFRASTRUCTURE.trim());
            request.setAttribute("see_gromacs_VONAME", see_gromacs_VONAME.trim());
            request.setAttribute("see_gromacs_TOPBDII", see_gromacs_TOPBDII.trim());
            request.setAttribute("see_gromacs_WMS", see_WMS);
            request.setAttribute("see_gromacs_ETOKENSERVER", see_gromacs_ETOKENSERVER.trim());
            request.setAttribute("see_gromacs_MYPROXYSERVER", see_gromacs_MYPROXYSERVER.trim());
            request.setAttribute("see_gromacs_PORT", see_gromacs_PORT.trim());
            request.setAttribute("see_gromacs_ROBOTID", see_gromacs_ROBOTID.trim());
            request.setAttribute("see_gromacs_WEBDAV", see_gromacs_WEBDAV.trim());
            request.setAttribute("see_gromacs_ROLE", see_gromacs_ROLE.trim());
            request.setAttribute("see_gromacs_RENEWAL", see_gromacs_RENEWAL);
            request.setAttribute("see_gromacs_DISABLEVOMS", see_gromacs_DISABLEVOMS);

            //request.setAttribute("gromacs_ENABLEINFRASTRUCTURE", gromacs_ENABLEINFRASTRUCTURE);
            request.setAttribute("gromacs_APPID", gromacs_APPID.trim());
            request.setAttribute("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
            request.setAttribute("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
            request.setAttribute("gromacs_OUTPUT_PATH", gromacs_OUTPUT_PATH.trim());
            request.setAttribute("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
            request.setAttribute("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }
        
        if (gisela_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
        {
            infras[5]="gisela";
            // Getting the GROMACS INFRASTRUCTURE from the portlet preferences for the GISELA VO
            String gisela_gromacs_INFRASTRUCTURE = portletPreferences.getValue("gisela_gromacs_INFRASTRUCTURE", "N/A");
            // Getting the GROMACS VONAME from the portlet preferences for the GISELA VO
            gisela_gromacs_VONAME = portletPreferences.getValue("gisela_gromacs_VONAME", "N/A");
            // Getting the GROMACS TOPPBDII from the portlet preferences for the GISELA VO
            gisela_gromacs_TOPBDII = portletPreferences.getValue("gisela_gromacs_TOPBDII", "N/A");
            // Getting the GROMACS WMS from the portlet preferences for the GISELA VO
            String[] gisela_gromacs_WMS = portletPreferences.getValues("gisela_gromacs_WMS", new String[5]);
            // Getting the GROMACS ETOKENSERVER from the portlet preferences for the GISELA VO
            String gisela_gromacs_ETOKENSERVER = portletPreferences.getValue("gisela_gromacs_ETOKENSERVER", "N/A");
            // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the GISELA VO
            String gisela_gromacs_MYPROXYSERVER = portletPreferences.getValue("gisela_gromacs_MYPROXYSERVER", "N/A");
            // Getting the GROMACS PORT from the portlet preferences for the GISELA VO
            String gisela_gromacs_PORT = portletPreferences.getValue("gisela_gromacs_PORT", "N/A");
            // Getting the GROMACS ROBOTID from the portlet preferences for the GISELA VO
            String gisela_gromacs_ROBOTID = portletPreferences.getValue("gisela_gromacs_ROBOTID", "N/A");
            // Getting the GROMACS WEBDAV from the portlet preferences for the GISELA VO
            String gisela_gromacs_WEBDAV = portletPreferences.getValue("gisela_gromacs_WEBDAV", "N/A");
            // Getting the GROMACS ROLE from the portlet preferences for the GISELA VO
            String gisela_gromacs_ROLE = portletPreferences.getValue("gisela_gromacs_ROLE", "N/A");
            // Getting the GROMACS RENEWAL from the portlet preferences for the GISELA VO
            String gisela_gromacs_RENEWAL = portletPreferences.getValue("gisela_gromacs_RENEWAL", "checked");
            // Getting the GROMACS DISABLEVOMS from the portlet preferences for the GISELA VO
            String gisela_gromacs_DISABLEVOMS = portletPreferences.getValue("gisela_gromacs_DISABLEVOMS", "unchecked");
            
            // Fetching all the WMS Endpoints for the GISELA VO
            String gisela_WMS = "";
            if (gisela_gromacs_ENABLEINFRASTRUCTURE.equals("checked")) {            
                if (gisela_gromacs_WMS!=null) {
                    //log.info("length="+gisela_gromacs_WMS.length);
                    for (int i = 0; i < gisela_gromacs_WMS.length; i++)
                        if (!(gisela_gromacs_WMS[i].trim().equals("N/A")) ) 
                            gisela_WMS += gisela_gromacs_WMS[i] + " ";                        
                } else { log.info("WMS not set for GISELA!"); gisela_gromacs_ENABLEINFRASTRUCTURE="unchecked"; }
            }
            
            // Save the portlet preferences
            request.setAttribute("gisela_gromacs_INFRASTRUCTURE", gisela_gromacs_INFRASTRUCTURE.trim());
            request.setAttribute("gisela_gromacs_VONAME", gisela_gromacs_VONAME.trim());
            request.setAttribute("gisela_gromacs_TOPBDII", gisela_gromacs_TOPBDII.trim());
            request.setAttribute("gisela_gromacs_WMS", gisela_WMS);
            request.setAttribute("gisela_gromacs_ETOKENSERVER", gisela_gromacs_ETOKENSERVER.trim());
            request.setAttribute("gisela_gromacs_MYPROXYSERVER", gisela_gromacs_MYPROXYSERVER.trim());
            request.setAttribute("gisela_gromacs_PORT", gisela_gromacs_PORT.trim());
            request.setAttribute("gisela_gromacs_ROBOTID", gisela_gromacs_ROBOTID.trim());
            request.setAttribute("gisela_gromacs_WEBDAV", gisela_gromacs_WEBDAV.trim());
            request.setAttribute("gisela_gromacs_ROLE", gisela_gromacs_ROLE.trim());
            request.setAttribute("gisela_gromacs_RENEWAL", gisela_gromacs_RENEWAL);
            request.setAttribute("gisela_gromacs_DISABLEVOMS", gisela_gromacs_DISABLEVOMS);

            //request.setAttribute("gromacs_ENABLEINFRASTRUCTURE", gromacs_ENABLEINFRASTRUCTURE);
            request.setAttribute("gromacs_APPID", gromacs_APPID.trim());
            request.setAttribute("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
            request.setAttribute("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
            request.setAttribute("gromacs_OUTPUT_PATH", gromacs_OUTPUT_PATH.trim());
            request.setAttribute("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
            request.setAttribute("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
            request.setAttribute("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
            request.setAttribute("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
            request.setAttribute("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
            request.setAttribute("SMTP_HOST", SMTP_HOST.trim());
            request.setAttribute("SENDER_MAIL", SENDER_MAIL.trim());
        }                
        
        // Save in the preferences the list of supported infrastructures 
        request.setAttribute("gromacs_ENABLEINFRASTRUCTURE", infras);

        HashMap<String,Properties> GPS_table = new HashMap<String, Properties>();
        HashMap<String,Properties> GPS_queue = new HashMap<String, Properties>();

        // ********************************************************
        List<String> CEqueues_lato = null;        
        List<String> CEqueues_garuda = null;
        List<String> CEqueues_eumed = null;
        List<String> CEqueues_sagrid = null;
        List<String> CEqueues_see = null;
        List<String> CEqueues_gisela = null;
        
        List<String> CEs_list_lato = null;        
        List<String> CEs_list_garuda = null;        
        List<String> CEs_list_eumed = null;
        List<String> CEs_list_sagrid = null;
        List<String> CEs_list_see = null;
        List<String> CEs_list_gisela = null;
        
        List<String> CEs_list_TOT = new ArrayList<String>();
        List<String> CEs_queue_TOT = new ArrayList<String>();
        
        BDII bdii = null;
        
        String[] SOFTWARE_LIST = gromacs_SOFTWARE.split(",");
        
        // Scanning the list of resources publishing the SW TAG(s)
        for(String SOFTWARE: SOFTWARE_LIST)
        {

            try {
                if (lato_gromacs_ENABLEINFRASTRUCTURE.equals("checked") && 
                   (!lato_gromacs_PASSWD.equals("N/A")) ) 
                {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                            log.info("-----*FETCHING*THE*<LATO>*RESOURCES*-----");
                    
                    CEs_list_lato = new ArrayList();                    
                    CEqueues_lato = new ArrayList();
                    
                    // Fetching all the WMS Endpoints for LATO                    
                    if (lato_gromacs_WMS!=null) {
                        for (int i = 0; i < lato_gromacs_WMS.length; i++)
                            if (!(lato_gromacs_WMS[i].trim().equals("N/A")) ) {                                    
                                CEqueues_lato.add(lato_gromacs_WMS[i].trim());
                                CEs_list_lato.add(lato_gromacs_WMS[i].trim().replace("ssh://", ""));                                    
                            }
                    } 
                }
             
                //=========================================================
                // IMPORTANT: THIS FIX IS ONLY TO SHOW GARUDA SITES 
                //            IN THE GOOGLE MAP                
                //=========================================================
                if (garuda_gromacs_ENABLEINFRASTRUCTURE.equals("checked") && 
                   (!garuda_gromacs_TOPBDII.equals("N/A")) ) 
                {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                            log.info("-----*FETCHING*THE*<GARUDA>*RESOURCES*-----");
                    
                    CEs_list_garuda = new ArrayList();
                    CEs_list_garuda.add("xn03.ctsf.cdacb.in");
                    
                    CEqueues_garuda = new ArrayList();
                    CEqueues_garuda.add("gatekeeper://xn03.ctsf.cdacb.in:8443/GW");
                }
                
                if (eumed_gromacs_ENABLEINFRASTRUCTURE.equals("checked") && 
                   (!eumed_gromacs_TOPBDII.equals("N/A")) ) 
                {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("-----*FETCHING*THE*<EUMED>*RESOURCES*-----");
                    
                    bdii = new BDII(new URI(eumed_gromacs_TOPBDII));
                    CEs_list_eumed = 
                            getListofCEForSoftwareTag(eumed_gromacs_VONAME,
                                                      eumed_gromacs_TOPBDII,
                                                      SOFTWARE);
                    
                    CEqueues_eumed = 
                            bdii.queryCEQueues(eumed_gromacs_VONAME);
                }
                
                if (sagrid_gromacs_ENABLEINFRASTRUCTURE.equals("checked") && 
                   (!sagrid_gromacs_TOPBDII.equals("N/A")) ) 
                {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("-----*FETCHING*THE*<SAGRID>*RESOURCES*-----");
                    
                    bdii = new BDII(new URI(sagrid_gromacs_TOPBDII));
                    CEs_list_sagrid = 
                            getListofCEForSoftwareTag(sagrid_gromacs_VONAME,
                                                      sagrid_gromacs_TOPBDII,
                                                      SOFTWARE);
                    
                    CEqueues_sagrid = 
                            bdii.queryCEQueues(sagrid_gromacs_VONAME);
                }
                
                if (see_gromacs_ENABLEINFRASTRUCTURE.equals("checked") &&
                   (!see_gromacs_TOPBDII.equals("N/A")) ) 
                {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("-----*FETCHING*THE*<SEE>*RESOURCES*-----");
                    
                    bdii = new BDII(new URI(see_gromacs_TOPBDII));
                    CEs_list_see = 
                            getListofCEForSoftwareTag(see_gromacs_VONAME,
                                                      see_gromacs_TOPBDII,
                                                      SOFTWARE);
                    
                    CEqueues_see = 
                            bdii.queryCEQueues(see_gromacs_VONAME);
                }
                
                if (gisela_gromacs_ENABLEINFRASTRUCTURE.equals("checked") &&
                   (!gisela_gromacs_TOPBDII.equals("N/A")) ) 
                {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("-----*FETCHING*THE*<GISELA>*RESOURCES*-----");
                    
                    bdii = new BDII(new URI(gisela_gromacs_TOPBDII));
                    CEs_list_gisela = 
                            getListofCEForSoftwareTag(gisela_gromacs_VONAME,
                                                      gisela_gromacs_TOPBDII,
                                                      SOFTWARE);
                    
                    CEqueues_gisela = 
                            bdii.queryCEQueues(gisela_gromacs_VONAME);
                }
                
                // Merging the list of CEs and queues                
                if (lato_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                        CEs_list_TOT.addAll(CEs_list_lato);
                if (garuda_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))                        
                        CEs_list_TOT.addAll(CEs_list_garuda);
                if (eumed_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                        CEs_list_TOT.addAll(CEs_list_eumed);
                if (sagrid_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                        CEs_list_TOT.addAll(CEs_list_sagrid);
                if (see_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                        CEs_list_TOT.addAll(CEs_list_see);
                if (gisela_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                        CEs_list_TOT.addAll(CEs_list_gisela);
                                
                if (lato_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_lato);
                if (garuda_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_garuda);
                if (eumed_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_eumed);
                if (sagrid_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_sagrid);
                if (see_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_see);
                if (gisela_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                    CEs_queue_TOT.addAll(CEqueues_gisela);
                
            } catch (URISyntaxException ex) {
               Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException e) {}
        } // fine list SW tag
                                
        //=========================================================
        // IMPORTANT: INSTANCIATE THE UsersTrackingDBInterface
        //            CLASS USING THE EMPTY CONSTRUCTOR WHEN
        //            WHEN THE PORTLET IS DEPLOYED IN PRODUCTION!!!
        //=========================================================
        /*UsersTrackingDBInterface DBInterface =
            new UsersTrackingDBInterface(
                TRACKING_DB_HOSTNAME.trim(),
                TRACKING_DB_USERNAME.trim(),
                TRACKING_DB_PASSWORD.trim());*/
                
         UsersTrackingDBInterface DBInterface =
            new UsersTrackingDBInterface();
                    
         if ( (CEs_list_TOT != null) && (!CEs_list_TOT.isEmpty()) )
         {
            log.info("NOT EMPTY LIST!");
            // Fetching the list of CEs publushing the SW
            for (String CE:CEs_list_TOT) 
            {
                log.info("Fetching the CE="+CE);
                Properties coordinates = new Properties();
                Properties queue = new Properties();

                float coords[] = DBInterface.getCECoordinate(CE);                        

                String GPS_LAT = Float.toString(coords[0]);
                String GPS_LNG = Float.toString(coords[1]);

                coordinates.setProperty("LAT", GPS_LAT);
                coordinates.setProperty("LNG", GPS_LNG);

                // Fetching the Queues
                for (String CEqueue:CEs_queue_TOT)
                    if (CEqueue.contains(CE))
                        queue.setProperty("QUEUE", CEqueue);
                        
                // Saving the GPS location in a Java HashMap
                GPS_table.put(CE, coordinates);

                // Saving the queue in a Java HashMap
                GPS_queue.put(CE, queue);
            }
         } else log.info ("EMPTY LIST!");

         // Checking the HashMap
         Set set = GPS_table.entrySet();
         Iterator iter = set.iterator();
         
         while ( iter.hasNext() )
         {
            Map.Entry entry = (Map.Entry)iter.next();
            log.info(" - GPS location of the CE " 
                     + entry.getKey() 
                     + " => " 
                     + entry.getValue());
         }

         // Checking the HashMap
         set = GPS_queue.entrySet();
         iter = set.iterator();
         while ( iter.hasNext() )
         {
            Map.Entry entry = (Map.Entry)iter.next();
            log.info(" - Queue " 
                     + entry.getKey() 
                     + " => " 
                     + entry.getValue());
         }

         Gson gson = new GsonBuilder().create();
         request.setAttribute ("GPS_table", gson.toJson(GPS_table));
         request.setAttribute ("GPS_queue", gson.toJson(GPS_queue));

         // ********************************************************
         dispatcher = getPortletContext().getRequestDispatcher("/view.jsp");       
         dispatcher.include(request, response);
    }

    // The init method will be called when installing for the first time the portlet
    // This is the right time to setup the default values into the preferences
    @Override
    public void init() throws PortletException {
        super.init();
    }

    @Override
    public void processAction(ActionRequest request,
                              ActionResponse response)
                throws PortletException, IOException 
    {
        try {
            String action = "";

            // Getting the action to be processed from the request
            action = request.getParameter("ActionEvent");

            // Determine the username and the email address
            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);        
            User user = themeDisplay.getUser();
            
            String username = user.getScreenName();
            String emailAddress = user.getDisplayEmailAddress();
            
            Company company = PortalUtil.getCompany(request);
                String portal = company.getName();

            PortletPreferences portletPreferences =
                    (PortletPreferences) request.getPreferences();

            if (action.equals("CONFIG_GROMACS_PORTLET")) {
                log.info("\nPROCESS ACTION => " + action);
                
                // Getting the GROMACS APPID from the portlet request
                String gromacs_APPID = request.getParameter("gromacs_APPID");
                // Getting the LOGLEVEL from the portlet request
                String gromacs_LOGLEVEL = request.getParameter("gromacs_LOGLEVEL");
                // Getting the GROMACS_METADATA_HOST from the portlet request
                String gromacs_METADATA_HOST = request.getParameter("gromacs_METADATA_HOST");
                // Getting the GROMACS OUTPUT_PATH from the portlet request
                String gromacs_OUTPUT_PATH = request.getParameter("gromacs_OUTPUT_PATH");
                // Getting the GROMACS SOFTWARE from the portlet request
                String gromacs_SOFTWARE = request.getParameter("gromacs_SOFTWARE");
                // Getting the GROMACS LOCAL_PROXY from the portlet request
                String gromacs_LOCAL_PROXY = request.getParameter("gromacs_LOCAL_PROXY");
                // Getting the TRACKING_DB_HOSTNAME from the portlet request
                String TRACKING_DB_HOSTNAME = request.getParameter("TRACKING_DB_HOSTNAME");
                // Getting the TRACKING_DB_USERNAME from the portlet request
                String TRACKING_DB_USERNAME = request.getParameter("TRACKING_DB_USERNAME");
                // Getting the TRACKING_DB_PASSWORD from the portlet request
                String TRACKING_DB_PASSWORD = request.getParameter("TRACKING_DB_PASSWORD");
                // Getting the SMTP_HOST from the portlet request
                String SMTP_HOST = request.getParameter("SMTP_HOST");
                // Getting the SENDER_MAIL from the portlet request
                String SENDER_MAIL = request.getParameter("SENDER_MAIL");
                String[] infras = new String[6];
                
                String lato_gromacs_ENABLEINFRASTRUCTURE = "unchecked";
                String garuda_gromacs_ENABLEINFRASTRUCTURE = "unchecked";
                String eumed_gromacs_ENABLEINFRASTRUCTURE = "unchecked";
                String sagrid_gromacs_ENABLEINFRASTRUCTURE = "unchecked";
                String see_gromacs_ENABLEINFRASTRUCTURE = "unchecked";
                String gisela_gromacs_ENABLEINFRASTRUCTURE = "unchecked";
                
                String[] gromacs_INFRASTRUCTURES = request.getParameterValues("gromacs_ENABLEINFRASTRUCTURES");         

                if (gromacs_INFRASTRUCTURES != null) {
                    Arrays.sort(gromacs_INFRASTRUCTURES);
                    lato_gromacs_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(gromacs_INFRASTRUCTURES, "lato") >= 0 ? "checked" : "unchecked";
                    garuda_gromacs_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(gromacs_INFRASTRUCTURES, "garuda") >= 0 ? "checked" : "unchecked";
                    eumed_gromacs_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(gromacs_INFRASTRUCTURES, "eumed") >= 0 ? "checked" : "unchecked";
                    sagrid_gromacs_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(gromacs_INFRASTRUCTURES, "sagrid") >= 0 ? "checked" : "unchecked";
                    see_gromacs_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(gromacs_INFRASTRUCTURES, "see") >= 0 ? "checked" : "unchecked";
                    gisela_gromacs_ENABLEINFRASTRUCTURE =
                        Arrays.binarySearch(gromacs_INFRASTRUCTURES, "gisela") >= 0 ? "checked" : "unchecked";
                }           
                
                if (lato_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[0]="lato";
                     // Getting the GROMACS INFRASTRUCTURE from the portlet request for LATO
                    String lato_gromacs_INFRASTRUCTURE = request.getParameter("lato_gromacs_INFRASTRUCTURE");
                    // Getting the GROMACS VONAME from the portlet request for LATO
                    String lato_gromacs_LOGIN = request.getParameter("lato_gromacs_LOGIN");
                    // Getting the GROMACS TOPBDII from the portlet request for LATO
                    String lato_gromacs_PASSWD = request.getParameter("lato_gromacs_PASSWD");
                    // Getting the GROMACS WMS from the portlet request for LATO
                    String[] lato_gromacs_WMS = request.getParameterValues("lato_gromacs_WMS");
                    // Getting the GROMACS ETOKENSERVER from the portlet request for LATO
                    String lato_gromacs_ETOKENSERVER = request.getParameter("lato_gromacs_ETOKENSERVER");
                    // Getting the GROMACS MYPROXYSERVER from the portlet request for LATO
                    String lato_gromacs_MYPROXYSERVER = request.getParameter("lato_gromacs_MYPROXYSERVER");
                    // Getting the GROMACS PORT from the portlet request for LATO
                    String lato_gromacs_PORT = request.getParameter("lato_gromacs_PORT");
                    // Getting the GROMACS ROBOTID from the portlet request for LATO
                    String lato_gromacs_ROBOTID = request.getParameter("lato_gromacs_ROBOTID");
                    // Getting the GROMACS WEBDAV from the portlet request for LATO
                    String lato_gromacs_WEBDAV = request.getParameter("lato_gromacs_WEBDAV");
                    // Getting the GROMACS ROLE from the portlet request for LATO
                    String lato_gromacs_ROLE = request.getParameter("lato_gromacs_ROLE");
                    // Getting the GROMACS OPTIONS from the portlet request for LATO
                    String[] lato_gromacs_OPTIONS = request.getParameterValues("lato_gromacs_OPTIONS");

                    String lato_gromacs_RENEWAL = "";
                    String lato_gromacs_DISABLEVOMS = "";

                    if (lato_gromacs_OPTIONS == null){
                        lato_gromacs_RENEWAL = "checked";
                        lato_gromacs_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(lato_gromacs_OPTIONS);
                        // Getting the GROMACS RENEWAL from the portlet preferences for LATO
                        lato_gromacs_RENEWAL = Arrays.binarySearch(lato_gromacs_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Getting the GROMACS DISABLEVOMS from the portlet preferences for LATO
                        lato_gromacs_DISABLEVOMS = Arrays.binarySearch(lato_gromacs_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < lato_gromacs_WMS.length; i++)
                        if ( lato_gromacs_WMS[i]!=null && (!lato_gromacs_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] lato_gromacs_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        lato_gromacs_WMS_trimmed[i]=lato_gromacs_WMS[i].trim();
                        log.info ("\n\nLATO [" + i + "] WMS=[" + lato_gromacs_WMS_trimmed[i] + "]");
                    }
                    
                    // Set the portlet preferences
                    portletPreferences.setValue("lato_gromacs_INFRASTRUCTURE", lato_gromacs_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("lato_gromacs_LOGIN", lato_gromacs_LOGIN.trim());
                    portletPreferences.setValue("lato_gromacs_PASSWD", lato_gromacs_PASSWD.trim());
                    portletPreferences.setValues("lato_gromacs_WMS", lato_gromacs_WMS_trimmed);
                    portletPreferences.setValue("lato_gromacs_ETOKENSERVER", lato_gromacs_ETOKENSERVER.trim());
                    portletPreferences.setValue("lato_gromacs_MYPROXYSERVER", lato_gromacs_MYPROXYSERVER.trim());
                    portletPreferences.setValue("lato_gromacs_PORT", lato_gromacs_PORT.trim());
                    portletPreferences.setValue("lato_gromacs_ROBOTID", lato_gromacs_ROBOTID.trim());
                    portletPreferences.setValue("lato_gromacs_WEBDAV", lato_gromacs_WEBDAV.trim());
                    portletPreferences.setValue("lato_gromacs_ROLE", lato_gromacs_ROLE.trim());
                    portletPreferences.setValue("lato_gromacs_RENEWAL", lato_gromacs_RENEWAL);
                    portletPreferences.setValue("lato_gromacs_DISABLEVOMS", lato_gromacs_DISABLEVOMS);                
                    
                    portletPreferences.setValue("gromacs_APPID", gromacs_APPID.trim());
                    portletPreferences.setValue("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
                    portletPreferences.setValue("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
                    portletPreferences.setValue("gromacs_OUTPUT_PATH", gromacs_OUTPUT_PATH.trim());
                    portletPreferences.setValue("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
                    portletPreferences.setValue("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the GROMACS portlet preferences ..."
                        + "\nlato_gromacs_INFRASTRUCTURE: " + lato_gromacs_INFRASTRUCTURE
                        + "\nlato_gromacs_LOGIN: " + lato_gromacs_LOGIN
                        + "\nlato_gromacs_PASSWD: " + lato_gromacs_PASSWD                    
                        + "\nlato_gromacs_ETOKENSERVER: " + lato_gromacs_ETOKENSERVER
                        + "\nlato_gromacs_MYPROXYSERVER: " + lato_gromacs_MYPROXYSERVER
                        + "\nlato_gromacs_PORT: " + lato_gromacs_PORT
                        + "\nlato_gromacs_ROBOTID: " + lato_gromacs_ROBOTID
                        + "\nlato_gromacs_WEBDAV: " + lato_gromacs_WEBDAV
                        + "\nlato_gromacs_ROLE: " + lato_gromacs_ROLE
                        + "\nlato_gromacs_RENEWAL: " + lato_gromacs_RENEWAL
                        + "\nlato_gromacs_DISABLEVOMS: " + lato_gromacs_DISABLEVOMS
                            
                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + "lato"
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOSTL: " + gromacs_METADATA_HOST
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }

                if (garuda_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[1]="garuda";
                     // Getting the GROMACS INFRASTRUCTURE from the portlet request for the GARUDA VO
                    String garuda_gromacs_INFRASTRUCTURE = request.getParameter("garuda_gromacs_INFRASTRUCTURE");
                    // Getting the GROMACS VONAME from the portlet request for the GARUDA VO
                    String garuda_gromacs_VONAME = request.getParameter("garuda_gromacs_VONAME");
                    // Getting the GROMACS TOPBDII from the portlet request for the GARUDA VO
                    String garuda_gromacs_TOPBDII = request.getParameter("garuda_gromacs_TOPBDII");
                    // Getting the GROMACS WMS from the portlet request for the GARUDA VO
                    String[] garuda_gromacs_WMS = request.getParameterValues("garuda_gromacs_WMS");
                    // Getting the GROMACS ETOKENSERVER from the portlet request for the GARUDA VO
                    String garuda_gromacs_ETOKENSERVER = request.getParameter("garuda_gromacs_ETOKENSERVER");
                    // Getting the GROMACS MYPROXYSERVER from the portlet request for the GARUDA VO
                    String garuda_gromacs_MYPROXYSERVER = request.getParameter("garuda_gromacs_MYPROXYSERVER");
                    // Getting the GROMACS PORT from the portlet request for the GARUDA VO
                    String garuda_gromacs_PORT = request.getParameter("garuda_gromacs_PORT");
                    // Getting the GROMACS ROBOTID from the portlet request for the GARUDA VO
                    String garuda_gromacs_ROBOTID = request.getParameter("garuda_gromacs_ROBOTID");
                    // Getting the GROMACS WEBDAV from the portlet request for the GARUDA VO
                    String garuda_gromacs_WEBDAV = request.getParameter("garuda_gromacs_WEBDAV");
                    // Getting the GROMACS ROLE from the portlet request for the GARUDA VO
                    String garuda_gromacs_ROLE = request.getParameter("garuda_gromacs_ROLE");
                    // Getting the GROMACS OPTIONS from the portlet request for the GARUDA VO
                    String[] garuda_gromacs_OPTIONS = request.getParameterValues("garuda_gromacs_OPTIONS");

                    String garuda_gromacs_RENEWAL = "";
                    String garuda_gromacs_DISABLEVOMS = "";

                    if (garuda_gromacs_OPTIONS == null){
                        garuda_gromacs_RENEWAL = "checked";
                        garuda_gromacs_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(garuda_gromacs_OPTIONS);
                        // Getting the GROMACS RENEWAL from the portlet preferences for the GARUDA VO
                        garuda_gromacs_RENEWAL = Arrays.binarySearch(garuda_gromacs_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Getting the GROMACS DISABLEVOMS from the portlet preferences for the GARUDA VO
                        garuda_gromacs_DISABLEVOMS = Arrays.binarySearch(garuda_gromacs_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < garuda_gromacs_WMS.length; i++)
                        if ( garuda_gromacs_WMS[i]!=null && (!garuda_gromacs_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] garuda_gromacs_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        garuda_gromacs_WMS_trimmed[i]=garuda_gromacs_WMS[i].trim();
                        log.info ("\n\nLATO [" + i + "] WMS=[" + garuda_gromacs_WMS_trimmed[i] + "]");
                    }
                    
                    // Set the portlet preferences
                    portletPreferences.setValue("garuda_gromacs_INFRASTRUCTURE", garuda_gromacs_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("garuda_gromacs_VONAME", garuda_gromacs_VONAME.trim());
                    portletPreferences.setValue("garuda_gromacs_TOPBDII", garuda_gromacs_TOPBDII.trim());
                    portletPreferences.setValues("garuda_gromacs_WMS", garuda_gromacs_WMS_trimmed);
                    portletPreferences.setValue("garuda_gromacs_ETOKENSERVER", garuda_gromacs_ETOKENSERVER.trim());
                    portletPreferences.setValue("garuda_gromacs_MYPROXYSERVER", garuda_gromacs_MYPROXYSERVER.trim());
                    portletPreferences.setValue("garuda_gromacs_PORT", garuda_gromacs_PORT.trim());
                    portletPreferences.setValue("garuda_gromacs_ROBOTID", garuda_gromacs_ROBOTID.trim());
                    portletPreferences.setValue("garuda_gromacs_WEBDAV", garuda_gromacs_WEBDAV.trim());
                    portletPreferences.setValue("garuda_gromacs_ROLE", garuda_gromacs_ROLE.trim());
                    portletPreferences.setValue("garuda_gromacs_RENEWAL", garuda_gromacs_RENEWAL);
                    portletPreferences.setValue("garuda_gromacs_DISABLEVOMS", garuda_gromacs_DISABLEVOMS);                
                    
                    portletPreferences.setValue("gromacs_APPID", gromacs_APPID.trim());
                    portletPreferences.setValue("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
                    portletPreferences.setValue("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
                    portletPreferences.setValue("gromacs_OUTPUT_PATH", gromacs_OUTPUT_PATH.trim());
                    portletPreferences.setValue("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
                    portletPreferences.setValue("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the GROMACS portlet preferences ..."
                        + "\ngaruda_gromacs_INFRASTRUCTURE: " + garuda_gromacs_INFRASTRUCTURE
                        + "\ngaruda_gromacs_VONAME: " + garuda_gromacs_VONAME
                        + "\ngaruda_gromacs_TOPBDII: " + garuda_gromacs_TOPBDII                    
                        + "\ngaruda_gromacs_ETOKENSERVER: " + garuda_gromacs_ETOKENSERVER
                        + "\ngaruda_gromacs_MYPROXYSERVER: " + garuda_gromacs_MYPROXYSERVER
                        + "\ngaruda_gromacs_PORT: " + garuda_gromacs_PORT
                        + "\ngaruda_gromacs_ROBOTID: " + garuda_gromacs_ROBOTID
                        + "\ngaruda_gromacs_WEBDAV: " + garuda_gromacs_WEBDAV
                        + "\ngaruda_gromacs_ROLE: " + garuda_gromacs_ROLE
                        + "\ngaruda_gromacs_RENEWAL: " + garuda_gromacs_RENEWAL
                        + "\ngaruda_gromacs_DISABLEVOMS: " + garuda_gromacs_DISABLEVOMS
                            
                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + "garuda"
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }

                if (eumed_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[2]="eumed";
                    // Getting the GROMACS INFRASTRUCTURE from the portlet request for the EUMED VO
                    String eumed_gromacs_INFRASTRUCTURE = request.getParameter("eumed_gromacs_INFRASTRUCTURE");
                    // Getting the GROMACS VONAME from the portlet request for the EUMED VO
                    String eumed_gromacs_VONAME = request.getParameter("eumed_gromacs_VONAME");
                    // Getting the GROMACS TOPBDII from the portlet request for the EUMED VO
                    String eumed_gromacs_TOPBDII = request.getParameter("eumed_gromacs_TOPBDII");
                    // Getting the GROMACS WMS from the portlet request for the EUMED VO
                    String[] eumed_gromacs_WMS = request.getParameterValues("eumed_gromacs_WMS");
                    // Getting the GROMACS ETOKENSERVER from the portlet request for the EUMED VO
                    String eumed_gromacs_ETOKENSERVER = request.getParameter("eumed_gromacs_ETOKENSERVER");
                    // Getting the GROMACS MYPROXYSERVER from the portlet request for the EUMED VO
                    String eumed_gromacs_MYPROXYSERVER = request.getParameter("eumed_gromacs_MYPROXYSERVER");
                    // Getting the GROMACS PORT from the portlet request for the EUMED VO
                    String eumed_gromacs_PORT = request.getParameter("eumed_gromacs_PORT");
                    // Getting the GROMACS ROBOTID from the portlet request for the EUMED VO
                    String eumed_gromacs_ROBOTID = request.getParameter("eumed_gromacs_ROBOTID");
                    // Getting the GROMACS WEBDAV from the portlet request for the EUMED VO
                    String eumed_gromacs_WEBDAV = request.getParameter("eumed_gromacs_WEBDAV");
                    // Getting the GROMACS ROLE from the portlet request for the EUMED VO
                    String eumed_gromacs_ROLE = request.getParameter("eumed_gromacs_ROLE");
                    // Getting the GROMACS OPTIONS from the portlet request for the EUMED VO
                    String[] eumed_gromacs_OPTIONS = request.getParameterValues("eumed_gromacs_OPTIONS");

                    String eumed_gromacs_RENEWAL = "";
                    String eumed_gromacs_DISABLEVOMS = "";

                    if (eumed_gromacs_OPTIONS == null){
                        eumed_gromacs_RENEWAL = "checked";
                        eumed_gromacs_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(eumed_gromacs_OPTIONS);
                        // Getting the GROMACS RENEWAL from the portlet preferences for the EUMED VO
                        eumed_gromacs_RENEWAL = Arrays.binarySearch(eumed_gromacs_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Getting the GROMACS DISABLEVOMS from the portlet preferences for the GARUDA VO
                        eumed_gromacs_DISABLEVOMS = Arrays.binarySearch(eumed_gromacs_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < eumed_gromacs_WMS.length; i++)
                        if ( eumed_gromacs_WMS[i]!=null && (!eumed_gromacs_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] eumed_gromacs_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        eumed_gromacs_WMS_trimmed[i]=eumed_gromacs_WMS[i].trim();
                        log.info ("\n\nEUMED [" + i + "] WMS=[" + eumed_gromacs_WMS_trimmed[i] + "]");
                    }
                    
                    // Set the portlet preferences
                    portletPreferences.setValue("eumed_gromacs_INFRASTRUCTURE", eumed_gromacs_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("eumed_gromacs_VONAME", eumed_gromacs_VONAME.trim());
                    portletPreferences.setValue("eumed_gromacs_TOPBDII", eumed_gromacs_TOPBDII.trim());
                    portletPreferences.setValues("eumed_gromacs_WMS", eumed_gromacs_WMS_trimmed);
                    portletPreferences.setValue("eumed_gromacs_ETOKENSERVER", eumed_gromacs_ETOKENSERVER.trim());
                    portletPreferences.setValue("eumed_gromacs_MYPROXYSERVER", eumed_gromacs_MYPROXYSERVER.trim());
                    portletPreferences.setValue("eumed_gromacs_PORT", eumed_gromacs_PORT.trim());
                    portletPreferences.setValue("eumed_gromacs_ROBOTID", eumed_gromacs_ROBOTID.trim());
                    portletPreferences.setValue("eumed_gromacs_WEBDAV", eumed_gromacs_WEBDAV.trim());
                    portletPreferences.setValue("eumed_gromacs_ROLE", eumed_gromacs_ROLE.trim());
                    portletPreferences.setValue("eumed_gromacs_RENEWAL", eumed_gromacs_RENEWAL);
                    portletPreferences.setValue("eumed_gromacs_DISABLEVOMS", eumed_gromacs_DISABLEVOMS); 
                    
                    portletPreferences.setValue("gromacs_APPID", gromacs_APPID.trim());
                    portletPreferences.setValue("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
                    portletPreferences.setValue("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
                    portletPreferences.setValue("gromacs_OUTPATH_PATH", gromacs_OUTPUT_PATH.trim());
                    portletPreferences.setValue("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
                    portletPreferences.setValue("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the GROMACS portlet preferences ..."                    
                        + "\n\neumed_gromacs_INFRASTRUCTURE: " + eumed_gromacs_INFRASTRUCTURE
                        + "\neumed_gromacs_VONAME: " + eumed_gromacs_VONAME
                        + "\neumed_gromacs_TOPBDII: " + eumed_gromacs_TOPBDII                    
                        + "\neumed_gromacs_ETOKENSERVER: " + eumed_gromacs_ETOKENSERVER
                        + "\neumed_gromacs_MYPROXYSERVER: " + eumed_gromacs_MYPROXYSERVER
                        + "\neumed_gromacs_PORT: " + eumed_gromacs_PORT
                        + "\neumed_gromacs_ROBOTID: " + eumed_gromacs_ROBOTID
                        + "\neumed_gromacs_WEBDAV: " + eumed_gromacs_WEBDAV
                        + "\neumed_gromacs_ROLE: " + eumed_gromacs_ROLE
                        + "\neumed_gromacs_RENEWAL: " + eumed_gromacs_RENEWAL
                        + "\neumed_gromacs_DISABLEVOMS: " + eumed_gromacs_DISABLEVOMS

                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + "eumed"
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }                                
                
                if (sagrid_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[3]="sagrid";
                    // Getting the GROMACS INFRASTRUCTURE from the portlet request for the SAGRID VO
                    String sagrid_gromacs_INFRASTRUCTURE = request.getParameter("sagrid_gromacs_INFRASTRUCTURE");
                    // Getting the GROMACS VONAME from the portlet request for the SAGRID VO
                    String sagrid_gromacs_VONAME = request.getParameter("sagrid_gromacs_VONAME");
                    // Getting the GROMACS TOPBDII from the portlet request for the SAGRID VO
                    String sagrid_gromacs_TOPBDII = request.getParameter("sagrid_gromacs_TOPBDII");
                    // Getting the GROMACS WMS from the portlet request for the SAGRID VO
                    String[] sagrid_gromacs_WMS = request.getParameterValues("sagrid_gromacs_WMS");
                    // Getting the GROMACS ETOKENSERVER from the portlet request for the SAGRID VO
                    String sagrid_gromacs_ETOKENSERVER = request.getParameter("sagrid_gromacs_ETOKENSERVER");
                    // Getting the GROMACS MYPROXYSERVER from the portlet request for the SAGRID VO
                    String sagrid_gromacs_MYPROXYSERVER = request.getParameter("sagrid_gromacs_MYPROXYSERVER");
                    // Getting the GROMACS PORT from the portlet request for the SAGRID VO
                    String sagrid_gromacs_PORT = request.getParameter("sagrid_gromacs_PORT");
                    // Getting the GROMACS ROBOTID from the portlet request for the SAGRID VO
                    String sagrid_gromacs_ROBOTID = request.getParameter("sagrid_gromacs_ROBOTID");
                    // Getting the GROMACS WEBDAV from the portlet request for the SAGRID VO
                    String sagrid_gromacs_WEBDAV = request.getParameter("sagrid_gromacs_WEBDAV");
                    // Getting the GROMACS ROLE from the portlet request for the SAGRID VO
                    String sagrid_gromacs_ROLE = request.getParameter("sagrid_gromacs_ROLE");
                    // Getting the GROMACS OPTIONS from the portlet request for the SAGRID VO
                    String[] sagrid_gromacs_OPTIONS = request.getParameterValues("sagrid_gromacs_OPTIONS");

                    String sagrid_gromacs_RENEWAL = "";
                    String sagrid_gromacs_DISABLEVOMS = "";

                    if (sagrid_gromacs_OPTIONS == null){
                        sagrid_gromacs_RENEWAL = "checked";
                        sagrid_gromacs_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(sagrid_gromacs_OPTIONS);
                        // Getting the GROMACS RENEWAL from the portlet preferences for the SAGRID VO
                        sagrid_gromacs_RENEWAL = Arrays.binarySearch(sagrid_gromacs_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Getting the GROMACS DISABLEVOMS from the portlet preferences for the SAGRID VO
                        sagrid_gromacs_DISABLEVOMS = Arrays.binarySearch(sagrid_gromacs_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < sagrid_gromacs_WMS.length; i++)
                        if ( sagrid_gromacs_WMS[i]!=null && (!sagrid_gromacs_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] sagrid_gromacs_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        sagrid_gromacs_WMS_trimmed[i]=sagrid_gromacs_WMS[i].trim();
                        log.info ("\n\nSAGRID [" + i + "] WMS=[" + sagrid_gromacs_WMS_trimmed[i] + "]");
                    }
                    
                    // Set the portlet preferences
                    portletPreferences.setValue("sagrid_gromacs_INFRASTRUCTURE", sagrid_gromacs_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("sagrid_gromacs_VONAME", sagrid_gromacs_VONAME.trim());
                    portletPreferences.setValue("sagrid_gromacs_TOPBDII", sagrid_gromacs_TOPBDII.trim());
                    portletPreferences.setValues("sagrid_gromacs_WMS", sagrid_gromacs_WMS_trimmed);
                    portletPreferences.setValue("sagrid_gromacs_ETOKENSERVER", sagrid_gromacs_ETOKENSERVER.trim());
                    portletPreferences.setValue("sagrid_gromacs_MYPROXYSERVER", sagrid_gromacs_MYPROXYSERVER.trim());
                    portletPreferences.setValue("sagrid_gromacs_PORT", sagrid_gromacs_PORT.trim());
                    portletPreferences.setValue("sagrid_gromacs_ROBOTID", sagrid_gromacs_ROBOTID.trim());
                    portletPreferences.setValue("sagrid_gromacs_WEBDAV", sagrid_gromacs_WEBDAV.trim());
                    portletPreferences.setValue("sagrid_gromacs_ROLE", sagrid_gromacs_ROLE.trim());
                    portletPreferences.setValue("sagrid_gromacs_RENEWAL", sagrid_gromacs_RENEWAL);
                    portletPreferences.setValue("sagrid_gromacs_DISABLEVOMS", sagrid_gromacs_DISABLEVOMS); 
                    
                    portletPreferences.setValue("gromacs_APPID", gromacs_APPID.trim());
                    portletPreferences.setValue("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
                    portletPreferences.setValue("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
                    portletPreferences.setValue("gromacs_OUTPATH_PATH", gromacs_OUTPUT_PATH.trim());
                    portletPreferences.setValue("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
                    portletPreferences.setValue("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the GROMACS portlet preferences ..."                    
                        + "\n\nsagrid_gromacs_INFRASTRUCTURE: " + sagrid_gromacs_INFRASTRUCTURE
                        + "\nsagrid_gromacs_VONAME: " + sagrid_gromacs_VONAME
                        + "\nsagrid_gromacs_TOPBDII: " + sagrid_gromacs_TOPBDII                    
                        + "\nsagrid_gromacs_ETOKENSERVER: " + sagrid_gromacs_ETOKENSERVER
                        + "\nsagrid_gromacs_MYPROXYSERVER: " + sagrid_gromacs_MYPROXYSERVER
                        + "\nsagrid_gromacs_PORT: " + sagrid_gromacs_PORT
                        + "\nsagrid_gromacs_ROBOTID: " + sagrid_gromacs_ROBOTID                            
                        + "\nsagrid_gromacs_WEBDAV: " + sagrid_gromacs_WEBDAV
                        + "\neumed_gromacs_ROLE: " + sagrid_gromacs_ROLE
                        + "\nsagrid_gromacs_RENEWAL: " + sagrid_gromacs_RENEWAL
                        + "\nsagrid_gromacs_DISABLEVOMS: " + sagrid_gromacs_DISABLEVOMS

                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + "sagrid"
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }
                
                if (see_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[4]="see";
                    // Getting the GROMACS INFRASTRUCTURE from the portlet request for the SEE VO
                    String see_gromacs_INFRASTRUCTURE = request.getParameter("see_gromacs_INFRASTRUCTURE");
                    // Getting the GROMACS VONAME from the portlet request for the SEE VO
                    String see_gromacs_VONAME = request.getParameter("see_gromacs_VONAME");
                    // Getting the GROMACS TOPBDII from the portlet request for the SEE VO
                    String see_gromacs_TOPBDII = request.getParameter("see_gromacs_TOPBDII");
                    // Getting the GROMACS WMS from the portlet request for the SEE VO
                    String[] see_gromacs_WMS = request.getParameterValues("see_gromacs_WMS");
                    // Getting the GROMACS ETOKENSERVER from the portlet request for the SEE VO
                    String see_gromacs_ETOKENSERVER = request.getParameter("see_gromacs_ETOKENSERVER");
                    // Getting the GROMACS MYPROXYSERVER from the portlet request for the SEE VO
                    String see_gromacs_MYPROXYSERVER = request.getParameter("see_gromacs_MYPROXYSERVER");
                    // Getting the GROMACS PORT from the portlet request for the SEE VO
                    String see_gromacs_PORT = request.getParameter("see_gromacs_PORT");
                    // Getting the GROMACS ROBOTID from the portlet request for the SEE VO
                    String see_gromacs_ROBOTID = request.getParameter("see_gromacs_ROBOTID");
                    // Getting the GROMACS WEBDAV from the portlet request for the SEE VO
                    String see_gromacs_WEBDAV = request.getParameter("see_gromacs_WEBDAV");
                    // Getting the GROMACS ROLE from the portlet request for the SEE VO
                    String see_gromacs_ROLE = request.getParameter("see_gromacs_ROLE");
                    // Getting the GROMACS OPTIONS from the portlet request for the SEE VO
                    String[] see_gromacs_OPTIONS = request.getParameterValues("see_gromacs_OPTIONS");

                    String see_gromacs_RENEWAL = "";
                    String see_gromacs_DISABLEVOMS = "";

                    if (see_gromacs_OPTIONS == null){
                        see_gromacs_RENEWAL = "checked";
                        see_gromacs_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(see_gromacs_OPTIONS);
                        // Get the GROMACS RENEWAL from the portlet preferences for the SEE VO
                        see_gromacs_RENEWAL = Arrays.binarySearch(see_gromacs_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Get the GROMACS DISABLEVOMS from the portlet preferences for the SEE VO
                        see_gromacs_DISABLEVOMS = Arrays.binarySearch(see_gromacs_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < see_gromacs_WMS.length; i++)
                        if ( see_gromacs_WMS[i]!=null && (!see_gromacs_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] see_gromacs_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        see_gromacs_WMS_trimmed[i]=see_gromacs_WMS[i].trim();
                        log.info ("\n\nSEE [" + i + "] WMS=[" + see_gromacs_WMS_trimmed[i] + "]");
                    }

                    // Set the portlet preferences
                    portletPreferences.setValue("see_gromacs_INFRASTRUCTURE", see_gromacs_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("see_gromacs_VONAME", see_gromacs_VONAME.trim());
                    portletPreferences.setValue("see_gromacs_TOPBDII", see_gromacs_TOPBDII.trim());
                    portletPreferences.setValues("see_gromacs_WMS", see_gromacs_WMS_trimmed);
                    portletPreferences.setValue("see_gromacs_ETOKENSERVER", see_gromacs_ETOKENSERVER.trim());
                    portletPreferences.setValue("see_gromacs_MYPROXYSERVER", see_gromacs_MYPROXYSERVER.trim());
                    portletPreferences.setValue("see_gromacs_PORT", see_gromacs_PORT.trim());
                    portletPreferences.setValue("see_gromacs_ROBOTID", see_gromacs_ROBOTID.trim());
                    portletPreferences.setValue("see_gromacs_WEBDAV", see_gromacs_WEBDAV.trim());
                    portletPreferences.setValue("see_gromacs_ROLE", see_gromacs_ROLE.trim());
                    portletPreferences.setValue("see_gromacs_RENEWAL", see_gromacs_RENEWAL);
                    portletPreferences.setValue("see_gromacs_DISABLEVOMS", see_gromacs_DISABLEVOMS);
                    
                    portletPreferences.setValue("gromacs_APPID", gromacs_APPID.trim());
                    portletPreferences.setValue("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
                    portletPreferences.setValue("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
                    portletPreferences.setValue("gromacs_OUTPUT_PATH", gromacs_OUTPUT_PATH.trim());
                    portletPreferences.setValue("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
                    portletPreferences.setValue("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the GROMACS portlet preferences ..."                    
                        + "\n\nsee_gromacs_INFRASTRUCTURE: " + see_gromacs_INFRASTRUCTURE
                        + "\nsee_gromacs_VONAME: " + see_gromacs_VONAME
                        + "\nsee_gromacs_TOPBDII: " + see_gromacs_TOPBDII                    
                        + "\nsee_gromacs_ETOKENSERVER: " + see_gromacs_ETOKENSERVER
                        + "\nsee_gromacs_MYPROXYSERVER: " + see_gromacs_MYPROXYSERVER
                        + "\nsee_gromacs_PORT: " + see_gromacs_PORT
                        + "\nsee_gromacs_ROBOTID: " + see_gromacs_ROBOTID
                        + "\nsee_gromacs_WEBDAV: " + see_gromacs_WEBDAV
                        + "\nsee_gromacs_ROLE: " + see_gromacs_ROLE
                        + "\nsee_gromacs_RENEWAL: " + see_gromacs_RENEWAL
                        + "\nsee_gromacs_DISABLEVOMS: " + see_gromacs_DISABLEVOMS

                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + "see"
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }
                
                if (gisela_gromacs_ENABLEINFRASTRUCTURE.equals("checked"))
                {
                    infras[5]="gisela";
                    // Getting the GROMACS INFRASTRUCTURE from the portlet request for the GISELA VO
                    String gisela_gromacs_INFRASTRUCTURE = request.getParameter("gisela_gromacs_INFRASTRUCTURE");
                    // Getting the GROMACS VONAME from the portlet request for the GISELA VO
                    String gisela_gromacs_VONAME = request.getParameter("gisela_gromacs_VONAME");
                    // Getting the GROMACS TOPBDII from the portlet request for the GISELA VO
                    String gisela_gromacs_TOPBDII = request.getParameter("gisela_gromacs_TOPBDII");
                    // Getting the GROMACS WMS from the portlet request for the GISELA VO
                    String[] gisela_gromacs_WMS = request.getParameterValues("gisela_gromacs_WMS");
                    // Getting the GROMACS ETOKENSERVER from the portlet request for the GISELA VO
                    String gisela_gromacs_ETOKENSERVER = request.getParameter("gisela_gromacs_ETOKENSERVER");
                    // Getting the GROMACS MYPROXYSERVER from the portlet request for the GISELA VO
                    String gisela_gromacs_MYPROXYSERVER = request.getParameter("gisela_gromacs_MYPROXYSERVER");
                    // Getting the GROMACS PORT from the portlet request for the GISELA VO
                    String gisela_gromacs_PORT = request.getParameter("gisela_gromacs_PORT");
                    // Getting the GROMACS ROBOTID from the portlet request for the GISELA VO
                    String gisela_gromacs_ROBOTID = request.getParameter("gisela_gromacs_ROBOTID");
                    // Getting the GROMACS WEBDAV from the portlet request for the GISELA VO
                    String gisela_gromacs_WEBDAV = request.getParameter("gisela_gromacs_WEBDAV");
                    // Getting the GROMACS ROLE from the portlet request for the GISELA VO
                    String gisela_gromacs_ROLE = request.getParameter("gisela_gromacs_ROLE");
                    // Getting the GROMACS OPTIONS from the portlet request for the GISELA VO
                    String[] gisela_gromacs_OPTIONS = request.getParameterValues("gisela_gromacs_OPTIONS");

                    String gisela_gromacs_RENEWAL = "";
                    String gisela_gromacs_DISABLEVOMS = "";

                    if (gisela_gromacs_OPTIONS == null){
                        gisela_gromacs_RENEWAL = "checked";
                        gisela_gromacs_DISABLEVOMS = "unchecked";
                    } else {
                        Arrays.sort(gisela_gromacs_OPTIONS);
                        // Get the GROMACS RENEWAL from the portlet preferences for the GISELA VO
                        gisela_gromacs_RENEWAL = Arrays.binarySearch(gisela_gromacs_OPTIONS, "enableRENEWAL") >= 0 ? "checked" : "unchecked";
                        // Get the GROMACS DISABLEVOMS from the portlet preferences for the GISELA VO
                        gisela_gromacs_DISABLEVOMS = Arrays.binarySearch(gisela_gromacs_OPTIONS, "disableVOMS") >= 0 ? "checked" : "unchecked";
                    }
                    
                    int nmax=0;                
                    for (int i = 0; i < gisela_gromacs_WMS.length; i++)
                        if ( gisela_gromacs_WMS[i]!=null && (!gisela_gromacs_WMS[i].trim().equals("N/A")) )                        
                            nmax++;
                    
                    log.info("\n\nLength="+nmax);
                    String[] gisela_gromacs_WMS_trimmed = new String[nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        gisela_gromacs_WMS_trimmed[i]=gisela_gromacs_WMS[i].trim();
                        log.info ("\n\nGISELA [" + i + "] WMS=[" + gisela_gromacs_WMS_trimmed[i] + "]");
                    }

                    // Set the portlet preferences
                    portletPreferences.setValue("gisela_gromacs_INFRASTRUCTURE", gisela_gromacs_INFRASTRUCTURE.trim());
                    portletPreferences.setValue("gisela_gromacs_VONAME", gisela_gromacs_VONAME.trim());
                    portletPreferences.setValue("gisela_gromacs_TOPBDII", gisela_gromacs_TOPBDII.trim());
                    portletPreferences.setValues("gisela_gromacs_WMS", gisela_gromacs_WMS_trimmed);
                    portletPreferences.setValue("gisela_gromacs_ETOKENSERVER", gisela_gromacs_ETOKENSERVER.trim());
                    portletPreferences.setValue("gisela_gromacs_MYPROXYSERVER", gisela_gromacs_MYPROXYSERVER.trim());
                    portletPreferences.setValue("gisela_gromacs_PORT", gisela_gromacs_PORT.trim());
                    portletPreferences.setValue("gisela_gromacs_ROBOTID", gisela_gromacs_ROBOTID.trim());
                    portletPreferences.setValue("gisela_gromacs_WEBDAV", gisela_gromacs_WEBDAV.trim());
                    portletPreferences.setValue("gisela_gromacs_ROLE", gisela_gromacs_ROLE.trim());
                    portletPreferences.setValue("gisela_gromacs_RENEWAL", gisela_gromacs_RENEWAL);
                    portletPreferences.setValue("gisela_gromacs_DISABLEVOMS", gisela_gromacs_DISABLEVOMS);
                    
                    portletPreferences.setValue("gromacs_APPID", gromacs_APPID.trim());
                    portletPreferences.setValue("gromacs_LOGLEVEL", gromacs_LOGLEVEL.trim());
                    portletPreferences.setValue("gromacs_METADATA_HOST", gromacs_METADATA_HOST.trim());
                    portletPreferences.setValue("gromacs_OUTPUT_PATH", gromacs_OUTPUT_PATH.trim());
                    portletPreferences.setValue("gromacs_SOFTWARE", gromacs_SOFTWARE.trim());
                    portletPreferences.setValue("gromacs_LOCAL_PROXY", gromacs_LOCAL_PROXY.trim());
                    portletPreferences.setValue("TRACKING_DB_HOSTNAME", TRACKING_DB_HOSTNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_USERNAME", TRACKING_DB_USERNAME.trim());
                    portletPreferences.setValue("TRACKING_DB_PASSWORD", TRACKING_DB_PASSWORD.trim());
                    portletPreferences.setValue("SMTP_HOST", SMTP_HOST.trim());
                    portletPreferences.setValue("SENDER_MAIL", SENDER_MAIL.trim());
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n\nPROCESS ACTION => " + action
                        + "\n- Storing the GROMACS portlet preferences ..."                    
                        + "\n\ngisela_gromacs_INFRASTRUCTURE: " + gisela_gromacs_INFRASTRUCTURE
                        + "\ngisela_gromacs_VONAME: " + gisela_gromacs_VONAME
                        + "\ngisela_gromacs_TOPBDII: " + gisela_gromacs_TOPBDII                    
                        + "\ngisela_gromacs_ETOKENSERVER: " + gisela_gromacs_ETOKENSERVER
                        + "\ngisela_gromacs_MYPROXYSERVER: " + gisela_gromacs_MYPROXYSERVER
                        + "\ngisela_gromacs_PORT: " + gisela_gromacs_PORT
                        + "\ngisela_gromacs_ROBOTID: " + gisela_gromacs_ROBOTID
                        + "\ngisela_gromacs_WEBDAV: " + gisela_gromacs_WEBDAV
                        + "\ngisela_gromacs_ROLE: " + gisela_gromacs_ROLE
                        + "\ngisela_gromacs_RENEWAL: " + gisela_gromacs_RENEWAL
                        + "\ngisela_gromacs_DISABLEVOMS: " + gisela_gromacs_DISABLEVOMS

                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + "gisela"
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                }
                
                for (int i=0; i<infras.length; i++)
                log.info("\n - Infrastructure Enabled = " + infras[i]);
                portletPreferences.setValues("gromacs_ENABLEINFRASTRUCTURE", infras);
                portletPreferences.setValue("lato_gromacs_ENABLEINFRASTRUCTURE", infras[0]);            
                portletPreferences.setValue("garuda_gromacs_ENABLEINFRASTRUCTURE", infras[1]);            
                portletPreferences.setValue("eumed_gromacs_ENABLEINFRASTRUCTURE", infras[2]);
                portletPreferences.setValue("sagrid_gromacs_ENABLEINFRASTRUCTURE", infras[3]);
                portletPreferences.setValue("see_gromacs_ENABLEINFRASTRUCTURE", infras[4]);
                portletPreferences.setValue("gisela_gromacs_ENABLEINFRASTRUCTURE", infras[5]);

                portletPreferences.store();
                response.setPortletMode(PortletMode.VIEW);
            } // end PROCESS ACTION [ CONFIG_GROMACS_PORTLET ]
            

            if (action.equals("SUBMIT_GROMACS_PORTLET")) {
                log.info("\nPROCESS ACTION => " + action);            
                InfrastructureInfo infrastructures[] = new InfrastructureInfo[6];
                String jdlRequirements[] = new String[4];
                String lato_gromacs_WEBDAV = "";
                String garuda_gromacs_WEBDAV = "";
                String eumed_gromacs_WEBDAV = "";
                String sagrid_gromacs_WEBDAV = "";
                String see_gromacs_WEBDAV = "";
                String gisela_gromacs_WEBDAV = "";
                
                //String gromacs_DEFAULT_STORAGE = "";
                int MAX=0;                
                
                // Getting the GROMACS APPID from the portlet preferences
                String gromacs_APPID = portletPreferences.getValue("gromacs_APPID", "N/A");
                // Getting the LOGLEVEL from the portlet preferences
                String gromacs_LOGLEVEL = portletPreferences.getValue("gromacs_LOGLEVEL", "INFO");
                // Getting the GROMACS_METADATA_HOST from the portlet preferences
                String gromacs_METADATA_HOST = portletPreferences.getValue("gromacs_METADATA_HOST", "INFO");
                // Getting the GROMACS OUTPUT_PATH from the portlet preferences
                String gromacs_OUTPUT_PATH = portletPreferences.getValue("gromacs_OUTPUT_PATH", "/tmp");
                // Getting the GROMACS SOFTWARE from the portlet preferences
                String gromacs_SOFTWARE = portletPreferences.getValue("gromacs_SOFTWARE", "N/A");
                // Getting the GROMACS LOCAL_PROXY from the portlet preferences
                String gromacs_LOCAL_PROXY = portletPreferences.getValue("gromacs_LOCAL_PROXY", "N/A");
                // Getting the TRACKING_DB_HOSTNAME from the portlet request
                String TRACKING_DB_HOSTNAME = portletPreferences.getValue("TRACKING_DB_HOSTNAME", "N/A");
                // Getting the TRACKING_DB_USERNAME from the portlet request
                String TRACKING_DB_USERNAME = portletPreferences.getValue("TRACKING_DB_USERNAME", "N/A");
                // Getting the TRACKING_DB_PASSWORD from the portlet request
                String TRACKING_DB_PASSWORD = portletPreferences.getValue("TRACKING_DB_PASSWORD","N/A");
                // Getting the SMTP_HOST from the portlet request
                String SMTP_HOST = portletPreferences.getValue("SMTP_HOST","N/A");
                // Getting the SENDER_MAIL from the portlet request
                String SENDER_MAIL = portletPreferences.getValue("SENDER_MAIL","N/A");
                
                String lato_gromacs_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("lato_gromacs_ENABLEINFRASTRUCTURE","null");
                String garuda_gromacs_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("garuda_gromacs_ENABLEINFRASTRUCTURE","null");
                String eumed_gromacs_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("eumed_gromacs_ENABLEINFRASTRUCTURE","null");
                String sagrid_gromacs_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("sagrid_gromacs_ENABLEINFRASTRUCTURE","null");
                String see_gromacs_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("see_gromacs_ENABLEINFRASTRUCTURE","null");
                String gisela_gromacs_ENABLEINFRASTRUCTURE =
                        portletPreferences.getValue("gisela_gromacs_ENABLEINFRASTRUCTURE","null");
                
                if (lato_gromacs_ENABLEINFRASTRUCTURE != null &&
                    lato_gromacs_ENABLEINFRASTRUCTURE.equals("lato"))
                {
                    MAX++;
                    // Getting the GROMACS VONAME from the portlet preferences for LATO
                    String lato_gromacs_INFRASTRUCTURE = portletPreferences.getValue("lato_gromacs_INFRASTRUCTURE", "N/A");
                    // Getting the GROMACS VONAME from the portlet preferences for LATO
                    String lato_gromacs_LOGIN = portletPreferences.getValue("lato_gromacs_LOGIN", "N/A");
                    // Getting the GROMACS TOPPBDII from the portlet preferences for LATO
                    String lato_gromacs_PASSWD = portletPreferences.getValue("lato_gromacs_PASSWD", "N/A");
                    // Getting the GROMACS WMS from the portlet preferences for LATO                
                    String[] lato_gromacs_WMS = portletPreferences.getValues("lato_gromacs_WMS", new String[5]);
                    // Getting the GROMACS ETOKENSERVER from the portlet preferences for LATO
                    String lato_gromacs_ETOKENSERVER = portletPreferences.getValue("lato_gromacs_ETOKENSERVER", "N/A");
                    // Getting the GROMACS MYPROXYSERVER from the portlet preferences for LATO
                    String lato_gromacs_MYPROXYSERVER = portletPreferences.getValue("lato_gromacs_MYPROXYSERVER", "N/A");
                    // Getting the GROMACS PORT from the portlet preferences for LATO
                    String lato_gromacs_PORT = portletPreferences.getValue("lato_gromacs_PORT", "N/A");
                    // Getting the GROMACS ROBOTID from the portlet preferences for LATO
                    String lato_gromacs_ROBOTID = portletPreferences.getValue("lato_gromacs_ROBOTID", "N/A");
                    // Getting the GROMACS WEBDAV from the portlet preferences for LATO
                    lato_gromacs_WEBDAV = portletPreferences.getValue("lato_gromacs_WEBDAV", "N/A");
                    // Getting the GROMACS ROLE from the portlet preferences for LATO
                    String lato_gromacs_ROLE = portletPreferences.getValue("lato_gromacs_ROLE", "N/A");
                    // Getting the GROMACS RENEWAL from the portlet preferences for LATO
                    String lato_gromacs_RENEWAL = portletPreferences.getValue("lato_gromacs_RENEWAL", "checked");
                    // Getting the GROMACS DISABLEVOMS from the portlet preferences for LATO
                    String lato_gromacs_DISABLEVOMS = portletPreferences.getValue("lato_gromacs_DISABLEVOMS", "unchecked");
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the GROMACS portlet preferences ..."
                        + "\nlato_gromacs_INFRASTRUCTURE: " + lato_gromacs_INFRASTRUCTURE
                        + "\nlato_gromacs_LOGIN: " + lato_gromacs_LOGIN
                        + "\nlato_gromacs_PASSWD: " + lato_gromacs_PASSWD                    
                        + "\nlato_gromacs_ETOKENSERVER: " + lato_gromacs_ETOKENSERVER
                        + "\nlato_gromacs_MYPROXYSERVER: " + lato_gromacs_MYPROXYSERVER
                        + "\nlato_gromacs_PORT: " + lato_gromacs_PORT
                        + "\nlato_gromacs_ROBOTID: " + lato_gromacs_ROBOTID
                        + "\nlato_gromacs_WEBDAV: " + lato_gromacs_WEBDAV
                        + "\nlato_gromacs_ROLE: " + lato_gromacs_ROLE
                        + "\nlato_gromacs_RENEWAL: " + lato_gromacs_RENEWAL
                        + "\nlato_gromacs_DISABLEVOMS: " + lato_gromacs_DISABLEVOMS
                       
                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + lato_gromacs_ENABLEINFRASTRUCTURE
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);
                    }
                    
                    // Defining the WMS list for the "LATO" Infrastructure
                    int nmax=0;
                    for (int i = 0; i < lato_gromacs_WMS.length; i++)
                        if ((lato_gromacs_WMS[i]!=null) && (!lato_gromacs_WMS[i].equals("N/A"))) nmax++;

                    String lato_wmsList[] = new String [nmax];                
                        for (int i = 0; i < nmax; i++)
                        {
                            if (lato_gromacs_WMS[i]!=null) {
                            lato_wmsList[i]=lato_gromacs_WMS[i].trim();
                            log.info ("\n\n[" + nmax
                                              + "] Submitting to LATO ["
                                              + i
                                              + "] using WMS=["
                                              + lato_wmsList[i]
                                              + "]");
                            }
                        }
                    
                    infrastructures[0] = new InfrastructureInfo(
                            "SSH",
                            "ssh",
                            lato_gromacs_LOGIN,
                            lato_gromacs_PASSWD,
                            lato_wmsList);               
                }
                            
                if (garuda_gromacs_ENABLEINFRASTRUCTURE != null &&
                    garuda_gromacs_ENABLEINFRASTRUCTURE.equals("garuda"))
                {
                    MAX++;
                    // Getting the GROMACSVONAME from the portlet preferences for the GARUDA VO
                    String garuda_gromacs_INFRASTRUCTURE = portletPreferences.getValue("garuda_gromacs_INFRASTRUCTURE", "N/A");
                    // Getting the GROMACS VONAME from the portlet preferences for the GARUDA VO
                    String garuda_gromacs_VONAME = portletPreferences.getValue("garuda_gromacs_VONAME", "N/A");
                    // Getting the GROMACS TOPPBDII from the portlet preferences for the GARUDA VO
                    String garuda_gromacs_TOPBDII = portletPreferences.getValue("garuda_gromacs_TOPBDII", "N/A");
                    // Getting the GROMACS WMS from the portlet preferences for the GARUDA VO                
                    String[] garuda_gromacs_WMS = portletPreferences.getValues("garuda_gromacs_WMS", new String[5]);
                    // Getting the GROMACS ETOKENSERVER from the portlet preferences for the GARUDA VO
                    String garuda_gromacs_ETOKENSERVER = portletPreferences.getValue("garuda_gromacs_ETOKENSERVER", "N/A");
                    // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the GARUDA VO
                    String garuda_gromacs_MYPROXYSERVER = portletPreferences.getValue("garuda_gromacs_MYPROXYSERVER", "N/A");
                    // Getting the GROMACS PORT from the portlet preferences for the GARUDA VO
                    String garuda_gromacs_PORT = portletPreferences.getValue("garuda_gromacs_PORT", "N/A");
                    // Getting the GROMACS ROBOTID from the portlet preferences for the GARUDA VO
                    String garuda_gromacs_ROBOTID = portletPreferences.getValue("garuda_gromacs_ROBOTID", "N/A");
                    // Getting the GROMACS WEBDAV from the portlet preferences for the GARUDA VO
                    garuda_gromacs_WEBDAV = portletPreferences.getValue("garuda_gromacs_WEBDAV", "N/A");
                    // Getting the GROMACS ROLE from the portlet preferences for the GARUDA VO
                    String garuda_gromacs_ROLE = portletPreferences.getValue("garuda_gromacs_ROLE", "N/A");
                    // Getting the GROMACS RENEWAL from the portlet preferences for the GARUDA VO
                    String garuda_gromacs_RENEWAL = portletPreferences.getValue("garuda_gromacs_RENEWAL", "checked");
                    // Getting the GROMACS DISABLEVOMS from the portlet preferences for the GARUDA VO
                    String garuda_gromacs_DISABLEVOMS = portletPreferences.getValue("garuda_gromacs_DISABLEVOMS", "unchecked");                    
                            
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the GROMACS portlet preferences ..."
                        + "\ngaruda_gromacs_INFRASTRUCTURE: " + garuda_gromacs_INFRASTRUCTURE
                        + "\ngaruda_gromacs_VONAME: " + garuda_gromacs_VONAME
                        + "\ngaruda_gromacs_TOPBDII: " + garuda_gromacs_TOPBDII                    
                        + "\ngaruda_gromacs_ETOKENSERVER: " + garuda_gromacs_ETOKENSERVER
                        + "\ngaruda_gromacs_MYPROXYSERVER: " + garuda_gromacs_MYPROXYSERVER
                        + "\ngaruda_gromacs_PORT: " + garuda_gromacs_PORT
                        + "\ngaruda_gromacs_ROBOTID: " + garuda_gromacs_ROBOTID
                        + "\ngaruda_gromacs_WEBDAV: " + garuda_gromacs_WEBDAV
                        + "\ngaruda_gromacs_ROLE: " + garuda_gromacs_ROLE
                        + "\ngaruda_gromacs_RENEWAL: " + garuda_gromacs_RENEWAL
                        + "\ngaruda_gromacs_DISABLEVOMS: " + garuda_gromacs_DISABLEVOMS
                       
                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + garuda_gromacs_ENABLEINFRASTRUCTURE
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);                                            
                    }
                    
                    // Defining the WMS list for the "GARUDA" Infrastructure
                    int nmax=0;
                    for (int i = 0; i < garuda_gromacs_WMS.length; i++)
                        if ((garuda_gromacs_WMS[i]!=null) && (!garuda_gromacs_WMS[i].equals("N/A"))) nmax++;

                    String wmsList[] = new String [nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        if (garuda_gromacs_WMS[i]!=null) {
                        wmsList[i]=garuda_gromacs_WMS[i].trim();
                        log.info ("\n\n[" + nmax
                                          + "] Submitting to GARUDA ["
                                          + i
                                          + "] using WMSGRAM=["
                                          + wmsList[i]
                                          + "]");
                        }
                    }

                    infrastructures[1] = new InfrastructureInfo(
                        "GARUDA", 
                        "gatekeeper", 
                        wmsList, 
                        gromacs_LOCAL_PROXY);
                }
                
                if (eumed_gromacs_ENABLEINFRASTRUCTURE != null &&
                    eumed_gromacs_ENABLEINFRASTRUCTURE.equals("eumed"))
                {
                    MAX++;
                    // Getting the GROMACS VONAME from the portlet preferences for the EUMED VO
                    String eumed_gromacs_INFRASTRUCTURE = portletPreferences.getValue("eumed_gromacs_INFRASTRUCTURE", "N/A");
                    // Getting the GROMACS VONAME from the portlet preferences for the EUMED VO
                    String eumed_gromacs_VONAME = portletPreferences.getValue("eumed_gromacs_VONAME", "N/A");
                    // Getting the GROMACS TOPPBDII from the portlet preferences for the EUMED VO
                    String eumed_gromacs_TOPBDII = portletPreferences.getValue("eumed_gromacs_TOPBDII", "N/A");
                    // Getting the GROMACS WMS from the portlet preferences for the EUMED VO
                    String[] eumed_gromacs_WMS = portletPreferences.getValues("eumed_gromacs_WMS", new String[5]);
                    // Getting the GROMACS ETOKENSERVER from the portlet preferences for the EUMED VO
                    String eumed_gromacs_ETOKENSERVER = portletPreferences.getValue("eumed_gromacs_ETOKENSERVER", "N/A");
                    // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the EUMED VO
                    String eumed_gromacs_MYPROXYSERVER = portletPreferences.getValue("eumed_gromacs_MYPROXYSERVER", "N/A");
                    // Getting the GROMACS PORT from the portlet preferences for the EUMED VO
                    String eumed_gromacs_PORT = portletPreferences.getValue("eumed_gromacs_PORT", "N/A");
                    // Getting the GROMACS ROBOTID from the portlet preferences for the EUMED VO
                    String eumed_gromacs_ROBOTID = portletPreferences.getValue("eumed_gromacs_ROBOTID", "N/A");
                    // Getting the GROMACS WEBDAV from the portlet preferences for the EUMED VO
                    eumed_gromacs_WEBDAV = portletPreferences.getValue("eumed_gromacs_WEBDAV", "N/A");
                    // Getting the GROMACS ROLE from the portlet preferences for the EUMED VO
                    String eumed_gromacs_ROLE = portletPreferences.getValue("eumed_gromacs_ROLE", "N/A");
                    // Getting the GROMACS RENEWAL from the portlet preferences for the EUMED VO
                    String eumed_gromacs_RENEWAL = portletPreferences.getValue("eumed_gromacs_RENEWAL", "checked");
                    // Getting the GROMACS DISABLEVOMS from the portlet preferences for the EUMED VO
                    String eumed_gromacs_DISABLEVOMS = portletPreferences.getValue("eumed_gromacs_DISABLEVOMS", "unchecked");                    
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the GROMACS portlet preferences ..."
                        + "\n\neumed_gromacs_INFRASTRUCTURE: " + eumed_gromacs_INFRASTRUCTURE
                        + "\neumed_gromacs_VONAME: " + eumed_gromacs_VONAME
                        + "\neumed_gromacs_TOPBDII: " + eumed_gromacs_TOPBDII                    
                        + "\neumed_gromacs_ETOKENSERVER: " + eumed_gromacs_ETOKENSERVER
                        + "\neumed_gromacs_MYPROXYSERVER: " + eumed_gromacs_MYPROXYSERVER
                        + "\neumed_gromacs_PORT: " + eumed_gromacs_PORT
                        + "\neumed_gromacs_ROBOTID: " + eumed_gromacs_ROBOTID
                        + "\neumed_gromacs_WEBDAV: " + eumed_gromacs_WEBDAV
                        + "\neumed_gromacs_ROLE: " + eumed_gromacs_ROLE
                        + "\neumed_gromacs_RENEWAL: " + eumed_gromacs_RENEWAL
                        + "\neumed_gromacs_DISABLEVOMS: " + eumed_gromacs_DISABLEVOMS

                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + eumed_gromacs_ENABLEINFRASTRUCTURE
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_DEFAULT_STORAGE: " + eumed_gromacs_WEBDAV
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);                                           
                    }
                    
                    // Defining the WMS list for the "EUMED" Infrastructure
                    int nmax=0;
                    for (int i = 0; i < eumed_gromacs_WMS.length; i++)
                        if ((eumed_gromacs_WMS[i]!=null) && (!eumed_gromacs_WMS[i].equals("N/A"))) nmax++;
                    
                    String wmsList[] = new String [nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        if (eumed_gromacs_WMS[i]!=null) {
                        wmsList[i]=eumed_gromacs_WMS[i].trim();
                        log.info ("\n\n[" + nmax
                                          + "] Submitting to EUMED ["
                                          + i
                                          + "] using WMS=["
                                          + wmsList[i]
                                          + "]");
                        }
                    }
                    
                    // Setting the JDL Requirements
                    String[] SOFTWARE_LIST = gromacs_SOFTWARE.split(",");
                    int index=0;
                    
                    for(String SOFTWARE: SOFTWARE_LIST)
                    {
                        jdlRequirements[index++] = 
                            "VO-"
                            + eumed_gromacs_VONAME
                            + "-"
                            + SOFTWARE;                            
                    }
                    
                    infrastructures[2] = new InfrastructureInfo(
                        eumed_gromacs_VONAME,
                        eumed_gromacs_TOPBDII,
                        wmsList,
                        eumed_gromacs_ETOKENSERVER,
                        eumed_gromacs_PORT,
                        eumed_gromacs_ROBOTID,
                        eumed_gromacs_VONAME,
                        eumed_gromacs_ROLE,
                        true, // set the RFC proxy for the infrastructure                            
                        jdlRequirements[0] + "," + jdlRequirements[1]);
                        //"VO-" + eumed_gromacs_VONAME + "-" + gromacs_SOFTWARE);
                }
                
                
                
                if (sagrid_gromacs_ENABLEINFRASTRUCTURE != null &&
                    sagrid_gromacs_ENABLEINFRASTRUCTURE.equals("sagrid"))
                {
                    MAX++;
                    // Getting the GROMACS VONAME from the portlet preferences for the SAGRID VO
                    String sagrid_gromacs_INFRASTRUCTURE = portletPreferences.getValue("sagrid_gromacs_INFRASTRUCTURE", "N/A");
                    // Getting the GROMACS VONAME from the portlet preferences for the SAGRID VO
                    String sagrid_gromacs_VONAME = portletPreferences.getValue("sagrid_gromacs_VONAME", "N/A");
                    // Getting the GROMACS TOPPBDII from the portlet preferences for the SAGRID VO
                    String sagrid_gromacs_TOPBDII = portletPreferences.getValue("sagrid_gromacs_TOPBDII", "N/A");
                    // Getting the GROMACS WMS from the portlet preferences for the SAGRID VO
                    String[] sagrid_gromacs_WMS = portletPreferences.getValues("sagrid_gromacs_WMS", new String[5]);
                    // Getting the GROMACS ETOKENSERVER from the portlet preferences for the SAGRID VO
                    String sagrid_gromacs_ETOKENSERVER = portletPreferences.getValue("sagrid_gromacs_ETOKENSERVER", "N/A");
                    // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the SAGRID VO
                    String sagrid_gromacs_MYPROXYSERVER = portletPreferences.getValue("sagrid_gromacs_MYPROXYSERVER", "N/A");
                    // Getting the GROMACS PORT from the portlet preferences for the SAGRID VO
                    String sagrid_gromacs_PORT = portletPreferences.getValue("sagrid_gromacs_PORT", "N/A");
                    // Getting the GROMACS ROBOTID from the portlet preferences for the SAGRID VO
                    String sagrid_gromacs_ROBOTID = portletPreferences.getValue("sagrid_gromacs_ROBOTID", "N/A");
                    // Getting the GROMACS WEBDAV from the portlet preferences for the SAGRID VO
                    sagrid_gromacs_WEBDAV = portletPreferences.getValue("sagrid_gromacs_WEBDAV", "N/A");
                    // Getting the GROMACS ROLE from the portlet preferences for the SAGRID VO
                    String sagrid_gromacs_ROLE = portletPreferences.getValue("sagrid_gromacs_ROLE", "N/A");
                    // Getting the GROMACS RENEWAL from the portlet preferences for the SAGRID VO
                    String sagrid_gromacs_RENEWAL = portletPreferences.getValue("sagrid_gromacs_RENEWAL", "checked");
                    // Getting the GROMACS DISABLEVOMS from the portlet preferences for the SAGRID VO
                    String sagrid_gromacs_DISABLEVOMS = portletPreferences.getValue("sagrid_gromacs_DISABLEVOMS", "unchecked");                    
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the GROMACS portlet preferences ..."
                        + "\n\nsagrid_gromacs_INFRASTRUCTURE: " + sagrid_gromacs_INFRASTRUCTURE
                        + "\nsagrid_gromacs_VONAME: " + sagrid_gromacs_VONAME
                        + "\nsagrid_gromacs_TOPBDII: " + sagrid_gromacs_TOPBDII                    
                        + "\nsagrid_gromacs_ETOKENSERVER: " + sagrid_gromacs_ETOKENSERVER
                        + "\nsagrid_gromacs_MYPROXYSERVER: " + sagrid_gromacs_MYPROXYSERVER
                        + "\nsagrid_gromacs_PORT: " + sagrid_gromacs_PORT
                        + "\nsagrid_gromacs_ROBOTID: " + sagrid_gromacs_ROBOTID
                        + "\nsagrid_gromacs_WEBDAV: " + sagrid_gromacs_WEBDAV
                        + "\nsagrid_gromacs_ROLE: " + sagrid_gromacs_ROLE
                        + "\nsagrid_gromacs_RENEWAL: " + sagrid_gromacs_RENEWAL
                        + "\nsagrid_gromacs_DISABLEVOMS: " + sagrid_gromacs_DISABLEVOMS

                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + eumed_gromacs_ENABLEINFRASTRUCTURE
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_DEFAULT_STORAGE: " + sagrid_gromacs_WEBDAV
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);                                            
                    }
                    
                    // Defining the WMS list for the "SAGRID" Infrastructure
                    int nmax=0;
                    for (int i = 0; i < sagrid_gromacs_WMS.length; i++)
                        if ((sagrid_gromacs_WMS[i]!=null) && (!sagrid_gromacs_WMS[i].equals("N/A"))) nmax++;
                    
                    String wmsList[] = new String [nmax];
                    for (int i = 0; i < nmax; i++)
                    {
                        if (sagrid_gromacs_WMS[i]!=null) {
                        wmsList[i]=sagrid_gromacs_WMS[i].trim();
                        log.info ("\n\n[" + nmax
                                          + "] Submitting to SAGRID ["
                                          + i
                                          + "] using WMS=["
                                          + wmsList[i]
                                          + "]");
                        }
                    }
                    
                    // Setting the JDL Requirements
                    String[] SOFTWARE_LIST = gromacs_SOFTWARE.split(",");
                    int index=0;
                    
                    for(String SOFTWARE: SOFTWARE_LIST)
                    {
                        jdlRequirements[index++] = 
                            "VO-"
                            + sagrid_gromacs_VONAME
                            + "-"
                            + SOFTWARE;                            
                    }
                    
                    infrastructures[3] = new InfrastructureInfo(
                        sagrid_gromacs_VONAME,
                        sagrid_gromacs_TOPBDII,
                        wmsList,
                        sagrid_gromacs_ETOKENSERVER,
                        sagrid_gromacs_PORT,
                        sagrid_gromacs_ROBOTID,
                        sagrid_gromacs_VONAME,
                        sagrid_gromacs_ROLE,
                        true, // set the RFC proxy for the infrastructure                            
                        jdlRequirements[0] + "," + jdlRequirements[1]);
                        //"VO-" + sagrid_gromacs_VONAME + "-" + gromacs_SOFTWARE);
                }

                if (see_gromacs_ENABLEINFRASTRUCTURE != null &&
                    see_gromacs_ENABLEINFRASTRUCTURE.equals("see")) 
                {
                    MAX++;
                    // Getting the GROMACS VONAME from the portlet preferences for the SEE VO
                    String see_gromacs_INFRASTRUCTURE = portletPreferences.getValue("see_gromacs_INFRASTRUCTURE", "N/A");
                    // Getting the GROMACS VONAME from the portlet preferences for the SEE VO
                    String see_gromacs_VONAME = portletPreferences.getValue("see_gromacs_VONAME", "N/A");
                    // Getting the GROMACS TOPPBDII from the portlet preferences for the SEE VO
                    String see_gromacs_TOPBDII = portletPreferences.getValue("see_gromacs_TOPBDII", "N/A");
                    // Getting the GROMACS WMS from the portlet preferences for the SEE VO
                    String[] see_gromacs_WMS = portletPreferences.getValues("see_gromacs_WMS", new String[5]);
                    // Getting the GROMACS ETOKENSERVER from the portlet preferences for the SEE VO
                    String see_gromacs_ETOKENSERVER = portletPreferences.getValue("see_gromacs_ETOKENSERVER", "N/A");
                    // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the SEE VO
                    String see_gromacs_MYPROXYSERVER = portletPreferences.getValue("see_gromacs_MYPROXYSERVER", "N/A");
                    // Getting the GROMACS PORT from the portlet preferences for the SEE VO
                    String see_gromacs_PORT = portletPreferences.getValue("see_gromacs_PORT", "N/A");
                    // Getting the GROMACS ROBOTID from the portlet preferences for the SEE VO
                    String see_gromacs_ROBOTID = portletPreferences.getValue("see_gromacs_ROBOTID", "N/A");
                    // Getting the GROMACS WEBDAV from the portlet preferences for the SEE VO
                    see_gromacs_WEBDAV = portletPreferences.getValue("see_gromacs_WEBDAV", "N/A");
                    // Getting the GROMACS ROLE from the portlet preferences for the SEE VO
                    String see_gromacs_ROLE = portletPreferences.getValue("see_gromacs_ROLE", "N/A");
                    // Getting the GROMACS RENEWAL from the portlet preferences for the SEE VO
                    String see_gromacs_RENEWAL = portletPreferences.getValue("see_gromacs_RENEWAL", "checked");
                    // Getting the GROMACS DISABLEVOMS from the portlet preferences for the SEE VO
                    String see_gromacs_DISABLEVOMS = portletPreferences.getValue("see_gromacs_DISABLEVOMS", "unchecked");                    
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the GROMACS portlet preferences ..."
                        + "\n\nsee_gromacs_INFRASTRUCTURE: " + see_gromacs_INFRASTRUCTURE
                        + "\nsee_gromacs_VONAME: " + see_gromacs_VONAME
                        + "\nsee_gromacs_TOPBDII: " + see_gromacs_TOPBDII                        
                        + "\nsee_gromacs_ETOKENSERVER: " + see_gromacs_ETOKENSERVER
                        + "\nsee_gromacs_MYPROXYSERVER: " + see_gromacs_MYPROXYSERVER
                        + "\nsee_gromacs_PORT: " + see_gromacs_PORT
                        + "\nsee_gromacs_ROBOTID: " + see_gromacs_ROBOTID
                        + "\nsee_gromacs_WEBDAV: " + see_gromacs_WEBDAV
                        + "\nsee_gromacs_ROLE: " + see_gromacs_ROLE
                        + "\nsee_gromacs_RENEWAL: " + see_gromacs_RENEWAL
                        + "\nsee_gromacs_DISABLEVOMS: " + see_gromacs_DISABLEVOMS

                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + see_gromacs_ENABLEINFRASTRUCTURE
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_DEFAULT_STORAGE: " + see_gromacs_WEBDAV
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH                        
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY                            
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);                                            
                    }
                    
                    // Defining the WMS list for the "SEE" Infrastructure
                    int nmax=0;
                    for (int i = 0; i < see_gromacs_WMS.length; i++)
                        if ((see_gromacs_WMS[i]!=null) && (!see_gromacs_WMS[i].equals("N/A"))) nmax++;
                    
                    String wmsList[] = new String [see_gromacs_WMS.length];
                    for (int i = 0; i < see_gromacs_WMS.length; i++)
                    {
                        if (see_gromacs_WMS[i]!=null) {
                        wmsList[i]=see_gromacs_WMS[i].trim();
                        log.info ("\n\nSubmitting for SEE [" + i + "] using WMS=[" + wmsList[i] + "]");
                        }
                    }
                    
                    // Setting the JDL Requirements
                    String[] SOFTWARE_LIST = gromacs_SOFTWARE.split(",");
                    int index=0;
                    
                    for(String SOFTWARE: SOFTWARE_LIST)
                    {
                        jdlRequirements[index++] = 
                        "VO-"
                        + see_gromacs_VONAME
                        + "-"
                        + SOFTWARE;                            
                    }
                    
                    infrastructures[4] = new InfrastructureInfo(
                        see_gromacs_VONAME,
                        see_gromacs_TOPBDII,
                        wmsList,
                        see_gromacs_ETOKENSERVER,
                        see_gromacs_PORT,
                        see_gromacs_ROBOTID,
                        see_gromacs_VONAME,
                        see_gromacs_ROLE,
                        true, // set the RFC proxy for the infrastructure
                        jdlRequirements[0] + "," + jdlRequirements[1]);
                        //"VO-" + see_gromacs_VONAME + "-" + gromacs_SOFTWARE);                                                            
                }
                
                if (gisela_gromacs_ENABLEINFRASTRUCTURE != null &&
                    gisela_gromacs_ENABLEINFRASTRUCTURE.equals("gisela")) 
                {
                    MAX++;
                    // Getting the GROMACS VONAME from the portlet preferences for the GISELA VO
                    String gisela_gromacs_INFRASTRUCTURE = portletPreferences.getValue("gisela_gromacs_INFRASTRUCTURE", "N/A");
                    // Getting the GROMACS VONAME from the portlet preferences for the GISELA VO
                    String gisela_gromacs_VONAME = portletPreferences.getValue("gisela_gromacs_VONAME", "N/A");
                    // Getting the GROMACS TOPPBDII from the portlet preferences for the GISELA VO
                    String gisela_gromacs_TOPBDII = portletPreferences.getValue("gisela_gromacs_TOPBDII", "N/A");
                    // Getting the GROMACS WMS from the portlet preferences for the GISELA VO
                    String[] gisela_gromacs_WMS = portletPreferences.getValues("gisela_gromacs_WMS", new String[5]);
                    // Getting the GROMACS ETOKENSERVER from the portlet preferences for the GISELA VO
                    String gisela_gromacs_ETOKENSERVER = portletPreferences.getValue("gisela_gromacs_ETOKENSERVER", "N/A");
                    // Getting the GROMACS MYPROXYSERVER from the portlet preferences for the GISELA VO
                    String gisela_gromacs_MYPROXYSERVER = portletPreferences.getValue("gisela_gromacs_MYPROXYSERVER", "N/A");
                    // Getting the GROMACS PORT from the portlet preferences for the GISELA VO
                    String gisela_gromacs_PORT = portletPreferences.getValue("gisela_gromacs_PORT", "N/A");
                    // Getting the GROMACS ROBOTID from the portlet preferences for the GISELA VO
                    String gisela_gromacs_ROBOTID = portletPreferences.getValue("gisela_gromacs_ROBOTID", "N/A");
                    // Getting the GROMACS WEBDAV from the portlet preferences for the GISELA VO
                    gisela_gromacs_WEBDAV = portletPreferences.getValue("gisela_gromacs_WEBDAV", "N/A");
                    // Getting the GROMACS ROLE from the portlet preferences for the GISELA VO
                    String gisela_gromacs_ROLE = portletPreferences.getValue("gisela_gromacs_ROLE", "N/A");
                    // Getting the GROMACS RENEWAL from the portlet preferences for the GISELA VO
                    String gisela_gromacs_RENEWAL = portletPreferences.getValue("gisela_gromacs_RENEWAL", "checked");
                    // Getting the GROMACS DISABLEVOMS from the portlet preferences for the GISELA VO
                    String gisela_gromacs_DISABLEVOMS = portletPreferences.getValue("gisela_gromacs_DISABLEVOMS", "unchecked");                    
                    
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) {
                    log.info("\n- Getting the GROMACS portlet preferences ..."
                        + "\n\ngisela_gromacs_INFRASTRUCTURE: " + gisela_gromacs_INFRASTRUCTURE
                        + "\ngisela_gromacs_VONAME: " + gisela_gromacs_VONAME
                        + "\ngisela_gromacs_TOPBDII: " + gisela_gromacs_TOPBDII                        
                        + "\ngisela_gromacs_ETOKENSERVER: " + gisela_gromacs_ETOKENSERVER
                        + "\ngisela_gromacs_MYPROXYSERVER: " + gisela_gromacs_MYPROXYSERVER
                        + "\ngisela_gromacs_PORT: " + gisela_gromacs_PORT
                        + "\ngisela_gromacs_ROBOTID: " + gisela_gromacs_ROBOTID
                        + "\ngisela_gromacs_WEBDAV: " + gisela_gromacs_WEBDAV
                        + "\ngisela_gromacs_ROLE: " + gisela_gromacs_ROLE
                        + "\ngisela_gromacs_RENEWAL: " + gisela_gromacs_RENEWAL
                        + "\ngisela_gromacs_DISABLEVOMS: " + gisela_gromacs_DISABLEVOMS

                        + "\n\ngromacs_ENABLEINFRASTRUCTURE: " + see_gromacs_ENABLEINFRASTRUCTURE
                        + "\ngromacs_APPID: " + gromacs_APPID
                        + "\ngromacs_LOGLEVEL: " + gromacs_LOGLEVEL
                        + "\ngromacs_METADATA_HOST: " + gromacs_METADATA_HOST
                        + "\ngromacs_DEFAULT_STORAGE: " + gisela_gromacs_WEBDAV
                        + "\ngromacs_OUTPUT_PATH: " + gromacs_OUTPUT_PATH                        
                        + "\ngromacs_SOFTWARE: " + gromacs_SOFTWARE
                        + "\ngromacs_LOCAL_PROXY: " + gromacs_LOCAL_PROXY
                        + "\nTracking_DB_Hostname: " + TRACKING_DB_HOSTNAME
                        + "\nTracking_DB_Username: " + TRACKING_DB_USERNAME
                        + "\nTracking_DB_Password: " + TRACKING_DB_PASSWORD
                        + "\nSMTP_HOST: " + SMTP_HOST
                        + "\nSENDER_MAIL: " + SENDER_MAIL);                                            
                    }
                    
                    // Defining the WMS list for the "GISELA" Infrastructure
                    int nmax=0;
                    for (int i = 0; i < gisela_gromacs_WMS.length; i++)
                        if ((gisela_gromacs_WMS[i]!=null) && (!gisela_gromacs_WMS[i].equals("N/A"))) nmax++;
                    
                    String wmsList[] = new String [gisela_gromacs_WMS.length];
                    for (int i = 0; i < gisela_gromacs_WMS.length; i++)
                    {
                        if (gisela_gromacs_WMS[i]!=null) {
                        wmsList[i]=gisela_gromacs_WMS[i].trim();
                        log.info ("\n\nSubmitting for GISELA [" + i + "] using WMS=[" + wmsList[i] + "]");
                        }
                    }
                    
                    // Setting the JDL Requirements
                    String[] SOFTWARE_LIST = gromacs_SOFTWARE.split(",");
                    int index=0;
                    
                    for(String SOFTWARE: SOFTWARE_LIST)
                    {
                        jdlRequirements[index++] = 
                        "VO-"
                        + gisela_gromacs_VONAME
                        + "-"
                        + SOFTWARE;                            
                    }
                    
                    infrastructures[5] = new InfrastructureInfo(
                        gisela_gromacs_VONAME,
                        gisela_gromacs_TOPBDII,
                        wmsList,
                        gisela_gromacs_ETOKENSERVER,
                        gisela_gromacs_PORT,
                        gisela_gromacs_ROBOTID,
                        gisela_gromacs_VONAME,
                        gisela_gromacs_ROLE,
                        true, // set the RFC proxy for the infrastructure
                        jdlRequirements[0] + "," + jdlRequirements[1]);
                        //"VO-" + gisela_gromacs_VONAME + "-" + gromacs_SOFTWARE);                                                            
                }
                
                String[] GROMACS_Parameters = new String [7];

                // Upload the input settings for the application
                GROMACS_Parameters = uploadGromacsSettings( request, response, username );
                //String trimmed = GROMACS_Parameters[1].replaceAll("\\s", "_");

                log.info ("\n\nPreparing to start a GROMACS simulation with these parameters. ");
                log.info("\n- Input Parameters: ");
                log.info("\n- Version = " + GROMACS_Parameters[6]);
                log.info("\n- ASCII File = " + GROMACS_Parameters[0]);
                log.info("\n- Description = " + GROMACS_Parameters[1]);
                log.info("\n- WallClock Time = " + GROMACS_Parameters[5]);
                log.info("\n- GROMACS_CE = " + GROMACS_Parameters[2]);
                log.info("\n- Enable Notification = " + GROMACS_Parameters[3]);
                log.info("\n- Enable Demo = " + GROMACS_Parameters[4]);
                
                // Preparing to submit jobs in different grid infrastructure..
                //=============================================================
                // IMPORTANT: INSTANCIATE THE MultiInfrastructureJobSubmission
                //            CLASS USING THE EMPTY CONSTRUCTOR WHEN
                //            WHEN THE PORTLET IS DEPLOYED IN PRODUCTION!!!
                //=============================================================
                /*MultiInfrastructureJobSubmission GromacsMultiJobSubmission =
                new MultiInfrastructureJobSubmission(TRACKING_DB_HOSTNAME,
                                                     TRACKING_DB_USERNAME,
                                                     TRACKING_DB_PASSWORD);*/
                                
                MultiInfrastructureJobSubmission GromacsMultiJobSubmission =
                    new MultiInfrastructureJobSubmission();

                // Set the list of infrastructure(s) activated for the portlet           
                if (infrastructures[0]!=null) {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the LATO Infrastructure.");
                     GromacsMultiJobSubmission.addInfrastructure(infrastructures[0]); 
                }            
                if (infrastructures[1]!=null) {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the GARUDA Infrastructure.");
                     GromacsMultiJobSubmission.addInfrastructure(infrastructures[1]); 
                }
                if (infrastructures[2]!=null) {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the EUMED Infrastructure.");
                     GromacsMultiJobSubmission.addInfrastructure(infrastructures[2]);
                }
                if (infrastructures[3]!=null) {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the SAGRID Infrastructure.");
                     GromacsMultiJobSubmission.addInfrastructure(infrastructures[3]);
                }
                if (infrastructures[4]!=null) {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the SEE Infrastructure.");
                     GromacsMultiJobSubmission.addInfrastructure(infrastructures[4]);
                }
                if (infrastructures[5]!=null) {
                    if (gromacs_LOGLEVEL.trim().equals("VERBOSE"))
                        log.info("\n- Adding the GISELA Infrastructure.");
                     GromacsMultiJobSubmission.addInfrastructure(infrastructures[5]);
                }
                
                String GromacsFilesPath = getPortletContext().getRealPath("/") +
                                        "WEB-INF/config";                        
                
                // Set the Output path forresults
                //GromacsMultiJobSubmission.setOutputPath("/tmp");
                GromacsMultiJobSubmission.setOutputPath(gromacs_OUTPUT_PATH);
                            
                // Set the StandardOutput for GROMACS
                GromacsMultiJobSubmission.setJobOutput("std.txt");

                // Set the StandardError for GROMACS
                GromacsMultiJobSubmission.setJobError("std.err");
                
                // Set the Executable for GROMACS
                GromacsMultiJobSubmission.setExecutable("start_gromacs.sh");
                
                String InputSandbox = "";
                String Arguments = "";
                
                InfrastructureInfo infrastructure = 
                        GromacsMultiJobSubmission.getInfrastructure();
                    
                String Middleware = null;
                if (infrastructure.getMiddleware().equals("glite"))
                    Middleware = "gLite";
                    
                if (infrastructure.getMiddleware().equals("gatekeeper"))
                    Middleware = "GARUDA";
                    
                if (infrastructure.getMiddleware().equals("ssh"))
                    Middleware = "ssh";
                
                String gromacs_DEFAULT_STORAGE = "";
                if (gromacs_LOGLEVEL.trim().equals("VERBOSE")) 
                {
                    log.info("\n- Selected Infrastructure = " 
                            + infrastructure.getName());
                    
                    if (infrastructure.getName().equals("eumed"))
                        gromacs_DEFAULT_STORAGE = eumed_gromacs_WEBDAV;
                    if (infrastructure.getName().equals("see"))
                        gromacs_DEFAULT_STORAGE = see_gromacs_WEBDAV;
                    if (infrastructure.getName().equals("gisela"))
                        gromacs_DEFAULT_STORAGE = gisela_gromacs_WEBDAV;
                
                    log.info("\n- Enabled Middleware = " + Middleware);
                    log.info("\n- WebDAV Storage = " + gromacs_DEFAULT_STORAGE);
                }                
                                          
                if (GROMACS_Parameters[4]!=null) {
                    // Demo ?
                    InputSandbox = 
                            GromacsFilesPath + "/start_gromacs.sh" + "," 
                          + GromacsFilesPath + "/em1.tpr" + ","
                          + GromacsFilesPath + "/curl" + ","
                          + GromacsFilesPath + "/libcurl.so.3" + ","
                          + GromacsFilesPath + "/libssl.so.6" + ","
                          + GromacsFilesPath + "/libcrypto.so.6";
                    
                    Arguments = 
                            GromacsFilesPath + "/em1.tpr" + ","
                          + Middleware + "," 
                          + gromacs_METADATA_HOST + ","
                          + username + "," 
                          + GROMACS_Parameters[1].replaceAll("\\s", "_") + ","                          
                          + gromacs_DEFAULT_STORAGE + ","
                          + GROMACS_Parameters[6];
                } else {
                    // User's file
                    InputSandbox = 
                        GromacsFilesPath + "/start_gromacs.sh" + "," 
                        + GROMACS_Parameters[0] + ","
                        + GromacsFilesPath + "/curl" + ","
                        + GromacsFilesPath + "/libcurl.so.3" + ","
                        + GromacsFilesPath + "/libssl.so.6" + ","
                        + GromacsFilesPath + "/libcrypto.so.6";
                    
                    Arguments = 
                            GROMACS_Parameters[0] + "," + Middleware + ","
                          + gromacs_METADATA_HOST + ","
                          + username + ","
                          + GROMACS_Parameters[1].replaceAll("\\s", "_") + ","                          
                          + gromacs_DEFAULT_STORAGE + ","
                          + GROMACS_Parameters[6]; 
                }
                
                // Set the list of Arguments for GROMACS
                GromacsMultiJobSubmission.setArguments(Arguments);
                
                // Set InputSandbox files (string with comma separated list of file names)
                GromacsMultiJobSubmission.setInputFiles(InputSandbox);                                

                // OutputSandbox (string with comma separated list of file names)
                String GROMACS_LOG = "gromacs.log";
                String ENV_LOG = "env.log";
                String CURL_LOG = "curl.log";
                String README = "output.README";             

                // Set the OutputSandbox files (string with comma separated list of file names)
                GromacsMultiJobSubmission
                        .setOutputFiles(GROMACS_LOG + "," 
                        + ENV_LOG + "," + CURL_LOG + "," + README); 
                
                // Set the MaxWallClockTime Requirements
                String MaxWallClockTimeRequirements[] = new String[1];
                MaxWallClockTimeRequirements[0] = 
                        "JDLRequirements=(other.GlueCEPolicyMaxWallClockTime>"
                        + GROMACS_Parameters[5]
                        + ")";
                
                GromacsMultiJobSubmission
                        .setJDLRequirements(MaxWallClockTimeRequirements);
                
                // Check if more than one infrastructure have been enabled
                // If NMAX>1 this option is disabled.
                if (MAX==1) {
                    
                    String gromacs_VONAME = "";
                    String gromacs_TOPBDII = "";
                    String RANDOM_CE = "";                    
                    int MAXWallClockTime = Integer.parseInt(GROMACS_Parameters[5]);
                    
                    if (eumed_gromacs_ENABLEINFRASTRUCTURE != null &&
                        eumed_gromacs_ENABLEINFRASTRUCTURE.equals("eumed")) 
                    {
                        // Getting the GROMACS VONAME from the portlet preferences for the EUMED VO
                        gromacs_VONAME = portletPreferences.getValue("eumed_gromacs_VONAME", "N/A");
                        // Getting the GROMACS TOPBDII from the portlet preferences for the EUMED VO
                        gromacs_TOPBDII = portletPreferences.getValue("eumed_gromacs_TOPBDII", "N/A");
                        
                        if (!GROMACS_Parameters[2].isEmpty()) {
                            log.info("\n- Submitting of the job to the CE [ " 
                                    + GROMACS_Parameters[2] 
                                    + " ] in progress...");
                            
                            GromacsMultiJobSubmission
                                .setJobQueue(GROMACS_Parameters[2]);
                        } else { 
                        // Get the random CE for the GROMACS portlet
                        RANDOM_CE = getRandomCE(gromacs_VONAME, gromacs_TOPBDII, 
                                                gromacs_SOFTWARE, MAXWallClockTime, "");
                        
                        log.info("\n- Submitting of the job to the CE [ " 
                                + RANDOM_CE 
                                + " ] in progress...");
                        
                        GromacsMultiJobSubmission
                                .setJobQueue(RANDOM_CE.toString().trim());
                        }
                    }
                    
                    if (gisela_gromacs_ENABLEINFRASTRUCTURE != null &&
                        gisela_gromacs_ENABLEINFRASTRUCTURE.equals("eumed")) 
                    {
                        // Getting the GROMACS VONAME from the portlet preferences for the GISELA VO
                        gromacs_VONAME = portletPreferences.getValue("gisela_gromacs_VONAME", "N/A");
                        // Getting the GROMACS TOPBDII from the portlet preferences for the GISELA VO
                        gromacs_TOPBDII = portletPreferences.getValue("gisela_gromacs_TOPBDII", "N/A");
                        
                        if (!GROMACS_Parameters[2].isEmpty()) {
                            log.info("\n- Submitting of the job to the CE [ " 
                                    + GROMACS_Parameters[2] 
                                    + " ] in progress...");
                            
                            GromacsMultiJobSubmission
                                .setJobQueue(GROMACS_Parameters[2]);
                        } else { 
                        // Get the random CE for the GROMACS portlet
                        RANDOM_CE = getRandomCE(gromacs_VONAME, gromacs_TOPBDII, 
                                                gromacs_SOFTWARE, MAXWallClockTime, "");
                        
                        log.info("\n- Submitting of the job to the CE [ " 
                                + RANDOM_CE 
                                + " ] in progress...");
                        
                        GromacsMultiJobSubmission
                                .setJobQueue(RANDOM_CE.toString().trim());
                        }
                    }
                    
                    if (see_gromacs_ENABLEINFRASTRUCTURE != null &&
                        see_gromacs_ENABLEINFRASTRUCTURE.equals("see")) 
                    {
                        // Getting the GROMACS VONAME from the portlet preferences for the SEE VO
                        gromacs_VONAME = portletPreferences.getValue("see_gromacs_VONAME", "N/A");
                        // Getting the GROMACS TOPBDII from the portlet preferences for the SEE VO
                        gromacs_TOPBDII = portletPreferences.getValue("see_gromacs_TOPBDII", "N/A");
                        
                        if (!GROMACS_Parameters[2].isEmpty()) {
                            log.info("\n- Submitting of the job to the CE [ " 
                                    + GROMACS_Parameters[2] 
                                    + " ] in progress...");
                            
                            GromacsMultiJobSubmission
                                .setJobQueue(GROMACS_Parameters[2]);
                        } else { 
                        // Get the random CE for the GROMACS portlet
                        RANDOM_CE = getRandomCE(gromacs_VONAME, gromacs_TOPBDII, 
                                                gromacs_SOFTWARE, MAXWallClockTime, "");
                        
                        log.info("\n- Submitting of the job to the CE [ " 
                                + RANDOM_CE 
                                + " ] in progress...");
                        
                        GromacsMultiJobSubmission
                                .setJobQueue(RANDOM_CE.toString().trim());
                        }
                    }
                }
                
                InetAddress addr = InetAddress.getLocalHost();
                //Company company;
                
                try {
                    company = PortalUtil.getCompany(request);
                    String gateway = company.getName();
                    
                    // Send a notification email to the user if enabled.
                    if (GROMACS_Parameters[3]!=null)
                        if ( (SMTP_HOST==null) || 
                             (SMTP_HOST.trim().equals("")) ||
                             (SMTP_HOST.trim().equals("N/A")) ||
                             (SENDER_MAIL==null) || 
                             (SENDER_MAIL.trim().equals("")) ||
                             (SENDER_MAIL.trim().equals("N/A"))
                           )
                        log.info ("\nThe Notification Service is not properly configured!!");
                    else {
                                // Setting the user's email for notifications
                                GromacsMultiJobSubmission
                                        .setUserEmail(emailAddress);
                                
                                // Setting the Sender
                                if (!SENDER_MAIL.isEmpty())
                                    GromacsMultiJobSubmission
                                            .setSenderEmail(SENDER_MAIL);
                            
                                if (GROMACS_Parameters[6].equals("v4_6_5"))
                                    sendHTMLEmail(username, 
                                           emailAddress, 
                                           SENDER_MAIL, 
                                           SMTP_HOST, 
                                           "GROMACS-4.6.5", 
                                           gateway);
                                else
                                    sendHTMLEmail(username, 
                                           emailAddress, 
                                           SENDER_MAIL, 
                                           SMTP_HOST, 
                                           "GROMACS-5.0.4", 
                                           gateway);
                    }                                        
                                    
                    log.info("\n- Submission in progress ...");
                    GromacsMultiJobSubmission.submitJobAsync(
                            infrastructure,
                            username,
                            addr.getHostAddress()+":8162",
                            Integer.valueOf(gromacs_APPID),
                            GROMACS_Parameters[1]);
                    
                } catch (PortalException ex) {
                    Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SystemException ex) {
                    Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
                }                                    
            } // end PROCESS ACTION [ SUBMIT_GROMACS_PORTLET ]
        } catch (PortalException ex) {
            Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response)
                throws PortletException, IOException
    {
        //super.serveResource(request, response);
        PortletPreferences portletPreferences = (PortletPreferences) request.getPreferences();

        final String action = (String) request.getParameter("action");

        if (action.equals("get-ratings")) {
            //Get CE Ratings from the portlet preferences
            String gromacs_CE = (String) request.getParameter("gromacs_CE");

            String json = "{ \"avg\":\"" + 
                    portletPreferences.getValue(gromacs_CE+"_avg", "0.0") +
                    "\", \"cnt\":\"" + portletPreferences.getValue(gromacs_CE+"_cnt", "0") + "\"}";

            response.setContentType("application/json");
            response.getPortletOutputStream().write( json.getBytes() );

        } else if (action.equals("set-ratings")) {

            String gromacs_CE = (String) request.getParameter("gromacs_CE");
            int vote = Integer.parseInt(request.getParameter("vote"));

             double avg = Double.parseDouble(portletPreferences.getValue(gromacs_CE+"_avg", "0.0"));
             long cnt = Long.parseLong(portletPreferences.getValue(gromacs_CE+"_cnt", "0"));

             portletPreferences.setValue(gromacs_CE+"_avg", Double.toString(((avg*cnt)+vote) / (cnt +1)));
             portletPreferences.setValue(gromacs_CE+"_cnt", Long.toString(cnt+1));

             portletPreferences.store();
        }
    }


    // Upload GROMACS input files
    public String[] uploadGromacsSettings(ActionRequest actionRequest,
                                          ActionResponse actionResponse, 
                                          String username)
    {
        String[] GROMACS_Parameters = new String [7];
        boolean status;

        // Check that we have a file upload request
        boolean isMultipart = PortletFileUpload.isMultipartContent(actionRequest);

        if (isMultipart) {
            // Create a factory for disk-based file items.
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Set factory constrains
            File GROMACS_Repository = new File ("/tmp");
            if (!GROMACS_Repository.exists()) status = GROMACS_Repository.mkdirs();
            factory.setRepository(GROMACS_Repository);

            // Create a new file upload handler.
            PortletFileUpload upload = new PortletFileUpload(factory);

            try {
                    // Parse the request
                    List items = upload.parseRequest(actionRequest);
                    // Processing items
                    Iterator iter = items.iterator();

                    while (iter.hasNext())
                    {
                        FileItem item = (FileItem) iter.next();
                        String fieldName = item.getFieldName();
                        
                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        String timeStamp = dateFormat.format(Calendar.getInstance().getTime());

                        // Processing a regular form field
                        if ( item.isFormField() )
                        {                                                        
                            if (fieldName.equals("gromacs_desc"))
                                if (item.getString().equals("Please, insert here a description"))
                                    GROMACS_Parameters[1]="GROMACS Simulation Started";
                                else
                                    GROMACS_Parameters[1]=item.getString();                            
                                                        
                            if (fieldName.equals("gromacs_CE"))
                                GROMACS_Parameters[2]=item.getString();                                                       
                            
                        } else {
                            // Processing a file upload
                            if (fieldName.equals("gromacs_file"))
                            {                                                               
                                log.info("\n- Uploading the following user's file: "
                                       + "\n[ " + item.getName() + " ]"
                                       + "\n[ " + item.getContentType() + " ]"
                                       + "\n[ " + item.getSize() + "KBytes ]"
                                       );                                                                

                                // Writing the file to disk
                                String uploadGromacsFile = 
                                        GROMACS_Repository +
                                        "/" + timeStamp +
                                        "_" + username +
                                        "_" + item.getName();

                                log.info("\n- Writing the user's file: [ "
                                        + uploadGromacsFile.toString()
                                        + " ] to disk");

                                item.write(new File(uploadGromacsFile));
                                
                                GROMACS_Parameters[0]=uploadGromacsFile;                                                                
                            }
                        }
                        
                        if (fieldName.equals("EnableNotification"))
                                GROMACS_Parameters[3]=item.getString();
                        
                        if (fieldName.equals("EnableDemo"))
                                GROMACS_Parameters[4]=item.getString();
                        
                        if (fieldName.equals("gromacs_maxwallclocktime"))
                                GROMACS_Parameters[5]=item.getString();
                        
                        if (fieldName.equals("gromacs_version"))
                                GROMACS_Parameters[6]=item.getString();
                    } // end while
            } catch (FileUploadException ex) {
              Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
              Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return GROMACS_Parameters;
    }
    
    // Retrieve a random Computing Element
    // matching the Software Tag for the application    
    public String getRandomCE(String gromacs_VONAME,
                              String gromacs_TOPBDII,
                              String gromacs_SOFTWARE,
                              Integer gromacs_MaxCPUTime,
                              String selected)
                              throws PortletException, IOException
    {
        String randomWCT_CE = null;
        String randomCE = null;
        List<String> CEqueues = null;
        BDII bdii = null;                   
                        
        String[] SOFTWARE_LIST = gromacs_SOFTWARE.split(",");
        
        for(String SOFTWARE: SOFTWARE_LIST)
        {
            log.info("\n- Querying the Information System [ "
                      + gromacs_TOPBDII
                      + " ] and fetching a random CE matching the SW tag [ VO-"
                      + gromacs_VONAME
                      + "-"
                      + SOFTWARE + " ]");  

            try {               

                bdii = new BDII( new URI(gromacs_TOPBDII) );               
                
                // Get the list of the available queues
                CEqueues = bdii.queryCEQueues(gromacs_VONAME);                
                                
                // Get the random CE matching the Software and the MaxWallClockTime reqs.
                randomWCT_CE =          
                        bdii.getRandomCEFromSWTag_MaxWallClockTime(  
                        "VO-" + gromacs_VONAME + "-" + SOFTWARE,
                        gromacs_VONAME,
                        gromacs_MaxCPUTime);
                
                // Fetching the Queues
                for (String CEqueue:CEqueues) {                        
                        if (CEqueue.contains(randomWCT_CE))
                            randomCE=CEqueue;
                }                                

            } catch (URISyntaxException ex) {
                    Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                    Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (selected.isEmpty()) 
        log.info("\n- [getRandomCE] Selected *randomly* the following cluster = " + randomCE);
        
        return randomCE;
    }        

    // Retrieve the list of Computing Elements
    // matching the Software Tag for the GROMACS application    
    public List<String> getListofCEForSoftwareTag(String gromacs_VONAME,
                                                  String gromacs_TOPBDII,
                                                  String gromacs_SOFTWARE)
                                throws PortletException, IOException
    {
        List<String> CEs_list = null;
        BDII bdii = null;
        
        log.info("\n- Querying the Information System [ "
                     + gromacs_TOPBDII
                     + " ] and looking for CEs matching SW tag [ VO-"
                     + gromacs_VONAME
                     + "-"
                     + gromacs_SOFTWARE + " ]");  

            try {

                    bdii = new BDII( new URI(gromacs_TOPBDII) );                
                    CEs_list = bdii.queryCEForSWTag(
                               "VO-"
                               + gromacs_VONAME
                               + "-"
                               + gromacs_SOFTWARE);

            } catch (URISyntaxException ex) {
                    Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                    Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
            }        

        return CEs_list;
    }

    // Get the GPS location of the given grid resource
    public String[] getCECoordinate(RenderRequest request,
                                    String CE)
                                    throws PortletException, IOException
    {
        String[] GPS_locations = null;
        BDII bdii = null;

        PortletPreferences portletPreferences =
                (PortletPreferences) request.getPreferences();

        // Getting the GROMACS TOPPBDII from the portlet preferences
        String garuda_gromacs_TOPBDII = 
                portletPreferences.getValue("garuda_gromacs_TOPBDII", "N/A");
        String eumed_gromacs_TOPBDII = 
                portletPreferences.getValue("eumed_gromacs_TOPBDII", "N/A");
        String sagrid_gromacs_TOPBDII = 
                portletPreferences.getValue("sagrid_gromacs_TOPBDII", "N/A");
        String see_gromacs_TOPBDII = 
                portletPreferences.getValue("see_gromacs_TOPBDII", "N/A");
        String gisela_gromacs_TOPBDII = 
                portletPreferences.getValue("gisela_gromacs_TOPBDII", "N/A");
        
        // Getting the GROMACS ENABLEINFRASTRUCTURE from the portlet preferences
        String gromacs_ENABLEINFRASTRUCTURE = 
                portletPreferences.getValue("gromacs_ENABLEINFRASTRUCTURE", "N/A");

            try {
                if ( gromacs_ENABLEINFRASTRUCTURE.equals("garuda") )
                     bdii = new BDII( new URI(garuda_gromacs_TOPBDII) );

                if ( gromacs_ENABLEINFRASTRUCTURE.equals("eumed") )
                     bdii = new BDII( new URI(eumed_gromacs_TOPBDII) );
                
                if ( gromacs_ENABLEINFRASTRUCTURE.equals("sagridd") )
                     bdii = new BDII( new URI(sagrid_gromacs_TOPBDII) );

                if ( gromacs_ENABLEINFRASTRUCTURE.equals("see") )
                    bdii = new BDII( new URI(see_gromacs_TOPBDII) );
                
                if ( gromacs_ENABLEINFRASTRUCTURE.equals("gisela") )
                    bdii = new BDII( new URI(gisela_gromacs_TOPBDII) );

                GPS_locations = bdii.queryCECoordinate("ldap://" + CE + ":2170");

            } catch (URISyntaxException ex) {
                Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);
            }

            return GPS_locations;
    }
    
    private void sendHTMLEmail (String USERNAME,
                                String TO, 
                                String FROM, 
                                String SMTP_HOST, 
                                String ApplicationAcronym,
                                String GATEWAY)
    {
                
        log.info("\n- Sending email notification to the user " + USERNAME + " [ " + TO + " ]");
        
        log.info("\n- SMTP Server = " + SMTP_HOST);
        log.info("\n- Sender = " + FROM);
        log.info("\n- Receiver = " + TO);
        log.info("\n- Application = " + ApplicationAcronym);
        log.info("\n- Gateway = " + GATEWAY);        
        
        // Assuming you are sending email from localhost
        String HOST = "localhost";
        
        // Get system properties
        Properties properties = System.getProperties();
        properties.setProperty(SMTP_HOST, HOST);
        
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        
        try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(FROM));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));
         //message.addRecipient(Message.RecipientType.CC, new InternetAddress(FROM));

         // Set Subject: header field
         message.setSubject(" [liferay-sg-gateway] - [ " + GATEWAY + " ] ");

	 Date currentDate = new Date();
	 currentDate.setTime (currentDate.getTime());

         // Send the actual HTML message, as big as you like
         message.setContent(
	 "<br/><H4>" +         
         "<img src=\"http://fbcdn-profile-a.akamaihd.net/hprofile-ak-snc6/195775_220075701389624_155250493_n.jpg\" width=\"100\">Science Gateway Notification" +
	 "</H4><hr><br/>" +
         "<b>Description:</b> Notification for the application <b>[ " + ApplicationAcronym + " ]</b><br/><br/>" +         
         "<i>The application has been successfully submitted from the [ " + GATEWAY + " ]</i><br/><br/>" +
         "<b>TimeStamp:</b> " + currentDate + "<br/><br/>" +
	 "<b>Disclaimer:</b><br/>" +
	 "<i>This is an automatic message sent by the Science Gateway based on Liferay technology.<br/>" + 
	 "If you did not submit any jobs through the Science Gateway, please " +
         "<a href=\"mailto:" + FROM + "\">contact us</a></i>",
	 "text/html");

         // Send message
         Transport.send(message);         
      } catch (MessagingException ex) { 
          Logger.getLogger(Gromacs.class.getName()).log(Level.SEVERE, null, ex);          
      }
    }
    
    /*private void getRobotProxy (String eTokenServer, 
                                String eTokenServerPort, 
                                String proxyId, 
                                String VO, 
                                String FQAN, 
                                String proxyRenewal) 
    {
        File proxyFile;
        proxyFile = new File("/tmp/proxy_RFC");
        
        String proxyContent="";
        
        try {
            
            URL proxyURL = 
                    new URL("http://" 
                    + eTokenServer + ":" + eTokenServerPort
                    + "/eTokenServer/eToken/" + proxyId 
                    + "?voms=" + VO + ":/" + VO + "&proxy-renewal=" + proxyRenewal
                    + "&disable-voms-proxy=false&rfc-proxy=true&cn-label=Empty");            
                    
            URLConnection proxyConnection = proxyURL.openConnection();
            proxyConnection.setDoInput(true);
            
            InputStream proxyStream = proxyConnection.getInputStream();
            BufferedReader input = new BufferedReader(new  InputStreamReader(proxyStream));
            
            String line = "";
            while ((line = input.readLine()) != null)                                 
                proxyContent += line+"\n";
                        
            FileUtils.writeStringToFile(proxyFile, proxyContent);
        } catch (Exception e) { e.printStackTrace(); }        
    }*/
}
