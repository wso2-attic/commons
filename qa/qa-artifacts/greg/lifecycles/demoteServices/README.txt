The DemoteServiceLifeCycle.xml contains a configuration where it allows us to demote the currect lifecycle to another environment. The default ServiceVersionExecutor was used for this. Did only the following changes to the LC to support the demote operation.

//This will allow you to specify the version text boxes
<data name="transitionUI">
                            <ui forEvent="Demote" href="../lifecycles/pre_invoke_aspect_ajaxprocessor.jsp?preserveOriginal=true%26viewVersion=true%26showDependencies=true%26currentEnvironment=/_system/governance/branches/production/"/>
                        </data>


// This will take you to the Service List once demoted

                        <data name="transitionScripts">
                            <js forEvent="Demote">
                                <console function="showServiceList">
<script type="text/javascript">
                                        showServiceList = function() { var element = document.getElementById('hidden_media_type'); var mediaType = ""; if (element) { mediaType = element.value;} if (mediaType == "application/vnd.wso2-service+xml") { location.href = unescape("../generic/list.jsp?region=region3%26item=governance_list_service_menu%26key=service%26breadcrumb=Services%26singularLabel=Service%26pluralLabel=Services"); } }
</script>
                                </console>
                            </js>
                        </data>                        
