/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2021
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.presentation.cmnbirest.resources.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by enmadmin on 11/5/21.
 */

@Provider
@PreMatching
public class JsonPatchEnableFilter implements ContainerRequestFilter
{

    private final Logger logger = LoggerFactory.getLogger(JsonPatchEnableFilter.class);

    @Override
    public void filter( ContainerRequestContext ctx ) throws IOException
    {
        logger.debug("JsonPatchEnableFilter:: START ctx={}", ctx);
        if ( ctx.getMethod().equals( HttpMethod.PATCH ))
        {
            logger.debug("JsonPatchEnableFilter FORCE TO USE MYJSONPATCH");
            ctx.setMethod( "MYJSONPATCH" );
        }
        logger.debug("JsonPatchEnableFilter:: STOP");
    }
}