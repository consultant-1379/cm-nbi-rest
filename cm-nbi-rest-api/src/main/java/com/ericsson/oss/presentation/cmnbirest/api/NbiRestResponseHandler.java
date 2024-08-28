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

package com.ericsson.oss.presentation.cmnbirest.api;

import com.ericsson.oss.itpf.sdk.core.annotation.EService;

import javax.ejb.Remote;

/**
 * Created by enmadmin on 9/24/21.
 */

@Remote
@EService
public interface NbiRestResponseHandler {
    NbiResponse retrieveOperationResponse(final String requestId, final int waitInterval);
}
