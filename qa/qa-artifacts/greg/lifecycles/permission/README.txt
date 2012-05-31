This lifecycle covers the following

a) permission for lifecycle items

E.g.:- Only users of archrole should be allowed to check the following LC item

<data name="checkItems">
    <item name="Requirements Gathered" forEvent="Promote,Demote">
        <permissions>
            <permission roles="archrole"/>
        </permissions>
    </item>
    :
</data>

b) forEvent attribute of checklist items

E.g.:- Inorder for the "Promote" and "Demote" buttons to be enabled, all three check list items should be clicked

<data name="checkItems">
    <item name="Effective Inspection Completed" forEvent="Promote,Demote">
        <permissions>
            <permission roles="devrole"/>
        </permissions>
    </item>
    <item name="Test Cases Passed" forEvent="Promote,Demote">
        <permissions>
            <permission roles="devrole"/>
        </permissions>
    </item>
    <item name="Smoke Test Passed" forEvent="Promote,Demote">
        <permissions>
            <permission roles="devrole"/>
        </permissions>
    </item>
</data>

c) permission for state transitions (promote/demote)

E.g.:- Only users who belong to "managerrole" should be able to promote the service to the next level

<data name="transitionPermission">
    <permission forEvent="Promote" roles="managerrole"/>
    <permission forEvent="Demote" roles="managerrole"/>
    <permission forEvent="Abort" roles="archrole"/>
</data>

d) script element usage for lifecycle transitions

E.g.:- Through this script, two actions should be done.
(i) A js alert should be promoted with given content, when the lifecycle is promoted. 
(ii) Once the service is promoted to the next state, the user should be directed to the Service List page.

<data name="transitionScripts">
    <js forEvent="Promote">
        <console function="doPromote">
            <script type="text/javascript">
                doPromote = function() {
                    window.location = unescape("../list/service.jsp?region=region3%26item=governance_list_services_menu");
                    alert("Promoted Resource to Creation State!");
                }
            </script>
        </console>
     </js> 				                   
</data>

e) using validator elements for checklist items

E.g.:- If you have three checklist items in your state, you need to click the first and second lifecycle items for the promote button to be enabled. Also, the third item should not be checked if you want the "promote" button to be enabled.

<data name="transitionValidation">
    <validation forEvent="Promote" class="org.wso2.carbon.governance.registry.extensions.validators.CheckItemTickedValidator">
        <parameter name="itemIndex" value="1"/>
        <parameter name="checked" value="true"/>                                
        <parameter name="itemIndex" value="2"/>
        <parameter name="checked" value="true"/>                                
        <parameter name="itemIndex" value="3"/>
        <parameter name="checked" value="false"/>
    </validation>
</data>	   

f) usage of "transitionExecution" for "Demote" event

E.g.:- Use the following xml segment in addition to the "Promote" event config. Then you will be able to verify whether your service is demoted to the previous environment correctly.

<data name="transitionExecution">
    <execution forEvent="Promote" class="org.wso2.carbon.governance.registry.extensions.executors.ServiceVersionExecutor">
     :
     :
    </execution>			
    <execution forEvent="Demote" class="org.wso2.carbon.governance.registry.extensions.executors.ServiceVersionExecutor">
        <parameter name="currentEnvironment" value="/_system/governance/wso2/carbon/branches/development/{@resourcePath}/{@version}/{@resourceName}"/>
        <parameter name="targetEnvironment" value="/_system/governance/wso2/carbon/trunk/{@resourcePath}/{@version}/{@resourceName}"/>
        <parameter name="service.mediatype" value="application/vnd.wso2-service+xml"/>
        <parameter name="wsdl.mediatype" value="application/wsdl+xml"/>
        <parameter name="endpoint.mediatype" value="application/vnd.wso2.endpoint"/>
    </execution>		
</data>

g) Usage of preserveOriginal=false and viewVersion=false parameters in the transitionUI segment of the lifecycle configuration

E.g.:- You can make 
(i) preserveOriginal parameter to true/false and verify whether the original service gets discarded or not once promoted
(ii) viewVersion parameter to true/false and verify whether text boxes are promoted to specify versions while promoting

<ui forEvent="Promote" href="../lifecycles/pre_invoke_aspect_ajaxprocessor.jsp?preserveOriginal=false%26viewVersion=false%26currentEnvironment=/_system/governance/wso2/carbon/branches/development/"/>

h) This lifecycle configuration ensures same environment (transition in same service) lifecycle  state transitions as well as different environment (trunk to branch) lifecycle state transitions.
