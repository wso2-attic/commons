1. Upload the TestGovernanceLC.jrxml to /_system/governance/repository/components/org.wso2.carbon.governance/templates

2. Upload TestGovernanceCycle.rxt to any location you want

3. Do required changes to the TestingLCReportGenerator.java as required and create a jar out of it (already created jar available -  TestingLCReportGenerator.jar). Then upload this jar through Extensions menu of Management Console.

4. Logout from Management Console and login again

5. Create a new report and provide required information
Report Name - [any name]
Template - [/_system/governance/repository/components/org.wso2.carbon.governance/templates/TestGovernanceLC.jrxml]
Report Type - [Any type]
Report Class - org.wso2.carbon.registry.samples.reporting.TestingLCReportGenerator
bar - [any value]
foo - [any value]

6. Add data to the newly added Test Governance Cycle configurable artifact (which will appread under Metadata)

7. Generate the report and you will get a report with data
