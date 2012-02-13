/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <stdio.h>
#include <axiom.h>
#include <axis2_util.h>
#include <axiom_soap.h>
#include <axis2_client.h>
#include <rampart_constants.h>
#include <neethi_util.h>
#include <neethi_policy.h>

axiom_node_t *
build_om_payload_for_echo_svc(const axutil_env_t *env);


int main(int argc, char** argv)
{
    const axutil_env_t *env = NULL;
    const axis2_char_t *address = NULL;
    const axis2_char_t *client_home = NULL;
    axis2_char_t *policy_file_name = NULL;
    axis2_char_t *file_name2 = NULL;
    axis2_endpoint_ref_t* endpoint_ref = NULL;
    axis2_options_t *options = NULL;
    axis2_svc_client_t* svc_client = NULL;
    axiom_node_t *payload = NULL;
    axiom_node_t *ret_node = NULL;
    axis2_status_t status = AXIS2_FAILURE;
    neethi_policy_t *policy = NULL;
	
	/* Set up the environment */
    env = axutil_env_create_all("sec_echo.log", AXIS2_LOG_LEVEL_TRACE);

    /* Set end-point-reference of echo service */
    address = "http://localhost:9090/axis2/services/sec_echo";
    if (argc > 3)
    {
        address = argv[1];
        client_home = argv[2];
		policy_file_name = argv[3];
        printf("Using endpoint : %s\n", address);
        printf("Using client_home : %s\n", client_home);
    }

    if (axutil_strcmp(address, "-h") == 0)
    {
        printf("Usage : %s [endpoint_url] [client_home]\n", argv[0]);
        printf("use -h for help\n");
        return 0;
    }

    /* Create end-point-reference with given address */
    endpoint_ref = axis2_endpoint_ref_create(env, address);

    /* Setup options */
    options = axis2_options_create(env);
    axis2_options_set_to(options, env, endpoint_ref);
    axis2_options_set_action(options, env,
            "http://example.com/ws/2004/09/policy/Test/EchoRequest");
    /*axis2_options_set_action(options, env,
            "urn:echo");*/


    /*If the client home is not specified, use the WSFC_HOME*/
    if (!client_home)
    {
        client_home = AXIS2_GETENV("WSFC_HOME");
    }

    /* Create service client */
    printf("client_home= %s", client_home);
    svc_client = axis2_svc_client_create(env, client_home);
    if (!svc_client)
    {
        printf("Error creating service client\n");
        return -1;
    }

    /* Set service client options */
    axis2_svc_client_set_options(svc_client, env, options);

    axis2_svc_client_engage_module(svc_client, env, AXIS2_MODULE_ADDRESSING);
    axis2_svc_client_engage_module(svc_client, env, "rampart");

    policy = neethi_util_create_policy_from_file(env, policy_file_name);
   
    if(!policy)
    {
        printf("\nPolicy creation failed from the file. %s\n", policy_file_name);
        /*printf("echo client invoke FAILED!\n");
        return 0;*/
    }

    status = axis2_svc_client_set_policy(svc_client, env, policy);

    if(status == AXIS2_FAILURE)
    {
        printf("Policy setting failed\n");
    }
    
    /* Build the SOAP request message payload using OM API.*/
    payload = build_om_payload_for_echo_svc(env);
    
    /*If not engaged in the client's axis2.xml, uncomment this line*/
    /*axis2_svc_client_engage_module(svc_client, env, "rampart");*/
    
    /* Send request */
    ret_node = axis2_svc_client_send_receive(svc_client, env, payload);


    if (axis2_svc_client_get_last_response_has_fault(svc_client, env))
    {
        axiom_soap_envelope_t *soap_envelope = NULL;
        axiom_soap_body_t *soap_body = NULL;
        axiom_soap_fault_t *soap_fault = NULL;

        printf ("\nResponse has a SOAP fault\n");
        soap_envelope =
            axis2_svc_client_get_last_response_soap_envelope(svc_client, env);
        if (soap_envelope)
            soap_body = axiom_soap_envelope_get_body(soap_envelope, env);
        if (soap_body)
            soap_fault = axiom_soap_body_get_fault(soap_body, env);
        if (soap_fault)
        {
            printf("\nReturned SOAP fault: %s\n",
            axiom_node_to_string(axiom_soap_fault_get_base_node(soap_fault,env),
                env));
        }
            printf("echo client invoke FAILED!\n");
            return -1;
    }
    
    if (ret_node)
    {
        axis2_char_t *om_str = NULL;
        om_str = axiom_node_to_string(ret_node, env);
        if (om_str)
        {
            printf("\nReceived OM : %s\n", om_str);
        }
        printf("\necho client invoke SUCCESSFUL!\n");
        AXIS2_FREE(env->allocator, om_str);
        ret_node = NULL;
    }
    else
    {
        printf("echo client invoke FAILED!\n");
        return -1;
    }

    if (svc_client)
    {
        axis2_svc_client_free(svc_client, env);
        svc_client = NULL;
    }
    if (env)
    {
        axutil_env_free((axutil_env_t *) env);
        env = NULL;
    }
    
    return 0;
}

/* build SOAP request message content using OM */
axiom_node_t *
build_om_payload_for_echo_svc(const axutil_env_t *env)
{
    axiom_node_t *echo_om_node = NULL;
    axiom_element_t* echo_om_ele = NULL;
    axiom_node_t* text_om_node = NULL;
    axiom_element_t * text_om_ele = NULL;
    axiom_namespace_t *ns1 = NULL;
    axis2_char_t *om_str = NULL;

    ns1 = axiom_namespace_create(env, "http://ws.apache.org/rampart/c/samples", "ns1");
    echo_om_ele = axiom_element_create(env, NULL, "echoIn", ns1, &echo_om_node);
    
    
    text_om_ele = axiom_element_create(env, echo_om_node, "text", NULL, &text_om_node);
    axiom_element_set_text(text_om_ele, env, "Hello", text_om_node);

    om_str = axiom_node_to_string(echo_om_node, env);
    if (om_str){
        printf("\nSending OM : %s\n", om_str);
        AXIS2_FREE(env->allocator, om_str);
        om_str =  NULL;
    }
    return echo_om_node;
}
