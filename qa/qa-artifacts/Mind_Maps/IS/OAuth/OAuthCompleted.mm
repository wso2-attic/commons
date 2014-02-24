<map version="0.8.0">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node COLOR="#ff0000" CREATED="1392711087320" ID="Freemind_Link_3565638" MODIFIED="1393094280050" STYLE="fork" TEXT="             OAuth                    ">
<font BOLD="true" NAME="SansSerif" SIZE="13"/>
<node CREATED="1392711724517" ID="_" MODIFIED="1392711941175" POSITION="right" TEXT="OAuth 1.0a ">
<node CREATED="1393174596534" ID="Freemind_Link_421059750" MODIFIED="1393174987927" TEXT="Consumer request token from service provider">
<icon BUILTIN="full-1"/>
<node CREATED="1393174672486" ID="Freemind_Link_703132542" MODIFIED="1393175010807" TEXT="Service provider issues an unauthorized equest token">
<icon BUILTIN="full-2"/>
<node CREATED="1393174821732" ID="Freemind_Link_1751197247" MODIFIED="1393175030690" TEXT="consumer directs the user to the service provider">
<icon BUILTIN="full-3"/>
<node CREATED="1393174836460" ID="Freemind_Link_1959708241" MODIFIED="1393175033327" TEXT="Service Provider Authenticates the User and Obtains Consent">
<icon BUILTIN="full-4"/>
<node CREATED="1393174895108" ID="Freemind_Link_908986079" MODIFIED="1393175035847" TEXT="Service Provider Directs the User Back to the Consumer">
<icon BUILTIN="full-5"/>
<node CREATED="1393174928059" ID="Freemind_Link_1968254087" MODIFIED="1393175047535" TEXT="The Consumer exchanges the Request Token for an Access Token capable of accessing the Protected Resources.">
<icon BUILTIN="full-6"/>
<node CREATED="1393174956171" ID="Freemind_Link_437765119" MODIFIED="1393175054471" TEXT="Service Provider Grants an Access Token">
<icon BUILTIN="full-7"/>
</node>
</node>
</node>
</node>
</node>
</node>
</node>
</node>
<node CREATED="1392711955285" ID="Freemind_Link_210498066" MODIFIED="1392711963887" POSITION="left" TEXT="Oauth 2.0">
<node CREATED="1393081889039" ID="Freemind_Link_867419627" MODIFIED="1393094472968" TEXT="Authorization Code Grant Type">
<font NAME="SansSerif" SIZE="13"/>
<node CREATED="1393086114816" ID="Freemind_Link_1347951917" MODIFIED="1393087691835" TEXT="Client redirects the user agent to the authorization end point">
<node CREATED="1393086200855" ID="Freemind_Link_846880669" MODIFIED="1393090094990" TEXT="The authorization server authenticates the resources owner via user agent">
<icon BUILTIN="full-1"/>
<node CREATED="1393086466943" ID="Freemind_Link_1742262420" MODIFIED="1393090136117" TEXT="Authorization server redirects user agent back to the client usin the redirection URL with authorization code">
<icon BUILTIN="full-2"/>
<node CREATED="1393086779329" ID="Freemind_Link_1376797968" MODIFIED="1393090105526" TEXT="The client request an access token from the authorizationserver endpoint by including the authorization code ">
<icon BUILTIN="full-3"/>
<node CREATED="1393087160278" ID="Freemind_Link_1586752987" MODIFIED="1393091745846" TEXT="The authorization server authenticates the client, validates the&#xa;        authorization code, and ensures that the redirection URI&#xa;        received matches the URI used to redirect the client in">
<icon BUILTIN="full-4"/>
</node>
</node>
</node>
</node>
<node CREATED="1393089914974" ID="Freemind_Link_71114022" MODIFIED="1393091998323" TEXT="failiure scenarios &#xa;&#xa; unauthorized_client (The client is not authorized to request an authorization  code using this  method&#xa;&#xa; access_denied    The resource owner or authorization server denied therequest. &#xa; &#xa;server_error  The authorization server encountered an unexpected  condition that prevented it from fulfilling the request.  &#xa;&#xa;temporarily_unavailable The authorization server is currently unable to handle the request due to a temporary overloading or maintenance  of the server.">
<icon BUILTIN="button_cancel"/>
</node>
</node>
</node>
<node CREATED="1393084740597" ID="Freemind_Link_1860292538" MODIFIED="1393094498904" STYLE="fork" TEXT="Implicit">
<font NAME="SansSerif" SIZE="13"/>
<node CREATED="1393084754717" ID="Freemind_Link_1963183776" MODIFIED="1393084754717" TEXT="">
<node CREATED="1393085072754" ID="Freemind_Link_1185707634" MODIFIED="1393090154877" TEXT="Client send the resources owners user agent to the authorizatin end point">
<icon BUILTIN="full-1"/>
<node CREATED="1393089829980" ID="Freemind_Link_907533924" MODIFIED="1393091125532" TEXT="The authorization server autheticate the user resources owner via user gent">
<icon BUILTIN="full-2"/>
<node CREATED="1393090854650" ID="Freemind_Link_1997128799" MODIFIED="1393091128324" TEXT="Authorization server redirect the user agent back to the client using the redirection URL">
<icon BUILTIN="full-3"/>
<node CREATED="1393091147511" ID="Freemind_Link_670656710" MODIFIED="1393091147511" TEXT="">
<node CREATED="1393091250326" ID="Freemind_Link_665887239" MODIFIED="1393091338842" TEXT="The user agent follows the redirection instructions by making a request to the web hosted client resources">
<icon BUILTIN="full-4"/>
<node CREATED="1393091455564" ID="Freemind_Link_989082807" MODIFIED="1393091561543" TEXT="The web hosted client retruns a web page with acces token">
<icon BUILTIN="full-5"/>
<node CREATED="1393091718161" ID="Freemind_Link_539555392" MODIFIED="1393091749254" TEXT="The user agent passes the access token to the client">
<icon BUILTIN="full-6"/>
</node>
</node>
</node>
</node>
</node>
</node>
<node CREATED="1393090235388" ID="Freemind_Link_1332152717" MODIFIED="1393093175864" TEXT="Faliure scenarios &#xa;&#xa;Resource owner denies the client access request&#xa;&#xa; invalid_request&#xa;               The request is missing a required parameter, includes an&#xa;               invalid parameter value, includes a parameter more than&#xa;               once, or is otherwise malformed.&#xa;&#xa;         unauthorized_client&#xa;               The client is not authorized to request an access token&#xa;               using this method.&#xa;&#xa;         access_denied&#xa;               The resource owner or authorization server denied the&#xa;               request.&#xa;&#xa;         unsupported_response_type&#xa;               The authorization server does not support obtaining an&#xa;               access token using this method.&#xa;&#xa;          server_error&#xa;               The authorization server encountered an unexpected&#xa;               condition that prevented it from fulfilling the request.&#xa;              &#xa;&#xa;         temporarily_unavailable&#xa;               The authorization server is currently unable to handle&#xa;               the request due to a temporary overloading or maintenance&#xa;               of the server. ">
<icon BUILTIN="button_cancel"/>
</node>
</node>
</node>
</node>
<node CREATED="1393091892520" ID="Freemind_Link_1502707515" MODIFIED="1393094510631" TEXT="Resources Owner passowrd credentials grant">
<font NAME="SansSerif" SIZE="13"/>
<node CREATED="1393092122708" ID="Freemind_Link_1928604116" MODIFIED="1393092228097" TEXT="Resource owner provide the client his user name and passowrd">
<icon BUILTIN="full-1"/>
<node CREATED="1393092201477" ID="Freemind_Link_1453271755" MODIFIED="1393092233280" TEXT="Client request an access token from authorization server">
<icon BUILTIN="full-2"/>
<node CREATED="1393092243644" ID="Freemind_Link_1921827687" MODIFIED="1393092311928" TEXT="The authorization server authenticates the client and validates the resource owner credentials,">
<icon BUILTIN="full-3"/>
</node>
</node>
</node>
<node CREATED="1393092446330" ID="Freemind_Link_980323085" MODIFIED="1393092562006" TEXT="Faliure scenarios&#xa;&#xa;Resouces owner provide invalid user name &amp; passowrd credentials">
<icon BUILTIN="button_cancel"/>
</node>
</node>
<node CREATED="1393092587721" ID="Freemind_Link_1671608721" MODIFIED="1393094516671" TEXT="Client credentials grant">
<font NAME="SansSerif" SIZE="13"/>
<node CREATED="1393092683032" ID="Freemind_Link_1083235824" MODIFIED="1393092831035" TEXT="The client authenticates with the authorization server and  requests an access token from the token endpoint.">
<icon BUILTIN="full-1"/>
<node CREATED="1393092767367" ID="Freemind_Link_514077643" MODIFIED="1393092852411" TEXT="The authorization server authenticates the client, and if valid,issues an access token.">
<icon BUILTIN="full-2"/>
</node>
<node CREATED="1393093015085" ID="Freemind_Link_1711338387" MODIFIED="1393093481114" TEXT="Faliure Scenarios&#xa;&#xa;The authorization server responds with an HTTP 400 (Bad Request)&#xa; status code due to the followin reasons&#xa;&#xa;&#xa; invalid_request&#xa;               &#xa;invalid_client              &#xa;&#xa;invalid_grant&#xa;               &#xa;unauthorized_client          &#xa;&#xa;unsupported_grant_type&#xa;              &#xa;">
<icon BUILTIN="button_cancel"/>
</node>
</node>
</node>
<node CREATED="1393175633025" ID="Freemind_Link_822725076" MODIFIED="1393175637046" TEXT="SAML2.0 Assertion grant type">
<node CREATED="1393175781628" ID="Freemind_Link_75274625" MODIFIED="1393175833040" TEXT="The client sends an access token request to the authorization server with an appropriate OAuth grant_type and includes a SAML 2.0 Assertion.">
<icon BUILTIN="full-1"/>
<node CREATED="1393175838292" ID="Freemind_Link_919896836" MODIFIED="1393175875056" TEXT="The authorization server validates the Assertion per the processing rules defined in this specification and issues an  access token.">
<icon BUILTIN="full-2"/>
</node>
<node CREATED="1393176058378" ID="Freemind_Link_386417964" MODIFIED="1393176144070" TEXT="faliure scenarios &#xa;&#xa;Asseration is not valid&#xa;&#xa;Confirmation requirments cannot be met">
<icon BUILTIN="button_cancel"/>
</node>
</node>
</node>
</node>
<node CREATED="1392976489073" ID="Freemind_Link_1595295540" MODIFIED="1392976502317" POSITION="right" TEXT="OAuth1.0 - Not in Use now "/>
<node CREATED="1393094364479" ID="Freemind_Link_570206069" MODIFIED="1393094560872" POSITION="left" STYLE="fork" TEXT="Access Tokens">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1393168625994" ID="Freemind_Link_738194038" MODIFIED="1393168643473" TEXT="BearerToken"/>
<node CREATED="1393168647464" ID="Freemind_Link_624169898" MODIFIED="1393168671938" TEXT="Mac Token"/>
<node CREATED="1393168678240" ID="Freemind_Link_1370641023" MODIFIED="1393168684474" TEXT="Refresh Token">
<node CREATED="1393168880822" ID="Freemind_Link_722948736" MODIFIED="1393169424298" TEXT="Client request an access token with athenticating with the authorization server">
<icon BUILTIN="full-1"/>
<node CREATED="1393168911782" ID="Freemind_Link_582382312" MODIFIED="1393169532910" TEXT="Authorization server autenticates the client and issue access token and refresh token">
<icon BUILTIN="full-2"/>
<node CREATED="1393169067325" ID="Freemind_Link_1046754648" MODIFIED="1393170541435" TEXT="Client make protected resource request to the resource server presenting the access token&#xa;repeat until the access token expires">
<icon BUILTIN="full-3"/>
<node CREATED="1393169195051" ID="Freemind_Link_1668441180" MODIFIED="1393170560734" TEXT="Resource server validates the access token and serves the request&#xa;repeat until the access token expires">
<icon BUILTIN="full-4"/>
<node CREATED="1393169293284" ID="Freemind_Link_369524884" MODIFIED="1393170497873" TEXT="Resources server identified that access token expires">
<icon BUILTIN="full-5"/>
<node CREATED="1393170215528" ID="Freemind_Link_682334782" MODIFIED="1393170500181" TEXT="Client request new access token presenting the refresh token">
<icon BUILTIN="full-6"/>
<node CREATED="1393170392553" ID="Freemind_Link_562246507" MODIFIED="1393170858527" TEXT="Authorization server validates the refresh token presnted by the client and issue new access token">
<icon BUILTIN="full-7"/>
</node>
<node CREATED="1393170962981" ID="Freemind_Link_1836045518" MODIFIED="1393170997681" TEXT="Validates the refresh token but does not provide the new access token">
<icon BUILTIN="button_cancel"/>
</node>
</node>
<node CREATED="1393170814038" ID="Freemind_Link_325131799" MODIFIED="1393170945380" TEXT="Faliure scenarios&#xa;&#xa;Resources server does not validate the refresh token proviced&#xa;&#xa;Invalid refresh token provid by the client">
<icon BUILTIN="button_cancel"/>
</node>
</node>
<node CREATED="1393170719152" ID="Freemind_Link_1869117795" MODIFIED="1393170783410" TEXT="Resource server doen not identified that the access token expires after the given time">
<icon BUILTIN="button_cancel"/>
</node>
</node>
</node>
</node>
</node>
</node>
<node CREATED="1393176353848" ID="Freemind_Link_363456293" MODIFIED="1393176589355" TEXT="Revoke Token&#xa;&#xa;Both access and refresh token">
<node CREATED="1393176603229" ID="Freemind_Link_53663351" MODIFIED="1393176677633" TEXT="The client requests the revocation of a particular token by making an    HTTP POST request to the token revocation endpoint URL">
<icon BUILTIN="full-1"/>
<node CREATED="1393176614328" ID="Freemind_Link_1971775499" MODIFIED="1393176679609" TEXT="The authorization server responds with HTTP status code 200 if the    token has been revoked sucessfully">
<icon BUILTIN="full-2"/>
</node>
<node CREATED="1393176720148" ID="Freemind_Link_1414389442" MODIFIED="1393176923239" TEXT="faliure scenarios&#xa;&#xa;unsupported_token_type  The authorization server does not support the revocation of the presented token type.">
<icon BUILTIN="button_cancel"/>
</node>
</node>
</node>
</node>
</node>
</map>
