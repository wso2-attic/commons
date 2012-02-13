Name:  	ServiceLifeCycle
State: 	Development 
=========================
With the below configuration, it will allow you to Promote the service and it's dependancies to the next stage only if first and the second check boxes are ticked and if the third check box is not.
 
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


State: 	Testing 
================
With the below configuration, it will allow you to Promote the service and it's dependancies to the next stage only if second check box is ticked and if the first and third check boxes are not.

<data name="transitionValidation">
    <validation forEvent="Promote" class="org.wso2.carbon.governance.registry.extensions.validators.CheckItemTickedValidator">
        <parameter name="itemIndex" value="1"/>
        <parameter name="checked" value="false"/>                                
        <parameter name="itemIndex" value="2"/>
        <parameter name="checked" value="true"/>                                
        <parameter name="itemIndex" value="3"/>
        <parameter name="checked" value="false"/>
    </validation>
</data>	
