<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>

    <xsl:template match="/class">
        <xsl:variable name="_soapVersion"><xsl:value-of select="@soap-version"/></xsl:variable>
        <html>
			<head>
				<title>
					<xsl:value-of select="@name"/>
				</title>

                <script type="text/javascript">
                    <!-- template call for inline scripts -->
                    <xsl:call-template name="_javascripts">
                        <xsl:with-param name="_soapVersion" select="$_soapVersion"/>
                    </xsl:call-template>
                </script>

                <style>
                    <!-- template call for style -->
                    <xsl:call-template name="_dynamic_client.css"/>
                </style>

              </head>
            <body>

                <div id="_open_breadcrumbs_div"/>

                <xsl:call-template name="_template_breadcrumbs_list_operations"/>
                <h3>
					<xsl:value-of select="@name"/>
				</h3>
                <xsl:call-template name="_templateNameOperations"/>
                <!-- template call for body-->
                <xsl:call-template name="_templateNameRequest"/>

                <xsl:call-template name="_templateNameResponse"/>

                <!-- Fault handling -->

                <xsl:call-template name="_template_fault_handling"/>

            </body>
        </html>
    </xsl:template>

    <xsl:template name="_template_breadcrumbs_list_operations">
        <script>
            <![CDATA[

                   var _text = "<h4><a href=\"#\" onClick=\"top.showServiceInitializer(); return false;\">Services</a>&nbsp;&gt;&nbsp;" +
                                   "<a href=\"#\" onClick=\"top.listServiceData('"+ top.getServiceSpecificName() +"'); return false;\">"+ top.getServiceSpecificName() + "</a>&nbsp;&gt;&nbsp;" + "Try!" +"</h4>";
                   document.getElementById('_open_breadcrumbs_div').innerHTML = _text;
                   ]]>
        </script>
    </xsl:template>

    <xsl:template name="_template_fault_handling">
        <div style="display:none;">
            <xsl:attribute name="id">_fault_handling_div_id</xsl:attribute>
            <h5>SOAP Fault occured when communicating with the server</h5>
            <textarea>
                 <xsl:attribute name="id">_fault_handling_ta_id</xsl:attribute>
                 <xsl:attribute name="style">border:1px solid blue;</xsl:attribute>
                 <xsl:attribute name="cols">90</xsl:attribute>
                 <xsl:attribute name="rows">25</xsl:attribute>
            </textarea>
        </div>
    </xsl:template>

    <xsl:template name="_templateNameOperations">
        <div id='_divOperations'>
            <h3>Operations Available</h3>
            <table class='styled'>
                <tr>
                    <th>Operation Name</th>
                </tr>

                    <xsl:for-each select="//class/method">
                      <tr>
                       <td>
                           <a>
                               <xsl:attribute name="href">#</xsl:attribute>
                               <xsl:attribute name="onClick">_request(document.getElementById('_divOperations'),document.getElementById('<xsl:value-of select="@name"/>'),'<xsl:value-of select="@name"/>',document.getElementById('_open_breadcrumbs_div'));return false;</xsl:attribute>
                               <xsl:value-of select="@name"/>
                           </a>
                       </td>
                      </tr>
                    </xsl:for-each>

            </table>
        </div>
    </xsl:template>


    <xsl:template name="_bean_template_request">
        <xsl:param name="_type"/>
        <xsl:param name="_nsuri"/>
        <xsl:param name="_nsprefix"/>
        <xsl:param name="_method"/>

        <xsl:param name="_soapaction"/>
        <xsl:param name="_request_table_id"/>
        <xsl:param name="_original_method_name"/>


        <xsl:for-each select="//class/bean">
             <xsl:choose>
                 <xsl:when test="$_type=@name">
                     <xsl:variable name="_typeRequest" select="property/@type"/>
                     <xsl:variable name="_ours" select="property/@ours"/>
                    <!-- Variable Needed for Soap body development -->
                     <xsl:variable name="_nsuri" select="@nsuri"/>
                     <xsl:variable name="_nsprefix" select="@nsprefix"/>
                     <xsl:variable name="_method" select="@originalName"/>

                     <xsl:choose>
                         <xsl:when test="$_ours">
                             <!--  Recursion goes here-->
                             <xsl:call-template name="_bean_template_request">
                                  <xsl:with-param name="_type" select="$_typeRequest"/>
                                  <xsl:with-param name="_nsuri" select="$_nsuri"/>
                                  <xsl:with-param name="_nsprefix" select="$_nsprefix"/>
                                  <xsl:with-param name="_method" select="$_method"/>
                                  <xsl:with-param name="_soapaction" select="$_soapaction"/>
                                  <xsl:with-param name="_request_table_id" select="$_request_table_id"/>
                                  <xsl:with-param name="_original_method_name" select="$_original_method_name"/>
                             </xsl:call-template>
                         </xsl:when>
                         <xsl:otherwise>
                             <!-- property goes here-->
                             <table class='styled'>
                                 <xsl:attribute name="id"><xsl:value-of select="$_request_table_id"/></xsl:attribute>
                                 <xsl:choose>
                                       <xsl:when test="property"> <!-- If atleast one property exist we put a header-->
                                              <tr>
                                                  <th>
                                                      Name
                                                  </th>
                                                  <th>
                                                      Type
                                                  </th>
                                                  <th>
                                                      Request Value
                                                  </th>
                                              </tr>
                                          <xsl:for-each select="property">

                                              <xsl:choose>
                                                  <xsl:when test="@unbound">
                                                          <!-- TODO FixMe-->
                                                          <xsl:variable name="_generated_id"><xsl:value-of select="generate-id()"/></xsl:variable>
                                                          <tr>
                                                              <xsl:attribute name="id"><xsl:value-of select="$_generated_id"/>_tr_array</xsl:attribute>
                                                              <td>
                                                                  <xsl:value-of select="@name"/>
                                                              </td>
                                                              <td>
                                                                  <xsl:value-of select="@shorttypename"/>
                                                              </td>
                                                              <td>
                                                                  <input type='text'>
                                                                      <xsl:attribute name="id"><xsl:value-of select="$_generated_id"/></xsl:attribute>
                                                                      <xsl:attribute name="name"><xsl:value-of select="$_generated_id"/></xsl:attribute>
                                                                  </input>
                                                              </td>
                                                              <td>
                                                                  <input type="button">
                                                                      <xsl:attribute name="onClick">javascrpt:_create_table_raw_with_data(document.getElementById('<xsl:value-of select="$_generated_id"/>_tr_array'),'<xsl:value-of select="@shorttypename"/>','<xsl:value-of select="$_generated_id"/>');return false;</xsl:attribute>
                                                                      <xsl:attribute name="value">Add</xsl:attribute>
                                                                  </input>
                                                              </td>
                                                          </tr>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                           <tr>
                                                              <td>
                                                                  <xsl:value-of select="@name"/>
                                                              </td>
                                                              <td>
                                                                  <xsl:value-of select="@shorttypename"/>
                                                              </td>
                                                              <td>
                                                                  <input type='text'>
                                                                      <xsl:attribute name="id"><xsl:value-of select="generate-id()"/></xsl:attribute>
                                                                      <xsl:attribute name="name"><xsl:value-of select="generate-id()"/></xsl:attribute>
                                                                  </input>
                                                              </td>
                                                          </tr>
                                                  </xsl:otherwise>
                                             </xsl:choose>
                                          </xsl:for-each>
                                     </xsl:when>
                                 </xsl:choose>
                             </table>
                                 <input value='Try' type='button'>
                                         <xsl:attribute name="onClick">
                                            var arrayOfNames = new Array();
                                            var arrayOfValues = new Array();
                                            var arryaOfInputObjsIds = new Array();
                                            <xsl:for-each select="property">
                                                 arrayOfNames[<xsl:value-of select="position()-1"/>] = '<xsl:value-of select="@name"/>';
                                                 arrayOfValues[<xsl:value-of select="position()-1"/>] = (document.getElementById('<xsl:value-of select="generate-id()"/>').value);
                                                 arryaOfInputObjsIds[<xsl:value-of select="position()-1"/>] = '<xsl:value-of select="generate-id()"/>';
                                            </xsl:for-each>
                                             _send_request(arryaOfInputObjsIds,arrayOfNames,arrayOfValues,'<xsl:value-of select="$_soapaction"/>','<xsl:value-of select="$_nsuri"/>','<xsl:value-of select="$_nsprefix"/>','<xsl:value-of select="$_method"/>',_fnr_<xsl:value-of select="$_original_method_name"/>_Response_Table_Id(),1);return false;

                                         </xsl:attribute>

                                 </input>
                                 <input>
                                     <xsl:attribute name="value">Reset</xsl:attribute>
                                     <xsl:attribute name="type">button</xsl:attribute>
                                     <xsl:attribute name="onClick">_reset_request_table(document.getElementById('<xsl:value-of select="$_request_table_id"/>'));return false;</xsl:attribute>
                                 </input>

                         </xsl:otherwise>
                     </xsl:choose>

                 </xsl:when>
             </xsl:choose>

        </xsl:for-each>


    </xsl:template>

    <xsl:template name="_bean_template_response_javascripts"> <!-- FixMe-->
                <xsl:param name="_type"/>
                <xsl:param name="_nsuri"/>
                <xsl:param name="_nsprefix"/>
                <xsl:param name="_method"/>

                <xsl:param name="_soapaction"/>
                <xsl:param name="_response_table_id"/>


                <xsl:for-each select="//class/bean">
                     <xsl:choose>
                         <xsl:when test="$_type=@name">
                             <xsl:variable name="_typeResponse" select="property/@type"/>
                             <xsl:variable name="_ours" select="property/@ours"/>
                            <!-- Variable Needed for Soap body development -->
                             <xsl:variable name="_nsuri" select="@nsuri"/>
                             <xsl:variable name="_nsprefix" select="@nsprefix"/>
                             <xsl:variable name="_method" select="@originalName"/>

                             <xsl:choose>
                                 <xsl:when test="$_ours">
                                     <!-- Recursion goes here-->
                                     <xsl:call-template name="_bean_template_response_javascripts">
                                          <xsl:with-param name="_type" select="$_typeResponse"/>
                                          <xsl:with-param name="_nsuri" select="$_nsuri"/>
                                          <xsl:with-param name="_nsprefix" select="$_nsprefix"/>
                                          <xsl:with-param name="_method" select="$_method"/>
                                          <xsl:with-param name="_soapaction" select="$_soapaction"/>
                                          <xsl:with-param name="_response_table_id" select="$_response_table_id"/>
                                     </xsl:call-template>
                                 </xsl:when>
                                 <xsl:otherwise>

                                      function _fnr_<xsl:value-of select="$_response_table_id"/>(){ <!-- TODO FixMe For Generality-->
                                      var _response_names = new Array();
                                      <xsl:for-each select="property">
                                            <xsl:choose>
                                                <xsl:when test="@unbound">
                                                     <!-- TODO FixMe simple Arryas-->
                                                     //for arrays
                                                    _response_names[<xsl:value-of select="position()-1"/>]=new Array('aa','<xsl:value-of select="@name"/>');
                                                </xsl:when>
                                                <!--xsd:anyType Handling -->
                                                <xsl:when test="@type='org.apache.axiom.om.OMElement'">
                                                     // for xsd:anyType
                                                    _response_names[<xsl:value-of select="position()-1"/>]=new Array('at','<xsl:value-of select="@name"/>');
                                                </xsl:when>
                                                <xsl:otherwise>

                                                   _response_names[<xsl:value-of select="position()-1"/>]=new Array('rr','<xsl:value-of select="@name"/>');
                                               </xsl:otherwise>
                                           </xsl:choose>
                                      </xsl:for-each>
                                         return _response_names;
                                       }
                                 </xsl:otherwise>
                             </xsl:choose>

                         </xsl:when>

                     </xsl:choose>

                </xsl:for-each>


    </xsl:template>

    <xsl:template name="_bean_template_response">
        <xsl:param name="_type"/>
        <xsl:param name="_nsuri"/>
        <xsl:param name="_nsprefix"/>
        <xsl:param name="_method"/>

        <xsl:param name="_soapaction"/>
        <xsl:param name="_response_table_id"/>


        <xsl:for-each select="//class/bean">
             <xsl:choose>
                 <xsl:when test="$_type=@name">
                     <xsl:variable name="_typeResponse" select="property/@type"/>
                     <xsl:variable name="_ours" select="property/@ours"/>
                    <!-- Variable Needed for Soap body development -->
                     <xsl:variable name="_nsuri" select="@nsuri"/>
                     <xsl:variable name="_nsprefix" select="@nsprefix"/>
                     <xsl:variable name="_method" select="@originalName"/>

                     <xsl:choose>
                         <xsl:when test="$_ours">
                             <!-- Recursion goes here-->
                             <xsl:call-template name="_bean_template_response">
                                  <xsl:with-param name="_type" select="$_typeResponse"/>
                                  <xsl:with-param name="_nsuri" select="$_nsuri"/>
                                  <xsl:with-param name="_nsprefix" select="$_nsprefix"/>
                                  <xsl:with-param name="_method" select="$_method"/>
                                  <xsl:with-param name="_soapaction" select="$_soapaction"/>
                                  <xsl:with-param name="_response_table_id" select="$_response_table_id"/>
                             </xsl:call-template>
                         </xsl:when>
                         <xsl:otherwise>
                             <!--property goes here-->
                             <table class='styled'>
                                 <xsl:attribute name="id"><xsl:value-of select="$_response_table_id"/></xsl:attribute>
                                 <xsl:choose>
                                     <xsl:when test="property"> <!--If only one property exists we put a header -->
                                             <tr>
                                                 <th>
                                                     Name
                                                 </th>
                                                 <th>
                                                     Type
                                                 </th>
                                             </tr>
                                         <xsl:for-each select="property">
                                             <tr>
                                                 <td>
                                                     <xsl:value-of select="@name"/>
                                                 </td>
                                                 <td>
                                                     <xsl:value-of select="@type"/>
                                                 </td>

                                             </tr>

                                         </xsl:for-each>
                                     </xsl:when>
                                 </xsl:choose>
                              </table>

                         </xsl:otherwise>
                     </xsl:choose>

                 </xsl:when>
             </xsl:choose>

        </xsl:for-each>


    </xsl:template>


    <xsl:template name="_templateNameRequest">
        <xsl:for-each select="//class/method">
            <xsl:variable name="_type" select="input/param/@type"/>
            <xsl:variable name="_nsuri">""</xsl:variable>
            <xsl:variable name="_nsprefix">""</xsl:variable>
            <xsl:variable name="_method" select="@name"/>
            <xsl:variable name="_soapaction" select="@soapaction"/>
            <xsl:variable name="_request_table_id"><xsl:value-of select="@name"/>_Request_Table_Id</xsl:variable>
            <xsl:variable name="_original_method_name" select="@name"/>


            <xsl:choose>
                 <xsl:when test="$_type=''">
                     <!-- When message's input is not available -->
                     <div style='display:none;'>
                        <xsl:attribute name="id"><xsl:value-of select="@name"/></xsl:attribute>
                             <table>
                                    <xsl:attribute name="id"><xsl:value-of select="$_request_table_id"/></xsl:attribute>
                             </table>
                             <input value='Try' type='button'>
                                     <xsl:attribute name="onClick">
                                        var arrayOfNames = new Array();
                                        var arrayOfValues = new Array();
                                        var arryaOfInputObjsIds = new Array();
                                        _send_request(arryaOfInputObjsIds,arrayOfNames,arrayOfValues,'<xsl:value-of select="$_soapaction"/>','<xsl:value-of select="$_nsuri"/>','<xsl:value-of select="$_nsprefix"/>','',_fnr_<xsl:value-of select="$_original_method_name"/>_Response_Table_Id(),0);return false;
                                     </xsl:attribute>
                             </input>
                     </div>
                 </xsl:when>
                 <xsl:otherwise>

                    <div style='display:none;'>
                        <xsl:attribute name="id"><xsl:value-of select="@name"/></xsl:attribute>

                        <h3>
                            Operations Table [ <xsl:value-of select="@name"/> ]

                        </h3>
                             <xsl:call-template name="_bean_template_request">
                                          <xsl:with-param name="_type" select="$_type"/>
                                          <xsl:with-param name="_nsuri" select="$_nsuri"/>
                                          <xsl:with-param name="_nsprefix" select="$_nsprefix"/>
                                          <xsl:with-param name="_method" select="$_method"/>
                                          <xsl:with-param name="_soapaction" select="$_soapaction"/>
                                          <xsl:with-param name="_request_table_id" select="$_request_table_id"/>
                                          <xsl:with-param name="_original_method_name" select="$_original_method_name"/>
                              </xsl:call-template>
                    </div>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>

    </xsl:template>

    <xsl:template name="_templateNameResponse">
          <xsl:for-each select="//class/method">
                <xsl:variable name="_type" select="output/param/@type"/>
                <xsl:variable name="_nsuri">""</xsl:variable>
                <xsl:variable name="_nsprefix">""</xsl:variable>
                <xsl:variable name="_method" select="@name"/>
                <xsl:variable name="_soapaction" select="@soapaction"/>
                <xsl:variable name="_response_table_id"><xsl:value-of select="@name"/>_Response_Table_Id</xsl:variable>
                <!--<xsl:comment><xsl:value-of select="$_response_table_id"/></xsl:comment>-->
              <xsl:choose>
                      <xsl:when test="$_type">
                          <div style='display:none;'>
                              <xsl:attribute name="id"><xsl:value-of select="@name"/>_Response</xsl:attribute>
                              <h3>
                                Output [ <xsl:value-of select="$_method"/> ]
                            </h3>
                               <xsl:call-template name="_bean_template_response">
                                              <xsl:with-param name="_type" select="$_type"/>
                                              <xsl:with-param name="_nsuri" select="$_nsuri"/>
                                              <xsl:with-param name="_nsprefix" select="$_nsprefix"/>
                                              <xsl:with-param name="_method" select="$_method"/>
                                              <xsl:with-param name="_soapaction" select="$_soapaction"/>
                                              <xsl:with-param name="_response_table_id" select="$_response_table_id"/>
                               </xsl:call-template>

                          </div>

                          <!-- inline javascripts for response manupilation-->
                          <script type="text/javascript">
                              <xsl:call-template name="_bean_template_response_javascripts">
                                      <xsl:with-param name="_type" select="$_type"/>
                                      <xsl:with-param name="_nsuri" select="$_nsuri"/>
                                      <xsl:with-param name="_nsprefix" select="$_nsprefix"/>
                                      <xsl:with-param name="_method" select="$_method"/>
                                      <xsl:with-param name="_soapaction" select="$_soapaction"/>
                                      <xsl:with-param name="_response_table_id" select="$_response_table_id"/>
                               </xsl:call-template>

                          </script>
                     </xsl:when>
                      <xsl:otherwise>
                          <!-- this account for IN_ONLY MEP-->
                          <div style='display:none;'>
                              <xsl:attribute name="id"><xsl:value-of select="@name"/>_Response</xsl:attribute>
                              <h3>
                                Output [ <xsl:value-of select="$_method"/> ]
                            </h3>
                          </div>

                         <script type="text/javascript">
                             function _fnr_<xsl:value-of select="$_response_table_id"/>(){
                                 return new Array('0');
                             }
                         </script>
                      </xsl:otherwise>
              </xsl:choose>

          </xsl:for-each>
    </xsl:template>


    <xsl:template name="_javascripts">
        <xsl:param name="_soapVersion"/>

        var xmlHttp;
        var result;
        var _response_table_object;
        var _response_table_name;
        var _response_javascript_name;

        var _returnTypesNames = new Array();
        var _returnTypesValues = new Array();
        var xmlStr = "";

        var _m_operationDiv;
        var _m_requestDiv;
        var _m_breadcrumbsDiv;
        var _m_responseDiv;

        var _m_response_table_static_data;
		function _getBrowserName(){
			var browserName;
			if(navigator.appName.indexOf("Netscape")>-1){
				browserName="Netscape";
			} else if((navigator.appName.indexOf("Microsoft")>-1) || (navigator.appName.indexOf("MSIE")>-1)){
				browserName="Microsoft Internet Explorer";
			} else if(navigator.appName.indexOf("Opera")>-1){
				browserName="Opera";
			} else {
				browserName=navigator.appName;
			}

			return browserName;
		}

		function _isIE(){
			return (_getBrowserName() == "Microsoft Internet Explorer");
		}

         function createXMLHttpRequestObject() {


            // branch for native XMLHttpRequest object
            if (window.XMLHttpRequest &amp;&amp; !_isIE()) {
                xmlHttp = new XMLHttpRequest();
                if (xmlHttp.overrideMimeType) {
         	        // set type accordingly to anticipated content type
                    xmlHttp.overrideMimeType('text/html');
                }
                // Following privilage has to set for Mozilla/Firefox newer versions
                try {
                   netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserRead");
                } catch (e) {}

            // branch for IE/Windows ActiveX version
            } else if (window.ActiveXObject) {
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

         }


        function _service_client(_arryaOfInputObjsIds,_arrayOfNames, _arrayOfValues, _callback,_soapaction,_nsuri,_nsprefix,_method,_level) {

                //Creating the Browser specific request Object
                createXMLHttpRequestObject();

                xmlHttp.onreadystatechange = _callback;

                if (_arrayOfNames.length != _arrayOfValues.length) {
                    return false;
                }

                var soapBodyXml = '';


                soapBodyXml = '&lt;' + _nsprefix + ':' +  _method + '       xmlns:' + _nsprefix+ '='+ '"'+ _nsuri + '"'+ '&gt;\n';

                for (var i = 0 ; i &lt; _arrayOfNames.length ; i ++ ) {
                    var _input_objs_names = _arryaOfInputObjsIds[i];

                    var _input_objs = document.getElementsByName(_input_objs_names);

                    for (var j = 0 ; j &lt; _input_objs.length ; j++ ) {
                       soapBodyXml += '&lt;' + _arrayOfNames[i] + '   xmlns=""' +'&gt;' + _input_objs[j].value + '&lt;/' + _arrayOfNames[i] + '&gt;\n';    // TODO FixMe
                    }

                 }

                soapBodyXml += '&lt;/' + _nsprefix + ':' +  _method + '&gt;\n';

                if(_arrayOfNames.length == 0 &amp;&amp; _level == 0) {
                    soapBodyXml = '';
                }

                //alert(soapBodyXml);

                // alert(_url());
                try {

                    xmlHttp.open('POST', _url(), true);

                } catch (e) {
                    top.alertMessage("Error occurred while trying to communicate with server. &lt;br/&gt; Please make sure that you selected a port which has an HTTPS binding,&lt;br/&gt; and try again. " + e);
                }
                // alert(soapBodyXml);
                // alert(_soapaction);
                var xmlPayload;

                <xsl:comment><xsl:value-of select="$_soapVersion"/></xsl:comment>

                <xsl:if test="starts-with($_soapVersion,'http://schemas.xmlsoap.org/soap/envelope')">
                     xmlPayload = xmlHttpRequestBodySOAP11(xmlHttp, soapBodyXml, _soapaction);
                </xsl:if>

                <xsl:if test="starts-with($_soapVersion,'http://www.w3.org/2003/05/soap-envelope')">
                    xmlPayload = xmlHttpRequestBodySOAP12(xmlHttp, soapBodyXml, _soapaction);
                </xsl:if>
              //alert(xmlPayload);

              xmlHttp.send(xmlPayload);

            }

            function xmlHttpRequestBodySOAP11(xmlHttp, soapBodyXml, _soapaction) {
                xmlHttp.setRequestHeader('Content-Type', 'text/xml');
                xmlHttp.setRequestHeader('SOAPAction', _soapaction);

                var xmlPayload = '&lt;soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"&gt;\n' +
                                 '&lt;soapenv:Header /&gt;\n' +
                                 '&lt;soapenv:Body&gt;\n'+
                                 soapBodyXml +
                                 '&lt;/soapenv:Body&gt;&lt;/soapenv:Envelope&gt;';
                return xmlPayload;

            }

            function xmlHttpRequestBodySOAP12(xmlHttp, soapBodyXml, _soapaction){
                xmlHttp.setRequestHeader('Content-Type', 'application/soap+xml; charset="utf-8"; action="'+_soapaction+'"');

                var xmlPayload = '&lt;soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"&gt;\n' +
                                 '&lt;soapenv:Header /&gt;\n' +
                                 '&lt;soapenv:Body&gt;\n'+
                                 soapBodyXml +
                                 '&lt;/soapenv:Body&gt;&lt;/soapenv:Envelope&gt;';
                return xmlPayload;

            }


        function _generic_callback() {

            if (xmlHttp.readyState == 4) {
                if (xmlHttp.status == 200) {

                    // branch for native XMLHttpRequest object
                    if (window.XMLHttpRequest &amp;&amp; !_isIE()) {
                        var parser = new DOMParser();

                        browserNeutralResponseDocument = getXmlHttp().responseXML;
                        if (browserNeutralResponseDocument == null) {
                            browserNeutralResponseDocument =  parser.parseFromString(getXmlHttp().responseText,"text/xml");
                        }

                        result = getResponseBody(browserNeutralResponseDocument);
                        // alert(new XMLSerializer().serializeToString(result));

                       _response_xml(result);

                       _response_table(_response_table_object,_returnTypesValues,_returnTypesNames,_m_response_table_static_data,result);<!--  TODO BIG For Respnses-->


                       _returnTypesValues = null; // Clears the array for next Iteration
                       _returnTypesNames = null;
                       _returnTypesValues = new Array();
                       _returnTypesNames = new Array();

                        // branch for IE/Windows ActiveX version
                    } else if (window.ActiveXObject) {
                        // ...processing statements go here...
                        // create the XML object
                        objXmlDoc = new ActiveXObject("Msxml2.DOMDocument");

                        var szResponse = getXmlHttp().responseText;

                        objXmlDoc.loadXML(szResponse);

                        if (objXmlDoc.parseError.errorCode != 0) {
                            var xmlErr = objXmlDoc.parseError;
                            alert("You have error " + xmlErr.reason);
                        }

                        browserNeutralResponseDocument = objXmlDoc.documentElement;

                        result = getResponseBody(browserNeutralResponseDocument);

                       // Response Test
                       _response_xml(result);

                        _response_table(_response_table_object,_returnTypesValues,_returnTypesNames,_m_response_table_static_data,result);<!--  TODO BIG For Respnses-->

                       _returnTypesValues = null; // Clears the array for next Iteration
                       _returnTypesNames = null;
                       _returnTypesValues = new Array();
                       _returnTypesNames = new Array();

                    }
                }
                if (xmlHttp.status == 500 || xmlHttp.status == 400) {
                    //alert('You Have Encounterd An Error');// TODO Fix Error Handling part
                    if (window.XMLHttpRequest &amp;&amp; !_isIE()) {
                       //alert(new XMLSerializer().serializeToString(getXmlHttp().responseXML));
                       var _response_xml_string = "Error Output is Not Available";
                       if(getXmlHttp().responseXML != null) {
                           _response_xml_string = (new XMLSerializer().serializeToString(getXmlHttp().responseXML));
                       } else {
                          _response_xml_string = getXmlHttp().responseText;
                       }
                       document.getElementById('_fault_handling_ta_id').value=_response_xml_string;
                       var _fault_handling_div = document.getElementById('_fault_handling_div_id');<!-- FixMe-->
                       _fault_handling_div.style.display='inline';


                    } else if (window.ActiveXObject) {
                       // create the XML object
                        objXmlDoc = new ActiveXObject("Msxml2.DOMDocument");

                        var szResponse = getXmlHttp().responseText;
                       
                        document.getElementById('_fault_handling_ta_id').value=szResponse;
                        var _fault_handling_div = document.getElementById('_fault_handling_div_id');<!-- FixMe-->
                       _fault_handling_div.style.display='inline';
                        //alert(_error_document.xml);
                    }
                }
            }

        }


        function showOperationDiv() {

        var _text = "&lt;h4&gt;&lt;a href=\"#\" onClick=\"top.showServiceInitializer(); return false;\"&gt;Services&lt;/a&gt;&#160;&gt;&#160;" +
                                           "&lt;a href=\"#\" onClick=\"top.listServiceData('"+ top.getServiceSpecificName() +"'); return false;\"&gt;"+ top.getServiceSpecificName() + "&lt;/a&gt;&#160;&gt;&#160;" +
                                           "Try!"+"&lt;/h4&gt;";
        _m_breadcrumbsDiv.innerHTML=_text;

        _m_operationDiv.style.display = 'inline';
        _m_requestDiv.style.display = 'none';
        _m_responseDiv.style.display = 'none';

        //If fault Present reset the fault handling div first
        var _fault_handling_div = document.getElementById('_fault_handling_div_id');<!-- FixMe-->
        _fault_handling_div.style.display='none';
        _reset_response_table(_response_table_object);
        }

        function _request(_operationDiv,_requestDiv, _operationName,_breadcrumbsDiv) {   <!--  Fixme For BreadCrumbs-->

           var _responseDiv = document.getElementById(_operationName + '_Response');    <!-- Fixme-->
           <!-- Resetting the request table -->
           var _request_table = document.getElementById(_operationName + '_Request_Table_Id');
           _reset_request_table(_request_table);

           // Response Table Object
           _response_table_object = document.getElementById(_operationName+'_Response_Table_Id');

           //Testing

           _response_table_name = _operationName + '_Response_Table_Id';
           _response_javascript_name = '_fnr_' + _response_table_name;
           // alert('_response_javascript_name : ' + _response_javascript_name);

           _m_operationDiv = _operationDiv;
           _m_requestDiv = _requestDiv;
           _m_breadcrumbsDiv = _breadcrumbsDiv;
           _m_responseDiv = _responseDiv;

           _operationDiv.style.display = 'none';
           _requestDiv.style.display = 'inline';
           _responseDiv.style.display = 'inline';

            var _text = "&lt;h4&gt;&lt;a href=\"#\" onClick=\"top.showServiceInitializer(); return false;\"&gt;Services&lt;/a&gt;&#160;&gt;&#160;" +
                                   "&lt;a href=\"#\" onClick=\"top.listServiceData('"+ top.getServiceSpecificName() +"'); return false;\"&gt;"+ top.getServiceSpecificName() + "&lt;/a&gt;&#160;&gt;&#160;" +
                                   "&lt;a href=\"#\" onClick=\"showOperationDiv(); return false;\"&gt;Try!&lt;/a&gt;&#160;&gt;&#160;" + "Operation"+"&lt;/h4&gt;";

            _breadcrumbsDiv.innerHTML=_text;



        }

        function _send_request(_arryaOfInputObjsIds,_arrayOfNames, _arrayOfValues,_soapaction,_nsuri,_nsprefix,_method,_fnr_method,_level) {

<!-- TODO -->
          //_level covers, at what level the _send_request has called.
          //alert('_fnr_metod  :' + _fnr_method.length );

          _m_response_table_static_data = _fnr_method;

          //Reseting the Response Table first
          _reset_response_table(_response_table_object);
          var _fault_handling_div = document.getElementById('_fault_handling_div_id');<!-- FixMe-->
          _fault_handling_div.style.display='none';

          // This is the place that callback is registered for the service
         _service_client(_arryaOfInputObjsIds,_arrayOfNames,_arrayOfValues,_generic_callback,_soapaction,_nsuri,_nsprefix,_method,_level);


       }

        function _url() {
           return '<xsl:value-of select="//class/endpoint"/>';
        }

        function getResponseBody(xml) {
              // branch for native XMLHttpRequest object
            if (window.XMLHttpRequest &amp;&amp; !_isIE()) {

                return xml.getElementsByTagName("Body")[0];

            // branch for IE/Windows ActiveX version
            } else if (window.ActiveXObject) {
                //alert('SOAP PREFIX  :' + xml.firstChild.nextSibling.prefix );

                return xml.getElementsByTagName(xml.firstChild.nextSibling.prefix + ":Body")[0];
            } else {
               alert('NO Native Object Found ');
            }


         }

        function getXmlHttp() {
             return xmlHttp;
         }



            function _response_xml(_response) {

                var _bodyFirstChild;

                for (var r = 0 ; r &lt; _response.childNodes.length ; r++ ) {
                    if (_response.childNodes[r].nodeType == 1) {
                        _bodyFirstChild = _response.childNodes[r];
                    }
                }

                parseXML(_bodyFirstChild);

               //alert('xmlStr' + xmlStr);
            }



        function parseXML(node){

            if(node.hasChildNodes &amp;&amp; node.childNodes[0].nodeType!=3){
              for(var n=0; n &lt;  node.childNodes.length; n++){
                parseXML(node.childNodes[n]);
              }
            }else{
              xmlStr+="name = "+node.nodeName+", value = "+node.childNodes[0].nodeValue;
              _returnTypesNames.push(node.nodeName);
              _returnTypesValues.push(node.childNodes[0].nodeValue);
            }
      }

        // More Generic _response_table
        <!--TODO Need to check this more-->
        function _response_table(_tableObj, _arrayOfValues,_arryOfNames,_static_response_tables_tr_names,_response_xml) {

            //alert('_static_response_tables_tr_names  : ' + _static_response_tables_tr_names.length );

            var _objs_trs = _tableObj.getElementsByTagName('tr');
            var _hearder_tr_bool = false;

            for(var i = 0 ; i &lt; _objs_trs.length ; i++){
                // Logic for header
                if(!_hearder_tr_bool) {
                  _hearder_tr_bool = true; // This is the check that will selet the first tr, which is the header tr
                  _append_table_obj(_objs_trs[i], 'Response','TH');

                }else if(_static_response_tables_tr_names[i-1][0] == 'aa'){
                    // handle for arryas
                    var _tdd = document.createElement('TD');

                  for (var n = 0 ; n &lt; _arryOfNames.length ; n++) {
                       if (_arryOfNames[n] == _static_response_tables_tr_names[i-1][1]){
                           var _txtNode = document.createTextNode(_arrayOfValues[n]);
                           _tdd.appendChild(_txtNode);
                           var _br = document.createElement('BR');
                           _tdd.appendChild(_br);
                      }

                  }
                  _objs_trs[i].appendChild(_tdd);

                }else if (_static_response_tables_tr_names[i-1][0] == 'rr') {
                    // handles regular types

                    for (var j = 0; j &lt; _arryOfNames.length; j++ ) {

                        if (_arryOfNames[j] == _static_response_tables_tr_names[i-1][1]){
                            _append_table_obj(_objs_trs[i], _arrayOfValues[j],'TD');
                        }
                    }
                }else if (_static_response_tables_tr_names[i-1][0] == 'at'){ //counter for xsd:anyType

                      var _xml_obj = _response_xml.getElementsByTagName( _static_response_tables_tr_names[i-1][1])[0];

                      var _xml_obj_to_string = "";
                      if( _xml_obj !=null){
                           _xml_obj_to_string = _to_string(_xml_obj);

                       _append_table_obj(_objs_trs[i],_xml_obj_to_string,'TD');

                       }
                  }
             }

        }

        function _append_table_obj(_trObj, _textNodeObj,_obj) {
            var _td = document.createElement(_obj);
            var _textNode = document.createTextNode(_textNodeObj);
            _td.appendChild(_textNode);
            _trObj.appendChild(_td);
        }

        function _reset_response_table(_tableObj) {
            if (_tableObj == null) {
               return false;
            }
            var _obj_trs = _tableObj.getElementsByTagName('tr');

            for (var i = 0 ; i &lt; _obj_trs.length ; i++) {
                if (i == 0) { // This is the Header Tr
                    _table_response_remove_obj('th',_obj_trs[0]);
                } else {
                    _table_response_remove_obj('td',_obj_trs[i]);
                }
            }
        }

        function _table_response_remove_obj(_obj_name, _original_obj) {

            var _obj_array = _original_obj.getElementsByTagName(_obj_name);

            for (var n = 0; n &lt; _obj_array.length; n++) {
                if (_obj_array.length > 2) {
                    if (n == (_obj_array.length - 1)) { // Reached the end remove the last object

                        _original_obj.removeChild(_obj_array[n]);
                    }
                } else {
                    return false;
                }
            }

        }

        function _create_table_obj(_table,_objName) {
            var _obj = document.createElement(_objName);
            _table.appendChild(_obj);
            return _obj;
        }

        function _create_table_raw_with_data(_table_raw_obj,_shorttypename,_generated_id) {

            var _new_td = document.createElement('TD');

            var _new_input = document.createElement('INPUT');
            _new_input.setAttribute('type','text');
            _new_input.setAttribute('id',_generated_id); // Now This is a IE Bug
            _new_input.setAttribute('name',_generated_id);
            _new_td.appendChild(_new_input);
            //_table_raw_obj.appendChild(_new_td);

            var _tr_tds = _table_raw_obj.getElementsByTagName('td');

            for (var i = 0; i &lt; _tr_tds.length ; i++  ) {
                if (i == (_tr_tds.length -1)){
                    _table_raw_obj.insertBefore(_new_td,_tr_tds[i]);
                }
            }

            //alert('count  ___ :  ' + document.getElementsByName(_generated_id).length);

        }

        function _reset_request_table(_request_table_obj){
            var _trs = _request_table_obj.getElementsByTagName('tr');


            for (var n = 0 ; n &lt; _trs.length ; n++) {
                if (n == 0) {continue;}// This counts for the header
                var _tds = _trs[n].getElementsByTagName('td');

                if (_tds.length > 4) {
                    var _orginal_td_length=_tds.length;

                    for (var m=(_orginal_td_length-2) ;m &gt;= 3 ; m-- ) {

                        _trs[n].removeChild(_tds[m]);
                    }
                }
                //Resetting the input box
                _tds[2].firstChild.value = '';
            }

        }
        // Environment independent xml objects to String serializer
        function _to_string(obj) {
            // branch for native XMLHttpRequest object
            if (window.XMLHttpRequest &amp;&amp; !_isIE()) {
                return (new XMLSerializer().serializeToString(obj));


                // branch for IE/Windows ActiveX version
            } else if (window.ActiveXObject) {
                // ...processing statements go here...
                return obj.xml;
            }
        }


    </xsl:template>

    <xsl:template name="_dynamic_client.css">
        <!-- Neutralizing the h4 properties -->
        h4 {
         margin: 0 0 1em 0;
         padding: 0;
        }
        h3 {
        font-weight: normal;
        color:darkorange;

        }

        table.styled {
        color: #FFF;
        background-color: #A3B2CC;
        font-family: sans-serif;
        font-size: 12px;
        font-style: normal;
        }

        table.styled td {
        border: thin white;
        color: blue;

        }

        table.styled td {
        background: white;
        }
        a, a:link {
                color: #039;
        }

        a:hover {
                color: #03C;
                text-decoration: none;
        }
    </xsl:template>

</xsl:stylesheet>
