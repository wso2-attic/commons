/*
 * Copyright 2005-2008 WSO2, Inc. http://wso2.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <string.h>

#include <libxml/xmlmemory.h>
#include <libxml/debugXML.h>
#include <libxml/HTMLtree.h>
#include <libxml/xmlIO.h>
#include <libxml/xinclude.h>
#include <libxml/catalog.h>
#include <libxslt/xslt.h>
#include <libxslt/xsltInternals.h>
#include <libxslt/transform.h>
#include <libxslt/xsltutils.h>

#include "wsf_wsdl_mode.h"

extern int xmlLoadExtDtdDefaultValue;

axis2_status_t WSF_WSDL_CALL 
wsf_wsdl_util_apply_xslt(
    const axutil_env_t* env, 
    axis2_char_t* source_wsdl_file, 
    axis2_char_t* xslt_location,
    axis2_bool_t is_service_call,
    axis2_bool_t* is_version1_wsdl,
    axiom_node_t** wsdl_axiom,
    axiom_node_t** sig_axiom)
{
    axis2_status_t status = AXIS2_FAILURE;
    axiom_node_t* wsdl_node = NULL;
    axiom_node_t* sig_node = NULL;
    axis2_char_t* wsdl_1to2_xslt_file = NULL;
    axis2_char_t* wsdl_2tosig_xslt_file = NULL;
    axis2_char_t* wsdl_intermediate_file = NULL;
    axis2_char_t* sig_file = NULL;

    wsdl_node = wsf_wsdl_util_deserialize_file(env, source_wsdl_file);

    if (wsdl_node)
    {
        if (wsf_wsdl_parser_determine_wsdl_version(env, wsdl_node, is_version1_wsdl))
        {
            wsf_wsdl_xslt_create_long_file_names(env, 
                                                 xslt_location,
                                                 is_service_call,
                                                 &wsdl_1to2_xslt_file,
                                                 &wsdl_2tosig_xslt_file,
                                                 &wsdl_intermediate_file,
                                                 &sig_file);
            if (*is_version1_wsdl)
            {
                /* version 1 wsdl */
                status = wsf_wsdl_xslt_apply(env,
                                             source_wsdl_file,
                                             wsdl_intermediate_file,
                                             wsdl_1to2_xslt_file);

                if (status)
                {
                    status = wsf_wsdl_xslt_apply(env,
                                                 wsdl_intermediate_file,
                                                 sig_file,
                                                 wsdl_2tosig_xslt_file);
                }
            }
            else
            {
                /* version 2 wsdl */
                status = wsf_wsdl_xslt_apply(env,
                                             source_wsdl_file,
                                             sig_file,
                                             wsdl_2tosig_xslt_file);
            }

            if (status)
            {
                sig_node = wsf_wsdl_util_deserialize_file(env, sig_file);
            }

            if (wsdl_1to2_xslt_file)
            {
                AXIS2_FREE(env->allocator, wsdl_1to2_xslt_file);
            }
            if (wsdl_2tosig_xslt_file)
            {
                AXIS2_FREE(env->allocator, wsdl_2tosig_xslt_file);
            }
            if (wsdl_intermediate_file)
            {
                AXIS2_FREE(env->allocator, wsdl_intermediate_file);
            }
            if (sig_file)
            {
                AXIS2_FREE(env->allocator, sig_file);
            }

            *wsdl_axiom = wsdl_node;
            *sig_axiom = sig_node;
        }
    }

    return status;
}


/**
* creates the absolute filenames. caller should free the allocated memory.
* @param env, environment structure
* @param xslt_file_location, 
* @param is_service_call, flag to say whether this is for service or client calls
* @param wsdl_1to2_xslt_file, 
* @param wsdl_2tosig_xslt_file, 
* @param intermediate_wsdl_file, 
* @param resultant_sig_file, 
* @returns AXIS2_SUCCESS on success, AXIS2_FAILURE otherwise.
*/
axis2_status_t
wsf_wsdl_xslt_create_long_file_names(
    const axutil_env_t* env,
    axis2_char_t* xslt_file_location,
    axis2_bool_t is_service_call,
    axis2_char_t** wsdl_1to2_xslt_file,
    axis2_char_t** wsdl_2tosig_xslt_file,
    axis2_char_t** intermediate_wsdl_file,
    axis2_char_t** resultant_sig_file)
{
    axis2_status_t status = AXIS2_FAILURE;
    is_service_call = AXIS2_TRUE; /* getting rid of warning. TODO: use this flag */
    
    *wsdl_2tosig_xslt_file = axutil_strcat(env, 
                                           xslt_file_location, 
                                           "/", 
                                           WSF_WSDL_XSLT_WSDL_2_TO_SIG, 
                                           NULL);
    *wsdl_1to2_xslt_file = axutil_strcat(env, 
                                         xslt_file_location, 
                                         "/", 
                                         WSF_WSDL_XSLT_WSDL_1_TO_2, 
                                         NULL);
    *intermediate_wsdl_file = axutil_strcat(env, 
                                            xslt_file_location, 
                                            "/", 
                                            WSF_WSDL_XSLT_WSDL_INTERMEDIATE, 
                                            NULL);
    *resultant_sig_file = axutil_strcat(env, 
                                        xslt_file_location, 
                                        "/", 
                                        WSF_WSDL_XSLT_SIG_FINAL, 
                                        NULL);
    if ((*wsdl_1to2_xslt_file) 
        && (*wsdl_2tosig_xslt_file) 
        && (*intermediate_wsdl_file) 
        && (*resultant_sig_file))
    {
        status = AXIS2_SUCCESS;
    }

    return status;
}

axis2_status_t
wsf_wsdl_xslt_apply(
    const axutil_env_t* env,
    const axis2_char_t* source_xml_file,
    axis2_char_t* target_xml_file,
    axis2_char_t* xslt_file)
{
    axis2_status_t status = AXIS2_FAILURE;
    xsltStylesheetPtr cur = NULL;	
    xmlDocPtr doc = NULL;
    xmlDocPtr res = NULL;				
    int save_result = -1;
    
    if (!env)
    {
        return AXIS2_FAILURE;
    }

    xmlSubstituteEntitiesDefault(1);
    xmlLoadExtDtdDefaultValue = 1;

    doc = xmlParseFile(source_xml_file);
    cur = xsltParseStylesheetFile((const xmlChar *)xslt_file);
    res = xsltApplyStylesheet(cur, doc, NULL);
    
    save_result = xsltSaveResultToFilename(target_xml_file, res, cur, 0);
    
    if (cur)
    {
        xsltFreeStylesheet(cur);
    }
    
    if (res)
    {
        xmlFreeDoc(res);
    }

    if (doc)
    {
        xmlFreeDoc(doc);
    }

    xsltCleanupGlobals();
    xmlCleanupParser();

    if (save_result != -1)
    {
        status = AXIS2_SUCCESS;
    }
    
    return status;
}