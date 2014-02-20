<map version="0.8.0">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1392799678910" ID="Freemind_Link_1503630624" MODIFIED="1392889790266" TEXT="SCIM (http://tools.ietf.org/html/draft-ietf-scim-api-01)">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392800906991" ID="_" MODIFIED="1392800919393" POSITION="right" TEXT="Sample (http://docs.wso2.org/display/IS460/Consuming+SCIM+Rest+Endpoints)"/>
<node CREATED="1392868175668" ID="Freemind_Link_913628009" MODIFIED="1392868776935" POSITION="right" TEXT="User Stores">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868186668" ID="Freemind_Link_270790852" MODIFIED="1392868187762" TEXT="AD"/>
<node CREATED="1392868188213" ID="Freemind_Link_1772901135" MODIFIED="1392868191123" TEXT="OpenLDAP"/>
<node CREATED="1392868191375" ID="Freemind_Link_175544349" MODIFIED="1392868194705" TEXT="Embedded LDAP"/>
<node CREATED="1392868195397" ID="Freemind_Link_509623950" MODIFIED="1392868198957" TEXT="JDBC"/>
<node CREATED="1392874091646" ID="Freemind_Link_1785281252" MODIFIED="1392874095151" TEXT="Seconday user stores"/>
</node>
<node CREATED="1392875329402" ID="Freemind_Link_377204192" MODIFIED="1392875596726" POSITION="left" TEXT="Response codes">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392875499952" ID="Freemind_Link_1657280091" MODIFIED="1392875501371" TEXT="400 BAD REQUEST"/>
<node CREATED="1392875536379" ID="Freemind_Link_1680795946" MODIFIED="1392875537872" TEXT="401 UNAUTHORIZED"/>
<node CREATED="1392875629012" ID="Freemind_Link_1247574384" MODIFIED="1392875630524" TEXT="403 FORBIDDEN"/>
<node CREATED="1392875544725" ID="Freemind_Link_1260320030" MODIFIED="1392875545691" TEXT="404 NOT FOUND"/>
<node CREATED="1392875552875" ID="Freemind_Link_913076297" MODIFIED="1392875553947" TEXT="409 CONFLICT"/>
<node CREATED="1392875561613" ID="Freemind_Link_914457291" MODIFIED="1392875562660" TEXT="412 PRECONDITION FAILED"/>
<node CREATED="1392875571034" ID="Freemind_Link_1350162323" MODIFIED="1392875572188" TEXT="413 REQUEST ENTITY TOO LARGE"/>
<node CREATED="1392875583774" ID="Freemind_Link_269304428" MODIFIED="1392875584796" TEXT="500 INTERNAL SERVER ERROR"/>
<node CREATED="1392875593354" ID="Freemind_Link_321873347" MODIFIED="1392875594724" TEXT="501 NOT IMPLEMENTED"/>
</node>
<node CREATED="1392876080974" ID="Freemind_Link_494664362" MODIFIED="1392876086157" POSITION="right" TEXT="SCIM Providers">
<node CREATED="1392806364627" ID="Freemind_Link_197546988" MODIFIED="1392876125263" TEXT="IS as SCIM service provider">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392867947223" ID="Freemind_Link_1090377158" LINK="#Freemind_Link_913628009" MODIFIED="1392874111395" TEXT="User Stores">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392808099789" ID="Freemind_Link_1310745020" MODIFIED="1392876920704" TEXT="User end point">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392867990459" ID="Freemind_Link_368735725" MODIFIED="1392868759087" TEXT="Other tenants">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392807829535" ID="Freemind_Link_535697207" MODIFIED="1392868798762" TEXT="Add user">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868136957" ID="Freemind_Link_674728483" MODIFIED="1392868139555" TEXT="Add new user">
<node CREATED="1392886262268" ID="Freemind_Link_780677339" MODIFIED="1392886288488" TEXT="Check whether user is shwon in UI without logging out (https://wso2.org/jira/browse/IDENTITY-1266)"/>
</node>
<node CREATED="1392868139881" ID="Freemind_Link_1156459547" MODIFIED="1392868143278" TEXT="Add duplicate user"/>
<node CREATED="1392868269774" ID="Freemind_Link_1291846529" MODIFIED="1392868278299" TEXT="Add user to seconday user store"/>
</node>
<node CREATED="1392807845918" ID="Freemind_Link_1517181038" MODIFIED="1392868800016" TEXT="Update user">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868325132" ID="Freemind_Link_422368956" MODIFIED="1392868334115" TEXT="Update existing user">
<node CREATED="1392884492848" ID="Freemind_Link_1156041177" MODIFIED="1392884495435" TEXT="Modify Email (UserID) via SCIM">
<node CREATED="1392884525677" ID="Freemind_Link_771363984" MODIFIED="1392884528324" TEXT="http://tanyamadurapperuma.blogspot.com/2013/10/tricky-way-to-modify-username-scim.html"/>
</node>
<node CREATED="1392884597041" ID="Freemind_Link_431995503" MODIFIED="1392884600544" TEXT="Change password">
<node CREATED="1392884720560" ID="Freemind_Link_905416340" MODIFIED="1392884723540" TEXT="By admin"/>
<node CREATED="1392884723799" ID="Freemind_Link_548845637" MODIFIED="1392884740491" TEXT="By user (https://wso2.org/jira/browse/IDENTITY-526)"/>
</node>
<node CREATED="1392884786805" ID="Freemind_Link_682762175" MODIFIED="1392884790015" TEXT="Update role list"/>
<node CREATED="1392885198824" ID="Freemind_Link_133821779" MODIFIED="1392885205491" TEXT="Updated added claim attributes">
<node CREATED="1392885259882" ID="Freemind_Link_485805109" MODIFIED="1392885269812" TEXT="Unable to update Added Claim Attributes via SCIM - SPRT"/>
</node>
</node>
<node CREATED="1392868340381" ID="Freemind_Link_235448995" MODIFIED="1392868346735" TEXT="Update non-existing user"/>
<node CREATED="1392868364071" ID="Freemind_Link_329390513" MODIFIED="1392885910033" TEXT="Update users added through UM UI (will receive  {&quot;Errors&quot;:[{&quot;description&quot;:&quot;Request is unparseable, syntactically incorrect, or violates schema&quot;,&quot;code&quot;:&quot;400&quot;}]} - not supported)">
<icon BUILTIN="button_cancel"/>
</node>
<node CREATED="1392868382131" ID="Freemind_Link_1519398211" MODIFIED="1392868385878" TEXT="Update admin profile"/>
<node CREATED="1392868396685" ID="Freemind_Link_1851520242" MODIFIED="1392868516205" TEXT="Update a user in secondary user store"/>
<node CREATED="1392868517184" ID="Freemind_Link_1993631156" MODIFIED="1392868521194" TEXT="Remove attributes"/>
</node>
<node CREATED="1392807861333" ID="Freemind_Link_450689671" MODIFIED="1392868801712" TEXT="List specific user">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868538367" ID="Freemind_Link_1926670556" MODIFIED="1392868543088" TEXT="List existing user"/>
<node CREATED="1392868543542" ID="Freemind_Link_1525102401" MODIFIED="1392868548166" TEXT="List non existing user"/>
<node CREATED="1392868548455" ID="Freemind_Link_96336329" MODIFIED="1392868562337" TEXT="List a user from secondary user store"/>
<node CREATED="1392885630544" ID="Freemind_Link_1913780290" MODIFIED="1392885761905" TEXT="Retrieve JPEG image (https://support.wso2.com/jira/browse/WSOQATESTC-88)"/>
</node>
<node CREATED="1392807866814" ID="Freemind_Link_182723470" MODIFIED="1392868802945" TEXT="List all users">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868589456" ID="Freemind_Link_1751703743" MODIFIED="1392868595556" TEXT="Check whether admin user is listed"/>
<node CREATED="1392868595831" ID="Freemind_Link_1645308163" MODIFIED="1392868604056" TEXT="Check whether users in secondary user stores are listed"/>
<node CREATED="1392868604297" ID="Freemind_Link_942609317" MODIFIED="1392868616097" TEXT="Check whether users added through UM UI are listed"/>
</node>
<node CREATED="1392807870035" ID="Freemind_Link_1023653103" MODIFIED="1392868804017" TEXT="Filter users">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868655744" ID="Freemind_Link_259549974" MODIFIED="1392868660739" TEXT="Filter existing user"/>
<node CREATED="1392868660986" ID="Freemind_Link_1346458919" MODIFIED="1392868665767" TEXT="Filter non existing user"/>
<node CREATED="1392868676876" ID="Freemind_Link_1069795800" MODIFIED="1392868679795" TEXT="Filter admin user"/>
<node CREATED="1392868693407" ID="Freemind_Link_1657079911" MODIFIED="1392868695023" TEXT="Filter user with username containing different characters"/>
<node CREATED="1392868708201" ID="Freemind_Link_1082085683" MODIFIED="1392868709828" TEXT="Filter users in secondary domains"/>
<node CREATED="1392868723285" ID="Freemind_Link_723971043" MODIFIED="1392884207522" TEXT="Filter using different attribute names">
<node CREATED="1392884167664" ID="Freemind_Link_1050906642" MODIFIED="1392884170834" TEXT="UserName"/>
<node CREATED="1392884171652" ID="Freemind_Link_700815219" MODIFIED="1392884356950" TEXT="Other e.g. userId - SPRT - Extended SCIM Filter Functionality"/>
</node>
<node CREATED="1392884210274" ID="Freemind_Link_1500110775" MODIFIED="1392884224870" TEXT="Attribute operator"/>
</node>
<node CREATED="1392807883406" ID="Freemind_Link_56694150" MODIFIED="1392868805672" TEXT="Delete users">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392869920123" ID="Freemind_Link_330301611" MODIFIED="1392869924938" TEXT="Delete non existing user">
<node CREATED="1392884238832" ID="Freemind_Link_899703249" MODIFIED="1392884240940" TEXT="Eq"/>
</node>
<node CREATED="1392869915524" ID="Freemind_Link_1751868977" MODIFIED="1392869919843" TEXT="Delete existing user"/>
<node CREATED="1392869925168" ID="Freemind_Link_1465823408" MODIFIED="1392869940482" TEXT="Delete user in secondary user store"/>
<node CREATED="1392869940709" ID="Freemind_Link_36975126" MODIFIED="1392869970638" TEXT="Try to delete admin user and users with admin privilage"/>
</node>
<node CREATED="1392875051549" ID="Freemind_Link_809210698" MODIFIED="1392875056362" TEXT="Bulk Operations">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392875061478" ID="Freemind_Link_1390436800" MODIFIED="1392875064708" TEXT="Add user"/>
<node CREATED="1392875065323" ID="Freemind_Link_613043698" MODIFIED="1392875068001" TEXT="Delete user"/>
</node>
</node>
<node CREATED="1392867986238" ID="Freemind_Link_567404653" MODIFIED="1392868756474" TEXT="Super tenant">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392807829535" ID="Freemind_Link_1919154279" MODIFIED="1392868785062" TEXT="Add user">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868136957" ID="Freemind_Link_1677949660" MODIFIED="1392868139555" TEXT="Add new user"/>
<node CREATED="1392868139881" ID="Freemind_Link_516026134" MODIFIED="1392868143278" TEXT="Add duplicate user"/>
<node CREATED="1392868269774" ID="Freemind_Link_1567033311" MODIFIED="1392868278299" TEXT="Add user to seconday user store"/>
</node>
<node CREATED="1392807845918" ID="Freemind_Link_575809644" MODIFIED="1392868787787" TEXT="Update user">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868325132" ID="Freemind_Link_1392133740" MODIFIED="1392868334115" TEXT="Update existing user">
<node CREATED="1392884492848" ID="Freemind_Link_1690923022" MODIFIED="1392884495435" TEXT="Modify Email (UserID) via SCIM">
<node CREATED="1392884525677" ID="Freemind_Link_160402325" MODIFIED="1392884528324" TEXT="http://tanyamadurapperuma.blogspot.com/2013/10/tricky-way-to-modify-username-scim.html"/>
</node>
<node CREATED="1392884597041" ID="Freemind_Link_1258859843" MODIFIED="1392884600544" TEXT="Change password">
<node CREATED="1392884720560" ID="Freemind_Link_1029426117" MODIFIED="1392884723540" TEXT="By admin"/>
<node CREATED="1392884723799" ID="Freemind_Link_829313937" MODIFIED="1392884740491" TEXT="By user (https://wso2.org/jira/browse/IDENTITY-526)"/>
</node>
<node CREATED="1392884786805" ID="Freemind_Link_1766700152" MODIFIED="1392884790015" TEXT="Update role list"/>
<node CREATED="1392885198824" ID="Freemind_Link_1581720751" MODIFIED="1392885205491" TEXT="Updated added claim attributes"/>
</node>
<node CREATED="1392868340381" ID="Freemind_Link_1875567498" MODIFIED="1392868346735" TEXT="Update non-existing user"/>
<node CREATED="1392868364071" ID="Freemind_Link_1331395249" MODIFIED="1392886030101" TEXT="Update users added through UM UI (will receive  {&quot;Errors&quot;:[{&quot;description&quot;:&quot;Request is unparseable, syntactically incorrect, or violates schema&quot;,&quot;code&quot;:&quot;400&quot;}]} - not supported)">
<icon BUILTIN="button_cancel"/>
</node>
<node CREATED="1392868382131" ID="Freemind_Link_226510322" MODIFIED="1392868385878" TEXT="Update admin profile"/>
<node CREATED="1392868396685" ID="Freemind_Link_1554135709" MODIFIED="1392868512617" TEXT="Update a user in secondary user store"/>
<node CREATED="1392868473172" ID="Freemind_Link_1604596176" MODIFIED="1392868481593" TEXT="Check the attributes supported and not supported"/>
<node CREATED="1392868488983" ID="Freemind_Link_1191559875" MODIFIED="1392868494229" TEXT="Remove attributes"/>
</node>
<node CREATED="1392807861333" ID="Freemind_Link_851114790" MODIFIED="1392868788998" TEXT="List specific user">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868538367" ID="Freemind_Link_988529918" MODIFIED="1392868543088" TEXT="List existing user"/>
<node CREATED="1392868543542" ID="Freemind_Link_58970045" MODIFIED="1392868548166" TEXT="List non existing user"/>
<node CREATED="1392868548455" ID="Freemind_Link_1795463967" MODIFIED="1392868562337" TEXT="List a user from secondary user store"/>
<node CREATED="1392885772792" ID="Freemind_Link_1349410954" MODIFIED="1392885774437" TEXT="Retrieve JPEG image (https://support.wso2.com/jira/browse/WSOQATESTC-88)"/>
</node>
<node CREATED="1392807866814" ID="Freemind_Link_938852410" MODIFIED="1392868790331" TEXT="List all users">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868589456" ID="Freemind_Link_1908099749" MODIFIED="1392868595556" TEXT="Check whether admin user is listed"/>
<node CREATED="1392868595831" ID="Freemind_Link_642239431" MODIFIED="1392868604056" TEXT="Check whether users in secondary user stores are listed"/>
<node CREATED="1392868604297" ID="Freemind_Link_1230055998" MODIFIED="1392868616097" TEXT="Check whether users added through UM UI are listed"/>
</node>
<node CREATED="1392807870035" ID="Freemind_Link_263747738" MODIFIED="1392868791649" TEXT="Filter users">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868655744" ID="Freemind_Link_1703865448" MODIFIED="1392868660739" TEXT="Filter existing user"/>
<node CREATED="1392868660986" ID="Freemind_Link_87084315" MODIFIED="1392868665767" TEXT="Filter non existing user"/>
<node CREATED="1392868676876" ID="Freemind_Link_760017006" MODIFIED="1392868679795" TEXT="Filter admin user"/>
<node CREATED="1392868693407" ID="Freemind_Link_87009628" MODIFIED="1392868695023" TEXT="Filter user with username containing different characters"/>
<node CREATED="1392868708201" ID="Freemind_Link_1444852591" MODIFIED="1392868709828" TEXT="Filter users in secondary domains"/>
<node CREATED="1392868723285" ID="Freemind_Link_315433213" MODIFIED="1392868725689" TEXT="Filter uses with other attributes (other than user name)"/>
<node CREATED="1392868723285" ID="Freemind_Link_592953491" MODIFIED="1392884207522" TEXT="Filter using different attribute names">
<node CREATED="1392884167664" ID="Freemind_Link_1619507656" MODIFIED="1392884170834" TEXT="UserName"/>
<node CREATED="1392884171652" ID="Freemind_Link_132732740" MODIFIED="1392884356950" TEXT="Other e.g. userId - SPRT - Extended SCIM Filter Functionality"/>
</node>
<node CREATED="1392884210274" ID="Freemind_Link_1348375663" MODIFIED="1392884224870" TEXT="Attribute operator"/>
</node>
<node CREATED="1392807883406" ID="Freemind_Link_178048167" MODIFIED="1392868792838" TEXT="Delete users">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392869915524" ID="Freemind_Link_467810381" MODIFIED="1392869919843" TEXT="Delete existing user"/>
<node CREATED="1392869920123" ID="Freemind_Link_839109754" MODIFIED="1392869924938" TEXT="Delete non existing user"/>
<node CREATED="1392869925168" ID="Freemind_Link_1629680283" MODIFIED="1392869940482" TEXT="Delete user in secondary user store"/>
<node CREATED="1392869940709" ID="Freemind_Link_449617304" MODIFIED="1392869970638" TEXT="Try to delete admin user and users with admin privilage"/>
</node>
<node CREATED="1392875006497" ID="Freemind_Link_1054305395" MODIFIED="1392875015637" TEXT="Bulk operations">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392875037962" ID="Freemind_Link_1701215384" MODIFIED="1392875039674" TEXT="Add user"/>
<node CREATED="1392875039937" ID="Freemind_Link_441823420" MODIFIED="1392875042451" TEXT="Delete user"/>
</node>
</node>
</node>
<node CREATED="1392864409794" ID="Freemind_Link_825449854" MODIFIED="1392876924633" TEXT="Group end point">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392868053677" ID="Freemind_Link_1360226585" MODIFIED="1392868763815" TEXT="Other tenants">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392864415960" ID="Freemind_Link_989963955" MODIFIED="1392868817473" TEXT="Add group">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392870204805" ID="Freemind_Link_109857178" MODIFIED="1392870207118" TEXT="Add a group"/>
<node CREATED="1392870207342" ID="Freemind_Link_1771907546" MODIFIED="1392870213074" TEXT="Add a group with duplicate name"/>
<node CREATED="1392873384666" ID="Freemind_Link_1066549126" MODIFIED="1392873389502" TEXT="Add group without member"/>
<node CREATED="1392873420064" ID="Freemind_Link_1569390791" MODIFIED="1392873425172" TEXT="Add a group to secondary user store"/>
</node>
<node CREATED="1392864420354" ID="Freemind_Link_1345947663" MODIFIED="1392868818562" TEXT="Update group">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392873966922" ID="Freemind_Link_1146290355" MODIFIED="1392873968508" TEXT="Try to update existing group">
<node CREATED="1392886696206" ID="Freemind_Link_1679516060" MODIFIED="1392886698970" TEXT="Group details"/>
<node CREATED="1392886699442" ID="Freemind_Link_1983334240" MODIFIED="1392886703621" TEXT="Add user to group"/>
</node>
<node CREATED="1392873978556" ID="Freemind_Link_1761327670" MODIFIED="1392873980013" TEXT="Update non existing group"/>
<node CREATED="1392873992662" ID="Freemind_Link_2595230" MODIFIED="1392873994078" TEXT="Update group in secondary user store"/>
<node CREATED="1392874002855" ID="Freemind_Link_312360966" MODIFIED="1392874005591" TEXT="Update group created through carbon UM"/>
<node CREATED="1392884800966" ID="Freemind_Link_422464803" MODIFIED="1392884811932" TEXT="Update user list (https://wso2.org/jira/browse/IDENTITY-526)"/>
</node>
<node CREATED="1392864424422" ID="Freemind_Link_1562619758" MODIFIED="1392868819916" TEXT="List specific group">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392873894387" ID="Freemind_Link_957977992" MODIFIED="1392873899942" TEXT="List an existing grouop"/>
<node CREATED="1392873903283" ID="Freemind_Link_1459436023" MODIFIED="1392873909088" TEXT="List non existing group"/>
<node CREATED="1392873925514" ID="Freemind_Link_1820216145" MODIFIED="1392873927404" TEXT="Try to list a group from secondary user store"/>
<node CREATED="1392873934097" ID="Freemind_Link_473064431" MODIFIED="1392873935475" TEXT="Try to list a group created through carbon UM"/>
</node>
<node CREATED="1392864431640" ID="Freemind_Link_1078353158" MODIFIED="1392868821068" TEXT="List all groups">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392873451703" ID="Freemind_Link_648956111" MODIFIED="1392873461889" TEXT="Check whether admin group is also listed"/>
<node CREATED="1392873477181" ID="Freemind_Link_1811353013" MODIFIED="1392873478766" TEXT="Check whether roles in secondary user stores are also listed"/>
<node CREATED="1392873489123" ID="Freemind_Link_149705011" MODIFIED="1392873490811" TEXT="Check whether groups created through UM are also listed"/>
</node>
<node CREATED="1392864438779" ID="Freemind_Link_1070001058" MODIFIED="1392868822189" TEXT="Filter groups">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392874795416" ID="Freemind_Link_525478255" MODIFIED="1392874800181" TEXT="Filter existing group"/>
<node CREATED="1392874800402" ID="Freemind_Link_1197242543" MODIFIED="1392874805187" TEXT="Filter non existing group"/>
<node CREATED="1392874805399" ID="Freemind_Link_87299979" MODIFIED="1392874861352" TEXT="filter group with special characters"/>
<node CREATED="1392874875552" ID="Freemind_Link_598056196" MODIFIED="1392874877056" TEXT="filter group in secondary user store"/>
<node CREATED="1392874904961" ID="Freemind_Link_517416319" MODIFIED="1392874906213" TEXT="Filter group created through carbon um"/>
</node>
<node CREATED="1392864442813" ID="Freemind_Link_1032772045" MODIFIED="1392868823354" TEXT="Delete groups">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392874614569" ID="Freemind_Link_250159114" MODIFIED="1392874616500" TEXT="delete non existing group"/>
<node CREATED="1392874678826" ID="Freemind_Link_269364532" MODIFIED="1392874684624" TEXT="delete existing group"/>
<node CREATED="1392874698125" ID="Freemind_Link_598864926" MODIFIED="1392874699571" TEXT="delete group with members"/>
<node CREATED="1392874700266" ID="Freemind_Link_67463074" MODIFIED="1392874707590" TEXT="Delete a group in secondary user store"/>
<node CREATED="1392874718895" ID="Freemind_Link_985735490" MODIFIED="1392874720445" TEXT="delete a group created through management console"/>
</node>
<node CREATED="1392875097145" ID="Freemind_Link_331842803" MODIFIED="1392875101857" TEXT="Bulk Operations">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392875107262" ID="Freemind_Link_1189536846" MODIFIED="1392875110336" TEXT="Create group"/>
</node>
</node>
<node CREATED="1392868049557" ID="Freemind_Link_1566821557" MODIFIED="1392868762862" TEXT="Super tenant">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392864415960" ID="Freemind_Link_1189188528" MODIFIED="1392868806755" TEXT="Add group">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392870204805" ID="Freemind_Link_1875683976" MODIFIED="1392870207118" TEXT="Add a group"/>
<node CREATED="1392870207342" ID="Freemind_Link_1184826082" MODIFIED="1392870213074" TEXT="Add a group with duplicate name"/>
<node CREATED="1392873384666" ID="Freemind_Link_972831719" MODIFIED="1392873389502" TEXT="Add group without member"/>
<node CREATED="1392873420064" ID="Freemind_Link_343247163" MODIFIED="1392873425172" TEXT="Add a group to secondary user store"/>
</node>
<node CREATED="1392864420354" ID="Freemind_Link_681372951" MODIFIED="1392868810411" TEXT="Update group">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392873966922" ID="Freemind_Link_1400292283" MODIFIED="1392873968508" TEXT="Try to update existing group">
<node CREATED="1392886696206" ID="Freemind_Link_1807042853" MODIFIED="1392886698970" TEXT="Group details"/>
<node CREATED="1392886699442" ID="Freemind_Link_256903451" MODIFIED="1392886703621" TEXT="Add user to group"/>
</node>
<node CREATED="1392873978556" ID="Freemind_Link_518821460" MODIFIED="1392873980013" TEXT="Update non existing group"/>
<node CREATED="1392873992662" ID="Freemind_Link_1649507099" MODIFIED="1392873994078" TEXT="Update group in secondary user store"/>
<node CREATED="1392874002855" ID="Freemind_Link_415057686" MODIFIED="1392874005591" TEXT="Update group created through carbon UM"/>
<node CREATED="1392884814683" ID="Freemind_Link_1459067436" MODIFIED="1392884820464" TEXT="Update user list (https://wso2.org/jira/browse/IDENTITY-526)"/>
</node>
<node CREATED="1392864424422" ID="Freemind_Link_1290493564" MODIFIED="1392868811457" TEXT="List specific group">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392873894387" ID="Freemind_Link_1010034863" MODIFIED="1392873899942" TEXT="List an existing grouop"/>
<node CREATED="1392873903283" ID="Freemind_Link_880577747" MODIFIED="1392873909088" TEXT="List non existing group"/>
<node CREATED="1392873925514" ID="Freemind_Link_119693168" MODIFIED="1392873927404" TEXT="Try to list a group from secondary user store"/>
<node CREATED="1392873934097" ID="Freemind_Link_331623824" MODIFIED="1392873935475" TEXT="Try to list a group created through carbon UM"/>
</node>
<node CREATED="1392864431640" ID="Freemind_Link_673835832" MODIFIED="1392868812546" TEXT="List all groups">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392873451703" ID="Freemind_Link_1866613096" MODIFIED="1392873461889" TEXT="Check whether admin group is also listed"/>
<node CREATED="1392873477181" ID="Freemind_Link_1612766194" MODIFIED="1392873478766" TEXT="Check whether roles in secondary user stores are also listed"/>
<node CREATED="1392873489123" ID="Freemind_Link_1926431118" MODIFIED="1392873490811" TEXT="Check whether groups created through UM are also listed"/>
</node>
<node CREATED="1392864438779" ID="Freemind_Link_327188169" MODIFIED="1392868813475" TEXT="Filter groups">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392874795416" ID="Freemind_Link_553802810" MODIFIED="1392874800181" TEXT="Filter existing group"/>
<node CREATED="1392874800402" ID="Freemind_Link_406561130" MODIFIED="1392874805187" TEXT="Filter non existing group"/>
<node CREATED="1392874805399" ID="Freemind_Link_1729555267" MODIFIED="1392874861352" TEXT="filter group with special characters"/>
<node CREATED="1392874875552" ID="Freemind_Link_1737837090" MODIFIED="1392874877056" TEXT="filter group in secondary user store"/>
<node CREATED="1392874904961" ID="Freemind_Link_687723995" MODIFIED="1392874906213" TEXT="Filter group created through carbon um"/>
</node>
<node CREATED="1392864442813" ID="Freemind_Link_1389326027" MODIFIED="1392868814401" TEXT="Delete groups">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392874614569" ID="Freemind_Link_695959010" MODIFIED="1392874616500" TEXT="delete non existing group"/>
<node CREATED="1392874678826" ID="Freemind_Link_887881373" MODIFIED="1392874684624" TEXT="delete existing group"/>
<node CREATED="1392874698125" ID="Freemind_Link_1920757119" MODIFIED="1392874699571" TEXT="delete group with members"/>
<node CREATED="1392874700266" ID="Freemind_Link_1202193442" MODIFIED="1392874707590" TEXT="Delete a group in secondary user store"/>
<node CREATED="1392874718895" ID="Freemind_Link_73336792" MODIFIED="1392874720445" TEXT="delete a group created through management console"/>
</node>
<node CREATED="1392875075412" ID="Freemind_Link_1348966303" MODIFIED="1392875082462" TEXT="Bulk Operations">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392875091598" ID="Freemind_Link_422451267" MODIFIED="1392875094325" TEXT="Create group"/>
</node>
</node>
</node>
</node>
</node>
<node CREATED="1392876286443" ID="Freemind_Link_1909441124" MODIFIED="1392876294943" TEXT="Manage SCIM providers">
<node CREATED="1392876101480" ID="Freemind_Link_1689215242" MODIFIED="1392876127721" TEXT="Register new SCIM provider">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392876137344" ID="Freemind_Link_1562851461" MODIFIED="1392876141391" TEXT="Global provider"/>
<node CREATED="1392876141517" ID="Freemind_Link_605922707" MODIFIED="1392876152804" TEXT="providers specific to a particular user account"/>
</node>
<node CREATED="1392876305587" ID="Freemind_Link_66286656" MODIFIED="1392876318710" TEXT="View and update SCIM providers"/>
<node CREATED="1392876319001" ID="Freemind_Link_605572111" MODIFIED="1392876325650" TEXT="Delete SCIM providers"/>
</node>
</node>
<node CREATED="1392876363727" ID="Freemind_Link_783390628" MODIFIED="1392876368846" POSITION="left" TEXT="Integration scenarios">
<node CREATED="1392876484205" ID="Freemind_Link_493742876" MODIFIED="1392876509853" TEXT="Manage user identities in multiple nodes (http://docs.wso2.org/display/IS460/Identity+Synchronization+Across+Multiple+Nodes)">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392876561611" ID="Freemind_Link_1837697875" MODIFIED="1392876573331" TEXT="IS as SCIM service provider and consumer"/>
<node CREATED="1392876573851" ID="Freemind_Link_382840147" MODIFIED="1392876598209" TEXT="Multiple nodes have different user stores"/>
<node CREATED="1392876605663" ID="Freemind_Link_1150756511" MODIFIED="1392876614741" TEXT="Nodes have secondary users stores"/>
<node CREATED="1392876618111" ID="Freemind_Link_1116214562" MODIFIED="1392876629168" TEXT="Test with tenant mode"/>
<node CREATED="1392876618232" ID="Freemind_Link_220932456" MODIFIED="1392876644642" TEXT="Secondary user stores in tenant mode"/>
<node CREATED="1392886788533" ID="Freemind_Link_230464329" MODIFIED="1392886797865" TEXT="SCIM Operations">
<node CREATED="1392886820677" ID="Freemind_Link_919169282" MODIFIED="1392886824084" TEXT="Super tenant">
<node CREATED="1392886854373" ID="Freemind_Link_164409412" MODIFIED="1392886856640" TEXT="Add user">
<node CREATED="1392887848440" ID="Freemind_Link_1108622862" MODIFIED="1392887852813" TEXT="Add use with a role"/>
<node CREATED="1392887853070" ID="Freemind_Link_1944919539" MODIFIED="1392887856141" TEXT="Add user without role"/>
</node>
<node CREATED="1392886856887" ID="Freemind_Link_196704006" MODIFIED="1392886858714" TEXT="Update user">
<node CREATED="1392886883257" ID="Freemind_Link_1540487709" MODIFIED="1392887169915" TEXT="Assign a role to user"/>
<node CREATED="1392886888043" ID="Freemind_Link_1024603865" MODIFIED="1392886892633" TEXT="Remove a user from role"/>
<node CREATED="1392886957491" ID="Freemind_Link_555420017" MODIFIED="1392886961248" TEXT="Change password">
<node CREATED="1392886961197" ID="Freemind_Link_1612630792" MODIFIED="1392886963557" TEXT="By admin"/>
<node CREATED="1392886963800" ID="Freemind_Link_1647785840" MODIFIED="1392886966945" TEXT="By the user"/>
</node>
<node CREATED="1392887601510" ID="Freemind_Link_294752776" MODIFIED="1392887608428" TEXT="Change other details of user"/>
</node>
<node CREATED="1392886859201" ID="Freemind_Link_1807183627" MODIFIED="1392886861599" TEXT="List all users"/>
<node CREATED="1392886862064" ID="Freemind_Link_1522572794" MODIFIED="1392886872588" TEXT="List specific user"/>
<node CREATED="1392886873362" ID="Freemind_Link_1051039522" MODIFIED="1392886876133" TEXT="Filter user"/>
<node CREATED="1392886876423" ID="Freemind_Link_962804190" MODIFIED="1392886882992" TEXT="delet user"/>
<node CREATED="1392886900444" ID="Freemind_Link_698995775" MODIFIED="1392886902509" TEXT="Add role">
<node CREATED="1392887880597" ID="Freemind_Link_1060918654" MODIFIED="1392887883838" TEXT="Add role with user"/>
<node CREATED="1392887884094" ID="Freemind_Link_404882391" MODIFIED="1392887887220" TEXT="Add role without user"/>
</node>
<node CREATED="1392886902956" ID="Freemind_Link_766778103" MODIFIED="1392886905672" TEXT="Update role"/>
<node CREATED="1392886906050" ID="Freemind_Link_186363710" MODIFIED="1392886909454" TEXT="List all roles"/>
<node CREATED="1392886910220" ID="Freemind_Link_331884243" MODIFIED="1392886914396" TEXT="List a specific role"/>
<node CREATED="1392886914652" ID="Freemind_Link_131863835" MODIFIED="1392886917209" TEXT="Filter role"/>
<node CREATED="1392886917483" ID="Freemind_Link_1565354086" MODIFIED="1392886920016" TEXT="Delete role"/>
<node CREATED="1392886920289" ID="Freemind_Link_1804011488" MODIFIED="1392886925795" TEXT="Add a new user to role"/>
<node CREATED="1392886925999" ID="Freemind_Link_1526212954" MODIFIED="1392887119008" TEXT="Remove last assigned user from role"/>
<node CREATED="1392886933426" ID="Freemind_Link_1915748753" MODIFIED="1392887623401" TEXT="Update user profile by user"/>
<node CREATED="1392886943395" ID="Freemind_Link_1043365486" MODIFIED="1392886948093" TEXT="User profile udpate by admin"/>
</node>
<node CREATED="1392886824345" ID="Freemind_Link_1549627302" MODIFIED="1392886827227" TEXT="Other tenants">
<node CREATED="1392886854373" ID="Freemind_Link_78020647" MODIFIED="1392886856640" TEXT="Add user">
<node CREATED="1392887862040" ID="Freemind_Link_848415252" MODIFIED="1392887865367" TEXT="Add user with role"/>
<node CREATED="1392887865623" ID="Freemind_Link_1382699702" MODIFIED="1392887872089" TEXT="Add user without role"/>
</node>
<node CREATED="1392886856887" ID="Freemind_Link_1523781393" MODIFIED="1392886858714" TEXT="Update user">
<node CREATED="1392886883257" ID="Freemind_Link_1422022945" MODIFIED="1392887169915" TEXT="Assign a role to user"/>
<node CREATED="1392886888043" ID="Freemind_Link_1926747383" MODIFIED="1392886892633" TEXT="Remove a user from role"/>
<node CREATED="1392886957491" ID="Freemind_Link_1240732920" MODIFIED="1392886961248" TEXT="Change password">
<node CREATED="1392886961197" ID="Freemind_Link_1790406873" MODIFIED="1392886963557" TEXT="By admin"/>
<node CREATED="1392886963800" ID="Freemind_Link_923642342" MODIFIED="1392886966945" TEXT="By the user"/>
</node>
<node CREATED="1392887601510" ID="Freemind_Link_24524353" MODIFIED="1392887608428" TEXT="Change other details of user"/>
</node>
<node CREATED="1392886859201" ID="Freemind_Link_1239836925" MODIFIED="1392886861599" TEXT="List all users"/>
<node CREATED="1392886862064" ID="Freemind_Link_1809298469" MODIFIED="1392886872588" TEXT="List specific user"/>
<node CREATED="1392886873362" ID="Freemind_Link_942934151" MODIFIED="1392886876133" TEXT="Filter user"/>
<node CREATED="1392886876423" ID="Freemind_Link_858181560" MODIFIED="1392886882992" TEXT="delet user"/>
<node CREATED="1392886900444" ID="Freemind_Link_1129154047" MODIFIED="1392886902509" TEXT="Add role">
<node CREATED="1392887891431" ID="Freemind_Link_229867946" MODIFIED="1392887895248" TEXT="Add role with user"/>
<node CREATED="1392887895622" ID="Freemind_Link_1752987505" MODIFIED="1392887900144" TEXT="Add role without user"/>
</node>
<node CREATED="1392886902956" ID="Freemind_Link_173333045" MODIFIED="1392886905672" TEXT="Update role"/>
<node CREATED="1392886906050" ID="Freemind_Link_1720096256" MODIFIED="1392886909454" TEXT="List all roles"/>
<node CREATED="1392886910220" ID="Freemind_Link_679470312" MODIFIED="1392886914396" TEXT="List a specific role"/>
<node CREATED="1392886914652" ID="Freemind_Link_1571172116" MODIFIED="1392886917209" TEXT="Filter role"/>
<node CREATED="1392886917483" ID="Freemind_Link_1106204234" MODIFIED="1392886920016" TEXT="Delete role"/>
<node CREATED="1392886920289" ID="Freemind_Link_296555155" MODIFIED="1392886925795" TEXT="Add a new user to role"/>
<node CREATED="1392886925999" ID="Freemind_Link_1794912027" MODIFIED="1392887128647" TEXT="Remove last assigned user from role a user from role"/>
<node CREATED="1392886933426" ID="Freemind_Link_358950683" MODIFIED="1392887629158" TEXT="Update user profile by user"/>
<node CREATED="1392886943395" ID="Freemind_Link_1802019823" MODIFIED="1392886948093" TEXT="User profile udpate by admin"/>
</node>
</node>
</node>
<node CREATED="1392877161772" ID="Freemind_Link_1487093776" MODIFIED="1392877165016" TEXT=" identity provisioning from on-premise to cloud using Identity Server and Stratos">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392877177383" ID="Freemind_Link_681002053" MODIFIED="1392877179327" TEXT="http://docs.wso2.org/display/IS460/Identity+Provisioning+from+On-premise+to+Cloud"/>
<node CREATED="1392886788533" ID="Freemind_Link_1189634429" MODIFIED="1392886797865" TEXT="SCIM Operations">
<node CREATED="1392886820677" ID="Freemind_Link_102693876" MODIFIED="1392886824084" TEXT="Super tenant">
<node CREATED="1392886854373" ID="Freemind_Link_1541811376" MODIFIED="1392886856640" TEXT="Add user"/>
<node CREATED="1392886856887" ID="Freemind_Link_1913404957" MODIFIED="1392886858714" TEXT="Update user"/>
<node CREATED="1392886859201" ID="Freemind_Link_376061615" MODIFIED="1392886861599" TEXT="List all users"/>
<node CREATED="1392886862064" ID="Freemind_Link_1625237306" MODIFIED="1392886872588" TEXT="List specific user"/>
<node CREATED="1392886873362" ID="Freemind_Link_1639712227" MODIFIED="1392886876133" TEXT="Filter user"/>
<node CREATED="1392886876423" ID="Freemind_Link_1032217200" MODIFIED="1392886882992" TEXT="delet user"/>
<node CREATED="1392886883257" ID="Freemind_Link_1045147105" MODIFIED="1392886887724" TEXT="Assign a user to role"/>
<node CREATED="1392886888043" ID="Freemind_Link_656040329" MODIFIED="1392886892633" TEXT="Remove a user from role"/>
<node CREATED="1392886900444" ID="Freemind_Link_1271290357" MODIFIED="1392886902509" TEXT="Add role"/>
<node CREATED="1392886902956" ID="Freemind_Link_1407827388" MODIFIED="1392886905672" TEXT="Update role"/>
<node CREATED="1392886906050" ID="Freemind_Link_1095880032" MODIFIED="1392886909454" TEXT="List all roles"/>
<node CREATED="1392886910220" ID="Freemind_Link_1873283925" MODIFIED="1392886914396" TEXT="List a specific role"/>
<node CREATED="1392886914652" ID="Freemind_Link_877801396" MODIFIED="1392886917209" TEXT="Filter role"/>
<node CREATED="1392886917483" ID="Freemind_Link_568425563" MODIFIED="1392886920016" TEXT="Delete role"/>
<node CREATED="1392886920289" ID="Freemind_Link_1591815957" MODIFIED="1392886925795" TEXT="Add a new user to role"/>
<node CREATED="1392886925999" ID="Freemind_Link_1951653107" MODIFIED="1392886933208" TEXT="Remove a user from role"/>
<node CREATED="1392886933426" ID="Freemind_Link_1681590322" MODIFIED="1392886942875" TEXT="Update user profile"/>
<node CREATED="1392886943395" ID="Freemind_Link_557636238" MODIFIED="1392886948093" TEXT="User profile udpate by admin"/>
<node CREATED="1392886957491" ID="Freemind_Link_91006905" MODIFIED="1392886961248" TEXT="Change password">
<node CREATED="1392886961197" ID="Freemind_Link_380269297" MODIFIED="1392886963557" TEXT="By admin"/>
<node CREATED="1392886963800" ID="Freemind_Link_988752182" MODIFIED="1392886966945" TEXT="By the user"/>
</node>
</node>
<node CREATED="1392886824345" ID="Freemind_Link_183801656" MODIFIED="1392886827227" TEXT="Other tenants">
<node CREATED="1392886854373" ID="Freemind_Link_1356391316" MODIFIED="1392886856640" TEXT="Add user"/>
<node CREATED="1392886856887" ID="Freemind_Link_438094654" MODIFIED="1392886858714" TEXT="Update user"/>
<node CREATED="1392886859201" ID="Freemind_Link_954172512" MODIFIED="1392886861599" TEXT="List all users"/>
<node CREATED="1392886862064" ID="Freemind_Link_1170652336" MODIFIED="1392886872588" TEXT="List specific user"/>
<node CREATED="1392886873362" ID="Freemind_Link_1636259939" MODIFIED="1392886876133" TEXT="Filter user"/>
<node CREATED="1392886876423" ID="Freemind_Link_1447730797" MODIFIED="1392886882992" TEXT="delet user"/>
<node CREATED="1392886883257" ID="Freemind_Link_598501664" MODIFIED="1392886887724" TEXT="Assign a user to role"/>
<node CREATED="1392886888043" ID="Freemind_Link_1836408468" MODIFIED="1392886892633" TEXT="Remove a user from role"/>
<node CREATED="1392886900444" ID="Freemind_Link_636249396" MODIFIED="1392886902509" TEXT="Add role"/>
<node CREATED="1392886902956" ID="Freemind_Link_1777777721" MODIFIED="1392886905672" TEXT="Update role"/>
<node CREATED="1392886906050" ID="Freemind_Link_1160481470" MODIFIED="1392886909454" TEXT="List all roles"/>
<node CREATED="1392886910220" ID="Freemind_Link_340547394" MODIFIED="1392886914396" TEXT="List a specific role"/>
<node CREATED="1392886914652" ID="Freemind_Link_387835826" MODIFIED="1392886917209" TEXT="Filter role"/>
<node CREATED="1392886917483" ID="Freemind_Link_1295720224" MODIFIED="1392886920016" TEXT="Delete role"/>
<node CREATED="1392886920289" ID="Freemind_Link_1743766555" MODIFIED="1392886925795" TEXT="Add a new user to role"/>
<node CREATED="1392886925999" ID="Freemind_Link_1831349059" MODIFIED="1392886933208" TEXT="Remove a user from role"/>
<node CREATED="1392886933426" ID="Freemind_Link_1882120643" MODIFIED="1392886942875" TEXT="Update user profile"/>
<node CREATED="1392886943395" ID="Freemind_Link_607946318" MODIFIED="1392886948093" TEXT="User profile udpate by admin"/>
<node CREATED="1392886957491" ID="Freemind_Link_744906621" MODIFIED="1392886961248" TEXT="Change password">
<node CREATED="1392886961197" ID="Freemind_Link_494821150" MODIFIED="1392886963557" TEXT="By admin"/>
<node CREATED="1392886963800" ID="Freemind_Link_218060526" MODIFIED="1392886966945" TEXT="By the user"/>
</node>
</node>
</node>
</node>
</node>
<node CREATED="1392876710031" ID="Freemind_Link_1428280005" MODIFIED="1392876713761" POSITION="left" TEXT="SCIM Schema Extensions">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392876724466" ID="Freemind_Link_1090378899" MODIFIED="1392876725539" TEXT="http://docs.wso2.org/display/IS460/Extensible+SCIM+User+Schemas+With+WSO2+Identity+Server"/>
<node CREATED="1392876737430" ID="Freemind_Link_1495592040" MODIFIED="1392876739101" TEXT="https://www.google.com/url?q=http://sureshatt.blogspot.com/2013/07/extensible-scim-user-schemas-with-wso2.html&amp;sa=D&amp;usg=ALhdy29g6EyvVKeDy2lvGF2_neJGk7o3uw"/>
<node CREATED="1392877292651" ID="Freemind_Link_874741654" MODIFIED="1392877296892" TEXT="Custom user attributes for SCIM - SPRT"/>
</node>
<node CREATED="1392876820163" ID="Freemind_Link_1804480480" MODIFIED="1392876829994" POSITION="right" TEXT="Authentication for SCIM end points">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392876831127" ID="Freemind_Link_1529459965" MODIFIED="1392876908999" TEXT="OAuth authentication for SCIM endpoints">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
<node CREATED="1392876843515" ID="Freemind_Link_1492590601" MODIFIED="1392876845542" TEXT="http://docs.wso2.org/display/IS460/OAuth+Bearer+Token-based+Authentication+for+SCIM+Endpoints"/>
</node>
<node CREATED="1392876889782" ID="Freemind_Link_194335846" MODIFIED="1392876911234" TEXT="Basic Auth Authentication.">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
</node>
</node>
<node CREATED="1392877534840" ID="Freemind_Link_1465375544" MODIFIED="1392877537215" POSITION="left" TEXT="Load Test"/>
<node CREATED="1392889390802" ID="Freemind_Link_1043031963" MODIFIED="1392889398150" POSITION="right" TEXT="Supported endpoints">
<node CREATED="1392889413600" ID="Freemind_Link_1888336411" MODIFIED="1392889492211" TEXT="/Users">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1392889420049" ID="Freemind_Link_336993968" MODIFIED="1392889492213" TEXT="/Groups">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1392889446710" ID="Freemind_Link_1562171574" MODIFIED="1392889468733" TEXT="/ServiceProviderConfigs"/>
<node CREATED="1392889470120" ID="Freemind_Link_599951037" MODIFIED="1392889471807" TEXT="/Schemas "/>
<node CREATED="1392889472360" ID="Freemind_Link_582772135" MODIFIED="1392889496332" TEXT="/Bulk">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1392889485885" ID="Freemind_Link_31451646" MODIFIED="1392889487497" TEXT="[prefix]/.search"/>
</node>
<node CREATED="1392889976603" ID="Freemind_Link_576928251" MODIFIED="1392889976603" POSITION="left" TEXT="">
<node CREATED="1392889976686" ID="Freemind_Link_1157734791" MODIFIED="1392889983428" TEXT="HTTP methods supported">
<node CREATED="1392889984642" ID="Freemind_Link_1921558972" MODIFIED="1392890270113" TEXT="GET">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1392890175820" ID="Freemind_Link_210425250" MODIFIED="1392890270112" TEXT="pUT">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1392890177514" ID="Freemind_Link_1371807049" MODIFIED="1392890270111" TEXT="POST">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1392890182341" ID="Freemind_Link_425974369" MODIFIED="1392890270109" TEXT="DELETE">
<font BOLD="true" NAME="SansSerif" SIZE="12"/>
</node>
<node CREATED="1392890185111" ID="Freemind_Link_24555442" MODIFIED="1392890277654" TEXT="PATCH">
<icon BUILTIN="button_cancel"/>
</node>
</node>
</node>
</node>
</map>
