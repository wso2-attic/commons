LC in RXT Definition
====================

1. Create a new life cycle.

<aspect name="SampleLifeCycle2" class="org.wso2.carbon.governance.registry.extensions.aspects.DefaultLifeCycle">
    <configuration type="literal">
        <lifecycle>
            <scxml xmlns="http://www.w3.org/2005/07/scxml"
                   version="1.0"
                   initialstate="Development">
                <state id="Development">
                    <datamodel>
                        <data name="checkItems">
                            <item name="Code Completed" forEvent="">
                                <!--<permissions>
                                    <permission roles=""/>
                                </permissions>
                                <validations>
                                    <validation forEvent="" class="">
                                        <parameter name="" value=""/>
                                    </validation>
                                </validations>-->
                            </item>
                            <item name="WSDL, Schema Created" forEvent="">
                            </item>
                            <item name="QoS Created" forEvent="">
                            </item>
                        </data>
                        <!--<data name="transitionValidation">
                            <validation forEvent="" class="">
                                <parameter name="" value=""/>
                            </validation>
                        </data>
                        <data name="transitionPermission">
                            <permission forEvent="" roles=""/>
                        </data>
                        <data name="transitionScripts">
                            <js forEvent="">
                                <console function="">
                                    <script type="text/javascript">
                                    </script>
                                </console>
                                <server function="">
                                    <script type="text/javascript"></script>
                                </server>
                            </js>
                        </data>
                        <data name="transitionApproval">
                            <approval forEvent="Promote" roles="" votes="2"/>
                        </data>-->
                    </datamodel>
                    <transition event="Promote" target="Tested"/>                  
                </state>
                <state id="Tested">
                    <datamodel>
                        <data name="checkItems">
                            <item name="Effective Inspection Completed" forEvent="">
                            </item>
                            <item name="Test Cases Passed" forEvent="">
                            </item>
                            <item name="Smoke Test Passed" forEvent="">
                            </item>
                        </data>
                    </datamodel>
                    <transition event="Promote" target="Production"/>
                    <transition event="Demote" target="Development"/>
                </state>
                <state id="Production">  
                    <transition event="Demote" target="Tested"/>
                </state>                
            </scxml>
        </lifecycle>
    </configuration>
</aspect>



2. Create a new rxt (artifact) providing the created life cycle as the deafult life cycle by providing the tag,

<lifecycle>SampleLifeCycle2</lifecycle>

--------------------------------------------


<?xml version="1.0"?>
<artifactType type="application/vnd.wso2-myNewRXT+xml" shortName="myNewRXT" singularLabel="My New RXT" pluralLabel="My New RXT" hasNamespace="false" iconSet="25">
	<storagePath>/test_suites/@{name}/@{overview_version}</storagePath>
	<nameAttribute>overview_name</nameAttribute>
	<lifecycle>SampleLifeCycle2</lifecycle>
	<ui>
		<list>
			<column name="Name">
				<data type="path" value="overview_name" href="@{storagePath}"/>
			</column>
		</list>
	</ui>
	
	<content>
		<table name="Overview">
			<field type="text" required="true">
				<name label="Name">Name</name>
			</field>
			<field type="text" required="true">                
				<name>Version</name>
			</field> 
			
			<field type="text-area">
				<name label="Description">Test Suite Description</name>
			</field>
		</table>
		
		
	</content>
</artifactType>

3. Once you create the new RXT it should be available in the meta data list as My New RXt.
4. Now add a My New RXT.
5. You should be able to see the life cycle SampleLifeCycle2 is assigned by deafult and the state of it as Development.

Name: 	SampleLifeCycle2
State: 	Development

