Enhanced RXT functionalities
=============================

The given artifact will cover,

1. Date field - calendar
2. Validate the text field using regular expression
3. Tool tip feature
4. Add unbounded field
5. required Attribute
6. combo-box 
7. text fields
8. table element
9. Read only


 


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
			   <field type="text" tooltip="Comments go here"> <name>Comments</name></field>
			   
			<field type="date">
     <name>From Date</name>
    </field>
    <field type="options">
     <name>Type</name>
     <values>
      <value>type1</value>
      <value>type2</value>
     </values>
    </field>
   <field type="text" validate="^\d+$">
     <name>Elements</name>
    </field>
 
    <field type="text" readonly="true">
     <name>Accept Terms</name>
    </field>
    
		</table>
		
		<table name="EndPoints">
<subheading> <heading>Environments</heading> <heading>URL</heading> </subheading>
<field type="option-text" maxoccurs="unbounded"> <name>EndPoint</name> <values> <value>Unknown</value> <value>Dev</value> <value>QA</value> <value>Test</value> </values> </field>
</table>
		
		
	</content>
</artifactType>

