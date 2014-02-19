<map version="0.8.0">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1392186464617" ID="Freemind_Link_629153807" MODIFIED="1392187537338" TEXT="STS/WS-Trust">
<node CREATED="1392187005209" ID="Freemind_Link_209677393" MODIFIED="1392187010514" POSITION="left" TEXT="Negative scenarios">
<node CREATED="1392187113436" ID="Freemind_Link_1064214845" MODIFIED="1392187122250" TEXT="Active STS">
<node CREATED="1392187040605" ID="Freemind_Link_750909050" MODIFIED="1392627934657" TEXT="Calling to a service provider with a token which does not match the required token version"/>
<node CREATED="1392187013226" ID="Freemind_Link_846938629" MODIFIED="1392187031723" TEXT="Calling to a service provider with a token which does not have all the expected claims"/>
<node CREATED="1392187079071" ID="Freemind_Link_1825597414" MODIFIED="1392187097749" TEXT="Calling to a service provider with a token with wrong token type"/>
</node>
<node CREATED="1392187139277" ID="Freemind_Link_1317819114" MODIFIED="1392187143652" TEXT="Passive STS"/>
</node>
<node CREATED="1392187567192" ID="Freemind_Link_1682583319" MODIFIED="1392187587450" POSITION="left" TEXT="Security token service">
<node CREATED="1392187593449" ID="Freemind_Link_641071760" MODIFIED="1392187593449" TEXT="">
<node CREATED="1392187593596" ID="Freemind_Link_1926927562" MODIFIED="1392628559348" TEXT="Securing STS with different security scenarios">
<node CREATED="1392628562083" ID="Freemind_Link_710204367" MODIFIED="1392628572482" TEXT="http://dulanja.blogspot.com/2013/04/accessing-claim-aware-services-using_2.html"/>
<node CREATED="1392628604986" ID="Freemind_Link_715021711" MODIFIED="1392714764076" TEXT="Invoking STS with kerberos security (http://wso2.com/library/articles/2012/08/securing-sts-security-token-service-kerberos/)"/>
<node CREATED="1392628590330" ID="Freemind_Link_800196709" MODIFIED="1392634144388" TEXT="http://charithaka.blogspot.com/2013/07/broker-trust-relationships-with-wso2.html"/>
</node>
</node>
<node CREATED="1392187683433" ID="Freemind_Link_85458052" MODIFIED="1392785055831" TEXT="Issuance">
<node CREATED="1392186922933" ID="Freemind_Link_913452264" MODIFIED="1392627899216" TEXT="Active STS">
<node CREATED="1392186885023" ID="Freemind_Link_297462234" MODIFIED="1392186902859" TEXT="SAML versions">
<node CREATED="1392186633938" ID="Freemind_Link_117213451" MODIFIED="1392635338010" TEXT="SAML 2.0">
<node CREATED="1392186754342" ID="Freemind_Link_16760362" MODIFIED="1392186761563" TEXT="Bearer subject confirmation">
<node CREATED="1392634693143" ID="Freemind_Link_1729152892" MODIFIED="1392634697768" TEXT="claims mandatory"/>
<node CREATED="1392634699770" ID="Freemind_Link_1774251961" MODIFIED="1392634703892" TEXT="claims optional"/>
</node>
<node CREATED="1392186813045" ID="Freemind_Link_665271744" MODIFIED="1392186814415" TEXT="Issued token as protection token">
<node CREATED="1392634693143" ID="Freemind_Link_867886391" MODIFIED="1392634697768" TEXT="claims mandatory"/>
<node CREATED="1392634699770" ID="Freemind_Link_593316082" MODIFIED="1392634703892" TEXT="claims optional"/>
</node>
<node CREATED="1392625438716" ID="Freemind_Link_1942563200" MODIFIED="1392635378385" TEXT="Sender vouches">
<icon BUILTIN="stop"/>
<node CREATED="1392634693143" ID="Freemind_Link_1027611548" MODIFIED="1392634697768" TEXT="claims mandatory"/>
<node CREATED="1392634699770" ID="Freemind_Link_1427875798" MODIFIED="1392634703892" TEXT="claims optional"/>
</node>
<node CREATED="1392625455373" ID="Freemind_Link_1006857263" MODIFIED="1392625458905" TEXT="HoK">
<node CREATED="1392186740949" ID="Freemind_Link_1014895354" MODIFIED="1392625471144" TEXT="symmetric key">
<node CREATED="1392634693143" ID="Freemind_Link_1091043012" MODIFIED="1392634697768" TEXT="claims mandatory"/>
<node CREATED="1392634699770" ID="Freemind_Link_1619061142" MODIFIED="1392634703892" TEXT="claims optional"/>
</node>
<node CREATED="1392186749475" ID="Freemind_Link_1782799351" MODIFIED="1392625475278" TEXT="public key">
<node CREATED="1392634693143" ID="Freemind_Link_1228675710" MODIFIED="1392634697768" TEXT="claims mandatory"/>
<node CREATED="1392634699770" ID="Freemind_Link_1809742812" MODIFIED="1392634703892" TEXT="claims optional"/>
</node>
</node>
</node>
<node CREATED="1392186620082" ID="_" MODIFIED="1392186631567" TEXT="SAML 1.1">
<node CREATED="1392186726194" ID="Freemind_Link_1147503701" MODIFIED="1392186733333" TEXT="Bearer subject confirmation">
<node CREATED="1392634693143" ID="Freemind_Link_1202639188" MODIFIED="1392634697768" TEXT="claims mandatory"/>
<node CREATED="1392634699770" ID="Freemind_Link_1603893483" MODIFIED="1392634703892" TEXT="claims optional"/>
</node>
<node CREATED="1392186808512" ID="Freemind_Link_1088353403" MODIFIED="1392186811986" TEXT="Issued token as protection token">
<node CREATED="1392634693143" ID="Freemind_Link_1057273396" MODIFIED="1392634697768" TEXT="claims mandatory"/>
<node CREATED="1392634699770" ID="Freemind_Link_1546423466" MODIFIED="1392634703892" TEXT="claims optional"/>
</node>
<node CREATED="1392625444336" ID="Freemind_Link_1386817746" MODIFIED="1392635395178" TEXT="Sender vouches">
<icon BUILTIN="stop"/>
<node CREATED="1392634693143" ID="Freemind_Link_871903021" MODIFIED="1392634697768" TEXT="claims mandatory"/>
<node CREATED="1392634699770" ID="Freemind_Link_510047477" MODIFIED="1392634703892" TEXT="claims optional"/>
</node>
<node CREATED="1392625478063" ID="Freemind_Link_313680639" MODIFIED="1392625488341" TEXT="HoK">
<node CREATED="1392186681542" ID="Freemind_Link_1027566164" MODIFIED="1392625497458" TEXT="symmetric key">
<node CREATED="1392634693143" ID="Freemind_Link_1889437450" MODIFIED="1392634697768" TEXT="claims mandatory"/>
<node CREATED="1392634699770" ID="Freemind_Link_1303229982" MODIFIED="1392634703892" TEXT="claims optional"/>
</node>
<node CREATED="1392186712213" ID="Freemind_Link_1154899200" MODIFIED="1392625500946" TEXT="public key">
<node CREATED="1392634693143" ID="Freemind_Link_1824934250" MODIFIED="1392634697768" TEXT="claims mandatory"/>
<node CREATED="1392634699770" ID="Freemind_Link_1000940921" MODIFIED="1392634703892" TEXT="claims optional"/>
</node>
</node>
</node>
</node>
<node CREATED="1392634837648" ID="Freemind_Link_295848133" MODIFIED="1392634839570" TEXT="AppliesTo"/>
<node CREATED="1392634840695" ID="Freemind_Link_1377499581" MODIFIED="1392634854031" TEXT="Lifetime"/>
<node CREATED="1392714209857" ID="Freemind_Link_1952885646" MODIFIED="1392714212204" TEXT="keysize"/>
</node>
<node CREATED="1392186778781" ID="Freemind_Link_1964826840" MODIFIED="1392186785216" TEXT="Passive STS">
<node CREATED="1392628653495" ID="Freemind_Link_1049627515" MODIFIED="1392628656513" TEXT="http://wso2.com/library/articles/2011/12/configuring-wso2-identity-server-passive-sts-aspnet-client/#comment-32411"/>
</node>
<node CREATED="1392782671227" ID="Freemind_Link_1164235903" MODIFIED="1392782685906" TEXT="http://blog.facilelogin.com/2009/05/security-token-service-with-wso2.html"/>
<node CREATED="1392782779153" ID="Freemind_Link_238162866" MODIFIED="1392782819090" TEXT="STS Client Sample program - SPRT - FSF-136"/>
<node CREATED="1392785043976" ID="Freemind_Link_1454930500" MODIFIED="1392785068352" TEXT="Check attributes in issued SAML token">
<node CREATED="1392785068278" ID="Freemind_Link_1462947745" MODIFIED="1392785356213" TEXT="NameIdentifier - SPRT - FSF-154"/>
<node CREATED="1392786838453" ID="Freemind_Link_1753406865" MODIFIED="1392786845208" TEXT="Change host name and check issuer value"/>
</node>
</node>
<node CREATED="1392187706039" ID="Freemind_Link_1828440449" MODIFIED="1392187707432" TEXT="Renewal"/>
<node CREATED="1392187722689" ID="Freemind_Link_893466861" MODIFIED="1392187724282" TEXT="Validation">
<node CREATED="1392722404393" ID="Freemind_Link_1327513736" MODIFIED="1392722432446" TEXT="Validation of the SAML token via STS- SPRT - WT-10">
<node CREATED="1392790478397" ID="Freemind_Link_1308802875" MODIFIED="1392790482083" TEXT="SAML version"/>
<node CREATED="1392790482927" ID="Freemind_Link_676805025" MODIFIED="1392790485308" TEXT="Timestamp"/>
<node CREATED="1392790495583" ID="Freemind_Link_1662034992" MODIFIED="1392790497705" TEXT="Claims"/>
<node CREATED="1392790498276" ID="Freemind_Link_1704796108" MODIFIED="1392790503513" TEXT="SAML token signature"/>
</node>
<node CREATED="1392783925533" ID="Freemind_Link_1940040133" MODIFIED="1392783937785" TEXT="STS validate not selecting from DB Token Store - SPRT"/>
<node CREATED="1392790401786" ID="Freemind_Link_993598111" MODIFIED="1392790443573" TEXT="Validation of SAML token at RP end (https://wso2.org/jira/browse/CARBON-7609)">
<node CREATED="1392790478397" ID="Freemind_Link_23863969" MODIFIED="1392791136504" TEXT="SAML version (https://wso2.org/jira/browse/CARBON-13157)"/>
<node CREATED="1392790482927" ID="Freemind_Link_1297371028" MODIFIED="1392790485308" TEXT="Timestamp"/>
<node CREATED="1392790495583" ID="Freemind_Link_1699003430" MODIFIED="1392790497705" TEXT="Claims"/>
<node CREATED="1392790498276" ID="Freemind_Link_1091942485" MODIFIED="1392790503513" TEXT="SAML token signature"/>
</node>
</node>
<node CREATED="1392187747409" ID="Freemind_Link_1669331807" MODIFIED="1392785405642" TEXT="Cancellation/revoke"/>
<node CREATED="1392786942573" ID="Freemind_Link_1287142804" MODIFIED="1392786947248" TEXT="Change the default key store">
<node CREATED="1392786992227" ID="Freemind_Link_762633484" MODIFIED="1392787123762" TEXT="At STS side import the public certificate of the client key store into wso2carbon.jks. In client side use new key store and import the public certificate used by IS in to client keystore (which is used as the client&apos;s trust store)"/>
</node>
</node>
<node CREATED="1392791425107" ID="Freemind_Link_1991247266" MODIFIED="1392791433489" POSITION="left" TEXT="DisableTokenStore in carbon.xml"/>
<node CREATED="1392618711593" ID="Freemind_Link_1358716491" MODIFIED="1392618718355" POSITION="right" TEXT="Integration scenarios">
<node CREATED="1392631504866" ID="Freemind_Link_351719697" MODIFIED="1392634152156" TEXT="Use token issued by IS to authenticate to an app in app server - http://charithaka.blogspot.com/2013/07/broker-trust-relationships-with-wso2.html"/>
<node CREATED="1392631679150" ID="Freemind_Link_1923073042" MODIFIED="1392717255583" TEXT="RESTful services - service and client unsecured, intermidate ESBs exchange SAML bearer token to OAuth bearer token and cache it to be used in every request"/>
<node CREATED="1392717270535" ID="Freemind_Link_1895760037" MODIFIED="1392717451119" TEXT="SOAP services - service and client unsecured. Intermeidate ESB gets a SAML token (HoK) from STS and caches it and use it to secure all outgoing messages"/>
<node CREATED="1392717434990" ID="Freemind_Link_1207576253" MODIFIED="1392780231980" TEXT="SOAP services - Service and client unsecured. Intermediate ESB talks to its STS to get a SAML token (HoK and Bearer). This STS in return talks to resource STS and exchange its SAML token to a new SAML token signed by resource STS which is used to secure out going messages "/>
<node CREATED="1392781510396" ID="Freemind_Link_489085139" MODIFIED="1392781696189" TEXT="client authenticates to STS (secured with UT) and gets a token from it with set of claims. Include the token with request and send it to ESB. ESB will check the required claims and then remove the header and replace it with a new header (E.g Sign and Entrypt). ESB sends the request to app server with this header and app server sends the response with similar header. ESB removes it and attaches SAML token and sends it to the client (STS authentication use cases)"/>
<node CREATED="1392782867635" ID="Freemind_Link_104896078" MODIFIED="1392782877304" TEXT="Claim aware proxy services with ESB (http://blog.facilelogin.com/2009/06/wso2-identity-server-claim-aware-proxy.html)"/>
<node CREATED="1392786515900" ID="Freemind_Link_1980182790" MODIFIED="1392786604851" TEXT="Cleint gets a token from STS and send it to ESB. ESB authenticate the client and get the username and other data from this SAML token. Then it writes it into the MessageContext and redirect it to entitlement mediator. PEP sends this back to PDP in IS which validates the user name and authorize it. When ESB gets authorized success response from IS, it will let the request go to SP end (SPRT - WS- Trust based implementation help using WSO2 IS as STS)"/>
<node CREATED="1392797911609" ID="Freemind_Link_1103618620" MODIFIED="1392797928020" TEXT="Extracting SAML assertions from a proxy service and adding them back to the service behind (http://blog.facilelogin.com/2009/06/extracting-saml-assertions-from-proxy.html)"/>
<node CREATED="1392798765874" ID="Freemind_Link_1902508846" MODIFIED="1392798818802" TEXT="Accessing proxy services in WSO2 ESB witha security token issued by the Identity Server (http://blog.facilelogin.com/2009/05/accessing-proxy-services-in-wso2-esb.html) "/>
</node>
<node CREATED="1392634473987" ID="Freemind_Link_360021348" MODIFIED="1392634479538" POSITION="right" TEXT="Trust patterns">
<node CREATED="1392634481118" ID="Freemind_Link_1222485744" MODIFIED="1392714018678" TEXT="Indirect brokered trust relationship"/>
<node CREATED="1392634504400" ID="Freemind_Link_1224435031" MODIFIED="1392713959483" TEXT="direct trust relationship"/>
<node CREATED="1392713971891" ID="Freemind_Link_1550738706" MODIFIED="1392713977415" TEXT="direct brokered trust"/>
<node CREATED="1392780114857" ID="Freemind_Link_1744380621" MODIFIED="1392780396865" TEXT="Federated identity ">
<node CREATED="1392780399604" ID="Freemind_Link_327084980" MODIFIED="1392780410166" TEXT="SOAP services - Service and client unsecured. Intermediate ESB talks to its STS to get a SAML token (HoK and Bearer). This STS in return talks to resource STS and exchange its SAML token to a new SAML token signed by resource STS which is used to secure out going messages"/>
<node CREATED="1392780434521" ID="Freemind_Link_589154357" MODIFIED="1392780769508" TEXT="RP authenticates with UT/kerberos, get a SAML token and send it to resource STS at service end. Resource STS give another type of SAML token (transform one token type into another) and sign it. This token is used to secure outgoing messages "/>
<node CREATED="1392780556217" ID="Freemind_Link_303255276" MODIFIED="1392780734242" TEXT="RP gets a SAML token (with a set of claims) from the STS in its domain and sends it to resource STS. Resource STS accepts it and create another token with different set of claims (claim transformation) and this SAML token can be used to secure outgoing messages"/>
</node>
</node>
<node CREATED="1392790080352" ID="Freemind_Link_1800795056" MODIFIED="1392790086124" POSITION="right" TEXT="Load and performance test">
<node CREATED="1392790804408" ID="Freemind_Link_3556278" MODIFIED="1392790833810" TEXT="With high number of concurrent threads (https://wso2.org/jira/browse/CARBON-11229)"/>
<node CREATED="1392791398989" ID="Freemind_Link_703778986" MODIFIED="1392791412463" TEXT="DisableTokenStore in carbon.xml and observe the performance"/>
</node>
</node>
</map>
